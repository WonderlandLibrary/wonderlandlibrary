/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiListExtended$IGuiListEntry
 *  net.minecraft.client.gui.ServerListEntryNormal
 *  net.minecraft.client.gui.ServerSelectionList
 */
package net.dev.important.injection.forge.mixins.bugfixes.crashes;

import java.util.List;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.gui.ServerSelectionList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ServerSelectionList.class})
public class ServerSelectionListMixin_ResolveCrash {
    @Shadow
    @Final
    private List<ServerListEntryNormal> field_148198_l;
    @Shadow
    @Final
    private GuiListExtended.IGuiListEntry field_148196_n;

    @Inject(method={"getListEntry"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$resolveIndexError(int index, CallbackInfoReturnable<GuiListExtended.IGuiListEntry> cir) {
        if (index > this.field_148198_l.size()) {
            cir.setReturnValue(this.field_148196_n);
        }
    }
}

