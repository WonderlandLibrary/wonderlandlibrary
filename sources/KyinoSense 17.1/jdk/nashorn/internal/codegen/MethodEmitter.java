/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.dynalink.support.NameCodec;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.ClassEmitter;
import jdk.nashorn.internal.codegen.CodeGenerator;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.Condition;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import jdk.nashorn.internal.codegen.types.ArrayType;
import jdk.nashorn.internal.codegen.types.BitwiseType;
import jdk.nashorn.internal.codegen.types.NumericType;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.JoinPredecessor;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.LocalVariableConversion;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.TryNode;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ArgumentSetter;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.RewriteException;
import jdk.nashorn.internal.runtime.Scope;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.options.Options;

public class MethodEmitter {
    private final MethodVisitor method;
    private final ClassEmitter classEmitter;
    protected FunctionNode functionNode;
    private Label.Stack stack;
    private boolean preventUndefinedLoad;
    private final Map<Symbol, LocalVariableDef> localVariableDefs = new IdentityHashMap<Symbol, LocalVariableDef>();
    private final Context context;
    static final int LARGE_STRING_THRESHOLD = 32768;
    private final DebugLogger log;
    private final boolean debug;
    private static final int DEBUG_TRACE_LINE;
    private static final Handle LINKERBOOTSTRAP;
    private static final Handle POPULATE_ARRAY_BOOTSTRAP;
    private final CompilerConstants.FieldAccess ERR_STREAM = CompilerConstants.staticField(System.class, "err", PrintStream.class);
    private final CompilerConstants.Call PRINT = CompilerConstants.virtualCallNoLookup(PrintStream.class, "print", Void.TYPE, Object.class);
    private final CompilerConstants.Call PRINTLN = CompilerConstants.virtualCallNoLookup(PrintStream.class, "println", Void.TYPE, Object.class);
    private final CompilerConstants.Call PRINT_STACKTRACE = CompilerConstants.virtualCallNoLookup(Throwable.class, "printStackTrace", Void.TYPE, new Class[0]);
    private static int linePrefix;

    MethodEmitter(ClassEmitter classEmitter, MethodVisitor method) {
        this(classEmitter, method, null);
    }

    MethodEmitter(ClassEmitter classEmitter, MethodVisitor method, FunctionNode functionNode) {
        this.context = classEmitter.getContext();
        this.classEmitter = classEmitter;
        this.method = method;
        this.functionNode = functionNode;
        this.stack = null;
        this.log = this.context.getLogger(CodeGenerator.class);
        this.debug = this.log.isEnabled();
    }

    public void begin() {
        this.classEmitter.beginMethod(this);
        this.newStack();
        this.method.visitCode();
    }

    public void end() {
        this.method.visitMaxs(0, 0);
        this.method.visitEnd();
        this.classEmitter.endMethod(this);
    }

    boolean isReachable() {
        return this.stack != null;
    }

    private void doesNotContinueSequentially() {
        this.stack = null;
    }

    private void newStack() {
        this.stack = new Label.Stack();
    }

    public String toString() {
        return "methodEmitter: " + (this.functionNode == null ? this.method : this.functionNode.getName()).toString() + ' ' + Debug.id(this);
    }

    void pushType(Type type) {
        if (type != null) {
            this.stack.push(type);
        }
    }

    private Type popType(Type expected) {
        Type type = this.popType();
        assert (type.isEquivalentTo(expected)) : type + " is not compatible with " + expected;
        return type;
    }

    private Type popType() {
        return this.stack.pop();
    }

    private NumericType popNumeric() {
        Type type = this.popType();
        if (type.isBoolean()) {
            return Type.INT;
        }
        assert (type.isNumeric());
        return (NumericType)type;
    }

    private BitwiseType popBitwise() {
        Type type = this.popType();
        if (type == Type.BOOLEAN) {
            return Type.INT;
        }
        return (BitwiseType)type;
    }

    private BitwiseType popInteger() {
        Type type = this.popType();
        if (type == Type.BOOLEAN) {
            return Type.INT;
        }
        assert (type == Type.INT);
        return (BitwiseType)type;
    }

    private ArrayType popArray() {
        Type type = this.popType();
        assert (type.isArray()) : type;
        return (ArrayType)type;
    }

    final Type peekType(int pos) {
        return this.stack.peek(pos);
    }

    final Type peekType() {
        return this.stack.peek();
    }

    MethodEmitter _new(String classDescriptor, Type type) {
        this.debug((Object)"new", (Object)classDescriptor);
        this.method.visitTypeInsn(187, classDescriptor);
        this.pushType(type);
        return this;
    }

    MethodEmitter _new(Class<?> clazz) {
        return this._new(CompilerConstants.className(clazz), Type.typeFor(clazz));
    }

    MethodEmitter newInstance(Class<?> clazz) {
        return this.invoke(CompilerConstants.constructorNoLookup(clazz));
    }

    MethodEmitter dup(int depth) {
        if (this.peekType().dup(this.method, depth) == null) {
            return null;
        }
        this.debug((Object)"dup", (Object)depth);
        switch (depth) {
            case 0: {
                int l0 = this.stack.getTopLocalLoad();
                this.pushType(this.peekType());
                this.stack.markLocalLoad(l0);
                break;
            }
            case 1: {
                int l0 = this.stack.getTopLocalLoad();
                Type p0 = this.popType();
                int l1 = this.stack.getTopLocalLoad();
                Type p1 = this.popType();
                this.pushType(p0);
                this.stack.markLocalLoad(l0);
                this.pushType(p1);
                this.stack.markLocalLoad(l1);
                this.pushType(p0);
                this.stack.markLocalLoad(l0);
                break;
            }
            case 2: {
                int l0 = this.stack.getTopLocalLoad();
                Type p0 = this.popType();
                int l1 = this.stack.getTopLocalLoad();
                Type p1 = this.popType();
                int l2 = this.stack.getTopLocalLoad();
                Type p2 = this.popType();
                this.pushType(p0);
                this.stack.markLocalLoad(l0);
                this.pushType(p2);
                this.stack.markLocalLoad(l2);
                this.pushType(p1);
                this.stack.markLocalLoad(l1);
                this.pushType(p0);
                this.stack.markLocalLoad(l0);
                break;
            }
            default: {
                assert (false) : "illegal dup depth = " + depth;
                return null;
            }
        }
        return this;
    }

    MethodEmitter dup2() {
        this.debug("dup2");
        if (this.peekType().isCategory2()) {
            int l0 = this.stack.getTopLocalLoad();
            this.pushType(this.peekType());
            this.stack.markLocalLoad(l0);
        } else {
            int l0 = this.stack.getTopLocalLoad();
            Type p0 = this.popType();
            int l1 = this.stack.getTopLocalLoad();
            Type p1 = this.popType();
            this.pushType(p0);
            this.stack.markLocalLoad(l0);
            this.pushType(p1);
            this.stack.markLocalLoad(l1);
            this.pushType(p0);
            this.stack.markLocalLoad(l0);
            this.pushType(p1);
            this.stack.markLocalLoad(l1);
        }
        this.method.visitInsn(92);
        return this;
    }

