package nl.ultimateapps.demoDrop.Models;

import lombok.*;
import nl.ultimateapps.demoDrop.Dtos.output.UserReportDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserReportMapper;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserReport {

    @Id
    @GenericGenerator(
            name = "reports-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "reports_sequence"),
                    @Parameter(name = "initial_value", value = "4017"),
                    @Parameter(name = "increment_size", value = "1")
            })
    @GeneratedValue(generator = "reports-sequence-generator")
    @Getter
    @Setter
    private Long userReportId;

    @Getter
    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate;

    @Getter
    @Setter
    private String type; // "user", "demo" or "conversation"

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String body;


    //Relationships:

    @ManyToOne
    @JoinColumn(name = "reporter_name")
    @Getter
    @Setter
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_demo_id")
    @Getter
    @Setter
    private Demo reportedDemo;

    @ManyToOne
    @JoinColumn(name = "reported_conversation_id")
    @Getter
    @Setter
    private Conversation reportedConversation;

    @ManyToOne
    @JoinColumn(name = "reported_user_name")
    @Getter
    @Setter
    private User reportedUser;

    public UserReportDto toDto() {
        return UserReportMapper.mapToDto(this);
    }
}


