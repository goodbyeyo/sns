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
import java.util.List;
import java.util.Optional;

/**
 * BeanPropertyRowMapper 를 사용하면, ResultSet 의 컬럼명과 동일한 이름의 필드를 찾아서 값을 채워준다.
 * 단 컬럼명과 필드명이 다르면, @Column(name = "컬럼명") 을 사용해야 한다.
 * Column(name = "컬럼명") 을 사용하면, 컬럼명과 필드명이 다르더라도 값을 채워준다.
 * Column(name = "컬럼명") 을 사용하지 않으면, 컬럼명과 필드명이 다르면 값을 채워주지 않는다.
 * Column(name = "컬럼명") 을 사용하지 않고, 컬럼명과 필드명이 다르면, BeanPropertyRowMapper 를 사용할 수 없다.
 * BeanPropertyRowMapper 를 사용하지 않고, RowMapper 를 직접 구현해야 한다.
 * 필드에 Setter 가 없으면, BeanPropertyRowMapper 를 사용할 수 없다.
 */
@RequiredArgsConstructor
@Repository
public class MemberRepository {

    public static final String TABLE_NAME = "Member";
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    static RowMapper<Member> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Member
            .builder()
            .id(resultSet.getLong("id"))
            .email(resultSet.getString("email"))
            .nickname(resultSet.getString("nickname"))
            .birthday(resultSet.getObject("birthday", LocalDate.class))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

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
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public Member update(Member member) {
        var sql = String.format(
                "UPDATE %s SET email = :email, nickname = :nickname, birthday = :birthday WHERE id = :id", TABLE_NAME);
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        namedJdbcTemplate.update(sql, params);
        return member;
    }

    public Optional<Member> findById(Long id) {
        var sql = String.format("SELECT * FROM %s WHERE id = :id",TABLE_NAME);
        // SqlParameterSource 사용가능 -> singleResult 반환
        var param = new MapSqlParameterSource()
                .addValue("id", id);

        var member = namedJdbcTemplate.queryForObject(sql, param, ROW_MAPPER);
        return Optional.ofNullable(member);
    }

    public List<Member> findAllByIdIn(List<Long> ids) {
        // todo :  sql 중복 제거 방법 고민
        if (ids.isEmpty()) {
            return List.of();
        }
        var sql = String.format("SELECT * FROM %s WHERE id IN (:ids)", TABLE_NAME);
        // ids 가 0개일 경우, in 절에 아무것도 들어가지 않아서 에러가 발생한다.
        var params = new MapSqlParameterSource().addValue("ids", ids);
        return namedJdbcTemplate.query(sql, params, ROW_MAPPER);
    }
}
