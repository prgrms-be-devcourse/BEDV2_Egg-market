package com.devcourse.eggmarket.users.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//@NotBlank 같은 속성 추가하기 위해 pom.xml에 validation 추가해야 함!
@Entity
@Table(name = "users")
public class Users {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column(name = "phone_number", nullable = false, unique = true)
//  @NotBlank
  private String phoneNumber;

  @Column(name = "manner_temperature", nullable = false)
  private float mannerTemperature;

  @Column(name = "image_path")
  private String imagePath;

  @Column(name = "role",nullable = false)
  private String role;

}

