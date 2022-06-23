package com.devcourse.eggmarket.domain.model.image;

import com.devcourse.eggmarket.domain.model.image.exception.InvalidFileException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class PostImage implements Image {

    private static final String DELIMITER = "_";
    private static final String EXTENSION_SEPARATOR = ".";
    private final ImageType type;
    private final byte[] bytes;
    private final String fileName;
    private final String extension;
    private final int order; // 이미지 순서
    private final long id; // 판매글 id

    private PostImage(ImageType type, byte[] bytes, String extension, int order,
        String fileName,
        long id) {
        this.type = type;
        this.bytes = bytes;
        this.extension = extension;
        this.fileName = fileName;
        this.order = order;
        this.id = id;
    }

    private PostImage(ImageType type, byte[] bytes, int order, String fileName,
        long id) {
        this(type, bytes, FilenameUtils.getExtension(fileName), order, fileName, id);
    }

    public static PostImage toImage(long postId, MultipartFile file, int img_order) {
        if (Image.isNotImage(file)) {
            throw new InvalidFileException("유효한 이미지 첨부가 아닙니다"); // 참고로 이미지 크기 오류는 따로 존재합니다
        }

        try (InputStream inputStream = file.getInputStream()) {
            return new PostImage(ImageType.POST, inputStream.readAllBytes(), img_order,
                file.getOriginalFilename(),
                postId);
        } catch (IOException e) {
            throw new InvalidFileException("판매글에 업로드된 파일을 읽어오는데 실패하였습니다", e);
        }
    }

    @Override
    public byte[] getContents() {
        return this.bytes;
    }

    @Override
    public ImageType getType() {
        return this.type;
    }

    public String pathTobeStored(String basePath) {
        String storedName = this.extensionAppendedPath(
            String.join(
                DELIMITER,
                Long.toString(id),
                Integer.toString(order))
        );

        return FilenameUtils.concat(basePath, storedName);
    }

    private String extensionAppendedPath(String fileNameWithoutExtension) {
        return String.join(EXTENSION_SEPARATOR, fileNameWithoutExtension, this.extension);
    }
}
