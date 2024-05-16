package com.sparta.schedule.entity;

import com.sparta.schedule.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "schedule") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Schedule extends Timestamped{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "title", nullable = false)
	private String title;
	@Column(name = "content", nullable = false, length = 500)
	private String content;
	@Column(name = "manager", nullable = false)
	private String manager;
	@Column(name = "password", nullable = false)
	private String password;

	public Schedule(ScheduleRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.content = requestDto.getContent();
		this.manager = requestDto.getManager();
		this.password = requestDto.getPassword();
	}
}
