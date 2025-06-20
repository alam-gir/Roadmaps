package com.roadmaps.Roadmaps.modules.roadmap.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CommentRequestDto {
    String text;
    MultipartFile image;
}
