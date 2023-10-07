/*
 * Decompiled with CFR 0.152.
 */
package org.spongepowered.asm.mixin.injection.throwables;

import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

public class InvalidInjectionPointException
extends InvalidInjectionException {
    private static final long serialVersionUID = 2L;

    public InvalidInjectionPointException(IMixinContext context, String format, Object ... args2) {
        super(context, String.format(format, args2));
    }

    public InvalidInjectionPointException(InjectionInfo info, String format, Object ... args2) {
        super(info, String.format(format, args2));
    }

    public InvalidInjectionPointException(IMixinContext context, Throwable cause, String format, Object ... args2) {
        super(context, String.format(format, args2), cause);
    }

    public InvalidInjectionPointException(InjectionInfo info, Throwable cause, String format, Object ... args2) {
        super(info, String.format(format, args2), cause);
    }
}

