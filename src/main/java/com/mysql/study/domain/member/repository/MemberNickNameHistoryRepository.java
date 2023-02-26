package com.mysql.study.domain.member.repository;

import com.mysql.study.domain.member.entity.MemberNameHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
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
public class MemberNickNameHistoryRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    public static final String TABLE_NAME = "MemberNicknameHistory";

    static final RowMapper<MemberNameHistory> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> MemberNameHistory
            .builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .nickname(resultSet.getString("nickname"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<MemberNameHistory> findAllByMemberId(Long memberId) {
        var sql = String.format("SELECT * FROM %s WHERE memberId = :memberId", TABLE_NAME);
        var params = new MapSqlParameterSource().addValue("memberId", memberId);
        return namedJdbcTemplate.query(sql, params, ROW_MAPPER);

    }

    public MemberNameHistory save(MemberNameHistory history) {
        if (history.getId() == null) {
            return insert(history);
        }
        throw new UnsupportedOperationException("can not renew MemberNicknameHistory");
    }

    public MemberNameHistory insert(MemberNameHistory history) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource params = new BeanPropertySqlParameterSource(history);
        var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return MemberNameHistory
                .builder()
                .id(id)
                .memberId(history.getMemberId())
                .nickname(history.getNickname())
                .createdAt(history.getCreatedAt())
                .build();
    }

    public Optional<MemberNameHistory> findById(Long id) {
        var sql = String.format("SELECT * FROM %s WHERE id = :id",TABLE_NAME);
        // SqlParameterSource 사용가능 -> singleResult 반환
        var param = new MapSqlParameterSource()
                .addValue("id", id);

        var member = namedJdbcTemplate.queryForObject(sql, param, ROW_MAPPER);
        return Optional.ofNullable(member);

    }
}
