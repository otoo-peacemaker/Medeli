package com.gads.medeli.repository;

import com.gads.medeli.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);

  Optional<User> findByPasswordAndEmail(String email, String password);

  Optional<User> findByPassword(String password);

}
