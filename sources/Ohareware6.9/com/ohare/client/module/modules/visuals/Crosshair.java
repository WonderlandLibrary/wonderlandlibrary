package com.ohare.client.module.modules.visuals;

import com.ohare.client.Client;
import com.ohare.client.event.events.render.CrosshairEvent;
import com.ohare.client.event.events.render.Render2DEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.RenderUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.NumberValue;
import dorkbox.messageBus.annotations.Subscribe;

import java.awt.*;

/**
 * made by Xen for OhareWare
 *
 * @since 6/11/2019
 **/
public class Crosshair extends Module {
    private BooleanValue dynamic = new BooleanValue("Dynamic", true);
    private NumberValue<Double> width = new NumberValue("Width", 1.0D, 0.5D, 10.0D, 0.5D);
    private NumberValue<Double> gap = new NumberValue("Gap", 3.0D, 0.5D, 10.0D, 0.5D);
    private NumberValue<Double> length = new NumberValue("Length", 7.0D, 0.5D, 100.0D, 0.5D);
    private NumberValue<Double> dynamicgap = new NumberValue("DynamicGap", 1.5D, 0.5D, 10.0D, 0.5D);
    private NumberValue<Integer> red = new NumberValue("Red", 255, 0, 255, 1);
    private NumberValue<Integer> green = new NumberValue("Green", 255, 0, 255, 1);
    private NumberValue<Integer> blue = new NumberValue("Blue", 255, 0, 255, 1);
    private NumberValue<Integer> alpha = new NumberValue("Alpha", 255, 0, 255, 1);
    private BooleanValue rainbow = new BooleanValue("Raindow", false);
    public BooleanValue staticRainbow = new BooleanValue("Static Rainbow", false);

    public Crosshair() {
        super("Crosshair", Category.VISUALS, 0);
        setDescription("Pretty obvious");
        addValues(staticRainbow, rainbow, dynamic, red, green, blue, alpha, width, gap, dynamicgap, length);
        setHidden(true);
    }

    @Subscribe
    public void onCrosshair(CrosshairEvent event) {
        event.setCanceled(true);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModule("hud");
        Color rai = new Color(RenderUtil.getRainbow(6000, -15));
        int color = staticRainbow.isEnabled() ? color(2, 100) : (rainbow.isEnabled() ? new Color(rai.getRed(), rai.getGreen(), rai.getBlue(), alpha.getValue()).getRGB() : new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()).getRGB());

        final double middlex = event.getSR().getScaledWidth() / 2;
        final double middley = event.getSR().getScaledHeight() / 2;
        // top box
        RenderUtil.drawBordered(middlex - (width.getValue()), middley - (gap.getValue() + length.getValue()) - ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middlex + (width.getValue()), middley - (gap.getValue()) - ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), 0.5, color, 0xff000000);
        // bottom box
        RenderUtil.drawBordered(middlex - (width.getValue()), middley + (gap.getValue()) + ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middlex + (width.getValue()), middley + (gap.getValue() + length.getValue()) + ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), 0.5, color, 0xff000000);
        // left box
        RenderUtil.drawBordered(middlex - (gap.getValue() + length.getValue()) - ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley - (width.getValue()), middlex - (gap.getValue()) - ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley + (width.getValue()), 0.5, color, 0xff000000);
        // right box
        RenderUtil.drawBordered(middlex + (gap.getValue()) + ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley - (width.getValue()), middlex + (gap.getValue() + length.getValue()) + ((mc.thePlayer.isMoving() && dynamic.isEnabled()) ? dynamicgap.getValue() : 0), middley + (width.getValue()), 0.5, color, 0xff000000);
    }
    public int color(int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(red.getValue(),green.getValue(),blue.getValue(), hsb);

        float brightness = Math.abs(((getOffset() + (index / (float) count) * 2) % 2) - 1);
        brightness = 0.4f + (0.4f * brightness);

        hsb[2] = brightness % 1f;
        Color clr = new Color(Color.HSBtoRGB(hsb[0],hsb[1], hsb[2]));
        return new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha.getValue()).getRGB();
    }

    private float getOffset() {
        return (System.currentTimeMillis() % 2000) / 1000f;
    }
}
