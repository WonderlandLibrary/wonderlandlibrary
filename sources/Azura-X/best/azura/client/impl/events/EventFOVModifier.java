package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class EventFOVModifier implements NamedEvent {
    private float modifier;

    public EventFOVModifier(float modifier) {
        this.modifier = modifier;
    }

    public float getModifier() {
        return modifier;
    }

    public void setModifier(float modifier) {
        this.modifier = modifier;
    }

    public void applyPotionModifiers(boolean doSprint, boolean reset) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (reset) modifier = 1.0f;
        if (mc.thePlayer.isSprinting() && doSprint) modifier *= 1.15;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) modifier *= 1.1 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
        if (mc.thePlayer.isPotionActive(Potion.moveSlowdown)) modifier /= 1.1 + mc.thePlayer.getActivePotionEffect(Potion.moveSlowdown).getAmplifier();
    }

    @Override
    public String name() {
        return "fovModifier";
    }
}