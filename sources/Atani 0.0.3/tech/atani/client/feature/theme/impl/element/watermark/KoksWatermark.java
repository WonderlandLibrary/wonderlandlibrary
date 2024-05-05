package tech.atani.client.feature.theme.impl.element.watermark;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.impl.hud.WaterMark;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.feature.theme.impl.element.DraggableElement;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.RenderUtil;

import java.awt.*;

@ThemeObjectInfo(name = "Koks", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.WATERMARK)
public class KoksWatermark extends DraggableElement {

    public KoksWatermark() {
        super(8, 7, 83, 30, null, WaterMark.class);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY) {
        final String version = "v" + CLIENT_VERSION;

        FontRenderer roboto18 = FontStorage.getInstance().findFont("Roboto", 18);
        FontRenderer raleway30 = FontStorage.getInstance().findFont("Raleway Regular", 30);
        FontRenderer raleway35 = FontStorage.getInstance().findFont("Raleway Regular", 35);

        RenderUtil.startScissorBox();
        RenderUtil.drawScissorBox(getPosX().getValue(), 3 + getPosY().getValue(), 100, 16f);
        raleway35.drawStringWithShadow(String.valueOf(CLIENT_NAME.charAt(0)), 2 + getPosX().getValue(), 3 + getPosY().getValue(), getRainbow(0, 3000, 0.6f, 1).getRGB());
        raleway30.drawStringWithShadow(CLIENT_NAME.substring(1) + "sense", 2 + getPosX().getValue() + raleway35.getStringWidthInt(String.valueOf(CLIENT_NAME.charAt(0))), 5 + getPosY().getValue(), -1);
        RenderUtil.endScissorBox();

        for (int i = 0; i < version.length(); i++) {
            roboto18.drawStringWithShadow(String.valueOf(version.charAt(i)), 2 + getPosX().getValue() + raleway35.getStringWidthInt(String.valueOf(CLIENT_NAME.charAt(0))) + raleway30.getStringWidthInt(CLIENT_NAME.substring(1) + "sense") + roboto18.getStringWidthInt(version.substring(0, i)) - roboto18.getStringWidthInt(version), 1 + getPosY().getValue(), getRainbow(100 * (i + 1), 3000, 0.6f, 1).getRGB());
        }

        final double motionX = getPlayer().posX - getPlayer().prevPosX;
        final double motionZ = getPlayer().posZ - getPlayer().prevPosZ;
        double speed = Math.sqrt(motionX * motionX + motionZ * motionZ) * 20 * getTimer().timerSpeed;
        speed = Math.round(speed * 10);
        speed = speed / 10;

        String bps = "Bps: ";


        for (int i = 0; i < bps.length(); i++) {
            final char character = bps.charAt(i);
            roboto18.drawStringWithShadow(character + "", 3 + getPosX().getValue() + roboto18.getStringWidthInt(bps.substring(0, i)), 21 + getPosY().getValue(), getRainbow(100 * (i + 1), 3000, 0.6f, 1).getRGB());
        }

        roboto18.drawStringWithShadow(speed + "", 3 + getPosX().getValue() + roboto18.getStringWidthInt(bps), 21 + getPosY().getValue(), new Color(-1).getRGB());

        if(this.getLocked().getValue())
            leftY.set(50);
    }

    // Yea I skidded the method to get the exact same rainbow, SUE ME
    public Color getRainbow(int offset, int speed, float saturation, float brightness) {
        float hue = ((System.currentTimeMillis() + offset) % speed) / (float) speed;
        return Color.getHSBColor(hue, saturation, brightness);
    }
    
}
