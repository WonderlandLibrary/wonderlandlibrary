/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemBucket
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

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
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

@ModuleInfo(name="Ignite", description="Automatically sets targets around you on fire.", category=ModuleCategory.COMBAT)
public class Ignite
extends Module {
    private final BoolValue lighterValue = new BoolValue("Lighter", true);
    private final BoolValue lavaBucketValue = new BoolValue("Lava", true);
    private final MSTimer msTimer = new MSTimer();

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        int lavaInHotbar;
        if (!this.msTimer.hasTimePassed(500L)) {
            return;
        }
        int lighterInHotbar = (Boolean)this.lighterValue.get() != false ? InventoryUtils.findItem(36, 45, Items.field_151033_d) : -1;
        int n = lavaInHotbar = (Boolean)this.lavaBucketValue.get() != false ? InventoryUtils.findItem(26, 45, Items.field_151129_at) : -1;
        if (lighterInHotbar == -1 && lavaInHotbar == -1) {
            return;
        }
        int fireInHotbar = lighterInHotbar != -1 ? lighterInHotbar : lavaInHotbar;
        for (Entity entity : Ignite.mc.field_71441_e.field_72996_f) {
            BlockPos blockPos;
            if (!EntityUtils.isSelected(entity, true) || entity.func_70027_ad() || Ignite.mc.field_71439_g.func_174818_b(blockPos = entity.func_180425_c()) >= 22.3 || !BlockUtils.isReplaceable(blockPos) || !(BlockUtils.getBlock(blockPos) instanceof BlockAir)) continue;
            RotationUtils.keepCurrentRotation = true;
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(fireInHotbar - 36));
            ItemStack itemStack = Ignite.mc.field_71439_g.field_71069_bz.func_75139_a(fireInHotbar).func_75211_c();
            if (itemStack.func_77973_b() instanceof ItemBucket) {
                double diffX = (double)blockPos.func_177958_n() + 0.5 - Ignite.mc.field_71439_g.field_70165_t;
                double diffY = (double)blockPos.func_177956_o() + 0.5 - (Ignite.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)Ignite.mc.field_71439_g.func_70047_e());
                double diffZ = (double)blockPos.func_177952_p() + 0.5 - Ignite.mc.field_71439_g.field_70161_v;
                double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
                float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
                float pitch = (float)(-(Math.atan2(diffY, sqrt) * 180.0 / Math.PI));
                mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(Ignite.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g((float)(yaw - Ignite.mc.field_71439_g.field_70177_z)), Ignite.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g((float)(pitch - Ignite.mc.field_71439_g.field_70125_A)), Ignite.mc.field_71439_g.field_70122_E));
                Ignite.mc.field_71442_b.func_78769_a((EntityPlayer)Ignite.mc.field_71439_g, (World)Ignite.mc.field_71441_e, itemStack);
            } else {
                for (EnumFacing side : EnumFacing.values()) {
                    BlockPos neighbor = blockPos.func_177972_a(side);
                    if (!BlockUtils.canBeClicked(neighbor)) continue;
                    double diffX = (double)neighbor.func_177958_n() + 0.5 - Ignite.mc.field_71439_g.field_70165_t;
                    double diffY = (double)neighbor.func_177956_o() + 0.5 - (Ignite.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)Ignite.mc.field_71439_g.func_70047_e());
                    double diffZ = (double)neighbor.func_177952_p() + 0.5 - Ignite.mc.field_71439_g.field_70161_v;
                    double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
                    float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
                    float pitch = (float)(-(Math.atan2(diffY, sqrt) * 180.0 / Math.PI));
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(Ignite.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g((float)(yaw - Ignite.mc.field_71439_g.field_70177_z)), Ignite.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g((float)(pitch - Ignite.mc.field_71439_g.field_70125_A)), Ignite.mc.field_71439_g.field_70122_E));
                    if (!Ignite.mc.field_71442_b.func_178890_a(Ignite.mc.field_71439_g, Ignite.mc.field_71441_e, itemStack, neighbor, side.func_176734_d(), new Vec3(side.func_176730_m()))) continue;
                    Ignite.mc.field_71439_g.func_71038_i();
                    break;
                }
            }
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(Ignite.mc.field_71439_g.field_71071_by.field_70461_c));
            RotationUtils.keepCurrentRotation = false;
            mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(Ignite.mc.field_71439_g.field_70177_z, Ignite.mc.field_71439_g.field_70125_A, Ignite.mc.field_71439_g.field_70122_E));
            this.msTimer.reset();
            break;
        }
    }
}

