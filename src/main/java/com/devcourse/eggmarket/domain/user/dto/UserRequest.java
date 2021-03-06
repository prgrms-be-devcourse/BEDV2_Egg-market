package com.devcourse.eggmarket.domain.user.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class UserRequest {

    public record Save(
        @NotBlank(message = "전화번호는 필수값입니다.")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
        String phoneNumber,

        @NotBlank(message = "닉네임은 필수값입니다.")
        @Size(min = 3, max = 12)
        String nickName,

        @NotBlank(message = "비밀번호는 필수값입니다.")
        @Size(min = 8, max = 64)
        @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,64})")
        String password,

        @NotNull(message = "isAdmin값은 null일 수 없습니다.")
        boolean isAdmin,

        MultipartFile profileImage
    ) {

    }

    public record Update(
        @NotBlank(message = "전화번호는 필수값입니다.")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
        String phoneNumber,

        @NotBlank(message = "닉네임은 필수값입니다.")
        @Size(min = 3, max = 12)
        String nickName

    ) {

    }

    public record Login(
        @NotBlank(message = "닉네임은 필수값입니다.")
        @Size(min = 3, max = 12)
        String nickName,

        @NotBlank(message = "비밀번호는 필수값입니다.")
        @Size(min = 8, max = 64)
        @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,64})")
        String password
    ) {

    }

    public record ChangePassword(
        @NotBlank(message = "비밀번호는 필수값입니다.")
        @Size(min = 8, max = 64)
        @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,64})")
        String newPassword
    ) {

    }

    public record FindNickname(
        @NotBlank(message = "전화번호는 필수값입니다.")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "잘못된 전화번호입니다.")
        String phoneNumber
    ) {

    }

    public record Profile(
        MultipartFile image

    ) {

    }
}
