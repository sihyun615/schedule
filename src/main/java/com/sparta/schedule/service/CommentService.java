package com.sparta.schedule.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sparta.schedule.dto.CommentRequestDto;
import com.sparta.schedule.dto.CommentResponseDto;
import com.sparta.schedule.entity.Comment;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.repository.CommentRepository;
import com.sparta.schedule.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final ScheduleRepository scheduleRepository;

	public CommentResponseDto createComment(CommentRequestDto requestDto) {

		Long scheduleId = requestDto.getScheduleId();
		if (scheduleId == null) {
			throw new IllegalArgumentException("댓글을 작성할 일정의 ID가 입력되지 않았습니다.");
		}

		String content = requestDto.getContent();
		if (content.isEmpty()) {
			throw new IllegalArgumentException("댓글의 내용이 비어있습니다.");
		}

		String userId = requestDto.getUserId();
		if (userId.isEmpty()) {
			throw new IllegalArgumentException("작성자 ID가 입력되지 않았습니다.");
		}


		// 선택한 일정이 DB에 저장되어 있는지 확인
		Optional<Schedule> checkSchedule = scheduleRepository.findById(scheduleId);
		if (checkSchedule.isEmpty()) {
			throw new NoSuchElementException("선택한 일정을 찾을 수 없습니다.");
		}

		Schedule schedule = checkSchedule.get();

		Comment comment = new Comment(content, userId, schedule);
		commentRepository.save(comment);
		return new CommentResponseDto(comment);
	}
}
