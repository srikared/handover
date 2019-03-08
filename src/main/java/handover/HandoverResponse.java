package handover;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class HandoverResponse {
    String response_type;
    String text;
    ArrayList<Attachment>  attachments = new ArrayList<Attachment>();

    public HandoverResponse(String response_type, String text, String attachment) {
        this.response_type = response_type;
        this.text = text;
        attachments.add(new Attachment(attachment));
    }
}
