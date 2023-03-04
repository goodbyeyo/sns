package com.mysql.study.domain.post.repository;

import com.mysql.study.util.PageHelper;
import com.mysql.study.domain.post.dto.DailyPostCount;
import com.mysql.study.domain.post.dto.DailyPostCountRequest;
import com.mysql.study.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class PostRepository {

    private final String TABLE_NAME = "POST";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final RowMapper<DailyPostCount> DAILY_POST_COUNT_MAPPER = (ResultSet rs, int rowNum) -> new DailyPostCount(
            rs.getLong("memberId"),
            rs.getObject("createdDate", LocalDate.class),
            rs.getLong("postCount")
    );

    private static final RowMapper<Post> ROW_MAPPER = (ResultSet rs, int rowNum) -> new Post(
            rs.getLong("id"),
            rs.getLong("memberId"),
            rs.getString("contents"),
            rs.getObject("createdDate", LocalDate.class),
            rs.getObject("createdAt", LocalDateTime.class)
    );

    public Post save(Post post) {
        if (post.getId() == null) {
            return insert(post);
        }
        throw new UnsupportedOperationException("Post is not support renew");
    }

    private Post insert(Post post) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        var id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request) {
        var sql = String.format("""
                select createdDate, memberId, count(id) as postCount
                from %s
                where memberId = :memberId and createdDate between :firstDate and :lastDate
                group by memberId, createdDate
                """,TABLE_NAME);  // language=MySQL
        var params = new BeanPropertySqlParameterSource(request);
        return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_MAPPER);
    }

    public void bulkInsert(List<Post> posts) {
        var sql = String.format("""
                insert into `%s` (memberId, contents, createdDate, createdAt)
                values (:memberId, :contents, :createdDate, :createdAt)
                """, TABLE_NAME); // language=MySQL

        SqlParameterSource[] params = posts.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        var sql = String.format("""
                select *
                from %s
                where memberId = :memberId
                order by %s
                limit :size offset :offset
                """, TABLE_NAME, PageHelper.orderBy(pageable.getSort())); // language=MySQL

        var posts = namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
        return new PageImpl<>(posts, pageable, getCount(memberId));
    }

    private Long getCount(Long memberId) {
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId);
        var sql = String.format("""
                select count(id) as count
                from %s
                where memberId = :memberId
                """, TABLE_NAME); // language=MySQL
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public List<Post> findAllByInMemberIdAndOrderByIdDesc(Long memberId, int size) {
        var sql = String.format("""
                select *
                from %s
                where memberId = :memberId
                order by id desc
                limit :size
                """, TABLE_NAME); // language=MySQL
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(Long memberId, Long id, int size) {
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size)
                .addValue("id", id);
        var sql = String.format("""
                select *
                from %s
                where memberId = :memberId and id < :id
                order by id desc
                limit :size
                """, TABLE_NAME); // language=MySQL
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByInMemberIdAndOrderByIdDesc(List<Long> memberIds, int size) {
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

    public List<Post> findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(List<Long> memberIds, Long id, int size) {
        if (memberIds.isEmpty()) {
            return List.of();
        }
        var params = new MapSqlParameterSource()
                .addValue("memberIds", memberIds)
                .addValue("size", size)
                .addValue("id", id);
        var sql = String.format("""
                select *
                from %s
                where memberId in (:memberIds) and id < :id
                order by id desc
                limit :size
                """, TABLE_NAME); // language=MySQL
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByInId(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        var params = new MapSqlParameterSource()
                .addValue("ids", ids);

        var sql = String.format("""
                select *
                from %s
                where id in :ids
                """, TABLE_NAME); // language=MySQL
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }
}
