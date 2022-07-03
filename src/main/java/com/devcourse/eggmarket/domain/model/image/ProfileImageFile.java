package com.devcourse.eggmarket.domain.model.image;

import com.devcourse.eggmarket.domain.model.image.exception.InvalidFileException;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class ProfileImageFile implements ImageFile {

    private static final String EXTENSION_DELIMITER = ".";

    private final ImageType type;
    private final byte[] bytes;
    private final Long userId;
    private final String fileName;

    private ProfileImageFile(ImageType type, byte[] bytes, Long userId,
        MultipartFile file) {
        this.type = type;
        this.bytes = bytes;
        this.userId = userId;
        this.fileName = file.getOriginalFilename();
    }

    public static ProfileImageFile toImage(Long userId, MultipartFile file) {
        if (ImageFile.isNotImage(file)) {
            throw new InvalidFileException("유효한 이미지 첨부가 아닙니다");
        }

        try {
            return new ProfileImageFile(ImageType.PROFILE, file.getBytes(), userId, file);
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
        String storedName = String.join(
            EXTENSION_DELIMITER,
            Long.toString(userId),
            FilenameUtils.getExtension(this.fileName));

        return FilenameUtils.concat(basePath, storedName);
    }

    @Override
    public ImageType getType() {
        return this.type;
    }
}
