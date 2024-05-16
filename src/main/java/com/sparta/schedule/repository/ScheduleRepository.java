package com.sparta.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.schedule.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
