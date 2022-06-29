package com.devcourse.eggmarket.domain.post.dto;


import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_BLANK_CATEGORY;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_BLANK_CONTENT;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_BLANK_POST_STATUS;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_BLANK_TITLE;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_NEGATIVE_ID;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_NEGATIVE_PRICE;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_NULL_USER;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_VALID_CATEGORY;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_VALID_POST_STATUS;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_VALID_RANGE_CATEGORY;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_VALID_RANGE_CONTENT;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_VALID_RANGE_POST_STATUS;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_VALID_RANGE_TITLE;

import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.PostStatus;
import com.devcourse.eggmarket.global.common.ValueOfEnum;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class PostRequest {

    public record Save(
        @NotBlank(message = NOT_BLANK_TITLE)
        @Size(min = 1, max = 255, message = NOT_VALID_RANGE_TITLE)
        String title,

        @NotBlank(message = NOT_BLANK_CONTENT)
        @Size(min = 1, max = 1024, message = NOT_VALID_RANGE_CONTENT)
        String content,

        @PositiveOrZero(message = NOT_NEGATIVE_PRICE)
        int price,

        @NotBlank(message = NOT_BLANK_CATEGORY)
        @Size(min = 1, max = 20, message = NOT_VALID_RANGE_CATEGORY)
        @ValueOfEnum(enumClass = Category.class, message = NOT_VALID_CATEGORY)
        String category,

        List<MultipartFile> images
    ) {

    }

    public record UpdatePost(
        @NotBlank(message = NOT_BLANK_TITLE)
        @Size(min = 1, max = 255, message = NOT_VALID_RANGE_TITLE)
        String title,

        @NotBlank(message = NOT_BLANK_CONTENT)
        @Size(min = 1, max = 1024, message = NOT_VALID_RANGE_CONTENT)
        String content,

        @PositiveOrZero(message = NOT_NEGATIVE_PRICE)
        int price,

        @NotBlank(message = NOT_BLANK_CATEGORY)
        @Size(min = 1, max = 20, message = NOT_VALID_RANGE_CATEGORY)
        @ValueOfEnum(enumClass = Category.class, message = NOT_VALID_CATEGORY)
        String category
    ) {

    }

    public record UpdatePurchaseInfo(
        @NotBlank(message = NOT_BLANK_POST_STATUS)
        @Size(min = 1, max = 20, message = NOT_VALID_RANGE_POST_STATUS)
        @ValueOfEnum(enumClass = PostStatus.class, message = NOT_VALID_POST_STATUS)
        String postStatus,

        @NotNull(message = NOT_NULL_USER)
        @PositiveOrZero(message = NOT_NEGATIVE_ID)
        Long buyerId
    ) {

    }
}

