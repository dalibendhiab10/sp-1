package com.project.model.dao;

import com.project.model.dto.Client;
import com.project.model.dto.EntityDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "client")
public class ClientDAO implements EntityDAO {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "client_id") private Long id;
	@JoinColumn(name = "client_user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "client_user_fk"))
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) private UserDAO user;
	@Column(name = "client_name", nullable = false) private String clientName;
	@Column(name = "nationality", nullable = false) private String nationality;
	@Column(name = "phone_number", unique = true, nullable = false) private String phoneNumber;
	@Column(name = "age", nullable = false) private Integer age;

	public ClientDAO(UserDAO user, String clientName, String nationality, String phoneNumber, Integer age) {
		this.user = user;
		this.clientName = clientName;
		this.nationality = nationality;
		this.phoneNumber = phoneNumber;
		this.age = age;
	}

	@Override public EntityDTO toDTO() { return new Client(user.getId(), user.getEmail(), clientName, nationality, phoneNumber, age); }
}
