package rosegoldclient.features;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.FontUtils;
import rosegoldclient.utils.RenderUtils;
import rosegoldclient.utils.WynncraftItem;

import java.util.HashMap;
import java.util.HashSet;

public class DroppedItemESP {
    private static final HashMap<EntityItem, WynncraftItem> highlightedItems = new HashMap<>();
    private static final HashSet<EntityItem> checked = new HashSet<>();
    private static int titleCooldown = 0;
    private static String mythicName = null;

    private static void highlightItem(EntityItem entityItem, WynncraftItem wynncraftItem) {
        highlightedItems.put(entityItem, wynncraftItem);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Main.configFile.droppedItemESP || Main.mc.player == null) return;
        if(titleCooldown > 0) titleCooldown--;
        if(Main.mc.player.ticksExisted % 40 == 0) {
            checked.clear();
        }
        for(EntityItem item : Main.mc.world.getEntities(EntityItem.class, o -> true)) {
            if(checked.contains(item)) continue;
            checked.add(item);
            if(item.getItem().getTagCompound() == null) continue;
            WynncraftItem wynncraftItem = checkName(item.getItem().getDisplayName());
            if(wynncraftItem != null) {
                highlightItem(item, wynncraftItem);
                if(wynncraftItem.getTier().equals("Mythic")) {
                    titleCooldown = 40;
                    mythicName = wynncraftItem.getTextColor() + wynncraftItem.getName();
                    playAnnoyingAlert();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Main.configFile.droppedItemESP) return;
        Main.mc.world.loadedEntityList.forEach(entity -> {
            if(entity instanceof EntityItem) {
                EntityItem entityItem = (EntityItem) entity;
                if (highlightedItems.containsKey(entityItem)) {
                    WynncraftItem wynncraftItem = highlightedItems.get(entityItem);
                    String name = wynncraftItem.getName();
                    RenderUtils.drawEntityESP(entityItem, wynncraftItem.getColor(), event.getPartialTicks(), new AxisAlignedBB(-0.2,0,-0.2,0.2,0.4,0.2));
                    RenderUtils.renderWaypointText(wynncraftItem.getTextColor() + name, entityItem.posX, entityItem.posY + 1, entityItem.posZ, event.getPartialTicks(), false);
                }
            }
        });
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        if(!Main.configFile.droppedItemESP || titleCooldown == 0) return;
        String str = " Mythic Item Dropped!!!";
        FontUtils.drawScaledCenteredString( mythicName + str, 3, Main.mc.displayWidth / 4, (int) (Main.mc.displayHeight * 0.15), true);
    }

    private static WynncraftItem checkName(String name) {
        WynncraftItem wynncraftItem = Main.wynncraftItems.get(name);
        if(wynncraftItem != null) {
            if (Main.configFile.droppedItemESPLegendaries && wynncraftItem.getTier().equals("Legendary") ||
                    Main.configFile.droppedItemESPFableds && wynncraftItem.getTier().equals("Fabled") ||
                    Main.configFile.droppedItemESPMythics && wynncraftItem.getTier().equals("Mythic")) {
                return wynncraftItem;
            }
        }

        return null;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        highlightedItems.clear();
        checked.clear();
    }

    private static void playAnnoyingAlert() {
        new Thread(() -> {
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
        }).start();
    }
}
