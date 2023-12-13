package koks.api.exceptions.errorcodes;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
public enum ErrorCode {

    START(0x001387),
    VM_PROTECTION(0x003781),
    CRACKED(0x1337187);

    private static ErrorCode instance;

    private final int codeId;

    ErrorCode(int codeId) {
        this.codeId = codeId;
    }

    public int getCodeId() {
        return codeId;
    }

    public static void sendError(ErrorCode error) {
        System.out.println("a Error occurred please contact the Koks Support");
        String errorCode = error.getCodeId() + "";
        if (error.equals(ErrorCode.CRACKED)) {
            int generated = error.getCodeId();
            while (generated == -1 || isInvalid(generated)) {
                generated = ThreadLocalRandom.current().nextInt(0x000000, 0x999999);
            }
            errorCode = generated + "";
        }
        int zero = 6 - errorCode.length();
        String zeroString = "";
        for(int i = 0; i < zero; i++)
            zeroString += "0";

        errorCode = zeroString + errorCode;

        System.out.println("Error Code: 0x" + errorCode);
    }

    public static boolean isInvalid(int code) {
        return Arrays.stream(values()).allMatch(errorCode -> errorCode.getCodeId() != code);
    }
}
