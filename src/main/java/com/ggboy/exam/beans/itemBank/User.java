package com.ggboy.exam.beans.itemBank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "Teacher_id")
    private String TeacherId;
    @Column(name = "Teacher_name")
    private String TeacherName;
    @Column(name = "Teacher_Specialty")
    private String TeacherSpecialty;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "flag")
    private Integer flag;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
