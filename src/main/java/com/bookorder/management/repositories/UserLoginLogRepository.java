package com.bookorder.management.repositories;

import com.bookorder.management.entities.UserLoginLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginLogRepository extends JpaRepository<UserLoginLogModel, Long> {

    @Query(nativeQuery = true, value = "DELETE FROM users_login_log WHERE user_id =  ?1")
    Long deleteLogByUserId(Long userId);
}
