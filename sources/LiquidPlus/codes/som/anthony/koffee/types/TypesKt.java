/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.tree.ClassNode
 */
package codes.som.anthony.koffee.types;

import kotlin.Metadata;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0012\u0010\u0015\u001a\u00020\u00012\n\u0010\u0016\u001a\u00060\u0017j\u0002`\u0018\"\u0019\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0019\u0010\u0005\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0019\u0010\u0007\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00018F\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\u0004\"\u0019\u0010\t\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00018F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u0004\"\u0019\u0010\u000b\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00018F\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\u0004\"\u0019\u0010\r\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u0004\"\u0019\u0010\u000f\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0004\"\u0019\u0010\u0011\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0004\"\u0019\u0010\u0013\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0004*\n\u0010\u0019\"\u00020\u00172\u00020\u0017\u00a8\u0006\u001a"}, d2={"boolean", "Lorg/objectweb/asm/Type;", "kotlin.jvm.PlatformType", "getBoolean", "()Lorg/objectweb/asm/Type;", "byte", "getByte", "char", "getChar", "double", "getDouble", "float", "getFloat", "int", "getInt", "long", "getLong", "short", "getShort", "void", "getVoid", "coerceType", "value", "", "Lcodes/som/anthony/koffee/types/TypeLike;", "TypeLike", "koffee"})
public final class TypesKt {
    public static final Type getVoid() {
        return Type.VOID_TYPE;
    }

    public static final Type getChar() {
        return Type.CHAR_TYPE;
    }

    public static final Type getByte() {
        return Type.BYTE_TYPE;
    }

    public static final Type getShort() {
        return Type.SHORT_TYPE;
    }

    public static final Type getInt() {
        return Type.INT_TYPE;
    }

    public static final Type getFloat() {
        return Type.FLOAT_TYPE;
    }

    public static final Type getLong() {
        return Type.LONG_TYPE;
    }

    public static final Type getDouble() {
        return Type.DOUBLE_TYPE;
    }

    public static final Type getBoolean() {
        return Type.BOOLEAN_TYPE;
    }

    @NotNull
    public static final Type coerceType(@NotNull Object value) {
        Type type;
        Intrinsics.checkParameterIsNotNull(value, "value");
        Object object = value;
        if (object instanceof Type) {
            type = (Type)value;
        } else if (object instanceof KClass) {
            Type type2 = Type.getType(JvmClassMappingKt.getJavaClass((KClass)value));
            type = type2;
            Intrinsics.checkExpressionValueIsNotNull(type2, "Type.getType(value.java)");
        } else if (object instanceof Class) {
            Type type3 = Type.getType((Class)((Class)value));
            type = type3;
            Intrinsics.checkExpressionValueIsNotNull(type3, "Type.getType(value)");
        } else if (object instanceof String) {
            Type type4 = Type.getObjectType((String)((String)value));
            type = type4;
            Intrinsics.checkExpressionValueIsNotNull(type4, "Type.getObjectType(value)");
        } else if (object instanceof ClassNode) {
            Type type5 = Type.getObjectType((String)((ClassNode)value).name);
            type = type5;
            Intrinsics.checkExpressionValueIsNotNull(type5, "Type.getObjectType(value.name)");
        } else {
            String string = "Non type-like object passed to coerceType()";
            boolean bl = false;
            throw (Throwable)new IllegalStateException(string.toString());
        }
        return type;
    }
}

