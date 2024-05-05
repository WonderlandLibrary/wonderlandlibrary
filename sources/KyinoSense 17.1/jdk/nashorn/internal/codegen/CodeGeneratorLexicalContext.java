/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import jdk.nashorn.internal.IntDeque;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.codegen.MethodEmitter;
import jdk.nashorn.internal.codegen.SharedScopeCall;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.WithNode;

final class CodeGeneratorLexicalContext
extends LexicalContext {
    private int dynamicScopeCount;
    private final Map<SharedScopeCall, SharedScopeCall> scopeCalls = new HashMap<SharedScopeCall, SharedScopeCall>();
    private final Deque<CompileUnit> compileUnits = new ArrayDeque<CompileUnit>();
    private final Deque<MethodEmitter> methodEmitters = new ArrayDeque<MethodEmitter>();
    private final Deque<Expression> discard = new ArrayDeque<Expression>();
    private final Deque<Map<String, Collection<Label>>> unwarrantedOptimismHandlers = new ArrayDeque<Map<String, Collection<Label>>>();
    private final Deque<StringBuilder> slotTypesDescriptors = new ArrayDeque<StringBuilder>();
    private final IntDeque splitNodes = new IntDeque();
    private int[] nextFreeSlots = new int[16];
    private int nextFreeSlotsSize;

    CodeGeneratorLexicalContext() {
    }

    private boolean isWithBoundary(Object node) {
        return node instanceof Block && !this.isEmpty() && this.peek() instanceof WithNode;
    }

    @Override
    public <T extends LexicalContextNode> T push(T node) {
        if (this.isWithBoundary(node)) {
            ++this.dynamicScopeCount;
        } else if (node instanceof FunctionNode) {
            if (((FunctionNode)node).inDynamicContext()) {
                ++this.dynamicScopeCount;
            }
            this.splitNodes.push(0);
        }
        return super.push(node);
    }

    void enterSplitNode() {
        this.splitNodes.getAndIncrement();
        this.pushFreeSlots(this.methodEmitters.peek().getUsedSlotsWithLiveTemporaries());
    }

    void exitSplitNode() {
        int count = this.splitNodes.decrementAndGet();
        assert (count >= 0);
    }

    @Override
    public <T extends Node> T pop(T node) {
        T popped = super.pop(node);
        if (this.isWithBoundary(node)) {
            --this.dynamicScopeCount;
            assert (this.dynamicScopeCount >= 0);
        } else if (node instanceof FunctionNode) {
            if (((FunctionNode)node).inDynamicContext()) {
                --this.dynamicScopeCount;
                assert (this.dynamicScopeCount >= 0);
            }
            assert (this.splitNodes.peek() == 0);
            this.splitNodes.pop();
        }
        return popped;
    }

    boolean inDynamicScope() {
        return this.dynamicScopeCount > 0;
    }

    boolean inSplitNode() {
        return !this.splitNodes.isEmpty() && this.splitNodes.peek() > 0;
    }

    MethodEmitter pushMethodEmitter(MethodEmitter newMethod) {
        this.methodEmitters.push(newMethod);
        return newMethod;
    }

    MethodEmitter popMethodEmitter(MethodEmitter oldMethod) {
        assert (this.methodEmitters.peek() == oldMethod);
        this.methodEmitters.pop();
        return this.methodEmitters.isEmpty() ? null : this.methodEmitters.peek();
    }

    void pushUnwarrantedOptimismHandlers() {
        this.unwarrantedOptimismHandlers.push(new HashMap());
        this.slotTypesDescriptors.push(new StringBuilder());
    }

    Map<String, Collection<Label>> getUnwarrantedOptimismHandlers() {
        return this.unwarrantedOptimismHandlers.peek();
    }

    Map<String, Collection<Label>> popUnwarrantedOptimismHandlers() {
        this.slotTypesDescriptors.pop();
        return this.unwarrantedOptimismHandlers.pop();
    }

    CompileUnit pushCompileUnit(CompileUnit newUnit) {
        this.compileUnits.push(newUnit);
        return newUnit;
    }

    CompileUnit popCompileUnit(CompileUnit oldUnit) {
        assert (this.compileUnits.peek() == oldUnit);
        CompileUnit unit = this.compileUnits.pop();
        assert (unit.hasCode()) : "compile unit popped without code";
        unit.setUsed();
        return this.compileUnits.isEmpty() ? null : this.compileUnits.peek();
    }

    boolean hasCompileUnits() {
        return !this.compileUnits.isEmpty();
    }

    Collection<SharedScopeCall> getScopeCalls() {
        return Collections.unmodifiableCollection(this.scopeCalls.values());
    }

    SharedScopeCall getScopeCall(CompileUnit unit, Symbol symbol, Type valueType, Type returnType, Type[] paramTypes, int flags) {
        SharedScopeCall scopeCall = new SharedScopeCall(symbol, valueType, returnType, paramTypes, flags);
        if (this.scopeCalls.containsKey(scopeCall)) {
            return this.scopeCalls.get(scopeCall);
        }
        scopeCall.setClassAndName(unit, this.getCurrentFunction().uniqueName(":scopeCall"));
        this.scopeCalls.put(scopeCall, scopeCall);
        return scopeCall;
    }

    SharedScopeCall getScopeGet(CompileUnit unit, Symbol symbol, Type valueType, int flags) {
        return this.getScopeCall(unit, symbol, valueType, valueType, null, flags);
    }

    void onEnterBlock(Block block) {
        this.pushFreeSlots(this.assignSlots(block, this.isFunctionBody() ? 0 : this.getUsedSlotCount()));
    }

    private void pushFreeSlots(int freeSlots) {
        if (this.nextFreeSlotsSize == this.nextFreeSlots.length) {
            int[] newNextFreeSlots = new int[this.nextFreeSlotsSize * 2];
            System.arraycopy(this.nextFreeSlots, 0, newNextFreeSlots, 0, this.nextFreeSlotsSize);
            this.nextFreeSlots = newNextFreeSlots;
        }
        this.nextFreeSlots[this.nextFreeSlotsSize++] = freeSlots;
    }

    int getUsedSlotCount() {
        return this.nextFreeSlots[this.nextFreeSlotsSize - 1];
    }

    void releaseSlots() {
        int undefinedFromSlot;
        --this.nextFreeSlotsSize;
        int n = undefinedFromSlot = this.nextFreeSlotsSize == 0 ? 0 : this.nextFreeSlots[this.nextFreeSlotsSize - 1];
        if (!this.slotTypesDescriptors.isEmpty()) {
            this.slotTypesDescriptors.peek().setLength(undefinedFromSlot);
        }
        this.methodEmitters.peek().undefineLocalVariables(undefinedFromSlot, false);
    }

    private int assignSlots(Block block, int firstSlot) {
        int fromSlot = firstSlot;
        MethodEmitter method = this.methodEmitters.peek();
        for (Symbol symbol : block.getSymbols()) {
            if (!symbol.hasSlot()) continue;
            symbol.setFirstSlot(fromSlot);
            int toSlot = fromSlot + symbol.slotCount();
            method.defineBlockLocalVariable(fromSlot, toSlot);
            fromSlot = toSlot;
        }
        return fromSlot;
    }

    static Type getTypeForSlotDescriptor(char typeDesc) {
        switch (typeDesc) {
            case 'I': 
            case 'i': {
                return Type.INT;
            }
            case 'J': 
            case 'j': {
                return Type.LONG;
            }
            case 'D': 
            case 'd': {
                return Type.NUMBER;
            }
            case 'A': 
            case 'a': {
                return Type.OBJECT;
            }
            case 'U': 
            case 'u': {
                return Type.UNKNOWN;
            }
        }
        throw new AssertionError();
    }

    void pushDiscard(Expression expr) {
        this.discard.push(expr);
    }

    boolean popDiscardIfCurrent(Expression expr) {
        if (this.isCurrentDiscard(expr)) {
            this.discard.pop();
            return true;
        }
        return false;
    }

    boolean isCurrentDiscard(Expression expr) {
        return this.discard.peek() == expr;
    }

    int quickSlot(Type type) {
        return this.methodEmitters.peek().defineTemporaryLocalVariable(type.getSlots());
    }
}

