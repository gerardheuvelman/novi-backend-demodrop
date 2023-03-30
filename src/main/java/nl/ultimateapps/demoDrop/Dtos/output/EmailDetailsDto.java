package nl.ultimateapps.demoDrop.Dtos.output;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EmailDetailsDto {

    @NotBlank
    @Size(min=3, max=30)
    @Getter
    @Setter
    private String recipientUsername;

    @NotBlank
    @Getter
    @Setter
    private String msgBody;

    @NotBlank
    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String attachment;

}
