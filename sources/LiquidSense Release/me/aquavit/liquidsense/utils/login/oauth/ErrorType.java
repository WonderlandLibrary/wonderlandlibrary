package me.aquavit.liquidsense.utils.login.oauth;

public enum ErrorType {
    QUERYING, QUERY_FAILED, // Query Stage
    WAIT_FOR_USER, USER_DECLINED, WRONG_DEVICE_CODE, TOKEN_EXPIRED, // Verification Stage
    SUCCESS; // Success
}