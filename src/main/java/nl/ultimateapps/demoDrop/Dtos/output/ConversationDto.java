package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Helpers.mappers.ConversationMapper;
import nl.ultimateapps.demoDrop.Models.Conversation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ConversationDto {

    @Getter
    @Setter
    private Long ConversationId;

    @NotNull
    @Getter
    @Setter
    private Date CreatedDate;

    @NotNull
    @Getter
    @Setter
    private Date latestReplyDate;

    @NotNull
    @Getter
    @Setter
    private boolean hasDemo;


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

    @NotNull
    @Getter
    @Setter
    private boolean readByInitiator;

    @NotNull
    @Getter
    @Setter
    private boolean readByCorrespondent;

    //Relationships:

    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private UserPublicDto initiator;

    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private UserPublicDto correspondent;

    @JsonIncludeProperties({"demoId", "title"})
    @Getter
    @Setter
    private DemoDto demo;

    @JsonIgnore
    @Getter
    @Setter
    private List<UserReportDto> userReports;

    public Conversation toModel() {
        return ConversationMapper.mapToModel(this);
    }
}
