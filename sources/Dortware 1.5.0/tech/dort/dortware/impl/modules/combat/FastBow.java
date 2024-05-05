package tech.dort.dortware.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import skidmonke.Client;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.ModuleData;
import tech.dort.dortware.api.property.impl.BooleanValue;
import tech.dort.dortware.api.property.impl.NumberValue;
import tech.dort.dortware.impl.events.UpdateEvent;
import tech.dort.dortware.impl.modules.exploit.InfiniteDurability;
import tech.dort.dortware.impl.modules.render.Rotate;
import tech.dort.dortware.impl.utils.combat.AimUtil;
import tech.dort.dortware.impl.utils.combat.FightUtil;
import tech.dort.dortware.impl.utils.combat.extras.Rotation;
import tech.dort.dortware.impl.utils.inventory.InventoryUtils;
import tech.dort.dortware.impl.utils.networking.PacketUtil;

import java.util.List;

public class FastBow extends Module {

    private final NumberValue packets = new NumberValue("Packets", this, 20, 5, 100, true);
    private final NumberValue range = new NumberValue("Range", this, 25, 1, 100, true);
    private final BooleanValue friends = new BooleanValue("Friends", this, false);
    private final BooleanValue teams = new BooleanValue("Teams", this, false);

    public FastBow(ModuleData moduleData) {
        super(moduleData);
        register(packets, range, friends, teams);
    }

    @Subscribe
    public void onMotionUpdate(UpdateEvent event) {
        if (event.isPre())
            return;
        mc.timer.timerSpeed = 1.0F;
        final ItemStack heldItem = mc.thePlayer.getHeldItem();

        if (!mc.gameSettings.keyBindUseItem.getIsKeyPressed() || heldItem == null || !(heldItem.getItem() instanceof ItemBow))
            return;

        List<EntityLivingBase> targets = FightUtil.getMultipleTargets(range.getValue(), true, false, false, false, true);

        if (!friends.getValue()) {
            targets.removeIf(e -> Client.INSTANCE.getFriendManager().getObjects().contains(e.getName().toLowerCase()));
        }

        if (teams.getValue()) {
            targets.removeIf(FightUtil::isOnSameTeam);
        }

        targets.forEach(entityLivingBase -> {
            mc.thePlayer.getHeldItem().setItemDamage(0);

            if (Client.INSTANCE.getModuleManager().get(InfiniteDurability.class).isToggled()) {
                InventoryUtils.swap(9, mc.thePlayer.inventory.currentItem);
                InventoryUtils.swap(9, mc.thePlayer.inventory.currentItem);
            }

            PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
            Rotation rotation = AimUtil.getBowAngles(entityLivingBase);

            if (Client.INSTANCE.getModuleManager().get(Rotate.class).isToggled()) {
                mc.thePlayer.renderPitchHead = rotation.getRotationPitch();
                mc.thePlayer.renderYawOffset = rotation.getRotationYaw();
                mc.thePlayer.renderYawHead = rotation.getRotationYaw();
            }

            for (int i = 0; i < packets.getCastedValue().intValue(); ++i) {
                PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, rotation.getRotationYaw(), rotation.getRotationPitch(), true));
            }

            if (Client.INSTANCE.getModuleManager().get(InfiniteDurability.class).isToggled()) {
                InventoryUtils.swap(9, mc.thePlayer.inventory.currentItem);
                InventoryUtils.swap(9, mc.thePlayer.inventory.currentItem);
            }

            PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        });
    }
}
