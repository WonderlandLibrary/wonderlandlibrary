// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import java.util.concurrent.atomic.AtomicLong;

public final class DebugKt
{
    private static final boolean ASSERTIONS_ENABLED;
    private static final boolean DEBUG;
    private static final boolean RECOVER_STACK_TRACES;
    private static final AtomicLong COROUTINE_ID;
    
    public static final boolean getASSERTIONS_ENABLED() {
        return DebugKt.ASSERTIONS_ENABLED;
    }
    
    public static final boolean getDEBUG() {
        return DebugKt.DEBUG;
    }
    
    public static final boolean getRECOVER_STACK_TRACES() {
        return DebugKt.RECOVER_STACK_TRACES;
    }
    
    public static final AtomicLong getCOROUTINE_ID() {
        return DebugKt.COROUTINE_ID;
    }
    
    static {
        ASSERTIONS_ENABLED = CoroutineId.class.desiredAssertionStatus();
        final String value;
        final String s = value = LockFreeTaskQueueCore.Companion.systemProp("kotlinx.coroutines.debug");
        boolean assertions_ENABLED = false;
        boolean b = false;
        Label_0171: {
            Label_0120: {
                if (s != null) {
                    final String s2 = s;
                    switch (s.hashCode()) {
                        case 0: {
                            if (s2.equals("")) {
                                break;
                            }
                            throw new IllegalStateException(("System property 'kotlinx.coroutines.debug' has unrecognized value '" + value + '\'').toString());
                        }
                        case 3005871: {
                            if (s2.equals("auto")) {
                                break Label_0120;
                            }
                            throw new IllegalStateException(("System property 'kotlinx.coroutines.debug' has unrecognized value '" + value + '\'').toString());
                        }
                        case 109935: {
                            if (s2.equals("off")) {
                                b = (assertions_ENABLED = false);
                                break Label_0171;
                            }
                            throw new IllegalStateException(("System property 'kotlinx.coroutines.debug' has unrecognized value '" + value + '\'').toString());
                        }
                        case 3551: {
                            if (s2.equals("on")) {
                                break;
                            }
                            throw new IllegalStateException(("System property 'kotlinx.coroutines.debug' has unrecognized value '" + value + '\'').toString());
                        }
                    }
                    b = (assertions_ENABLED = true);
                    break Label_0171;
                }
            }
            b = (assertions_ENABLED = DebugKt.ASSERTIONS_ENABLED);
        }
        DEBUG = assertions_ENABLED;
        RECOVER_STACK_TRACES = (b && LockFreeTaskQueueCore.Companion.systemProp("kotlinx.coroutines.stacktrace.recovery", true));
        COROUTINE_ID = new AtomicLong(0L);
    }
}
