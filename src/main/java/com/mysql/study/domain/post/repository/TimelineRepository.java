package com.mysql.study.domain.post.repository;

import com.mysql.study.domain.post.entity.Post;
import com.mysql.study.domain.post.entity.Timeline;
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

@RequiredArgsConstructor
@Repository
public class TimelineRepository {

    private final String TABLE_NAME = "Timeline";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final RowMapper<Timeline> ROW_MAPPER = (ResultSet rs, int rowNum) -> new Timeline(
            rs.getLong("id"),
            rs.getLong("memberId"),
            rs.getLong("postId"),
            rs.getObject("createdDate", LocalDateTime.class)
    );

    public Timeline save(Timeline timeline) {
        if (timeline.getId() == null) {
            return insert(timeline);
        }
        throw new UnsupportedOperationException("Timeline is not support renew");
    }

    private Timeline insert(Timeline timeline) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(timeline);
        var id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Timeline.builder()
                .id(id)
                .memberId(timeline.getMemberId())
                .postId(timeline.getPostId())
                .createdAt(timeline.getCreatedAt())
                .build();
    }

    public List<Timeline> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size) {
        var sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY id DESC
                LIMIT :size
                """,TABLE_NAME);
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public void bulkInsert(List<Timeline> timelines) {
        var sql = String.format("""
                insert into `%s` (memberId, postId, createdAt)
                values (:memberId, :postId, :createdAt)
                """, TABLE_NAME); // language=MySQL

        SqlParameterSource[] params = timelines.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    public List<Timeline> findAllByInMemberIdAndOrderByIdDesc(List<Long> memberIds, int size) {
        if (memberIds.isEmpty()) {
            return List.of();
        }
        var params = new MapSqlParameterSource()
                .addValue("memberIds", memberIds)
                .addValue("size", size);

        var sql = String.format("""
                select *
                from %s
                where memberId in (:memberIds)
                order by id desc
                limit :size
                """, TABLE_NAME); // language=MySQL
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Timeline> findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(Long memberId, Long id, int size) {
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size)
                .addValue("id", id);
        var sql = String.format("""
                select *
                from %s
                where memberId = :memberIds and id < :id
                order by id desc
                limit :size
                """, TABLE_NAME); // language=MySQL
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

}
