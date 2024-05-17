package com.sparta.schedule.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.service.ScheduleService;

@RestController
@RequestMapping("/api")
public class ScheduleController {

	private final ScheduleService scheduleService;

	public ScheduleController(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	@PostMapping("/schedule")
	public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
		return scheduleService.createSchedule(requestDto);
	}

	@GetMapping("/schedule/info")
	public ScheduleResponseDto getScheduleById(Long id) {
		return scheduleService.getScheduleById(id);
	}

	@GetMapping("/schedules")
	public List<ScheduleResponseDto> getSchedules() {
		return scheduleService.getSchedules();
	}

	@PutMapping("/schedule/{id}")
	public ScheduleResponseDto updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
		String password = requestDto.getPassword();
		return scheduleService.updateSchedule(id, requestDto, password);
	}

	@DeleteMapping("/schedule/{id}")
	public Long deleteSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
		String password = requestDto.getPassword();
		return scheduleService.deleteSchedule(id, password);
	}
}
