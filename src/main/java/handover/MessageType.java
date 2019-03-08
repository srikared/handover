package handover;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageType {
    String client_msg_id;
    String type;
    String text;
    String user;
    String ts;
    ArrayList<Reaction> reactions = new ArrayList();
    String subtype = "";
}


