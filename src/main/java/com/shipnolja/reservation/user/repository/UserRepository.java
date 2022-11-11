package com.shipnolja.reservation.user.repository;


import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> findByUserid(String userid);
    
    Optional<UserInfo> findById(Long id);

    Optional<UserInfo> findByIdAndRole(Long id,UserRole userRole);

    //권한 변경
    @Modifying
    @Query("update UserInfo u set u.role = :role where u.id = :id")
    void updateUserRole(UserRole role, Long id);


}
