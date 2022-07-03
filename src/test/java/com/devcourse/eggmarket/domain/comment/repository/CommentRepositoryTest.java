package com.devcourse.eggmarket.domain.comment.repository;

import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User postWriter;
    private Post post;
    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        postWriter = User.builder()
            .phoneNumber("123456789")
            .nickName("user01")
            .password("User01234*")
            .role("USER")
            .build();

        post = Post.builder()
            .title("title01")
            .content("content01")
            .seller(postWriter)
            .price(1000)
            .category(Category.BEAUTY)
            .build();

        comment1 = new Comment(post, postWriter, "I'm writing comment to my own post!");
        comment2 = new Comment(post, postWriter, "I'm writing comment to my own post!");

        userRepository.save(postWriter);
        postRepository.save(post);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("해당 판매글의 댓글들을 가져올 수 있다")
    void findAllByPost() {
        List<Comment> allComments = commentRepository.findAllByPost(post);

        Assertions.assertThat(allComments.size())
            .isEqualTo(2);
    }

    @Test
    @DisplayName("포스트를 삭제하면 해당 포스트에 대한 모든 댓글들을 삭제한다")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void cascadeDeleteByPost() {
        postRepository.delete(post);

        List<Comment> allComments = commentRepository.findAllByPost(post);

        Assertions.assertThat(allComments.isEmpty())
            .isTrue();
    }

    @Test
    @DisplayName("commentId 의 식별자를 가지면서 해당 포스트에 속한 단일 댓글이 존재할 경우 그 커멘트를 찾아온다")
    void findByIdAndPostId() {
        Comment foundComment =
            commentRepository.findByIdAndPostId(
                comment1.getId(),
                comment1.getPost().getId()).get();

        Assertions.assertThat(foundComment)
            .usingRecursiveComparison()
            .isEqualTo(comment1);
    }

    @Test
    @DisplayName("특정 판매글의 댓글들을 전달된 Pageable 요청을 기반으로 최신순으로 조회한다")
    void findByPostAndIdLessThanOrderById() {
        // given
        int savedCommentSize = 29;
        Comment lastSavedComment = null;
        String content = "CONTENT";

        Post newPost = postRepository.save(Post.builder()
            .title("abc")
            .category(Category.BEAUTY)
            .content("content")
            .seller(postWriter)
            .price(1000)
            .build());

        for (int i = 0; i <= savedCommentSize; i++) {
            lastSavedComment = commentRepository.save(Comment.builder()
                .content(content + i)
                .post(newPost)
                .user(postWriter)
                .build());
        }
        // lastSavedComment 는 CONTENT29 라는 컨텐트 값을 갖는다

        Pageable pageRequest = PageRequest.of(0, 10);
        Long lastRequestedCommentId = lastSavedComment.getId() - 10;

        // when
        List<Comment> comments =
            commentRepository.findAllByPostAndIdGreaterThan(
                newPost,
                lastRequestedCommentId,
                pageRequest);

        // then
        Assertions.assertThat(comments.get(0).getContent())
            .isEqualTo("CONTENT20");

    }
}