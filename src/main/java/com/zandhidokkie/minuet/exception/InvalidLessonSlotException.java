package com.zandhidokkie.minuet.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class InvalidLessonSlotException extends IllegalArgumentException {
    private final Map<String, String> fieldErrors;

    public InvalidLessonSlotException(Map<String, String> fieldErrors) {
        super("레슨 등록 정보를 확인해 주세요.");
        this.fieldErrors = Collections.unmodifiableMap(new LinkedHashMap<>(fieldErrors));
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
