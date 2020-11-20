package com.ggboy.exam.beans.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Table(name = "stu_info")
@AllArgsConstructor
@NoArgsConstructor
public class StuInfo {

    @Column(name = "id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "username")
    private String username;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "salt")
    private String salt;

    public StuInfo(String phone){
        this.phone = phone;
    }

    public StuInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public StuInfo(String id, String username, String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }

}
