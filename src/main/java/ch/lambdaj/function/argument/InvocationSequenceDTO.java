package ch.lambdaj.function.argument;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.succinctllc.hazelcast.lambdaj.Inflatable;

public class InvocationSequenceDTO implements Serializable,
        Inflatable<InvocationSequence> {
    private static final long serialVersionUID = 1L;

    private Class<?>          rootInvokedClass;
    private InvocationDTO     lastInvocation;

    public InvocationSequenceDTO(InvocationSequence invocationSequence) {
        try {
            this.rootInvokedClass = invocationSequence.getRootInvokedClass();
            Field f1 = InvocationSequence.class
                    .getDeclaredField("lastInvocation");
            f1.setAccessible(true);
            this.lastInvocation = new InvocationDTO(
                    (Invocation) f1.get(invocationSequence));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InvocationSequence inflate() {
        InvocationSequence seq = new InvocationSequence(rootInvokedClass);
        try {
            Field f = InvocationSequence.class
                    .getDeclaredField("lastInvocation");
            f.setAccessible(true);
            f.set(seq, lastInvocation.inflate());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return seq;
    }

}
