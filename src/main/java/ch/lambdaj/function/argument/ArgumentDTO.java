package ch.lambdaj.function.argument;

import java.io.Serializable;
import java.lang.reflect.Field;

import ch.lambdaj.function.convert.ArgumentConverter;

import com.succinctllc.hazelcast.lambdaj.Inflatable;

public class ArgumentDTO<T> implements Serializable, Inflatable<Argument<T>> {
	private static final long serialVersionUID = 1L;
	private InvocationSequenceDTO invocationSequence;

	public ArgumentDTO(Argument<T> argument) {
		Field f;
		try {
			f = Argument.class.getDeclaredField("invocationSequence");
			f.setAccessible(true);
			this.invocationSequence = new InvocationSequenceDTO((InvocationSequence) f.get(argument));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Argument<T> inflate(){
		return new Argument<T>(invocationSequence.inflate());
	}
}
