package com.mysql.study.domain.post;

import com.mysql.study.domain.post.entity.Post;
import com.mysql.study.domain.post.repository.PostRepository;
import com.mysql.study.util.PostFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.stream.IntStream;

// @Deprecated
@SpringBootTest
public class PostBulkInsertTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void bulkInsertTest() {
        var easyRandom = PostFixtureFactory.get(
                4L,
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2023, 2, 1)
        );

        var stopWatch = new StopWatch();
        stopWatch.start();

        var posts = IntStream.range(0, 1000000)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .toList();
                //.forEach(x -> System.out.println(x.getMemberId() + "/" + x.getCreatedDate()));
        stopWatch.stop();
        System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeMillis() + "ms");

        var queryStopWatch = new StopWatch();
        queryStopWatch.start();
        postRepository.bulkInsert(posts); // bulk insert : 리스트 데이터를 한번에 insert
        queryStopWatch.stop();
        System.out.println("DB 인서트 시간 : " + queryStopWatch.getTotalTimeMillis() + "ms");
    }

}
