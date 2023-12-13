/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import java.lang.reflect.Method;

abstract class VarArgsChecker {
    private VarArgsChecker() {
    }

    static VarArgsChecker create() {
        try {
            Method isVarArgsMethod = Method.class.getMethod("isVarArgs", new Class[0]);
            if (isVarArgsMethod != null) {
                return new RealVarArgsChecker();
            }
            return new NoVarArgsChecker();
        }
        catch (NoSuchMethodException e2) {
            return new NoVarArgsChecker();
        }
        catch (SecurityException e3) {
            return new NoVarArgsChecker();
        }
    }

    abstract boolean isVarArgs(Method var1);

    abstract int fixedArgs(Method var1);

    private static final class NoVarArgsChecker
    extends VarArgsChecker {
        private NoVarArgsChecker() {
        }

        @Override
        boolean isVarArgs(Method m2) {
            return false;
        }

        @Override
        int fixedArgs(Method m2) {
            return 0;
        }
    }

    private static final class RealVarArgsChecker
    extends VarArgsChecker {
        private RealVarArgsChecker() {
        }

        @Override
        boolean isVarArgs(Method m2) {
            return m2.isVarArgs();
        }

        @Override
        int fixedArgs(Method m2) {
            return m2.isVarArgs() ? m2.getParameterTypes().length - 1 : 0;
        }
    }
}

