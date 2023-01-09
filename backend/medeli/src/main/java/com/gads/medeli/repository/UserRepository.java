package com.gads.medeli.repository;

import com.gads.medeli.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);

  Optional<User> findByPasswordAndEmail(String email, String password);

  Optional<User> findByPassword(String password);

  @Transactional
  @Modifying
  @Query("UPDATE User a " + "SET a.accountVerified = TRUE WHERE a.email = ?1")
  int enableAppUser(String email);
}
