package com.mysql.study.domain.post.repository;

import com.mysql.study.domain.post.entity.PostLike;
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
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostLikeRepository {

    private final String TABLE_NAME = "PostLike";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final RowMapper<PostLike> ROW_MAPPER = (ResultSet rs, int rowNum) -> PostLike.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("memberId"))
            .postId(rs.getLong("postId"))
            .createdAt(rs.getObject("createdDate", LocalDateTime.class))
            .build();

    public PostLike save(PostLike postLike) {
        if (postLike.getId() == null) {
            return insert(postLike);
        }
        throw new UnsupportedOperationException("Timeline is not support renew");
    }

    private PostLike insert(PostLike timeline) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(timeline);
        var id = jdbcInsert.executeAndReturnKey(params).longValue();

        return PostLike.builder()
                .id(id)
                .memberId(timeline.getMemberId())
                .postId(timeline.getPostId())
                .createdAt(timeline.getCreatedAt())
                .build();
    }

    public Long getCount(Long postId) {
        var sql = String.format("""
        SELECT COUNT(id) FROM PostLike WHERE postId = :postId""", TABLE_NAME);
        var params = new MapSqlParameterSource("postId", postId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }
}
