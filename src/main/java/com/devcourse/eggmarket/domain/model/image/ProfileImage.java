package com.devcourse.eggmarket.domain.model.image;

import com.devcourse.eggmarket.domain.model.image.exception.InvalidFileException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public class ProfileImage implements Image {

    private final ImageType type;
	private final byte[] bytes;
    private final Long userId;
    private final String fileName;

    private ProfileImage(ImageType type, byte[] bytes, Long userId,
        MultipartFile file) {
        this.type = type;
        this.bytes = bytes;
        this.userId = userId;
        this.fileName = userId.toString() + "." + file.getContentType().split("/")[1];
    }

    public static ProfileImage toImage(Long userId, MultipartFile file) {
        if (Image.isNotImage(file)) {
            throw new InvalidFileException("유효한 이미지 첨부가 아닙니다");
        }

        try {
			byte[] bytes = file.getBytes();
            return new ProfileImage(ImageType.PROFILE, bytes, userId, file);
        } catch (IOException e) {
			throw new InvalidFileException("업로드된 Profile 파일을 읽어오는데 실패하였습니다");
        }
    }

    @Override
    public byte[] getContents() {
        return this.bytes;
    }

    @Override
    public String pathTobeStored(String basePath) {
        return basePath + "\\" + fileName;
    }

    @Override
    public ImageType getType() {
        return this.type;
    }
}
