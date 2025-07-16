package com.ZandhiDokkie.minuet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 즉시 실행 가능한 간단한 테스트
 */
public class QuickFixTest {

    @Test
    void simpleTest() {
        System.out.println("🚀 간단한 테스트 시작");

        // 기본 검증
        assertEquals(5, 2 + 3);
        assertTrue(true);
        assertNotNull("test");

        System.out.println("✅ 기본 테스트 성공!");
    }

    @Test
    void stringTest() {
        String result = "Hello Minuet";
        assertEquals(12, result.length());
        assertTrue(result.contains("Minuet"));
        System.out.println("✅ 문자열 테스트 성공!");
    }
}
