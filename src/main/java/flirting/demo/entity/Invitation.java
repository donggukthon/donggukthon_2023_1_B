package flirting.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id", unique = true, nullable = false)
    private Long id;

    @Column(name = "is_accepted")
    @ColumnDefault("false")
    private boolean isAccepted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public void updateIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    @Builder
    public Invitation(boolean isAccepted, Member sender, Member receiver, Group group) {
        this.isAccepted = isAccepted;
        this.sender = sender;
        this.receiver = receiver;
        this.group = group;
    }
}