    MethodEmitter dup() {
        return this.dup(0);
    }

    MethodEmitter pop() {
        this.debug((Object)"pop", (Object)this.peekType());
        this.popType().pop(this.method);
        return this;
    }

    MethodEmitter pop2() {
        if (this.peekType().isCategory2()) {
            this.popType();
        } else {
            this.get2n();
        }
        return this;
    }

    MethodEmitter swap() {
        this.debug("swap");
        int l0 = this.stack.getTopLocalLoad();
        Type p0 = this.popType();
        int l1 = this.stack.getTopLocalLoad();
        Type p1 = this.popType();
        p0.swap(this.method, p1);
        this.pushType(p0);
        this.stack.markLocalLoad(l0);
        this.pushType(p1);
        this.stack.markLocalLoad(l1);
        return this;
    }

    void pack() {
        Type type = this.peekType();
        if (type.isInteger()) {
            this.convert(ObjectClassGenerator.PRIMITIVE_FIELD_TYPE);
        } else if (!type.isLong()) {
            if (type.isNumber()) {
                this.invokestatic("java/lang/Double", "doubleToRawLongBits", "(D)J");
            } else assert (false) : type + " cannot be packed!";
        }
    }

    void initializeMethodParameter(Symbol symbol, Type type, Label start) {
        assert (symbol.isBytecodeLocal());
        this.localVariableDefs.put(symbol, new LocalVariableDef(start.getLabel(), type));
    }

    MethodEmitter newStringBuilder() {
        return this.invoke(CompilerConstants.constructorNoLookup(StringBuilder.class)).dup();
    }

    MethodEmitter stringBuilderAppend() {
        this.convert(Type.STRING);
        return this.invoke(CompilerConstants.virtualCallNoLookup(StringBuilder.class, "append", StringBuilder.class, String.class));
    }

    MethodEmitter and() {
        this.debug("and");
        this.pushType(this.get2i().and(this.method));
        return this;
    }

    MethodEmitter or() {
        this.debug("or");
        this.pushType(this.get2i().or(this.method));
        return this;
    }

    MethodEmitter xor() {
        this.debug("xor");
        this.pushType(this.get2i().xor(this.method));
        return this;
    }

    MethodEmitter shr() {
        this.debug("shr");
        this.popInteger();
        this.pushType(this.popBitwise().shr(this.method));
        return this;
    }

    MethodEmitter shl() {
        this.debug("shl");
        this.popInteger();
        this.pushType(this.popBitwise().shl(this.method));
        return this;
    }

    MethodEmitter sar() {
        this.debug("sar");
        this.popInteger();
        this.pushType(this.popBitwise().sar(this.method));
        return this;
    }

    MethodEmitter neg(int programPoint) {
        this.debug("neg");
        this.pushType(this.popNumeric().neg(this.method, programPoint));
        return this;
    }

    void _catch(Label recovery) {
        assert (this.stack == null);
        recovery.onCatch();
        this.label(recovery);
        this.beginCatchBlock();
    }

    void _catch(Collection<Label> recoveries) {
        assert (this.stack == null);
        for (Label l : recoveries) {
            this.label(l);
        }
        this.beginCatchBlock();
    }

    private void beginCatchBlock() {
        if (!this.isReachable()) {
            this.newStack();
        }
        this.pushType(Type.typeFor(Throwable.class));
    }

    private void _try(Label entry, Label exit, Label recovery, String typeDescriptor, boolean isOptimismHandler) {
        recovery.joinFromTry(entry.getStack(), isOptimismHandler);
        this.method.visitTryCatchBlock(entry.getLabel(), exit.getLabel(), recovery.getLabel(), typeDescriptor);
    }

    void _try(Label entry, Label exit, Label recovery, Class<?> clazz) {
        this._try(entry, exit, recovery, CompilerConstants.className(clazz), clazz == UnwarrantedOptimismException.class);
    }

    void _try(Label entry, Label exit, Label recovery) {
        this._try(entry, exit, recovery, null, false);
    }

    void markLabelAsOptimisticCatchHandler(Label label, int liveLocalCount) {
        label.markAsOptimisticCatchHandler(this.stack, liveLocalCount);
    }

    MethodEmitter loadConstants() {
        this.getStatic(this.classEmitter.getUnitClassName(), CompilerConstants.CONSTANTS.symbolName(), CompilerConstants.CONSTANTS.descriptor());
        assert (this.peekType().isArray()) : this.peekType();
        return this;
    }

    MethodEmitter loadUndefined(Type type) {
        this.debug((Object)"load undefined ", (Object)type);
        this.pushType(type.loadUndefined(this.method));
        return this;
    }

    MethodEmitter loadForcedInitializer(Type type) {
        this.debug((Object)"load forced initializer ", (Object)type);
        this.pushType(type.loadForcedInitializer(this.method));
        return this;
    }

    MethodEmitter loadEmpty(Type type) {
        this.debug((Object)"load empty ", (Object)type);
        this.pushType(type.loadEmpty(this.method));
        return this;
    }

    MethodEmitter loadNull() {
        this.debug("aconst_null");
        this.pushType(Type.OBJECT.ldc(this.method, null));
        return this;
    }

    MethodEmitter loadType(String className) {
        this.debug((Object)"load type", (Object)className);
        this.method.visitLdcInsn(jdk.internal.org.objectweb.asm.Type.getObjectType(className));
        this.pushType(Type.OBJECT);
        return this;
    }

    MethodEmitter load(boolean b) {
        this.debug((Object)"load boolean", (Object)b);
        this.pushType(Type.BOOLEAN.ldc(this.method, b));
        return this;
    }

    MethodEmitter load(int i) {
        this.debug((Object)"load int", (Object)i);
        this.pushType(Type.INT.ldc(this.method, i));
        return this;
    }

    MethodEmitter load(double d) {
        this.debug((Object)"load double", (Object)d);
        this.pushType(Type.NUMBER.ldc(this.method, d));
        return this;
    }

    MethodEmitter load(long l) {
        this.debug((Object)"load long", (Object)l);
        this.pushType(Type.LONG.ldc(this.method, l));
        return this;
    }

    MethodEmitter arraylength() {
        this.debug("arraylength");
        this.popType(Type.OBJECT);
        this.pushType(Type.OBJECT_ARRAY.arraylength(this.method));
        return this;
    }

    MethodEmitter load(String s) {
        this.debug((Object)"load string", (Object)s);
        if (s == null) {
            this.loadNull();
            return this;
        }
        int length = s.length();
        if (length > 32768) {
            this._new(StringBuilder.class);
            this.dup();
            this.load(length);
            this.invoke(CompilerConstants.constructorNoLookup(StringBuilder.class, Integer.TYPE));
            for (int n = 0; n < length; n += 32768) {
                String part = s.substring(n, Math.min(n + 32768, length));
                this.load(part);
                this.stringBuilderAppend();
            }
            this.invoke(CompilerConstants.virtualCallNoLookup(StringBuilder.class, "toString", String.class, new Class[0]));
            return this;
        }
        this.pushType(Type.OBJECT.ldc(this.method, s));
        return this;
    }

