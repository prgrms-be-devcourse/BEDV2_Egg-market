package com.devcourse.eggmarket.domain.model.image;

import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("image")
public final class ImageLocalPathProperties {
	private final String base;
	private final Type type;
	private String postBasePath;
	private String profileBasePath;

	public ImageLocalPathProperties(String base, Type type) {
		this.base = base;
		this.type = type;
	}

	public String postImagePath() {
		if (this.postBasePath == null) {
			this.postBasePath = FilenameUtils.concat(this.base, this.type.post);
		}
		return this.postBasePath;
	}

	public String profileImagePath() {
		if (this.profileBasePath == null) {
			this.profileBasePath = FilenameUtils.concat(this.base, this.type.profile);
		}
		return this.profileBasePath;
	}

	@Getter
	public static final class Type {
		private final String profile;
		private final String post;

		public Type(String profile, String post) {
			this.profile = profile;
			this.post = post;
		}
	}

}
