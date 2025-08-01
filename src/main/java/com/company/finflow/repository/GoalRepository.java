package com.company.finflow.repository;

import com.company.finflow.model.Goal;
import com.company.finflow.model.AppUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser(AppUser user);
}