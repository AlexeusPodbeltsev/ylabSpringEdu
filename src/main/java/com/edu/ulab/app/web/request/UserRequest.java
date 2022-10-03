package com.edu.ulab.app.web.request;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class UserRequest {
    @Nullable
    private Long id;
    private String fullName;
    private String title;
    private String status;
    private int age;
}
