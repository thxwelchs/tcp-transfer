package repository;

import config.MySQLConnector;
import domain.Source;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SourceRepository extends TransferableEntityRepository<Source> {
    public SourceRepository(MySQLConnector connector, Class<Source> entityType) {
        super(connector, entityType);
    }

    public void save(int rNum) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO source(r_num, ms) VALUES(?, (MICROSECOND(NOW(3)) / 1000))";
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setInt(1, rNum);
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println("[SQL Error]: " + e.getMessage());
        } finally {
            conn.closeConnection(pstmt);
        }
    }

    public Source findById(int id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT *" +
                " FROM " + tableName +
                " WHERE id = ?";
        Source source = null;
        try {
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                source = new Source(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getTimestamp(3).toLocalDateTime(),
                        rs.getInt(4));
            }
//            System.out.println(rs.getStatement());
        } catch (SQLException e) {
            System.out.println("[SQL Error]: " + e.getMessage());
        } finally {
            conn.closeConnection(pstmt, rs);
        }

        return source;
    }

    public List<Source> findByCreatedTimeLessThan(LocalDateTime createdTime) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT *" +
                " FROM " + tableName +
                " WHERE created_time < ?" +
                " LIMIT 100 FOR UPDATE";
        List<Source> sources = new ArrayList<>();
        Connection connection = conn.getConnection();
        try {
            connection.setAutoCommit(false);

            pstmt = connection.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(createdTime));
            rs = pstmt.executeQuery();
//            synchronized (rs) {
                while(rs.next()) {
                    sources.add(new Source(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getTimestamp(3).toLocalDateTime(),
                            rs.getInt(4)));
                }
                System.out.println(rs.getStatement());
//            }
//            if(rs == null) return sources;
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("[SQL Error]: " + e.getMessage());
        } finally {
            conn.closeConnection(pstmt, rs);
        }

        return sources;
    }
}
