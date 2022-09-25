package com.edu.ulab.app.web.request;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class BookRequest {
    @Nullable
    private Long id;
    private String title;
    private String author;
    private long pageCount;
}
