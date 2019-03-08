package handover;

import lombok.Data;

@Data
public class Item {
    String message;
    String channel;
    String ts;
    String type;
    String file_comment;
    String file;
}
