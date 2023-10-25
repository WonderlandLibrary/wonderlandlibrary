package wtf.evolution.module.impl.Combat;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
@ModuleInfo(name = "AntiBot", type = Category.Combat)
public class AntiBot extends Module {

    public static ArrayList<Entity> isBot = new ArrayList<>();

    @EventTarget
    public void onMotion(EventMotion e) {
        for (Entity entity : mc.world.loadedEntityList) {

            if (!entity.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.getName()).getBytes(StandardCharsets.UTF_8))) && entity instanceof EntityOtherPlayerMP) {
                if (!isBot.contains(entity)) {
                    isBot.add(entity);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        isBot.clear();
    }

}
