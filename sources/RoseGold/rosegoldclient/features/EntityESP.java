package rosegoldclient.features;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rosegoldclient.Main;
import rosegoldclient.events.RenderLivingEntityEvent;
import rosegoldclient.utils.RenderUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class EntityESP {
    private static final HashMap<Entity, String> highlightedEntities = new HashMap<>();
    private static final HashSet<Entity> checked = new HashSet<>();

    private static void highlightEntity(Entity entity, String name) {
        highlightedEntities.put(entity, name);
    }

    private static boolean checkName(String name) {
        if (name.contains("[Lv") && name.contains("]")) {
            return true;
        }
        if (Main.configFile.NPCESP && name.contains("NPC")) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onRenderEntityLiving(RenderLivingEntityEvent event) {
        if (checked.contains(event.entity)) return;
        if (Main.configFile.revealInsivibleEntities) event.entity.setInvisible(false);
        if (!Main.configFile.entityESP && !Main.configFile.NPCESP) return;
        if (event.entity.hasCustomName() && checkName(event.entity.getCustomNameTag())) {
            if (Main.configFile.entityESPRange != 0 && event.entity.getDistance(Main.mc.player) > Main.configFile.entityESPRange)
                return;
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
        if (!Main.configFile.entityESP && !Main.configFile.NPCESP) return;
        Main.mc.world.loadedEntityList.forEach(entity -> {
            if (highlightedEntities.containsKey(entity)) {
                String name = highlightedEntities.get(entity);
                Color color = name.contains("§a") ? Color.GREEN : name.contains("§b") ? Color.CYAN : name.contains("§c") ? Color.RED : name.contains("§7") ? new Color(0, 127, 0) : Color.WHITE;
                RenderUtils.drawEntityESP(entity, color, event.getPartialTicks());
                RenderUtils.renderWaypointText(name, entity.posX, entity.posY + entity.height, entity.posZ, event.getPartialTicks());
            }
        });
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Main.mc.player == null) return;
        if (!Main.configFile.entityESP && !Main.configFile.NPCESP) return;
        if (Main.mc.player.ticksExisted % 40 == 0) {
            checked.clear();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        highlightedEntities.clear();
        checked.clear();
    }
}
