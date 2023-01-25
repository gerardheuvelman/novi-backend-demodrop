package nl.ultimateapps.demoDrop.Models;

import lombok.*;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @Getter
    @Setter
    private String fileName;

    @Getter
    @Setter
    private String contentType;

    @Getter
    @Setter
    private String url;

    // Relationships:
    // A one-to-one (mappedBy "file") Relationship met "Demo" is Implied!
}
