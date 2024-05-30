package com.sparta.schedule.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	@Size(min = 4, max = 10, message = "username은 최소 4자 이상, 10자 이하이어야 합니다.")
	@Pattern(regexp = "^[a-z0-9]*$", message = "username은 알파벳 소문자(a~z), 숫자(0~9)로만 구성되어야 합니다.")
	private String username;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Size(min = 8, max = 15, message = "password는 최소 8자 이상, 15자 이하이어야 합니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,15}$", message = "password는 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로만 구성되어야 합니다.")
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Schedule> schedules = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Comment> comments = new ArrayList<>();

	public User(String username, String nickname, String password, UserRoleEnum role) {
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.role = role;
	}
}