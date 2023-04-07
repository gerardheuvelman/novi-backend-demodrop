package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserMapper;
import nl.ultimateapps.demoDrop.Models.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserPrivateDto {

    @NotBlank
    @Size(min=3, max=30)
    @Getter
    @Setter
    private String username;

    @NotBlank
    @Size(min=4, max=30)
    @Getter
    @Setter
    private String password;

    @NotNull
    @Getter
    @Setter
    private boolean enabled;

    @Email
    @Getter
    @Setter
    private String email;

    @NotNull
    @Getter
    @Setter
    private Date createdDate;


    //Relationships:
    @JsonIncludeProperties({"authority"})
    @Getter
    @Setter
    private Set<AuthorityDto> authorities;

    @JsonIncludeProperties({"title"})
    @Getter
    @Setter
    private List<DemoDto> demos;

    @JsonIgnore
    @Getter
    @Setter
    private List<ConversationDto> conversationsAsInitiator;

    @JsonIgnore
    @Getter
    @Setter
    private List<ConversationDto> conversationsAsCorrespondent;

    @JsonIgnore
    @Getter
    @Setter
    private List<UserReportDto> userReportsAsReporter;

    @JsonIgnore
    @Getter
    @Setter
    private List<UserReportDto> userReportsAsReportedUser;

    @JsonIgnore
    @Getter
    @Setter
    private List<DemoDto> favoriteDemos;

    public User toModel() {
        return UserMapper.mapToModel(this);
    }

    public UserPublicDto toUserPublicDto() {
        UserPublicDto userPublicDto = new UserPublicDto();
        userPublicDto.setUsername(this.getUsername());
        userPublicDto.setCreatedDate(this.getCreatedDate());
        userPublicDto.setDemos(this.getDemos());
        return userPublicDto;
    }
}