package com.sparta.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sparta.schedule.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
