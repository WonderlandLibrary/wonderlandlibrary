/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;

class CacheAst
extends SimpleNodeVisitor {
    private final Deque<RecompilableScriptFunctionData> dataStack = new ArrayDeque<RecompilableScriptFunctionData>();
    private final Compiler compiler;

    CacheAst(Compiler compiler) {
        this.compiler = compiler;
        assert (!compiler.isOnDemandCompilation());
    }

    @Override
    public boolean enterFunctionNode(FunctionNode functionNode) {
        int id = functionNode.getId();
        this.dataStack.push(this.dataStack.isEmpty() ? this.compiler.getScriptFunctionData(id) : this.dataStack.peek().getScriptFunctionData(id));
        return true;
    }

    @Override
    public Node leaveFunctionNode(FunctionNode functionNode) {
        RecompilableScriptFunctionData data = this.dataStack.pop();
        if (functionNode.isSplit()) {
            data.setCachedAst(functionNode);
        }
        if (!this.dataStack.isEmpty() && (this.dataStack.peek().getFunctionFlags() & 0x10) != 0) {
            return functionNode.setBody(this.lc, functionNode.getBody().setStatements(null, Collections.emptyList()));
        }
        return functionNode;
    }
}

