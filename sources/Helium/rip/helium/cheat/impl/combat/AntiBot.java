package rip.helium.cheat.impl.combat;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerUpdateEvent;

import java.util.ArrayList;

public class AntiBot extends Cheat {


    public static ArrayList<EntityPlayer> bots = new ArrayList<>();

    public AntiBot() {
        super("AntiBot", "Removes Bots", CheatCategory.COMBAT);

    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent e) {
        if (mc.getCurrentServerData() != null && mc.theWorld != null && mc.getCurrentServerData().serverIP.contains("hypixel")) {
            if (e.isPre()) {
                if (mc.thePlayer.ticksExisted % 600 == 0) {
                    bots.clear();
                }
            }
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityPlayer) {
                    if (entity != mc.thePlayer) {
                        //EntityPlayer entity = (EntityPlayer) entities;
                        if (entity != mc.thePlayer) {

                            if (mc.thePlayer.getDistanceToEntity(entity) < 10) {
                                if (!entity.getDisplayName().getFormattedText().startsWith("§") || entity.isInvisible() || entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
                                    bots.add((EntityPlayer) entity);
                                    //ClientBase.chat(entity.getName() + " has big gay");
                                }
                            }
                        }
                        if (bots.contains(entity) && !entity.isInvisible()) {
                            bots.remove(entity);

                        }
                    } else {
                        bots.remove(entity);
                    }
                }

            }
        } else if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.contains("mineplex")) {
            for (final EntityPlayer object2 : mc.theWorld.playerEntities) {
                EntityPlayer entity = object2;
                if (!(object2.getHealth() == Double.NaN)) {
                    bots.add(object2);
                }
            }
        }
    }

    private boolean isBot(EntityLivingBase entity) {
        return (entity.isInvisible() && !entity.onGround && entity.isPotionActive(14) == false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        bots.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        bots.clear();
    }
}
