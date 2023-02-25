package com.mysql.study.domain.member;

import com.mysql.study.util.MemberFixtureFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberTest {

    @DisplayName("회원은 닉네임을 변경 할 수 있다")
    @Test
    void changeNameTest() {
//        LongStream.range(0, 10)
//                .mapToObj(MemberFixtureFactory::create)
//                // .mapToObj(i -> MemberFixtureFactory.create(i))
//                .forEach(member -> {
//                    System.out.println("member = " + member.getNickName());
//                });
        var member = MemberFixtureFactory.create();
        var expectedName = "wook";
        member.changeNickName(expectedName);
        Assertions.assertEquals(expectedName, member.getNickname());
    }

    @DisplayName("회원은 닉네임은 10자리를 초과 할 수 없다")
    @Test
    void nickNameMaxLengthTest() {
        var member = MemberFixtureFactory.create();
        var expectedName = "wookwookwook";
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> member.changeNickName(expectedName)
        );
    }
}