    MethodEmitter load(IdentNode ident) {
        return this.load(ident.getSymbol(), ident.getType());
    }

    MethodEmitter load(Symbol symbol, Type type) {
        assert (symbol != null);
        if (symbol.hasSlot()) {
            int slot = symbol.getSlot(type);
            this.debug((Object)"load symbol", (Object)symbol.getName(), (Object)" slot=", (Object)slot, (Object)"type=", (Object)type);
            this.load(type, slot);
        } else if (symbol.isParam()) {
            assert (this.functionNode.isVarArg()) : "Non-vararg functions have slotted parameters";
            int index = symbol.getFieldIndex();
            if (this.functionNode.needsArguments()) {
                this.debug((Object)"load symbol", (Object)symbol.getName(), (Object)" arguments index=", (Object)index);
                this.loadCompilerConstant(CompilerConstants.ARGUMENTS);
                this.load(index);
                ScriptObject.GET_ARGUMENT.invoke(this);
            } else {
                this.debug((Object)"load symbol", (Object)symbol.getName(), (Object)" array index=", (Object)index);
                this.loadCompilerConstant(CompilerConstants.VARARGS);
                this.load(symbol.getFieldIndex());
                this.arrayload();
            }
        }
        return this;
    }

    MethodEmitter load(Type type, int slot) {
        this.debug((Object)"explicit load", (Object)type, (Object)slot);
        Type loadType = type.load(this.method, slot);
        assert (loadType != null);
        this.pushType(loadType == Type.OBJECT && this.isThisSlot(slot) ? Type.THIS : loadType);
        assert (!this.preventUndefinedLoad || slot < this.stack.localVariableTypes.size() && this.stack.localVariableTypes.get(slot) != Type.UNKNOWN) : "Attempted load of uninitialized slot " + slot + " (as type " + type + ")";
        this.stack.markLocalLoad(slot);
        return this;
    }

    private boolean isThisSlot(int slot) {
        if (this.functionNode == null) {
            return slot == CompilerConstants.JAVA_THIS.slot();
        }
        int thisSlot = this.getCompilerConstantSymbol(CompilerConstants.THIS).getSlot(Type.OBJECT);
        assert (!this.functionNode.needsCallee() || thisSlot == 1);
        assert (this.functionNode.needsCallee() || thisSlot == 0);
        return slot == thisSlot;
    }

    MethodEmitter loadHandle(String className, String methodName, String descName, EnumSet<ClassEmitter.Flag> flags) {
        this.debug("load handle ");
        this.pushType(Type.OBJECT.ldc(this.method, new Handle(ClassEmitter.Flag.getValue(flags), className, methodName, descName)));
        return this;
    }

    private Symbol getCompilerConstantSymbol(CompilerConstants cc) {
        return this.functionNode.getBody().getExistingSymbol(cc.symbolName());
    }

    boolean hasScope() {
        return this.getCompilerConstantSymbol(CompilerConstants.SCOPE).hasSlot();
    }

    MethodEmitter loadCompilerConstant(CompilerConstants cc) {
        return this.loadCompilerConstant(cc, null);
    }

    MethodEmitter loadCompilerConstant(CompilerConstants cc, Type type) {
        if (cc == CompilerConstants.SCOPE && this.peekType() == Type.SCOPE) {
            this.dup();
            return this;
        }
        return this.load(this.getCompilerConstantSymbol(cc), type != null ? type : MethodEmitter.getCompilerConstantType(cc));
    }

    MethodEmitter loadScope() {
        return this.loadCompilerConstant(CompilerConstants.SCOPE).checkcast(Scope.class);
    }

    MethodEmitter setSplitState(int state) {
        return this.loadScope().load(state).invoke(Scope.SET_SPLIT_STATE);
    }

    void storeCompilerConstant(CompilerConstants cc) {
        this.storeCompilerConstant(cc, null);
    }

    void storeCompilerConstant(CompilerConstants cc, Type type) {
        Symbol symbol = this.getCompilerConstantSymbol(cc);
        if (!symbol.hasSlot()) {
            return;
        }
        this.debug((Object)"store compiler constant ", (Object)symbol);
        this.store(symbol, type != null ? type : MethodEmitter.getCompilerConstantType(cc));
    }

    private static Type getCompilerConstantType(CompilerConstants cc) {
        Class<?> constantType = cc.type();
        assert (constantType != null);
        return Type.typeFor(constantType);
    }

    MethodEmitter arrayload() {
        this.debug("Xaload");
        this.popType(Type.INT);
        this.pushType(this.popArray().aload(this.method));
        return this;
    }

    void arraystore() {
        this.debug("Xastore");
        Type value = this.popType();
        Type index = this.popType(Type.INT);
        assert (index.isInteger()) : "array index is not integer, but " + index;
        ArrayType array = this.popArray();
        assert (value.isEquivalentTo(array.getElementType())) : "Storing " + value + " into " + array;
        assert (array.isObject());
        array.astore(this.method);
    }

    void store(IdentNode ident) {
        Type type = ident.getType();
        Symbol symbol = ident.getSymbol();
        if (type == Type.UNDEFINED) {
            assert (this.peekType() == Type.UNDEFINED);
            this.store(symbol, Type.OBJECT);
        } else {
            this.store(symbol, type);
        }
    }

    void closeLocalVariable(Symbol symbol, Label label) {
        LocalVariableDef def = this.localVariableDefs.get(symbol);
        if (def != null) {
            this.endLocalValueDef(symbol, def, label.getLabel());
        }
        if (this.isReachable()) {
            this.markDeadLocalVariable(symbol);
        }
    }

    void markDeadLocalVariable(Symbol symbol) {
        if (!symbol.isDead()) {
            this.markDeadSlots(symbol.getFirstSlot(), symbol.slotCount());
        }
    }

    void markDeadSlots(int firstSlot, int slotCount) {
        this.stack.markDeadLocalVariables(firstSlot, slotCount);
    }

    private void endLocalValueDef(Symbol symbol, LocalVariableDef def, jdk.internal.org.objectweb.asm.Label label) {
        String name = symbol.getName();
        if (name.equals(CompilerConstants.THIS.symbolName())) {
            name = CompilerConstants.THIS_DEBUGGER.symbolName();
        }
        this.method.visitLocalVariable(name, def.type.getDescriptor(), null, def.label, label, symbol.getSlot(def.type));
    }

    void store(Symbol symbol, Type type) {
        this.store(symbol, type, true);
    }

