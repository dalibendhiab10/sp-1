package com.project.model.dao;

import com.project.model.dto.Admin;
import com.project.model.dto.EntityDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "admin")
public class AdminDAO implements EntityDAO {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "admin_id") private Long id;
	@JoinColumn(name = "admin_user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "admin_user_fk"))
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) private UserDAO user;
	@Column(name = "admin_name", nullable = false) private String adminName;
	@Column(name = "clearance_level", nullable = false) private Byte clearanceLevel;

	public AdminDAO(UserDAO user, String adminName, Byte clearanceLevel) {
		this.user = user;
		this.adminName = adminName;
		this.clearanceLevel = clearanceLevel;
	}

	@Override public EntityDTO toDTO() { return new Admin(user.getId(), user.getEmail(), adminName, clearanceLevel); }


}
