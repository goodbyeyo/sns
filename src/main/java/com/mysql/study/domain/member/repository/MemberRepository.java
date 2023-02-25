package com.mysql.study.domain.member.repository;

import com.mysql.study.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * BeanPropertyRowMapper 를 사용하면, ResultSet 의 컬럼명과 동일한 이름의 필드를 찾아서 값을 채워준다.
 * 단 컬럼명과 필드명이 다르면, @Column(name = "컬럼명") 을 사용해야 한다.
 * @Column(name = "컬럼명") 을 사용하면, 컬럼명과 필드명이 다르더라도 값을 채워준다.
 * @Column(name = "컬럼명") 을 사용하지 않으면, 컬럼명과 필드명이 다르면 값을 채워주지 않는다.
 * @Column(name = "컬럼명") 을 사용하지 않고, 컬럼명과 필드명이 다르면, BeanPropertyRowMapper 를 사용할 수 없다.
 * BeanPropertyRowMapper 를 사용하지 않고, RowMapper 를 직접 구현해야 한다.
 * 필드에 Setter 가 없으면, BeanPropertyRowMapper 를 사용할 수 없다.
 */
@RequiredArgsConstructor
@Repository
public class MemberRepository {

    public static final String TABLE_NAME = "Member";
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    // member id 에 따라 갱신 또는 생성, id 반환
    public Member save(Member member) {
        if (member.getId() == null) {
            return insert(member);
        }
        return update(member);
    }

    public Member insert(Member member) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return Member
                .builder()
                .id(id)
                .email(member.getEmail())
                .nickName(member.getNickName())
                .birthDay(member.getBirthDay())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public Member update(Member member) {
        // todo : implemented
        return member;
    }

    public Optional<Member> findById(Long id) {
        var sql = String.format("SELECT * FROM %s WHERE id = :id",TABLE_NAME);
        // SqlParameterSource 사용가능 -> singleResult 반환
        var param = new MapSqlParameterSource()
                .addValue("id", id);
        RowMapper<Member> rowMapper = (ResultSet resultSet, int rowNum) -> Member
                .builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .nickName(resultSet.getString("nickName"))
                .birthDay(resultSet.getObject("birthDay", LocalDate.class))
                .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
                .build();

        var member = namedJdbcTemplate.queryForObject(sql, param, rowMapper);
        return Optional.ofNullable(member);


    }
}
