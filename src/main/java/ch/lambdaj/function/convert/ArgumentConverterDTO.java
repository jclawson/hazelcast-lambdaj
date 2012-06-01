package ch.lambdaj.function.convert;

import java.lang.reflect.Field;

import ch.lambdaj.function.argument.Argument;
import ch.lambdaj.function.argument.ArgumentDTO;

import com.succinctllc.hazelcast.lambdaj.Inflatable;

public class ArgumentConverterDTO<F, T> implements ConverterDTO<F, T>, Inflatable<ArgumentConverter<F, T>> {
	private static final long serialVersionUID = 1L;
	private ArgumentDTO<T> argument;
	
	public ArgumentConverterDTO(ArgumentConverter<F, T> arg){
		Field f;
		try {
			f = ArgumentConverter.class.getDeclaredField("argument");
			f.setAccessible(true);
			this.argument = new ArgumentDTO<T>((Argument<T>) f.get(arg));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ArgumentConverter<F, T> inflate() {
		return new ArgumentConverter<F, T>(argument.inflate());
	}

	public T convert(F from) {
		return inflate().convert(from);
	}
}
