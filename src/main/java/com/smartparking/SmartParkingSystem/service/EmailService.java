package com.smartparking.SmartParkingSystem.service;



import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailService {
    private static String generatePdfReceipt(
        String vehicle,
        double fee,
        String entryTime,
        String exitTime) throws Exception {

    String pdfFile =
            "receipt_" + vehicle + ".pdf";

    Document document =
            new Document();

    PdfWriter.getInstance(
            document,
            new FileOutputStream(pdfFile)
    );

    document.open();

    document.add(
            new Paragraph("KS PARKING RECEIPT")
    );

    document.add(
            new Paragraph(" ")
    );

    document.add(
            new Paragraph(
                    "Vehicle Number : " + vehicle
            )
    );

    document.add(
            new Paragraph(
                    "Entry Time : " + entryTime
            )
    );

    document.add(
            new Paragraph(
                    "Exit Time : " + exitTime
            )
    );

    document.add(
            new Paragraph(
                    "Amount Paid : Rs. " + fee
            )
    );

    document.add(
            new Paragraph(
                    "Status : SUCCESS"
            )
    );

    document.close();

    return pdfFile;
}

    public static void sendEmailReceipt(
            String toEmail,
            String vehicle,
            double fee,
            String entryTime,
            String exitTime) {

        final String fromEmail = System.getenv("EMAIL");
        final String appPassword = System.getenv("EMAIL_PASSWORD");

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(
                props,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(fromEmail, appPassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(fromEmail));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            message.setSubject("KS_PARKING Receipt");

            // CURRENT DATE
            String currentDate =
                    LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            // HTML RECEIPT
            String htmlContent =

                    "<html>" +

                    "<body style='background-color:#f4f4f4; padding:20px; font-family:Arial;'>"

                    +

                    
                    "<div style='width:450px; margin:auto; background:white; border-radius:10px; padding:20px; border:1px solid #ccc;'>"
                    +

                    // LOGO
                    "<div style='text-align:center;'>"

                    +

                    "<img src='https://i.postimg.cc/R0YnM14J/KS.png' width='100' height='100'>"

                    +

                    "<h1 style='color:#222;'>KS_PARKING</h1>"

                    +

                    "<p style='color:green; font-size:20px;'><b>PAYMENT SUCCESS</b></p>"

                    +

                    "<hr>"

                    +

                    "</div>"

                    +

                    
                    "<table style='width:100%; font-size:16px; border-collapse:collapse;'>"

                    +

                    "<tr>"
                    +
                    "<td><b>Vehicle Number</b></td>"
                    +
                    "<td>" + vehicle + "</td>"
                    +
                    "</tr>"

                    +

                    "<tr>"
                    +
                    "<td><b>Date</b></td>"
                    +
                    "<td>" + currentDate + "</td>"
                    +
                    "</tr>"

                    +

                    "<tr>"
                    +
                    "<td><b>From Time</b></td>"
                    +
                    "<td>" + entryTime + "</td>"
                    +
                    "</tr>"

                    +

                    "<tr>"
                    +
                    "<td><b>To Time</b></td>"
                    +
                    "<td>" + exitTime + "</td>"
                    +
                    "</tr>"

                    +

                    "<tr>"
                    +
                    "<td><b>Amount Paid</b></td>"
                    +
                    
                    "<td>Rs. " + fee + "</td>"
                    +
                    "</tr>"

                    +

                    "<tr>"
                    +
                    "<td><b>Status</b></td>"
                    +
                    "<td style='color:green;'><b>SUCCESS</b></td>"
                    +
                    "</tr>"

                    +

                    "</table>"

                    +

                    "<hr>"

                    +

                    
                    "<p style='text-align:center; margin-top:20px; font-size:14px;'>Thank you for using KS_PARKING</p>"

                    +

                    "</div>"

                    +

                    "</body>"

                    +

                    "</html>";

            // SEND HTML
            //message.setContent(htmlContent, "text/html");
            String pdfFile =
        generatePdfReceipt(
                vehicle,
                fee,
                entryTime,
                exitTime
        );

MimeBodyPart htmlPart =
        new MimeBodyPart();

htmlPart.setContent(
        htmlContent,
        "text/html"
);

MimeBodyPart attachmentPart =
        new MimeBodyPart();

attachmentPart.attachFile(
        new File(pdfFile)
);

Multipart multipart =
        new MimeMultipart();

multipart.addBodyPart(htmlPart);

multipart.addBodyPart(attachmentPart);

message.setContent(multipart);
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (Exception e) {

            System.out.println("Failed to send email");
            e.printStackTrace();
        }
    }
}