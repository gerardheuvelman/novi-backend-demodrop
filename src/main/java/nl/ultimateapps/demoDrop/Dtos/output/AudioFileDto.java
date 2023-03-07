package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.Setter;
import nl.ultimateapps.demoDrop.Models.Demo;

import javax.persistence.OneToOne;
import java.util.Date;

public class AudioFileDto {

    @Getter
    @Setter
    private long audioFileId;

    @Getter
    @Setter
    private Date createdDate;

    @Getter
    @Setter
    private String originalFileName;

    @JsonIncludeProperties({"demoId", "title"})
    @Getter
    @Setter
    private Demo demo;
}