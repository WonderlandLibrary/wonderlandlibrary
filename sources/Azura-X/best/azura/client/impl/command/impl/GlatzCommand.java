package best.azura.client.impl.command.impl;

import best.azura.client.impl.Client;
import best.azura.client.api.command.ACommand;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventRender2D;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GlatzCommand extends ACommand {

    private final DelayUtil delayUtil = new DelayUtil(), hideDelay = new DelayUtil();
    private boolean show = false;

    @Override
    public String getName() {
        return "glatzscare";
    }

    @Override
    public String getDescription() {
        return "Spawns a GLATZKOPF";
    }

    @Override
    public String[] getAliases() {
        return new String[] { "glatz", "g", "bald", "glatzkopf" };
    }

    @Override
    public void handleCommand(String[] args) {
        if (Client.INSTANCE.getEventBus().isRegistered(this))
            Client.INSTANCE.getEventBus().unregister(this);
        else Client.INSTANCE.getEventBus().register(this);
    }

    @EventHandler
    private void onEvent(final Event event) {
        if (event instanceof EventRender2D) {
            final ScaledResolution sr = new ScaledResolution(mc);
            if (hideDelay.hasReached(1000)) show = false;
            if (!show) return;
            GlStateManager.resetColor();
            GlStateManager.enableAlpha();
            GlStateManager.color(1, 1, 1, 1);
            mc.getTextureManager().bindTexture(new ResourceLocation("custom/glatzkopf.png"));
            RenderUtil.INSTANCE.drawTexture(0, 0, sr.getScaledWidth(), sr.getScaledHeight());
        }
        if (event instanceof EventMotion && ((EventMotion) event).isPre() && delayUtil.hasReached(MathUtil.getRandom_int(1000 * 5, 60 * 4 * 1000))) {
            for (int i = 0; i < 60; i++) {
                mc.thePlayer.playSound("random.eat", 50, 1f);
                mc.thePlayer.playSound("random.fizz", 50, 1f);
                mc.thePlayer.playSound("random.eat", 1000, 0f);
                mc.thePlayer.playSound("random.fizz", 1000, 0.5f);
                mc.thePlayer.playSound("mob.horse.donkey.angry", 1000, 0.5f);
            }
            show = true;
            delayUtil.reset();
            hideDelay.reset();
        }
    }

}