package nl.ultimateapps.demoDrop.Dtos.output;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EmailDetailsDto {

    @Getter
    @Setter
    private String recipientUsername;

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
