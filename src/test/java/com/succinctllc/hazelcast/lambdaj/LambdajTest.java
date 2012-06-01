package com.succinctllc.hazelcast.lambdaj;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import static java.util.Arrays.asList;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class LambdajTest {
	
	
	
	@Test
	public void testSum(){
		Person me = new Person("Mario",  35, "2.99");
		Person luca = new Person("Luca",  29, "1.00");
		Person biagio = new Person("Biagio",  39, "2.00");
		Person celestino = new Person("Celestino",  29, "3.00");
		List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);
		
		Assert.assertEquals((Integer)132, sum(meAndMyFriends, on(Person.class).getAge() ));
		
		
		Object o = on(Person.class).getMoney();
		Assert.assertEquals(new BigDecimal("8.99"), sum(meAndMyFriends, o));
		
		//List<Person> oldFriends = filter(having(on(Person.class).getAge(), greaterThan(30)), meAndMyFriends);
	}
}
