package com.checkcheck.ecoreading.domain.users.repository;

import com.checkcheck.ecoreading.domain.users.entity.Roles;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles,Long> {
    Optional<Roles> findByName(String name);
}
