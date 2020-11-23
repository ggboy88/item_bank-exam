package com.ggboy.exam.beans;

import com.ggboy.exam.beans.itemBank.Subject;
import com.ggboy.exam.beans.itemBank.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeaCourseLinkResponse {

    private List<User> users;

    private Subject subject;

}
