package com.shipnolja.reservation.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@DynamicUpdate  //더티체킹 , 변경된 값만 확인한 이후 업데이트 쿼리 전송
public class UserInfo implements UserDetails {
    //공통부분
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userid")
    private String userid;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    
    //권한
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    /**                            탈퇴 관련                            **/

    //회원 탈퇴 여부
    @Column(name = "Enabled")
    private boolean userEnabled;

    //회원 탈퇴 날짜
    @Column(name = "deleted_date")
    private LocalDateTime userDeletedDate;


    @Builder
    public UserInfo(Long id, String userid, String password, String name, String phone,
                    UserRole role, boolean userEnabled,LocalDateTime userDeletedDate) {
        this.id = id;
        this.userid = userid;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.userEnabled = userEnabled;
        this.userDeletedDate = userDeletedDate;
    }


    // 사용자의 권한을 콜렉션 형태로 반환
    // 단, 클래스 자료형은 GrantedAuthority를 구현해야함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : role.toString().split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }
    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return userEnabled; // true -> 사용 가능
    }

    // 사용자의 id를 반환 (unique한 값)
    @Override
    public String getUsername() {
        return userid;
    }

    // 사용자의 password를 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

}
