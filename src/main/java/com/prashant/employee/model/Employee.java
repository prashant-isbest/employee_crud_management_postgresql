package com.prashant.employee.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Employee_records")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column()
	private String firstName;

	@Column()
	private String lastName;

	@Column()
	private String email;

	@CreationTimestamp
	@Column(updatable = false)
	Timestamp dateCreated;

	@UpdateTimestamp
	@Column()
	Timestamp lastModified;

}
