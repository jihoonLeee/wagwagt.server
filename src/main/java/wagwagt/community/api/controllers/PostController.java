package wagwagt.community.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wagwagt.community.api.entities.Post;
import wagwagt.community.api.entities.User;
import wagwagt.community.api.requests.PostRegisterRequest;
import wagwagt.community.api.responses.PostListResponse;
import wagwagt.community.api.responses.PostResponse;
import wagwagt.community.api.usecases.PostUsecase;
import wagwagt.community.api.usecases.UserUsecase;

import java.net.URI;

@Tag(name="post_api", description = "POST Apis")
@RequestMapping("posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostUsecase postUsecase;
    private final UserUsecase userUsecase;

    @Operation(summary = "글작성" , description = "게시글 작성")
    @Parameter(name = "PostRegisterRequest")
    @PostMapping("/register")
    public ResponseEntity<Void> posting(@RequestBody PostRegisterRequest req){
        User user = userUsecase.findByEmail(req.getEmail()).get();
        Post post = Post.builder()
                .title(req.getTitle())
                .contents(req.getContents())
                .user(user)
                .visitCnt(0)
                .likeCnt(0)
                .build();
        return ResponseEntity.created(URI.create("/register/"+postUsecase.posting(post))).build();
    }

    @Operation(summary = "글 목록" , description = "게시글 목록")
    @GetMapping("/list")
    public ResponseEntity<PostListResponse> allPostList(@RequestParam int page){
        PostListResponse res = postUsecase.getPostList(page,10);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "게시글 조회" , description = "게시글 목록")
    @GetMapping("/post")
    public ResponseEntity<PostResponse> getPost(@RequestParam Long id){
        PostResponse res = postUsecase.getPost(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
