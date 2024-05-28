package com.sparta.schedule.service;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.schedule.dto.CommentRequestDto;
import com.sparta.schedule.dto.CommentResponseDto;
import com.sparta.schedule.entity.Comment;
import com.sparta.schedule.entity.Response;
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
		Schedule schedule = findScheduleById(requestDto.getScheduleId());
		Comment comment = new Comment(requestDto.getContent(), requestDto.getUserId(), schedule);
		commentRepository.save(comment);
		return new CommentResponseDto(comment);
	}

	@Transactional
	public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto) {
		checkCommentIdNull(commentId);  // 댓글 Id 입력받았는지 확인
		findScheduleById(requestDto.getScheduleId());  // 선택한 일정이 DB에 저장되어 있는지 확인

		// 댓글 ID로 댓글 조회 및 사용자 ID로 권한 확인
		Comment comment = findCommentByIdAndUserId(commentId, requestDto.getUserId());

		// 댓글 내용이 변경되었는지 확인하고 변경된 경우에만 업데이트
		String newContent = requestDto.getContent();
		if (!newContent.equals(comment.getContent())) {
			comment.setContent(newContent);
			commentRepository.save(comment);
		}

		return new CommentResponseDto(comment);
	}


	public ResponseEntity<Response> deleteComment(Long commentId, CommentRequestDto requestDto) {
		checkCommentIdNull(commentId);  //댓글 Id 입력받았는지 확인
		findScheduleById(requestDto.getScheduleId());  // 선택한 일정이 DB에 저장되어 있는지 확인

		// 댓글 ID로 댓글 조회 및 사용자 ID로 권한 확인
		Comment comment = findCommentByIdAndUserId(commentId, requestDto.getUserId());

		commentRepository.delete(comment);  // 댓글 삭제

		Response response = new Response(HttpStatus.OK.value(), "댓글이 성공적으로 삭제되었습니다.");
		return ResponseEntity.ok(response);  // 성공 메시지와 상태 코드 반환
	}


	// 댓글 ID 유효성 검사 메소드
	private void checkCommentIdNull(Long commentId) {
		if (commentId == null) {
			throw new IllegalArgumentException("댓글의 ID가 입력되지 않았습니다.");
		}
	}

	// 선택한 일정이 DB에 저장되어 있는지 확인하는 메소드
	private Schedule findScheduleById(Long scheduleId) {
		return scheduleRepository.findById(scheduleId)
			.orElseThrow(() -> new NoSuchElementException("선택한 일정을 찾을 수 없습니다."));
	}

	// 선택한 댓글이 DB에 저장되어 있는지 확인하는 메소드
	private Comment findCommentById(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new NoSuchElementException("선택한 댓글을 찾을 수 없습니다."));
	}

	// 댓글 ID로 댓글 조회 및 사용자 ID로 권한 확인 메소드
	private Comment findCommentByIdAndUserId(Long commentId, String userId) {
		Comment comment = findCommentById(commentId);  // 선택한 댓글이 DB에 저장되어 있는지 확인
		if (!comment.getUserId().equals(userId)) {
			throw new IllegalArgumentException("선택한 댓글의 사용자가 현재 사용자와 일치하지 않습니다.");
		}
		return comment;
	}
}
