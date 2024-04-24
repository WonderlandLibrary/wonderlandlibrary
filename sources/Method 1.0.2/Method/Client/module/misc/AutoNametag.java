package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.visual.ChatUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.Comparator;

import static Method.Client.Main.setmgr;

public class AutoNametag extends Module {

    Setting Radius = setmgr.add( new Setting("range", this, 4, 0, 10, true));
    Setting ReplaceOldNames = setmgr.add( new Setting("ReplaceOldNames", this, true));
    Setting AutoSwitch = setmgr.add( new Setting("AutoSwitch", this, true));
    Setting WithersOnly = setmgr.add( new Setting("WithersOnly ", this, true));


    public AutoNametag() {
        super("AutoNametag", Keyboard.KEY_NONE, Category.MISC, "AutoNametag");
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.currentScreen != null) {
            return;
        }

        ItemStack nameTagItem = findNameTagItem();

        if (nameTagItem == null || !nameTagItem.hasDisplayName()) {
            return;
        }

        EntityLivingBase targetEntity = findTargetEntity(nameTagItem.getDisplayName());

        if (targetEntity == null) {
            return;
        }

        giveNameTagToEntity(nameTagItem, targetEntity);
    }

    private ItemStack findNameTagItem() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);

            if (itemStack.getItem() instanceof ItemNameTag && itemStack.hasDisplayName()) {
                return itemStack;
            }
        }

        return null;
    }

    private EntityLivingBase findTargetEntity(String displayName) {
        return mc.world.loadedEntityList.stream()
                .filter(entity -> isValidTarget(entity, displayName))
                .map(entity -> (EntityLivingBase) entity)
                .min(Comparator.comparingDouble(entity -> mc.player.getDistance(entity)))
                .orElse(null);
    }

    private boolean isValidTarget(Entity entity, String displayName) {
        return entity instanceof EntityLivingBase
                && entity != mc.player
                && entity.getDistance(mc.player) <= Radius.getValDouble()
                && !(entity instanceof EntityPlayer)
                && (entity.getCustomNameTag().isEmpty() || ReplaceOldNames.getValBoolean() && !entity.getName().equals(displayName))
                && (!WithersOnly.getValBoolean() || entity instanceof EntityWither);
    }

    private void giveNameTagToEntity(ItemStack nameTagItem, EntityLivingBase targetEntity) {
        double[] lookAt = calculateLookAt(targetEntity.posX, targetEntity.posY, targetEntity.posZ, mc.player);

        mc.player.rotationYawHead = (float) lookAt[0];
        mc.playerController.interactWithEntity(mc.player, targetEntity, EnumHand.MAIN_HAND);
        ChatUtils.message(String.format("Gave %s the nametag of %s", targetEntity.getName(), nameTagItem.getDisplayName()));
    }

    private double[] calculateLookAt(double targetX, double targetY, double targetZ, EntityPlayer player) {
        double diffX = player.posX - targetX;
        double diffY = player.posY - targetY;
        double diffZ = player.posZ - targetZ;

        double distance = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

        double pitch = Math.asin(diffY / distance) * 180.0 / Math.PI;
        double yaw = (Math.atan2(diffZ, diffX) * 180.0 / Math.PI) + 90.0;

        return new double[]{yaw, pitch};
    }



}
