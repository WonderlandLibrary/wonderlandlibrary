/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.Type
 */
package codes.som.anthony.koffee.insns.sugar;

import codes.som.anthony.koffee.insns.InstructionAssembly;
import codes.som.anthony.koffee.insns.jvm.MethodsKt;
import codes.som.anthony.koffee.insns.jvm.ObjectManagementKt;
import codes.som.anthony.koffee.insns.jvm.StackManipulationKt;
import codes.som.anthony.koffee.insns.sugar.ObjectConstructionKt;
import codes.som.anthony.koffee.types.TypesKt;
import java.util.Arrays;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u00000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001af\u0010\u0000\u001a\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003*\u0002H\u00022\n\u0010\u0004\u001a\u00060\u0005j\u0002`\u00062\u001a\u0010\u0007\u001a\u000e\u0012\n\b\u0001\u0012\u00060\u0005j\u0002`\u00060\b\"\u00060\u0005j\u0002`\u00062\b\b\u0002\u0010\t\u001a\u00020\n2\u0019\b\u0002\u0010\u000b\u001a\u0013\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\f\u00a2\u0006\u0002\b\r\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u000f"}, d2={"construct", "", "S", "Lcodes/som/anthony/koffee/insns/InstructionAssembly;", "type", "", "Lcodes/som/anthony/koffee/types/TypeLike;", "constructorTypes", "", "initializerName", "", "initializerBlock", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Lcodes/som/anthony/koffee/insns/InstructionAssembly;Ljava/lang/Object;[Ljava/lang/Object;Ljava/lang/String;Lkotlin/jvm/functions/Function1;)V", "koffee"})
public final class ObjectConstructionKt {
    public static final <S extends InstructionAssembly> void construct(@NotNull S $this$construct, @NotNull Object type, @NotNull Object[] constructorTypes, @NotNull String initializerName, @NotNull Function1<? super S, Unit> initializerBlock) {
        Object object;
        Intrinsics.checkParameterIsNotNull($this$construct, "$this$construct");
        Intrinsics.checkParameterIsNotNull(type, "type");
        Intrinsics.checkParameterIsNotNull(constructorTypes, "constructorTypes");
        Intrinsics.checkParameterIsNotNull(initializerName, "initializerName");
        Intrinsics.checkParameterIsNotNull(initializerBlock, "initializerBlock");
        Object[] objectArray = constructorTypes;
        int n = 0;
        boolean bl = false;
        if (n <= ArraysKt.getLastIndex(objectArray)) {
            object = objectArray[n];
        } else {
            int it = n;
            boolean bl2 = false;
            Type type2 = TypesKt.getVoid();
            object = type2;
            Intrinsics.checkExpressionValueIsNotNull(type2, "void");
        }
        Object returnType = object;
        Collection $this$toTypedArray$iv = ArraysKt.drop(constructorTypes, 1);
        boolean $i$f$toTypedArray = false;
        Collection collection = $this$toTypedArray$iv;
        if (collection == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
        }
        Collection thisCollection$iv = collection;
        Object[] objectArray2 = thisCollection$iv.toArray(new Object[0]);
        if (objectArray2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        Object[] parameterTypes = objectArray2;
        ObjectManagementKt.new($this$construct, type);
        StackManipulationKt.getDup($this$construct);
        initializerBlock.invoke($this$construct);
        Object object2 = returnType;
        Intrinsics.checkExpressionValueIsNotNull(object2, "returnType");
        MethodsKt.invokespecial($this$construct, type, initializerName, object2, Arrays.copyOf(parameterTypes, parameterTypes.length));
    }

    public static /* synthetic */ void construct$default(InstructionAssembly instructionAssembly, Object object, Object[] objectArray, String string, Function1 function1, int n, Object object2) {
        if ((n & 4) != 0) {
            string = "<init>";
        }
        if ((n & 8) != 0) {
            function1 = construct.1.INSTANCE;
        }
        ObjectConstructionKt.construct(instructionAssembly, object, objectArray, string, function1);
    }
}

