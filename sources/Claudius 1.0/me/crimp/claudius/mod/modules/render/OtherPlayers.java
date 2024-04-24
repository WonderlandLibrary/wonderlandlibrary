package me.crimp.claudius.mod.modules.render;

import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;

public class OtherPlayers extends Module {
    public final Setting<Boolean> crouch = register(new Setting<>("EveryoneCrouches", true));
    public final Setting<Boolean> Glowing = register(new Setting<>("EveryoneGlows", true));
    public final Setting<Boolean> invis = register(new Setting<>("Everyone invis", true));
    public final Setting<Boolean> funny = register(new Setting<>("Everyone Becomes funny", true));

    public OtherPlayers() {
        super("OtherPlayers", "Does Stuff To Other Players", Category.Render, false, false);
    }
    public static OtherPlayers INSTANCE;
    @Override
    public void onUpdate() {
        if (crouch.getValue()) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player.equals(mc.player)) continue;
                player.setSneaking(true);
            }
        }
        if (Glowing.getValue()) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player.equals(mc.player)) continue;
                player.setGlowing(true);
            }
        }
        if (invis.getValue()) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player.equals(mc.player)) continue;
                player.setInvisible(true);
            }
        }
        if (funny.getValue()) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player.equals(mc.player)) continue;
                player.setCustomNameTag("Crimply");
            }
        }
    }
}
