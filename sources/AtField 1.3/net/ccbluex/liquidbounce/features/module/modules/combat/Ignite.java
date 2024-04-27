/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="Ignite", description="Automatically sets targets around you on fire.", category=ModuleCategory.COMBAT)
public class Ignite
extends Module {
    private final BoolValue lighterValue = new BoolValue("Lighter", true);
    private final MSTimer msTimer;
    private final BoolValue lavaBucketValue = new BoolValue("Lava", true);

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        int n;
        if (!this.msTimer.hasTimePassed(500L)) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = mc.getThePlayer();
        IWorldClient iWorldClient = mc.getTheWorld();
        if (iEntityPlayerSP == null || iWorldClient == null) {
            return;
        }
        int n2 = (Boolean)this.lighterValue.get() != false ? InventoryUtils.findItem(36, 45, classProvider.getItemEnum(ItemType.FLINT_AND_STEEL)) : -1;
        int n3 = n = (Boolean)this.lavaBucketValue.get() != false ? InventoryUtils.findItem(26, 45, classProvider.getItemEnum(ItemType.LAVA_BUCKET)) : -1;
        if (n2 == -1 && n == -1) {
            return;
        }
        int n4 = n2 != -1 ? n2 : n;
        for (IEntity iEntity : iWorldClient.getLoadedEntityList()) {
            if (!EntityUtils.isSelected(iEntity, true) || iEntity.isBurning()) continue;
            WBlockPos wBlockPos = iEntity.getPosition();
            if (mc.getThePlayer().getDistanceSq(wBlockPos) >= 22.3 || !BlockUtils.isReplaceable(wBlockPos) || !classProvider.isBlockAir(BlockUtils.getBlock(wBlockPos))) continue;
            RotationUtils.keepCurrentRotation = true;
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(n4 - 36));
            IItemStack iItemStack = mc.getThePlayer().getInventory().getStackInSlot(n4);
            if (classProvider.isItemBucket(iItemStack.getItem())) {
                double d = (double)wBlockPos.getX() + 0.5 - mc.getThePlayer().getPosX();
                double d2 = (double)wBlockPos.getY() + 0.5 - (iEntityPlayerSP.getEntityBoundingBox().getMinY() + (double)iEntityPlayerSP.getEyeHeight());
                double d3 = (double)wBlockPos.getZ() + 0.5 - iEntityPlayerSP.getPosZ();
                double d4 = Math.sqrt(d * d + d3 * d3);
                float f = (float)(Math.atan2(d3, d) * 180.0 / Math.PI) - 90.0f;
                float f2 = (float)(-(Math.atan2(d2, d4) * 180.0 / Math.PI));
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerLook(iEntityPlayerSP.getRotationYaw() + WMathHelper.wrapAngleTo180_float(f - iEntityPlayerSP.getRotationYaw()), iEntityPlayerSP.getRotationPitch() + WMathHelper.wrapAngleTo180_float(f2 - iEntityPlayerSP.getRotationPitch()), iEntityPlayerSP.getOnGround()));
                mc.getPlayerController().sendUseItem(iEntityPlayerSP, iWorldClient, iItemStack);
            } else {
                for (EnumFacingType enumFacingType : EnumFacingType.values()) {
                    IEnumFacing iEnumFacing = classProvider.getEnumFacing(enumFacingType);
                    WBlockPos wBlockPos2 = wBlockPos.offset(iEnumFacing);
                    if (!BlockUtils.canBeClicked(wBlockPos2)) continue;
                    double d = (double)wBlockPos2.getX() + 0.5 - iEntityPlayerSP.getPosX();
                    double d5 = (double)wBlockPos2.getY() + 0.5 - (iEntityPlayerSP.getEntityBoundingBox().getMinY() + (double)iEntityPlayerSP.getEyeHeight());
                    double d6 = (double)wBlockPos2.getZ() + 0.5 - iEntityPlayerSP.getPosZ();
                    double d7 = Math.sqrt(d * d + d6 * d6);
                    float f = (float)(Math.atan2(d6, d) * 180.0 / Math.PI) - 90.0f;
                    float f3 = (float)(-(Math.atan2(d5, d7) * 180.0 / Math.PI));
                    mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerLook(iEntityPlayerSP.getRotationYaw() + WMathHelper.wrapAngleTo180_float(f - iEntityPlayerSP.getRotationYaw()), iEntityPlayerSP.getRotationPitch() + WMathHelper.wrapAngleTo180_float(f3 - iEntityPlayerSP.getRotationPitch()), iEntityPlayerSP.getOnGround()));
                    if (!mc.getPlayerController().onPlayerRightClick(iEntityPlayerSP, iWorldClient, iItemStack, wBlockPos2, iEnumFacing.getOpposite(), new WVec3(iEnumFacing.getDirectionVec()))) continue;
                    iEntityPlayerSP.swingItem();
                    break;
                }
            }
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(iEntityPlayerSP.getInventory().getCurrentItem()));
            RotationUtils.keepCurrentRotation = false;
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerLook(iEntityPlayerSP.getRotationYaw(), iEntityPlayerSP.getRotationPitch(), iEntityPlayerSP.getOnGround()));
            this.msTimer.reset();
            break;
        }
    }

    public Ignite() {
        this.msTimer = new MSTimer();
    }
}

