package domain;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "destination")
public class Destination extends TransferableEntity implements Serializable {
    private static final long serialVersionUID = 3L;

    public Destination(int id, int rNum, LocalDateTime createdTime) {
        super(id, rNum, createdTime);
    }

    public Destination(int id, int rNum, LocalDateTime createdTime, int ms) {
        super(id, rNum, createdTime, ms);
    }

    //
    @Override
    public String toString() {
        return "Destination{" +
                "id=" + id +
                ", rNum=" + rNum +
                ", createdTime=" + createdTime +
                ", ms=" + ms +
                '}';
    }
}
