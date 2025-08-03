package com.example.hackathon.repositories;

import com.example.hackathon.model.Role;
import com.example.hackathon.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);  // Update to use RoleType

	Optional<Role> findByName(String name);
}
