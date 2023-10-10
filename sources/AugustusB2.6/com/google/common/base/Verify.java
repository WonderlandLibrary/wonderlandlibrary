// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Verify
{
    public static void verify(final boolean expression) {
        if (!expression) {
            throw new VerifyException();
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, @CheckForNull final Object... errorMessageArgs) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final char p1) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final int p1) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final long p1) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, @CheckForNull final Object p1) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final char p1, final char p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final int p1, final char p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final long p1, final char p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, @CheckForNull final Object p1, final char p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final char p1, final int p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final int p1, final int p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final long p1, final int p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, @CheckForNull final Object p1, final int p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final char p1, final long p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final int p1, final long p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final long p1, final long p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, @CheckForNull final Object p1, final long p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final char p1, @CheckForNull final Object p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final int p1, @CheckForNull final Object p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, final long p1, @CheckForNull final Object p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2, @CheckForNull final Object p3) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3));
        }
    }
    
    public static void verify(final boolean expression, final String errorMessageTemplate, @CheckForNull final Object p1, @CheckForNull final Object p2, @CheckForNull final Object p3, @CheckForNull final Object p4) {
        if (!expression) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, p1, p2, p3, p4));
        }
    }
    
    @CanIgnoreReturnValue
    public static <T> T verifyNotNull(@CheckForNull final T reference) {
        return verifyNotNull(reference, "expected a non-null reference", new Object[0]);
    }
    
    @CanIgnoreReturnValue
    public static <T> T verifyNotNull(@CheckForNull final T reference, final String errorMessageTemplate, @CheckForNull final Object... errorMessageArgs) {
        if (reference == null) {
            throw new VerifyException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }
    
    private Verify() {
    }
}
