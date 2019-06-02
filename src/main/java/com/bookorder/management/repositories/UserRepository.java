package com.bookorder.management.repositories;

import com.bookorder.management.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM users u WHERE u.username = ?1 LIMIT 1")
    UserModel getUserByUsername(String username);

}
