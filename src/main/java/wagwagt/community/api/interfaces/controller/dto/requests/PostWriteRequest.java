package wagwagt.community.api.interfaces.controller.dto.requests;

import lombok.Getter;
import lombok.Setter;
import wagwagt.community.api.entities.domain.User;

@Getter
@Setter
public class PostWriteRequest {

    private Long postId;
    private String title;
    private String contents;
    private User user;
}
