// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode.stackmap;

import javassist.bytecode.ConstPool;
import javassist.bytecode.StackMap;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.StackMapTable;
import javassist.bytecode.MethodInfo;
import javassist.ClassPool;

public class MapMaker extends Tracer
{
    public static StackMapTable make(final ClassPool classes, final MethodInfo minfo) throws BadBytecode {
        final CodeAttribute ca = minfo.getCodeAttribute();
        if (ca == null) {
            return null;
        }
        final TypedBlock[] blocks = TypedBlock.makeBlocks(minfo, ca, true);
        if (blocks == null) {
            return null;
        }
        final MapMaker mm = new MapMaker(classes, minfo, ca);
        mm.make(blocks, ca.getCode());
        return mm.toStackMap(blocks);
    }
    
    public static StackMap make2(final ClassPool classes, final MethodInfo minfo) throws BadBytecode {
        final CodeAttribute ca = minfo.getCodeAttribute();
        if (ca == null) {
            return null;
        }
        final TypedBlock[] blocks = TypedBlock.makeBlocks(minfo, ca, true);
        if (blocks == null) {
            return null;
        }
        final MapMaker mm = new MapMaker(classes, minfo, ca);
        mm.make(blocks, ca.getCode());
        return mm.toStackMap2(minfo.getConstPool(), blocks);
    }
    
    public MapMaker(final ClassPool classes, final MethodInfo minfo, final CodeAttribute ca) {
        super(classes, minfo.getConstPool(), ca.getMaxStack(), ca.getMaxLocals(), TypedBlock.getRetType(minfo.getDescriptor()));
    }
    
    protected MapMaker(final MapMaker old, final boolean copyStack) {
        super(old, copyStack);
    }
    
    void make(final TypedBlock[] blocks, final byte[] code) throws BadBytecode {
        final TypedBlock first = blocks[0];
        this.fixParamTypes(first);
        final TypeData[] srcTypes = first.localsTypes;
        Tracer.copyFrom(srcTypes.length, srcTypes, this.localsTypes);
        this.make(code, first);
        for (int n = blocks.length, i = 0; i < n; ++i) {
            this.evalExpected(blocks[i]);
        }
    }
    
    private void fixParamTypes(final TypedBlock first) throws BadBytecode {
        for (final TypeData t : first.localsTypes) {
            if (t instanceof TypeData.ClassName) {
                TypeData.setType(t, t.getName(), this.classPool);
            }
        }
    }
    
    private void make(final byte[] code, final TypedBlock tb) throws BadBytecode {
        for (BasicBlock.Catch handlers = tb.toCatch; handlers != null; handlers = handlers.next) {
            this.traceException(code, handlers);
        }
        for (int pos = tb.position, end = pos + tb.length; pos < end; pos += this.doOpcode(pos, code)) {}
        if (tb.exit != null) {
            for (int i = 0; i < tb.exit.length; ++i) {
                final TypedBlock e = (TypedBlock)tb.exit[i];
                if (e.alreadySet()) {
                    this.mergeMap(e, true);
                }
                else {
                    this.recordStackMap(e);
                    final MapMaker maker = new MapMaker(this, true);
                    maker.make(code, e);
                }
            }
        }
    }
    
    private void traceException(final byte[] code, final BasicBlock.Catch handler) throws BadBytecode {
        final TypedBlock tb = (TypedBlock)handler.body;
        if (tb.alreadySet()) {
            this.mergeMap(tb, false);
        }
        else {
            this.recordStackMap(tb, handler.typeIndex);
            final MapMaker maker = new MapMaker(this, false);
            maker.stackTypes[0] = tb.stackTypes[0].getSelf();
            maker.stackTop = 1;
            maker.make(code, tb);
        }
    }
    
    private void mergeMap(final TypedBlock dest, final boolean mergeStack) {
        final boolean[] inputs = dest.inputs;
        for (int n = inputs.length, i = 0; i < n; ++i) {
            if (inputs[i]) {
                this.merge(this.localsTypes[i], dest.localsTypes[i]);
            }
        }
        if (mergeStack) {
            for (int n = this.stackTop, i = 0; i < n; ++i) {
                this.merge(this.stackTypes[i], dest.stackTypes[i]);
            }
        }
    }
    
    private void merge(final TypeData td, final TypeData target) {
        boolean tdIsObj = false;
        boolean targetIsObj = false;
        if (td != MapMaker.TOP && td.isObjectType()) {
            tdIsObj = true;
        }
        if (target != MapMaker.TOP && target.isObjectType()) {
            targetIsObj = true;
        }
        if (tdIsObj && targetIsObj) {
            target.merge(td);
        }
    }
    
