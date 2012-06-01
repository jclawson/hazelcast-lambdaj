package com.succinctllc.hazelcast.lambdaj;

import java.io.Serializable;
import java.util.Collection;

public abstract class CollectionProvider<T> extends AbstractTypeProvider<T, Collection<T>> implements Serializable{
	private static final long serialVersionUID = 1L;
}
