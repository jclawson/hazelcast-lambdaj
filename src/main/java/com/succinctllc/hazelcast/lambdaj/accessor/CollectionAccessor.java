package com.succinctllc.hazelcast.lambdaj.accessor;

import java.io.Serializable;
import java.util.Collection;

import com.succinctllc.hazelcast.lambdaj.AbstractTypeProvider;

public abstract class CollectionAccessor<T> extends
        AbstractTypeProvider<T, Collection<T>> implements Serializable {
    private static final long serialVersionUID = 1L;
}
