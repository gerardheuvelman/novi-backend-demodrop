package nl.ultimateapps.demoDrop.Models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AudioFile {

    @Id
    @GenericGenerator(
            name = "files-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "files_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "3017"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            })
    @GeneratedValue(generator = "files-sequence-generator")
    @Getter
    @Setter
    private long audioFileId;

    @Getter
    @Setter
    private String originalFileName;

    // Relationships:
    // A one-to-one (mappedBy "file") Relationship met "Demo" is Implied!

}
