DROP TABLE IF EXISTS post_attention;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS evaluation;
DROP TABLE IF EXISTS post_image;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id 						BIGINT AUTO_INCREMENT,
    phone_number 			VARCHAR(15)	NOT NULL UNIQUE,
    nickname				VARCHAR(12) NOT NULL UNIQUE,
    password                VARCHAR(64) NOT NULL,
    manner_temperature 		FLOAT NOT NULL,
    image_path				VARCHAR(100),
    role					VARCHAR(20) NOT NULL,
    created_at				DATE NOT NULL,
    updated_at				DATE,

    PRIMARY KEY(id)
);

CREATE TABLE post
(
    id						BIGINT AUTO_INCREMENT,
    seller_id				BIGINT,
    buyer_id				BIGINT,
    price					INT NOT NULL,
    title					VARCHAR(255) NOT NULL,
    content					VARCHAR(1024) NOT NULL,
    post_status				VARCHAR(20) NOT NULL,
    category				VARCHAR(20) NOT NULL,
    created_at				DATE NOT NULL,
    updated_at				DATE,

    PRIMARY KEY(id)
);

CREATE TABLE post_attention
(
    id						BIGINT AUTO_INCREMENT,
    post_id					BIGINT NOT NULL,
    user_id					BIGINT NOT NULL,
    created_at				DATE NOT NULL,
    updated_at				DATE,

    PRIMARY KEY(id)
);

CREATE TABLE post_image
(
    id						BIGINT AUTO_INCREMENT,
    post_id					BIGINT NOT NULL,
    image_path				VARCHAR(100) NOT NULL,
    created_at				DATE NOT NULL,
    updated_at				DATE,

    PRIMARY KEY(id)
);

CREATE TABLE evaluation
(
    id						BIGINT AUTO_INCREMENT,
    reviewer_id				BIGINT,
    reviewee_id				BIGINT NOT NULL, -- DELETE ON CASCADE 를 해야할지?
    post_id					BIGINT,
    score					TINYINT NOT NULL,
    content					VARCHAR(500) NOT NULL,
    created_at				DATE NOT NULL,
    updated_at				DATE,

    PRIMARY KEY(id)
);

CREATE TABLE comment
(
    id						BIGINT AUTO_INCREMENT,
    post_id					BIGINT NOT NULL,
    user_id					BIGINT NOT NULL,
    content					VARCHAR(500) NOT NULL,
    created_at				DATE NOT NULL,
    updated_at				DATE,

    PRIMARY KEY(id)
);

ALTER TABLE comment
    ADD CONSTRAINT comment_fk_post
        FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE;

ALTER TABLE comment
    ADD CONSTRAINT comment_fk_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE evaluation
    ADD CONSTRAINT evaluation_fk_reviewer_user
        FOREIGN KEY (reviewer_id) REFERENCES users(id);

ALTER TABLE evaluation
    ADD CONSTRAINT evaluation_fk_post
        FOREIGN KEY (post_id) REFERENCES post(id);

ALTER TABLE evaluation
    ADD CONSTRAINT evaluation_fk_reviewee_user
        FOREIGN KEY (reviewee_id) REFERENCES users(id) ON DELETE CASCADE;

-- ALTER TABLE users
-- 	ADD CONSTRAINT phone_unique UNIQUE (phone_number);
--
-- ALTER TABLE users
-- 	ADD CONSTRAINT name_unique UNIQUE (nickname);

ALTER TABLE post
    ADD CONSTRAINT post_fk_user
        FOREIGN KEY (seller_id) REFERENCES users(id);

ALTER TABLE post_image
    ADD CONSTRAINT post_image_fk_post
        FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE;

ALTER TABLE post_attention
    ADD CONSTRAINT post_attention_fk_post
        FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE;

ALTER TABLE post_attention
    ADD CONSTRAINT post_attention_fk_users
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;


