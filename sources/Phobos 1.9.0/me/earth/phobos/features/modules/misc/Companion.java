package me.earth.phobos.features.modules.misc;

import com.mojang.text2speech.Narrator;
import me.earth.phobos.event.events.DeathEvent;
import me.earth.phobos.event.events.TotemPopEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Companion
        extends Module {
    public Setting<String> totemPopMessage = this.register(new Setting<String>("PopMessage", "<player> watch out you're popping!"));
    public Setting<String> deathMessages = this.register(new Setting<String>("DeathMessage", "<player> you retard you just fucking died!"));
    private final Narrator narrator = Narrator.getNarrator();

    public Companion() {
        super("Companion", "The best module", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        this.narrator.say("Hello and welcome to phobos");
    }

    @Override
    public void onDisable() {
        this.narrator.clear();
    }

    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (event.getEntity() == Companion.mc.player) {
            this.narrator.say(this.totemPopMessage.getValue().replaceAll("<player>", Companion.mc.player.getName()));
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.player == Companion.mc.player) {
            this.narrator.say(this.deathMessages.getValue().replaceAll("<player>", Companion.mc.player.getName()));
        }
    }
}

