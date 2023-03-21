package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Models.Demo;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

import lombok.*;
import nl.ultimateapps.demoDrop.Models.User;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ConversationDto {

    @Getter
    @Setter
    private Long ConversationId;

    @Getter
    @Setter
    private Date CreatedDate;

    @Getter
    @Setter
    private Date latestReplyDate;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String body;

    @Getter
    @Setter
    private boolean readBbyProducer;

    @Getter
    @Setter
    private boolean readByInterestedUser;

    //Relationships:
    @JsonIncludeProperties({"demoId", "title"})
    @Getter
    @Setter
    private Demo demo;

    @JsonIncludeProperties({"username", "email"})
    @Getter
    @Setter
    private User producer;

    @JsonIncludeProperties({"username", "email"})
    @Getter
    @Setter
    private User interestedUser;
}
