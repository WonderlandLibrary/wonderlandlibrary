package net.caffeinemc.phosphor.module.modules.misc;

import net.caffeinemc.phosphor.api.event.events.TickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.module.Module;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TextColor;

import java.util.HashSet;

public class TeamsModule extends Module {
    public TeamsModule() {
        super("Teams", "Doesn't allow combat features to attack teammates", Category.MISC);
    }

    public final HashSet<AbstractClientPlayerEntity> teammates = new HashSet<>();

    @Override
    public void onEnable() {
        teammates.clear();
    }

    @Override
    public void onDisable() {
        teammates.clear();
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (mc.world == null || mc.player == null) return;

        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (player instanceof ClientPlayerEntity) continue;

            OrderedText orderedDisplayName = player.getDisplayName().asOrderedText();
            OrderedText orderedLocalDisplayName = mc.player.getDisplayName().asOrderedText();

            // Checking only 1st character because returning false after 1st
            orderedLocalDisplayName.accept(((index, style, codePoint) -> {
                TextColor localColor = style.getColor();

                if (localColor != null) {
                    orderedDisplayName.accept(((index1, style1, codePoint1) -> {
                        TextColor color = style1.getColor();

                        if (color != null && color.equals(localColor)) {
                            teammates.add(player);
                        } else {
                            teammates.remove(player);
                        }

                        return false;
                    }));
                } else {
                    teammates.remove(player);
                }

                return false;
            }));
        }
    }
}
