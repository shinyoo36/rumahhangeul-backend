package com.stiven.rumahhangeul.repository;

import com.stiven.rumahhangeul.entity.User;
import com.stiven.rumahhangeul.entity.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<UserProjection> findByUsernameAndPassword(String username, String password);
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<UserProjection> findByUserId(@Param("userId") Long userId);

    Optional<User> findByUsername(String username);
    Optional<User> findById(Long userId);
    Optional<User> findByEmail(String email);

    List<UserProjection> findAllByOrderByScoreDescNamaDepanAsc();

    User save(User user);

}
