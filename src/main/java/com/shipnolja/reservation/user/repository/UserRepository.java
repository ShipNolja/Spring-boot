package com.shipnolja.reservation.user.repository;


import com.shipnolja.reservation.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> findByUserid(String userid);
    
    Optional<UserInfo> findById(Long id);

    @Modifying
    @Query("update UserInfo u set u.password = :password where u.id = :id")
    void updateUserInfoPassword(String password,Long id);

}
