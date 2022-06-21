package com.devcourse.eggmarket.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class UserRequest {
  public record Save(
      @NotBlank(message = "전화번호는 필수값입니다.")
      String phoneNumber,
      @NotBlank(message = "닉네임은 필수값입니다.")
      String nickName,
      @NotBlank(message = "비밀번호는 필수값입니다.")
      String password,
      @NotNull(message = "isAdmin값은 null일 수 없습니다.")
      boolean isAdmin,
      MultipartFile profileImage
  ){}

  public record Update(
      @NotBlank(message = "전화번호는 필수값입니다.")
      String phoneNumber,
      @NotBlank(message = "닉네임은 필수값입니다.")
      String nickName,
      @NotBlank(message = "비밀번호는 필수값입니다.")
      String password,
      MultipartFile profileImage
  ) {}
}
