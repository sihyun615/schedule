package com.sparta.schedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content", nullable = false, length = 500)
	private String content;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@ManyToOne
	@JoinColumn(name = "schedule_id", nullable = false)
	private Schedule schedule;

	public Comment(String content, String userId, Schedule schedule) {
		this.content = content;
		this.userId = userId;
		this.schedule = schedule;
	}
}
