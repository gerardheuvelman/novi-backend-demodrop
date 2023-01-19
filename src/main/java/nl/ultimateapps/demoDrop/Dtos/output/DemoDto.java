package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Models.Conversation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URL;
import java.util.Date;
import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class DemoDto {

    @NotNull
    @Size(min = 2, max = 120)
    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Date createdDate;

    @Getter
    @Setter
    private Double length;

    @Getter
    @Setter
    private String audiofileUrl;

    // Relationships
    //    @JsonIgnore // OPTIONEEL, maar wel geadviseerd om de datasize klein te houden. Let ook op dat je recursie vermijdt!!
    @JsonIncludeProperties({"id", "subject"})
    @Getter
    @Setter
    private List<Conversation> conversations;
}

