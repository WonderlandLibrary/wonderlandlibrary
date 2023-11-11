package me.nyan.flush.module.impl.combat;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.utils.other.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class AntiBot extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Hypixel", "Hypixel", "Erisium");
    private final BooleanSetting removeFromWorld = new BooleanSetting("Remove From World", this, false);

    private final Timer timer = new Timer();

    public AntiBot() {
        super("AntiBot", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (removeFromWorld.getValue()) {
            ArrayList<EntityPlayer> bots = new ArrayList<>();

            for (EntityPlayer player : mc.theWorld.playerEntities) {
                if (isBot(player)) {
                    bots.add(player);
                }
            }
            bots.forEach(mc.theWorld::removeEntity);
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }

    public boolean isBot(Entity e) {
        if (!isEnabled() || e == mc.thePlayer || !(e instanceof EntityPlayer)) {
            return false;
        }
        switch (mode.getValue().toLowerCase()) {
            case "hypixel":
                return !isEntityInTabList(e) && e.ticksExisted >= 20;
            case "erisium":
                return e.isInvisible() && e.ticksExisted < 100 && hasNoArmor((EntityPlayer) e);
        }
        return false;
    }

    private static boolean hasNoArmor(EntityPlayer entity) {
        for (int i = 0; i < entity.inventory.armorInventory.length; ++i) {
            if (entity.getEquipmentInSlot(i) != null) {
                return false;
            }
        }
        return true;
    }

    private boolean isEntityInTabList(Entity entity) {
        return mc.getNetHandler().getPlayerInfoMap().stream().anyMatch(info -> info.getGameProfile().getName().equalsIgnoreCase(entity.getName()));
    }
}