    void store(Symbol symbol, Type type, boolean onlySymbolLiveValue) {
        assert (symbol != null) : "No symbol to store";
        if (symbol.hasSlot()) {
            boolean isLiveType = symbol.hasSlotFor(type);
            LocalVariableDef existingDef = this.localVariableDefs.get(symbol);
            if (existingDef == null || existingDef.type != type) {
                jdk.internal.org.objectweb.asm.Label here = new jdk.internal.org.objectweb.asm.Label();
                if (isLiveType) {
                    LocalVariableDef newDef = new LocalVariableDef(here, type);
                    this.localVariableDefs.put(symbol, newDef);
                }
                this.method.visitLabel(here);
                if (existingDef != null) {
                    this.endLocalValueDef(symbol, existingDef, here);
                }
            }
            if (isLiveType) {
                int slot = symbol.getSlot(type);
                this.debug((Object)"store symbol", (Object)symbol.getName(), (Object)" type=", (Object)type, (Object)" slot=", (Object)slot);
                this.storeHidden(type, slot, onlySymbolLiveValue);
            } else {
                if (onlySymbolLiveValue) {
                    this.markDeadLocalVariable(symbol);
                }
                this.debug((Object)"dead store symbol ", (Object)symbol.getName(), (Object)" type=", (Object)type);
                this.pop();
            }
        } else if (symbol.isParam()) {
            assert (!symbol.isScope());
            assert (this.functionNode.isVarArg()) : "Non-vararg functions have slotted parameters";
            int index = symbol.getFieldIndex();
            if (this.functionNode.needsArguments()) {
                this.convert(Type.OBJECT);
                this.debug((Object)"store symbol", (Object)symbol.getName(), (Object)" arguments index=", (Object)index);
                this.loadCompilerConstant(CompilerConstants.ARGUMENTS);
                this.load(index);
                ArgumentSetter.SET_ARGUMENT.invoke(this);
            } else {
                this.convert(Type.OBJECT);
                this.debug((Object)"store symbol", (Object)symbol.getName(), (Object)" array index=", (Object)index);
                this.loadCompilerConstant(CompilerConstants.VARARGS);
                this.load(index);
                ArgumentSetter.SET_ARRAY_ELEMENT.invoke(this);
            }
        } else {
            this.debug((Object)"dead store symbol ", (Object)symbol.getName(), (Object)" type=", (Object)type);
            this.pop();
        }
    }

    void storeHidden(Type type, int slot) {
        this.storeHidden(type, slot, true);
    }

    void storeHidden(Type type, int slot, boolean onlyLiveSymbolValue) {
        this.explicitStore(type, slot);
        this.stack.onLocalStore(type, slot, onlyLiveSymbolValue);
    }

    void storeTemp(Type type, int slot) {
        this.explicitStore(type, slot);
        this.defineTemporaryLocalVariable(slot, slot + type.getSlots());
        this.onLocalStore(type, slot);
    }

    void onLocalStore(Type type, int slot) {
        this.stack.onLocalStore(type, slot, true);
    }

    private void explicitStore(Type type, int slot) {
        assert (slot != -1);
        this.debug((Object)"explicit store", (Object)type, (Object)slot);
        this.popType(type);
        type.store(this.method, slot);
    }

    void defineBlockLocalVariable(int fromSlot, int toSlot) {
        this.stack.defineBlockLocalVariable(fromSlot, toSlot);
    }

    void defineTemporaryLocalVariable(int fromSlot, int toSlot) {
        this.stack.defineTemporaryLocalVariable(fromSlot, toSlot);
    }

    int defineTemporaryLocalVariable(int width) {
        return this.stack.defineTemporaryLocalVariable(width);
    }

    void undefineLocalVariables(int fromSlot, boolean canTruncateSymbol) {
        if (this.isReachable()) {
            this.stack.undefineLocalVariables(fromSlot, canTruncateSymbol);
        }
    }

    List<Type> getLocalVariableTypes() {
        return this.stack.localVariableTypes;
    }

    List<Type> getWidestLiveLocals(List<Type> localTypes) {
        return this.stack.getWidestLiveLocals(localTypes);
    }

    String markSymbolBoundariesInLvarTypesDescriptor(String lvarDescriptor) {
        return this.stack.markSymbolBoundariesInLvarTypesDescriptor(lvarDescriptor);
    }

    void iinc(int slot, int increment) {
        this.debug("iinc");
        this.method.visitIincInsn(slot, increment);
    }

    public void athrow() {
        this.debug("athrow");
        Type receiver = this.popType(Type.OBJECT);
        assert (Throwable.class.isAssignableFrom(receiver.getTypeClass())) : receiver.getTypeClass();
        this.method.visitInsn(191);
        this.doesNotContinueSequentially();
    }

    MethodEmitter _instanceof(String classDescriptor) {
        this.debug((Object)"instanceof", (Object)classDescriptor);
        this.popType(Type.OBJECT);
        this.method.visitTypeInsn(193, classDescriptor);
        this.pushType(Type.INT);
        return this;
    }

    MethodEmitter _instanceof(Class<?> clazz) {
        return this._instanceof(CompilerConstants.className(clazz));
    }

    MethodEmitter checkcast(String classDescriptor) {
        this.debug((Object)"checkcast", (Object)classDescriptor);
        assert (this.peekType().isObject());
        this.method.visitTypeInsn(192, classDescriptor);
        return this;
    }

    MethodEmitter checkcast(Class<?> clazz) {
        return this.checkcast(CompilerConstants.className(clazz));
    }

    MethodEmitter newarray(ArrayType arrayType) {
        this.debug((Object)"newarray ", (Object)"arrayType=", (Object)arrayType);
        this.popType(Type.INT);
        this.pushType(arrayType.newarray(this.method));
        return this;
    }

    MethodEmitter multinewarray(ArrayType arrayType, int dims) {
        this.debug((Object)"multianewarray ", (Object)arrayType, (Object)dims);
        for (int i = 0; i < dims; ++i) {
            this.popType(Type.INT);
        }
        this.pushType(arrayType.newarray(this.method, dims));
        return this;
    }

    private Type fixParamStack(String signature) {
        Type[] params = Type.getMethodArguments(signature);
        for (int i = params.length - 1; i >= 0; --i) {
            this.popType(params[i]);
        }
        Type returnType = Type.getMethodReturnType(signature);
        return returnType;
    }

    MethodEmitter invoke(CompilerConstants.Call call) {
        return call.invoke(this);
    }

    private MethodEmitter invoke(int opcode, String className, String methodName, String methodDescriptor, boolean hasReceiver) {
        Type returnType = this.fixParamStack(methodDescriptor);
        if (hasReceiver) {
            this.popType(Type.OBJECT);
        }
        this.method.visitMethodInsn(opcode, className, methodName, methodDescriptor, opcode == 185);
        if (returnType != null) {
            this.pushType(returnType);
        }
        return this;
    }

    MethodEmitter invokespecial(String className, String methodName, String methodDescriptor) {
        this.debug((Object)"invokespecial", (Object)className, (Object)".", (Object)methodName, (Object)methodDescriptor);
        return this.invoke(183, className, methodName, methodDescriptor, true);
    }

