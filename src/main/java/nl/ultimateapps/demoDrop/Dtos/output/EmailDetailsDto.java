package nl.ultimateapps.demoDrop.Dtos.output;

import lombok.*;

@Data // MOET DIT HIER???
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetailsDto {

    @Getter
    @Setter
    private String recipient;

    @Getter
    @Setter
    private String msgBody;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String attachment;

}
