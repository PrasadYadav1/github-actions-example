package com.technoidentity.repository;

import com.technoidentity.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  List<User> findByEmailContainingIgnoreCase(String email);

  Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

  Page<User> findByStatus(Integer status, Pageable pageable);

  Page<User> findByStatusAndEmailContainingIgnoreCase(
      Integer status, String search, Pageable pageable);
}
