/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Ghost;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="AutoRespawn", description="Automatically respawns you after dying.", category=ModuleCategory.PLAYER)
public final class AutoRespawn
extends Module {
    private final BoolValue instantValue = new BoolValue("Instant", true);

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        boolean bl;
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null) return;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Ghost.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (module.getState()) {
            return;
        }
        if (((Boolean)this.instantValue.get()).booleanValue()) {
            if (thePlayer.getHealth() != 0.0f) {
                if (!thePlayer.isDead()) return;
            }
            bl = true;
        } else {
            if (!MinecraftInstance.classProvider.isGuiGameOver(MinecraftInstance.mc.getCurrentScreen())) return;
            IGuiScreen iGuiScreen = MinecraftInstance.mc.getCurrentScreen();
            if (iGuiScreen == null) {
                Intrinsics.throwNpe();
            }
            if (iGuiScreen.asGuiGameOver().getEnableButtonsTimer() < 20) return;
            bl = true;
        }
        if (!bl) return;
        thePlayer.respawnPlayer();
        MinecraftInstance.mc.displayGuiScreen(null);
    }
}

