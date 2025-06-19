package com.roadmaps.Roadmaps.common.r2Storage;

import org.springframework.web.multipart.MultipartFile;

public interface R2StorageService {
    String fileUpload(MultipartFile file, String folder);
    void deleteFile(String fileUrl);
}
