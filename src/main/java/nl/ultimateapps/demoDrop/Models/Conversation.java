package nl.ultimateapps.demoDrop.Models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import lombok.*;
import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.ConversationMapper;
import nl.ultimateapps.demoDrop.Helpers.mappers.DemoMapper;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Conversation {

    @Id
    @GenericGenerator(
            name = "conversations-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "conversations_sequence"),
                    @Parameter(name = "initial_value", value = "2017"),
                    @Parameter(name = "increment_size", value = "1")
            })
    @GeneratedValue(generator = "conversations-sequence-generator")
    @Getter
    @Setter
    private Long ConversationId;

    @Getter
    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate;

    @Getter
    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date latestReplyDate;

    @Getter
    @Setter
    private Boolean hasDemo;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String body;

    @Getter
    @Setter
    private boolean readByInitiator;

    @Getter
    @Setter
    private boolean readByCorrespondent;

    //Relationships:

    @ManyToOne
    @JoinColumn(name = "initiator")
    @Getter
    @Setter
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "correspondent")
    @Getter
    @Setter
    private User correspondent;

    @ManyToOne
    @JoinColumn(name = "Demo_id")  //optioneel. Dit is sowieso de default naam
    @Getter
    @Setter
    private Demo demo;

    @OneToMany(mappedBy = "reportedConversation", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<UserReport> userReports;

    public ConversationDto toDto() {
        return ConversationMapper.mapToDto(this);
    }
}
