/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.codegen.types.ArrayType;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.LexicalContextNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.PropertyKey;
import jdk.nashorn.internal.ir.Splittable;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Undefined;

@Immutable
public abstract class LiteralNode<T>
extends Expression
implements PropertyKey {
    private static final long serialVersionUID = 1L;
    protected final T value;
    public static final Object POSTSET_MARKER = new Object();

    protected LiteralNode(long token, int finish, T value) {
        super(token, finish);
        this.value = value;
    }

    protected LiteralNode(LiteralNode<T> literalNode) {
        this(literalNode, literalNode.value);
    }

    protected LiteralNode(LiteralNode<T> literalNode, T newValue) {
        super(literalNode);
        this.value = newValue;
    }

    public LiteralNode<?> initialize(LexicalContext lc) {
        return this;
    }

    public boolean isNull() {
        return this.value == null;
    }

    @Override
    public Type getType() {
        return Type.typeFor(this.value.getClass());
    }

    @Override
    public String getPropertyName() {
        return JSType.toString(this.getObject());
    }

    public boolean getBoolean() {
        return JSType.toBoolean(this.value);
    }

    public int getInt32() {
        return JSType.toInt32(this.value);
    }

    public long getUint32() {
        return JSType.toUint32(this.value);
    }

    public long getLong() {
        return JSType.toLong(this.value);
    }

    public double getNumber() {
        return JSType.toNumber(this.value);
    }

    public String getString() {
        return JSType.toString(this.value);
    }

    public Object getObject() {
        return this.value;
    }

    public boolean isString() {
        return this.value instanceof String;
    }

    public boolean isNumeric() {
        return this.value instanceof Number;
    }

    @Override
    public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
        if (visitor.enterLiteralNode(this)) {
            return visitor.leaveLiteralNode(this);
        }
        return this;
    }

    @Override
    public void toString(StringBuilder sb, boolean printType) {
        if (this.value == null) {
            sb.append("null");
        } else {
            sb.append(this.value.toString());
        }
    }

    public final T getValue() {
        return this.value;
    }

    private static Expression[] valueToArray(List<Expression> value) {
        return value.toArray(new Expression[value.size()]);
    }

    public static LiteralNode<Object> newInstance(long token, int finish) {
        return new NullLiteralNode(token, finish);
    }

    public static LiteralNode<Object> newInstance(Node parent) {
        return new NullLiteralNode(parent.getToken(), parent.getFinish());
    }

    public static LiteralNode<Boolean> newInstance(long token, int finish, boolean value) {
        return new BooleanLiteralNode(token, finish, value);
    }

    public static LiteralNode<?> newInstance(Node parent, boolean value) {
        return new BooleanLiteralNode(parent.getToken(), parent.getFinish(), value);
    }

    public static LiteralNode<Number> newInstance(long token, int finish, Number value) {
        assert (!(value instanceof Long));
        return new NumberLiteralNode(token, finish, value);
    }

    public static LiteralNode<?> newInstance(Node parent, Number value) {
        return new NumberLiteralNode(parent.getToken(), parent.getFinish(), value);
    }

    public static LiteralNode<Undefined> newInstance(long token, int finish, Undefined value) {
        return new UndefinedLiteralNode(token, finish);
    }

    public static LiteralNode<?> newInstance(Node parent, Undefined value) {
        return new UndefinedLiteralNode(parent.getToken(), parent.getFinish());
    }

    public static LiteralNode<String> newInstance(long token, int finish, String value) {
        return new StringLiteralNode(token, finish, value);
    }

    public static LiteralNode<?> newInstance(Node parent, String value) {
        return new StringLiteralNode(parent.getToken(), parent.getFinish(), value);
    }

    public static LiteralNode<Lexer.LexerToken> newInstance(long token, int finish, Lexer.LexerToken value) {
        return new LexerTokenLiteralNode(token, finish, value);
    }

    public static LiteralNode<?> newInstance(Node parent, Lexer.LexerToken value) {
        return new LexerTokenLiteralNode(parent.getToken(), parent.getFinish(), value);
    }

    public static Object objectAsConstant(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Number || object instanceof String || object instanceof Boolean) {
            return object;
        }
        if (object instanceof LiteralNode) {
            return LiteralNode.objectAsConstant(((LiteralNode)object).getValue());
        }
        return POSTSET_MARKER;
    }

    public static boolean isConstant(Object object) {
        return LiteralNode.objectAsConstant(object) != POSTSET_MARKER;
    }

    public static LiteralNode<Expression[]> newInstance(long token, int finish, List<Expression> value) {
        return new ArrayLiteralNode(token, finish, LiteralNode.valueToArray(value));
    }

    public static LiteralNode<?> newInstance(Node parent, List<Expression> value) {
        return new ArrayLiteralNode(parent.getToken(), parent.getFinish(), LiteralNode.valueToArray(value));
    }

    public static LiteralNode<Expression[]> newInstance(long token, int finish, Expression[] value) {
        return new ArrayLiteralNode(token, finish, value);
    }

    @Immutable
    public static final class ArrayLiteralNode
    extends LiteralNode<Expression[]>
    implements LexicalContextNode,
    Splittable {
        private static final long serialVersionUID = 1L;
        private final Type elementType;
        private final Object presets;
        private final int[] postsets;
        private final List<Splittable.SplitRange> splitRanges;

        protected ArrayLiteralNode(long token, int finish, Expression[] value) {
            super(Token.recast(token, TokenType.ARRAY), finish, value);
            this.elementType = Type.UNKNOWN;
            this.presets = null;
            this.postsets = null;
            this.splitRanges = null;
        }

        private ArrayLiteralNode(ArrayLiteralNode node, Expression[] value, Type elementType, int[] postsets, Object presets, List<Splittable.SplitRange> splitRanges) {
            super(node, value);
            this.elementType = elementType;
            this.postsets = postsets;
            this.presets = presets;
            this.splitRanges = splitRanges;
        }

        public List<Expression> getElementExpressions() {
            return Collections.unmodifiableList(Arrays.asList((Object[])this.value));
        }

        public ArrayLiteralNode initialize(LexicalContext lc) {
            return Node.replaceInLexicalContext(lc, this, ArrayLiteralInitializer.initialize(this));
        }

        public ArrayType getArrayType() {
            return ArrayLiteralNode.getArrayType(this.getElementType());
        }

        private static ArrayType getArrayType(Type elementType) {
            if (elementType.isInteger()) {
                return Type.INT_ARRAY;
            }
            if (elementType.isNumeric()) {
                return Type.NUMBER_ARRAY;
            }
            return Type.OBJECT_ARRAY;
        }

        @Override
        public Type getType() {
            return Type.typeFor(NativeArray.class);
        }

        public Type getElementType() {
            assert (!this.elementType.isUnknown()) : this + " has elementType=unknown";
            return this.elementType;
        }

        public int[] getPostsets() {
            assert (this.postsets != null) : this + " elementType=" + this.elementType + " has no postsets";
            return this.postsets;
        }

        private boolean presetsMatchElementType() {
            if (this.elementType == Type.INT) {
                return this.presets instanceof int[];
            }
            if (this.elementType == Type.NUMBER) {
                return this.presets instanceof double[];
            }
            return this.presets instanceof Object[];
        }

        public Object getPresets() {
            assert (this.presets != null && this.presetsMatchElementType()) : this + " doesn't have presets, or invalid preset type: " + this.presets;
            return this.presets;
        }

        @Override
        public List<Splittable.SplitRange> getSplitRanges() {
            return this.splitRanges == null ? null : Collections.unmodifiableList(this.splitRanges);
        }

        public ArrayLiteralNode setSplitRanges(LexicalContext lc, List<Splittable.SplitRange> splitRanges) {
            if (this.splitRanges == splitRanges) {
                return this;
            }
            return Node.replaceInLexicalContext(lc, this, new ArrayLiteralNode(this, (Expression[])this.value, this.elementType, this.postsets, this.presets, splitRanges));
        }

        @Override
        public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
            return LexicalContextNode.Acceptor.accept(this, visitor);
        }

        @Override
        public Node accept(LexicalContext lc, NodeVisitor<? extends LexicalContext> visitor) {
            if (visitor.enterLiteralNode(this)) {
                List<Object> newValue;
                List<Object> oldValue = Arrays.asList((Object[])this.value);
                return visitor.leaveLiteralNode(oldValue != (newValue = Node.accept(visitor, oldValue)) ? this.setValue(lc, newValue) : this);
            }
            return this;
        }

        private ArrayLiteralNode setValue(LexicalContext lc, Expression[] value) {
            if (this.value == value) {
                return this;
            }
            return Node.replaceInLexicalContext(lc, this, new ArrayLiteralNode(this, value, this.elementType, this.postsets, this.presets, this.splitRanges));
        }

        private ArrayLiteralNode setValue(LexicalContext lc, List<Expression> value) {
            return this.setValue(lc, value.toArray(new Expression[value.size()]));
        }

        @Override
        public void toString(StringBuilder sb, boolean printType) {
            sb.append('[');
            boolean first = true;
            for (Expression node : (Expression[])this.value) {
                if (!first) {
                    sb.append(',');
                    sb.append(' ');
                }
                if (node == null) {
                    sb.append("undefined");
                } else {
                    node.toString(sb, printType);
                }
                first = false;
            }
            sb.append(']');
        }

        private static final class ArrayLiteralInitializer {
            private ArrayLiteralInitializer() {
            }

            static ArrayLiteralNode initialize(ArrayLiteralNode node) {
                Type elementType = ArrayLiteralInitializer.computeElementType((Expression[])node.value);
                int[] postsets = ArrayLiteralInitializer.computePostsets((Expression[])node.value);
                Object presets = ArrayLiteralInitializer.computePresets((Expression[])node.value, elementType, postsets);
                return new ArrayLiteralNode(node, (Expression[])node.value, elementType, postsets, presets, node.splitRanges);
            }

            private static Type computeElementType(Expression[] value) {
                Type widestElementType = Type.INT;
                for (Expression elem : value) {
                    Type type;
                    if (elem == null) {
                        widestElementType = widestElementType.widest(Type.OBJECT);
                        break;
                    }
                    Type type2 = type = elem.getType().isUnknown() ? Type.OBJECT : elem.getType();
                    if (type.isBoolean()) {
                        widestElementType = widestElementType.widest(Type.OBJECT);
                        break;
                    }
                    if ((widestElementType = widestElementType.widest(type)).isObject()) break;
                }
                return widestElementType;
            }

            private static int[] computePostsets(Expression[] value) {
                int[] computed = new int[value.length];
                int nComputed = 0;
                for (int i = 0; i < value.length; ++i) {
                    Expression element = value[i];
                    if (element != null && LiteralNode.isConstant(element)) continue;
                    computed[nComputed++] = i;
                }
                return Arrays.copyOf(computed, nComputed);
            }

            private static boolean setArrayElement(int[] array, int i, Object n) {
                if (n instanceof Number) {
                    array[i] = ((Number)n).intValue();
                    return true;
                }
                return false;
            }

            private static boolean setArrayElement(long[] array, int i, Object n) {
                if (n instanceof Number) {
                    array[i] = ((Number)n).longValue();
                    return true;
                }
                return false;
            }

            private static boolean setArrayElement(double[] array, int i, Object n) {
                if (n instanceof Number) {
                    array[i] = ((Number)n).doubleValue();
                    return true;
                }
                return false;
            }

            private static int[] presetIntArray(Expression[] value, int[] postsets) {
                int[] array = new int[value.length];
                int nComputed = 0;
                for (int i = 0; i < value.length; ++i) {
                    if (!ArrayLiteralInitializer.setArrayElement(array, i, LiteralNode.objectAsConstant(value[i]))) assert (postsets[nComputed++] == i);
                }
                assert (postsets.length == nComputed);
                return array;
            }

            private static long[] presetLongArray(Expression[] value, int[] postsets) {
                long[] array = new long[value.length];
                int nComputed = 0;
                for (int i = 0; i < value.length; ++i) {
                    if (!ArrayLiteralInitializer.setArrayElement(array, i, LiteralNode.objectAsConstant(value[i]))) assert (postsets[nComputed++] == i);
                }
                assert (postsets.length == nComputed);
                return array;
            }

            private static double[] presetDoubleArray(Expression[] value, int[] postsets) {
                double[] array = new double[value.length];
                int nComputed = 0;
                for (int i = 0; i < value.length; ++i) {
                    if (!ArrayLiteralInitializer.setArrayElement(array, i, LiteralNode.objectAsConstant(value[i]))) assert (postsets[nComputed++] == i);
                }
                assert (postsets.length == nComputed);
                return array;
            }

            private static Object[] presetObjectArray(Expression[] value, int[] postsets) {
                Object[] array = new Object[value.length];
                int nComputed = 0;
                for (int i = 0; i < value.length; ++i) {
                    Expression node = value[i];
                    if (node == null) {
                        assert (postsets[nComputed++] == i);
                        continue;
                    }
                    Object element = LiteralNode.objectAsConstant(node);
                    if (element != POSTSET_MARKER) {
                        array[i] = element;
                        continue;
                    }
                    assert (postsets[nComputed++] == i);
                }
                assert (postsets.length == nComputed);
                return array;
            }

            static Object computePresets(Expression[] value, Type elementType, int[] postsets) {
                assert (!elementType.isUnknown());
                if (elementType.isInteger()) {
                    return ArrayLiteralInitializer.presetIntArray(value, postsets);
                }
                if (elementType.isNumeric()) {
                    return ArrayLiteralInitializer.presetDoubleArray(value, postsets);
                }
                return ArrayLiteralInitializer.presetObjectArray(value, postsets);
            }
        }
    }

    private static final class NullLiteralNode
    extends PrimitiveLiteralNode<Object> {
        private static final long serialVersionUID = 1L;

        private NullLiteralNode(long token, int finish) {
            super(Token.recast(token, TokenType.OBJECT), finish, null);
        }

        @Override
        public Node accept(NodeVisitor<? extends LexicalContext> visitor) {
            if (visitor.enterLiteralNode(this)) {
                return visitor.leaveLiteralNode(this);
            }
            return this;
        }

        @Override
        public Type getType() {
            return Type.OBJECT;
        }

        @Override
        public Type getWidestOperationType() {
            return Type.OBJECT;
        }
    }

    @Immutable
    private static class LexerTokenLiteralNode
    extends LiteralNode<Lexer.LexerToken> {
        private static final long serialVersionUID = 1L;

        private LexerTokenLiteralNode(long token, int finish, Lexer.LexerToken value) {
            super(Token.recast(token, TokenType.STRING), finish, value);
        }

        private LexerTokenLiteralNode(LexerTokenLiteralNode literalNode) {
            super(literalNode);
        }

        @Override
        public Type getType() {
            return Type.OBJECT;
        }

        @Override
        public void toString(StringBuilder sb, boolean printType) {
            sb.append(((Lexer.LexerToken)this.value).toString());
        }
    }

    @Immutable
    private static class StringLiteralNode
    extends PrimitiveLiteralNode<String> {
        private static final long serialVersionUID = 1L;

        private StringLiteralNode(long token, int finish, String value) {
            super(Token.recast(token, TokenType.STRING), finish, value);
        }

        private StringLiteralNode(StringLiteralNode literalNode) {
            super(literalNode);
        }

        @Override
        public void toString(StringBuilder sb, boolean printType) {
            sb.append('\"');
            sb.append((String)this.value);
            sb.append('\"');
        }
    }

    private static class UndefinedLiteralNode
    extends PrimitiveLiteralNode<Undefined> {
        private static final long serialVersionUID = 1L;

        private UndefinedLiteralNode(long token, int finish) {
            super(Token.recast(token, TokenType.OBJECT), finish, ScriptRuntime.UNDEFINED);
        }

        private UndefinedLiteralNode(UndefinedLiteralNode literalNode) {
            super(literalNode);
        }
    }

    @Immutable
    private static final class NumberLiteralNode
    extends PrimitiveLiteralNode<Number> {
        private static final long serialVersionUID = 1L;
        private final Type type;

        private NumberLiteralNode(long token, int finish, Number value) {
            super(Token.recast(token, TokenType.DECIMAL), finish, value);
            this.type = NumberLiteralNode.numberGetType((Number)this.value);
        }

        private NumberLiteralNode(NumberLiteralNode literalNode) {
            super(literalNode);
            this.type = NumberLiteralNode.numberGetType((Number)this.value);
        }

        private static Type numberGetType(Number number) {
            if (number instanceof Integer) {
                return Type.INT;
            }
            if (number instanceof Double) {
                return Type.NUMBER;
            }
            assert (false);
            return null;
        }

        @Override
        public Type getType() {
            return this.type;
        }

        @Override
        public Type getWidestOperationType() {
            return this.getType();
        }
    }

    @Immutable
    private static final class BooleanLiteralNode
    extends PrimitiveLiteralNode<Boolean> {
        private static final long serialVersionUID = 1L;

        private BooleanLiteralNode(long token, int finish, boolean value) {
            super(Token.recast(token, value ? TokenType.TRUE : TokenType.FALSE), finish, value);
        }

        private BooleanLiteralNode(BooleanLiteralNode literalNode) {
            super(literalNode);
        }

        @Override
        public boolean isTrue() {
            return (Boolean)this.value;
        }

        @Override
        public Type getType() {
            return Type.BOOLEAN;
        }

        @Override
        public Type getWidestOperationType() {
            return Type.BOOLEAN;
        }
    }

    public static class PrimitiveLiteralNode<T>
    extends LiteralNode<T> {
        private static final long serialVersionUID = 1L;

        private PrimitiveLiteralNode(long token, int finish, T value) {
            super(token, finish, value);
        }

        private PrimitiveLiteralNode(PrimitiveLiteralNode<T> literalNode) {
            super(literalNode);
        }

        public boolean isTrue() {
            return JSType.toBoolean(this.value);
        }

        @Override
        public boolean isLocal() {
            return true;
        }

        @Override
        public boolean isAlwaysFalse() {
            return !this.isTrue();
        }

        @Override
        public boolean isAlwaysTrue() {
            return this.isTrue();
        }
    }
}

