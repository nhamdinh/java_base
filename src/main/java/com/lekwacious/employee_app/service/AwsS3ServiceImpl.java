package com.lekwacious.employee_app.service;


import com.lekwacious.employee_app.aws.AwsS3;
import com.lekwacious.employee_app.model.request.UploadFileRequest;
import com.lekwacious.employee_app.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3ServiceImpl.class);
    public static final String AVATAR_PREFIX = "user/avatar/";
    public static final String QUESTION_PREFIX = "question/file/";
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    private final AwsS3 awsS3;



    public String getStorageDirectoryFromMultipartFile(MultipartFile file) throws IOException {
        try {
            File tempFile = File.createTempFile("temp", null);
            FileCopyUtils.copy(file.getBytes(), tempFile);
            return tempFile.getParent();
        } catch (IOException e) {
            LOGGER.error("get Storage directory error: " + e.getMessage());
            throw e;
        }
    }

    public String generateFilePath(MultipartFile file, Integer type, Integer typeId) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String dateNow = dateFormat.format(date);
//            if (type.equals(Constant.UPLOAD_AVATAR_USER_TYPE))
//                path = AVATAR_PREFIX + username + "/T" + dateNow + file.getOriginalFilename();
//            else if(type.equals(Constant.UPLOAD_QUESTION_FILE_TYPE))
//                path=QUESTION_PREFIX+"question"+questionID+"/T"+dateNow+file.getOriginalFilename();
            return switch (type) {
                case Constant.UPLOAD_AVATAR_USER_TYPE ->
                        AVATAR_PREFIX  + "/T" + dateNow + file.getOriginalFilename();
                case Constant.UPLOAD_QUESTION_FILE_TYPE ->
                        QUESTION_PREFIX + "question" + typeId + "/T" + dateNow + file.getOriginalFilename();
                case Constant.UPLOAD_ANSWER_IMAGE_TYPE ->
                        QUESTION_PREFIX + "answer" + typeId + "/T" + dateNow + file.getOriginalFilename();
                default -> "";
            };
        } catch (Exception e) {
            LOGGER.error("generate path error:" + e.getMessage());
            throw e;
        }
    }

    public String generateFileUrl(MultipartFile file, int type, Integer typeId) {
        try {
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, Region.AP_NORTHEAST_2, generateFilePath(file, type, typeId));
        } catch (Exception e) {
            LOGGER.error("generate file url:" + e.getMessage());
            throw e;
        }
    }

    private boolean imageType(MultipartFile file) {
        String[] arrImageType = {"png", "jpg", "jfif", "tiff", "nef"};
        List<String> imageType = Arrays.asList(arrImageType);
        String[] splitFile = file.getOriginalFilename().split("[.]");
        String fileType = "";
        if (splitFile.length > 1) fileType = splitFile[splitFile.length - 1].toLowerCase();
        if (imageType.contains(fileType)) return true;
        return false;
    }

    @Override
    public String uploadFile(MultipartFile file, UploadFileRequest request) throws IOException {
        try {

            File convertedFile = new File(getStorageDirectoryFromMultipartFile(file), Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(convertedFile);
            //Xóa file trong folder cũ
            String deleteKey = AVATAR_PREFIX  + "/";
            String filePath = generateFilePath(file, request.getType(), request.getTypeId());
            switch (request.getType()) {
                case Constant.UPLOAD_AVATAR_USER_TYPE -> {
                    if (!imageType(file)) {
                        return "DataUtils.generateErrorBaseResponse(Collections.singletonList(ErrorEnum.ERR_400_17.getErrors()))";
                    } else {
                        awsS3.deleteFilesInPackage(deleteKey);
                        awsS3.upload(convertedFile, filePath);
                    }
                }
                case Constant.UPLOAD_ANSWER_IMAGE_TYPE -> {
                    if (!imageType(file)) {
                        LOGGER.error("file type invalid");
                        return "DataUtils.generateErrorBaseResponse(Collections.singletonList(ErrorEnum.ERR_400_17.getErrors()))";
                    }
                    awsS3.upload(convertedFile, filePath);
                }


            }
            return  generateFileUrl(file, request.getType(), request.getTypeId());
        } catch (Exception e) {
            LOGGER.error("Upload image error: " + e.getMessage());
            return "DataUtils.generateErrorBaseResponse(Collections.singletonList(ErrorEnum.ERR_400_1.getErrors()))";
        }

    }
}
