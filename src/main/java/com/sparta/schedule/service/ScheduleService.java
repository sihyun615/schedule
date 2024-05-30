package com.sparta.schedule.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.entity.User;
import com.sparta.schedule.exception.NotFoundException;
import com.sparta.schedule.repository.ScheduleRepository;
import com.sparta.schedule.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final로 선언된 멤버 변수를 파라미터로 사용하여 생성자를 자동으로 생성
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final UserRepository userRepository;

	public void createSchedule(String username, ScheduleRequestDto requestDto) {
		String title = requestDto.getTitle();
		String content = requestDto.getContent();
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException("등록된 사용자가 없습니다.")
		);
		Schedule schedule = new Schedule(title, content, user);
		scheduleRepository.save(schedule);
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
	public void updateSchedule(String username, Long scheduleId, ScheduleRequestDto requestDto) {
		// 해당 일정이 DB에 존재하는지 확인
		Schedule schedule = findScheduleByIdAndUserId(scheduleId, username);

		String title = requestDto.getTitle();
		String content = requestDto.getContent();
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException("등록된 사용자가 없습니다.")
		);
		schedule.update(title, content, user);
	}

	public void deleteSchedule(String username, Long scheduleId) {
		// 해당 일정이  DB에 존재하는지 확인
		Schedule schedule = findScheduleByIdAndUserId(scheduleId, username);
		scheduleRepository.delete(schedule);
	}

	private Schedule findSchedule(Long id) {
		return scheduleRepository.findById(id).orElseThrow(() ->
				new NotFoundException("선택한 일정은 존재하지 않습니다.")
		);
	}

	// 일정 ID로 일정 조회 및 사용자 ID로 권한 확인 메소드
	private Schedule findScheduleByIdAndUserId(Long scheduleId, String userId) {
		Schedule schedule = findSchedule(scheduleId);  // 선택한 일정이 DB에 저장되어 있는지 확인
		if (!schedule.getUser().getUsername().equals(userId)) {
			throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
		}
		return schedule;
	}
}
