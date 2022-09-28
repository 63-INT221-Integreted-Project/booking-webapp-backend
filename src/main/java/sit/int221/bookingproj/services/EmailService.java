package sit.int221.bookingproj.services;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sit.int221.bookingproj.dtos.EventCreateDto;

import javax.persistence.JoinColumn;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService
{
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SimpleMailMessage preConfiguredMessage;

    public void sendMail(String to, String subject, String body)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("oasipsitkmuttkp2@outlook.com");
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendPreConfiguredMail(String message)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

    public String createBody(EventCreateDto eventCreateDto){
        ZonedDateTime thTime = eventCreateDto.getEventStartTime().atZone(ZoneId.of("Asia/Bangkok"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss");
        String thTimeFormatted = thTime.format(formatter);
        String name = "ชื่อการจอง: " + eventCreateDto.getBookingName();
        String email = "อีเมลที่จอง: " + eventCreateDto.getBookingEmail();
        String notes = "โน๊ต: " + eventCreateDto.getEventNotes();
        String eventStartTime = "เวลาที่เริ่มต้น " + thTimeFormatted;
        String duration = "ระยะเวลา: " + eventCreateDto.getEventDuration() + " นาที";
        return name + "\n" + email + "\n" + notes + "\n" + eventStartTime + "\n" + duration;
    }
}