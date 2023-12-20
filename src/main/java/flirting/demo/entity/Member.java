package flirting.demo.entity;

import flirting.demo.dto.response.MemberResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long id;

    @Column(length = 10, name = "username", nullable = false)
    private String username;

    @Column(length = 50)
    private String email;

    @Column(name = "oauth_id",length = 1000)
    private String oauthId;

    @Column(length = 50, nullable = false)
    private int snowflake = 40;

    @ManyToMany(mappedBy = "members")
    private List<Group> groups = new ArrayList<>();

    public void updateSnowflake(int amount) {
        this.snowflake += amount;
    }



}
