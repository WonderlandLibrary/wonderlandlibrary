/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@ModuleInfo(name="AutoBreak", description="Automatically breaks the block you are looking at.", category=ModuleCategory.WORLD)
public final class AutoBreak
extends Module {
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        block8: {
            block7: {
                if (MinecraftInstance.mc.getObjectMouseOver() == null) break block7;
                IMovingObjectPosition iMovingObjectPosition = MinecraftInstance.mc.getObjectMouseOver();
                if (iMovingObjectPosition == null) {
                    Intrinsics.throwNpe();
                }
                if (iMovingObjectPosition.getBlockPos() != null && MinecraftInstance.mc.getTheWorld() != null) break block8;
            }
            return;
        }
        IKeyBinding iKeyBinding = MinecraftInstance.mc.getGameSettings().getKeyBindAttack();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IMovingObjectPosition iMovingObjectPosition = MinecraftInstance.mc.getObjectMouseOver();
        if (iMovingObjectPosition == null) {
            Intrinsics.throwNpe();
        }
        WBlockPos wBlockPos = iMovingObjectPosition.getBlockPos();
        if (wBlockPos == null) {
            Intrinsics.throwNpe();
        }
        iKeyBinding.setPressed(iWorldClient.getBlockState(wBlockPos).getBlock().equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.AIR)) ^ true);
    }

    @Override
    public void onDisable() {
        if (!MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindAttack())) {
            MinecraftInstance.mc.getGameSettings().getKeyBindAttack().setPressed(false);
        }
    }
}

