package vestige.module.impl.visual;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import vestige.Vestige;
import vestige.setting.impl.*;
import vestige.event.Listener;
import vestige.event.impl.PostMotionEvent;
import vestige.font.VestigeFontRenderer;
import vestige.module.*;
import vestige.util.animation.AnimationType;
import vestige.util.animation.AnimationHolder;
import vestige.util.animation.AnimationUtil;
import vestige.util.render.DrawUtil;
import vestige.util.render.FontUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class ModuleList extends HUDModule {

    private boolean initialised;

    private final ArrayList<AnimationHolder<Module>> modules = new ArrayList<>();

    private final ModeSetting mode = new ModeSetting("Mode", "Simple", "Simple", "New", "Outline", "Custom");

    private final ModeSetting font = FontUtil.getFontSetting(() -> mode.is("Custom"));

    private final EnumModeSetting<AnimationType> animType = AnimationUtil.getAnimationType(() -> mode.is("Custom"), AnimationType.POP);
    private final IntegerSetting animDuration = AnimationUtil.getAnimationDuration(() -> mode.is("Custom"), 250);

    private final DoubleSetting verticalSpacing = new DoubleSetting("Vertical spacing", () -> mode.is("Custom"), 10.5, 8, 20, 0.5);

    private final DoubleSetting extraWidth = new DoubleSetting("Extra width", () -> mode.is("Custom"), 0.5, 0, 6, 0.5);

    private final BooleanSetting box = new BooleanSetting("Box", () -> mode.is("Custom"), false);
    private final IntegerSetting boxAlpha = new IntegerSetting("Box alpha", () -> mode.is("Custom") && box.isEnabled(), 100, 5, 255, 5);

    private final BooleanSetting leftOutline = new BooleanSetting("Left outline", () -> mode.is("Custom"), false);
    private final BooleanSetting rightOutline = new BooleanSetting("Right outline", () -> mode.is("Custom"), false);
    private final BooleanSetting topOutline = new BooleanSetting("Top outline", () -> mode.is("Custom"), false);
    private final BooleanSetting bottomOutline = new BooleanSetting("Bottom outline", () -> mode.is("Custom"), false);

    private final EnumModeSetting<AlignType> alignMode = new EnumModeSetting<>("Align type", AlignType.RIGHT, AlignType.values());

    private VestigeFontRenderer productSans;

    private ClientTheme theme;

    public ModuleList() {
        super("Module List", Category.VISUAL, 5, 5, 100, 200, AlignType.RIGHT);
        this.addSettings(mode, font, animType, animDuration, verticalSpacing, box, extraWidth, boxAlpha, leftOutline, rightOutline, topOutline, bottomOutline, alignMode);
        this.listenType = EventListenType.MANUAL;
        this.startListening();
        this.setEnabledSilently(true);
    }

    @Override
    public void onClientStarted() {
        Vestige.instance.getModuleManager().modules.forEach(m -> modules.add(new AnimationHolder(m)));

        productSans = Vestige.instance.getFontManager().getProductSans();

        theme = Vestige.instance.getModuleManager().getModule(ClientTheme.class);
    }

    @Override
    protected void renderModule(boolean inChat) {
        if (!initialised) {
            sort();
            initialised = true;
        }

        alignType = alignMode.getMode();

        if(mc.gameSettings.showDebugInfo) return;

        switch (mode.getMode()) {
            case "Simple":
                renderSimple();
                break;
            case "New":
                renderNew();
                break;
            case "Outline":
                renderOutline();
                break;
            case "Custom":
                renderCustom();
                break;
        }
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {
        sort();
    }

    private void sort() {
        Collections.reverse(modules);

        modules.sort((m1, m2) -> (int) (Math.round((getStringWidth(m1.get().getName()) * 8) - Math.round(getStringWidth(m2.get().getName()) * 8))));

        Collections.reverse(modules);
    }

    private void renderSimple() {
        ScaledResolution sr = new ScaledResolution(mc);

        float x = (float) posX.getValue();
        float y = (float) posY.getValue();

        float offsetY = 10.5F;

        float width = 0;

        for (AnimationHolder<Module> holder : modules) {
            Module m = holder.get();
            String name = m.getName();

            float startX = alignMode.getMode() == AlignType.LEFT ? x : (float) (sr.getScaledWidth() - getStringWidth(name) - x);
            float startY = y;

            float endX = alignMode.getMode() == AlignType.LEFT ? (float) (x + getStringWidth(name)) : sr.getScaledWidth() - x;
            float endY = y + offsetY;

            if(Math.abs(endX - startX) > width) {
                width = Math.abs(endX - startX);
            }

            holder.setAnimType(AnimationType.POP2);
            holder.setAnimDuration(250);
            holder.updateState(m.isEnabled());
            if (!holder.isAnimDone() || holder.isRendered()) {
                holder.render(() -> drawStringWithShadow(name, startX, startY, getColor((int) (startY * -17))), startX, startY, endX, endY);
                y += offsetY * holder.getYMult();
            }
        }

        this.width = (int) (width) + 1;
        this.height = (int) (y - posY.getValue()) + 1;
    }

    private void renderNew() {
        ScaledResolution sr = new ScaledResolution(mc);

        float x = (float) posX.getValue();
        float y = (float) posY.getValue();

        float offsetY = 10.5F;

        float width = 0;

        for (AnimationHolder<Module> holder : modules) {
            Module m = holder.get();

            holder.setAnimType(AnimationType.SLIDE);
            holder.setAnimDuration(350);
            holder.updateState(m.isEnabled());

            if (!holder.isAnimDone() || holder.isRendered()) {
                String name = m.getName();
                double nameLength = getStringWidth(name);

                float mult = holder.getYMult();

                float startX = alignMode.getMode() == AlignType.LEFT ? x - 5 : (float) (sr.getScaledWidth() - nameLength - x - 5);
                float startY = y;

                float endX = alignMode.getMode() == AlignType.LEFT ? (float) (x + nameLength) : sr.getScaledWidth() - x;
                float endY = y + offsetY;

                if(Math.abs(endX - startX) > width) {
                    width = Math.abs(endX - startX);
                }

                holder.render(() -> {
                    Gui.drawRect(startX, startY, startX + 2, endY, getColor((int) (startY * -17)));
                    Gui.drawRect(startX + 2, startY, endX, endY, 0x70000000);
                    drawStringWithShadow(name, startX + 3.5F, startY + 1.5F, getColor((int) (startY * -17)));
                }, startX, startY, endX, startY + offsetY * mult);

                y += offsetY * Math.min(mult * 4, 1);
            }
        }

        this.width = (int) (width) + 1;
        this.height = (int) (y - posY.getValue()) + 1;
    }

    private void renderOutline() {
        ScaledResolution sr = new ScaledResolution(mc);

        float x = (float) posX.getValue();
        float y = (float) posY.getValue();

        float offsetY = 11F;

        float lastStartX = 0, lastEndX = 0;

        boolean firstModule = true;

        float width = 0;

        for (AnimationHolder<Module> holder : modules) {
            Module m = holder.get();
            String name = m.getName();

            holder.setAnimType(AnimationType.POP2);
            holder.setAnimDuration(250);
            holder.updateState(m.isEnabled());

            if (!holder.isAnimDone() || holder.isRendered()) {
                float startX = alignMode.getMode() == AlignType.LEFT ? x : (float) (sr.getScaledWidth() - getStringWidth(name) - x);
                float startY = y;

                float endX = alignMode.getMode() == AlignType.LEFT ? (float) (x + getStringWidth(name)) : sr.getScaledWidth() - x;
                float endY = y + offsetY;

                if(Math.abs(endX - startX) > width) {
                    width = Math.abs(endX - startX);
                }

                if (firstModule) {
                    DrawUtil.drawGradientVerticalRect(startX - 1.5, y - 2, endX + 1.5, y, 0, 0x50000000);
                } else {
                    double diff = (startX - 3.5) - (lastStartX - 1.5);

                    if (diff > 1) {
                        DrawUtil.drawGradientVerticalRect(lastStartX - 1.5, y, startX - 3, y + 2, 0x50000000, 0);
                    }
                }

                DrawUtil.drawGradientSideRect(endX + 1.5, startY, endX + 3.5, startY + offsetY * holder.getYMult(), 0x50000000, 0);

                holder.render(() -> {
                    DrawUtil.drawGradientSideRect(startX - 3.5, startY, startX - 1.5, endY, 0, 0x50000000);

                    drawStringWithShadow(name, startX, startY + 2, getColor((int) (startY * -17)));
                }, startX - 3.5F, startY, endX, endY);

                y += offsetY * holder.getYMult();

                lastStartX = startX;
                lastEndX = endX;

                firstModule = false;
            }
        }

        DrawUtil.drawGradientVerticalRect(lastStartX - 1.5, y, lastEndX + 1.5, y + 2, 0x50000000, 0);

        this.width = (int) (width) + 1;
        this.height = (int) (y - posY.getValue()) + 1;
    }

    private void renderCustom() {
        ScaledResolution sr = new ScaledResolution(mc);

        float x = (float) posX.getValue();
        float y = (float) posY.getValue();

        float offsetY = (float) verticalSpacing.getValue();
        double extraWidth = this.extraWidth.getValue();

        float width = 0;

        float space = offsetY - getFontHeight();

        boolean firstModule = true;

        float lastStartX = 0, lastEndX = 0;

        for (AnimationHolder<Module> holder : modules) {
            Module m = holder.get();
            String name = m.getName();

            holder.setAnimType(animType.getMode());
            holder.setAnimDuration(animDuration.getValue());
            holder.updateState(m.isEnabled());

            if (!holder.isAnimDone() || holder.isRendered()) {
                float startX = alignMode.getMode() == AlignType.LEFT ? x : (float) (sr.getScaledWidth() - getStringWidth(name) - x);
                float startY = y;

                float endX = alignMode.getMode() == AlignType.LEFT ? (float) (x + getStringWidth(name)) : sr.getScaledWidth() - x;

                if(Math.abs(endX - startX) > width) {
                    width = Math.abs(endX - startX);
                }

                float mult = holder.getYMult();

                if(leftOutline.isEnabled()) {
                    Gui.drawRect(startX - 2 - extraWidth, startY, startX - extraWidth, startY + offsetY * mult, getColor((int) (startY * -17)));
                }

                if(rightOutline.isEnabled()) {
                    Gui.drawRect(endX + extraWidth, startY, endX + extraWidth + 2, startY + offsetY * mult, getColor((int) (startY * -17)));
                }

                if(firstModule) {
                    if(topOutline.isEnabled()) {
                        double left = leftOutline.isEnabled() ? startX - extraWidth - 2 : startX - extraWidth;
                        double right = rightOutline.isEnabled() ? endX + extraWidth + 2 : endX + extraWidth;

                        Gui.drawRect(left, startY - 2, right, startY, getColor((int) (startY * -17)));
                    }
                } else {
                    if(bottomOutline.isEnabled()) {
                        double left = leftOutline.isEnabled() ? lastStartX - extraWidth - 2 : lastStartX - extraWidth;
                        double right = leftOutline.isEnabled() ? startX - extraWidth - 2 : startX - extraWidth;

                        Gui.drawRect(left, startY, right, startY + 2, getColor((int) (startY * -17)));
                    }
                }

                if(box.isEnabled()) {
                    Gui.drawRect(startX - extraWidth, startY, endX + extraWidth, startY + offsetY * mult, new Color(0, 0, 0, boxAlpha.getValue()).getRGB());
                }

                y += offsetY * mult;

                firstModule = false;
                lastStartX = startX;
                lastEndX = endX;
            }
        }

        if(bottomOutline.isEnabled() && !firstModule) {
            double left = leftOutline.isEnabled() ? lastStartX - extraWidth - 2 : lastStartX - extraWidth;
            double right = rightOutline.isEnabled() ? lastEndX + extraWidth + 2 : lastEndX + extraWidth;

            Gui.drawRect(left, y, right, y + 2, getColor((int) (y * -17)));
        }

        y = (float) posY.getValue();

        for (AnimationHolder<Module> holder : modules) {
            Module m = holder.get();
            String name = m.getName();

            if (!holder.isAnimDone() || holder.isRendered()) {
                float startX = alignMode.getMode() == AlignType.LEFT ? x : (float) (sr.getScaledWidth() - getStringWidth(name) - x);
                float startY = y;

                float endX = alignMode.getMode() == AlignType.LEFT ? (float) (x + getStringWidth(name)) : sr.getScaledWidth() - x;
                float endY = y + offsetY;

                float mult = holder.getYMult();

                float renderY = startY + Math.round(space + 1) / 2F;

                holder.render(() -> drawStringWithShadow(name, startX, renderY, getColor((int) (startY * -17))), startX, startY, endX, endY);
                y += offsetY * mult;
            }
        }

        this.width = (int) (width) + 1;
        this.height = (int) (y - posY.getValue()) + 1;
    }

    public void drawString(String text, float x, float y, int color) {
        switch (mode.getMode()) {
            case "Simple":
                mc.fontRendererObj.drawString(text, x, y, color);
                return;
            case "New":
            case "Outline":
                productSans.drawString(text, x, y, color);
                return;
            case "Custom":
                FontUtil.drawString(font.getMode(), text, x, y, color);
                break;
        }

        mc.fontRendererObj.drawString(text, x, y, color);
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        switch (mode.getMode()) {
            case "Simple":
                mc.fontRendererObj.drawStringWithShadow(text, x, y, color);
                break;
            case "New":
            case "Outline":
                productSans.drawStringWithShadow(text, x, y, color);
                break;
            case "Custom":
                FontUtil.drawStringWithShadow(font.getMode(), text, x, y, color);
                break;
        }
    }

    public double getStringWidth(String s) {
        switch (mode.getMode()) {
            case "Simple":
                return mc.fontRendererObj.getStringWidth(s);
            case "New":
            case "Outline":
                return productSans.getStringWidth(s);
            case "Custom":
                return FontUtil.getStringWidth(font.getMode(), s);
        }

        return mc.fontRendererObj.getStringWidth(s);
    }

    public int getFontHeight() {
        switch (mode.getMode()) {
            case "Simple":
                return mc.fontRendererObj.FONT_HEIGHT;
            case "New":
            case "Outline":
                return productSans.getHeight();
            case "Custom":
                return FontUtil.getFontHeight(font.getMode());
        }

        return mc.fontRendererObj.FONT_HEIGHT;
    }

    public int getColor(int offset) {
        return theme.getColor(offset);
    }

}