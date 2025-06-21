package com.roadmaps.Roadmaps.common.r2Storage.impl;

import com.roadmaps.Roadmaps.common.exceptions.ApiException;
import com.roadmaps.Roadmaps.common.r2Storage.R2StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class R2StorageServiceImpl implements R2StorageService {
    private final S3Client  s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;
    @Value("${cloud.aws.credentials.public-url}")
    private String publicUrl;

    @Override
    public String fileUpload(MultipartFile file, String folder) {
        String fileName = file.getOriginalFilename();
        String uniqueName = UUID.randomUUID() + "_" + (fileName != null ? fileName : "file");
        String key = (folder != null) ? folder + "/" + uniqueName : uniqueName;

        try{
            byte[] fileBytes = file.getBytes();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength((long) fileBytes.length)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));
            return getUrlFromKey(key);
        } catch (Exception e){
            log.error("Failed to upload file to cloud : {}", e.getMessage(), e);
            throw new ApiException("Fail to upload file to cloud!");
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        String key = getKeyFromUrl(fileUrl);
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error("Failed to delete file from cloud : {}", e.getMessage(), e);
            throw new ApiException("Failed to delete file from cloud!");
        }

    }

    @Override
    public void deleteAllFiles(List<String> images) {
        for(String image : images){
            deleteFile(image);
        }
    }

    private String getUrlFromKey(String key) {
        return publicUrl.concat("/").concat(key);
    }

    private String getKeyFromUrl(String url) {
        String prefix = publicUrl.concat("/");
        if(url.startsWith(prefix)){
            return url.substring(prefix.length());
        }

        throw new ApiException("Invalid file URL");
    }
}
