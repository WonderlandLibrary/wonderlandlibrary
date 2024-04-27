/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.jvm.internal;

import java.util.List;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KVariance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB'\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0013\u0010\u0018\u001a\u00020\t2\b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u0096\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0014\u0010\u001c\u001a\u00020\u001d2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\b\u0010\u001e\u001a\u00020\u0005H\u0016R\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\tX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000eR\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R \u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\f8VX\u0096\u0004\u00a2\u0006\f\u0012\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006 "}, d2={"Lkotlin/jvm/internal/TypeParameterReference;", "Lkotlin/reflect/KTypeParameter;", "container", "", "name", "", "variance", "Lkotlin/reflect/KVariance;", "isReified", "", "(Ljava/lang/Object;Ljava/lang/String;Lkotlin/reflect/KVariance;Z)V", "bounds", "", "Lkotlin/reflect/KType;", "()Z", "getName", "()Ljava/lang/String;", "upperBounds", "getUpperBounds$annotations", "()V", "getUpperBounds", "()Ljava/util/List;", "getVariance", "()Lkotlin/reflect/KVariance;", "equals", "other", "hashCode", "", "setUpperBounds", "", "toString", "Companion", "kotlin-stdlib"})
@SinceKotlin(version="1.4")
public final class TypeParameterReference
implements KTypeParameter {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private final Object container;
    @NotNull
    private final String name;
    @NotNull
    private final KVariance variance;
    private final boolean isReified;
    @Nullable
    private volatile List<? extends KType> bounds;

    public TypeParameterReference(@Nullable Object container, @NotNull String name, @NotNull KVariance variance, boolean isReified) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter((Object)variance, "variance");
        this.container = container;
        this.name = name;
        this.variance = variance;
        this.isReified = isReified;
    }

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

    @Override
    @NotNull
    public KVariance getVariance() {
        return this.variance;
    }

    @Override
    public boolean isReified() {
        return this.isReified;
    }

    @Override
    @NotNull
    public List<KType> getUpperBounds() {
        List<KType> list;
        List<KType> list2 = this.bounds;
        if (list2 == null) {
            List<KType> list3;
            List<KType> it = list3 = CollectionsKt.listOf(Reflection.nullableTypeOf(Object.class));
            boolean bl = false;
            this.bounds = it;
            list = list3;
        } else {
            list = list2;
        }
        return list;
    }

    public static /* synthetic */ void getUpperBounds$annotations() {
    }

    public final void setUpperBounds(@NotNull List<? extends KType> upperBounds) {
        Intrinsics.checkNotNullParameter(upperBounds, "upperBounds");
        if (this.bounds != null) {
            throw new IllegalStateException(("Upper bounds of type parameter '" + this + "' have already been initialized.").toString());
        }
        this.bounds = upperBounds;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof TypeParameterReference && Intrinsics.areEqual(this.container, ((TypeParameterReference)other).container) && Intrinsics.areEqual(this.getName(), ((TypeParameterReference)other).getName());
    }

    public int hashCode() {
        Object object = this.container;
        return (object == null ? 0 : object.hashCode()) * 31 + this.getName().hashCode();
    }

    @NotNull
    public String toString() {
        return Companion.toString(this);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2={"Lkotlin/jvm/internal/TypeParameterReference$Companion;", "", "()V", "toString", "", "typeParameter", "Lkotlin/reflect/KTypeParameter;", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final String toString(@NotNull KTypeParameter typeParameter) {
            StringBuilder stringBuilder;
            Intrinsics.checkNotNullParameter(typeParameter, "typeParameter");
            StringBuilder $this$toString_u24lambda_u2d0 = stringBuilder = new StringBuilder();
            boolean bl = false;
            KVariance kVariance = typeParameter.getVariance();
            int n = WhenMappings.$EnumSwitchMapping$0[kVariance.ordinal()];
            switch (n) {
                case 1: {
                    break;
                }
                case 2: {
                    $this$toString_u24lambda_u2d0.append("in ");
                    break;
                }
                case 3: {
                    $this$toString_u24lambda_u2d0.append("out ");
                }
            }
            $this$toString_u24lambda_u2d0.append(typeParameter.getName());
            String string = stringBuilder.toString();
            Intrinsics.checkNotNullExpressionValue(string, "StringBuilder().apply(builderAction).toString()");
            return string;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        @Metadata(mv={1, 6, 0}, k=3, xi=48)
        public final class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] nArray = new int[KVariance.values().length];
                nArray[KVariance.INVARIANT.ordinal()] = 1;
                nArray[KVariance.IN.ordinal()] = 2;
                nArray[KVariance.OUT.ordinal()] = 3;
                $EnumSwitchMapping$0 = nArray;
            }
        }
    }
}

