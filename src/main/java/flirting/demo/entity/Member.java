package flirting.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long id;

    @Column(length = 10, name = "username", nullable = false)
    private String username;

    @Column(length = 500, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private int snowflake;

    @ManyToMany(mappedBy = "members")
    private List<Group> groups;

}
