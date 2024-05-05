/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@ModuleInfo(name="Eagle", description="Makes you eagle (aka. FastBridge).", category=ModuleCategory.PLAYER)
public final class Eagle
extends Module {
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IKeyBinding iKeyBinding = MinecraftInstance.mc.getGameSettings().getKeyBindSneak();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        iKeyBinding.setPressed(iWorldClient.getBlockState(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 1.0, thePlayer.getPosZ())).getBlock().equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR)));
    }

    @Override
    public void onDisable() {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindSneak())) {
            MinecraftInstance.mc.getGameSettings().getKeyBindSneak().setPressed(false);
        }
    }
}

