package handover;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelHistory {
    Boolean ok;
    ArrayList<MessageType> messages = new ArrayList();
}
