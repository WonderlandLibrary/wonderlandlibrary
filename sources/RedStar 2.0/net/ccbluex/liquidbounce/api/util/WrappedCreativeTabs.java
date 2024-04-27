package net.ccbluex.liquidbounce.api.util;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.minecraft.creativetabs.ICreativeTabs;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\u0000\n!\n\n\u0000\n\n\b\n\n\u0000\b&\u000020B\r0¢J\r02\f\b00HJ\b0HJ\b0HJ\b0HR0¢\b\n\u0000\bR0\bX.¢\n\u0000\b\t\n\"\b\f¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedCreativeTabs;", "", "name", "", "(Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "representedType", "Lnet/ccbluex/liquidbounce/api/minecraft/creativetabs/ICreativeTabs;", "getRepresentedType", "()Lnet/ccbluex/liquidbounce/api/minecraft/creativetabs/ICreativeTabs;", "setRepresentedType", "(Lnet/ccbluex/liquidbounce/api/minecraft/creativetabs/ICreativeTabs;)V", "displayAllReleventItems", "", "items", "", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "getTabIconItem", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "getTranslatedTabLabel", "hasSearchBar", "", "Pride"})
public abstract class WrappedCreativeTabs {
    @NotNull
    public ICreativeTabs representedType;
    @NotNull
    private final String name;

    @NotNull
    public final ICreativeTabs getRepresentedType() {
        ICreativeTabs iCreativeTabs = this.representedType;
        if (iCreativeTabs == null) {
            Intrinsics.throwUninitializedPropertyAccessException("representedType");
        }
        return iCreativeTabs;
    }

    public final void setRepresentedType(@NotNull ICreativeTabs iCreativeTabs) {
        Intrinsics.checkParameterIsNotNull(iCreativeTabs, "<set-?>");
        this.representedType = iCreativeTabs;
    }

    public void displayAllReleventItems(@NotNull List<IItemStack> items) {
        Intrinsics.checkParameterIsNotNull(items, "items");
    }

    @NotNull
    public String getTranslatedTabLabel() {
        return "asdf";
    }

    @NotNull
    public IItem getTabIconItem() {
        return WrapperImpl.INSTANCE.getClassProvider().getItemEnum(ItemType.WRITABLE_BOOK);
    }

    public boolean hasSearchBar() {
        return true;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public WrappedCreativeTabs(@NotNull String name) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        this.name = name;
        LiquidBounce.INSTANCE.getWrapper().getClassProvider().wrapCreativeTab(this.name, this);
    }
}
