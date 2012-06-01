import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import com.hazelcast.core.Hazelcast;
import com.succinctllc.hazelcast.lambdaj.CollectionProvider;
import com.succinctllc.hazelcast.lambdaj.HazelcastLambdaj;
import com.succinctllc.hazelcast.lambdaj.ObjectProvider;

public class HC1 {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		Hazelcast.getMap("test");
		
		Thread.sleep(10000);
		
		BigDecimal o = on(Person.class).getMoney();
		//System.out.println(HazelcastLambdaj.sum(new MyCollectionProvider(), o));
		
		int i =0;
		for(BigDecimal b : HazelcastLambdaj.collect(new MyCollectionProvider(), o)) {
			System.out.println(++i+": "+b);
		}
		
	}
	
	
	public static class MyProvider extends ObjectProvider<Person> {

		@Override
		public Person get() {
			return Test.getPerson();
		}
	}
	
	public static class MyCollectionProvider extends CollectionProvider<Person> {

		@Override
		public Collection<Person> get() {
			return Test.getPeople();
		}
	}
	
	
	public static class Test {
		public static Person person = new Person("Jason", 28, "1.22");
		public static Person getPerson(){
			return person;
		}
		
		public static Collection<Person> getPeople() {
			Person me = new Person("Mario",  35, "2.99");
			Person luca = new Person("Luca",  29, "1.00");
			Person biagio = new Person("Biagio",  39, "2.00");
			Person celestino = new Person("Celestino",  29, "3.00");
			return asList(me, luca, biagio, celestino);
		}
	}
	
	public static class Person implements Serializable {
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

}
