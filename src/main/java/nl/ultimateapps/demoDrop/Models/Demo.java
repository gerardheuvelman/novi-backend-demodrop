package nl.ultimateapps.demoDrop.Models;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import lombok.*;
import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.DemoMapper;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Demo {

    @Id
    @GenericGenerator(
            name = "demos-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "demos_sequence"),
                    @Parameter(name = "initial_value", value = "1017"),
                    @Parameter(name = "increment_size", value = "1")
            })
    @GeneratedValue(generator = "demos-sequence-generator")
    @Getter
    @Setter
    private Long demoId;

    @Getter
    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Double length;

    @Getter
    @Setter
    private Double bpm;

    //Relationships:
    @OneToOne
    @JoinColumn(name = "audio_file")
    @Getter
    @Setter
    private AudioFile audioFile;

    @ManyToOne
    @JoinColumn(name = "genre")
    @Getter
    @Setter
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "producer")
    @Getter
    @Setter
    private User producer;

    @OneToMany(mappedBy = "demo", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<Conversation> conversations;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "demo_id"), inverseJoinColumns = @JoinColumn(name = "user_id"), name = "demos_users_favorites_")
    @Getter
    @Setter
    private List<User> favoriteOfUsers;

    @OneToMany(mappedBy = "reportedDemo", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<UserReport> userReports;

    public DemoDto toDto() {
        return DemoMapper.mapToDto(this);
    }
}