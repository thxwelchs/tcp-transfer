package repository;

import config.MySQLConnector;
import domain.Destination;
import domain.TransferableEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DestinationRepository extends TransferableEntityRepository<Destination> {
    public DestinationRepository(MySQLConnector connector, Class<Destination> entityType) {
        super(connector, entityType);
    }

    public boolean saveAll(List<? extends TransferableEntity> sourceList) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        int size = sourceList.size();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ")
                    .append(this.tableName).append(" ").append("(r_num, created_time, ms) VALUES ");
            for (int i = 0; i < size; i++) {
                TransferableEntity e = sourceList.get(i);
                sb.append("(")
                        .append(e.getRNum())
                        .append(",")
                        .append(e.getSQLFormattedCreatedTime())
                        .append(",")
                        .append(e.getMs())
                        .append(")");
                if(i < size - 1) {
                    sb.append(",");
                }
            }
            System.out.println(sb.toString());
            pstmt = conn.getConnection().prepareStatement(sb.toString());
            count = pstmt.executeUpdate();
//            System.out.println(pstmt.toString());
        } catch (SQLException e) {
            System.out.println("[SQL Error]: " + e.getMessage());
        } finally {
            conn.closeConnection(pstmt);
        }

        return count == size;
    }
}
