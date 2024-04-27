package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReference;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import net.ccbluex.liquidbounce.api.IClassProvider;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\n\u0000\n\u0000\n\n\b\u0000020¢\f\b\b\b\b\b(¢\b"}, d2={"<anonymous>", "", "p1", "", "Lkotlin/ParameterName;", "name", "obj", "invoke"})
final class LiquidWalk$onUpdate$1
extends FunctionReference
implements Function1<Object, Boolean> {
    @Override
    public final boolean invoke(@Nullable Object p1) {
        return ((IClassProvider)this.receiver).isBlockLiquid(p1);
    }

    @Override
    public final KDeclarationContainer getOwner() {
        return Reflection.getOrCreateKotlinClass(IClassProvider.class);
    }

    @Override
    public final String getName() {
        return "isBlockLiquid";
    }

    @Override
    public final String getSignature() {
        return "isBlockLiquid(Ljava/lang/Object;)Z";
    }

    LiquidWalk$onUpdate$1(IClassProvider iClassProvider) {
        super(1, iClassProvider);
    }
}
