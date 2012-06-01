package ch.lambdaj.function.argument;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.succinctllc.hazelcast.lambdaj.Inflatable;

public class InvocationDTO implements Serializable, Inflatable<Invocation> {
    private static final long serialVersionUID = 1L;

    private Class<?>          invokedClass;

    private String            invokeMethodName;
    private Class<?>[]        invokeMethodParamTypes;

    // Invocation previousInvocation;
    private Object[]          args;

    // TODO: verify that Object[] args are serializable!
    public InvocationDTO(Invocation invocation) {
        try {
            this.invokedClass = invocation.getInvokedClass();
            Method invokedMethod = invocation.getInvokedMethod();
            invokeMethodName = invokedMethod.getName();
            invokeMethodParamTypes = invokedMethod.getParameterTypes();

            Method m1 = Invocation.class.getDeclaredMethod("getConcreteArgs",
                    null);
            m1.setAccessible(true);
            this.args = (Object[]) m1.invoke(invocation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Invocation inflate() {
        try {
            Method invokedMethod = invokedClass.getMethod(invokeMethodName,
                    invokeMethodParamTypes);
            return new Invocation(invokedClass, invokedMethod, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
