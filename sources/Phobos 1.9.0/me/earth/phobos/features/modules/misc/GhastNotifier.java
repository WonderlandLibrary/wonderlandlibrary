package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.init.SoundEvents;

import java.util.HashSet;
import java.util.Set;

public class GhastNotifier
        extends Module {
    public Setting<Boolean> Chat = this.register(new Setting<Boolean>("Chat", true));
    public Setting<Boolean> Sound = this.register(new Setting<Boolean>("Sound", true));
    private final Set<Entity> ghasts = new HashSet<Entity>();

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

