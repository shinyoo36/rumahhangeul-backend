package com.stiven.rumahhangeul.repository;

import com.stiven.rumahhangeul.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByUserId(Long userId);
    Optional<Course> findByNamaCourseAndUserId(String namaCourse, Long userId);

}
