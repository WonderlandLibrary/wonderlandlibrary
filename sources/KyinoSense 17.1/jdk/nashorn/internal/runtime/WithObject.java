/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import java.lang.invoke.TypeDescriptor;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.FindProperty;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.Scope;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.nashorn.internal.runtime.linker.NashornGuards;

public final class WithObject
extends Scope {
    private static final MethodHandle WITHEXPRESSIONGUARD = WithObject.findOwnMH("withExpressionGuard", Boolean.TYPE, Object.class, PropertyMap.class, SwitchPoint[].class);
    private static final MethodHandle WITHEXPRESSIONFILTER = WithObject.findOwnMH("withFilterExpression", Object.class, Object.class);
    private static final MethodHandle WITHSCOPEFILTER = WithObject.findOwnMH("withFilterScope", Object.class, Object.class);
    private static final MethodHandle BIND_TO_EXPRESSION_OBJ = WithObject.findOwnMH("bindToExpression", Object.class, Object.class, Object.class);
    private static final MethodHandle BIND_TO_EXPRESSION_FN = WithObject.findOwnMH("bindToExpression", Object.class, ScriptFunction.class, Object.class);
    private final ScriptObject expression;

    WithObject(ScriptObject scope, ScriptObject expression) {
        super(scope, null);
        this.expression = expression;
    }

    @Override
    public boolean delete(Object key, boolean strict) {
        ScriptObject self = this.expression;
        String propName = JSType.toString(key);
        FindProperty find = self.findProperty(propName, true);
        if (find != null) {
            return self.delete(propName, strict);
        }
        return false;
    }

    @Override
    public GuardedInvocation lookup(CallSiteDescriptor desc, LinkRequest request) {
        String name;
        boolean isNamedOperation;
        if (request.isCallSiteUnstable()) {
            return super.lookup(desc, request);
        }
        NashornCallSiteDescriptor ndesc = (NashornCallSiteDescriptor)desc;
        FindProperty find = null;
        GuardedInvocation link = null;
        if (desc.getNameTokenCount() > 2) {
            isNamedOperation = true;
            name = desc.getNameToken(2);
        } else {
            isNamedOperation = false;
            name = null;
        }
        ScriptObject self = this.expression;
        if (isNamedOperation) {
            find = self.findProperty(name, true);
        }
        if (find != null && (link = self.lookup(desc, request)) != null) {
            return WithObject.fixExpressionCallSite(ndesc, link);
        }
        ScriptObject scope = this.getProto();
        if (isNamedOperation) {
            find = scope.findProperty(name, true);
        }
        if (find != null) {
            return this.fixScopeCallSite(scope.lookup(desc, request), name, find.getOwner());
        }
        if (self != null) {
            String fallBack;
            String operator;
            switch (operator = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0)) {
                case "callMethod": {
                    throw new AssertionError();
                }
                case "getMethod": {
                    fallBack = "__noSuchMethod__";
                    break;
                }
                case "getProp": 
                case "getElem": {
                    fallBack = "__noSuchProperty__";
                    break;
                }
                default: {
                    fallBack = null;
                }
            }
            if (fallBack != null && (find = self.findProperty(fallBack, true)) != null) {
                switch (operator) {
                    case "getMethod": {
                        link = self.noSuchMethod(desc, request);
                        break;
                    }
                    case "getProp": 
                    case "getElem": {
                        link = self.noSuchProperty(desc, request);
                        break;
                    }
                }
            }
            if (link != null) {
                return WithObject.fixExpressionCallSite(ndesc, link);
            }
        }
        if ((link = scope.lookup(desc, request)) != null) {
            return this.fixScopeCallSite(link, name, null);
        }
        return null;
    }

    @Override
    protected FindProperty findProperty(String key, boolean deep, ScriptObject start) {
        FindProperty exprProperty = this.expression.findProperty(key, true, this.expression);
        if (exprProperty != null) {
            return exprProperty;
        }
        return super.findProperty(key, deep, start);
    }

    @Override
    protected Object invokeNoSuchProperty(String name, boolean isScope, int programPoint) {
        Object func;
        FindProperty find = this.expression.findProperty("__noSuchProperty__", true);
        if (find != null && (func = find.getObjectValue()) instanceof ScriptFunction) {
            ScriptFunction sfunc = (ScriptFunction)func;
            ScriptObject self = isScope && sfunc.isStrict() ? ScriptRuntime.UNDEFINED : this.expression;
            return ScriptRuntime.apply(sfunc, self, name);
        }
        return this.getProto().invokeNoSuchProperty(name, isScope, programPoint);
    }

    @Override
    public void setSplitState(int state) {
        ((Scope)this.getNonWithParent()).setSplitState(state);
    }

    @Override
    public int getSplitState() {
        return ((Scope)this.getNonWithParent()).getSplitState();
    }

    @Override
    public void addBoundProperties(ScriptObject source, Property[] properties) {
        this.getNonWithParent().addBoundProperties(source, properties);
    }

    private ScriptObject getNonWithParent() {
        ScriptObject proto;
        for (proto = this.getProto(); proto != null && proto instanceof WithObject; proto = proto.getProto()) {
        }
        return proto;
    }

    private static GuardedInvocation fixReceiverType(GuardedInvocation link, MethodHandle filter) {
        MethodType invType = link.getInvocation().type();
        MethodType newInvType = invType.changeParameterType(0, (Class<?>)filter.type().returnType());
        return link.asType(newInvType);
    }

    private static GuardedInvocation fixExpressionCallSite(NashornCallSiteDescriptor desc, GuardedInvocation link) {
        if (!"getMethod".equals(desc.getFirstOperator())) {
            return WithObject.fixReceiverType(link, WITHEXPRESSIONFILTER).filterArguments(0, WITHEXPRESSIONFILTER);
        }
        MethodHandle linkInvocation = link.getInvocation();
        MethodType linkType = linkInvocation.type();
        boolean linkReturnsFunction = ScriptFunction.class.isAssignableFrom((Class<?>)linkType.returnType());
        return link.replaceMethods(Lookup.MH.foldArguments(linkReturnsFunction ? BIND_TO_EXPRESSION_FN : BIND_TO_EXPRESSION_OBJ, WithObject.filterReceiver(linkInvocation.asType(linkType.changeReturnType(linkReturnsFunction ? ScriptFunction.class : Object.class).changeParameterType(0, Object.class)), WITHEXPRESSIONFILTER)), WithObject.filterGuardReceiver(link, WITHEXPRESSIONFILTER));
    }

    private GuardedInvocation fixScopeCallSite(GuardedInvocation link, String name, ScriptObject owner) {
        GuardedInvocation newLink = WithObject.fixReceiverType(link, WITHSCOPEFILTER);
        MethodHandle expressionGuard = this.expressionGuard(name, owner);
        MethodHandle filterGuardReceiver = WithObject.filterGuardReceiver(newLink, WITHSCOPEFILTER);
        return link.replaceMethods(WithObject.filterReceiver(newLink.getInvocation(), WITHSCOPEFILTER), NashornGuards.combineGuards(expressionGuard, filterGuardReceiver));
    }

    private static MethodHandle filterGuardReceiver(GuardedInvocation link, MethodHandle receiverFilter) {
        MethodHandle test = link.getGuard();
        if (test == null) {
            return null;
        }
        TypeDescriptor.OfField receiverType = test.type().parameterType(0);
        MethodHandle filter = Lookup.MH.asType(receiverFilter, receiverFilter.type().changeParameterType(0, (Class<?>)receiverType).changeReturnType((Class<?>)receiverType));
        return WithObject.filterReceiver(test, filter);
    }

    private static MethodHandle filterReceiver(MethodHandle mh, MethodHandle receiverFilter) {
        return Lookup.MH.filterArguments(mh, 0, receiverFilter.asType(receiverFilter.type().changeReturnType((Class<?>)mh.type().parameterType(0))));
    }

    public static Object withFilterExpression(Object receiver) {
        return ((WithObject)receiver).expression;
    }

    private static Object bindToExpression(Object fn, final Object receiver) {
        ScriptObjectMirror mirror;
        if (fn instanceof ScriptFunction) {
            return WithObject.bindToExpression((ScriptFunction)fn, receiver);
        }
        if (fn instanceof ScriptObjectMirror && (mirror = (ScriptObjectMirror)fn).isFunction()) {
            return new AbstractJSObject(){

                @Override
                public Object call(Object thiz, Object ... args2) {
                    return mirror.call(WithObject.withFilterExpression(receiver), args2);
                }
            };
        }
        return fn;
    }

    private static Object bindToExpression(ScriptFunction fn, Object receiver) {
        return fn.createBound(WithObject.withFilterExpression(receiver), ScriptRuntime.EMPTY_ARRAY);
    }

    private MethodHandle expressionGuard(String name, ScriptObject owner) {
        PropertyMap map = this.expression.getMap();
        SwitchPoint[] sp = this.expression.getProtoSwitchPoints(name, owner);
        return Lookup.MH.insertArguments(WITHEXPRESSIONGUARD, 1, map, sp);
    }

    private static boolean withExpressionGuard(Object receiver, PropertyMap map, SwitchPoint[] sp) {
        return ((WithObject)receiver).expression.getMap() == map && !WithObject.hasBeenInvalidated(sp);
    }

    private static boolean hasBeenInvalidated(SwitchPoint[] switchPoints) {
        if (switchPoints != null) {
            for (SwitchPoint switchPoint : switchPoints) {
                if (!switchPoint.hasBeenInvalidated()) continue;
                return true;
            }
        }
        return false;
    }

    public static Object withFilterScope(Object receiver) {
        return ((WithObject)receiver).getProto();
    }

    public ScriptObject getExpression() {
        return this.expression;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), WithObject.class, name, Lookup.MH.type(rtype, types));
    }
}

