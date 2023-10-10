/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode;

import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.bytecode.ByteVector;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.Opcode;

public class Bytecode
extends ByteVector
implements Cloneable,
Opcode {
    public static final CtClass THIS = ConstPool.THIS;
    ConstPool constPool;
    int maxStack;
    int maxLocals;
    ExceptionTable tryblocks;
    private int stackDepth;

    public Bytecode(ConstPool cp, int stacksize, int localvars) {
        this.constPool = cp;
        this.maxStack = stacksize;
        this.maxLocals = localvars;
        this.tryblocks = new ExceptionTable(cp);
        this.stackDepth = 0;
    }

    public Bytecode(ConstPool cp) {
        this(cp, 0, 0);
    }

    @Override
    public Object clone() {
        try {
            Bytecode bc = (Bytecode)super.clone();
            bc.tryblocks = (ExceptionTable)this.tryblocks.clone();
            return bc;
        }
        catch (CloneNotSupportedException cnse) {
            throw new RuntimeException(cnse);
        }
    }

    public ConstPool getConstPool() {
        return this.constPool;
    }

    public ExceptionTable getExceptionTable() {
        return this.tryblocks;
    }

    public CodeAttribute toCodeAttribute() {
        return new CodeAttribute(this.constPool, this.maxStack, this.maxLocals, this.get(), this.tryblocks);
    }

    public int length() {
        return this.getSize();
    }

    public byte[] get() {
        return this.copy();
    }

    public int getMaxStack() {
        return this.maxStack;
    }

    public void setMaxStack(int size) {
        this.maxStack = size;
    }

    public int getMaxLocals() {
        return this.maxLocals;
    }

    public void setMaxLocals(int size) {
        this.maxLocals = size;
    }

    public void setMaxLocals(boolean isStatic, CtClass[] params, int locals) {
        if (!isStatic) {
            ++locals;
        }
        if (params != null) {
            CtClass doubleType = CtClass.doubleType;
            CtClass longType = CtClass.longType;
            for (CtClass type : params) {
                if (type == doubleType || type == longType) {
                    locals += 2;
                    continue;
                }
                ++locals;
            }
        }
        this.maxLocals = locals;
    }

    public void incMaxLocals(int diff) {
        this.maxLocals += diff;
    }

    public void addExceptionHandler(int start, int end, int handler, CtClass type) {
        this.addExceptionHandler(start, end, handler, this.constPool.addClassInfo(type));
    }

    public void addExceptionHandler(int start, int end, int handler, String type) {
        this.addExceptionHandler(start, end, handler, this.constPool.addClassInfo(type));
    }

    public void addExceptionHandler(int start, int end, int handler, int type) {
        this.tryblocks.add(start, end, handler, type);
    }

    public int currentPc() {
        return this.getSize();
    }

    @Override
    public int read(int offset) {
        return super.read(offset);
    }

    public int read16bit(int offset) {
        int v1 = this.read(offset);
        int v2 = this.read(offset + 1);
        return (v1 << 8) + (v2 & 0xFF);
    }

    public int read32bit(int offset) {
        int v1 = this.read16bit(offset);
        int v2 = this.read16bit(offset + 2);
        return (v1 << 16) + (v2 & 0xFFFF);
    }

    @Override
    public void write(int offset, int value) {
        super.write(offset, value);
    }

    public void write16bit(int offset, int value) {
        this.write(offset, value >> 8);
        this.write(offset + 1, value);
    }

    public void write32bit(int offset, int value) {
        this.write16bit(offset, value >> 16);
        this.write16bit(offset + 2, value);
    }

    @Override
    public void add(int code) {
        super.add(code);
    }

    public void add32bit(int value) {
        this.add(value >> 24, value >> 16, value >> 8, value);
    }

    @Override
    public void addGap(int length) {
        super.addGap(length);
    }

    public void addOpcode(int code) {
        this.add(code);
        this.growStack(STACK_GROW[code]);
    }

    public void growStack(int diff) {
        this.setStackDepth(this.stackDepth + diff);
    }

    public int getStackDepth() {
        return this.stackDepth;
    }

    public void setStackDepth(int depth) {
        this.stackDepth = depth;
        if (this.stackDepth > this.maxStack) {
            this.maxStack = this.stackDepth;
        }
    }

    public void addIndex(int index) {
        this.add(index >> 8, index);
    }

    public void addAload(int n2) {
        if (n2 < 4) {
            this.addOpcode(42 + n2);
        } else if (n2 < 256) {
            this.addOpcode(25);
            this.add(n2);
        } else {
            this.addOpcode(196);
            this.addOpcode(25);
            this.addIndex(n2);
        }
    }

    public void addAstore(int n2) {
        if (n2 < 4) {
            this.addOpcode(75 + n2);
        } else if (n2 < 256) {
            this.addOpcode(58);
            this.add(n2);
        } else {
            this.addOpcode(196);
            this.addOpcode(58);
            this.addIndex(n2);
        }
    }

    public void addIconst(int n2) {
        if (n2 < 6 && -2 < n2) {
            this.addOpcode(3 + n2);
        } else if (n2 <= 127 && -128 <= n2) {
            this.addOpcode(16);
            this.add(n2);
        } else if (n2 <= Short.MAX_VALUE && Short.MIN_VALUE <= n2) {
            this.addOpcode(17);
            this.add(n2 >> 8);
            this.add(n2);
        } else {
            this.addLdc(this.constPool.addIntegerInfo(n2));
        }
    }

    public void addConstZero(CtClass type) {
        if (type.isPrimitive()) {
            if (type == CtClass.longType) {
                this.addOpcode(9);
            } else if (type == CtClass.floatType) {
                this.addOpcode(11);
            } else if (type == CtClass.doubleType) {
                this.addOpcode(14);
            } else {
                if (type == CtClass.voidType) {
                    throw new RuntimeException("void type?");
                }
                this.addOpcode(3);
            }
        } else {
            this.addOpcode(1);
        }
    }

    public void addIload(int n2) {
        if (n2 < 4) {
            this.addOpcode(26 + n2);
        } else if (n2 < 256) {
            this.addOpcode(21);
            this.add(n2);
        } else {
            this.addOpcode(196);
            this.addOpcode(21);
            this.addIndex(n2);
        }
    }

    public void addIstore(int n2) {
        if (n2 < 4) {
            this.addOpcode(59 + n2);
        } else if (n2 < 256) {
            this.addOpcode(54);
            this.add(n2);
        } else {
            this.addOpcode(196);
            this.addOpcode(54);
            this.addIndex(n2);
        }
    }

    public void addLconst(long n2) {
        if (n2 == 0L || n2 == 1L) {
            this.addOpcode(9 + (int)n2);
        } else {
            this.addLdc2w(n2);
        }
    }

    public void addLload(int n2) {
        if (n2 < 4) {
            this.addOpcode(30 + n2);
        } else if (n2 < 256) {
            this.addOpcode(22);
            this.add(n2);
        } else {
            this.addOpcode(196);
            this.addOpcode(22);
            this.addIndex(n2);
        }
    }

    public void addLstore(int n2) {
        if (n2 < 4) {
            this.addOpcode(63 + n2);
        } else if (n2 < 256) {
            this.addOpcode(55);
            this.add(n2);
        } else {
            this.addOpcode(196);
            this.addOpcode(55);
            this.addIndex(n2);
        }
    }

    public void addDconst(double d2) {
        if (d2 == 0.0 || d2 == 1.0) {
            this.addOpcode(14 + (int)d2);
        } else {
            this.addLdc2w(d2);
        }
    }

    public void addDload(int n2) {
        if (n2 < 4) {
            this.addOpcode(38 + n2);
        } else if (n2 < 256) {
            this.addOpcode(24);
            this.add(n2);
        } else {
            this.addOpcode(196);
            this.addOpcode(24);
            this.addIndex(n2);
        }
    }

    public void addDstore(int n2) {
        if (n2 < 4) {
            this.addOpcode(71 + n2);
        } else if (n2 < 256) {
            this.addOpcode(57);
            this.add(n2);
        } else {
            this.addOpcode(196);
            this.addOpcode(57);
            this.addIndex(n2);
        }
    }

    public void addFconst(float f2) {
        if (f2 == 0.0f || f2 == 1.0f || f2 == 2.0f) {
            this.addOpcode(11 + (int)f2);
        } else {
            this.addLdc(this.constPool.addFloatInfo(f2));
        }
    }

    public void addFload(int n2) {
        if (n2 < 4) {
            this.addOpcode(34 + n2);
        } else if (n2 < 256) {
            this.addOpcode(23);
            this.add(n2);
        } else {
            this.addOpcode(196);
            this.addOpcode(23);
            this.addIndex(n2);
        }
    }

    public void addFstore(int n2) {
        if (n2 < 4) {
            this.addOpcode(67 + n2);
        } else if (n2 < 256) {
            this.addOpcode(56);
            this.add(n2);
        } else {
            this.addOpcode(196);
            this.addOpcode(56);
            this.addIndex(n2);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public int addLoad(int n2, CtClass type) {
        if (!type.isPrimitive()) {
            this.addAload(n2);
            return 1;
        }
        if (type == CtClass.booleanType || type == CtClass.charType || type == CtClass.byteType || type == CtClass.shortType || type == CtClass.intType) {
            this.addIload(n2);
            return 1;
        }
        if (type == CtClass.longType) {
            this.addLload(n2);
            return 2;
        }
        if (type == CtClass.floatType) {
            this.addFload(n2);
            return 1;
        }
        if (type != CtClass.doubleType) throw new RuntimeException("void type?");
        this.addDload(n2);
        return 2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int addStore(int n2, CtClass type) {
        if (!type.isPrimitive()) {
            this.addAstore(n2);
            return 1;
        }
        if (type == CtClass.booleanType || type == CtClass.charType || type == CtClass.byteType || type == CtClass.shortType || type == CtClass.intType) {
            this.addIstore(n2);
            return 1;
        }
        if (type == CtClass.longType) {
            this.addLstore(n2);
            return 2;
        }
        if (type == CtClass.floatType) {
            this.addFstore(n2);
            return 1;
        }
        if (type != CtClass.doubleType) throw new RuntimeException("void type?");
        this.addDstore(n2);
        return 2;
    }

    public int addLoadParameters(CtClass[] params, int offset) {
        int stacksize = 0;
        if (params != null) {
            int n2 = params.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                stacksize += this.addLoad(stacksize + offset, params[i2]);
            }
        }
        return stacksize;
    }

    public void addCheckcast(CtClass c2) {
        this.addOpcode(192);
        this.addIndex(this.constPool.addClassInfo(c2));
    }

    public void addCheckcast(String classname) {
        this.addOpcode(192);
        this.addIndex(this.constPool.addClassInfo(classname));
    }

    public void addInstanceof(String classname) {
        this.addOpcode(193);
        this.addIndex(this.constPool.addClassInfo(classname));
    }

    public void addGetfield(CtClass c2, String name, String type) {
        this.add(180);
        int ci = this.constPool.addClassInfo(c2);
        this.addIndex(this.constPool.addFieldrefInfo(ci, name, type));
        this.growStack(Descriptor.dataSize(type) - 1);
    }

    public void addGetfield(String c2, String name, String type) {
        this.add(180);
        int ci = this.constPool.addClassInfo(c2);
        this.addIndex(this.constPool.addFieldrefInfo(ci, name, type));
        this.growStack(Descriptor.dataSize(type) - 1);
    }

    public void addGetstatic(CtClass c2, String name, String type) {
        this.add(178);
        int ci = this.constPool.addClassInfo(c2);
        this.addIndex(this.constPool.addFieldrefInfo(ci, name, type));
        this.growStack(Descriptor.dataSize(type));
    }

    public void addGetstatic(String c2, String name, String type) {
        this.add(178);
        int ci = this.constPool.addClassInfo(c2);
        this.addIndex(this.constPool.addFieldrefInfo(ci, name, type));
        this.growStack(Descriptor.dataSize(type));
    }

    public void addInvokespecial(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes) {
        String desc = Descriptor.ofMethod(returnType, paramTypes);
        this.addInvokespecial(clazz, name, desc);
    }

    public void addInvokespecial(CtClass clazz, String name, String desc) {
        boolean isInterface = clazz == null ? false : clazz.isInterface();
        this.addInvokespecial(isInterface, this.constPool.addClassInfo(clazz), name, desc);
    }

    public void addInvokespecial(String clazz, String name, String desc) {
        this.addInvokespecial(false, this.constPool.addClassInfo(clazz), name, desc);
    }

    public void addInvokespecial(int clazz, String name, String desc) {
        this.addInvokespecial(false, clazz, name, desc);
    }

    public void addInvokespecial(boolean isInterface, int clazz, String name, String desc) {
        int index = isInterface ? this.constPool.addInterfaceMethodrefInfo(clazz, name, desc) : this.constPool.addMethodrefInfo(clazz, name, desc);
        this.addInvokespecial(index, desc);
    }

    public void addInvokespecial(int index, String desc) {
        this.add(183);
        this.addIndex(index);
        this.growStack(Descriptor.dataSize(desc) - 1);
    }

    public void addInvokestatic(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes) {
        String desc = Descriptor.ofMethod(returnType, paramTypes);
        this.addInvokestatic(clazz, name, desc);
    }

    public void addInvokestatic(CtClass clazz, String name, String desc) {
        boolean isInterface = clazz == THIS ? false : clazz.isInterface();
        this.addInvokestatic(this.constPool.addClassInfo(clazz), name, desc, isInterface);
    }

    public void addInvokestatic(String classname, String name, String desc) {
        this.addInvokestatic(this.constPool.addClassInfo(classname), name, desc);
    }

    public void addInvokestatic(int clazz, String name, String desc) {
        this.addInvokestatic(clazz, name, desc, false);
    }

    private void addInvokestatic(int clazz, String name, String desc, boolean isInterface) {
        this.add(184);
        int index = isInterface ? this.constPool.addInterfaceMethodrefInfo(clazz, name, desc) : this.constPool.addMethodrefInfo(clazz, name, desc);
        this.addIndex(index);
        this.growStack(Descriptor.dataSize(desc));
    }

    public void addInvokevirtual(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes) {
        String desc = Descriptor.ofMethod(returnType, paramTypes);
        this.addInvokevirtual(clazz, name, desc);
    }

    public void addInvokevirtual(CtClass clazz, String name, String desc) {
        this.addInvokevirtual(this.constPool.addClassInfo(clazz), name, desc);
    }

    public void addInvokevirtual(String classname, String name, String desc) {
        this.addInvokevirtual(this.constPool.addClassInfo(classname), name, desc);
    }

    public void addInvokevirtual(int clazz, String name, String desc) {
        this.add(182);
        this.addIndex(this.constPool.addMethodrefInfo(clazz, name, desc));
        this.growStack(Descriptor.dataSize(desc) - 1);
    }

    public void addInvokeinterface(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes, int count) {
        String desc = Descriptor.ofMethod(returnType, paramTypes);
        this.addInvokeinterface(clazz, name, desc, count);
    }

    public void addInvokeinterface(CtClass clazz, String name, String desc, int count) {
        this.addInvokeinterface(this.constPool.addClassInfo(clazz), name, desc, count);
    }

    public void addInvokeinterface(String classname, String name, String desc, int count) {
        this.addInvokeinterface(this.constPool.addClassInfo(classname), name, desc, count);
    }

    public void addInvokeinterface(int clazz, String name, String desc, int count) {
        this.add(185);
        this.addIndex(this.constPool.addInterfaceMethodrefInfo(clazz, name, desc));
        this.add(count);
        this.add(0);
        this.growStack(Descriptor.dataSize(desc) - 1);
    }

    public void addInvokedynamic(int bootstrap, String name, String desc) {
        int nt = this.constPool.addNameAndTypeInfo(name, desc);
        int dyn = this.constPool.addInvokeDynamicInfo(bootstrap, nt);
        this.add(186);
        this.addIndex(dyn);
        this.add(0, 0);
        this.growStack(Descriptor.dataSize(desc));
    }

    public void addLdc(String s2) {
        this.addLdc(this.constPool.addStringInfo(s2));
    }

    public void addLdc(int i2) {
        if (i2 > 255) {
            this.addOpcode(19);
            this.addIndex(i2);
        } else {
            this.addOpcode(18);
            this.add(i2);
        }
    }

    public void addLdc2w(long l2) {
        this.addOpcode(20);
        this.addIndex(this.constPool.addLongInfo(l2));
    }

    public void addLdc2w(double d2) {
        this.addOpcode(20);
        this.addIndex(this.constPool.addDoubleInfo(d2));
    }

    public void addNew(CtClass clazz) {
        this.addOpcode(187);
        this.addIndex(this.constPool.addClassInfo(clazz));
    }

    public void addNew(String classname) {
        this.addOpcode(187);
        this.addIndex(this.constPool.addClassInfo(classname));
    }

    public void addAnewarray(String classname) {
        this.addOpcode(189);
        this.addIndex(this.constPool.addClassInfo(classname));
    }

    public void addAnewarray(CtClass clazz, int length) {
        this.addIconst(length);
        this.addOpcode(189);
        this.addIndex(this.constPool.addClassInfo(clazz));
    }

    public void addNewarray(int atype, int length) {
        this.addIconst(length);
        this.addOpcode(188);
        this.add(atype);
    }

    public int addMultiNewarray(CtClass clazz, int[] dimensions) {
        int len = dimensions.length;
        for (int i2 = 0; i2 < len; ++i2) {
            this.addIconst(dimensions[i2]);
        }
        this.growStack(len);
        return this.addMultiNewarray(clazz, len);
    }

    public int addMultiNewarray(CtClass clazz, int dim) {
        this.add(197);
        this.addIndex(this.constPool.addClassInfo(clazz));
        this.add(dim);
        this.growStack(1 - dim);
        return dim;
    }

    public int addMultiNewarray(String desc, int dim) {
        this.add(197);
        this.addIndex(this.constPool.addClassInfo(desc));
        this.add(dim);
        this.growStack(1 - dim);
        return dim;
    }

    public void addPutfield(CtClass c2, String name, String desc) {
        this.addPutfield0(c2, null, name, desc);
    }

    public void addPutfield(String classname, String name, String desc) {
        this.addPutfield0(null, classname, name, desc);
    }

    private void addPutfield0(CtClass target, String classname, String name, String desc) {
        this.add(181);
        int ci = classname == null ? this.constPool.addClassInfo(target) : this.constPool.addClassInfo(classname);
        this.addIndex(this.constPool.addFieldrefInfo(ci, name, desc));
        this.growStack(-1 - Descriptor.dataSize(desc));
    }

    public void addPutstatic(CtClass c2, String name, String desc) {
        this.addPutstatic0(c2, null, name, desc);
    }

    public void addPutstatic(String classname, String fieldName, String desc) {
        this.addPutstatic0(null, classname, fieldName, desc);
    }

    private void addPutstatic0(CtClass target, String classname, String fieldName, String desc) {
        this.add(179);
        int ci = classname == null ? this.constPool.addClassInfo(target) : this.constPool.addClassInfo(classname);
        this.addIndex(this.constPool.addFieldrefInfo(ci, fieldName, desc));
        this.growStack(-Descriptor.dataSize(desc));
    }

    public void addReturn(CtClass type) {
        if (type == null) {
            this.addOpcode(177);
        } else if (type.isPrimitive()) {
            CtPrimitiveType ptype = (CtPrimitiveType)type;
            this.addOpcode(ptype.getReturnOp());
        } else {
            this.addOpcode(176);
        }
    }

    public void addRet(int var2) {
        if (var2 < 256) {
            this.addOpcode(169);
            this.add(var2);
        } else {
            this.addOpcode(196);
            this.addOpcode(169);
            this.addIndex(var2);
        }
    }

    public void addPrintln(String message) {
        this.addGetstatic("java.lang.System", "err", "Ljava/io/PrintStream;");
        this.addLdc(message);
        this.addInvokevirtual("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
    }
}

