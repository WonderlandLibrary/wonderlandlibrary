/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.ir.CompileUnitHolder;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.Splittable;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;

abstract class ReplaceCompileUnits
extends SimpleNodeVisitor {
    ReplaceCompileUnits() {
    }

    abstract CompileUnit getReplacement(CompileUnit var1);

    CompileUnit getExistingReplacement(CompileUnitHolder node) {
        CompileUnit oldUnit = node.getCompileUnit();
        assert (oldUnit != null);
        CompileUnit newUnit = this.getReplacement(oldUnit);
        assert (newUnit != null);
        return newUnit;
    }

    @Override
    public Node leaveFunctionNode(FunctionNode node) {
        return node.setCompileUnit(this.lc, this.getExistingReplacement(node));
    }

    @Override
    public Node leaveLiteralNode(LiteralNode<?> node) {
        if (node instanceof LiteralNode.ArrayLiteralNode) {
            LiteralNode.ArrayLiteralNode aln = (LiteralNode.ArrayLiteralNode)node;
            if (aln.getSplitRanges() == null) {
                return node;
            }
            ArrayList<Splittable.SplitRange> newArrayUnits = new ArrayList<Splittable.SplitRange>();
            for (Splittable.SplitRange au : aln.getSplitRanges()) {
                newArrayUnits.add(new Splittable.SplitRange(this.getExistingReplacement(au), au.getLow(), au.getHigh()));
            }
            return aln.setSplitRanges(this.lc, newArrayUnits);
        }
        return node;
    }

    @Override
    public Node leaveObjectNode(ObjectNode objectNode) {
        List<Splittable.SplitRange> ranges = objectNode.getSplitRanges();
        if (ranges != null) {
            ArrayList<Splittable.SplitRange> newRanges = new ArrayList<Splittable.SplitRange>();
            for (Splittable.SplitRange range : ranges) {
                newRanges.add(new Splittable.SplitRange(this.getExistingReplacement(range), range.getLow(), range.getHigh()));
            }
            return objectNode.setSplitRanges(this.lc, newRanges);
        }
        return super.leaveObjectNode(objectNode);
    }
}

