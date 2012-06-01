package com.succinctllc.hazelcast.lambdaj;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractTypeProvider<TYPE, VAL> {
private Class<TYPE> cls;
	
	@SuppressWarnings("unchecked")
	public AbstractTypeProvider(){
		Class<?> clazz = getClass();
	    while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
	        clazz = clazz.getSuperclass();
	        if(clazz == null)
	        	throw new RuntimeException("Unable to determine managed class, did you forget to provide the T template parameter to AbstractDao?");
	    }


	    Type type = ((ParameterizedType) clazz.getGenericSuperclass())
    		.getActualTypeArguments()[0];

	    cls = (Class<TYPE>) (type instanceof ParameterizedType
	    	? ((ParameterizedType) type).getRawType()
	    	: type);
	}
	
	public Class<TYPE> getProvidedClass(){
		return cls;
	}
	
	public abstract VAL get();
}
