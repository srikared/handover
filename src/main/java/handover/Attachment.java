package handover;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attachment {
    String text;

    public Attachment(String text){
        this.text = text;
    }
}
