package net.app.savable.domain.Member;

import jakarta.persistence.*;
import lombok.*;
import net.app.savable.domain.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "app_user")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true, unique=true)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String phoneNumber;

    @Builder
    public Member(String name, String email, String picture, Role role, String phoneNumber){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    public Member update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
