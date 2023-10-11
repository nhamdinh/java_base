package com.lekwacious.employee_app.aws;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;

@Component
@Slf4j
public class AwsS3 {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3.class);
    private S3Client s3Client;
    private String accessKey; // 액세스키
    private String secretKey; // 스크릿 엑세스 키

    final private Region clientRegion = Region.AP_NORTHEAST_2; // 한국
    private String bucket; // 버킷 명

    public AwsS3(@Value("${aws.accessKey}") String accessKey,
                  @Value("${aws.secretKey}") String secretKey,
                  @Value("${aws.s3.bucket-name}") String bucket) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        createS3Client();
    }


    public void createS3Client() {
        AwsCredentials credentials= AwsBasicCredentials.create(accessKey,secretKey);
        this.s3Client = S3Client.builder()
                .region(clientRegion)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();


    }

    public void upload(File file, String key) {
        PutObjectRequest putObjectRequest= PutObjectRequest.builder().bucket(bucket).key(key).build();
        s3Client.putObject(putObjectRequest,file.toPath());
        LOGGER.info("upload complete: " + putObjectRequest.key());
    }

    // 삭제 메서드
    public void delete(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest= DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key).build();
            // Delete 객체 생성
//            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);

            // Delete
            this.s3Client.deleteObject(deleteObjectRequest);
            LOGGER.info("deleted key: " + key);
        } catch (Exception e) {
            LOGGER.error("delete key error: " + e.getMessage());
        }
    }

    public void deleteFilesInPackage(String packagePrefix) {
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(bucket)
                .prefix(packagePrefix)
                .build();

        ListObjectsResponse listResponse = s3Client.listObjects(listRequest);

        for (S3Object object : listResponse.contents()) {
            String key = object.key();
            delete(key);
        }
    }
}
