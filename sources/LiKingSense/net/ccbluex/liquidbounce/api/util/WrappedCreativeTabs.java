/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0003H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\u0007\u001a\u00020\bX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f\u00a8\u0006\u0017"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedCreativeTabs;", "", "name", "", "(Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "representedType", "Lnet/ccbluex/liquidbounce/api/minecraft/creativetabs/ICreativeTabs;", "getRepresentedType", "()Lnet/ccbluex/liquidbounce/api/minecraft/creativetabs/ICreativeTabs;", "setRepresentedType", "(Lnet/ccbluex/liquidbounce/api/minecraft/creativetabs/ICreativeTabs;)V", "displayAllReleventItems", "", "items", "", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "getTabIconItem", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "getTranslatedTabLabel", "hasSearchBar", "", "LiKingSense"})
public abstract class WrappedCreativeTabs {
    @NotNull
    public ICreativeTabs representedType;
    @NotNull
    private final String name;

    @NotNull
    public final ICreativeTabs getRepresentedType() {
        ICreativeTabs iCreativeTabs = this.representedType;
        if (iCreativeTabs == null) {
            Intrinsics.throwUninitializedPropertyAccessException((String)"representedType");
        }
        return iCreativeTabs;
    }

    public final void setRepresentedType(@NotNull ICreativeTabs iCreativeTabs) {
        Intrinsics.checkParameterIsNotNull((Object)iCreativeTabs, (String)"<set-?>");
        this.representedType = iCreativeTabs;
    }

    public void displayAllReleventItems(@NotNull List<IItemStack> items) {
        Intrinsics.checkParameterIsNotNull(items, (String)"items");
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
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        this.name = name;
        LiquidBounce.INSTANCE.getWrapper().getClassProvider().wrapCreativeTab(this.name, this);
    }
}

