package com.mysql.study.util;

import java.util.List;

public record PageCursor<T>(
        CursorRequest nextCursorRequest,  // 클라이언트가 요청할 키
        List<T> body) {     //
}
