package repository;

import config.MySQLConnector;
import domain.Table;
import domain.TransferableEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TransferableEntityRepository<E extends TransferableEntity> {
    protected MySQLConnector conn;
    private Class<E> entityType;
    protected String tableName;

    public TransferableEntityRepository(MySQLConnector connector, Class<E> entityType) {
        this.entityType = entityType;
        this.tableName = this.entityType.getAnnotation(Table.class).name();
        this.conn = connector;
    }

    public List<E> findByCreatedTime(LocalDateTime createdTime) throws SQLException {
        String sql = "SELECT *" +
                " FROM " + tableName +
                " WHERE created_time = ?";
        List<E> entities = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(createdTime));
            rs = pstmt.executeQuery();
//            synchronized (rs) {
                while(rs.next()) {
                    entities.add((E) new TransferableEntity(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getTimestamp(3).toLocalDateTime(),
                            rs.getInt(4)));
                }
                System.out.println(rs.getStatement());
//            }
        } catch (SQLException e) {
            System.out.println("[SQL Error]: " + e.getMessage());
        } finally {
            conn.closeConnection(pstmt, rs);
        }

        return entities;
    }

    public void delete(List<Integer> ids) {
        PreparedStatement pstmt = null;
        try {
            String sql = "DELETE FROM " + this.tableName + " WHERE id IN (" +
                    ids.stream().map(String::valueOf).collect(Collectors.joining(","))
                    + ")";
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[SQL Error]: " + e.getMessage());
        } finally {
            conn.closeConnection(pstmt);
        }
    }

    public void save(int rNum, LocalDateTime createdTime) {
        PreparedStatement pstmt = null;
        try {
            String sql = "INSERT INTO "+ this.tableName + " (r_num, created_time) VALUES(?, ?)";
            pstmt = conn.getConnection().prepareStatement(sql);
            pstmt.setInt(1, rNum);
            pstmt.setTimestamp(2, Timestamp.valueOf(createdTime));
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println("[SQL Error]: " + e.getMessage());
        } finally {
            conn.closeConnection(pstmt);
        }
    }

}
