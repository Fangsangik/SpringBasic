package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository{

    private final DataSource dataSource;

    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Member save(Member member) {
        String sql = "insert into member(name) values (?)";

        //자원들 release
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // Statement.RETURN_GENERATED_KEYS -> DB에 insert id 값 get
            pstmt.setString(1, member.getName());

            //DB에 실제 쿼리 날라간다.
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()){
                member.setId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e){
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            //조회
            rst = pstmt.executeQuery();

            if (rst.next()){
                Member member = new Member();
                member.setId(rst.getLong("id"));
                member.setName(rst.getString("name"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e){
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rst);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);

            rst = pstmt.executeQuery();

            if (rst.next()){
                Member member = new Member();
                member.setId(rst.getLong("id"));
                member.setName(rst.getString("name"));
                return Optional.of(member);
            }
            return Optional.empty();
        } catch (Exception e){
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rst);
        }

    }

    @Override
    public List<Member> findALl() {
        return null;
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null){
                rs.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        try {
            if (conn != null){
                conn.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        try {
            if (pstmt != null){
                pstmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Connection getConnection(){
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn) throws SQLException {
        //닫을떄
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
