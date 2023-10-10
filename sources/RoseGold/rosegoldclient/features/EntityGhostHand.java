package rosegoldclient.features;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.RenderUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityGhostHand {

    private static Entity entityToInteract;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!Main.configFile.entityGhostHand) return;
        if (event instanceof PlayerInteractEvent.RightClickBlock ||
                event instanceof PlayerInteractEvent.RightClickEmpty ||
                event instanceof PlayerInteractEvent.RightClickItem) {
            if (entityToInteract != null) {
                CursorTP.disable = true;
                interactWithEntity(entityToInteract);
                entityToInteract = null;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!Main.configFile.entityGhostHand) return;
        entityToInteract = null;
        CursorTP.disable = false;
        ArrayList<Entity> entities = getAllEntitiesInRange();
        for (Entity entity : entities) {
            if (isLookingAtAABB(entity.getEntityBoundingBox(), 1f)) {
                entityToInteract = entity;
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (Main.mc.player == null || Main.mc.world == null) return;
        if (!Main.configFile.entityGhostHand) return;
        if (entityToInteract != null) {
            Entity stand = getClosestArmorStand(entityToInteract);
            String entityName = "Null";
            if (stand != null) {
                entityName = stand.getCustomNameTag();
                if (entityName.equals("")) {
                    entityName = stand.getName();
                }
            }
            RenderUtils.drawEntityESP(entityToInteract, Color.MAGENTA, event.getPartialTicks());
            RenderUtils.renderWaypointText(entityName, entityToInteract.posX, entityToInteract.posY + entityToInteract.height, entityToInteract.posZ, event.getPartialTicks());
        }
    }

    private static void interactWithEntity(Entity entity) {
        Main.mc.playerController.interactWithEntity(
                Main.mc.player,
                entity,
                EnumHand.MAIN_HAND
        );
    }

    private static Entity getClosestArmorStand(Entity entity) {
        List<Entity> possibleEntities = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().offset(0, 1, 0), en -> (en != Main.mc.player));
        if (!possibleEntities.isEmpty()) {
            return possibleEntities.get(0);
        }
        return null;
    }

    private static boolean isLookingAtAABB(AxisAlignedBB aabb, float partialTicks) {
        Vec3d position = new Vec3d(Main.mc.player.posX, (Main.mc.player.posY + Main.mc.player.getEyeHeight()), Main.mc.player.posZ);
        Vec3d look = Main.mc.player.getLook(partialTicks);
        look = scaleVec(look, 0.1f);
        for (int i = 0; i < 70; i++) {
            if (aabb.minX <= position.x && aabb.maxX >= position.x && aabb.minY <= position.y && aabb.maxY >= position.y && aabb.minZ <= position.z && aabb.maxZ >= position.z) {
                return true;
            }
            position = position.add(look);
        }

        return false;
    }

    private static ArrayList<Entity> getAllEntitiesInRange() {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity1 : (Main.mc.world.getLoadedEntityList())) {
            if (!(entity1 instanceof EntityItem) && !(entity1 instanceof EntityXPOrb) && !(entity1 instanceof EntityWither) && !(entity1 instanceof EntityPlayerSP)) {
                entities.add(entity1);
            }
        }
        return entities;
    }

    private static Vec3d scaleVec(Vec3d vec, float f) {
        return new Vec3d(vec.x * f, vec.y * f, vec.z * f);
    }
}
