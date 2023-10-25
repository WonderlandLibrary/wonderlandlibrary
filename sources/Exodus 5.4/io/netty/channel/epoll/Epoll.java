/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.channel.epoll.Native;

public final class Epoll {
    private static final Throwable UNAVAILABILITY_CAUSE;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static {
        Throwable throwable;
        block17: {
            int n;
            block16: {
                throwable = null;
                int n2 = -1;
                n = -1;
                try {
                    n2 = Native.epollCreate();
                    n = Native.eventFd();
                    if (n2 == -1) break block16;
                }
                catch (Throwable throwable2) {
                    throwable = throwable2;
                    if (n2 != -1) {
                        try {
                            Native.close(n2);
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                    if (n != -1) {
                        try {
                            Native.close(n);
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                    break block17;
                }
                try {
                    Native.close(n2);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (n != -1) {
                try {
                    Native.close(n);
                }
                catch (Exception exception) {}
            }
        }
        if (throwable != null) {
            UNAVAILABILITY_CAUSE = throwable;
            return;
        }
        UNAVAILABILITY_CAUSE = null;
    }

    public static boolean isAvailable() {
        return UNAVAILABILITY_CAUSE == null;
    }

    private Epoll() {
    }

    public static void ensureAvailability() {
        if (UNAVAILABILITY_CAUSE != null) {
            throw (Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE);
        }
    }

    public static Throwable unavailabilityCause() {
        return UNAVAILABILITY_CAUSE;
    }
}

