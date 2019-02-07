package com.revature.screenforce.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SCREENER")
public class Screener {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="SCREENER_ID")
	private int screenerId;
	
	@Column(name="NAME")
	private String name;

	public int getScreenerId() {
		return screenerId;
	}

	public void setScreenerId(int screenerId) {
		this.screenerId = screenerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Screener(int screenerId, String name) {
		super();
		this.screenerId = screenerId;
		this.name = name;
	}

	public Screener() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
