package com.succinctllc.hazelcast.lambdaj;

import static ch.lambdaj.Lambda.aggregate;
import static ch.lambdaj.util.iterator.IteratorFactory.asIterator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ch.lambdaj.function.aggregate.Aggregator;
import ch.lambdaj.function.aggregate.Sum;
import ch.lambdaj.function.aggregate.SumDouble;
import ch.lambdaj.function.aggregate.SumInteger;
import ch.lambdaj.function.aggregate.SumLong;
import ch.lambdaj.function.convert.ArgumentConverter;
import ch.lambdaj.function.convert.ArgumentConverterDTO;
import ch.lambdaj.function.convert.ConverterDTO;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.Member;

public class HazelcastLambdaj {
		
	public static <T, TYPE, VAL> T sum(AbstractTypeProvider<TYPE,VAL> accessor, T argument) {
        Set<Member> members = Hazelcast.getCluster().getMembers();
        ArgumentConverter<TYPE, T> converter = new ArgumentConverter<TYPE, T>(argument);
        ArgumentConverterDTO<TYPE, T> converterDto = new ArgumentConverterDTO<TYPE, T>(converter);
        
        return (T)typedSum(convertIterator(accessor, members, converterDto), argument.getClass());
	}
	
	public static <F, T, TYPE, VAL> Iterator<T> convertIterator(AbstractTypeProvider<TYPE,VAL> accessor, Set<Member> members, ConverterDTO<TYPE, T> converter) {
        return new DistributedIterator<F,T,TYPE,VAL>(accessor, converter, members);
    }
	
	public static <T, TYPE, VAL> List<T> collect(AbstractTypeProvider<TYPE,VAL> accessor, T argument) {
		Set<Member> members = Hazelcast.getCluster().getMembers();
		ArgumentConverter<TYPE, T> converter = new ArgumentConverter<TYPE, T>(argument);
        ArgumentConverterDTO<TYPE, T> converterDto = new ArgumentConverterDTO<TYPE, T>(converter);
		
        Iterator<T> it = convertIterator(accessor, members, converterDto);
        List<T> result = new LinkedList<T>();
        while(it.hasNext()) {
        	result.add(it.next());
        }
		return result;
	}
	
	private static Number typedSum(Object iterable, Class<?> numberClass) {
        if (iterable instanceof Number) return (Number)iterable;
        Iterator<?> iterator = asIterator(iterable);
        return iterator.hasNext() ? aggregate(iterator, getSumAggregator(iterator.next())) : typedZero(numberClass);
    }
	
	private static Aggregator<? extends Number> getSumAggregator(Object object) {
		if (object instanceof Integer) return new SumInteger((Integer)object);
		if (object instanceof Double) return new SumDouble((Double)object);
		if (object instanceof Long) return new SumLong((Long)object);
		return new Sum((Number)object);
	}
	
	private static Number typedZero(Class<?> numberClass) {
        if (numberClass == Double.class) return 0.0;
        if (numberClass == Float.class) return 0.0f;
        if (BigInteger.class.isAssignableFrom(numberClass)) return BigInteger.ZERO;
        if (BigDecimal.class.isAssignableFrom(numberClass)) return BigDecimal.ZERO;
        return 0;
    }
	

}
