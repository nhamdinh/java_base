package com.lekwacious.employee_app.utils;


public class Constant {
    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String STATUS_RESPONSE_ERROR = "FAIL";
    public static final String STATUS_RESPONSE_SUCCESS = "OK";

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_TEMPORARY = 2;
    public static final String EXPIRE_TIME_OTP_CODE = "EXPIRE_OTP";
    public static final String EXPIRE_FIND_USER_CODE = "EXPIRE_VERIFICATION";
    public static final String EXPIRE_TIME_DEFAULT = "150000";

    public static final String COMMON_DATEONLY_FORMAT = "dd/MM/yyyy";
    public static final String COMMON_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATETIME_FORMAT_TDATE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    public static final int FAVORITE_COMPANY_TYPE = 1;
    public static final int FAVORITE_PRODUCT_TYPE = 2;

    public static final Integer SEARCH_GROUP_CHAT_BY_USER_TYPE = 1;

    public static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z]).{8,20}$";
    public static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final String FULLNAME_PARTERN="[가-힣a-zA-Z\\p{L} \\s]+";

    public static final int UPLOAD_AVATAR_USER_TYPE=1;
    public static final int UPLOAD_LOGO_COMPANY_TYPE=2;
    public static final int UPLOAD_PRODUCT_IMAGE_TYPE=3;
    public static final int UPLOAD_BANNER_IMAGE_TYPE=4;
    public static final int UPLOAD_QUESTION_FILE_TYPE=5;
    public static final int UPLOAD_ANSWER_IMAGE_TYPE=6;
    public static final int FIND_ALL_USER=1;
    public static final int FIND_COWORKER=2;

}

