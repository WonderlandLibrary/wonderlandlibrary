/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.WithNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;

@Logger(name="scopedepths")
final class FindScopeDepths
extends SimpleNodeVisitor
implements Loggable {
    private final Compiler compiler;
    private final Map<Integer, Map<Integer, RecompilableScriptFunctionData>> fnIdToNestedFunctions = new HashMap<Integer, Map<Integer, RecompilableScriptFunctionData>>();
    private final Map<Integer, Map<String, Integer>> externalSymbolDepths = new HashMap<Integer, Map<String, Integer>>();
    private final Map<Integer, Set<String>> internalSymbols = new HashMap<Integer, Set<String>>();
    private final Set<Block> withBodies = new HashSet<Block>();
    private final DebugLogger log;
    private int dynamicScopeCount;

    FindScopeDepths(Compiler compiler) {
        this.compiler = compiler;
        this.log = this.initLogger(compiler.getContext());
    }

    @Override
    public DebugLogger getLogger() {
        return this.log;
    }

    @Override
    public DebugLogger initLogger(Context context) {
        return context.getLogger(this.getClass());
    }

    static int findScopesToStart(LexicalContext lc, FunctionNode fn, Block block) {
        Block bodyBlock = FindScopeDepths.findBodyBlock(lc, fn, block);
        Iterator<Block> iter = lc.getBlocks(block);
        Block b = iter.next();
        int scopesToStart = 0;
        while (true) {
            if (b.needsScope()) {
                ++scopesToStart;
            }
            if (b == bodyBlock) break;
            b = iter.next();
        }
        return scopesToStart;
    }

    static int findInternalDepth(LexicalContext lc, FunctionNode fn, Block block, Symbol symbol) {
        Block bodyBlock = FindScopeDepths.findBodyBlock(lc, fn, block);
        Iterator<Block> iter = lc.getBlocks(block);
        Block b = iter.next();
        int scopesToStart = 0;
        while (true) {
            if (FindScopeDepths.definedInBlock(b, symbol)) {
                return scopesToStart;
            }
            if (b.needsScope()) {
                ++scopesToStart;
            }
            if (b == bodyBlock) break;
            b = iter.next();
        }
        return -1;
    }

    private static boolean definedInBlock(Block block, Symbol symbol) {
        if (symbol.isGlobal()) {
            return block.isGlobalScope();
        }
        return block.getExistingSymbol(symbol.getName()) == symbol;
    }

    static Block findBodyBlock(LexicalContext lc, FunctionNode fn, Block block) {
        Iterator<Block> iter = lc.getBlocks(block);
        while (iter.hasNext()) {
            Block next = iter.next();
            if (fn.getBody() != next) continue;
            return next;
        }
        return null;
    }

    private static Block findGlobalBlock(LexicalContext lc, Block block) {
        Iterator<Block> iter = lc.getBlocks(block);
        Block globalBlock = null;
        while (iter.hasNext()) {
            globalBlock = iter.next();
        }
        return globalBlock;
    }

    private static boolean isDynamicScopeBoundary(FunctionNode fn) {
        return fn.needsDynamicScope();
    }

    private boolean isDynamicScopeBoundary(Block block) {
        return this.withBodies.contains(block);
    }

    @Override
    public boolean enterFunctionNode(FunctionNode functionNode) {
        int fnId;
        Map<Integer, RecompilableScriptFunctionData> nestedFunctions;
        if (this.compiler.isOnDemandCompilation()) {
            return true;
        }
        if (FindScopeDepths.isDynamicScopeBoundary(functionNode)) {
            this.increaseDynamicScopeCount(functionNode);
        }
        if ((nestedFunctions = this.fnIdToNestedFunctions.get(fnId = functionNode.getId())) == null) {
            nestedFunctions = new HashMap<Integer, RecompilableScriptFunctionData>();
            this.fnIdToNestedFunctions.put(fnId, nestedFunctions);
        }
        return true;
    }

    @Override
    public Node leaveFunctionNode(FunctionNode functionNode) {
        String name = functionNode.getName();
        FunctionNode newFunctionNode = functionNode;
        if (this.compiler.isOnDemandCompilation()) {
            RecompilableScriptFunctionData data = this.compiler.getScriptFunctionData(newFunctionNode.getId());
            if (data.inDynamicContext()) {
                this.log.fine("Reviving scriptfunction ", DebugLogger.quote(name), " as defined in previous (now lost) dynamic scope.");
                newFunctionNode = newFunctionNode.setInDynamicContext(this.lc);
            }
            if (newFunctionNode == this.lc.getOutermostFunction() && !newFunctionNode.hasApplyToCallSpecialization()) {
                data.setCachedAst(newFunctionNode);
            }
            return newFunctionNode;
        }
        if (this.inDynamicScope()) {
            this.log.fine("Tagging ", DebugLogger.quote(name), " as defined in dynamic scope");
            newFunctionNode = newFunctionNode.setInDynamicContext(this.lc);
        }
        int fnId = newFunctionNode.getId();
        Map<Integer, RecompilableScriptFunctionData> nestedFunctions = this.fnIdToNestedFunctions.remove(fnId);
        assert (nestedFunctions != null);
        RecompilableScriptFunctionData data = new RecompilableScriptFunctionData(newFunctionNode, this.compiler.getCodeInstaller(), ObjectClassGenerator.createAllocationStrategy(newFunctionNode.getThisProperties(), this.compiler.getContext().useDualFields()), nestedFunctions, this.externalSymbolDepths.get(fnId), this.internalSymbols.get(fnId));
        if (this.lc.getOutermostFunction() != newFunctionNode) {
            FunctionNode parentFn = this.lc.getParentFunction(newFunctionNode);
            if (parentFn != null) {
                this.fnIdToNestedFunctions.get(parentFn.getId()).put(fnId, data);
            }
        } else {
            this.compiler.setData(data);
        }
        if (FindScopeDepths.isDynamicScopeBoundary(functionNode)) {
            this.decreaseDynamicScopeCount(functionNode);
        }
        return newFunctionNode;
    }

    private boolean inDynamicScope() {
        return this.dynamicScopeCount > 0;
    }

    private void increaseDynamicScopeCount(Node node) {
        assert (this.dynamicScopeCount >= 0);
        ++this.dynamicScopeCount;
        if (this.log.isEnabled()) {
            this.log.finest(DebugLogger.quote(this.lc.getCurrentFunction().getName()), " ++dynamicScopeCount = ", this.dynamicScopeCount, " at: ", node, node.getClass());
        }
    }

    private void decreaseDynamicScopeCount(Node node) {
        --this.dynamicScopeCount;
        assert (this.dynamicScopeCount >= 0);
        if (this.log.isEnabled()) {
            this.log.finest(DebugLogger.quote(this.lc.getCurrentFunction().getName()), " --dynamicScopeCount = ", this.dynamicScopeCount, " at: ", node, node.getClass());
        }
    }

    @Override
    public boolean enterWithNode(WithNode node) {
        this.withBodies.add(node.getBody());
        return true;
    }

    @Override
    public boolean enterBlock(Block block) {
        if (this.compiler.isOnDemandCompilation()) {
            return true;
        }
        if (this.isDynamicScopeBoundary(block)) {
            this.increaseDynamicScopeCount(block);
        }
        if (!this.lc.isFunctionBody()) {
            return true;
        }
        FunctionNode fn = this.lc.getCurrentFunction();
        final HashSet symbols = new HashSet();
        block.accept(new SimpleNodeVisitor(){

            @Override
            public boolean enterIdentNode(IdentNode identNode) {
                Symbol symbol = identNode.getSymbol();
                if (symbol != null && symbol.isScope()) {
                    symbols.add(symbol);
                }
                return true;
            }
        });
        HashMap<String, Integer> internals = new HashMap<String, Integer>();
        Block globalBlock = FindScopeDepths.findGlobalBlock(this.lc, block);
        Block bodyBlock = FindScopeDepths.findBodyBlock(this.lc, fn, block);
        assert (globalBlock != null);
        assert (bodyBlock != null);
        block0: for (Symbol symbol : symbols) {
            boolean internal;
            int internalDepth = FindScopeDepths.findInternalDepth(this.lc, fn, block, symbol);
            boolean bl = internal = internalDepth >= 0;
            if (internal) {
                internals.put(symbol.getName(), internalDepth);
            }
            if (internal) continue;
            int depthAtStart = 0;
            Iterator<Block> iter = this.lc.getAncestorBlocks(bodyBlock);
            while (iter.hasNext()) {
                Block b2 = iter.next();
                if (FindScopeDepths.definedInBlock(b2, symbol)) {
                    this.addExternalSymbol(fn, symbol, depthAtStart);
                    continue block0;
                }
                if (!b2.needsScope()) continue;
                ++depthAtStart;
            }
        }
        this.addInternalSymbols(fn, internals.keySet());
        if (this.log.isEnabled()) {
            this.log.info(fn.getName() + " internals=" + internals + " externals=" + this.externalSymbolDepths.get(fn.getId()));
        }
        return true;
    }

    @Override
    public Node leaveBlock(Block block) {
        if (this.compiler.isOnDemandCompilation()) {
            return block;
        }
        if (this.isDynamicScopeBoundary(block)) {
            this.decreaseDynamicScopeCount(block);
        }
        return block;
    }

    private void addInternalSymbols(FunctionNode functionNode, Set<String> symbols) {
        int fnId = functionNode.getId();
        assert (this.internalSymbols.get(fnId) == null || this.internalSymbols.get(fnId).equals(symbols));
        this.internalSymbols.put(fnId, symbols);
    }

    private void addExternalSymbol(FunctionNode functionNode, Symbol symbol, int depthAtStart) {
        int fnId = functionNode.getId();
        Map<String, Integer> depths = this.externalSymbolDepths.get(fnId);
        if (depths == null) {
            depths = new HashMap<String, Integer>();
            this.externalSymbolDepths.put(fnId, depths);
        }
        depths.put(symbol.getName(), depthAtStart);
    }
}

