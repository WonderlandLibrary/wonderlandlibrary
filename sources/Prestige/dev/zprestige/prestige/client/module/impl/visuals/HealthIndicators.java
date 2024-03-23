package dev.zprestige.prestige.client.module.impl.visuals;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.HealtEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

public class HealthIndicators extends Module {

    public HealthIndicators() {
        super("Health Indicators", Category.Visual, "Shows health on players");
    }

    @EventListener
    public void event(HealtEvent event) {
        float f = (float)Math.ceil(getHealth(event.getPlayer()));
        String string = " | " + getColor(f) + f + Formatting.RESET + " | ";
        event.setText(string);
        event.setCancelled();
    }

    private float getHealth(PlayerEntity playerEntity) {
        return playerEntity.getHealth() + playerEntity.getAbsorptionAmount();
    }

    private Formatting getColor(float f) {
        if (f <= 5) {
            return Formatting.RED;
        }
        if (f <= 10) {
            return Formatting.GOLD;
        }
        if (f <= 15) {
            return Formatting.YELLOW;
        }
        return f <= 20 ? Formatting.GREEN : Formatting.DARK_GREEN;
    }
}
