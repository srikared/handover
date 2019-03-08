package handover;

import lombok.Data;

@Data
public class ChallengeReq {

    String token;
    String challenge;
    String type;
}
