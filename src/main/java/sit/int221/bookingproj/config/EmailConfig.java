package sit.int221.bookingproj.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfig
{
    @Bean
    public SimpleMailMessage emailTemplate()
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("oasipsitkmuttkp2@outlook.com");
        message.setFrom("oasipsitkmuttkp2@outlook.com");
        message.setSubject("Your Booking' details at OASIP");
        message.setText("FATAL - Application crash. Save your job !!");
        return message;
    }
}