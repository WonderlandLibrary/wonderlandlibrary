/*
 * Decompiled with CFR 0.152.
 */
package javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;

public interface Translator {
    public void start(ClassPool var1) throws NotFoundException, CannotCompileException;

    public void onLoad(ClassPool var1, String var2) throws NotFoundException, CannotCompileException;
}