    private void recordStackMap(final TypedBlock target) throws BadBytecode {
        final TypeData[] tStackTypes = new TypeData[this.stackTypes.length];
        final int st = this.stackTop;
        Tracer.copyFrom(st, this.stackTypes, tStackTypes);
        this.recordStackMap0(target, st, tStackTypes);
    }
    
    private void recordStackMap(final TypedBlock target, final int exceptionType) throws BadBytecode {
        String type;
        if (exceptionType == 0) {
            type = "java.lang.Throwable";
        }
        else {
            type = this.cpool.getClassInfo(exceptionType);
        }
        final TypeData[] tStackTypes = new TypeData[this.stackTypes.length];
        tStackTypes[0] = new TypeData.ClassName(type);
        this.recordStackMap0(target, 1, tStackTypes);
    }
    
    private void recordStackMap0(final TypedBlock target, final int st, final TypeData[] tStackTypes) throws BadBytecode {
        final int n = this.localsTypes.length;
        final TypeData[] tLocalsTypes = new TypeData[n];
        final int k = Tracer.copyFrom(n, this.localsTypes, tLocalsTypes);
        final boolean[] inputs = target.inputs;
        for (int i = 0; i < n; ++i) {
            if (!inputs[i]) {
                tLocalsTypes[i] = MapMaker.TOP;
            }
        }
        target.setStackMap(st, tStackTypes, k, tLocalsTypes);
    }
    
    void evalExpected(final TypedBlock target) throws BadBytecode {
        final ClassPool cp = this.classPool;
        evalExpected(cp, target.stackTop, target.stackTypes);
        final TypeData[] types = target.localsTypes;
        if (types != null) {
            evalExpected(cp, types.length, types);
        }
    }
    
    private static void evalExpected(final ClassPool cp, final int n, final TypeData[] types) throws BadBytecode {
        for (final TypeData td : types) {
            if (td != null) {
                td.evalExpectedType(cp);
            }
        }
    }
    
    public StackMapTable toStackMap(final TypedBlock[] blocks) {
        final StackMapTable.Writer writer = new StackMapTable.Writer(32);
        final int n = blocks.length;
        TypedBlock prev = blocks[0];
        int offsetDelta = prev.length;
        if (prev.incoming > 0) {
            writer.sameFrame(0);
            --offsetDelta;
        }
        for (int i = 1; i < n; ++i) {
            final TypedBlock bb = blocks[i];
            if (this.isTarget(bb, blocks[i - 1])) {
                bb.resetNumLocals();
                final int diffL = stackMapDiff(prev.numLocals, prev.localsTypes, bb.numLocals, bb.localsTypes);
                this.toStackMapBody(writer, bb, diffL, offsetDelta, prev);
                offsetDelta = bb.length - 1;
                prev = bb;
            }
            else {
                offsetDelta += bb.length;
            }
        }
        return writer.toStackMapTable(this.cpool);
    }
    
    private boolean isTarget(final TypedBlock cur, final TypedBlock prev) {
        final int in = cur.incoming;
        return in > 1 || (in >= 1 && prev.stop);
    }
    
    private void toStackMapBody(final StackMapTable.Writer writer, final TypedBlock bb, final int diffL, final int offsetDelta, final TypedBlock prev) {
        final int stackTop = bb.stackTop;
        if (stackTop == 0) {
            if (diffL == 0) {
                writer.sameFrame(offsetDelta);
                return;
            }
            if (0 > diffL && diffL >= -3) {
                writer.chopFrame(offsetDelta, -diffL);
                return;
            }
            if (0 < diffL && diffL <= 3) {
                final int[] data = new int[diffL];
                final int[] tags = this.fillStackMap(bb.numLocals - prev.numLocals, prev.numLocals, data, bb.localsTypes);
                writer.appendFrame(offsetDelta, tags, data);
                return;
            }
        }
        else {
            if (stackTop == 1 && diffL == 0) {
                final TypeData td = bb.stackTypes[0];
                if (td == MapMaker.TOP) {
                    writer.sameLocals(offsetDelta, 0, 0);
                }
                else {
                    writer.sameLocals(offsetDelta, td.getTypeTag(), td.getTypeData(this.cpool));
                }
                return;
            }
            if (stackTop == 2 && diffL == 0) {
                final TypeData td = bb.stackTypes[0];
                if (td != MapMaker.TOP && td.is2WordType()) {
                    writer.sameLocals(offsetDelta, td.getTypeTag(), td.getTypeData(this.cpool));
                    return;
                }
            }
        }
        final int[] sdata = new int[stackTop];
        final int[] stags = this.fillStackMap(stackTop, 0, sdata, bb.stackTypes);
        final int[] ldata = new int[bb.numLocals];
        final int[] ltags = this.fillStackMap(bb.numLocals, 0, ldata, bb.localsTypes);
        writer.fullFrame(offsetDelta, ltags, ldata, stags, sdata);
    }
    
