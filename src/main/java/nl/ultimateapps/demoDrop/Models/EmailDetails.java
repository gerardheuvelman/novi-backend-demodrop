package nl.ultimateapps.demoDrop.Models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {

    @Getter
    @Setter
    private String recipient;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String msgBody;

    @Getter
    @Setter
    private String attachment;
}
