package handover;

import lombok.Data;

@Data
public class Message {

    String type;

    String token;
    String team_id;
    String api_app_id;
    Event event;
    String[] authed_teams;
    String event_id;
    Integer event_time;


    String user;
    String reaction;
    String item_user;
    Item item;
    String event_ts;
}