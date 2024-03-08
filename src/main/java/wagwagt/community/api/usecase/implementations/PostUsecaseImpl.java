package wagwagt.community.api.usecase.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wagwagt.community.api.common.service.LikeService;
import wagwagt.community.api.entities.domain.*;
import wagwagt.community.api.interfaces.controller.repositories.PostRepository;
import wagwagt.community.api.interfaces.controller.repositories.UserRepository;
import wagwagt.community.api.interfaces.controller.dto.requests.PostWriteRequest;
import wagwagt.community.api.interfaces.controller.dto.responses.PostListResponse;
import wagwagt.community.api.interfaces.controller.dto.responses.PostResponse;
import wagwagt.community.api.usecase.PostUsecase;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostUsecaseImpl implements PostUsecase {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeService likeService;

    @Override
    @Transactional
    public Long posting(PostWriteRequest req){
        User user = userRepository.findByEmail(req.getEmail()).get();
        Post post = Post.builder()
                .title(req.getTitle())
                .contents(req.getContents())
                .user(user)
                .visitCnt(0)
                .likeCnt(0)
                .build();
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public Post findOne(Long postId){
        return postRepository.findById(postId);
    }

    @Override
    @Transactional
    public void modifyPost(Post post) {

    }

    @Override
    public void deletePost(Post post) {

    }

    @Override
    @Transactional
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id);
        post.setVisitCnt(post.getVisitCnt()+1);
        return PostResponse.builder()
                .postId(id)
                .title(post.getTitle())
                .contents(post.getContents())
                .nickName(post.getUser().getName())
                .regDate(post.getRegDate())
                .updDate(post.getUpdDate())
                .visitCnt(post.getVisitCnt())
                .likeCnt(post.getLikeCnt())
                .build();
    }

    @Override
    public PostListResponse getPostList(int page, int size) {

        List<PostResponse> posts= postRepository.findAll(page,size).stream()
                .map(post -> PostResponse.builder()
                        .postId(post.getId())
                        .nickName(post.getUser().getName())
                        .title(post.getTitle())
                        .contents(post.getContents())
                        .regDate(post.getRegDate())
                        .updDate(post.getUpdDate())
                        .likeCnt(post.getLikeCnt())
                        .visitCnt(post.getVisitCnt())
                        .build())
                .collect(Collectors.toList());
        return PostListResponse.builder().posts(posts)
                .totalCount(postRepository.findPostCounts())
                .build();
    }

    @Override
    @Transactional
    public PostResponse postLike(Post post , User user){
        PostLikeId postLikeId = new PostLikeId(post.getId(),user.getId());
        boolean isEnabled = likeService.likeDupleCheck(PostLikeId.class,postLikeId);

        if(isEnabled){
            post.setLikeCnt(post.getLikeCnt()+1);
            postRepository.save(post);
            postRepository.postLike(PostLike.builder().post(post).user(user).build());
            return PostResponse.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .contents(post.getContents())
                    .nickName(post.getUser().getName())
                    .regDate(post.getRegDate())
                    .updDate(post.getUpdDate())
                    .visitCnt(post.getVisitCnt())
                    .likeCnt(post.getLikeCnt())
                    .build();
        }else{
            throw new IllegalStateException("이미 따봉을 눌렀습니다.");
        }
    }

}