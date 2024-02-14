package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.*;

public class JdbcTemplateMemberRepository implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;


    //Spring dataSource 자동 inject
    public JdbcTemplateMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //Insert문
    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        //쿼리 짤 필요 없음
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        //insert문 형성
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)); //key를 받고
        member.setId(key.longValue()); //member에서 set id를 형성
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> rst = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id);
        return rst.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> rst =jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name);
        return rst.stream().findAny();
    }

    @Override
    public List<Member> findALl() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    //객체 형성
    private RowMapper<Member> memberRowMapper (){
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
}