    MethodEmitter invokevirtual(String className, String methodName, String methodDescriptor) {
        this.debug((Object)"invokevirtual", (Object)className, (Object)".", (Object)methodName, (Object)methodDescriptor, (Object)" ", (Object)this.stack);
        return this.invoke(182, className, methodName, methodDescriptor, true);
    }

    MethodEmitter invokestatic(String className, String methodName, String methodDescriptor) {
        this.debug((Object)"invokestatic", (Object)className, (Object)".", (Object)methodName, (Object)methodDescriptor);
        this.invoke(184, className, methodName, methodDescriptor, false);
        return this;
    }

    MethodEmitter invokestatic(String className, String methodName, String methodDescriptor, Type returnType) {
        this.invokestatic(className, methodName, methodDescriptor);
        this.popType();
        this.pushType(returnType);
        return this;
    }

    MethodEmitter invokeinterface(String className, String methodName, String methodDescriptor) {
        this.debug((Object)"invokeinterface", (Object)className, (Object)".", (Object)methodName, (Object)methodDescriptor);
        return this.invoke(185, className, methodName, methodDescriptor, true);
    }

    static jdk.internal.org.objectweb.asm.Label[] getLabels(Label ... table) {
        jdk.internal.org.objectweb.asm.Label[] internalLabels = new jdk.internal.org.objectweb.asm.Label[table.length];
        for (int i = 0; i < table.length; ++i) {
            internalLabels[i] = table[i].getLabel();
        }
        return internalLabels;
    }

    void lookupswitch(Label defaultLabel, int[] values2, Label ... table) {
        this.debug((Object)"lookupswitch", (Object)this.peekType());
        this.adjustStackForSwitch(defaultLabel, table);
        this.method.visitLookupSwitchInsn(defaultLabel.getLabel(), values2, MethodEmitter.getLabels(table));
        this.doesNotContinueSequentially();
    }

    void tableswitch(int lo, int hi, Label defaultLabel, Label ... table) {
        this.debug((Object)"tableswitch", (Object)this.peekType());
        this.adjustStackForSwitch(defaultLabel, table);
        this.method.visitTableSwitchInsn(lo, hi, defaultLabel.getLabel(), MethodEmitter.getLabels(table));
        this.doesNotContinueSequentially();
    }

    private void adjustStackForSwitch(Label defaultLabel, Label ... table) {
        this.popType(Type.INT);
        this.joinTo(defaultLabel);
        for (Label label : table) {
            this.joinTo(label);
        }
    }

    void conditionalJump(Condition cond, Label trueLabel) {
        this.conditionalJump(cond, cond != Condition.GT && cond != Condition.GE, trueLabel);
    }

    void conditionalJump(Condition cond, boolean isCmpG, Label trueLabel) {
        if (this.peekType().isCategory2()) {
            this.debug((Object)"[ld]cmp isCmpG=", (Object)isCmpG);
            this.pushType(this.get2n().cmp(this.method, isCmpG));
            this.jump(Condition.toUnary(cond), trueLabel, 1);
        } else {
            this.debug((Object)"if", (Object)cond);
            this.jump(Condition.toBinary(cond, this.peekType().isObject()), trueLabel, 2);
        }
    }

    void _return(Type type) {
        this.debug((Object)"return", (Object)type);
        assert (this.stack.size() == 1) : "Only return value on stack allowed at return point - depth=" + this.stack.size() + " stack = " + this.stack;
        Type stackType = this.peekType();
        if (!Type.areEquivalent(type, stackType)) {
            this.convert(type);
        }
        this.popType(type)._return(this.method);
        this.doesNotContinueSequentially();
    }

    void _return() {
        this._return(this.peekType());
    }

    void returnVoid() {
        this.debug("return [void]");
        assert (this.stack.isEmpty()) : this.stack;
        this.method.visitInsn(177);
        this.doesNotContinueSequentially();
    }

    MethodEmitter cmp(boolean isCmpG) {
        this.pushType(this.get2n().cmp(this.method, isCmpG));
        return this;
    }

    private void jump(int opcode, Label label, int n) {
        for (int i = 0; i < n; ++i) {
            assert (this.peekType().isInteger() || this.peekType().isBoolean() || this.peekType().isObject()) : "expecting integer type or object for jump, but found " + this.peekType();
            this.popType();
        }
        this.joinTo(label);
        this.method.visitJumpInsn(opcode, label.getLabel());
    }

    void if_acmpeq(Label label) {
        this.debug((Object)"if_acmpeq", (Object)label);
        this.jump(165, label, 2);
    }

    void if_acmpne(Label label) {
        this.debug((Object)"if_acmpne", (Object)label);
        this.jump(166, label, 2);
    }

    void ifnull(Label label) {
        this.debug((Object)"ifnull", (Object)label);
        this.jump(198, label, 1);
    }

    void ifnonnull(Label label) {
        this.debug((Object)"ifnonnull", (Object)label);
        this.jump(199, label, 1);
    }

    void ifeq(Label label) {
        this.debug((Object)"ifeq ", (Object)label);
        this.jump(153, label, 1);
    }

    void if_icmpeq(Label label) {
        this.debug((Object)"if_icmpeq", (Object)label);
        this.jump(159, label, 2);
    }

    void ifne(Label label) {
        this.debug((Object)"ifne", (Object)label);
        this.jump(154, label, 1);
    }

    void if_icmpne(Label label) {
        this.debug((Object)"if_icmpne", (Object)label);
        this.jump(160, label, 2);
    }

    void iflt(Label label) {
        this.debug((Object)"iflt", (Object)label);
        this.jump(155, label, 1);
    }

    void if_icmplt(Label label) {
        this.debug((Object)"if_icmplt", (Object)label);
        this.jump(161, label, 2);
    }

    void ifle(Label label) {
        this.debug((Object)"ifle", (Object)label);
        this.jump(158, label, 1);
    }

    void if_icmple(Label label) {
        this.debug((Object)"if_icmple", (Object)label);
        this.jump(164, label, 2);
    }

    void ifgt(Label label) {
        this.debug((Object)"ifgt", (Object)label);
        this.jump(157, label, 1);
    }

    void if_icmpgt(Label label) {
        this.debug((Object)"if_icmpgt", (Object)label);
        this.jump(163, label, 2);
    }

    void ifge(Label label) {
        this.debug((Object)"ifge", (Object)label);
        this.jump(156, label, 1);
    }

    void if_icmpge(Label label) {
        this.debug((Object)"if_icmpge", (Object)label);
        this.jump(162, label, 2);
    }

    void _goto(Label label) {
        this.debug((Object)"goto", (Object)label);
        this.jump(167, label, 0);
        this.doesNotContinueSequentially();
    }

    void gotoLoopStart(Label loopStart) {
        this.debug((Object)"goto (loop)", (Object)loopStart);
        this.jump(167, loopStart, 0);
    }

    void uncheckedGoto(Label target) {
        this.method.visitJumpInsn(167, target.getLabel());
    }

