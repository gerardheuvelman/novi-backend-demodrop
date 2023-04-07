package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Helpers.mappers.DemoMapper;
import nl.ultimateapps.demoDrop.Models.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DemoDto {

    @Getter
    @Setter
    private Long demoId;

    @NotBlank
    @Getter
    @Setter
    private Date createdDate;

    @NotBlank
    @Size(min=2 , max =30)
    @Getter
    @Setter
    private String title;

    @NotNull
    @Min(10)
    @Max(10000)
    @Getter
    @Setter
    private Double length;

    @NotNull
    @Min(10)
    @Max(1000)
    @Getter
    @Setter
    private Double bpm;

    //Relationships:
    @JsonIncludeProperties({"audioFileId", "originalFileName"})
    @Getter
    @Setter
    private AudioFileDto audioFile;

    @NotNull
    @JsonIncludeProperties({"name"})
    @Getter
    @Setter
    private GenreDto genre;

    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private UserPublicDto producer;

    @JsonIgnore
    @Getter
    @Setter
    private List<ConversationDto> conversations;

    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private List<UserPublicDto> favoriteOfUsers;

    @JsonIgnore
    @Getter
    @Setter
    private List<UserReportDto> userReports;

    public Demo toModel() {
        return DemoMapper.mapToModel(this);
    }
}