package com.lekwacious.employee_app.service;

import com.lekwacious.employee_app.model.request.UploadFileRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AwsS3Service {
    String uploadFile(MultipartFile file, UploadFileRequest request) throws  IOException;

}
