package domain;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "source")
public class Source extends TransferableEntity implements Serializable {
    private static final long serialVersionUID = 2L;

    public Source(int id, int rNum, LocalDateTime createdTime) {
       super(id, rNum, createdTime);
    }

    public Source(int id, int rNum, LocalDateTime createdTime, int ms) {
        super(id, rNum, createdTime, ms);
    }

    public Source(int id, int rNum, LocalDateTime createdTime, int ms, boolean isProcessed) {
        super(id, rNum, createdTime, ms);
    }

    @Override
    public String toString() {
        return "Source{" +
                "id=" + id +
                ", rNum=" + rNum +
                ", createdTime=" + createdTime +
                ", ms=" + ms +
                '}';
    }
}
