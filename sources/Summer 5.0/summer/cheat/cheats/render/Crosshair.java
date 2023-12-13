package summer.cheat.cheats.render;

import java.awt.Color;

import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.render.EventRender2D;
import summer.base.utilities.Render2DUtil;
import summer.base.manager.config.Cheats;

public class Crosshair extends Cheats {

    private final Setting dynamic;
    private final Setting colorRed;
    private final Setting colorGreen;
    private final Setting colorBlue;
    private final Setting width;
    private final Setting gap;
    private final Setting length;
    private final Setting dynamicGap;
    private final Setting rainbowCrosshair;

    public Crosshair() {
        super("Crosshair", "Draws a crosshair in the middle of the screen", Selection.RENDER);
        Summer.INSTANCE.settingsManager.Property(colorRed = new Setting("Red", this, 249, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(colorGreen = new Setting("Green", this, 255, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(colorBlue = new Setting("Blue", this, 0, 0, 255, true));
        Summer.INSTANCE.settingsManager.Property(width = new Setting("Width", this, 1.0, 0.5, 8.0, true));
        Summer.INSTANCE.settingsManager.Property(gap = new Setting("Gap", this, 2.0, 0.5, 10.0, true));
        Summer.INSTANCE.settingsManager.Property(length = new Setting("Length", this, 3.0, 0.5, 30.0, true));
        Summer.INSTANCE.settingsManager.Property(dynamic = new Setting("Dynamic", this, false));
        Summer.INSTANCE.settingsManager.Property(dynamicGap = new Setting("Dynamic Gap", this, 3.0, 1.0, 20.0, true, dynamic::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(rainbowCrosshair = new Setting("Rainbow Crosshair", this, false));
    }

    @EventTarget
    public void onEvent(EventRender2D event) {
        int color = new Color(this.colorRed.getValFloat() / 255F, this.colorGreen.getValFloat() / 255F, this.colorBlue.getValFloat() / 255F).getRGB();
        int screenWidth = ((EventRender2D) event).getWidth();
        int screenHeight = event.getHeight();
        int wMiddle = screenWidth / 2;
        int hMiddle = screenHeight / 2;
        boolean dyn = dynamic.getValBoolean();
        double dyngap = dynamicGap.getValDouble();
        double wid = width.getValDouble();
        double len = length.getValDouble();
        boolean wider = dyn && mc.thePlayer.isMoving();
        double gaps = wider ? dyngap : gap.getValDouble();

        if (rainbowCrosshair.getValBoolean()) {
            // Left
            Render2DUtil.drawBorderedRect(wMiddle - gaps - len, hMiddle - (wid / 2.0), wMiddle - gaps,
                    hMiddle + (wid / 2.0), 0.5f, Color.black.getRGB(), Chams.getRainbow(6000, -15), false);
            // Right
            Render2DUtil.drawBorderedRect(wMiddle + gaps, hMiddle - (wid / 2.0), wMiddle + gaps + len,
                    hMiddle + (wid / 2.0), 0.5f, Color.black.getRGB(), Chams.getRainbow(6000, -15), false);
            // Top
            Render2DUtil.drawBorderedRect(wMiddle - (wid / 2.0), hMiddle - gaps - len, wMiddle + (wid / 2.0),
                    hMiddle - gaps, 0.5f, Color.black.getRGB(), Chams.getRainbow(6000, -15), false);
            // Bottom
            Render2DUtil.drawBorderedRect(wMiddle - (wid / 2.0), hMiddle + gaps, wMiddle + (wid / 2.0),
                    hMiddle + gaps + len, 0.5f, Color.black.getRGB(), Chams.getRainbow(6000, -15), false);
        } else {
            // Left
            Render2DUtil.drawBorderedRect(wMiddle - gaps - len, hMiddle - (wid / 2.0), wMiddle - gaps,
                    hMiddle + (wid / 2.0), 0.5f, Color.black.getRGB(), color, false);
            // Right
            Render2DUtil.drawBorderedRect(wMiddle + gaps, hMiddle - (wid / 2.0), wMiddle + gaps + len,
                    hMiddle + (wid / 2.0), 0.5f, Color.black.getRGB(), color, false);
            // Top
            Render2DUtil.drawBorderedRect(wMiddle - (wid / 2.0), hMiddle - gaps - len, wMiddle + (wid / 2.0),
                    hMiddle - gaps, 0.5f, Color.black.getRGB(), color, false);
            // Bottom
            Render2DUtil.drawBorderedRect(wMiddle - (wid / 2.0), hMiddle + gaps, wMiddle + (wid / 2.0),
                    hMiddle + gaps + len, 0.5f, Color.black.getRGB(), color, false);
        }
    }
}
