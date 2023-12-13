/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import javassist.bytecode.analysis.Subroutine;
import javassist.bytecode.analysis.Util;

public class SubroutineScanner
implements Opcode {
    private Subroutine[] subroutines;
    Map<Integer, Subroutine> subTable = new HashMap<Integer, Subroutine>();
    Set<Integer> done = new HashSet<Integer>();

    public Subroutine[] scan(MethodInfo method) throws BadBytecode {
        CodeAttribute code = method.getCodeAttribute();
        CodeIterator iter = code.iterator();
        this.subroutines = new Subroutine[code.getCodeLength()];
        this.subTable.clear();
        this.done.clear();
        this.scan(0, iter, null);
        ExceptionTable exceptions = code.getExceptionTable();
        for (int i2 = 0; i2 < exceptions.size(); ++i2) {
            int handler = exceptions.handlerPc(i2);
            this.scan(handler, iter, this.subroutines[exceptions.startPc(i2)]);
        }
        return this.subroutines;
    }

    private void scan(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
        boolean next;
        if (this.done.contains(pos)) {
            return;
        }
        this.done.add(pos);
        int old = iter.lookAhead();
        iter.move(pos);
        while (next = this.scanOp(pos = iter.next(), iter, sub) && iter.hasNext()) {
        }
        iter.move(old);
    }

    private boolean scanOp(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
        this.subroutines[pos] = sub;
        int opcode = iter.byteAt(pos);
        if (opcode == 170) {
            this.scanTableSwitch(pos, iter, sub);
            return false;
        }
        if (opcode == 171) {
            this.scanLookupSwitch(pos, iter, sub);
            return false;
        }
        if (Util.isReturn(opcode) || opcode == 169 || opcode == 191) {
            return false;
        }
        if (Util.isJumpInstruction(opcode)) {
            int target = Util.getJumpTarget(pos, iter);
            if (opcode == 168 || opcode == 201) {
                Subroutine s2 = this.subTable.get(target);
                if (s2 == null) {
                    s2 = new Subroutine(target, pos);
                    this.subTable.put(target, s2);
                    this.scan(target, iter, s2);
                } else {
                    s2.addCaller(pos);
                }
            } else {
                this.scan(target, iter, sub);
                if (Util.isGoto(opcode)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void scanLookupSwitch(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
        int index = (pos & 0xFFFFFFFC) + 4;
        this.scan(pos + iter.s32bitAt(index), iter, sub);
        int npairs = iter.s32bitAt(index += 4);
        int end = npairs * 8 + (index += 4);
        index += 4;
        while (index < end) {
            int target = iter.s32bitAt(index) + pos;
            this.scan(target, iter, sub);
            index += 8;
        }
    }

    private void scanTableSwitch(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
        int index = (pos & 0xFFFFFFFC) + 4;
        this.scan(pos + iter.s32bitAt(index), iter, sub);
        int low = iter.s32bitAt(index += 4);
        int high = iter.s32bitAt(index += 4);
        int end = (high - low + 1) * 4 + (index += 4);
        while (index < end) {
            int target = iter.s32bitAt(index) + pos;
            this.scan(target, iter, sub);
            index += 4;
        }
    }
}

