package com.itravel.backend.service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class CloudFlareR2Service {
    private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucket;

    public String uploadFile(MultipartFile file) {
        System.out.println("Using bucket: " + bucket);

        String original = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new UnsupportedMediaTypeException("Filename is missing")).toLowerCase();
        String contentType = Optional.ofNullable(file.getContentType())
                .orElseThrow(() -> new UnsupportedMediaTypeException("Content-Type is unknown"));

        String ext = getFileExtension(original);
        String folder = switch (ext) {
        case "jpg", "jpeg", "png", "gif" -> "images";
        case "mp4", "mov" -> "videos";
        case "pdf", "doc", "docx", "txt" -> "documents";
        default -> throw new UnsupportedMediaTypeException("Unsupported file type: " + contentType);
        };

        String key = String.format("%s/%s-%s", folder, UUID.randomUUID(), original);

        PutObjectRequest req = PutObjectRequest.builder().bucket(bucket).key(key).contentType(contentType).build();

        try {
            s3Client.putObject(req, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new FileUploadException("File upload to Cloudflare R2 failed", e);
        }

        return key;
    }

    private String getFileExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length() - 1) {
            throw new IllegalArgumentException("Invalid file extension in filename: " + filename);
        }
        return filename.substring(idx + 1);
    }

    public class UnsupportedMediaTypeException extends RuntimeException {
        public UnsupportedMediaTypeException(String msg) {
            super(msg);
        }
    }

    public class FileUploadException extends RuntimeException {
        public FileUploadException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}