// 
// Decompiled by Procyon v0.5.36
// 

package javassist.bytecode.stackmap;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;

public class Liveness
{
    protected static final byte UNKNOWN = 0;
    protected static final byte READ = 1;
    protected static final byte UPDATED = 2;
    protected byte[] localsUsage;
    public static boolean useArgs;
    static final int NOT_YET = 0;
    static final int CHANGED_LAST = 1;
    static final int DONE = 2;
    static final int CHANGED_NOW = 3;
    
    public void compute(final CodeIterator ci, final TypedBlock[] blocks, final int maxLocals, final TypeData[] args) throws BadBytecode {
        this.computeUsage(ci, blocks, maxLocals);
        if (Liveness.useArgs) {
            this.useAllArgs(blocks, args);
        }
        this.computeLiveness1(blocks[0]);
        while (this.hasChanged(blocks)) {
            this.computeLiveness2(blocks[0]);
        }
    }
    
    private void useAllArgs(final TypedBlock[] blocks, final TypeData[] args) {
        for (int k = 0; k < blocks.length; ++k) {
            final byte[] usage = blocks[k].localsUsage;
            for (int i = 0; i < args.length; ++i) {
                if (args[i] != TypeTag.TOP) {
                    usage[i] = 1;
                }
            }
        }
    }
    
    private void computeLiveness1(final TypedBlock tb) {
        if (tb.updating) {
            this.computeLiveness1u(tb);
            return;
        }
        if (tb.inputs != null) {
            return;
        }
        tb.updating = true;
        final byte[] usage = tb.localsUsage;
        final int n = usage.length;
        final boolean[] in = new boolean[n];
        for (int i = 0; i < n; ++i) {
            in[i] = (usage[i] == 1);
        }
        for (BasicBlock.Catch handlers = tb.toCatch; handlers != null; handlers = handlers.next) {
            final TypedBlock h = (TypedBlock)handlers.body;
            this.computeLiveness1(h);
            for (int k = 0; k < n; ++k) {
                if (h.inputs[k]) {
                    in[k] = true;
                }
            }
        }
        if (tb.exit != null) {
            for (int j = 0; j < tb.exit.length; ++j) {
                final TypedBlock e = (TypedBlock)tb.exit[j];
                this.computeLiveness1(e);
                for (int l = 0; l < n; ++l) {
                    if (!in[l]) {
                        in[l] = (usage[l] == 0 && e.inputs[l]);
                    }
                }
            }
        }
        tb.updating = false;
        if (tb.inputs == null) {
            tb.inputs = in;
            tb.status = 2;
        }
        else {
            for (int j = 0; j < n; ++j) {
                if (in[j] && !tb.inputs[j]) {
                    tb.inputs[j] = true;
                    tb.status = 3;
                }
            }
        }
    }
    
    private void computeLiveness1u(final TypedBlock tb) {
        if (tb.inputs == null) {
            final byte[] usage = tb.localsUsage;
            final int n = usage.length;
            final boolean[] in = new boolean[n];
            for (int i = 0; i < n; ++i) {
                in[i] = (usage[i] == 1);
            }
            tb.inputs = in;
            tb.status = 2;
        }
    }
    
    private void computeLiveness2(final TypedBlock tb) {
        if (tb.updating || tb.status >= 2) {
            return;
        }
        tb.updating = true;
        if (tb.exit == null) {
            tb.status = 2;
        }
        else {
            boolean changed = false;
            for (int i = 0; i < tb.exit.length; ++i) {
                final TypedBlock e = (TypedBlock)tb.exit[i];
                this.computeLiveness2(e);
                if (e.status != 2) {
                    changed = true;
                }
            }
            if (changed) {
                changed = false;
                final byte[] usage = tb.localsUsage;
                final int n = usage.length;
                for (int j = 0; j < tb.exit.length; ++j) {
                    final TypedBlock e2 = (TypedBlock)tb.exit[j];
                    if (e2.status != 2) {
                        for (int k = 0; k < n; ++k) {
                            if (!tb.inputs[k] && usage[k] == 0 && e2.inputs[k]) {
                                tb.inputs[k] = true;
                                changed = true;
                            }
                        }
                    }
                }
                tb.status = (changed ? 3 : 2);
            }
            else {
                tb.status = 2;
            }
        }
        if (this.computeLiveness2except(tb)) {
            tb.status = 3;
        }
        tb.updating = false;
    }
    
