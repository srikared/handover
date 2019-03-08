package handover;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDAO {

    int id;
    String message;

    public MessageDAO(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
