/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.HashSet;
import java.util.Set;
import jdk.nashorn.internal.IntDeque;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

class ProgramPoints
extends SimpleNodeVisitor {
    private final IntDeque nextProgramPoint = new IntDeque();
    private final Set<Node> noProgramPoint = new HashSet<Node>();

    ProgramPoints() {
    }

    private int next() {
        int next = this.nextProgramPoint.getAndIncrement();
        if (next > 0x1FFFFF) {
            throw new AssertionError((Object)"Function has more than 2097151 program points");
        }
        return next;
    }

    @Override
    public boolean enterFunctionNode(FunctionNode functionNode) {
        this.nextProgramPoint.push(1);
        return true;
    }

    @Override
    public Node leaveFunctionNode(FunctionNode functionNode) {
        this.nextProgramPoint.pop();
        return functionNode;
    }

    private Expression setProgramPoint(Optimistic optimistic) {
        if (this.noProgramPoint.contains(optimistic)) {
            return (Expression)((Object)optimistic);
        }
        return (Expression)((Object)(optimistic.canBeOptimistic() ? optimistic.setProgramPoint(this.next()) : optimistic));
    }

    @Override
    public boolean enterVarNode(VarNode varNode) {
        this.noProgramPoint.add(varNode.getName());
        return true;
    }

    @Override
    public boolean enterIdentNode(IdentNode identNode) {
        if (identNode.isInternal()) {
            this.noProgramPoint.add(identNode);
        }
        return true;
    }

    @Override
    public Node leaveIdentNode(IdentNode identNode) {
        if (identNode.isPropertyName()) {
            return identNode;
        }
        return this.setProgramPoint(identNode);
    }

    @Override
    public Node leaveCallNode(CallNode callNode) {
        return this.setProgramPoint(callNode);
    }

    @Override
    public Node leaveAccessNode(AccessNode accessNode) {
        return this.setProgramPoint(accessNode);
    }

    @Override
    public Node leaveIndexNode(IndexNode indexNode) {
        return this.setProgramPoint(indexNode);
    }

    @Override
    public Node leaveBinaryNode(BinaryNode binaryNode) {
        return this.setProgramPoint(binaryNode);
    }

    @Override
    public Node leaveUnaryNode(UnaryNode unaryNode) {
        return this.setProgramPoint(unaryNode);
    }
}

