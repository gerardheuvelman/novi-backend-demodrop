package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Models.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class DemoDto {

    @Getter
    @Setter
    private Long DemoId;

    @Getter
    @Setter
    private Date createdDate;

//    @NotNull // MOET VALIDATIE HIER OF IN DE MODEL KLASSE? (ik denk hier in de DTO)
//    @Size(min = 2, max = 120)
    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Double length;

    @Getter
    @Setter
    private Double BPM;

    //Relationships:
    @JsonIncludeProperties({"fileName", "url"})
    @Getter
    @Setter
    private File file;

    @JsonIncludeProperties({"name"})
    @Getter
    @Setter
    private Genre genre;

    // Relationships:
    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private User user;

    @JsonIgnore
    @Getter
    @Setter
    private List<Conversation> conversations;

    @JsonIgnore
    @Getter
    @Setter
    private List<User> favoriteOfUsers;
}