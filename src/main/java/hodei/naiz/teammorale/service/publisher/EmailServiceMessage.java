package hodei.naiz.teammorale.service.publisher;

import lombok.Builder;
import lombok.Data;

/**
 * Created by Hodei Eceiza
 * Date: 1/20/2022
 * Time: 10:07
 * Project: TeamMorale
 * Copyright: MIT
 */
@Data

public class EmailServiceMessage {
    private String teamName;
    private String to;
    private String message;
    private String username;
    private EmailType emailType;
    private String confirmationToken;


    @Builder(builderMethodName = "buildSignedUp",builderClassName = "BuildSignedUp")
    public static EmailServiceMessage signedUp(String to, String username, EmailType emailType, String confirmationToken, String message) {
        EmailServiceMessage emailService = new EmailServiceMessage();
        emailService.to = to;
        emailService.username = username;
        emailService.emailType = emailType;
        emailService.confirmationToken = confirmationToken;
        emailService.message = message;
        return emailService;

    }
    @Builder(builderMethodName = "buildAddedToTeam")
    public static EmailServiceMessage addedToTeam(String to, String username, EmailType emailType, String message,String teamName) {
        EmailServiceMessage emailService = new EmailServiceMessage();
        emailService.to = to;
        emailService.username = username;
        emailService.emailType = emailType;
        emailService.teamName = teamName;
        emailService.message = message;
        return emailService;

    }
}
