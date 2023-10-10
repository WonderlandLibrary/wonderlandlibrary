/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode;

import java.util.Map;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;

public class Descriptor {
    public static String toJvmName(String classname) {
        return classname.replace('.', '/');
    }

    public static String toJavaName(String classname) {
        return classname.replace('/', '.');
    }

    public static String toJvmName(CtClass clazz) {
        if (clazz.isArray()) {
            return Descriptor.of(clazz);
        }
        return Descriptor.toJvmName(clazz.getName());
    }

    public static String toClassName(String descriptor) {
        String name;
        int arrayDim = 0;
        int i2 = 0;
        char c2 = descriptor.charAt(0);
        while (c2 == '[') {
            ++arrayDim;
            c2 = descriptor.charAt(++i2);
        }
        if (c2 == 'L') {
            int i22 = descriptor.indexOf(59, i2++);
            name = descriptor.substring(i2, i22).replace('/', '.');
            i2 = i22;
        } else if (c2 == 'V') {
            name = "void";
        } else if (c2 == 'I') {
            name = "int";
        } else if (c2 == 'B') {
            name = "byte";
        } else if (c2 == 'J') {
            name = "long";
        } else if (c2 == 'D') {
            name = "double";
        } else if (c2 == 'F') {
            name = "float";
        } else if (c2 == 'C') {
            name = "char";
        } else if (c2 == 'S') {
            name = "short";
        } else if (c2 == 'Z') {
            name = "boolean";
        } else {
            throw new RuntimeException("bad descriptor: " + descriptor);
        }
        if (i2 + 1 != descriptor.length()) {
            throw new RuntimeException("multiple descriptors?: " + descriptor);
        }
        if (arrayDim == 0) {
            return name;
        }
        StringBuffer sbuf = new StringBuffer(name);
        do {
            sbuf.append("[]");
        } while (--arrayDim > 0);
        return sbuf.toString();
    }

    public static String of(String classname) {
        if (classname.equals("void")) {
            return "V";
        }
        if (classname.equals("int")) {
            return "I";
        }
        if (classname.equals("byte")) {
            return "B";
        }
        if (classname.equals("long")) {
            return "J";
        }
        if (classname.equals("double")) {
            return "D";
        }
        if (classname.equals("float")) {
            return "F";
        }
        if (classname.equals("char")) {
            return "C";
        }
        if (classname.equals("short")) {
            return "S";
        }
        if (classname.equals("boolean")) {
            return "Z";
        }
        return "L" + Descriptor.toJvmName(classname) + ";";
    }

