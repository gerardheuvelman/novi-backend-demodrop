package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserMapper;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserReportMapper;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Models.UserReport;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserReportDto {

    @Getter
    @Setter
    private Long userReportId;

    @Getter
    @Setter
    private Date createdDate;

    @NotBlank
    @Getter
    @Setter
    private String type; // "user", "demo" or "conversation"

    @NotBlank
    @Size(min=2 , max =100)
    @Getter
    @Setter
    private String subject;

    @NotBlank
    @Size(min=2 , max =100000)
    @Getter
    @Setter
    private String body;


    //Relationships:
    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private UserPublicDto reporter;

    @JsonIncludeProperties({"demoId", "title"})
    @Getter
    @Setter
    private DemoDto reportedDemo;

    @JsonIncludeProperties({"conversationId", "subject"})
    @Getter
    @Setter
    private ConversationDto reportedConversation;

    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private UserPublicDto reportedUser;

    public UserReport toModel() {
        return UserReportMapper.mapToModel(this);
    }
}


