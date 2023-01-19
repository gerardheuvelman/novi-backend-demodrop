package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Models.Demo;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {

    @Getter
    @Setter
    private Date CreatedDate;

    @Getter
    @Setter
    private Date latestReplyDate;

    @NotNull
    @Size(min = 2, max = 120)
    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String body;

    //Relationships
    @JsonIncludeProperties({"id", "title"})
    @Getter
    @Setter
    private Demo demo;
}
