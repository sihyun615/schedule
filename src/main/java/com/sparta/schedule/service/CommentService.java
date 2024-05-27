package com.sparta.schedule.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto) {
		Long scheduleId = requestDto.getScheduleId();
		if (scheduleId == null) {
			throw new IllegalArgumentException("댓글을 작성할 일정의 ID가 입력되지 않았습니다.");
		}

		if (commentId == null) {
			throw new IllegalArgumentException("댓글의 ID가 입력되지 않았습니다.");
		}

		String newContent = requestDto.getContent();
		if (newContent.isEmpty()) {
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

		// 선택한 댓글이 DB에 저장되어 있는지 확인
		Optional<Comment> checkComment = commentRepository.findById(commentId);
		if (checkComment.isEmpty()) {
			throw new NoSuchElementException("선택한 댓글을 찾을 수 없습니다.");
		}

		Comment comment = checkComment.get();

		if (!comment.getUserId().equals(userId)) {
			throw new IllegalArgumentException("선택한 댓글의 사용자가 현재 사용자와 일치하지 않습니다.");
		}

		// 댓글 내용이 변경되었는지 확인하고 변경된 경우에만 업데이트
		if (!newContent.equals(comment.getContent())) {
			comment.setContent(newContent);
			commentRepository.save(comment);
		}

		return new CommentResponseDto(comment);
	}
}