    void canThrow(Label catchLabel) {
        catchLabel.joinFromTry(this.stack, false);
    }

    private void joinTo(Label label) {
        assert (this.isReachable());
        label.joinFrom(this.stack);
    }

    void label(Label label) {
        this.breakLabel(label, -1);
    }

    void breakLabel(Label label, int liveLocals) {
        Label.Stack labelStack;
        if (!this.isReachable()) {
            assert (label.getStack() == null != label.isReachable());
        } else {
            this.joinTo(label);
        }
        Label.Stack stack = this.stack = (labelStack = label.getStack()) == null ? null : labelStack.clone();
        if (this.stack != null && label.isBreakTarget() && liveLocals != -1) {
            assert (this.stack.firstTemp >= liveLocals);
            this.stack.firstTemp = liveLocals;
        }
        this.debug_label(label);
        this.method.visitLabel(label.getLabel());
    }

    MethodEmitter convert(Type to) {
        Type from = this.peekType();
        Type type = from.convert(this.method, to);
        if (type != null) {
            if (!from.isEquivalentTo(to)) {
                this.debug((Object)"convert", (Object)from, (Object)"->", (Object)to);
            }
            if (type != from) {
                int l0 = this.stack.getTopLocalLoad();
                this.popType();
                this.pushType(type);
                if (!from.isObject()) {
                    this.stack.markLocalLoad(l0);
                }
            }
        }
        return this;
    }

    private Type get2() {
        Type p0 = this.popType();
        Type p1 = this.popType();
        assert (p0.isEquivalentTo(p1)) : "expecting equivalent types on stack but got " + p0 + " and " + p1;
        return p0;
    }

    private BitwiseType get2i() {
        BitwiseType p0 = this.popBitwise();
        BitwiseType p1 = this.popBitwise();
        assert (p0.isEquivalentTo(p1)) : "expecting equivalent types on stack but got " + p0 + " and " + p1;
        return p0;
    }

    private NumericType get2n() {
        NumericType p0 = this.popNumeric();
        NumericType p1 = this.popNumeric();
        assert (p0.isEquivalentTo(p1)) : "expecting equivalent types on stack but got " + p0 + " and " + p1;
        return p0;
    }

    MethodEmitter add(int programPoint) {
        this.debug("add");
        this.pushType(this.get2().add(this.method, programPoint));
        return this;
    }

    MethodEmitter sub(int programPoint) {
        this.debug("sub");
        this.pushType(this.get2n().sub(this.method, programPoint));
        return this;
    }

    MethodEmitter mul(int programPoint) {
        this.debug("mul ");
        this.pushType(this.get2n().mul(this.method, programPoint));
        return this;
    }

    MethodEmitter div(int programPoint) {
        this.debug("div");
        this.pushType(this.get2n().div(this.method, programPoint));
        return this;
    }

    MethodEmitter rem(int programPoint) {
        this.debug("rem");
        this.pushType(this.get2n().rem(this.method, programPoint));
        return this;
    }

    protected Type[] getTypesFromStack(int count) {
        return this.stack.getTopTypes(count);
    }

    int[] getLocalLoadsOnStack(int from, int to) {
        return this.stack.getLocalLoads(from, to);
    }

    int getStackSize() {
        return this.stack.size();
    }

    int getFirstTemp() {
        return this.stack.firstTemp;
    }

    int getUsedSlotsWithLiveTemporaries() {
        return this.stack.getUsedSlotsWithLiveTemporaries();
    }

    private String getDynamicSignature(Type returnType, int argCount) {
        Type[] paramTypes = new Type[argCount];
        int pos = 0;
        for (int i = argCount - 1; i >= 0; --i) {
            Type pt;
            if (ScriptObject.class.isAssignableFrom((pt = this.stack.peek(pos++)).getTypeClass()) && !NativeArray.class.isAssignableFrom(pt.getTypeClass())) {
                pt = Type.SCRIPT_OBJECT;
            }
            paramTypes[i] = pt;
        }
        String descriptor = Type.getMethodDescriptor(returnType, paramTypes);
        for (int i = 0; i < argCount; ++i) {
            this.popType(paramTypes[argCount - i - 1]);
        }
        return descriptor;
    }

    MethodEmitter invalidateSpecialName(String name) {
        switch (name) {
            case "apply": 
            case "call": {
                this.debug((Object)"invalidate_name", (Object)"name=", (Object)name);
                this.load("Function");
                this.invoke(ScriptRuntime.INVALIDATE_RESERVED_BUILTIN_NAME);
                break;
            }
        }
        return this;
    }

    MethodEmitter dynamicNew(int argCount, int flags) {
        return this.dynamicNew(argCount, flags, null);
    }

    MethodEmitter dynamicNew(int argCount, int flags, String msg) {
        assert (!MethodEmitter.isOptimistic(flags));
        this.debug((Object)"dynamic_new", (Object)"argcount=", (Object)argCount);
        String signature = this.getDynamicSignature(Type.OBJECT, argCount);
        this.method.visitInvokeDynamicInsn(msg != null && msg.length() < 32768 ? "dyn:new:" + NameCodec.encode(msg) : "dyn:new", signature, LINKERBOOTSTRAP, flags);
        this.pushType(Type.OBJECT);
        return this;
    }

    MethodEmitter dynamicCall(Type returnType, int argCount, int flags) {
        return this.dynamicCall(returnType, argCount, flags, null);
    }

    MethodEmitter dynamicCall(Type returnType, int argCount, int flags, String msg) {
        this.debug((Object)"dynamic_call", (Object)"args=", (Object)argCount, (Object)"returnType=", (Object)returnType);
        String signature = this.getDynamicSignature(returnType, argCount);
        this.debug((Object)"   signature", (Object)signature);
        this.method.visitInvokeDynamicInsn(msg != null && msg.length() < 32768 ? "dyn:call:" + NameCodec.encode(msg) : "dyn:call", signature, LINKERBOOTSTRAP, flags);
        this.pushType(returnType);
        return this;
    }

    MethodEmitter dynamicArrayPopulatorCall(int argCount, int startIndex) {
        this.debug((Object)"populate_array", (Object)"args=", (Object)argCount, (Object)"startIndex=", (Object)startIndex);
        String signature = this.getDynamicSignature(Type.OBJECT_ARRAY, argCount);
        this.method.visitInvokeDynamicInsn("populateArray", signature, POPULATE_ARRAY_BOOTSTRAP, startIndex);
        this.pushType(Type.OBJECT_ARRAY);
        return this;
    }

