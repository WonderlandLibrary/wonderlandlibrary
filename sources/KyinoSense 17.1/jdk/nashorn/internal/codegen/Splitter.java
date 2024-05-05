/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.nashorn.internal.codegen.CodeGenerator;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.WeighNodes;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.LiteralNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.ObjectNode;
import jdk.nashorn.internal.ir.PropertyNode;
import jdk.nashorn.internal.ir.SplitNode;
import jdk.nashorn.internal.ir.Splittable;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.options.Options;

@Logger(name="splitter")
final class Splitter
extends SimpleNodeVisitor
implements Loggable {
    private final Compiler compiler;
    private final FunctionNode outermost;
    private final CompileUnit outermostCompileUnit;
    private final Map<Node, Long> weightCache = new HashMap<Node, Long>();
    public static final long SPLIT_THRESHOLD = Options.getIntProperty("nashorn.compiler.splitter.threshold", 32768);
    private final DebugLogger log;

    public Splitter(Compiler compiler, FunctionNode functionNode, CompileUnit outermostCompileUnit) {
        this.compiler = compiler;
        this.outermost = functionNode;
        this.outermostCompileUnit = outermostCompileUnit;
        this.log = this.initLogger(compiler.getContext());
    }

    @Override
    public DebugLogger initLogger(Context context) {
        return context.getLogger(this.getClass());
    }

    @Override
    public DebugLogger getLogger() {
        return this.log;
    }

    FunctionNode split(FunctionNode fn, boolean top) {
        FunctionNode functionNode = fn;
        this.log.fine("Initiating split of '", functionNode.getName(), "'");
        long weight = WeighNodes.weigh(functionNode);
        assert (this.lc.isEmpty()) : "LexicalContext not empty";
        if (weight >= SPLIT_THRESHOLD) {
            this.log.info("Splitting '", functionNode.getName(), "' as its weight ", weight, " exceeds split threshold ", SPLIT_THRESHOLD);
            functionNode = (FunctionNode)functionNode.accept((NodeVisitor)this);
            if (functionNode.isSplit()) {
                weight = WeighNodes.weigh(functionNode, this.weightCache);
                functionNode = functionNode.setBody(null, functionNode.getBody().setNeedsScope(null));
            }
            if (weight >= SPLIT_THRESHOLD) {
                functionNode = functionNode.setBody(null, this.splitBlock(functionNode.getBody(), functionNode));
                functionNode = functionNode.setFlag(null, 16);
                weight = WeighNodes.weigh(functionNode.getBody(), this.weightCache);
            }
        }
        assert (functionNode.getCompileUnit() == null) : "compile unit already set for " + functionNode.getName();
        if (top) {
            assert (this.outermostCompileUnit != null) : "outermost compile unit is null";
            functionNode = functionNode.setCompileUnit(null, this.outermostCompileUnit);
            this.outermostCompileUnit.addWeight(weight + 40L);
        } else {
            functionNode = functionNode.setCompileUnit(null, this.findUnit(weight));
        }
        Block body = functionNode.getBody();
        final List<FunctionNode> dc = Splitter.directChildren(functionNode);
        Block newBody = (Block)body.accept(new SimpleNodeVisitor(){

            @Override
            public boolean enterFunctionNode(FunctionNode nestedFunction) {
                return dc.contains(nestedFunction);
            }

            @Override
            public Node leaveFunctionNode(FunctionNode nestedFunction) {
                FunctionNode split = new Splitter(Splitter.this.compiler, nestedFunction, Splitter.this.outermostCompileUnit).split(nestedFunction, false);
                this.lc.replace(nestedFunction, split);
                return split;
            }
        });
        functionNode = functionNode.setBody(null, newBody);
        assert (functionNode.getCompileUnit() != null);
        return functionNode;
    }

    private static List<FunctionNode> directChildren(final FunctionNode functionNode) {
        final ArrayList<FunctionNode> dc = new ArrayList<FunctionNode>();
        functionNode.accept((NodeVisitor)new SimpleNodeVisitor(){

            @Override
            public boolean enterFunctionNode(FunctionNode child) {
                if (child == functionNode) {
                    return true;
                }
                if (this.lc.getParentFunction(child) == functionNode) {
                    dc.add(child);
                }
                return false;
            }
        });
        return dc;
    }

    protected CompileUnit findUnit(long weight) {
        return this.compiler.findUnit(weight);
    }

    private Block splitBlock(Block block, FunctionNode function) {
        ArrayList<Statement> splits = new ArrayList<Statement>();
        ArrayList<Statement> statements = new ArrayList<Statement>();
        long statementsWeight = 0L;
        for (Statement statement : block.getStatements()) {
            long weight = WeighNodes.weigh(statement, this.weightCache);
            if ((statementsWeight + weight >= SPLIT_THRESHOLD || statement.isTerminal()) && !statements.isEmpty()) {
                splits.add(this.createBlockSplitNode(block, function, statements, statementsWeight));
                statements = new ArrayList();
                statementsWeight = 0L;
            }
            if (statement.isTerminal()) {
                splits.add(statement);
                continue;
            }
            statements.add(statement);
            statementsWeight += weight;
        }
        if (!statements.isEmpty()) {
            splits.add(this.createBlockSplitNode(block, function, statements, statementsWeight));
        }
        return block.setStatements(this.lc, splits);
    }

    private SplitNode createBlockSplitNode(Block parent, FunctionNode function, List<Statement> statements, long weight) {
        long token = parent.getToken();
        int finish = parent.getFinish();
        String name = function.uniqueName(CompilerConstants.SPLIT_PREFIX.symbolName());
        Block newBlock = new Block(token, finish, statements);
        return new SplitNode(name, newBlock, this.compiler.findUnit(weight + 40L));
    }

    @Override
    public boolean enterBlock(Block block) {
        if (block.isCatchBlock()) {
            return false;
        }
        long weight = WeighNodes.weigh(block, this.weightCache);
        if (weight < SPLIT_THRESHOLD) {
            this.weightCache.put(block, weight);
            return false;
        }
        return true;
    }

    @Override
    public Node leaveBlock(Block block) {
        assert (!block.isCatchBlock());
        Block newBlock = block;
        long weight = WeighNodes.weigh(block, this.weightCache);
        if (weight >= SPLIT_THRESHOLD) {
            FunctionNode currentFunction = this.lc.getCurrentFunction();
            newBlock = this.splitBlock(block, currentFunction);
            weight = WeighNodes.weigh(newBlock, this.weightCache);
            this.lc.setFlag(currentFunction, 16);
        }
        this.weightCache.put(newBlock, weight);
        return newBlock;
    }

    @Override
    public Node leaveLiteralNode(LiteralNode literal) {
        long weight = WeighNodes.weigh(literal);
        if (weight < SPLIT_THRESHOLD) {
            return literal;
        }
        FunctionNode functionNode = this.lc.getCurrentFunction();
        this.lc.setFlag(functionNode, 16);
        if (literal instanceof LiteralNode.ArrayLiteralNode) {
            LiteralNode.ArrayLiteralNode arrayLiteralNode = (LiteralNode.ArrayLiteralNode)literal;
            Node[] value = (Node[])arrayLiteralNode.getValue();
            int[] postsets = arrayLiteralNode.getPostsets();
            ArrayList<Splittable.SplitRange> ranges = new ArrayList<Splittable.SplitRange>();
            long totalWeight = 0L;
            int lo = 0;
            for (int i = 0; i < postsets.length; ++i) {
                int postset = postsets[i];
                Node element = value[postset];
                weight = WeighNodes.weigh(element);
                if ((totalWeight += 2L + weight) < SPLIT_THRESHOLD) continue;
                CompileUnit unit = this.compiler.findUnit(totalWeight - weight);
                ranges.add(new Splittable.SplitRange(unit, lo, i));
                lo = i;
                totalWeight = weight;
            }
            if (lo != postsets.length) {
                CompileUnit unit = this.compiler.findUnit(totalWeight);
                ranges.add(new Splittable.SplitRange(unit, lo, postsets.length));
            }
            return arrayLiteralNode.setSplitRanges(this.lc, ranges);
        }
        return literal;
    }

    @Override
    public Node leaveObjectNode(ObjectNode objectNode) {
        long weight = WeighNodes.weigh(objectNode);
        if (weight < SPLIT_THRESHOLD) {
            return objectNode;
        }
        FunctionNode functionNode = this.lc.getCurrentFunction();
        this.lc.setFlag(functionNode, 16);
        ArrayList<Splittable.SplitRange> ranges = new ArrayList<Splittable.SplitRange>();
        List<PropertyNode> properties = objectNode.getElements();
        boolean isSpillObject = properties.size() > CodeGenerator.OBJECT_SPILL_THRESHOLD;
        long totalWeight = 0L;
        int lo = 0;
        for (int i = 0; i < properties.size(); ++i) {
            PropertyNode property = properties.get(i);
            boolean isConstant = LiteralNode.isConstant(property.getValue());
            if (isConstant && isSpillObject) continue;
            long l = weight = isConstant ? 0L : WeighNodes.weigh(property.getValue());
            if ((totalWeight += 2L + weight) < SPLIT_THRESHOLD) continue;
            CompileUnit unit = this.compiler.findUnit(totalWeight - weight);
            ranges.add(new Splittable.SplitRange(unit, lo, i));
            lo = i;
            totalWeight = weight;
        }
        if (lo != properties.size()) {
            CompileUnit unit = this.compiler.findUnit(totalWeight);
            ranges.add(new Splittable.SplitRange(unit, lo, properties.size()));
        }
        return objectNode.setSplitRanges(this.lc, ranges);
    }

    @Override
    public boolean enterFunctionNode(FunctionNode node) {
        return node == this.outermost;
    }
}

