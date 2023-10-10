package tech.atani.client.feature.module.impl.server.hypixel;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Native
@ModuleData(name = "AutoBounty", identifier = "mc.hypixel.net AutoBounty", description = "Tracks people bounty on the Pit", category = Category.SERVER, supportedIPs = {"mc.hypixel.net"})
public class AutoBounty extends Module {
    
    private final SliderValue<Integer> minBounty = new SliderValue<>("Minimum Bounty", "What should be the minimum bounty?", this, 500, 50, 5000, 0);

    private final List<Entity> entities = new CopyOnWriteArrayList<>();

    @Listen
    public void onUpdate(UpdateEvent event) {
        entities.removeIf(player -> !mc.theWorld.playerEntities.contains(player));

        double maxDistanceSq = 200 * 200;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != mc.thePlayer && entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;

                double distanceSq = mc.thePlayer.getDistanceSqToEntity(player);

                if (distanceSq <= maxDistanceSq) {
                    String display = player.getDisplayName().getUnformattedText();
                    String name = player.getCommandSenderName();

                    if (display.contains("§6§l")) {
                        String[] split = display.split(" ");
                        if (split.length > 2) {
                            int bounty = Integer.parseInt(
                                    split[split.length - 1]
                                            .replace("§6§1l", "")
                                            .replace("g", "")
                            );
                            if (bounty >= minBounty.getValue()) {
                                if (!entities.contains(player)) {
                                    entities.add(player);
                                    sendMessage(name + " has " + bounty + "g on him!");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
