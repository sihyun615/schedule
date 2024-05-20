package com.sparta.schedule.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.exception.InvalidPasswordException;
import com.sparta.schedule.exception.NotFoundException;
import com.sparta.schedule.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final로 선언된 멤버 변수를 파라미터로 사용하여 생성자를 자동으로 생성
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;

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
			new NotFoundException("선택한 일정은 존재하지 않습니다.")
		);
	}

	public List<ScheduleResponseDto> getSchedules() {
		// DB 조회
		return scheduleRepository.findAllByOrderByCreatedAtDesc().stream().map(ScheduleResponseDto::new).toList();
	}

	@Transactional
	public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, String password) {
		// 해당 일정이 DB에 존재하는지 확인
		Schedule schedule = findSchedule(id);

		// 비밀번호 확인
		checkPassword(schedule.getPassword(), password);

		// schedule 내용 수정
		schedule.update(requestDto);

		ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule);

		return scheduleResponseDto;
	}

	public Long deleteSchedule(Long id, String password) {
		// 해당 일정이  DB에 존재하는지 확인
		Schedule schedule = findSchedule(id);

		// 비밀번호 확인
		checkPassword(schedule.getPassword(), password);

		// memo 삭제
		scheduleRepository.delete(schedule);

		return id;
	}

	private Schedule findSchedule(Long id) {
		return scheduleRepository.findById(id).orElseThrow(() ->
				new NotFoundException("선택한 일정은 존재하지 않습니다.")
		);
	}

	private void checkPassword(String actualPassword, String providedPassword) {
		if (!actualPassword.equals(providedPassword)) {
			throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
		}
	}
}
