/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.bytecode;

import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.bytecode.AttributeInfo;
import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.ByteArray;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import com.viaversion.viaversion.libs.javassist.bytecode.Descriptor;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignatureAttribute
extends AttributeInfo {
    public static final String tag = "Signature";

    SignatureAttribute(ConstPool cp, int n2, DataInputStream in) throws IOException {
        super(cp, n2, in);
    }

    public SignatureAttribute(ConstPool cp, String signature) {
        super(cp, tag);
        int index = cp.addUtf8Info(signature);
        byte[] bvalue = new byte[]{(byte)(index >>> 8), (byte)index};
        this.set(bvalue);
    }

    public String getSignature() {
        return this.getConstPool().getUtf8Info(ByteArray.readU16bit(this.get(), 0));
    }

    public void setSignature(String sig) {
        int index = this.getConstPool().addUtf8Info(sig);
        ByteArray.write16bit(index, this.info, 0);
    }

    @Override
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        return new SignatureAttribute(newCp, this.getSignature());
    }

    @Override
    void renameClass(String oldname, String newname) {
        String sig = SignatureAttribute.renameClass(this.getSignature(), oldname, newname);
        this.setSignature(sig);
    }

    @Override
    void renameClass(Map<String, String> classnames) {
        String sig = SignatureAttribute.renameClass(this.getSignature(), classnames);
        this.setSignature(sig);
    }

    static String renameClass(String desc, String oldname, String newname) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(oldname, newname);
        return SignatureAttribute.renameClass(desc, map);
    }

    static String renameClass(String desc, Map<String, String> map) {
        int j2;
        if (map == null) {
            return desc;
        }
        StringBuilder newdesc = new StringBuilder();
        int head = 0;
        int i2 = 0;
        while ((j2 = desc.indexOf(76, i2)) >= 0) {
            char c2;
            StringBuilder nameBuf = new StringBuilder();
            int k2 = j2;
            try {
                while ((c2 = desc.charAt(++k2)) != ';') {
                    nameBuf.append(c2);
                    if (c2 != '<') continue;
                    while ((c2 = desc.charAt(++k2)) != '>') {
                        nameBuf.append(c2);
                    }
                    nameBuf.append(c2);
                }
            }
            catch (IndexOutOfBoundsException e2) {
                break;
            }
            i2 = k2 + 1;
            String name = nameBuf.toString();
            String name2 = map.get(name);
            if (name2 == null) continue;
            newdesc.append(desc.substring(head, j2));
            newdesc.append('L');
            newdesc.append(name2);
            newdesc.append(c2);
            head = i2;
        }
        if (head == 0) {
            return desc;
        }
        int len = desc.length();
        if (head < len) {
            newdesc.append(desc.substring(head, len));
        }
        return newdesc.toString();
    }

    private static boolean isNamePart(int c2) {
        return c2 != 59 && c2 != 60;
    }

    public static ClassSignature toClassSignature(String sig) throws BadBytecode {
        try {
            return SignatureAttribute.parseSig(sig);
        }
        catch (IndexOutOfBoundsException e2) {
            throw SignatureAttribute.error(sig);
        }
    }

    public static MethodSignature toMethodSignature(String sig) throws BadBytecode {
        try {
            return SignatureAttribute.parseMethodSig(sig);
        }
        catch (IndexOutOfBoundsException e2) {
            throw SignatureAttribute.error(sig);
        }
    }

    public static ObjectType toFieldSignature(String sig) throws BadBytecode {
        try {
            return SignatureAttribute.parseObjectType(sig, new Cursor(), false);
        }
        catch (IndexOutOfBoundsException e2) {
            throw SignatureAttribute.error(sig);
        }
    }

    public static Type toTypeSignature(String sig) throws BadBytecode {
        try {
            return SignatureAttribute.parseType(sig, new Cursor());
        }
        catch (IndexOutOfBoundsException e2) {
            throw SignatureAttribute.error(sig);
        }
    }

    private static ClassSignature parseSig(String sig) throws BadBytecode, IndexOutOfBoundsException {
        Cursor cur = new Cursor();
        TypeParameter[] tp = SignatureAttribute.parseTypeParams(sig, cur);
        ClassType superClass = SignatureAttribute.parseClassType(sig, cur);
        int sigLen = sig.length();
        ArrayList<ClassType> ifArray = new ArrayList<ClassType>();
        while (cur.position < sigLen && sig.charAt(cur.position) == 'L') {
            ifArray.add(SignatureAttribute.parseClassType(sig, cur));
        }
        ClassType[] ifs = ifArray.toArray(new ClassType[ifArray.size()]);
        return new ClassSignature(tp, superClass, ifs);
    }

    private static MethodSignature parseMethodSig(String sig) throws BadBytecode {
        Cursor cur = new Cursor();
        TypeParameter[] tp = SignatureAttribute.parseTypeParams(sig, cur);
        if (sig.charAt(cur.position++) != '(') {
            throw SignatureAttribute.error(sig);
        }
        ArrayList<Type> params = new ArrayList<Type>();
        while (sig.charAt(cur.position) != ')') {
            Type t2 = SignatureAttribute.parseType(sig, cur);
            params.add(t2);
        }
        ++cur.position;
        Type ret = SignatureAttribute.parseType(sig, cur);
        int sigLen = sig.length();
        ArrayList<ObjectType> exceptions = new ArrayList<ObjectType>();
        while (cur.position < sigLen && sig.charAt(cur.position) == '^') {
            ++cur.position;
            ObjectType t3 = SignatureAttribute.parseObjectType(sig, cur, false);
            if (t3 instanceof ArrayType) {
                throw SignatureAttribute.error(sig);
            }
            exceptions.add(t3);
        }
        Type[] p2 = params.toArray(new Type[params.size()]);
        ObjectType[] ex = exceptions.toArray(new ObjectType[exceptions.size()]);
        return new MethodSignature(tp, p2, ret, ex);
    }

    private static TypeParameter[] parseTypeParams(String sig, Cursor cur) throws BadBytecode {
        ArrayList<TypeParameter> typeParam = new ArrayList<TypeParameter>();
        if (sig.charAt(cur.position) == '<') {
            ++cur.position;
            while (sig.charAt(cur.position) != '>') {
                int nameBegin = cur.position;
                int nameEnd = cur.indexOf(sig, 58);
                ObjectType classBound = SignatureAttribute.parseObjectType(sig, cur, true);
                ArrayList<ObjectType> ifBound = new ArrayList<ObjectType>();
                while (sig.charAt(cur.position) == ':') {
                    ++cur.position;
                    ObjectType t2 = SignatureAttribute.parseObjectType(sig, cur, false);
                    ifBound.add(t2);
                }
                TypeParameter p2 = new TypeParameter(sig, nameBegin, nameEnd, classBound, ifBound.toArray(new ObjectType[ifBound.size()]));
                typeParam.add(p2);
            }
            ++cur.position;
        }
        return typeParam.toArray(new TypeParameter[typeParam.size()]);
    }

    private static ObjectType parseObjectType(String sig, Cursor c2, boolean dontThrow) throws BadBytecode {
        int begin = c2.position;
        switch (sig.charAt(begin)) {
            case 'L': {
                return SignatureAttribute.parseClassType2(sig, c2, null);
            }
            case 'T': {
                int i2 = c2.indexOf(sig, 59);
                return new TypeVariable(sig, begin + 1, i2);
            }
            case '[': {
                return SignatureAttribute.parseArray(sig, c2);
            }
        }
        if (dontThrow) {
            return null;
        }
        throw SignatureAttribute.error(sig);
    }

    private static ClassType parseClassType(String sig, Cursor c2) throws BadBytecode {
        if (sig.charAt(c2.position) == 'L') {
            return SignatureAttribute.parseClassType2(sig, c2, null);
        }
        throw SignatureAttribute.error(sig);
    }

    private static ClassType parseClassType2(String sig, Cursor c2, ClassType parent) throws BadBytecode {
        TypeArgument[] targs;
        char t2;
        int start = ++c2.position;
        while ((t2 = sig.charAt(c2.position++)) != '$' && t2 != '<' && t2 != ';') {
        }
        int end = c2.position - 1;
        if (t2 == '<') {
            targs = SignatureAttribute.parseTypeArgs(sig, c2);
            t2 = sig.charAt(c2.position++);
        } else {
            targs = null;
        }
        ClassType thisClass = ClassType.make(sig, start, end, targs, parent);
        if (t2 == '$' || t2 == '.') {
            --c2.position;
            return SignatureAttribute.parseClassType2(sig, c2, thisClass);
        }
        return thisClass;
    }

    private static TypeArgument[] parseTypeArgs(String sig, Cursor c2) throws BadBytecode {
        char t2;
        ArrayList<TypeArgument> args = new ArrayList<TypeArgument>();
        while ((t2 = sig.charAt(c2.position++)) != '>') {
            TypeArgument ta;
            if (t2 == '*') {
                ta = new TypeArgument(null, '*');
            } else {
                if (t2 != '+' && t2 != '-') {
                    t2 = ' ';
                    --c2.position;
                }
                ta = new TypeArgument(SignatureAttribute.parseObjectType(sig, c2, false), t2);
            }
            args.add(ta);
        }
        return args.toArray(new TypeArgument[args.size()]);
    }

    private static ObjectType parseArray(String sig, Cursor c2) throws BadBytecode {
        int dim = 1;
        while (sig.charAt(++c2.position) == '[') {
            ++dim;
        }
        return new ArrayType(dim, SignatureAttribute.parseType(sig, c2));
    }

    private static Type parseType(String sig, Cursor c2) throws BadBytecode {
        Type t2 = SignatureAttribute.parseObjectType(sig, c2, true);
        if (t2 == null) {
            t2 = new BaseType(sig.charAt(c2.position++));
        }
        return t2;
    }

    private static BadBytecode error(String sig) {
        return new BadBytecode("bad signature: " + sig);
    }

    public static class TypeVariable
    extends ObjectType {
        String name;

        TypeVariable(String sig, int begin, int end) {
            this.name = sig.substring(begin, end);
        }

        public TypeVariable(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }

        @Override
        void encode(StringBuffer sb) {
            sb.append('T').append(this.name).append(';');
        }
    }

    public static class ArrayType
    extends ObjectType {
        int dim;
        Type componentType;

        public ArrayType(int d2, Type comp) {
            this.dim = d2;
            this.componentType = comp;
        }

        public int getDimension() {
            return this.dim;
        }

        public Type getComponentType() {
            return this.componentType;
        }

        public String toString() {
            StringBuffer sbuf = new StringBuffer(this.componentType.toString());
            for (int i2 = 0; i2 < this.dim; ++i2) {
                sbuf.append("[]");
            }
            return sbuf.toString();
        }

        @Override
        void encode(StringBuffer sb) {
            for (int i2 = 0; i2 < this.dim; ++i2) {
                sb.append('[');
            }
            this.componentType.encode(sb);
        }
    }

    public static class NestedClassType
    extends ClassType {
        ClassType parent;

        NestedClassType(String s2, int b2, int e2, TypeArgument[] targs, ClassType p2) {
            super(s2, b2, e2, targs);
            this.parent = p2;
        }

        public NestedClassType(ClassType parent, String className, TypeArgument[] args) {
            super(className, args);
            this.parent = parent;
        }

        @Override
        public ClassType getDeclaringClass() {
            return this.parent;
        }
    }

    public static class ClassType
    extends ObjectType {
        String name;
        TypeArgument[] arguments;
        public static ClassType OBJECT = new ClassType("java.lang.Object", null);

        static ClassType make(String s2, int b2, int e2, TypeArgument[] targs, ClassType parent) {
            if (parent == null) {
                return new ClassType(s2, b2, e2, targs);
            }
            return new NestedClassType(s2, b2, e2, targs, parent);
        }

        ClassType(String signature, int begin, int end, TypeArgument[] targs) {
            this.name = signature.substring(begin, end).replace('/', '.');
            this.arguments = targs;
        }

        public ClassType(String className, TypeArgument[] args) {
            this.name = className;
            this.arguments = args;
        }

        public ClassType(String className) {
            this(className, null);
        }

        public String getName() {
            return this.name;
        }

        public TypeArgument[] getTypeArguments() {
            return this.arguments;
        }

        public ClassType getDeclaringClass() {
            return null;
        }

        public String toString() {
            StringBuffer sbuf = new StringBuffer();
            ClassType parent = this.getDeclaringClass();
            if (parent != null) {
                sbuf.append(parent.toString()).append('.');
            }
            return this.toString2(sbuf);
        }

        private String toString2(StringBuffer sbuf) {
            sbuf.append(this.name);
            if (this.arguments != null) {
                sbuf.append('<');
                int n2 = this.arguments.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    if (i2 > 0) {
                        sbuf.append(", ");
                    }
                    sbuf.append(this.arguments[i2].toString());
                }
                sbuf.append('>');
            }
            return sbuf.toString();
        }

        @Override
        public String jvmTypeName() {
            StringBuffer sbuf = new StringBuffer();
            ClassType parent = this.getDeclaringClass();
            if (parent != null) {
                sbuf.append(parent.jvmTypeName()).append('$');
            }
            return this.toString2(sbuf);
        }

        @Override
        void encode(StringBuffer sb) {
            sb.append('L');
            this.encode2(sb);
            sb.append(';');
        }

        void encode2(StringBuffer sb) {
            ClassType parent = this.getDeclaringClass();
            if (parent != null) {
                parent.encode2(sb);
                sb.append('$');
            }
            sb.append(this.name.replace('.', '/'));
            if (this.arguments != null) {
                TypeArgument.encode(sb, this.arguments);
            }
        }
    }

    public static abstract class ObjectType
    extends Type {
        public String encode() {
            StringBuffer sb = new StringBuffer();
            this.encode(sb);
            return sb.toString();
        }
    }

    public static class BaseType
    extends Type {
        char descriptor;

        BaseType(char c2) {
            this.descriptor = c2;
        }

        public BaseType(String typeName) {
            this(Descriptor.of(typeName).charAt(0));
        }

        public char getDescriptor() {
            return this.descriptor;
        }

        public CtClass getCtlass() {
            return Descriptor.toPrimitiveClass(this.descriptor);
        }

        public String toString() {
            return Descriptor.toClassName(Character.toString(this.descriptor));
        }

        @Override
        void encode(StringBuffer sb) {
            sb.append(this.descriptor);
        }
    }

    public static abstract class Type {
        abstract void encode(StringBuffer var1);

        static void toString(StringBuffer sbuf, Type[] ts) {
            for (int i2 = 0; i2 < ts.length; ++i2) {
                if (i2 > 0) {
                    sbuf.append(", ");
                }
                sbuf.append(ts[i2]);
            }
        }

        public String jvmTypeName() {
            return this.toString();
        }
    }

    public static class TypeArgument {
        ObjectType arg;
        char wildcard;

        TypeArgument(ObjectType a2, char w2) {
            this.arg = a2;
            this.wildcard = w2;
        }

        public TypeArgument(ObjectType t2) {
            this(t2, ' ');
        }

        public TypeArgument() {
            this(null, '*');
        }

        public static TypeArgument subclassOf(ObjectType t2) {
            return new TypeArgument(t2, '+');
        }

        public static TypeArgument superOf(ObjectType t2) {
            return new TypeArgument(t2, '-');
        }

        public char getKind() {
            return this.wildcard;
        }

        public boolean isWildcard() {
            return this.wildcard != ' ';
        }

        public ObjectType getType() {
            return this.arg;
        }

        public String toString() {
            if (this.wildcard == '*') {
                return "?";
            }
            String type = this.arg.toString();
            if (this.wildcard == ' ') {
                return type;
            }
            if (this.wildcard == '+') {
                return "? extends " + type;
            }
            return "? super " + type;
        }

        static void encode(StringBuffer sb, TypeArgument[] args) {
            sb.append('<');
            for (int i2 = 0; i2 < args.length; ++i2) {
                TypeArgument ta = args[i2];
                if (ta.isWildcard()) {
                    sb.append(ta.wildcard);
                }
                if (ta.getType() == null) continue;
                ta.getType().encode(sb);
            }
            sb.append('>');
        }
    }

    public static class TypeParameter {
        String name;
        ObjectType superClass;
        ObjectType[] superInterfaces;

        TypeParameter(String sig, int nb, int ne, ObjectType sc, ObjectType[] si) {
            this.name = sig.substring(nb, ne);
            this.superClass = sc;
            this.superInterfaces = si;
        }

        public TypeParameter(String name, ObjectType superClass, ObjectType[] superInterfaces) {
            this.name = name;
            this.superClass = superClass;
            this.superInterfaces = superInterfaces == null ? new ObjectType[0] : superInterfaces;
        }

        public TypeParameter(String name) {
            this(name, null, null);
        }

        public String getName() {
            return this.name;
        }

        public ObjectType getClassBound() {
            return this.superClass;
        }

        public ObjectType[] getInterfaceBound() {
            return this.superInterfaces;
        }

        public String toString() {
            int len;
            StringBuffer sbuf = new StringBuffer(this.getName());
            if (this.superClass != null) {
                sbuf.append(" extends ").append(this.superClass.toString());
            }
            if ((len = this.superInterfaces.length) > 0) {
                for (int i2 = 0; i2 < len; ++i2) {
                    if (i2 > 0 || this.superClass != null) {
                        sbuf.append(" & ");
                    } else {
                        sbuf.append(" extends ");
                    }
                    sbuf.append(this.superInterfaces[i2].toString());
                }
            }
            return sbuf.toString();
        }

        static void toString(StringBuffer sbuf, TypeParameter[] tp) {
            sbuf.append('<');
            for (int i2 = 0; i2 < tp.length; ++i2) {
                if (i2 > 0) {
                    sbuf.append(", ");
                }
                sbuf.append(tp[i2]);
            }
            sbuf.append('>');
        }

        void encode(StringBuffer sb) {
            sb.append(this.name);
            if (this.superClass == null) {
                sb.append(":Ljava/lang/Object;");
            } else {
                sb.append(':');
                this.superClass.encode(sb);
            }
            for (int i2 = 0; i2 < this.superInterfaces.length; ++i2) {
                sb.append(':');
                this.superInterfaces[i2].encode(sb);
            }
        }
    }

    public static class MethodSignature {
        TypeParameter[] typeParams;
        Type[] params;
        Type retType;
        ObjectType[] exceptions;

        public MethodSignature(TypeParameter[] tp, Type[] params, Type ret, ObjectType[] ex) {
            this.typeParams = tp == null ? new TypeParameter[]{} : tp;
            this.params = params == null ? new Type[]{} : params;
            this.retType = ret == null ? new BaseType("void") : ret;
            this.exceptions = ex == null ? new ObjectType[]{} : ex;
        }

        public TypeParameter[] getTypeParameters() {
            return this.typeParams;
        }

        public Type[] getParameterTypes() {
            return this.params;
        }

        public Type getReturnType() {
            return this.retType;
        }

        public ObjectType[] getExceptionTypes() {
            return this.exceptions;
        }

        public String toString() {
            StringBuffer sbuf = new StringBuffer();
            TypeParameter.toString(sbuf, this.typeParams);
            sbuf.append(" (");
            Type.toString(sbuf, this.params);
            sbuf.append(") ");
            sbuf.append(this.retType);
            if (this.exceptions.length > 0) {
                sbuf.append(" throws ");
                Type.toString(sbuf, this.exceptions);
            }
            return sbuf.toString();
        }

        public String encode() {
            int i2;
            StringBuffer sbuf = new StringBuffer();
            if (this.typeParams.length > 0) {
                sbuf.append('<');
                for (i2 = 0; i2 < this.typeParams.length; ++i2) {
                    this.typeParams[i2].encode(sbuf);
                }
                sbuf.append('>');
            }
            sbuf.append('(');
            for (i2 = 0; i2 < this.params.length; ++i2) {
                this.params[i2].encode(sbuf);
            }
            sbuf.append(')');
            this.retType.encode(sbuf);
            if (this.exceptions.length > 0) {
                for (i2 = 0; i2 < this.exceptions.length; ++i2) {
                    sbuf.append('^');
                    this.exceptions[i2].encode(sbuf);
                }
            }
            return sbuf.toString();
        }
    }

    public static class ClassSignature {
        TypeParameter[] params;
        ClassType superClass;
        ClassType[] interfaces;

        public ClassSignature(TypeParameter[] params, ClassType superClass, ClassType[] interfaces) {
            this.params = params == null ? new TypeParameter[]{} : params;
            this.superClass = superClass == null ? ClassType.OBJECT : superClass;
            this.interfaces = interfaces == null ? new ClassType[]{} : interfaces;
        }

        public ClassSignature(TypeParameter[] p2) {
            this(p2, null, null);
        }

        public TypeParameter[] getParameters() {
            return this.params;
        }

        public ClassType getSuperClass() {
            return this.superClass;
        }

        public ClassType[] getInterfaces() {
            return this.interfaces;
        }

        public String toString() {
            StringBuffer sbuf = new StringBuffer();
            TypeParameter.toString(sbuf, this.params);
            sbuf.append(" extends ").append(this.superClass);
            if (this.interfaces.length > 0) {
                sbuf.append(" implements ");
                Type.toString(sbuf, this.interfaces);
            }
            return sbuf.toString();
        }

        public String encode() {
            int i2;
            StringBuffer sbuf = new StringBuffer();
            if (this.params.length > 0) {
                sbuf.append('<');
                for (i2 = 0; i2 < this.params.length; ++i2) {
                    this.params[i2].encode(sbuf);
                }
                sbuf.append('>');
            }
            this.superClass.encode(sbuf);
            for (i2 = 0; i2 < this.interfaces.length; ++i2) {
                this.interfaces[i2].encode(sbuf);
            }
            return sbuf.toString();
        }
    }

    private static class Cursor {
        int position = 0;

        private Cursor() {
        }

        int indexOf(String s2, int ch) throws BadBytecode {
            int i2 = s2.indexOf(ch, this.position);
            if (i2 < 0) {
                throw SignatureAttribute.error(s2);
            }
            this.position = i2 + 1;
            return i2;
        }
    }
}

