/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.options.Options;

public final class Symbol
implements Comparable<Symbol>,
Cloneable,
Serializable {
    private static final long serialVersionUID = 1L;
    public static final int IS_GLOBAL = 1;
    public static final int IS_VAR = 2;
    public static final int IS_PARAM = 3;
    public static final int KINDMASK = 3;
    public static final int IS_SCOPE = 4;
    public static final int IS_THIS = 8;
    public static final int IS_LET = 16;
    public static final int IS_CONST = 32;
    public static final int IS_INTERNAL = 64;
    public static final int IS_FUNCTION_SELF = 128;
    public static final int IS_FUNCTION_DECLARATION = 256;
    public static final int IS_PROGRAM_LEVEL = 512;
    public static final int HAS_SLOT = 1024;
    public static final int HAS_INT_VALUE = 2048;
    public static final int HAS_DOUBLE_VALUE = 4096;
    public static final int HAS_OBJECT_VALUE = 8192;
    public static final int HAS_BEEN_DECLARED = 16384;
    private final String name;
    private int flags;
    private transient int firstSlot = -1;
    private transient int fieldIndex = -1;
    private int useCount;
    private static final Set<String> TRACE_SYMBOLS;
    private static final Set<String> TRACE_SYMBOLS_STACKTRACE;

    public Symbol(String name, int flags) {
        this.name = name;
        this.flags = flags;
        if (this.shouldTrace()) {
            this.trace("CREATE SYMBOL " + name);
        }
    }

    public Symbol clone() {
        try {
            return (Symbol)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError((Object)e);
        }
    }

    private static String align(String string, int max) {
        StringBuilder sb = new StringBuilder();
        sb.append(string.substring(0, Math.min(string.length(), max)));
        while (sb.length() < max) {
            sb.append(' ');
        }
        return sb.toString();
    }

    void print(PrintWriter stream) {
        StringBuilder sb = new StringBuilder();
        sb.append(Symbol.align(this.name, 20)).append(": ").append(", ").append(Symbol.align(this.firstSlot == -1 ? "none" : "" + this.firstSlot, 10));
        switch (this.flags & 3) {
            case 1: {
                sb.append(" global");
                break;
            }
            case 2: {
                if (this.isConst()) {
                    sb.append(" const");
                    break;
                }
                if (this.isLet()) {
                    sb.append(" let");
                    break;
                }
                sb.append(" var");
                break;
            }
            case 3: {
                sb.append(" param");
                break;
            }
        }
        if (this.isScope()) {
            sb.append(" scope");
        }
        if (this.isInternal()) {
            sb.append(" internal");
        }
        if (this.isThis()) {
            sb.append(" this");
        }
        if (this.isProgramLevel()) {
            sb.append(" program");
        }
        sb.append('\n');
        stream.print(sb.toString());
    }

    public boolean less(int other) {
        return (this.flags & 3) < (other & 3);
    }

    public Symbol setNeedsSlot(boolean needsSlot) {
        if (needsSlot) {
            assert (!this.isScope());
            this.flags |= 0x400;
        } else {
            this.flags &= 0xFFFFFBFF;
        }
        return this;
    }

    public int slotCount() {
        return ((this.flags & 0x800) == 0 ? 0 : 1) + ((this.flags & 0x1000) == 0 ? 0 : 2) + ((this.flags & 0x2000) == 0 ? 0 : 1);
    }

    private boolean isSlotted() {
        return this.firstSlot != -1 && (this.flags & 0x400) != 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append(' ');
        if (this.hasSlot()) {
            sb.append(' ').append('(').append("slot=").append(this.firstSlot).append(' ');
            if ((this.flags & 0x800) != 0) {
                sb.append('I');
            }
            if ((this.flags & 0x1000) != 0) {
                sb.append('D');
            }
            if ((this.flags & 0x2000) != 0) {
                sb.append('O');
            }
            sb.append(')');
        }
        if (this.isScope()) {
            if (this.isGlobal()) {
                sb.append(" G");
            } else {
                sb.append(" S");
            }
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Symbol other) {
        return this.name.compareTo(other.name);
    }

    public boolean hasSlot() {
        return (this.flags & 0x400) != 0;
    }

    public boolean isBytecodeLocal() {
        return this.hasSlot() && !this.isScope();
    }

    public boolean isDead() {
        return (this.flags & 0x404) == 0;
    }

    public boolean isScope() {
        assert ((this.flags & 3) != 1 || (this.flags & 4) == 4) : "global without scope flag";
        return (this.flags & 4) != 0;
    }

    public boolean isFunctionDeclaration() {
        return (this.flags & 0x100) != 0;
    }

    public Symbol setIsScope() {
        if (!this.isScope()) {
            if (this.shouldTrace()) {
                this.trace("SET IS SCOPE");
            }
            this.flags |= 4;
            if (!this.isParam()) {
                this.flags &= 0xFFFFFBFF;
            }
        }
        return this;
    }

    public void setIsFunctionDeclaration() {
        if (!this.isFunctionDeclaration()) {
            if (this.shouldTrace()) {
                this.trace("SET IS FUNCTION DECLARATION");
            }
            this.flags |= 0x100;
        }
    }

    public boolean isVar() {
        return (this.flags & 3) == 2;
    }

    public boolean isGlobal() {
        return (this.flags & 3) == 1;
    }

    public boolean isParam() {
        return (this.flags & 3) == 3;
    }

    public boolean isProgramLevel() {
        return (this.flags & 0x200) != 0;
    }

    public boolean isConst() {
        return (this.flags & 0x20) != 0;
    }

    public boolean isInternal() {
        return (this.flags & 0x40) != 0;
    }

    public boolean isThis() {
        return (this.flags & 8) != 0;
    }

    public boolean isLet() {
        return (this.flags & 0x10) != 0;
    }

    public boolean isFunctionSelf() {
        return (this.flags & 0x80) != 0;
    }

    public boolean isBlockScoped() {
        return this.isLet() || this.isConst();
    }

    public boolean hasBeenDeclared() {
        return (this.flags & 0x4000) != 0;
    }

    public void setHasBeenDeclared() {
        if (!this.hasBeenDeclared()) {
            this.flags |= 0x4000;
        }
    }

    public int getFieldIndex() {
        assert (this.fieldIndex != -1) : "fieldIndex must be initialized " + this.fieldIndex;
        return this.fieldIndex;
    }

    public Symbol setFieldIndex(int fieldIndex) {
        if (this.fieldIndex != fieldIndex) {
            this.fieldIndex = fieldIndex;
        }
        return this;
    }

    public int getFlags() {
        return this.flags;
    }

    public Symbol setFlags(int flags) {
        if (this.flags != flags) {
            this.flags = flags;
        }
        return this;
    }

    public Symbol setFlag(int flag) {
        if ((this.flags & flag) == 0) {
            this.flags |= flag;
        }
        return this;
    }

    public Symbol clearFlag(int flag) {
        if ((this.flags & flag) != 0) {
            this.flags &= ~flag;
        }
        return this;
    }

    public String getName() {
        return this.name;
    }

    public int getFirstSlot() {
        assert (this.isSlotted());
        return this.firstSlot;
    }

    public int getSlot(Type type) {
        assert (this.isSlotted());
        int typeSlot = this.firstSlot;
        if (type.isBoolean() || type.isInteger()) {
            assert ((this.flags & 0x800) != 0);
            return typeSlot;
        }
        typeSlot += (this.flags & 0x800) == 0 ? 0 : 1;
        if (type.isNumber()) {
            assert ((this.flags & 0x1000) != 0);
            return typeSlot;
        }
        assert (type.isObject());
        assert ((this.flags & 0x2000) != 0) : this.name;
        return typeSlot + ((this.flags & 0x1000) == 0 ? 0 : 2);
    }

    public boolean hasSlotFor(Type type) {
        if (type.isBoolean() || type.isInteger()) {
            return (this.flags & 0x800) != 0;
        }
        if (type.isNumber()) {
            return (this.flags & 0x1000) != 0;
        }
        assert (type.isObject());
        return (this.flags & 0x2000) != 0;
    }

    public void setHasSlotFor(Type type) {
        if (type.isBoolean() || type.isInteger()) {
            this.setFlag(2048);
        } else if (type.isNumber()) {
            this.setFlag(4096);
        } else {
            assert (type.isObject());
            this.setFlag(8192);
        }
    }

    public void increaseUseCount() {
        if (this.isScope()) {
            ++this.useCount;
        }
    }

    public int getUseCount() {
        return this.useCount;
    }

    public Symbol setFirstSlot(int firstSlot) {
        assert (firstSlot >= 0 && firstSlot <= 65535);
        if (firstSlot != this.firstSlot) {
            if (this.shouldTrace()) {
                this.trace("SET SLOT " + firstSlot);
            }
            this.firstSlot = firstSlot;
        }
        return this;
    }

    public static Symbol setSymbolIsScope(LexicalContext lc, Symbol symbol) {
        symbol.setIsScope();
        if (!symbol.isGlobal()) {
            lc.setBlockNeedsScope(lc.getDefiningBlock(symbol));
        }
        return symbol;
    }

    private boolean shouldTrace() {
        return TRACE_SYMBOLS != null && (TRACE_SYMBOLS.isEmpty() || TRACE_SYMBOLS.contains(this.name));
    }

    private void trace(String desc) {
        Context.err(Debug.id(this) + " SYMBOL: '" + this.name + "' " + desc);
        if (TRACE_SYMBOLS_STACKTRACE != null && (TRACE_SYMBOLS_STACKTRACE.isEmpty() || TRACE_SYMBOLS_STACKTRACE.contains(this.name))) {
            new Throwable().printStackTrace(Context.getCurrentErr());
        }
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        this.firstSlot = -1;
        this.fieldIndex = -1;
    }

    static {
        StringTokenizer st;
        String trace;
        String stacktrace = Options.getStringProperty("nashorn.compiler.symbol.stacktrace", null);
        if (stacktrace != null) {
            trace = stacktrace;
            TRACE_SYMBOLS_STACKTRACE = new HashSet<String>();
            st = new StringTokenizer(stacktrace, ",");
            while (st.hasMoreTokens()) {
                TRACE_SYMBOLS_STACKTRACE.add(st.nextToken());
            }
        } else {
            trace = Options.getStringProperty("nashorn.compiler.symbol.trace", null);
            TRACE_SYMBOLS_STACKTRACE = null;
        }
        if (trace != null) {
            TRACE_SYMBOLS = new HashSet<String>();
            st = new StringTokenizer(trace, ",");
            while (st.hasMoreTokens()) {
                TRACE_SYMBOLS.add(st.nextToken());
            }
        } else {
            TRACE_SYMBOLS = null;
        }
    }
}

