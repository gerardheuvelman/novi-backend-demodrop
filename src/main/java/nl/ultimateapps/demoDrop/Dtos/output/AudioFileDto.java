package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;
import nl.ultimateapps.demoDrop.Helpers.mappers.AudioFileMapper;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserMapper;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AudioFileDto {

    @Getter
    @Setter
    private long audioFileId;

    @NotNull
    @Getter
    @Setter
    private Date createdDate;

    @NotBlank
    @Getter
    @Setter
    private String originalFileName;

    // Relationships:

    @NotNull
    @JsonIncludeProperties({"demoId", "title"})
    @Getter
    @Setter
    private DemoDto demo;

    public AudioFile toModel() {
        return AudioFileMapper.mapToModel(this);
    }
}