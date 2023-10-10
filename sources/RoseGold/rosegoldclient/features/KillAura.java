package rosegoldclient.features;

import gg.essential.api.utils.Multithreading;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.commands.KillAuraFilter;
import rosegoldclient.events.PlayerMoveEvent;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.RenderUtils;
import rosegoldclient.utils.RotationUtils;

import java.awt.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAura {

    public static EntityLivingBase target;
    private static final CPacketPlayerTryUseItem use = new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND);
    private static final CPacketAnimation swing = new CPacketAnimation(EnumHand.MAIN_HAND);

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if(!Main.killAura || !Main.configFile.killAura || Main.mc.player == null || Main.mc.world == null) return;
        target = getEntity();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onUpdatePre(PlayerMoveEvent.Pre pre) {
        if(!Main.killAura) {
            target = null;
            return;
        }
        if(!Main.configFile.killAura || Main.mc.player == null || Main.mc.world == null) return;
        if (target != null) {
            if(Main.configFile.killAuraType == 1) {
                RotationUtils.smoothLook(RotationUtils.getBowRotationToEntity(target), 0, () -> {});
            } else {
                RotationUtils.smoothLook(RotationUtils.getRotationToEntity(target), 0, () -> {});
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUpdatePost(PlayerMoveEvent.Post post) {
        if(!Main.killAura || !Main.configFile.killAura || Main.mc.player == null || Main.mc.world == null) return;
        if(Main.mc.getConnection() == null) return;
        if(target == null) return;
        if(Main.mc.player.ticksExisted % 2 != 0) return;
        if(SpellCaster.packetList.size() != 0) return;
        if(AutoSneak.abilityReady) {
            Main.mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(Main.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            attack();
            Multithreading.runAsync(() -> {
                try {
                    Thread.sleep(100);
                    Main.mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(Main.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                } catch (Exception ignored) {}
            });
        } else {
            attack();
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if(!Main.killAura || !Main.configFile.killAura || Main.mc.player == null || Main.mc.world == null || !Main.configFile.highlightKA) return;
        if (target != null) {
            RenderUtils.drawEntityESP(target, Color.RED, event.getPartialTicks());
        }
    }

    private static void attack() {
        if (Main.configFile.killAuraType == 1) { //bow
            Main.mc.getConnection().getNetworkManager().sendPacket(use);
        } else {
            Main.mc.getConnection().getNetworkManager().sendPacket(swing);
        }
    }

    private static EntityLivingBase getEntity() {
        if(Main.mc.world == null) return null;
        float range = 4F;
        boolean throughWalls = true;
        switch (Main.configFile.killAuraType) {
            case 1: //bow
                range = 40F;
                throughWalls = false;
                break;
            case 2: //mage beam
                range = 12F;
                break;
            case 3: //shaman
                range = 30F;
        }

        float finalRange = range;
        boolean finalThroughWalls = throughWalls;

        List<Entity> entityList = Main.mc.world.getLoadedEntityList().stream().filter(
                entity -> entity instanceof EntityLivingBase
        ).filter(
                entity -> isValid((EntityLivingBase)entity, finalRange, finalThroughWalls)
        ).sorted(
                Comparator.comparingDouble(e -> e.getDistance(Main.mc.player))
        ).collect(Collectors.toList());
        //can do more sorting here
        Iterator<Entity> iterator = entityList.iterator();
        if(iterator.hasNext()) {
            Entity en = iterator.next();
            return (EntityLivingBase) en;
        }
        return null;
    }

    private static boolean isValid(EntityLivingBase entity, float range, boolean throughWalls) {
        if(Main.configFile.killAuraCustomNames && !entity.hasCustomName()) {
            return false;
        }
        if(entity.getCustomNameTag().contains(" By ")) return false;
        if(Main.configFile.killAuraFilter) {
            if(Main.configFile.killAuraFilterBlacklist) {
                for (String search : KillAuraFilter.KASettings) {
                    if (removeFormatting(entity.getCustomNameTag()).contains(search)) {
                        return false;
                    }
                }
            } else {
                b1:
                {
                    for (String search : KillAuraFilter.KASettings) {
                        if (removeFormatting(entity.getCustomNameTag()).contains(search)) {
                            break b1;
                        }
                    }
                    return false;
                }
            }
        }
        b2: {
            b3: {
                if (entity == Main.mc.player) {
                    break b3;
                }
                if((entity instanceof EntityPlayer || entity instanceof EntityWither || entity instanceof EntityBat) && entity.isInvisible()) {
                    break b3;
                }
                if(!Main.mc.player.canEntityBeSeen(entity) && !throughWalls) {
                    break b3;
                }
                if(entity.getHealth() <= 0.0f) {
                    break b3;
                }
                double dist = entity.getDistance(Main.mc.player);
                if(range > dist) break b2;
            }
            return false;
        }
        if (entity instanceof EntityPlayer) {
            return true;
        }
        return !(entity instanceof EntityVillager);
    }

    private static String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }

}
