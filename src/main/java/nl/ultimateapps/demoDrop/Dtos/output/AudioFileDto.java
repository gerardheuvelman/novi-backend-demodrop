package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;
import nl.ultimateapps.demoDrop.Models.Demo;

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

    @NotNull
    @JsonIncludeProperties({"demoId", "title"})
    @Getter
    @Setter
    private Demo demo;
}