package com.ZandhiDokkie.minuet.exception;

public class LessonSlotConflictException extends RuntimeException {
    private static final String MESSAGE = "같은 요일에 선생님 또는 학생의 시간이 겹치는 레슨이 이미 등록되어 있습니다.";

    public LessonSlotConflictException() {
        super(MESSAGE);
    }

    public LessonSlotConflictException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
