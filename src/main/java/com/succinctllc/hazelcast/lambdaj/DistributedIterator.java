package com.succinctllc.hazelcast.lambdaj;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import ch.lambdaj.function.convert.ConverterDTO;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.Member;

public class DistributedIterator<F, T, TYPE,VAL> implements Iterator<T> {
	private ConverterDTO<TYPE, T> converter;
	AbstractTypeProvider<TYPE,VAL> provider;
	Set<Member> members;
	Iterator<Member> memberIterator;
	
	
	//TODO: abstract this out into another class
	Iterator<TYPE> localValueIterator;
	
	public DistributedIterator(AbstractTypeProvider<TYPE,VAL> provider, ConverterDTO<TYPE, T> converter, Set<Member> members) {
		this.members = members;
		this.converter = converter;
		memberIterator = members.iterator();
		this.provider = provider;
	}
	
	public boolean hasNext() {
		return (localValueIterator != null && localValueIterator.hasNext()) 
				|| memberIterator.hasNext();
	}
	
	public TYPE localNext(){
		try {
			if(localValueIterator != null)
				return localValueIterator.next();
			else
				return null;
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	private VAL fetchVal(){
		try {
			Member member = memberIterator.next();
			Future<VAL> future = Hazelcast.getExecutorService().submit(new FetchTask<TYPE,VAL>(provider));
			return future.get();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	public T next() {
		TYPE type = localNext();
		if(type == null) {
			while(type == null && hasNext()) {
				VAL value = fetchVal();
				if(value instanceof Collection) {
					localValueIterator = ((Collection<TYPE>)value).iterator();
					if(localValueIterator.hasNext())
						type = localValueIterator.next();
				} else {
					return converter.convert((TYPE) value);
				}
			}
		}
		if(type != null)
			return converter.convert(type);
		else
			return null;
	}
	
	public void remove() {
		throw new RuntimeException("Not implemented!");
	}
	
	public static class FetchTask<TYPE, VAL> implements Callable<VAL>, Serializable {
		private static final long serialVersionUID = 1L;
		private AbstractTypeProvider<TYPE, VAL> provider;
		
		public FetchTask(AbstractTypeProvider<TYPE, VAL> provider){
			this.provider = provider;
		}

		public VAL call() throws Exception {
			return provider.get();
		}
	}
	
	public static class ConvertTask<F, T> implements Callable<T>, Serializable {
		private ConverterDTO<F, T> converter;
		private AbstractTypeProvider<?,F> provider;
		
		public ConvertTask(ConverterDTO<F, T> converter, AbstractTypeProvider<?,F> provider){
			this.converter = converter;
			this.provider = provider;
		}

		public T call() throws Exception {
			return this.converter.convert(provider.get());
		}
	}
}
