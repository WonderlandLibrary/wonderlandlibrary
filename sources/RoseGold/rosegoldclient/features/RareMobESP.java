package rosegoldclient.features;

import gg.essential.api.utils.Multithreading;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldclient.Main;
import rosegoldclient.events.RenderLivingEntityEvent;
import rosegoldclient.events.SecondEvent;
import rosegoldclient.utils.FontUtils;
import rosegoldclient.utils.RenderUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RareMobESP {
    private static final HashMap<Entity, String> highlightedEntities = new HashMap<>();
    private static final HashMap<Entity, String> existingEntities = new HashMap<>();
    private static final HashSet<Entity> checked = new HashSet<>();

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        if (!Main.configFile.rareMobESP) return;
        int i = 0;
        for(Entity entity : existingEntities.keySet()) {
            i++;
            FontUtils.drawScaledCenteredString("§b" + existingEntities.get(entity) + " detected nearby!", 1, Main.mc.displayWidth / 4, (int) (Main.mc.displayHeight * 0.03 * i), true);
        }
    }

    @SubscribeEvent
    public void onRenderEntityLiving(RenderLivingEntityEvent event) {
        if (checked.contains(event.entity)) return;
        if (!Main.configFile.rareMobESP) return;
        if (event.entity.hasCustomName() && checkName(event.entity.getCustomNameTag())) {
            if (event.entity instanceof EntityArmorStand) {
                List<Entity> possibleEntities = event.entity.getEntityWorld().getEntitiesInAABBexcluding(event.entity, event.entity.getEntityBoundingBox().offset(0, -1, 0), entity -> (!(entity instanceof EntityArmorStand) && entity != Main.mc.player));
                if (!possibleEntities.isEmpty()) {
                    highlightEntity(possibleEntities.get(0), event.entity.getCustomNameTag());
                } else {
                    highlightEntity(event.entity, event.entity.getCustomNameTag());
                }
            } else {
                highlightEntity(event.entity, event.entity.getCustomNameTag());
            }
            checked.add(event.entity);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Main.configFile.rareMobESP) return;
        existingEntities.clear();
        Main.mc.world.loadedEntityList.forEach(entity -> {
            if (highlightedEntities.containsKey(entity)) {
                String name = highlightedEntities.get(entity);
                existingEntities.put(entity, name);
                RenderUtils.drawEntityESP(entity, Color.MAGENTA, event.getPartialTicks());
                RenderUtils.renderWaypointText(name, entity.posX, entity.posY + entity.height, entity.posZ, event.getPartialTicks());
            }
        });
    }

    @SubscribeEvent
    public void onSecond(SecondEvent event) {
        if (!Main.configFile.rareMobESP) return;
        if(!Main.configFile.notifyRareMobESP) return;
        if(existingEntities.size() == 0) return;
        playAnnoyingAlert();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Main.mc.player == null) return;
        if (!Main.configFile.rareMobESP) return;
        if (Main.mc.player.ticksExisted % 40 == 0) {
            checked.clear();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        highlightedEntities.clear();
        checked.clear();
    }

    private static void playAnnoyingAlert() {
        Multithreading.runAsync(() -> {
            try {
                Main.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 0.5F);
                Thread.sleep(100);
                Main.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 0.5F);
                Thread.sleep(100);
                Main.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 0.5F);
                Thread.sleep(100);
                Main.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 0.5F);
                Thread.sleep(100);
                Main.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 0.5F);
                Thread.sleep(100);
                Main.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 0.5F);
                Thread.sleep(100);
                Main.mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 0.5F);
            } catch (Exception ignored) {}
        });
    }

    private static void highlightEntity(Entity entity, String name) {
        highlightedEntities.put(entity, name);
    }

    private static boolean checkName(String name) {
        String[] split = Main.configFile.rareMobESPFilter.split(",");
        Set<String> entityNames = Stream.of(split).collect(Collectors.toSet());
        return entityNames.stream().anyMatch(name::contains);
    }
}
