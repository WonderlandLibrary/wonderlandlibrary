package fun.expensive.client.feature.impl.combat;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.expensive.client.feature.Feature;
import fun.expensive.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AntiBot extends Feature {
    public static List<Entity> isBotPlayer = new ArrayList<>();
    public ListSetting antiBotMode = new ListSetting("AntiBot Mode", "Matrix", () -> true, "Matrix", "Wellmore");
    public BooleanSetting removeWorld = new BooleanSetting("Remove from World", true, () -> antiBotMode.currentMode.equalsIgnoreCase("Wellmore"));

    public AntiBot() {
        super("AntiBot", "Удаляет ботов созданных анти-читом", FeatureCategory.Combat);
        addSettings(antiBotMode, removeWorld);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        for (Entity entity : mc.world.playerEntities) {
            if (antiBotMode.currentMode.equalsIgnoreCase("Matrix")) {
                if (!entity.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.getName()).getBytes(StandardCharsets.UTF_8))) && entity instanceof EntityOtherPlayerMP) {
                    isBotPlayer.add(entity);
                }
            } else if (antiBotMode.currentMode.equalsIgnoreCase("Wellmore")) {
                for (Entity entity2 : mc.world.loadedEntityList) {
                    String Test = entity2.getDisplayName().getFormattedText();
                    if (!entity2.isInvisible() && entity2 != mc.player && entity2.ticksExisted <= 30 && mc.player.getDistanceToEntity(entity2) <= 8) {
                        if (!Test.startsWith("§7")) {
                            mc.world.removeEntity(entity2);
                        }
                    }
                }
            }
        }
    }
}

