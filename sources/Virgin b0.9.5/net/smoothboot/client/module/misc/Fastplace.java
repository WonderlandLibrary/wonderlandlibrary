package net.smoothboot.client.module.misc;

import net.minecraft.client.MinecraftClient;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.mixin.MinecraftClientAccessor;
import net.smoothboot.client.module.Mod;

public class Fastplace extends Mod {

        public Fastplace() {
        super("Fast Place", "Removes block place delay", Category.Misc);
    }

    @Override
    public void onTick() {
                ((MinecraftClientAccessor) MinecraftClient.getInstance()).setItemUseCooldown(0);
        super.onTick();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}