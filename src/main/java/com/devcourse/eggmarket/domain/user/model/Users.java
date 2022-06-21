package com.devcourse.eggmarket.domain.user.model;

import com.devcourse.eggmarket.domain.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor()
@Table(name = "users")
public class Users extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "phone_number", nullable = false, unique = true, length = 15)
  @NotBlank
  private String phoneNumber;

  @Column(name = "nick_name", nullable = false, unique = true, length = 12)
  @Size(min = 3, max = 12)
  @NotBlank
  private String nickName;

  @Column(name = "password", nullable = false, length = 64)
  @Size(min = 8 ,max = 64 )
  @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,64})")
  @NotBlank
  private String password;

  @Column(name = "manner_temperature", nullable = false)
  @NotNull
  @Positive
  private float mannerTemperature;

  @Column(name = "image_path")
  private String imagePath;

  @Column(name = "role",nullable = false)
  private String role;

  @Builder
  public Users(String phoneNumber, String nickName, String password, float mannerTemperature,
      String imagePath, String role) {
    
    if (mannerTemperature <= 0){
      mannerTemperature = 36.5F;
    }

    this.phoneNumber = phoneNumber;
    this.nickName = nickName;
    this.password = password;
    this.mannerTemperature = mannerTemperature;
    this.imagePath = imagePath;
    this.role = role;
  }

  public void changeNickName(String nickName) {
    this.nickName = nickName;
  }

}

