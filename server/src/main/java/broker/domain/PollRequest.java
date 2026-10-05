package broker.domain;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class PollRequest implements Serializable {

    private PollResult poll;
    private String motivation;
    private String name;

}
