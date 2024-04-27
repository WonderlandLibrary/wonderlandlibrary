/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.Type
 */
package codes.som.anthony.koffee.sugar;

import codes.som.anthony.koffee.types.TypesKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0014\u0010\u0017\u001a\u00020\u00032\n\u0010\u0018\u001a\u00060\u0001j\u0002`\u0019H\u0016R\u001c\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u001c\u0010\u0007\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\u0006R\u001c\u0010\t\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u0006R\u001c\u0010\u000b\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\u0006R\u001c\u0010\r\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u0006R\u001c\u0010\u000f\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0006R\u001c\u0010\u0011\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0006R\u001c\u0010\u0013\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0006R\u001c\u0010\u0015\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0006\u00a8\u0006\u001a"}, d2={"Lcodes/som/anthony/koffee/sugar/TypesAccess;", "", "boolean", "Lorg/objectweb/asm/Type;", "kotlin.jvm.PlatformType", "getBoolean", "()Lorg/objectweb/asm/Type;", "byte", "getByte", "char", "getChar", "double", "getDouble", "float", "getFloat", "int", "getInt", "long", "getLong", "short", "getShort", "void", "getVoid", "coerceType", "value", "Lcodes/som/anthony/koffee/types/TypeLike;", "koffee"})
public interface TypesAccess {
    public Type getVoid();

    public Type getChar();

    public Type getByte();

    public Type getShort();

    public Type getInt();

    public Type getFloat();

    public Type getLong();

    public Type getDouble();

    public Type getBoolean();

    @NotNull
    public Type coerceType(@NotNull Object var1);

    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=3)
    public static final class DefaultImpls {
        public static Type getVoid(TypesAccess $this) {
            return TypesKt.getVoid();
        }

        public static Type getChar(TypesAccess $this) {
            return TypesKt.getChar();
        }

        public static Type getByte(TypesAccess $this) {
            return TypesKt.getByte();
        }

        public static Type getShort(TypesAccess $this) {
            return TypesKt.getShort();
        }

        public static Type getInt(TypesAccess $this) {
            return TypesKt.getInt();
        }

        public static Type getFloat(TypesAccess $this) {
            return TypesKt.getFloat();
        }

        public static Type getLong(TypesAccess $this) {
            return TypesKt.getLong();
        }

        public static Type getDouble(TypesAccess $this) {
            return TypesKt.getDouble();
        }

        public static Type getBoolean(TypesAccess $this) {
            return TypesKt.getBoolean();
        }

        @NotNull
        public static Type coerceType(TypesAccess $this, @NotNull Object value) {
            Intrinsics.checkParameterIsNotNull(value, "value");
            return TypesKt.coerceType(value);
        }
    }
}

