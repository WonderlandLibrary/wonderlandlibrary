// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode.stackmap;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.MethodInfo;

public class TypedBlock extends BasicBlock
{
    public int stackTop;
    public int numLocals;
    public TypeData[] stackTypes;
    public TypeData[] localsTypes;
    public boolean[] inputs;
    public boolean updating;
    public int status;
    public byte[] localsUsage;
    
    public static TypedBlock[] makeBlocks(final MethodInfo minfo, final CodeAttribute ca, final boolean optimize) throws BadBytecode {
        final TypedBlock[] blocks = (TypedBlock[])new Maker().make(minfo);
        if (optimize && blocks.length < 2 && (blocks.length == 0 || blocks[0].incoming == 0)) {
            return null;
        }
        final ConstPool pool = minfo.getConstPool();
        final boolean isStatic = (minfo.getAccessFlags() & 0x8) != 0x0;
        blocks[0].initFirstBlock(ca.getMaxStack(), ca.getMaxLocals(), pool.getClassName(), minfo.getDescriptor(), isStatic, minfo.isConstructor());
        new Liveness().compute(ca.iterator(), blocks, ca.getMaxLocals(), blocks[0].localsTypes);
        return blocks;
    }
    
    protected TypedBlock(final int pos) {
        super(pos);
        this.localsTypes = null;
        this.inputs = null;
        this.updating = false;
    }
    
    protected void toString2(final StringBuffer sbuf) {
        super.toString2(sbuf);
        sbuf.append(",\n stack={");
        this.printTypes(sbuf, this.stackTop, this.stackTypes);
        sbuf.append("}, locals={");
        this.printTypes(sbuf, this.numLocals, this.localsTypes);
        sbuf.append("}, inputs={");
        if (this.inputs != null) {
            for (int i = 0; i < this.inputs.length; ++i) {
                sbuf.append(this.inputs[i] ? "1, " : "0, ");
            }
        }
        sbuf.append('}');
    }
    
    private void printTypes(final StringBuffer sbuf, final int size, final TypeData[] types) {
        if (types == null) {
            return;
        }
        for (int i = 0; i < size; ++i) {
            if (i > 0) {
                sbuf.append(", ");
            }
            final TypeData td = types[i];
            sbuf.append((td == null) ? "<>" : td.toString());
        }
    }
    
    public boolean alreadySet() {
        return this.localsTypes != null;
    }
    
    public void setStackMap(final int st, final TypeData[] stack, final int nl, final TypeData[] locals) throws BadBytecode {
        this.stackTop = st;
        this.stackTypes = stack;
        this.numLocals = nl;
        this.localsTypes = locals;
    }
    
    public void resetNumLocals() {
        if (this.localsTypes != null) {
            int nl;
            for (nl = this.localsTypes.length; nl > 0 && this.localsTypes[nl - 1] == TypeTag.TOP; --nl) {
                if (nl > 1) {
                    final TypeData td = this.localsTypes[nl - 2];
                    if (td == TypeTag.LONG) {
                        break;
                    }
                    if (td == TypeTag.DOUBLE) {
                        break;
                    }
                }
            }
            this.numLocals = nl;
        }
    }
    
    void initFirstBlock(final int maxStack, final int maxLocals, final String className, final String methodDesc, final boolean isStatic, final boolean isConstructor) throws BadBytecode {
        if (methodDesc.charAt(0) != '(') {
            throw new BadBytecode("no method descriptor: " + methodDesc);
        }
        this.stackTop = 0;
        this.stackTypes = new TypeData[maxStack];
        final TypeData[] locals = new TypeData[maxLocals];
        if (isConstructor) {
            locals[0] = new TypeData.UninitThis(className);
        }
        else if (!isStatic) {
            locals[0] = new TypeData.ClassName(className);
        }
        int n = isStatic ? -1 : 0;
        int i = 1;
        try {
            while ((i = descToTag(methodDesc, i, ++n, locals)) > 0) {
                if (locals[n].is2WordType()) {
                    locals[++n] = TypeTag.TOP;
                }
            }
        }
        catch (StringIndexOutOfBoundsException e) {
            throw new BadBytecode("bad method descriptor: " + methodDesc);
        }
        this.numLocals = n;
        this.localsTypes = locals;
    }
    
    private static int descToTag(final String desc, int i, final int n, final TypeData[] types) throws BadBytecode {
        final int i2 = i;
        int arrayDim = 0;
        char c = desc.charAt(i);
        if (c == ')') {
            return 0;
        }
        while (c == '[') {
            ++arrayDim;
            c = desc.charAt(++i);
        }
        if (c == 'L') {
            int i3 = desc.indexOf(59, ++i);
            if (arrayDim > 0) {
                types[n] = new TypeData.ClassName(desc.substring(i2, ++i3));
            }
            else {
                types[n] = new TypeData.ClassName(desc.substring(i2 + 1, ++i3 - 1).replace('/', '.'));
            }
            return i3;
        }
        if (arrayDim > 0) {
            types[n] = new TypeData.ClassName(desc.substring(i2, ++i));
            return i;
        }
        final TypeData t = toPrimitiveTag(c);
        if (t == null) {
            throw new BadBytecode("bad method descriptor: " + desc);
        }
        types[n] = t;
        return i + 1;
    }
    
    private static TypeData toPrimitiveTag(final char c) {
        switch (c) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z': {
                return TypeTag.INTEGER;
            }
            case 'J': {
                return TypeTag.LONG;
            }
            case 'F': {
                return TypeTag.FLOAT;
            }
            case 'D': {
                return TypeTag.DOUBLE;
            }
            default: {
                return null;
            }
        }
    }
    
    public static String getRetType(final String desc) {
        final int i = desc.indexOf(41);
        if (i < 0) {
            return "java.lang.Object";
        }
        final char c = desc.charAt(i + 1);
        if (c == '[') {
            return desc.substring(i + 1);
        }
        if (c == 'L') {
            return desc.substring(i + 2, desc.length() - 1).replace('/', '.');
        }
        return "java.lang.Object";
    }
    
    public static class Maker extends BasicBlock.Maker
    {
        protected BasicBlock makeBlock(final int pos) {
            return new TypedBlock(pos);
        }
        
        protected BasicBlock[] makeArray(final int size) {
            return new TypedBlock[size];
        }
    }
}
