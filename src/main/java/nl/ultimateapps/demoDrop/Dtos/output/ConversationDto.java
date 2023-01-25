package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Models.Demo;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import lombok.*;
import nl.ultimateapps.demoDrop.Models.User;

@AllArgsConstructor
@NoArgsConstructor
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

    // LEt OP deze annotaties staan uit (volges mij alleen nodig bij entiteiten)
    //    @NotNull
    //    @Size(min = 2, max = 120)
    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String body;

    //Relationships:
    @Getter
    @Setter
    private DemoDto demoDto;

    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private UserDto interestedUser;
}
