package wagwagt.community.api.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLikeRequest {

    private Long postId;
    private String email;
}
