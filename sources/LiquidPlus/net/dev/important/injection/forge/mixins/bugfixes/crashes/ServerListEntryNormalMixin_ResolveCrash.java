/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ServerListEntryNormal
 *  net.minecraft.client.multiplayer.ServerData
 */
package net.dev.important.injection.forge.mixins.bugfixes.crashes;

import net.dev.important.utils.ClientUtils;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={ServerListEntryNormal.class})
public abstract class ServerListEntryNormalMixin_ResolveCrash {
    @Shadow
    @Final
    private ServerData field_148301_e;

    @Shadow
    protected abstract void func_148297_b();

    @Redirect(method={"drawEntry"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/ServerListEntryNormal;prepareServerIcon()V"))
    private void patcher$resolveCrash(ServerListEntryNormal serverListEntryNormal) {
        try {
            this.func_148297_b();
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("Failed to prepare server icon, setting to default.", (Throwable)e);
            this.field_148301_e.func_147407_a(null);
        }
    }
}

