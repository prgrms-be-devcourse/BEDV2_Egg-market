package com.devcourse.eggmarket.domain.model.image;

import com.devcourse.eggmarket.domain.model.image.exception.InvalidFileException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public class ProfileImage implements Image {

	private final ImageType type;
	private final InputStream inputStream;
	private final Long userId;
	private final String fileName;

	private ProfileImage(ImageType type, InputStream inputStream, Long userId,
		MultipartFile file) {
		this.type = type;
		this.inputStream = inputStream;
		this.userId = userId;
		this.fileName = userId.toString() + "." + file.getContentType().split("/")[1];
	}

	public static ProfileImage toImage(Long userId, MultipartFile file){
		if (Image.isNotImage(file)) {
			throw new InvalidFileException("유효한 이미지 첨부가 아닙니다");
		}

		try (InputStream inputStream = file.getInputStream()) {
			return new ProfileImage(ImageType.PROFILE, inputStream, userId, file);
		} catch (IOException e) {
			throw new InvalidFileException("업로드된 프로필 이미지 파일을 읽어오는데 실패하였습니다", e);
		}
	}

	@Override
	public InputStream getContents() {
		return this.inputStream;
	}

	@Override
	public String pathTobeStored(String basePath) {
		return basePath +"\\" + fileName;
	}

	@Override
	public ImageType getType() {
		return this.type;
	}
}
