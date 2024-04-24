package Method.Client.module.Onscreen;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.utils.font.CFontRenderer;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.text.DecimalFormat;

import static Method.Client.clickgui.component.Component.FontRend;

public class PinableFrame {

    private final int width;
    public int defaultWidth;
    public int y;
    public int x;
    public int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    private boolean isPinned = false;
    public CFontRenderer FontRender;
    public String title;

    public String[] text;
    protected Minecraft mc = Minecraft.getMinecraft();

    public PinableFrame(String title, String[] text, int y, int x) {
        FontRender = new CFontRenderer(new Font("Impact", Font.PLAIN, 18), true, false);
        this.title = title;
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = 88;
        this.defaultWidth = 88;
        this.barHeight = 13;
        this.dragX = 0;
        this.isDragging = false;
    }

    public static void Toggle(PinableFrame pin, boolean isPinned) {
        if (isPinned)
            Main.eventBus.register(pin);
        else
            Main.eventBus.unregister(pin);
        pin.setPinned(isPinned);
    }

    protected DecimalFormat getDecimalFormat(int i) {
        switch (i) {
            case 0:
                return new DecimalFormat("0");
            case 1:
                return new DecimalFormat("0.0");
            case 2:
                return new DecimalFormat("0.00");
            case 3:
                return new DecimalFormat("0.000");
            case 4:
                return new DecimalFormat("0.0000");
            case 5:
                return new DecimalFormat("0.00000");
            default:
                return null;
        }
    }

    protected void getSetup(PinableFrame pinableFrame, Setting xpos, Setting ypos, Setting Frame, Setting FontSize) {
        pinableFrame.x = (int) xpos.getValDouble();
        pinableFrame.y = (int) ypos.getValDouble();
        if (Frame.getValString().equalsIgnoreCase("false") || Frame.getValString() == null)
            Frame.setValString("Times");
        pinableFrame.FontRender.setFontS(Frame.getValString(), FontSize.getValDouble(), this.FontRender);
    }

    protected void getInit(PinableFrame pinableFrame, Setting xpos, Setting ypos, Setting Frame, Setting FontSize) {
        if (pinableFrame.FontRender.getSize() != (int) FontSize.getValDouble() || !pinableFrame.FontRender.getFont().getName().equalsIgnoreCase(Frame.getValString()))
            pinableFrame.FontRender.setFontS(Frame.getValString(), FontSize.getValDouble(), pinableFrame.FontRender);
        if (!pinableFrame.getDrag()) {
            pinableFrame.x = (int) xpos.getValDouble();
            pinableFrame.y = (int) ypos.getValDouble();
        } else {
            xpos.setValDouble(pinableFrame.x);
            ypos.setValDouble(pinableFrame.y);
        }
    }

    protected void fontSelect(Setting s, String name, float v, float v1, int color, boolean shadow) {
        if (s.getValString().equalsIgnoreCase("MC")) {
            if (shadow)
                mc.fontRenderer.drawStringWithShadow(name, (int) v, (int) v1, color);
            if (!shadow)
                mc.fontRenderer.drawString(name, (int) v, (int) v1, color);
        } else {
            if (shadow)
                this.FontRender.drawStringWithShadow(name, v, v1, color);
            if (!shadow)
                this.FontRender.String(name, (int) v, (int) v1, color);
        }
    }

    protected int widthcal(Setting s, String name) {
        if (s.getValString().equalsIgnoreCase("MC"))
            return mc.fontRenderer.getStringWidth(name);
        return this.FontRender.getStringWidth(name);
    }

    protected int heightcal(Setting s, String name) {
        if (s.getValString().equalsIgnoreCase("MC"))
            return 10;  // There is no mc String Height function as it does not change unless you Stretch it
        return this.FontRender.getStringHeight(name);
    }

    public void renderFrame() {
        if (this.isPinned)
            FontRend.drawStringWithShadow(title, this.x + 3, this.y + 3, 0xFFFFFFFF);
    }

    public void scale(boolean up) {
        if (up)
            GlStateManager.scale(RenderUtils.simpleScale(false), RenderUtils.simpleScale(false), 0.5f);
        else
            GlStateManager.scale(1f, 1f, 0.5f);
    }

    public void Ongui() {
    }

    public void renderFrameText() {
        int yCount = this.y + this.barHeight + 3;
        for (String line : text) {
            FontRend.drawString(line, this.x + 3, yCount, -1);
            yCount += 10;
        }

    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging && this.isPinned) {
            this.setX(mouseX - dragX);
            this.setY(mouseY - dragY);
        }
    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }

    public boolean isWithinExtendRange(int x, int y) {
        return x <= this.x + this.width - 2 && x >= this.x + this.width - 2 - 8 && y >= this.y + 2 && y <= this.y + 10;
    }


    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    public Boolean getDrag() {
        return this.isDragging;
    }

    public int getX() {
        return x;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public int getY() {
        return y;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public int getWidth() {
        return width;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public String getTitle() {
        return title;
    }

    public void setPinned(boolean pinned) {
        this.isPinned = pinned;
    }

    public void setup() {
    }

}