    private boolean computeLiveness2except(final TypedBlock tb) {
        BasicBlock.Catch handlers = tb.toCatch;
        boolean changed = false;
        while (handlers != null) {
            final TypedBlock h = (TypedBlock)handlers.body;
            this.computeLiveness2(h);
            if (h.status != 2) {
                final boolean[] in = tb.inputs;
                for (int n = in.length, k = 0; k < n; ++k) {
                    if (!in[k] && h.inputs[k]) {
                        in[k] = true;
                        changed = true;
                    }
                }
            }
            handlers = handlers.next;
        }
        return changed;
    }
    
    private boolean hasChanged(final TypedBlock[] blocks) {
        final int n = blocks.length;
        boolean changed = false;
        for (final TypedBlock tb : blocks) {
            if (tb.status == 3) {
                tb.status = 1;
                changed = true;
            }
            else {
                tb.status = 0;
            }
        }
        return changed;
    }
    
    private void computeUsage(final CodeIterator ci, final TypedBlock[] blocks, final int maxLocals) throws BadBytecode {
        for (final TypedBlock typedBlock : blocks) {
            final TypedBlock tb = typedBlock;
            final byte[] array = new byte[maxLocals];
            typedBlock.localsUsage = array;
            this.localsUsage = array;
            final int pos = tb.position;
            this.analyze(ci, pos, pos + tb.length);
            this.localsUsage = null;
        }
    }
    
    protected final void readLocal(final int reg) {
        if (this.localsUsage[reg] == 0) {
            this.localsUsage[reg] = 1;
        }
    }
    
    protected final void writeLocal(final int reg) {
        if (this.localsUsage[reg] == 0) {
            this.localsUsage[reg] = 2;
        }
    }
    
    protected void analyze(final CodeIterator ci, final int begin, final int end) throws BadBytecode {
        ci.begin();
        ci.move(begin);
        while (ci.hasNext()) {
            final int index = ci.next();
            if (index >= end) {
                break;
            }
            final int op = ci.byteAt(index);
            if (op < 96) {
                if (op < 54) {
                    this.doOpcode0_53(ci, index, op);
                }
                else {
                    this.doOpcode54_95(ci, index, op);
                }
            }
            else if (op == 132) {
                this.readLocal(ci.byteAt(index + 1));
            }
            else {
                if (op != 196) {
                    continue;
                }
                this.doWIDE(ci, index);
            }
        }
    }
    
    private void doOpcode0_53(final CodeIterator ci, final int pos, final int op) {
        switch (op) {
            case 21:
            case 22:
            case 23:
            case 24:
            case 25: {
                this.readLocal(ci.byteAt(pos + 1));
                break;
            }
            case 26:
            case 27:
            case 28:
            case 29: {
                this.readLocal(op - 26);
                break;
            }
            case 30:
            case 31:
            case 32:
            case 33: {
                this.readLocal(op - 30);
                break;
            }
            case 34:
            case 35:
            case 36:
            case 37: {
                this.readLocal(op - 34);
                break;
            }
            case 38:
            case 39:
            case 40:
            case 41: {
                this.readLocal(op - 38);
                break;
            }
            case 42:
            case 43:
            case 44:
            case 45: {
                this.readLocal(op - 42);
                break;
            }
        }
    }
    
    private void doOpcode54_95(final CodeIterator ci, final int pos, final int op) {
        switch (op) {
            case 54:
            case 55:
            case 56:
            case 57:
            case 58: {
                this.writeLocal(ci.byteAt(pos + 1));
                break;
            }
            case 59:
            case 60:
            case 61:
            case 62: {
                this.writeLocal(op - 59);
                break;
            }
            case 63:
            case 64:
            case 65:
            case 66: {
                this.writeLocal(op - 63);
                break;
            }
            case 67:
            case 68:
            case 69:
            case 70: {
                this.writeLocal(op - 67);
                break;
            }
            case 71:
            case 72:
            case 73:
            case 74: {
                this.writeLocal(op - 71);
                break;
            }
            case 75:
            case 76:
            case 77:
            case 78: {
                this.writeLocal(op - 75);
                break;
            }
        }
    }
    
    private void doWIDE(final CodeIterator ci, final int pos) throws BadBytecode {
        final int op = ci.byteAt(pos + 1);
        final int var = ci.u16bitAt(pos + 2);
        switch (op) {
            case 21:
            case 22:
            case 23:
            case 24:
            case 25: {
                this.readLocal(var);
                break;
            }
            case 54:
            case 55:
            case 56:
            case 57:
            case 58: {
                this.writeLocal(var);
                break;
            }
            case 132: {
                this.readLocal(var);
                break;
            }
        }
    }
    
    static {
        Liveness.useArgs = true;
    }
}
