package domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransferableEntity implements Serializable, Comparable<TransferableEntity> {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected int rNum;
    protected LocalDateTime createdTime;
    protected int ms;

    public TransferableEntity(int id, int rNum, LocalDateTime createdTime) {
        this.id = id;
        this.rNum = rNum;
        this.createdTime = createdTime;
        this.ms = 0;
    }

    public TransferableEntity(int id, int rNum, LocalDateTime createdTime, int ms) {
        this.id = id;
        this.rNum = rNum;
        this.createdTime = createdTime;
        this.ms = ms;
    }

    public int getId() {
        return id;
    }

    public int getRNum() {
        return rNum;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public String getSQLFormattedCreatedTime() {
        return "'" + createdTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")) + "'";
    }

    public int getMs() {
        return ms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransferableEntity that = (TransferableEntity) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(TransferableEntity o) {
        return createdTime.compareTo(o.createdTime);
    }
}
