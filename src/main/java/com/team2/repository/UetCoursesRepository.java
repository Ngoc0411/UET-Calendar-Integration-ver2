package com.team2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.model.ERole;
import com.team2.model.Role;
import com.team2.model.UetCoursesAccount;

@Repository
public interface UetCoursesRepository extends JpaRepository<UetCoursesAccount, Long> {
}
