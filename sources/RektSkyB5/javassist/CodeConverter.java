/*
 * Decompiled with CFR 0.152.
 */
package javassist;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.convert.TransformAccessArrayField;
import javassist.convert.TransformAfter;
import javassist.convert.TransformBefore;
import javassist.convert.TransformCall;
import javassist.convert.TransformCallToStatic;
import javassist.convert.TransformFieldAccess;
import javassist.convert.TransformNew;
import javassist.convert.TransformNewClass;
import javassist.convert.TransformReadField;
import javassist.convert.TransformWriteField;
import javassist.convert.Transformer;

public class CodeConverter {
    protected Transformer transformers = null;

    public void replaceNew(CtClass newClass, CtClass calledClass, String calledMethod) {
        this.transformers = new TransformNew(this.transformers, newClass.getName(), calledClass.getName(), calledMethod);
    }

    public void replaceNew(CtClass oldClass, CtClass newClass) {
        this.transformers = new TransformNewClass(this.transformers, oldClass.getName(), newClass.getName());
    }

    public void redirectFieldAccess(CtField field, CtClass newClass, String newFieldname) {
        this.transformers = new TransformFieldAccess(this.transformers, field, newClass.getName(), newFieldname);
    }

    public void replaceFieldRead(CtField field, CtClass calledClass, String calledMethod) {
        this.transformers = new TransformReadField(this.transformers, field, calledClass.getName(), calledMethod);
    }

    public void replaceFieldWrite(CtField field, CtClass calledClass, String calledMethod) {
        this.transformers = new TransformWriteField(this.transformers, field, calledClass.getName(), calledMethod);
    }

    public void replaceArrayAccess(CtClass calledClass, ArrayAccessReplacementMethodNames names) throws NotFoundException {
        this.transformers = new TransformAccessArrayField(this.transformers, calledClass.getName(), names);
    }

    public void redirectMethodCall(CtMethod origMethod, CtMethod substMethod) throws CannotCompileException {
        String d2;
        String d1 = origMethod.getMethodInfo2().getDescriptor();
        if (!d1.equals(d2 = substMethod.getMethodInfo2().getDescriptor())) {
            throw new CannotCompileException("signature mismatch: " + substMethod.getLongName());
        }
        int mod1 = origMethod.getModifiers();
        int mod2 = substMethod.getModifiers();
        if (Modifier.isStatic(mod1) != Modifier.isStatic(mod2) || Modifier.isPrivate(mod1) && !Modifier.isPrivate(mod2) || origMethod.getDeclaringClass().isInterface() != substMethod.getDeclaringClass().isInterface()) {
            throw new CannotCompileException("invoke-type mismatch " + substMethod.getLongName());
        }
        this.transformers = new TransformCall(this.transformers, origMethod, substMethod);
    }

    public void redirectMethodCall(String oldMethodName, CtMethod newMethod) throws CannotCompileException {
        this.transformers = new TransformCall(this.transformers, oldMethodName, newMethod);
    }

    public void redirectMethodCallToStatic(CtMethod origMethod, CtMethod staticMethod) {
        this.transformers = new TransformCallToStatic(this.transformers, origMethod, staticMethod);
    }

    public void insertBeforeMethod(CtMethod origMethod, CtMethod beforeMethod) throws CannotCompileException {
        try {
            this.transformers = new TransformBefore(this.transformers, origMethod, beforeMethod);
        }
        catch (NotFoundException e2) {
            throw new CannotCompileException(e2);
        }
    }

    public void insertAfterMethod(CtMethod origMethod, CtMethod afterMethod) throws CannotCompileException {
        try {
            this.transformers = new TransformAfter(this.transformers, origMethod, afterMethod);
        }
        catch (NotFoundException e2) {
            throw new CannotCompileException(e2);
        }
    }

    protected void doit(CtClass clazz, MethodInfo minfo, ConstPool cp) throws CannotCompileException {
        Transformer t2;
        CodeAttribute codeAttr = minfo.getCodeAttribute();
        if (codeAttr == null || this.transformers == null) {
            return;
        }
        for (t2 = this.transformers; t2 != null; t2 = t2.getNext()) {
            t2.initialize(cp, clazz, minfo);
        }
        CodeIterator iterator = codeAttr.iterator();
        while (iterator.hasNext()) {
            try {
                int pos = iterator.next();
                for (t2 = this.transformers; t2 != null; t2 = t2.getNext()) {
                    pos = t2.transform(clazz, pos, iterator, cp);
                }
            }
            catch (BadBytecode e2) {
                throw new CannotCompileException(e2);
            }
        }
        int locals = 0;
        int stack = 0;
        for (t2 = this.transformers; t2 != null; t2 = t2.getNext()) {
            int s2 = t2.extraLocals();
            if (s2 > locals) {
                locals = s2;
            }
            if ((s2 = t2.extraStack()) <= stack) continue;
            stack = s2;
        }
        for (t2 = this.transformers; t2 != null; t2 = t2.getNext()) {
            t2.clean();
        }
        if (locals > 0) {
            codeAttr.setMaxLocals(codeAttr.getMaxLocals() + locals);
        }
        if (stack > 0) {
            codeAttr.setMaxStack(codeAttr.getMaxStack() + stack);
        }
        try {
            minfo.rebuildStackMapIf6(clazz.getClassPool(), clazz.getClassFile2());
        }
        catch (BadBytecode b2) {
            throw new CannotCompileException(b2.getMessage(), b2);
        }
    }

    public static class DefaultArrayAccessReplacementMethodNames
    implements ArrayAccessReplacementMethodNames {
        @Override
        public String byteOrBooleanRead() {
            return "arrayReadByteOrBoolean";
        }

        @Override
        public String byteOrBooleanWrite() {
            return "arrayWriteByteOrBoolean";
        }

        @Override
        public String charRead() {
            return "arrayReadChar";
        }

        @Override
        public String charWrite() {
            return "arrayWriteChar";
        }

        @Override
        public String doubleRead() {
            return "arrayReadDouble";
        }

        @Override
        public String doubleWrite() {
            return "arrayWriteDouble";
        }

        @Override
        public String floatRead() {
            return "arrayReadFloat";
        }

        @Override
        public String floatWrite() {
            return "arrayWriteFloat";
        }

        @Override
        public String intRead() {
            return "arrayReadInt";
        }

        @Override
        public String intWrite() {
            return "arrayWriteInt";
        }

        @Override
        public String longRead() {
            return "arrayReadLong";
        }

        @Override
        public String longWrite() {
            return "arrayWriteLong";
        }

        @Override
        public String objectRead() {
            return "arrayReadObject";
        }

        @Override
        public String objectWrite() {
            return "arrayWriteObject";
        }

        @Override
        public String shortRead() {
            return "arrayReadShort";
        }

        @Override
        public String shortWrite() {
            return "arrayWriteShort";
        }
    }

    public static interface ArrayAccessReplacementMethodNames {
        public String byteOrBooleanRead();

        public String byteOrBooleanWrite();

        public String charRead();

        public String charWrite();

        public String doubleRead();

        public String doubleWrite();

        public String floatRead();

        public String floatWrite();

        public String intRead();

        public String intWrite();

        public String longRead();

        public String longWrite();

        public String objectRead();

        public String objectWrite();

        public String shortRead();

        public String shortWrite();
    }
}

