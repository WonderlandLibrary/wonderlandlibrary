package Method.Client.module.combat;


import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.Utils;
import Method.Client.utils.ValidUtils;
import Method.Client.utils.visual.ChatUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;

public class BowMod extends Module {

    public Setting BowSpam = setmgr.add(new Setting("BowSpam", this, false));
    public Setting PacketSpam = setmgr.add(new Setting("PacketSpam", this, false, BowSpam));
    public Setting AimBot = setmgr.add(new Setting("AimBot", this, false));
    public Setting walls = setmgr.add(new Setting("walls", this, false, AimBot));
    public Setting yaw = setmgr.add(new Setting("Yaw", this, 22, 0, 50, false, AimBot));
    public Setting FOV = setmgr.add(new Setting("FOV", this, 90, 1, 180, true, AimBot));
    public Setting KickBow = setmgr.add(new Setting("KickBow", this, false));

    public EntityLivingBase target;
    public float rangeAimVelocity = 0;


    public BowMod() {
        super("BowMod", Keyboard.KEY_NONE, Category.COMBAT, "BowMod");
    }

    @Override
    public void onDisable() {
        this.target = null;
        super.onDisable();
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        mc.player.inventory.getCurrentItem();
        if (!(mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow)) {
            return;
        }
        if (!mc.gameSettings.keyBindUseItem.isKeyDown()) {
            return;
        }
        if (KickBow.getValBoolean()) {
            if (mc.player.inventory.getCurrentItem().getItem() instanceof net.minecraft.item.ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 25) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                mc.player.stopActiveHand();
                if (findShulker() != -1) {
                    changeItem(findShulker());
                } else {
                    ChatUtils.message("No shulker found in hotbar, not switching...");
                    this.toggle();
                }
            }
        }
        if (AimBot.getValBoolean()) {
            this.target = this.getClosestEntity();

            if (this.target == null) {
                return;
            }

            float rangeCharge = mc.player.getItemInUseCount();

            rangeAimVelocity = rangeCharge / 20;
            rangeAimVelocity = (rangeAimVelocity * rangeAimVelocity + rangeAimVelocity * 2) / 3;

            double posX = this.target.posX - mc.player.posX;
            double posY = this.target.posY + this.target.getEyeHeight() - 0.15 - mc.player.posY - mc.player.getEyeHeight();
            double posZ = this.target.posZ - mc.player.posZ;
            double y2 = Math.sqrt(posX * posX + posZ * posZ);
            float g = 0.006F;
            float tmp = (float) (rangeAimVelocity * rangeAimVelocity * rangeAimVelocity * rangeAimVelocity - g * (g * (y2 * y2) + 2 * posY * (rangeAimVelocity * rangeAimVelocity)));
            float pitch = (float) -Math.toDegrees(Math.atan((rangeAimVelocity * rangeAimVelocity - Math.sqrt(tmp)) / (g * y2)));

            float[] rot = Utils.getNeededRotations(this.target.getPositionVector(), (float) yaw.getValDouble(), 0);
            mc.player.rotationYaw = rot[0];
            mc.player.rotationPitch = pitch;
        }

        if (BowSpam.getValBoolean()) {
            if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow) {
                if (mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                    if (PacketSpam.getValBoolean())
                        for (int var1 = 0; var1 < 10; ++var1) {
                            mc.player.connection.sendPacket(new CPacketPlayer(true));
                        }
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                    mc.player.stopActiveHand();
                }
            }
        }
    }

    public int findShulker() {
        byte b = -1;
        for (byte b1 = 0; b1 < 9; b1++) {
            ItemStack itemStack = mc.player.inventory.mainInventory.get(b1);
            if (itemStack.getItem() instanceof net.minecraft.item.ItemShulkerBox)
                b = b1;
        }
        return b;
    }

    public void changeItem(int paramInt) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(paramInt));
        mc.player.inventory.currentItem = paramInt;
    }


    public boolean check(EntityLivingBase entity) {
        if (Checks(entity, FOV)) return false;
        if (!this.walls.getValBoolean()) {
            return mc.player.canEntityBeSeen(entity);
        }
        return true;
    }
    boolean Checks(EntityLivingBase entity, Setting fov) {
        if (entity instanceof EntityArmorStand) {
            return true;
        }
        if (entity == mc.player) {
            return true;
        }
        if (entity.isDead) {
            return true;
        }
        if (ValidUtils.isBot(entity)) {
            return true;
        }
        if ((ValidUtils.isFriendEnemy(entity))) {
            return true;
        }
        if (!ValidUtils.isInAttackFOV(entity, (int) fov.getValDouble())) {
            return true;
        }
        if (entity instanceof EntityPlayer)
            return false;
        return true;
    }

    EntityLivingBase getClosestEntity() {
        EntityLivingBase closestEntity = null;
        for (Object o : mc.world.loadedEntityList) {
            if (o instanceof EntityLivingBase && !(o instanceof EntityArmorStand)) {
                EntityLivingBase entity = (EntityLivingBase) o;
                if (check(entity)) {
                    if (closestEntity == null || mc.player.getDistance(entity) < mc.player.getDistance(closestEntity)) {
                        closestEntity = entity;
                    }
                }
            }
        }
        return closestEntity;
    }
}
