package opensuite.crm.notifications.domain.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import opensuite.crm.notifications.domain.SmtpConfig;
import opensuite.crm.notifications.domain.repository.SMTPConfigRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class SMTPConfigServiceImpl implements SMTPConfigService  {

    private final SMTPConfigRepository smtpConfigRepository;

    @Override
    public Optional<SmtpConfig> getSMTPConfig() {
        return smtpConfigRepository.findSMTPConfig();
    }

    @Override
    public Boolean updateSMTPConfig(SmtpConfig smtpConfig) {
        return smtpConfigRepository.updateConfig(smtpConfig);
    }

    public String sendOfferByEmail(String to, String subject, String idOffer, byte[] fileContent, String filename) {
        try {
            JavaMailSenderImpl mailSender = configMail();
            JavaMailSender javaMailSender = mailSender;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/sendOffer.html");
            String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlTemplate.replace("{{idOffer}}", idOffer), true);
            helper.addAttachment(filename, new ByteArrayResource(fileContent));

            javaMailSender.send(message);
            return "";
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String sendLinkRestorePasswordUser(String to, String subject, String urlResetPassword) {
        try {
            JavaMailSenderImpl mailSender = configMail();
            JavaMailSender javaMailSender = mailSender;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/startResetPassword.html");
            String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlTemplate.replace("{{url}}", urlResetPassword), true);

            javaMailSender.send(message);
            return "";
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String confirmRestorePasswordUser(String to, String subject) {
        try {
            JavaMailSenderImpl mailSender = configMail();
            JavaMailSender javaMailSender = mailSender;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/confirmResetPassword.html");
            String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlTemplate, true);

            javaMailSender.send(message);
            return "";
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String testMail(String to, String subject) {
        try {
            JavaMailSenderImpl mailSender = configMail();
            JavaMailSender javaMailSender = mailSender;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/testMail.html");
            String htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlTemplate, true);

            javaMailSender.send(message);
            return "";
        } catch (Exception e) {
            return "Traza: " + e;
        }
    }

    public JavaMailSenderImpl configMail(){
        SmtpConfig smtpConfig = smtpConfigRepository.findSMTPConfig().get();
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(smtpConfig.getHost());
        mailSenderImpl.setProtocol(smtpConfig.getProtocol());
        mailSenderImpl.setUsername(smtpConfig.getUsername());
        mailSenderImpl.setPassword(smtpConfig.getPassword());

        mailSenderImpl.getJavaMailProperties().put("mail.smtp.starttls.enable", smtpConfig.getUseSSL());
        mailSenderImpl.getJavaMailProperties().put("mail.smtp.auth", smtpConfig.getAuth());
        mailSenderImpl.getJavaMailProperties().put("mail.smtp.socketFactory.port", smtpConfig.getPort());
        return mailSenderImpl;
    }

}
