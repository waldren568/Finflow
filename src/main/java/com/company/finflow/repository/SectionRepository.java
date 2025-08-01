package com.company.finflow.repository;

import com.company.finflow.model.Section;
import com.company.finflow.model.AppUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByUser(AppUser user);
}