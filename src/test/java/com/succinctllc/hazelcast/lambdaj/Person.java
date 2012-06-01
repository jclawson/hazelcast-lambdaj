package com.succinctllc.hazelcast.lambdaj;

import java.math.BigDecimal;

public class Person {
	public Person(String name, int age, String money) {
		this.name = name;
		this.age = age;
		this.money = new BigDecimal(money);
	}
	
	private String name;
	private int age;
	private BigDecimal money;
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public BigDecimal getMoney() {
		return money;
	}
}
