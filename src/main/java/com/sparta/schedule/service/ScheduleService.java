package com.sparta.schedule.service;

import org.springframework.stereotype.Service;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.repository.ScheduleRepository;

@Service
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;

	public ScheduleService(ScheduleRepository scheduleRepository) {
		this.scheduleRepository = scheduleRepository;
	}

	public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {

		// RequestDto -> Entity
		Schedule schedule = new Schedule(requestDto);

		// DB 저장
		Schedule saveSchedule = scheduleRepository.save(schedule);


		// Entity -> ResponseDto
		ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule);

		return scheduleResponseDto;
	}

	public ScheduleResponseDto getScheduleById(Long id) {
		return scheduleRepository.findById(id)
			.map(ScheduleResponseDto::new)
			.orElseThrow(() ->
			new IllegalArgumentException("선택한 일정은 존재하지 않습니다.")
		);
	}
}
