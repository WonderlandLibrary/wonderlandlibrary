/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.ListenableFuture
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.GameSettings
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import com.google.common.util.concurrent.ListenableFuture;
import net.dev.important.patcher.ducks.GameSettingsExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={GameSettings.class})
public class GameSettingsMixin_MipmapSlider
implements GameSettingsExt {
    @Shadow
    protected Minecraft field_74317_L;
    private boolean patcher$needsResourceRefresh;

    @Override
    public void patcher$onSettingsGuiClosed() {
        if (this.patcher$needsResourceRefresh) {
            this.field_74317_L.func_175603_A();
            this.patcher$needsResourceRefresh = false;
        }
    }

    @Redirect(method={"setOptionFloatValue"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;scheduleResourcesRefresh()Lcom/google/common/util/concurrent/ListenableFuture;"))
    private ListenableFuture<Object> patcher$scheduleResourceRefresh(Minecraft instance) {
        this.patcher$needsResourceRefresh = true;
        return null;
    }
}

