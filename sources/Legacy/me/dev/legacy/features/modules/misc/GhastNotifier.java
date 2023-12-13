package me.dev.legacy.features.modules.misc;

import java.util.HashSet;
import java.util.Set;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.init.SoundEvents;

public class GhastNotifier extends Module {

    private Set<Entity> ghasts = new HashSet<Entity>();
    public Setting<Boolean> Chat = this.register(new Setting<Boolean>("Chat", true));
    public Setting<Boolean> Sound = this.register(new Setting<Boolean>("Sound", true));

    public GhastNotifier() {
        super("GhastNotifier", "Helps you find ghasts", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        this.ghasts.clear();
    }

    @Override
    public void onUpdate() {
        for (Entity entity : GhastNotifier.mc.world.getLoadedEntityList()) {
            if (!(entity instanceof EntityGhast) || this.ghasts.contains(entity)) continue;
            if (this.Chat.getValue().booleanValue()) {
                Command.sendMessage("Ghast Detected at: " + entity.getPosition().getX() + "x, " + entity.getPosition().getY() + "y, " + entity.getPosition().getZ() + "z.");
            }
            this.ghasts.add(entity);
            if (!this.Sound.getValue().booleanValue()) continue;
            GhastNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
        }
    }
}