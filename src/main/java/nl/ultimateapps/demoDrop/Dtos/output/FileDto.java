package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

public class FileDto {

    @Getter
    @Setter
    private String fileName;

    @Getter
    @Setter
    private String contentType;

    @Getter
    @Setter
    private String url;
}

