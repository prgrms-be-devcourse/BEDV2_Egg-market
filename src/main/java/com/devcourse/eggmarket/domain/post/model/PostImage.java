package com.devcourse.eggmarket.domain.post.model;

import javax.persistence.Column;
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
public class PostImage {

    @Id
    @GeneratedValue
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    private Post post;

    @Column(length = 100, nullable = false)
    private String imagePath;

    @Builder
    public PostImage(Long id, Post post, String imagePath) {
        this.id = id;
        this.post = post;
        this.imagePath = imagePath;
    }

    public PostImage(Post post, String imagePath) {
        this(null, post, imagePath);
    }

    public String getImagePath() {
        return imagePath;
    }
}
