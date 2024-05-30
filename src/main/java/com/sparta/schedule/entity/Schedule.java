package com.sparta.schedule.entity;

import java.util.ArrayList;
import java.util.List;

import com.sparta.schedule.dto.ScheduleRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

	@OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
	private List<Comment> commentList = new ArrayList<>();

	public Schedule(String title, String content, String manager) {
		this.title = title;
		this.content = content;
		this.manager = manager;
	}

	public void update(String title, String content, String manager) {
		this.title = title;
		this.content = content;
		this.manager = manager;
	}
}
