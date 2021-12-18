package com.team2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team2.model.GoogleAccount;
import com.team2.model.UetCoursesAccount;

@Repository
public interface GoogleAccountRepository extends JpaRepository<GoogleAccount, Long>{

}
