package com.devcourse.eggmarket.domain.user.model;

import com.devcourse.eggmarket.domain.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    private static final float DEFAULT_TEMP = 36.5F;
    private static final float HIGHEST_TEMP = 100.0F;
    private static final float LOWEST_TEMP = 0.0F;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "nickname", nullable = false, unique = true, length = 12)
    private String nickName;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "manner_temperature", nullable = false)
    private float mannerTemperature;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(Long id, String phoneNumber, String nickName, String password,
        float mannerTemperature,
        String imagePath, UserRole role) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
        this.password = password;
        this.mannerTemperature = mannerTemperature;
        this.imagePath = imagePath;
        this.role = role;
    }

    @Builder
    public User(final String phoneNumber, final String nickName, final String password,
        final String imagePath, final String role) {
        this(null, phoneNumber, nickName, password, DEFAULT_TEMP, imagePath,
            UserRole.valueOf(role));
    }

    public void changeNickName(String nickName) {
        this.nickName = nickName;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPassword() {
        return password;
    }

    public float getMannerTemperature() {
        return mannerTemperature;
    }

    public String getImagePath() {
        return imagePath;
    }

    public UserRole getRole() {
        return role;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void updateMannerTemperature(float mannerTemperature) {
        float updatedMannerTemperature = this.mannerTemperature + mannerTemperature;

        updatedMannerTemperature = validTemperatureScope(updatedMannerTemperature);

        this.mannerTemperature = updatedMannerTemperature;
    }

    public boolean isSameUser(User user) {
        return this.id.equals(user.getId());
    }

    private float validTemperatureScope(final float mannerTemperature) {
        if (mannerTemperature < LOWEST_TEMP) {
            return LOWEST_TEMP;
        }
        return Math.min(mannerTemperature, HIGHEST_TEMP);
    }
}