    MethodEmitter dynamicGet(Type valueType, String name, int flags, boolean isMethod, boolean isIndex) {
        if (name.length() > 32768) {
            return this.load(name).dynamicGetIndex(valueType, flags, isMethod);
        }
        this.debug((Object)"dynamic_get", (Object)name, (Object)valueType, (Object)MethodEmitter.getProgramPoint(flags));
        Type type = valueType;
        if (type.isObject() || type.isBoolean()) {
            type = Type.OBJECT;
        }
        this.popType(Type.SCOPE);
        this.method.visitInvokeDynamicInsn(MethodEmitter.dynGetOperation(isMethod, isIndex) + ':' + NameCodec.encode(name), Type.getMethodDescriptor(type, Type.OBJECT), LINKERBOOTSTRAP, flags);
        this.pushType(type);
        this.convert(valueType);
        return this;
    }

    void dynamicSet(String name, int flags, boolean isIndex) {
        if (name.length() > 32768) {
            this.load(name).swap().dynamicSetIndex(flags);
            return;
        }
        assert (!MethodEmitter.isOptimistic(flags));
        this.debug((Object)"dynamic_set", (Object)name, (Object)this.peekType());
        Type type = this.peekType();
        if (type.isObject() || type.isBoolean()) {
            type = Type.OBJECT;
            this.convert(Type.OBJECT);
        }
        this.popType(type);
        this.popType(Type.SCOPE);
        this.method.visitInvokeDynamicInsn(MethodEmitter.dynSetOperation(isIndex) + ':' + NameCodec.encode(name), CompilerConstants.methodDescriptor(Void.TYPE, Object.class, type.getTypeClass()), LINKERBOOTSTRAP, flags);
    }

    MethodEmitter dynamicGetIndex(Type result, int flags, boolean isMethod) {
        Type index;
        assert (result.getTypeClass().isPrimitive() || result.getTypeClass() == Object.class);
        this.debug((Object)"dynamic_get_index", (Object)this.peekType(1), (Object)"[", (Object)this.peekType(), (Object)"]", (Object)MethodEmitter.getProgramPoint(flags));
        Type resultType = result;
        if (result.isBoolean()) {
            resultType = Type.OBJECT;
        }
        if ((index = this.peekType()).isObject() || index.isBoolean()) {
            index = Type.OBJECT;
            this.convert(Type.OBJECT);
        }
        this.popType();
        this.popType(Type.OBJECT);
        String signature = Type.getMethodDescriptor(resultType, Type.OBJECT, index);
        this.method.visitInvokeDynamicInsn(MethodEmitter.dynGetOperation(isMethod, true), signature, LINKERBOOTSTRAP, flags);
        this.pushType(resultType);
        if (result.isBoolean()) {
            this.convert(Type.BOOLEAN);
        }
        return this;
    }

    private static String getProgramPoint(int flags) {
        if ((flags & 8) == 0) {
            return "";
        }
        return "pp=" + String.valueOf((flags & 0xFFFFF800) >> 11);
    }

    void dynamicSetIndex(int flags) {
        assert (!MethodEmitter.isOptimistic(flags));
        this.debug((Object)"dynamic_set_index", (Object)this.peekType(2), (Object)"[", (Object)this.peekType(1), (Object)"] =", (Object)this.peekType());
        Type value = this.peekType();
        if (value.isObject() || value.isBoolean()) {
            value = Type.OBJECT;
            this.convert(Type.OBJECT);
        }
        this.popType();
        Type index = this.peekType();
        if (index.isObject() || index.isBoolean()) {
            index = Type.OBJECT;
            this.convert(Type.OBJECT);
        }
        this.popType(index);
        Type receiver = this.popType(Type.OBJECT);
        assert (receiver.isObject());
        this.method.visitInvokeDynamicInsn("dyn:setElem|setProp", CompilerConstants.methodDescriptor(Void.TYPE, receiver.getTypeClass(), index.getTypeClass(), value.getTypeClass()), LINKERBOOTSTRAP, flags);
    }

    MethodEmitter loadKey(Object key) {
        if (key instanceof IdentNode) {
            this.method.visitLdcInsn(((IdentNode)key).getName());
        } else if (key instanceof LiteralNode) {
            this.method.visitLdcInsn(((LiteralNode)key).getString());
        } else {
            this.method.visitLdcInsn(JSType.toString(key));
        }
        this.pushType(Type.OBJECT);
        return this;
    }

    private static Type fieldType(String desc) {
        switch (desc) {
            case "Z": 
            case "B": 
            case "C": 
            case "S": 
            case "I": {
                return Type.INT;
            }
            case "F": {
                assert (false);
            }
            case "D": {
                return Type.NUMBER;
            }
            case "J": {
                return Type.LONG;
            }
        }
        assert (desc.startsWith("[") || desc.startsWith("L")) : desc + " is not an object type";
        switch (desc.charAt(0)) {
            case 'L': {
                return Type.OBJECT;
            }
            case '[': {
                return Type.typeFor(Array.newInstance(MethodEmitter.fieldType(desc.substring(1)).getTypeClass(), 0).getClass());
            }
        }
        assert (false);
        return Type.OBJECT;
    }

    MethodEmitter getField(CompilerConstants.FieldAccess fa) {
        return fa.get(this);
    }

    void putField(CompilerConstants.FieldAccess fa) {
        fa.put(this);
    }

    MethodEmitter getField(String className, String fieldName, String fieldDescriptor) {
        this.debug((Object)"getfield", (Object)"receiver=", (Object)this.peekType(), (Object)className, (Object)".", (Object)fieldName, (Object)fieldDescriptor);
        Type receiver = this.popType();
        assert (receiver.isObject());
        this.method.visitFieldInsn(180, className, fieldName, fieldDescriptor);
        this.pushType(MethodEmitter.fieldType(fieldDescriptor));
        return this;
    }

    MethodEmitter getStatic(String className, String fieldName, String fieldDescriptor) {
        this.debug((Object)"getstatic", (Object)className, (Object)".", (Object)fieldName, (Object)".", (Object)fieldDescriptor);
        this.method.visitFieldInsn(178, className, fieldName, fieldDescriptor);
        this.pushType(MethodEmitter.fieldType(fieldDescriptor));
        return this;
    }

    void putField(String className, String fieldName, String fieldDescriptor) {
        this.debug((Object)"putfield", (Object)"receiver=", (Object)this.peekType(1), (Object)"value=", (Object)this.peekType());
        this.popType(MethodEmitter.fieldType(fieldDescriptor));
        this.popType(Type.OBJECT);
        this.method.visitFieldInsn(181, className, fieldName, fieldDescriptor);
    }

    void putStatic(String className, String fieldName, String fieldDescriptor) {
        this.debug((Object)"putfield", (Object)"value=", (Object)this.peekType());
        this.popType(MethodEmitter.fieldType(fieldDescriptor));
        this.method.visitFieldInsn(179, className, fieldName, fieldDescriptor);
    }

    void lineNumber(int line) {
        if (this.context.getEnv()._debug_lines) {
            this.debug_label("[LINE]", line);
            jdk.internal.org.objectweb.asm.Label l = new jdk.internal.org.objectweb.asm.Label();
            this.method.visitLabel(l);
            this.method.visitLineNumber(line, l);
        }
    }

