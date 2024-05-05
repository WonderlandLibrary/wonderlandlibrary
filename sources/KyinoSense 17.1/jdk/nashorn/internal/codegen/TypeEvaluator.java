/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.lang.invoke.MethodType;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.AccessNode;
import jdk.nashorn.internal.ir.CallNode;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.IndexNode;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.objects.ArrayBufferView;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.FindProperty;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Undefined;

final class TypeEvaluator {
    private static final MethodType EMPTY_INVOCATION_TYPE = MethodType.methodType(Object.class, ScriptFunction.class, Object.class);
    private final Compiler compiler;
    private final ScriptObject runtimeScope;

    TypeEvaluator(Compiler compiler, ScriptObject runtimeScope) {
        this.compiler = compiler;
        this.runtimeScope = runtimeScope;
    }

    boolean hasStringPropertyIterator(Expression expr) {
        return this.evaluateSafely(expr) instanceof ScriptObject;
    }

    Type getOptimisticType(Optimistic node) {
        assert (this.compiler.useOptimisticTypes());
        int programPoint = node.getProgramPoint();
        Type validType = this.compiler.getInvalidatedProgramPointType(programPoint);
        if (validType != null) {
            return validType;
        }
        Type mostOptimisticType = node.getMostOptimisticType();
        Type evaluatedType = this.getEvaluatedType(node);
        if (evaluatedType != null) {
            if (evaluatedType.widerThan(mostOptimisticType)) {
                Type newValidType = evaluatedType.isObject() || evaluatedType.isBoolean() ? Type.OBJECT : evaluatedType;
                this.compiler.addInvalidatedProgramPoint(node.getProgramPoint(), newValidType);
            }
            return evaluatedType;
        }
        return mostOptimisticType;
    }

    private static Type getPropertyType(ScriptObject sobj, String name) {
        Undefined value;
        FindProperty find = sobj.findProperty(name, true);
        if (find == null) {
            return null;
        }
        Property property = find.getProperty();
        Class<?> propertyClass = property.getType();
        if (propertyClass == null) {
            return null;
        }
        if (propertyClass.isPrimitive()) {
            return Type.typeFor(propertyClass);
        }
        ScriptObject owner = find.getOwner();
        if (property.hasGetterFunction(owner)) {
            return Type.OBJECT;
        }
        Undefined undefined = value = property.needsDeclaration() ? ScriptRuntime.UNDEFINED : property.getObjectValue(owner, owner);
        if (value == ScriptRuntime.UNDEFINED) {
            return null;
        }
        return Type.typeFor(JSType.unboxedFieldType(value));
    }

    void declareLocalSymbol(String symbolName) {
        assert (this.compiler.useOptimisticTypes() && this.compiler.isOnDemandCompilation() && this.runtimeScope != null) : "useOptimistic=" + this.compiler.useOptimisticTypes() + " isOnDemand=" + this.compiler.isOnDemandCompilation() + " scope=" + this.runtimeScope;
        if (this.runtimeScope.findProperty(symbolName, false) == null) {
            this.runtimeScope.addOwnProperty(symbolName, 7, ScriptRuntime.UNDEFINED);
        }
    }

    private Object evaluateSafely(Expression expr) {
        if (expr instanceof IdentNode) {
            return this.runtimeScope == null ? null : TypeEvaluator.evaluatePropertySafely(this.runtimeScope, ((IdentNode)expr).getName());
        }
        if (expr instanceof AccessNode) {
            AccessNode accessNode = (AccessNode)expr;
            Object base = this.evaluateSafely(accessNode.getBase());
            if (!(base instanceof ScriptObject)) {
                return null;
            }
            return TypeEvaluator.evaluatePropertySafely((ScriptObject)base, accessNode.getProperty());
        }
        return null;
    }

    private static Object evaluatePropertySafely(ScriptObject sobj, String name) {
        ScriptObject owner;
        FindProperty find = sobj.findProperty(name, true);
        if (find == null) {
            return null;
        }
        Property property = find.getProperty();
        if (property.hasGetterFunction(owner = find.getOwner())) {
            return null;
        }
        return property.getObjectValue(owner, owner);
    }

    private Type getEvaluatedType(Optimistic expr) {
        CallNode callExpr;
        Expression fnExpr;
        if (expr instanceof IdentNode) {
            if (this.runtimeScope == null) {
                return null;
            }
            return TypeEvaluator.getPropertyType(this.runtimeScope, ((IdentNode)expr).getName());
        }
        if (expr instanceof AccessNode) {
            AccessNode accessNode = (AccessNode)expr;
            Object base = this.evaluateSafely(accessNode.getBase());
            if (!(base instanceof ScriptObject)) {
                return null;
            }
            return TypeEvaluator.getPropertyType((ScriptObject)base, accessNode.getProperty());
        }
        if (expr instanceof IndexNode) {
            IndexNode indexNode = (IndexNode)expr;
            Object base = this.evaluateSafely(indexNode.getBase());
            if (base instanceof NativeArray || base instanceof ArrayBufferView) {
                return ((ScriptObject)base).getArray().getOptimisticType();
            }
        } else if (expr instanceof CallNode && (fnExpr = (callExpr = (CallNode)expr).getFunction()) instanceof FunctionNode && this.compiler.getContext().getEnv()._lazy_compilation) {
            RecompilableScriptFunctionData data;
            FunctionNode fn = (FunctionNode)fnExpr;
            if (callExpr.getArgs().isEmpty() && (data = this.compiler.getScriptFunctionData(fn.getId())) != null) {
                Type returnType = Type.typeFor(data.getReturnType(EMPTY_INVOCATION_TYPE, this.runtimeScope));
                if (returnType == Type.BOOLEAN) {
                    return Type.OBJECT;
                }
                assert (returnType == Type.INT || returnType == Type.NUMBER || returnType == Type.OBJECT);
                return returnType;
            }
        }
        return null;
    }
}