    private int[] fillStackMap(final int num, final int offset, final int[] data, final TypeData[] types) {
        final int realNum = diffSize(types, offset, offset + num);
        final ConstPool cp = this.cpool;
        final int[] tags = new int[realNum];
        int j = 0;
        for (int i = 0; i < num; ++i) {
            final TypeData td = types[offset + i];
            if (td == MapMaker.TOP) {
                data[j] = (tags[j] = 0);
            }
            else {
                tags[j] = td.getTypeTag();
                data[j] = td.getTypeData(cp);
                if (td.is2WordType()) {
                    ++i;
                }
            }
            ++j;
        }
        return tags;
    }
    
    private static int stackMapDiff(final int oldTdLen, final TypeData[] oldTd, final int newTdLen, final TypeData[] newTd) {
        final int diff = newTdLen - oldTdLen;
        int len;
        if (diff > 0) {
            len = oldTdLen;
        }
        else {
            len = newTdLen;
        }
        if (!stackMapEq(oldTd, newTd, len)) {
            return -100;
        }
        if (diff > 0) {
            return diffSize(newTd, len, newTdLen);
        }
        return -diffSize(oldTd, len, oldTdLen);
    }
    
    private static boolean stackMapEq(final TypeData[] oldTd, final TypeData[] newTd, final int len) {
        for (int i = 0; i < len; ++i) {
            final TypeData td = oldTd[i];
            if (td == MapMaker.TOP) {
                if (newTd[i] != MapMaker.TOP) {
                    return false;
                }
            }
            else if (!oldTd[i].equals(newTd[i])) {
                return false;
            }
        }
        return true;
    }
    
    private static int diffSize(final TypeData[] types, int offset, final int len) {
        int num = 0;
        while (offset < len) {
            final TypeData td = types[offset++];
            ++num;
            if (td != MapMaker.TOP && td.is2WordType()) {
                ++offset;
            }
        }
        return num;
    }
    
    public StackMap toStackMap2(final ConstPool cp, final TypedBlock[] blocks) {
        final StackMap.Writer writer = new StackMap.Writer();
        final int n = blocks.length;
        final boolean[] effective = new boolean[n];
        TypedBlock prev = blocks[0];
        effective[0] = (prev.incoming > 0);
        int num = effective[0] ? 1 : 0;
        for (int i = 1; i < n; ++i) {
            final TypedBlock bb = blocks[i];
            final boolean[] array = effective;
            final int n2 = i;
            final boolean target = this.isTarget(bb, blocks[i - 1]);
            array[n2] = target;
            if (target) {
                bb.resetNumLocals();
                prev = bb;
                ++num;
            }
        }
        if (num == 0) {
            return null;
        }
        writer.write16bit(num);
        for (int i = 0; i < n; ++i) {
            if (effective[i]) {
                this.writeStackFrame(writer, cp, blocks[i].position, blocks[i]);
            }
        }
        return writer.toStackMap(cp);
    }
    
    private void writeStackFrame(final StackMap.Writer writer, final ConstPool cp, final int offset, final TypedBlock tb) {
        writer.write16bit(offset);
        this.writeVerifyTypeInfo(writer, cp, tb.localsTypes, tb.numLocals);
        this.writeVerifyTypeInfo(writer, cp, tb.stackTypes, tb.stackTop);
    }
    
    private void writeVerifyTypeInfo(final StackMap.Writer writer, final ConstPool cp, final TypeData[] types, final int num) {
        int numDWord = 0;
        for (int i = 0; i < num; ++i) {
            final TypeData td = types[i];
            if (td != null && td.is2WordType()) {
                ++numDWord;
                ++i;
            }
        }
        writer.write16bit(num - numDWord);
        for (int i = 0; i < num; ++i) {
            final TypeData td = types[i];
            if (td == MapMaker.TOP) {
                writer.writeVerifyTypeInfo(0, 0);
            }
            else {
                writer.writeVerifyTypeInfo(td.getTypeTag(), td.getTypeData(cp));
                if (td.is2WordType()) {
                    ++i;
                }
            }
        }
    }
}
