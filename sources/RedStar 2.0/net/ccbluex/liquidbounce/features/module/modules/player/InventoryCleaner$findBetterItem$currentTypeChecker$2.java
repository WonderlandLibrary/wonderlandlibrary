package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\u000002\b0H\n¢\b"}, d2={"<anonymous>", "", "obj", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "invoke"})
final class InventoryCleaner$findBetterItem$currentTypeChecker$2
extends Lambda
implements Function1<IItem, Boolean> {
    public static final InventoryCleaner$findBetterItem$currentTypeChecker$2 INSTANCE = new /* invalid duplicate definition of identical inner class */;

    @Override
    public final boolean invoke(@Nullable IItem obj) {
        return MinecraftInstance.classProvider.isItemPickaxe(obj);
    }

    InventoryCleaner$findBetterItem$currentTypeChecker$2() {
    }
}
