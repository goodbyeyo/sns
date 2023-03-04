package com.mysql.study.domain.follow.repository;


import com.mysql.study.domain.follow.entity.Follow;
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
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FollowRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public static final String TABLE_NAME = "Follow";

    static final RowMapper<Follow> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Follow.builder()
            .id(resultSet.getLong("id"))
            .fromMemberId(resultSet.getLong("fromMemberId"))
            .toMemberId(resultSet.getLong("toMemberId"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public Follow save(Follow follow) {
        if (follow.getId() == null) {
            return insert(follow);
        }
        throw new UnsupportedOperationException("can not renew Follow");
    }
    private Follow insert(Follow follow) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(follow);
        var id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Follow.builder()
                .id(id)
                .fromMemberId(follow.getFromMemberId())
                .toMemberId(follow.getToMemberId())
                .createdAt(follow.getCreatedAt())
                .build();
    }

    public List<Follow> findAllFromMemberId(Long fromMemberId) {
        var sql = String.format("SELECT * FROM %s WHERE fromMemberId = :fromMemberId", TABLE_NAME);
        var params = new MapSqlParameterSource().addValue("fromMemberId", fromMemberId);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Follow> findAllToMemberId(Long toMemberId) {
        var sql = String.format("SELECT * FROM %s WHERE toMemberId = :toMemberId", TABLE_NAME);
        var params = new MapSqlParameterSource().addValue("toMemberId", toMemberId);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }
}
