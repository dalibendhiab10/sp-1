package com.project.model.dao;

import com.project.model.dto.EntityDTO;
import com.project.model.dto.User;
import com.project.model.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter @NoArgsConstructor
@Entity @Table(name = "users")
public class UserDAO implements UserDetails, EntityDAO {

	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
	@SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
	@Column(name = "user_id") private Long id;
	@Column(name = "email") private String email;
	@Column(name = "password") private String password;
	@Column(name = "role") @Enumerated(EnumType.STRING) private UserRole role;
	public UserDAO(String email, String password, UserRole role) {
		this.email = email; this.password = password; this.role = role;
	}


	@Override public EntityDTO toDTO() { return new User(id,email,password,role); }

	@Override public String getUsername() { return email; }
	@Override public String getPassword() { return password; }
	@Override public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
	}
	@Override public boolean isAccountNonExpired() { return true; }
	@Override public boolean isAccountNonLocked() { return true; }
	@Override public boolean isCredentialsNonExpired() { return true; }
	@Override public boolean isEnabled() { return true; }
}
