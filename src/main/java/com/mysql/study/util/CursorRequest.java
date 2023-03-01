package com.mysql.study.util;
/**
 * 커서 기반 페이징 요청
 */
public record CursorRequest(Long key, int size) {

    // 마지막 페이지 요청(더 이상 페이지 없음을 의미)
    public static final Long NONE_KEY = -1L;

    public Boolean hasNext() {
        return key != null;
    }

    // 다음 페이지 요청(다음 페이지에서 사용할 키)
    public CursorRequest next(Long key) {
        return new CursorRequest(key, size);
    }
}
