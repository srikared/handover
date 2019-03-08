package handover;

import lombok.Data;

@Data
public class Event {
    String type;
    String channel;
    String user;
    String text;
    String ts;
    String event_ts;
    String channel_type;
}
