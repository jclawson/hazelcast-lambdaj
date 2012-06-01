package com.succinctllc.hazelcast.lambdaj.accessor;

import java.io.Serializable;

import com.succinctllc.hazelcast.lambdaj.AbstractTypeProvider;

public abstract class ObjectAccessor<T> extends AbstractTypeProvider<T, T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public abstract T get();
}
