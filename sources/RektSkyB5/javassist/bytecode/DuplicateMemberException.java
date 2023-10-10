/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode;

import javassist.CannotCompileException;

public class DuplicateMemberException
extends CannotCompileException {
    private static final long serialVersionUID = 1L;

    public DuplicateMemberException(String msg) {
        super(msg);
    }
}

