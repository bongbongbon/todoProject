package com.example.cicdtest.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.cicdtest.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private static final Set<String> AVAILABLE_IMAGE_EXTENSION
            = new HashSet<>(
            Arrays.asList(
                    "gif", "png", "jpg", "jpeg", "bmp", "webp",
                    "GIF", "PNG", "JPG", "JPEG", "BMP", "WebP", "WEBP"
            )
    );

    private final AmazonS3 amazonS3;

    @Value("${spring.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.aws.s3.directory}")
    private String directory;

    public String uploadImage(MultipartFile multipartFile) {
        validateImageFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String s3FileName = createS3FileName(multipartFile.getOriginalFilename());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setContentType(multipartFile.getContentType());

            amazonS3.putObject(
                    new PutObjectRequest(bucketName, s3FileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw CustomException.FAILED_FILE_UPLOAD;
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }


    public void deleteImage(String imageUrl) {
        try {
            String decodingUrl = URLDecoder.decode(imageUrl, "UTF-8");	// URL 디코딩
            int startIdx = decodingUrl.indexOf(directory);	// directory 경로가 처음 나온 부분부터 Object Key에 해당
            String objectKey = decodingUrl.substring(startIdx);
            amazonS3.deleteObject(bucketName, objectKey);
        } catch (UnsupportedEncodingException | AmazonServiceException e) {
            log.error(e.getMessage());
            throw CustomException.INTERNAL_SERVER_ERROR;
        }
    }

    private void validateImageFileName(String fileName) {
        String[] splitElements = fileName.split("\\.");
        String fileExtension = splitElements[splitElements.length - 1];

        if (!AVAILABLE_IMAGE_EXTENSION.contains(fileExtension)) {
            throw CustomException.INVALID_IMAGE_EXTENSION;
        }
    }

    private String createS3FileName(String fileName) {
        return directory + "/" + UUID.randomUUID() + fileName;
    }
}


