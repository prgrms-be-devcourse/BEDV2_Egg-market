package com.devcourse.eggmarket.domain.model.image;

import com.devcourse.eggmarket.domain.model.image.exception.InvalidFileException;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class PostImageFile implements ImageFile {

    private static final String DELIMITER = "_";
    private static final String EXTENSION_SEPARATOR = ".";
    private final ImageType type;
    private final byte[] bytes;
    private final String fileName;
    private final int order;
    private final long id;

    private PostImageFile(ImageType type, byte[] bytes, int order,
        String fileName,
        long id) {
        this.type = type;
        this.bytes = bytes;
        this.fileName = fileName;
        this.order = order;
        this.id = id;
    }

    public static PostImageFile toImage(long postId, MultipartFile file, int img_order) {
        if (ImageFile.isNotImage(file)) {
            throw new InvalidFileException("유효한 이미지 첨부가 아닙니다");
        }

        try {
            return new PostImageFile(
                ImageType.POST,
                file.getBytes(),
                img_order,
                file.getOriginalFilename(),
                postId);
        } catch (IOException e) {
            throw new InvalidFileException("판매글에 업로드된 파일을 읽어오는데 실패하였습니다");
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

    private String extensionAppendedPath(String filenameToBeStored) {
        return String.join(
            EXTENSION_SEPARATOR,
            filenameToBeStored,
            FilenameUtils.getExtension(this.fileName));
    }
}