    void beforeJoinPoint(JoinPredecessor joinPredecessor) {
        for (LocalVariableConversion next = joinPredecessor.getLocalVariableConversion(); next != null; next = next.getNext()) {
            Symbol symbol = next.getSymbol();
            if (next.isLive()) {
                this.emitLocalVariableConversion(next, true);
                continue;
            }
            this.markDeadLocalVariable(symbol);
        }
    }

    void beforeTry(TryNode tryNode, Label recovery) {
        for (LocalVariableConversion next = tryNode.getLocalVariableConversion(); next != null; next = next.getNext()) {
            if (!next.isLive()) continue;
            Type to = this.emitLocalVariableConversion(next, false);
            recovery.getStack().onLocalStore(to, next.getSymbol().getSlot(to), true);
        }
    }

    private static String dynGetOperation(boolean isMethod, boolean isIndex) {
        if (isMethod) {
            return isIndex ? "dyn:getMethod|getElem|getProp" : "dyn:getMethod|getProp|getElem";
        }
        return isIndex ? "dyn:getElem|getProp|getMethod" : "dyn:getProp|getElem|getMethod";
    }

    private static String dynSetOperation(boolean isIndex) {
        return isIndex ? "dyn:setElem|setProp" : "dyn:setProp|setElem";
    }

    private Type emitLocalVariableConversion(LocalVariableConversion conversion, boolean onlySymbolLiveValue) {
        Type from = conversion.getFrom();
        Type to = conversion.getTo();
        Symbol symbol = conversion.getSymbol();
        assert (symbol.isBytecodeLocal());
        if (from == Type.UNDEFINED) {
            this.loadUndefined(to);
        } else {
            this.load(symbol, from).convert(to);
        }
        this.store(symbol, to, onlySymbolLiveValue);
        return to;
    }

    void print() {
        this.getField(this.ERR_STREAM);
        this.swap();
        this.convert(Type.OBJECT);
        this.invoke(this.PRINT);
    }

    void println() {
        this.getField(this.ERR_STREAM);
        this.swap();
        this.convert(Type.OBJECT);
        this.invoke(this.PRINTLN);
    }

    void print(String string) {
        this.getField(this.ERR_STREAM);
        this.load(string);
        this.invoke(this.PRINT);
    }

    void println(String string) {
        this.getField(this.ERR_STREAM);
        this.load(string);
        this.invoke(this.PRINTLN);
    }

    void stacktrace() {
        this._new(Throwable.class);
        this.dup();
        this.invoke(CompilerConstants.constructorNoLookup(Throwable.class));
        this.invoke(this.PRINT_STACKTRACE);
    }

    private void debug(Object ... args2) {
        if (this.debug) {
            this.debug(30, args2);
        }
    }

    private void debug(String arg) {
        if (this.debug) {
            this.debug((Object)30, (Object)arg);
        }
    }

    private void debug(Object arg0, Object arg1) {
        if (this.debug) {
            this.debug(30, arg0, arg1);
        }
    }

    private void debug(Object arg0, Object arg1, Object arg2) {
        if (this.debug) {
            this.debug(30, arg0, arg1, arg2);
        }
    }

    private void debug(Object arg0, Object arg1, Object arg2, Object arg3) {
        if (this.debug) {
            this.debug(30, arg0, arg1, arg2, arg3);
        }
    }

    private void debug(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        if (this.debug) {
            this.debug(30, arg0, arg1, arg2, arg3, arg4);
        }
    }

    private void debug(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
        if (this.debug) {
            this.debug(30, arg0, arg1, arg2, arg3, arg4, arg5);
        }
    }

    private void debug(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
        if (this.debug) {
            this.debug(30, arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
    }

    private void debug_label(Object ... args2) {
        if (this.debug) {
            this.debug(22, args2);
        }
    }

    private void debug(int padConstant, Object ... args2) {
        if (this.debug) {
            int pad;
            StringBuilder sb = new StringBuilder();
            sb.append('#');
            sb.append(++linePrefix);
            for (pad = 5 - sb.length(); pad > 0; --pad) {
                sb.append(' ');
            }
            if (this.isReachable() && !this.stack.isEmpty()) {
                sb.append("{");
                sb.append(this.stack.size());
                sb.append(":");
                for (int pos = 0; pos < this.stack.size(); ++pos) {
                    Type t = this.stack.peek(pos);
                    if (t == Type.SCOPE) {
                        sb.append("scope");
                    } else if (t == Type.THIS) {
                        sb.append("this");
                    } else if (t.isObject()) {
                        int i;
                        String desc = t.getDescriptor();
                        for (i = 0; desc.charAt(i) == '[' && i < desc.length(); ++i) {
                            sb.append('[');
                        }
                        int slash = (desc = desc.substring(i)).lastIndexOf(47);
                        if (slash != -1) {
                            desc = desc.substring(slash + 1, desc.length() - 1);
                        }
                        if ("Object".equals(desc)) {
                            sb.append('O');
                        } else {
                            sb.append(desc);
                        }
                    } else {
                        sb.append(t.getDescriptor());
                    }
                    int loadIndex = this.stack.localLoads[this.stack.sp - 1 - pos];
                    if (loadIndex != -1) {
                        sb.append('(').append(loadIndex).append(')');
                    }
                    if (pos + 1 >= this.stack.size()) continue;
                    sb.append(' ');
                }
                sb.append('}');
                sb.append(' ');
            }
            for (pad = padConstant - sb.length(); pad > 0; --pad) {
                sb.append(' ');
            }
            for (Object arg : args2) {
                sb.append(arg);
                sb.append(' ');
            }
            if (this.context.getEnv() != null) {
                this.log.info(sb);
                if (DEBUG_TRACE_LINE == linePrefix) {
                    new Throwable().printStackTrace(this.log.getOutputStream());
                }
            }
        }
    }

    void setFunctionNode(FunctionNode functionNode) {
        this.functionNode = functionNode;
    }

    void setPreventUndefinedLoad() {
        this.preventUndefinedLoad = true;
    }

    private static boolean isOptimistic(int flags) {
        return (flags & 8) != 0;
    }

    static {
        String tl = Options.getStringProperty("nashorn.codegen.debug.trace", "-1");
        int line = -1;
        try {
            line = Integer.parseInt(tl);
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        DEBUG_TRACE_LINE = line;
        LINKERBOOTSTRAP = new Handle(6, Bootstrap.BOOTSTRAP.className(), Bootstrap.BOOTSTRAP.name(), Bootstrap.BOOTSTRAP.descriptor());
        POPULATE_ARRAY_BOOTSTRAP = new Handle(6, RewriteException.BOOTSTRAP.className(), RewriteException.BOOTSTRAP.name(), RewriteException.BOOTSTRAP.descriptor());
        linePrefix = 0;
    }

    private static class LocalVariableDef {
        private final jdk.internal.org.objectweb.asm.Label label;
        private final Type type;

        LocalVariableDef(jdk.internal.org.objectweb.asm.Label label, Type type) {
            this.label = label;
            this.type = type;
        }
    }
}

