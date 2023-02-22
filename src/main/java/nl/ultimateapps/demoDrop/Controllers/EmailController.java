package nl.ultimateapps.demoDrop.Controllers;


import lombok.AllArgsConstructor;
import nl.ultimateapps.demoDrop.Dtos.output.EmailDetailsDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.DemoMapper;
import nl.ultimateapps.demoDrop.Helpers.mappers.EmailDetailsMapper;
import nl.ultimateapps.demoDrop.Models.EmailDetails;
import nl.ultimateapps.demoDrop.Services.EmailService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/email")
public class EmailController {
    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendsimple")
    public String sendEmail(@RequestBody EmailDetailsDto emailDetailsDto) {
        EmailDetails emailDetails = EmailDetailsMapper.mapToModel(emailDetailsDto);
        String status= emailService.sendSimpleMail(emailDetails);
        return status;
    }

    @PostMapping("/sendwithattachment")
    public String sendEmailWithAttachment(@RequestBody EmailDetailsDto emailDetailsDto) {
        EmailDetails emailDetails = EmailDetailsMapper.mapToModel(emailDetailsDto);
        String status = emailService.sendMailWithAttachment(emailDetails);
        return status;
    }
}
