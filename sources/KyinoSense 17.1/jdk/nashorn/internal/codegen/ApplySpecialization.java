/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.lang.invoke.MethodType;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.TypeMap;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.ir.visitor.SimpleNodeVisitor;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.options.Options;

@Logger(name="apply2call")
public final class ApplySpecialization
extends SimpleNodeVisitor
implements Loggable {
    private static final boolean USE_APPLY2CALL = Options.getBooleanProperty("nashorn.apply2call", true);
    private final DebugLogger log;
    private final Compiler compiler;
    private final Set<Integer> changed = new HashSet<Integer>();
    private final Deque<List<IdentNode>> explodedArguments = new ArrayDeque<List<IdentNode>>();
    private final Deque<MethodType> callSiteTypes = new ArrayDeque<MethodType>();
    private static final String ARGUMENTS = CompilerConstants.ARGUMENTS_VAR.symbolName();
    private static final AppliesFoundException HAS_APPLIES = new AppliesFoundException();

    public ApplySpecialization(Compiler compiler) {
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

    private boolean hasApplies(final FunctionNode functionNode) {
        try {
            functionNode.accept((NodeVisitor)new SimpleNodeVisitor(){

                @Override
                public boolean enterFunctionNode(FunctionNode fn) {
                    return fn == functionNode;
                }

                @Override
                public boolean enterCallNode(CallNode callNode) {
                    if (ApplySpecialization.isApply(callNode)) {
                        throw HAS_APPLIES;
                    }
                    return true;
                }
            });
        }
        catch (AppliesFoundException e) {
            return true;
        }
        this.log.fine("There are no applies in ", DebugLogger.quote(functionNode.getName()), " - nothing to do.");
        return false;
    }

    private static void checkValidTransform(final FunctionNode functionNode) {
        final HashSet argumentsFound = new HashSet();
        final ArrayDeque stack = new ArrayDeque();
        functionNode.accept((NodeVisitor)new SimpleNodeVisitor(){

            private boolean isCurrentArg(Expression expr) {
                return !stack.isEmpty() && ((Set)stack.peek()).contains(expr);
            }

            private boolean isArguments(Expression expr) {
                if (expr instanceof IdentNode && ARGUMENTS.equals(((IdentNode)expr).getName())) {
                    argumentsFound.add(expr);
                    return true;
                }
                return false;
            }

            private boolean isParam(String name) {
                for (IdentNode param : functionNode.getParameters()) {
                    if (!param.getName().equals(name)) continue;
                    return true;
                }
                return false;
            }

            @Override
            public Node leaveIdentNode(IdentNode identNode) {
                if (this.isParam(identNode.getName())) {
                    throw new TransformFailedException(this.lc.getCurrentFunction(), "parameter: " + identNode.getName());
                }
                if (this.isArguments(identNode) && !this.isCurrentArg(identNode)) {
                    throw new TransformFailedException(this.lc.getCurrentFunction(), "is 'arguments': " + identNode.getName());
                }
                return identNode;
            }

            @Override
            public boolean enterCallNode(CallNode callNode) {
                HashSet<Expression> callArgs = new HashSet<Expression>();
                if (ApplySpecialization.isApply(callNode)) {
                    List<Expression> argList = callNode.getArgs();
                    if (argList.size() != 2 || !this.isArguments(argList.get(argList.size() - 1))) {
                        throw new TransformFailedException(this.lc.getCurrentFunction(), "argument pattern not matched: " + argList);
                    }
                    callArgs.addAll(callNode.getArgs());
                }
                stack.push(callArgs);
                return true;
            }

            @Override
            public Node leaveCallNode(CallNode callNode) {
                stack.pop();
                return callNode;
            }
        });
    }

    @Override
    public boolean enterCallNode(CallNode callNode) {
        return !this.explodedArguments.isEmpty();
    }

    @Override
    public Node leaveCallNode(CallNode callNode) {
        List<IdentNode> newParams = this.explodedArguments.peek();
        if (ApplySpecialization.isApply(callNode)) {
            ArrayList<Expression> newArgs = new ArrayList<Expression>();
            for (Expression arg : callNode.getArgs()) {
                if (arg instanceof IdentNode && ARGUMENTS.equals(((IdentNode)arg).getName())) {
                    newArgs.addAll(newParams);
                    continue;
                }
                newArgs.add(arg);
            }
            this.changed.add(this.lc.getCurrentFunction().getId());
            CallNode newCallNode = callNode.setArgs(newArgs).setIsApplyToCall();
            if (this.log.isEnabled()) {
                this.log.fine("Transformed ", callNode, " from apply to call => ", newCallNode, " in ", DebugLogger.quote(this.lc.getCurrentFunction().getName()));
            }
            return newCallNode;
        }
        return callNode;
    }

    private void pushExplodedArgs(FunctionNode functionNode) {
        int start = 0;
        MethodType actualCallSiteType = this.compiler.getCallSiteType(functionNode);
        if (actualCallSiteType == null) {
            throw new TransformFailedException(this.lc.getCurrentFunction(), "No callsite type");
        }
        assert (actualCallSiteType.parameterType(actualCallSiteType.parameterCount() - 1) != Object[].class) : "error vararg callsite passed to apply2call " + functionNode.getName() + " " + actualCallSiteType;
        TypeMap ptm = this.compiler.getTypeMap();
        if (ptm.needsCallee()) {
            ++start;
        }
        ++start;
        assert (functionNode.getNumOfParams() == 0) : "apply2call on function with named paramaters!";
        ArrayList<IdentNode> newParams = new ArrayList<IdentNode>();
        long to = actualCallSiteType.parameterCount() - start;
        int i = 0;
        while ((long)i < to) {
            newParams.add(new IdentNode(functionNode.getToken(), functionNode.getFinish(), CompilerConstants.EXPLODED_ARGUMENT_PREFIX.symbolName() + i));
            ++i;
        }
        this.callSiteTypes.push(actualCallSiteType);
        this.explodedArguments.push(newParams);
    }

    @Override
    public boolean enterFunctionNode(FunctionNode functionNode) {
        if (!USE_APPLY2CALL || !this.compiler.isOnDemandCompilation() || !functionNode.needsArguments() || functionNode.hasEval() || functionNode.getNumOfParams() != 0) {
            return false;
        }
        if (!Global.isBuiltinFunctionPrototypeApply()) {
            this.log.fine("Apply transform disabled: apply/call overridden");
            assert (!Global.isBuiltinFunctionPrototypeCall()) : "call and apply should have the same SwitchPoint";
            return false;
        }
        if (!this.hasApplies(functionNode)) {
            return false;
        }
        if (this.log.isEnabled()) {
            this.log.info("Trying to specialize apply to call in '", functionNode.getName(), "' params=", functionNode.getParameters(), " id=", functionNode.getId(), " source=", ApplySpecialization.massageURL(functionNode.getSource().getURL()));
        }
        try {
            ApplySpecialization.checkValidTransform(functionNode);
            this.pushExplodedArgs(functionNode);
        }
        catch (TransformFailedException e) {
            this.log.info("Failure: ", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Node leaveFunctionNode(FunctionNode functionNode) {
        FunctionNode newFunctionNode = functionNode;
        String functionName = newFunctionNode.getName();
        if (this.changed.contains(newFunctionNode.getId())) {
            newFunctionNode = newFunctionNode.clearFlag(this.lc, 8).setFlag(this.lc, 4096).setParameters(this.lc, this.explodedArguments.peek());
            if (this.log.isEnabled()) {
                this.log.info("Success: ", ApplySpecialization.massageURL(newFunctionNode.getSource().getURL()), Character.valueOf('.'), functionName, "' id=", newFunctionNode.getId(), " params=", this.callSiteTypes.peek());
            }
        }
        this.callSiteTypes.pop();
        this.explodedArguments.pop();
        return newFunctionNode;
    }

    private static boolean isApply(CallNode callNode) {
        Expression f = callNode.getFunction();
        return f instanceof AccessNode && "apply".equals(((AccessNode)f).getProperty());
    }

    private static String massageURL(URL url) {
        if (url == null) {
            return "<null>";
        }
        String str = url.toString();
        int slash = str.lastIndexOf(47);
        if (slash == -1) {
            return str;
        }
        return str.substring(slash + 1);
    }

    private static class AppliesFoundException
    extends RuntimeException {
        AppliesFoundException() {
            super("applies_found", null, false, false);
        }
    }

    private static class TransformFailedException
    extends RuntimeException {
        TransformFailedException(FunctionNode fn, String message) {
            super(ApplySpecialization.massageURL(fn.getSource().getURL()) + '.' + fn.getName() + " => " + message, null, false, false);
        }
    }
}

