/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityTNTPrimed;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="TNTBlock", description="Automatically blocks with your sword when TNT around you explodes.", category=ModuleCategory.COMBAT)
public final class TNTBlock
extends Module {
    private final BoolValue autoSwordValue;
    private final IntegerValue fuseValue = new IntegerValue("Fuse", 10, 0, 80);
    private boolean blocked;
    private final FloatValue rangeValue = new FloatValue("Range", 9.0f, 1.0f, 20.0f);

    @EventTarget
    public final void onMotionUpdate(@Nullable MotionEvent motionEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            return;
        }
        IWorldClient iWorldClient2 = iWorldClient;
        for (IEntity iEntity : iWorldClient2.getLoadedEntityList()) {
            IEntityTNTPrimed iEntityTNTPrimed;
            if (!MinecraftInstance.classProvider.isEntityTNTPrimed(iEntity) || !(iEntityPlayerSP2.getDistanceToEntity(iEntity) <= ((Number)this.rangeValue.get()).floatValue()) || (iEntityTNTPrimed = iEntity.asEntityTNTPrimed()).getFuse() > ((Number)this.fuseValue.get()).intValue()) continue;
            if (((Boolean)this.autoSwordValue.get()).booleanValue()) {
                int n = -1;
                float f = 1.0f;
                int n2 = 8;
                for (int i = 0; i <= n2; ++i) {
                    float f2;
                    IItemStack iItemStack = iEntityPlayerSP2.getInventory().getStackInSlot(i);
                    if (iItemStack == null || !MinecraftInstance.classProvider.isItemSword(iItemStack.getItem())) continue;
                    IItem iItem = iItemStack.getItem();
                    if (iItem == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!((f2 = iItem.asItemSword().getDamageVsEntity() + 4.0f) > f)) continue;
                    f = f2;
                    n = i;
                }
                if (n != -1 && n != iEntityPlayerSP2.getInventory().getCurrentItem()) {
                    iEntityPlayerSP2.getInventory().setCurrentItem(n);
                    MinecraftInstance.mc.getPlayerController().updateController();
                }
            }
            if (iEntityPlayerSP2.getHeldItem() != null) {
                IItemStack iItemStack = iEntityPlayerSP2.getHeldItem();
                if (iItemStack == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemSword(iItemStack.getItem())) {
                    MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().setPressed(true);
                    this.blocked = true;
                }
            }
            return;
        }
        if (this.blocked && !MinecraftInstance.mc.getGameSettings().isKeyDown(MinecraftInstance.mc.getGameSettings().getKeyBindUseItem())) {
            MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().setPressed(false);
            this.blocked = false;
        }
    }

    public TNTBlock() {
        this.autoSwordValue = new BoolValue("AutoSword", true);
    }
}

