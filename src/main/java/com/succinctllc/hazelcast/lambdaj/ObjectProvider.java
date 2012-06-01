package com.succinctllc.hazelcast.lambdaj;

import java.io.Serializable;

public abstract class ObjectProvider<T> extends AbstractTypeProvider<T, T> implements Serializable{
	private static final long serialVersionUID = 1L;
	public abstract T get();
}
