package com.devcourse.eggmarket.domain.post.model;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.ALREADY_COMPLETED;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_EXIST_BUYER;

import com.devcourse.eggmarket.domain.model.BaseEntity;
import com.devcourse.eggmarket.domain.post.exception.AlreadyCompletedException;
import com.devcourse.eggmarket.domain.post.exception.NotExistBuyerException;
import com.devcourse.eggmarket.domain.user.model.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
public class Post extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue
  private Long id;

  @Column(name = "price")
  private int price;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "content", nullable = false, length = 1024)
  private String content;

  @Column(name = "post_status", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private PostStatus postStatus;

  @Column(name = "category", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private Category category;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  private User seller;

  @ManyToOne
  @JoinColumn(name = "buyer_id")
  private User buyer;

  protected Post(String title,
      String content,
      Category category,
      int price,
      PostStatus postStatus,
      User seller) {
    this.title = title;
    this.content = content;
    this.category = category;
    this.price = price;
    this.postStatus = postStatus;
    this.seller = seller;
  }

  @Builder
  public Post(String title, String content, Category category, int price, User seller) {
    this(title, content, category, price, PostStatus.SALE, seller);
  }

  public Long getId() {
    return id;
  }

  public int getPrice() {
    return price;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public PostStatus getPostStatus() {
    return postStatus;
  }

  public Category getCategory() {
    return category;
  }

  public User getSeller() {
    return seller;
  }

  public User getBuyer() {
    return buyer;
  }

  public void updatePrice(int price) {
    this.price = price;
  }

  public void updateTitle(String title) {
    this.title = title;
  }

  public void updateContent(String content) {
    this.content = content;
  }

  public void updateCategory(Category category) {
    this.category = category;
  }

  private void checkAlreadyCompleted() {
    if (this.postStatus == PostStatus.COMPLETED) {
      throw new AlreadyCompletedException(ALREADY_COMPLETED);
    }
  }

  private void checkNotExistBuyer(User buyer) {
    if (buyer == null) {
      throw new NotExistBuyerException(NOT_EXIST_BUYER);
    }
  }
  public void updatePurchaseInfo(PostStatus postStatus, User buyer) {
    checkAlreadyCompleted();
    checkNotExistBuyer(buyer);
    this.postStatus = postStatus;
    this.buyer = buyer;
  }
}
