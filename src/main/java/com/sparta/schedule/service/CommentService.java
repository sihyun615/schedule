package com.sparta.schedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.schedule.dto.CommentRequestDto;
import com.sparta.schedule.entity.Comment;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.entity.User;
import com.sparta.schedule.exception.NotFoundException;
import com.sparta.schedule.repository.CommentRepository;
import com.sparta.schedule.repository.ScheduleRepository;
import com.sparta.schedule.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final ScheduleRepository scheduleRepository;
	private final UserRepository userRepository;

	public void createComment(String username, CommentRequestDto requestDto) {
		Schedule schedule = findScheduleById(requestDto.getScheduleId());
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException("등록된 사용자가 없습니다.")
		);
		Comment comment = new Comment(requestDto.getContent(), user, schedule);
		commentRepository.save(comment);
	}

	@Transactional
	public void updateComment(Long commentId, String username, CommentRequestDto requestDto) {
		checkCommentIdNull(commentId);  // 댓글 Id 입력받았는지 확인
		findScheduleById(requestDto.getScheduleId());  // 선택한 일정이 DB에 저장되어 있는지 확인

		// 댓글 ID로 댓글 조회 및 사용자 ID로 권한 확인
		Comment comment = findCommentByIdAndUserId(commentId, username);

		// 댓글 내용이 변경되었는지 확인하고 변경된 경우에만 업데이트
		String newContent = requestDto.getContent();
		if (!newContent.equals(comment.getContent())) {
			comment.setContent(newContent);
			commentRepository.save(comment);
		}
	}


	public void deleteComment(Long commentId, String username, CommentRequestDto requestDto) {
		checkCommentIdNull(commentId);  //댓글 Id 입력받았는지 확인
		findScheduleById(requestDto.getScheduleId());  // 선택한 일정이 DB에 저장되어 있는지 확인

		// 댓글 ID로 댓글 조회 및 사용자 ID로 권한 확인
		Comment comment = findCommentByIdAndUserId(commentId, username);

		commentRepository.delete(comment);  // 댓글 삭제
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
			.orElseThrow(() -> new NotFoundException("선택한 일정을 찾을 수 없습니다."));
	}

	// 선택한 댓글이 DB에 저장되어 있는지 확인하는 메소드
	private Comment findCommentById(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new NotFoundException("선택한 댓글을 찾을 수 없습니다."));
	}

	// 댓글 ID로 댓글 조회 및 사용자 ID로 권한 확인 메소드
	private Comment findCommentByIdAndUserId(Long commentId, String username) {
		Comment comment = findCommentById(commentId);  // 선택한 댓글이 DB에 저장되어 있는지 확인
		if (!comment.getUser().getUsername().equals(username)) {
			throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
		}
		return comment;
	}
}
