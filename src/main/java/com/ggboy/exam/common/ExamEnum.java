package com.ggboy.exam.common;

public enum ExamEnum {
    PENDING("pending","待执行"),
    PROCESSING("processing","考试中"),
    END("end","已结束"),
    ;

    private String encode;

    private String message;

    ExamEnum(String encode, String message) {
        this.encode = encode;
        this.message = message;
    }

    public String getEncode() {
        return encode;
    }

    public String getMessage() {
        return message;
    }
}
