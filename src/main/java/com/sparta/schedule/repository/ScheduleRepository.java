package com.sparta.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.schedule.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	List<Schedule> findAllByOrderByCreatedAtDesc();
}
