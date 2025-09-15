package com.example.demo.repository;

import com.example.demo.entity.Role;
import com.example.demo.entity.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @EntityGraph(attributePaths = {"roles"})
    Optional<Role> findByName(RoleName name);
}