    public static String rename(String desc, String oldname, String newname) {
        int j2;
        if (desc.indexOf(oldname) < 0) {
            return desc;
        }
        StringBuffer newdesc = new StringBuffer();
        int head = 0;
        int i2 = 0;
        while ((j2 = desc.indexOf(76, i2)) >= 0) {
            if (desc.startsWith(oldname, j2 + 1) && desc.charAt(j2 + oldname.length() + 1) == ';') {
                newdesc.append(desc.substring(head, j2));
                newdesc.append('L');
                newdesc.append(newname);
                newdesc.append(';');
                head = i2 = j2 + oldname.length() + 2;
                continue;
            }
            i2 = desc.indexOf(59, j2) + 1;
            if (i2 >= 1) continue;
            break;
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

    public static String rename(String desc, Map<String, String> map) {
        int k2;
        int j2;
        if (map == null) {
            return desc;
        }
        StringBuffer newdesc = new StringBuffer();
        int head = 0;
        int i2 = 0;
        while ((j2 = desc.indexOf(76, i2)) >= 0 && (k2 = desc.indexOf(59, j2)) >= 0) {
            i2 = k2 + 1;
            String name = desc.substring(j2 + 1, k2);
            String name2 = map.get(name);
            if (name2 == null) continue;
            newdesc.append(desc.substring(head, j2));
            newdesc.append('L');
            newdesc.append(name2);
            newdesc.append(';');
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

    public static String of(CtClass type) {
        StringBuffer sbuf = new StringBuffer();
        Descriptor.toDescriptor(sbuf, type);
        return sbuf.toString();
    }

    private static void toDescriptor(StringBuffer desc, CtClass type) {
        if (type.isArray()) {
            desc.append('[');
            try {
                Descriptor.toDescriptor(desc, type.getComponentType());
            }
            catch (NotFoundException e2) {
                desc.append('L');
                String name = type.getName();
                desc.append(Descriptor.toJvmName(name.substring(0, name.length() - 2)));
                desc.append(';');
            }
        } else if (type.isPrimitive()) {
            CtPrimitiveType pt = (CtPrimitiveType)type;
            desc.append(pt.getDescriptor());
        } else {
            desc.append('L');
            desc.append(type.getName().replace('.', '/'));
            desc.append(';');
        }
    }

    public static String ofConstructor(CtClass[] paramTypes) {
        return Descriptor.ofMethod(CtClass.voidType, paramTypes);
    }

    public static String ofMethod(CtClass returnType, CtClass[] paramTypes) {
        StringBuffer desc = new StringBuffer();
        desc.append('(');
        if (paramTypes != null) {
            int n2 = paramTypes.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                Descriptor.toDescriptor(desc, paramTypes[i2]);
            }
        }
        desc.append(')');
        if (returnType != null) {
            Descriptor.toDescriptor(desc, returnType);
        }
        return desc.toString();
    }

    public static String ofParameters(CtClass[] paramTypes) {
        return Descriptor.ofMethod(null, paramTypes);
    }

    public static String appendParameter(String classname, String desc) {
        int i2 = desc.indexOf(41);
        if (i2 < 0) {
            return desc;
        }
        StringBuffer newdesc = new StringBuffer();
        newdesc.append(desc.substring(0, i2));
        newdesc.append('L');
        newdesc.append(classname.replace('.', '/'));
        newdesc.append(';');
        newdesc.append(desc.substring(i2));
        return newdesc.toString();
    }

    public static String insertParameter(String classname, String desc) {
        if (desc.charAt(0) != '(') {
            return desc;
        }
        return "(L" + classname.replace('.', '/') + ';' + desc.substring(1);
    }

    public static String appendParameter(CtClass type, String descriptor) {
        int i2 = descriptor.indexOf(41);
        if (i2 < 0) {
            return descriptor;
        }
        StringBuffer newdesc = new StringBuffer();
        newdesc.append(descriptor.substring(0, i2));
        Descriptor.toDescriptor(newdesc, type);
        newdesc.append(descriptor.substring(i2));
        return newdesc.toString();
    }

    public static String insertParameter(CtClass type, String descriptor) {
        if (descriptor.charAt(0) != '(') {
            return descriptor;
        }
        return "(" + Descriptor.of(type) + descriptor.substring(1);
    }

    public static String changeReturnType(String classname, String desc) {
        int i2 = desc.indexOf(41);
        if (i2 < 0) {
            return desc;
        }
        StringBuffer newdesc = new StringBuffer();
        newdesc.append(desc.substring(0, i2 + 1));
        newdesc.append('L');
        newdesc.append(classname.replace('.', '/'));
        newdesc.append(';');
        return newdesc.toString();
    }

    public static CtClass[] getParameterTypes(String desc, ClassPool cp) throws NotFoundException {
        if (desc.charAt(0) != '(') {
            return null;
        }
        int num = Descriptor.numOfParameters(desc);
        CtClass[] args = new CtClass[num];
        int n2 = 0;
        int i2 = 1;
        while ((i2 = Descriptor.toCtClass(cp, desc, i2, args, n2++)) > 0) {
        }
        return args;
    }

    public static boolean eqParamTypes(String desc1, String desc2) {
        if (desc1.charAt(0) != '(') {
            return false;
        }
        int i2 = 0;
        char c2;
        while ((c2 = desc1.charAt(i2)) == desc2.charAt(i2)) {
            if (c2 == ')') {
                return true;
            }
            ++i2;
        }
        return false;
    }

    public static String getParamDescriptor(String decl) {
        return decl.substring(0, decl.indexOf(41) + 1);
    }

    public static CtClass getReturnType(String desc, ClassPool cp) throws NotFoundException {
        int i2 = desc.indexOf(41);
        if (i2 < 0) {
            return null;
        }
        CtClass[] type = new CtClass[1];
        Descriptor.toCtClass(cp, desc, i2 + 1, type, 0);
        return type[0];
    }

    public static int numOfParameters(String desc) {
        char c2;
        int n2 = 0;
        int i2 = 1;
        while ((c2 = desc.charAt(i2)) != ')') {
            while (c2 == '[') {
                c2 = desc.charAt(++i2);
            }
            if (c2 == 'L') {
                if ((i2 = desc.indexOf(59, i2) + 1) <= 0) {
                    throw new IndexOutOfBoundsException("bad descriptor");
                }
            } else {
                ++i2;
            }
            ++n2;
        }
        return n2;
    }

    public static CtClass toCtClass(String desc, ClassPool cp) throws NotFoundException {
        CtClass[] clazz = new CtClass[1];
        int res = Descriptor.toCtClass(cp, desc, 0, clazz, 0);
        if (res >= 0) {
            return clazz[0];
        }
        return cp.get(desc.replace('/', '.'));
    }

    private static int toCtClass(ClassPool cp, String desc, int i2, CtClass[] args, int n2) throws NotFoundException {
        String name;
        int i22;
        int arrayDim = 0;
        char c2 = desc.charAt(i2);
        while (c2 == '[') {
            ++arrayDim;
            c2 = desc.charAt(++i2);
        }
        if (c2 == 'L') {
            i22 = desc.indexOf(59, ++i2);
            name = desc.substring(i2, i22++).replace('/', '.');
        } else {
            CtClass type = Descriptor.toPrimitiveClass(c2);
            if (type == null) {
                return -1;
            }
            i22 = i2 + 1;
            if (arrayDim == 0) {
                args[n2] = type;
                return i22;
            }
            name = type.getName();
        }
        if (arrayDim > 0) {
            StringBuffer sbuf = new StringBuffer(name);
            while (arrayDim-- > 0) {
                sbuf.append("[]");
            }
            name = sbuf.toString();
        }
        args[n2] = cp.get(name);
        return i22;
    }

    static CtClass toPrimitiveClass(char c2) {
        CtClass type = null;
        switch (c2) {
            case 'Z': {
                type = CtClass.booleanType;
                break;
            }
            case 'C': {
                type = CtClass.charType;
                break;
            }
            case 'B': {
                type = CtClass.byteType;
                break;
            }
            case 'S': {
                type = CtClass.shortType;
                break;
            }
            case 'I': {
                type = CtClass.intType;
                break;
            }
            case 'J': {
                type = CtClass.longType;
                break;
            }
            case 'F': {
                type = CtClass.floatType;
                break;
            }
            case 'D': {
                type = CtClass.doubleType;
                break;
            }
            case 'V': {
                type = CtClass.voidType;
            }
        }
        return type;
    }

    public static int arrayDimension(String desc) {
        int dim = 0;
        while (desc.charAt(dim) == '[') {
            ++dim;
        }
        return dim;
    }

    public static String toArrayComponent(String desc, int dim) {
        return desc.substring(dim);
    }

    public static int dataSize(String desc) {
        return Descriptor.dataSize(desc, true);
    }

    public static int paramSize(String desc) {
        return -Descriptor.dataSize(desc, false);
    }

    private static int dataSize(String desc, boolean withRet) {
        int n2 = 0;
        char c2 = desc.charAt(0);
        if (c2 == '(') {
            int i2 = 1;
            while (true) {
                if ((c2 = desc.charAt(i2)) == ')') {
                    c2 = desc.charAt(i2 + 1);
                    break;
                }
                boolean array = false;
                while (c2 == '[') {
                    array = true;
                    c2 = desc.charAt(++i2);
                }
                if (c2 == 'L') {
                    if ((i2 = desc.indexOf(59, i2) + 1) <= 0) {
                        throw new IndexOutOfBoundsException("bad descriptor");
                    }
                } else {
                    ++i2;
                }
                if (!(array || c2 != 'J' && c2 != 'D')) {
                    n2 -= 2;
                    continue;
                }
                --n2;
            }
        }
        if (withRet) {
            if (c2 == 'J' || c2 == 'D') {
                n2 += 2;
            } else if (c2 != 'V') {
                ++n2;
            }
        }
        return n2;
    }

    public static String toString(String desc) {
        return PrettyPrinter.toString(desc);
    }

    public static class Iterator {
        private String desc;
        private int index;
        private int curPos;
        private boolean param;

        public Iterator(String s2) {
            this.desc = s2;
            this.curPos = 0;
            this.index = 0;
            this.param = false;
        }

        public boolean hasNext() {
            return this.index < this.desc.length();
        }

        public boolean isParameter() {
            return this.param;
        }

        public char currentChar() {
            return this.desc.charAt(this.curPos);
        }

        public boolean is2byte() {
            char c2 = this.currentChar();
            return c2 == 'D' || c2 == 'J';
        }

        public int next() {
            int nextPos;
            char c2;
            if ((c2 = this.desc.charAt(nextPos = this.index++)) == '(') {
                c2 = this.desc.charAt(++nextPos);
                this.param = true;
            }
            if (c2 == ')') {
                ++this.index;
                c2 = this.desc.charAt(++nextPos);
                this.param = false;
            }
            while (c2 == '[') {
                c2 = this.desc.charAt(++nextPos);
            }
            if (c2 == 'L') {
                if ((nextPos = this.desc.indexOf(59, nextPos) + 1) <= 0) {
                    throw new IndexOutOfBoundsException("bad descriptor");
                }
            } else {
                ++nextPos;
            }
            this.curPos = this.index;
            this.index = nextPos;
            return this.curPos;
        }
    }

    static class PrettyPrinter {
        PrettyPrinter() {
        }

        static String toString(String desc) {
            StringBuffer sbuf = new StringBuffer();
            if (desc.charAt(0) == '(') {
                int pos = 1;
                sbuf.append('(');
                while (desc.charAt(pos) != ')') {
                    if (pos > 1) {
                        sbuf.append(',');
                    }
                    pos = PrettyPrinter.readType(sbuf, pos, desc);
                }
                sbuf.append(')');
            } else {
                PrettyPrinter.readType(sbuf, 0, desc);
            }
            return sbuf.toString();
        }

        static int readType(StringBuffer sbuf, int pos, String desc) {
            char c2 = desc.charAt(pos);
            int arrayDim = 0;
            while (c2 == '[') {
                ++arrayDim;
                c2 = desc.charAt(++pos);
            }
            if (c2 == 'L') {
                while ((c2 = desc.charAt(++pos)) != ';') {
                    if (c2 == '/') {
                        c2 = '.';
                    }
                    sbuf.append(c2);
                }
            } else {
                CtClass t2 = Descriptor.toPrimitiveClass(c2);
                sbuf.append(t2.getName());
            }
            while (arrayDim-- > 0) {
                sbuf.append("[]");
            }
            return pos + 1;
        }
    }
}

