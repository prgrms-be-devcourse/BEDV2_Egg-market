package com.devcourse.eggmarket.domain.post.model;

import com.devcourse.eggmarket.domain.model.BaseEntity;
import com.devcourse.eggmarket.domain.user.model.User;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostAttention extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public PostAttention(Long id, Post post, User user) {
        this.id = id;
        this.post = post;
        this.user = user;
    }

    @Builder
    public PostAttention(Post post, User user) {
        this(null, post, user);
    }

    public Post getPost() {
        return post;
    }

    public User getUser() {
        return user;
    }
}
