package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Models.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DemoDto {

    @Getter
    @Setter
    private Long demoId;

    @Getter
    @Setter
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
    @JsonIncludeProperties({"audioFileId", "originalFileName"})
    @Getter
    @Setter
    private AudioFile audioFile;

    @JsonIncludeProperties({"name"})
    @Getter
    @Setter
    private Genre genre;

    // Relationships:
    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private User user;

    @JsonIgnore
    @Getter
    @Setter
    private List<Conversation> conversations;

    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private List<User> favoriteOfUsers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemoDto demoDto = (DemoDto) o;
        return Objects.equals(demoId, demoDto.demoId) && Objects.equals(createdDate, demoDto.createdDate) && Objects.equals(title, demoDto.title) && Objects.equals(length, demoDto.length) && Objects.equals(bpm, demoDto.bpm) && Objects.equals(audioFile, demoDto.audioFile) && Objects.equals(genre, demoDto.genre) && Objects.equals(user, demoDto.user) && Objects.equals(conversations, demoDto.conversations) && Objects.equals(favoriteOfUsers, demoDto.favoriteOfUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(demoId, createdDate, title, length, bpm, audioFile, genre, user, conversations, favoriteOfUsers);
    }
}