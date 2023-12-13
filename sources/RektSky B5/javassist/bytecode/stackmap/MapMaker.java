/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode.stackmap;

import java.util.ArrayList;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ByteArray;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.StackMap;
import javassist.bytecode.StackMapTable;
import javassist.bytecode.stackmap.BasicBlock;
import javassist.bytecode.stackmap.Tracer;
import javassist.bytecode.stackmap.TypeData;
import javassist.bytecode.stackmap.TypedBlock;

public class MapMaker
extends Tracer {
    public static StackMapTable make(ClassPool classes, MethodInfo minfo) throws BadBytecode {
        TypedBlock[] blocks;
        CodeAttribute ca = minfo.getCodeAttribute();
        if (ca == null) {
            return null;
        }
        try {
            blocks = TypedBlock.makeBlocks(minfo, ca, true);
        }
        catch (BasicBlock.JsrBytecode e2) {
            return null;
        }
        if (blocks == null) {
            return null;
        }
        MapMaker mm = new MapMaker(classes, minfo, ca);
        try {
            mm.make(blocks, ca.getCode());
        }
        catch (BadBytecode bb) {
            throw new BadBytecode(minfo, (Throwable)bb);
        }
        return mm.toStackMap(blocks);
    }

    public static StackMap make2(ClassPool classes, MethodInfo minfo) throws BadBytecode {
        TypedBlock[] blocks;
        CodeAttribute ca = minfo.getCodeAttribute();
        if (ca == null) {
            return null;
        }
        try {
            blocks = TypedBlock.makeBlocks(minfo, ca, true);
        }
        catch (BasicBlock.JsrBytecode e2) {
            return null;
        }
        if (blocks == null) {
            return null;
        }
        MapMaker mm = new MapMaker(classes, minfo, ca);
        try {
            mm.make(blocks, ca.getCode());
        }
        catch (BadBytecode bb) {
            throw new BadBytecode(minfo, (Throwable)bb);
        }
        return mm.toStackMap2(minfo.getConstPool(), blocks);
    }

    public MapMaker(ClassPool classes, MethodInfo minfo, CodeAttribute ca) {
        super(classes, minfo.getConstPool(), ca.getMaxStack(), ca.getMaxLocals(), TypedBlock.getRetType(minfo.getDescriptor()));
    }

    protected MapMaker(MapMaker old) {
        super(old);
    }

    void make(TypedBlock[] blocks, byte[] code) throws BadBytecode {
        this.make(code, blocks[0]);
        this.findDeadCatchers(code, blocks);
        try {
            this.fixTypes(code, blocks);
        }
        catch (NotFoundException e2) {
            throw new BadBytecode("failed to resolve types", (Throwable)e2);
        }
    }

    private void make(byte[] code, TypedBlock tb) throws BadBytecode {
        int pos;
        MapMaker.copyTypeData(tb.stackTop, tb.stackTypes, this.stackTypes);
        this.stackTop = tb.stackTop;
        MapMaker.copyTypeData(tb.localsTypes.length, tb.localsTypes, this.localsTypes);
        this.traceException(code, tb.toCatch);
        int end = pos + tb.length;
        for (pos = tb.position; pos < end; pos += this.doOpcode(pos, code)) {
            this.traceException(code, tb.toCatch);
        }
        if (tb.exit != null) {
            for (int i2 = 0; i2 < tb.exit.length; ++i2) {
                TypedBlock e2 = (TypedBlock)tb.exit[i2];
                if (e2.alreadySet()) {
                    this.mergeMap(e2, true);
                    continue;
                }
                this.recordStackMap(e2);
                MapMaker maker = new MapMaker(this);
                maker.make(code, e2);
            }
        }
    }

    private void traceException(byte[] code, BasicBlock.Catch handler) throws BadBytecode {
        while (handler != null) {
            TypedBlock tb = (TypedBlock)handler.body;
            if (tb.alreadySet()) {
                this.mergeMap(tb, false);
                if (tb.stackTop < 1) {
                    throw new BadBytecode("bad catch clause: " + handler.typeIndex);
                }
                tb.stackTypes[0] = this.merge(this.toExceptionType(handler.typeIndex), tb.stackTypes[0]);
            } else {
                this.recordStackMap(tb, handler.typeIndex);
                MapMaker maker = new MapMaker(this);
                maker.make(code, tb);
            }
            handler = handler.next;
        }
    }

    private void mergeMap(TypedBlock dest, boolean mergeStack) throws BadBytecode {
        int i2;
        int n2 = this.localsTypes.length;
        for (i2 = 0; i2 < n2; ++i2) {
            dest.localsTypes[i2] = this.merge(MapMaker.validateTypeData(this.localsTypes, n2, i2), dest.localsTypes[i2]);
        }
        if (mergeStack) {
            n2 = this.stackTop;
            for (i2 = 0; i2 < n2; ++i2) {
                dest.stackTypes[i2] = this.merge(this.stackTypes[i2], dest.stackTypes[i2]);
            }
        }
    }

    private TypeData merge(TypeData src, TypeData target) throws BadBytecode {
        if (src == target) {
            return target;
        }
        if (target instanceof TypeData.ClassName || target instanceof TypeData.BasicType) {
            return target;
        }
        if (target instanceof TypeData.AbsTypeVar) {
            ((TypeData.AbsTypeVar)target).merge(src);
            return target;
        }
        throw new RuntimeException("fatal: this should never happen");
    }

    private void recordStackMap(TypedBlock target) throws BadBytecode {
        TypeData[] tStackTypes = TypeData.make(this.stackTypes.length);
        int st = this.stackTop;
        MapMaker.recordTypeData(st, this.stackTypes, tStackTypes);
        this.recordStackMap0(target, st, tStackTypes);
    }

    private void recordStackMap(TypedBlock target, int exceptionType) throws BadBytecode {
        TypeData[] tStackTypes = TypeData.make(this.stackTypes.length);
        tStackTypes[0] = this.toExceptionType(exceptionType).join();
        this.recordStackMap0(target, 1, tStackTypes);
    }

    private TypeData.ClassName toExceptionType(int exceptionType) {
        String type = exceptionType == 0 ? "java.lang.Throwable" : this.cpool.getClassInfo(exceptionType);
        return new TypeData.ClassName(type);
    }

    private void recordStackMap0(TypedBlock target, int st, TypeData[] tStackTypes) throws BadBytecode {
        int n2 = this.localsTypes.length;
        TypeData[] tLocalsTypes = TypeData.make(n2);
        int k2 = MapMaker.recordTypeData(n2, this.localsTypes, tLocalsTypes);
        target.setStackMap(st, tStackTypes, k2, tLocalsTypes);
    }

    protected static int recordTypeData(int n2, TypeData[] srcTypes, TypeData[] destTypes) {
        int k2 = -1;
        for (int i2 = 0; i2 < n2; ++i2) {
            TypeData t2 = MapMaker.validateTypeData(srcTypes, n2, i2);
            destTypes[i2] = t2.join();
            if (t2 == TOP) continue;
            k2 = i2 + 1;
        }
        return k2 + 1;
    }

    protected static void copyTypeData(int n2, TypeData[] srcTypes, TypeData[] destTypes) {
        for (int i2 = 0; i2 < n2; ++i2) {
            destTypes[i2] = srcTypes[i2];
        }
    }

    private static TypeData validateTypeData(TypeData[] data, int length, int index) {
        TypeData td = data[index];
        if (td.is2WordType() && index + 1 < length && data[index + 1] != TOP) {
            return TOP;
        }
        return td;
    }

    private void findDeadCatchers(byte[] code, TypedBlock[] blocks) throws BadBytecode {
        for (TypedBlock block : blocks) {
            TypedBlock tb;
            if (block.alreadySet()) continue;
            this.fixDeadcode(code, block);
            BasicBlock.Catch handler = block.toCatch;
            if (handler == null || (tb = (TypedBlock)handler.body).alreadySet()) continue;
            this.recordStackMap(tb, handler.typeIndex);
            this.fixDeadcode(code, tb);
            tb.incoming = 1;
        }
    }

    private void fixDeadcode(byte[] code, TypedBlock block) throws BadBytecode {
        int pos = block.position;
        int len = block.length - 3;
        if (len < 0) {
            if (len == -1) {
                code[pos] = 0;
            }
            code[pos + block.length - 1] = -65;
            block.incoming = 1;
            this.recordStackMap(block, 0);
            return;
        }
        block.incoming = 0;
        for (int k2 = 0; k2 < len; ++k2) {
            code[pos + k2] = 0;
        }
        code[pos + len] = -89;
        ByteArray.write16bit(-len, code, pos + len + 1);
    }

    private void fixTypes(byte[] code, TypedBlock[] blocks) throws NotFoundException, BadBytecode {
        ArrayList<TypeData> preOrder = new ArrayList<TypeData>();
        int len = blocks.length;
        int index = 0;
        for (int i2 = 0; i2 < len; ++i2) {
            int j2;
            TypedBlock block = blocks[i2];
            if (!block.alreadySet()) continue;
            int n2 = block.localsTypes.length;
            for (j2 = 0; j2 < n2; ++j2) {
                index = block.localsTypes[j2].dfs(preOrder, index, this.classPool);
            }
            n2 = block.stackTop;
            for (j2 = 0; j2 < n2; ++j2) {
                index = block.stackTypes[j2].dfs(preOrder, index, this.classPool);
            }
        }
    }

    public StackMapTable toStackMap(TypedBlock[] blocks) {
        StackMapTable.Writer writer = new StackMapTable.Writer(32);
        int n2 = blocks.length;
        TypedBlock prev = blocks[0];
        int offsetDelta = prev.length;
        if (prev.incoming > 0) {
            writer.sameFrame(0);
            --offsetDelta;
        }
        for (int i2 = 1; i2 < n2; ++i2) {
            TypedBlock bb = blocks[i2];
            if (this.isTarget(bb, blocks[i2 - 1])) {
                bb.resetNumLocals();
                int diffL = MapMaker.stackMapDiff(prev.numLocals, prev.localsTypes, bb.numLocals, bb.localsTypes);
                this.toStackMapBody(writer, bb, diffL, offsetDelta, prev);
                offsetDelta = bb.length - 1;
                prev = bb;
                continue;
            }
            if (bb.incoming == 0) {
                writer.sameFrame(offsetDelta);
                offsetDelta = bb.length - 1;
                continue;
            }
            offsetDelta += bb.length;
        }
        return writer.toStackMapTable(this.cpool);
    }

    private boolean isTarget(TypedBlock cur, TypedBlock prev) {
        int in = cur.incoming;
        if (in > 1) {
            return true;
        }
        if (in < 1) {
            return false;
        }
        return prev.stop;
    }

    private void toStackMapBody(StackMapTable.Writer writer, TypedBlock bb, int diffL, int offsetDelta, TypedBlock prev) {
        int stackTop = bb.stackTop;
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
                int[] data = new int[diffL];
                int[] tags = this.fillStackMap(bb.numLocals - prev.numLocals, prev.numLocals, data, bb.localsTypes);
                writer.appendFrame(offsetDelta, tags, data);
                return;
            }
        } else {
            TypeData td;
            if (stackTop == 1 && diffL == 0) {
                TypeData td2 = bb.stackTypes[0];
                writer.sameLocals(offsetDelta, td2.getTypeTag(), td2.getTypeData(this.cpool));
                return;
            }
            if (stackTop == 2 && diffL == 0 && (td = bb.stackTypes[0]).is2WordType()) {
                writer.sameLocals(offsetDelta, td.getTypeTag(), td.getTypeData(this.cpool));
                return;
            }
        }
        int[] sdata = new int[stackTop];
        int[] stags = this.fillStackMap(stackTop, 0, sdata, bb.stackTypes);
        int[] ldata = new int[bb.numLocals];
        int[] ltags = this.fillStackMap(bb.numLocals, 0, ldata, bb.localsTypes);
        writer.fullFrame(offsetDelta, ltags, ldata, stags, sdata);
    }

    private int[] fillStackMap(int num, int offset, int[] data, TypeData[] types) {
        int realNum = MapMaker.diffSize(types, offset, offset + num);
        ConstPool cp = this.cpool;
        int[] tags = new int[realNum];
        int j2 = 0;
        for (int i2 = 0; i2 < num; ++i2) {
            TypeData td = types[offset + i2];
            tags[j2] = td.getTypeTag();
            data[j2] = td.getTypeData(cp);
            if (td.is2WordType()) {
                ++i2;
            }
            ++j2;
        }
        return tags;
    }

    private static int stackMapDiff(int oldTdLen, TypeData[] oldTd, int newTdLen, TypeData[] newTd) {
        int diff = newTdLen - oldTdLen;
        int len = diff > 0 ? oldTdLen : newTdLen;
        if (MapMaker.stackMapEq(oldTd, newTd, len)) {
            if (diff > 0) {
                return MapMaker.diffSize(newTd, len, newTdLen);
            }
            return -MapMaker.diffSize(oldTd, len, oldTdLen);
        }
        return -100;
    }

    private static boolean stackMapEq(TypeData[] oldTd, TypeData[] newTd, int len) {
        for (int i2 = 0; i2 < len; ++i2) {
            if (oldTd[i2].eq(newTd[i2])) continue;
            return false;
        }
        return true;
    }

    private static int diffSize(TypeData[] types, int offset, int len) {
        int num = 0;
        while (offset < len) {
            TypeData td = types[offset++];
            ++num;
            if (!td.is2WordType()) continue;
            ++offset;
        }
        return num;
    }

    public StackMap toStackMap2(ConstPool cp, TypedBlock[] blocks) {
        int i2;
        StackMap.Writer writer = new StackMap.Writer();
        int n2 = blocks.length;
        boolean[] effective = new boolean[n2];
        TypedBlock prev = blocks[0];
        effective[0] = prev.incoming > 0;
        int num = effective[0] ? 1 : 0;
        for (i2 = 1; i2 < n2; ++i2) {
            TypedBlock bb = blocks[i2];
            effective[i2] = this.isTarget(bb, blocks[i2 - 1]);
            if (!effective[i2]) continue;
            bb.resetNumLocals();
            prev = bb;
            ++num;
        }
        if (num == 0) {
            return null;
        }
        writer.write16bit(num);
        for (i2 = 0; i2 < n2; ++i2) {
            if (!effective[i2]) continue;
            this.writeStackFrame(writer, cp, blocks[i2].position, blocks[i2]);
        }
        return writer.toStackMap(cp);
    }

    private void writeStackFrame(StackMap.Writer writer, ConstPool cp, int offset, TypedBlock tb) {
        writer.write16bit(offset);
        this.writeVerifyTypeInfo(writer, cp, tb.localsTypes, tb.numLocals);
        this.writeVerifyTypeInfo(writer, cp, tb.stackTypes, tb.stackTop);
    }

    private void writeVerifyTypeInfo(StackMap.Writer writer, ConstPool cp, TypeData[] types, int num) {
        TypeData td;
        int i2;
        int numDWord = 0;
        for (i2 = 0; i2 < num; ++i2) {
            td = types[i2];
            if (td == null || !td.is2WordType()) continue;
            ++numDWord;
            ++i2;
        }
        writer.write16bit(num - numDWord);
        for (i2 = 0; i2 < num; ++i2) {
            td = types[i2];
            writer.writeVerifyTypeInfo(td.getTypeTag(), td.getTypeData(cp));
            if (!td.is2WordType()) continue;
            ++i2;
        }
    }
}

