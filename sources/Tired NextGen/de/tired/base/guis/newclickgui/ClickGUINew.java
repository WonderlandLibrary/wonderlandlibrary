package de.tired.base.guis.newclickgui;

import de.tired.base.guis.newclickgui.renderables.RenderableCategory;
import de.tired.util.animation.Animation;
import de.tired.util.animation.Easings;
import de.tired.base.guis.CustomScreen;
import de.tired.base.guis.config.ConfigGUI;
import de.tired.util.file.FileUtil;
import de.tired.util.misc.KeyInputUtil;
import de.tired.util.render.StencilUtil;
import de.tired.util.math.TimerUtil;
import de.tired.util.misc.Trie;
import de.tired.base.font.CustomFont;
import de.tired.base.font.FontManager;
import de.tired.util.render.AnimationUtil;
import de.tired.util.render.ColorUtil;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.base.module.implementation.visual.Shader;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.ShaderRenderer;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import de.tired.Tired;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ClickGUINew extends CustomScreen {

    private int alpha = 0;

    private Animation circleAnimation = new Animation();

    private final KeyInputUtil keyInputUtil = new KeyInputUtil();

    private final Animation searchBarWidthAnimation = new Animation();

    private final TimerUtil timerUtil = new TimerUtil();

    private final Animation alphaBarAnimationExtended = new Animation();

    private boolean selectedAllModules = false;

    private final Animation barAnimation = new Animation();

    public final ArrayList<String> names = new ArrayList<>();

    private final ArrayList<RenderableCategory> renderableCategories = new ArrayList<>();

    private final Animation heightAnimation = new Animation();

    private final Animation scaleAnimation = new Animation();

    public ClickGUINew() {
        super(true);
        final AtomicInteger x = new AtomicInteger(-10);

        for (ModuleCategory category : ModuleCategory.values()) {
            renderableCategories.add(new RenderableCategory(x.addAndGet(130), 40, category));
        }
        for (Module m : Tired.INSTANCE.moduleManager.getModuleList()) {
            names.add(m.name.toLowerCase());
        }


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        alpha = (int) AnimationUtil.getAnimationState(alpha, 215, 900);

        circleAnimation.update();
        circleAnimation.animate(pressedEscape ? 0 : 122, .2, Easings.BACK_IN4);
        this.isAnimationDone = scaleAnimation.getValue() < -1;
        scaleAnimation.update();
        scaleAnimation.animate(pressedEscape ? -2 : .2, !pressedEscape ? .03 : .2, !pressedEscape ? Easings.BOUNCE_IN : Easings.BOUNCE_OUT);

        setAnimationDone(scaleAnimation.getValue() < -1);

        if (scaleAnimation.getValue() < -.1)
            mc.thePlayer.closeScreen();
        Color firstColor = ColorUtil.interpolateColorsBackAndForth(12, 0, new Color(84, 51, 158, alpha).brighter(), new Color(104, 127, 203, alpha), true);


        if (Shader.getInstance().clickGUIBlur.getValue())
            ShaderRenderer.stopBlur();


        if (scaleAnimation.getValue() < .01) return;
        float scaleAnim = scaleAnimation.getValue() + .8f;

        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2f, height / 2f, 0);
        GlStateManager.scale(scaleAnim, scaleAnim, 0);
        GlStateManager.translate(-(width / 2f), -(height / 2f), 0);

        for (RenderableCategory renderableCategory : renderableCategories) {
            renderableCategory.drawScreen(mouseX, mouseY, renderableCategory.x, renderableCategory.y);
            if (isHovering(mouseX, mouseY, renderableCategory.x, renderableCategory.y, renderableCategory.width, renderableCategory.height + renderableCategory.additionalHeight)) {
                Tired.INSTANCE.hoverCategory = renderableCategory.category;
            }
            renderableCategory.alphaAnimation.update();
            if (renderableCategory.category == Tired.INSTANCE.hoverCategory) {
                renderableCategory.scrollHandler.handleScroll();
                renderableCategory.alphaAnimation.animate(155, .1, Easings.NONE);
            } else {
                renderableCategory.alphaAnimation.animate(0, .1, Easings.NONE);
            }
        }
        GlStateManager.popMatrix();

        barAnimation.update();
        barAnimation.animate(!keyInputUtil.typedString.isEmpty() ? 10 : -30, .03, Easings.BOUNCE_IN);

        //  sectionRenderer.drawSections(-10, height - 20, mouseX, mouseY);


        if (Math.round(barAnimation.getValue()) >= -20) {
            final int rectWidth = 80;
            final int rectWidthBig = 100;

            final Trie trie = new Trie(names);

            searchBarWidthAnimation.update();

            searchBarWidthAnimation.animate(!selectedAllModules ? rectWidth : rectWidthBig, .3, Easings.NONE);

            final List<String> suggestedSort = trie.suggest(keyInputUtil.typedString.toLowerCase());


            final AtomicInteger height = new AtomicInteger(20);

            suggestedSort.forEach(height2 -> height.addAndGet(20));

            alphaBarAnimationExtended.update();
            alphaBarAnimationExtended.animate(!selectedAllModules ? 0 : 255, .15, Easings.NONE);

            heightAnimation.update();
            heightAnimation.animate(!selectedAllModules ? 20 : height.get(), .05, Easings.BOUNCE_IN);

            ShaderRenderer.startBlur();

            ShaderManager.shaderBy(RoundedRectShader.class).drawRound((width / 2) - searchBarWidthAnimation.getValue(), barAnimation.getValue(), searchBarWidthAnimation.getValue() * 2, (int) heightAnimation.getValue(), 3, new Color(20, 20, 20));

            ShaderRenderer.stopBlur();

            ShaderManager.shaderBy(RoundedRectShader.class).drawRound((width / 2) - searchBarWidthAnimation.getValue(), barAnimation.getValue(), searchBarWidthAnimation.getValue() * 2, (int) heightAnimation.getValue(), 3, new Color(19, 19, 19, 124));


            if (keyInputUtil.typedString.isEmpty())
                selectedAllModules = false;
            StencilUtil.initStencilToWrite();
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound((width / 2) - searchBarWidthAnimation.getValue(), barAnimation.getValue(), searchBarWidthAnimation.getValue() * 2, heightAnimation.getValue(), 3, new Color(19, 19, 19, 124));
            StencilUtil.readStencilBuffer(1);
            if (selectedAllModules) {
                final AtomicInteger yAdditional = new AtomicInteger(0);
                for (String suggest : suggestedSort) {

                    String name = suggest;
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    if (alphaBarAnimationExtended.getValue() > 90) {
                        ShaderManager.shaderBy(RoundedRectShader.class).drawRound((width / 2) - searchBarWidthAnimation.getValue() + 5, barAnimation.getValue() + 1 + yAdditional.addAndGet(20), (searchBarWidthAnimation.getValue() * 2) - 10, 15, 3, isHovering(mouseX, mouseY, (width / 2) - (int) searchBarWidthAnimation.getValue() + 5, 11 + yAdditional.get(), ((int) searchBarWidthAnimation.getValue() * 2) - 10, 15) ? new Color(40, 40, 40, (int) alphaBarAnimationExtended.getValue() - 89) : new Color(30, 30, 30, (int) alphaBarAnimationExtended.getValue() - 89));

                        FontManager.raleWay20.drawString(name, calculateMiddle(name, FontManager.raleWay20, (width / 2) - Math.round(searchBarWidthAnimation.getValue()) + 5, searchBarWidthAnimation.getValue() * 2), 17 + yAdditional.get(), new Color(255, 255, 255, (int) alphaBarAnimationExtended.getValue()).getRGB());
                    }
                }
                for (int i = 0; isHovering(mouseX, mouseY, (width / 2) + (int) searchBarWidthAnimation.getValue() - 21, (int) barAnimation.getValue() + 6, 20, 10) ? i < 3 : i < 2; i++) {
                    FontManager.iconFont.drawString("f", (width / 2) + (int) searchBarWidthAnimation.getValue() - 16, barAnimation.getValue() + 10, Integer.MAX_VALUE);
                }
            }
            StencilUtil.uninitStencilBuffer();

            FontManager.raleWay20.drawString(keyInputUtil.typedString.toLowerCase(), (width / 2) - searchBarWidthAnimation.getValue() + 4, barAnimation.getValue() + 8, -1);

            if (suggestedSort.size() < 1) return;
            if (suggestedSort.get(0) != null)
                FontManager.raleWay20.drawString(suggestedSort.get(0), (width / 2) - searchBarWidthAnimation.getValue() + 4, barAnimation.getValue() + 8, Integer.MAX_VALUE);

            for (int i = 0; isHovering(mouseX, mouseY, (width / 2) + (int) searchBarWidthAnimation.getValue() - 13, (int) barAnimation.getValue() + 6, 10, 10) ? i < 3 : i < 2; i++) {
                if (!selectedAllModules)
                    FontManager.iconFont.drawString("'", (width / 2) + searchBarWidthAnimation.getValue() - 13, barAnimation.getValue() + 9.5f, Integer.MAX_VALUE);
            }


        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean isHovering(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1)
            mc.displayGuiScreen(new ConfigGUI());
        super.actionPerformed(button);
    }


    public int calculateMiddle(String text, CustomFont fontRenderer, double x, double width) {
        return (int) ((float) (x + width) - (fontRenderer.getStringWidth(text) / 2f) - (float) width / 2);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        final int rectWidth = 80;
        final int rectWidthBig = 100;
        if (isHovering(mouseX, mouseY, (width / 2) + rectWidthBig - 21, 16, 20, 10)) {
            if (mouseButton == 0)
                this.selectedAllModules = false;
        }
        if (isHovering(mouseX, mouseY, (width / 2) + rectWidth - 13, (int) 16, 10, 10)) {
            if (mouseButton == 0) {
                this.selectedAllModules = !selectedAllModules;
            }
        }
        final Trie trie = new Trie(names);
        final List<String> suggestedSort = trie.suggest(keyInputUtil.typedString.toLowerCase());


        final AtomicInteger height = new AtomicInteger(20);

        suggestedSort.forEach(height2 -> height.addAndGet(20));

        if (!keyInputUtil.typedString.isEmpty()) {
            final AtomicInteger yAdditional = new AtomicInteger(0);
            for (String suggest : suggestedSort) {

                String name = suggest;
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                ShaderManager.shaderBy(RoundedRectShader.class).drawRound((width / 2) - rectWidthBig + 5, 11 + yAdditional.addAndGet(20), (rectWidthBig * 2) - 10, 15, 3, isHovering(mouseX, mouseY, (width / 2) - rectWidthBig + 5, 11 + yAdditional.get(), (rectWidthBig * 2) - 10, 15) ? new Color(40, 40, 40, 222) : new Color(30, 30, 30, 222));
                if (isHovering(mouseX, mouseY, (width / 2) - rectWidthBig + 5, 11 + yAdditional.get(), (rectWidthBig * 2) - 10, 15)) {
                    if (mouseButton == 0) {
                        final Module module = Tired.INSTANCE.moduleManager.moduleBy(suggest);
                        module.setState(!module.state);
                    }
                }
            }
        }

        for (RenderableCategory renderableCategory : renderableCategories)
            renderableCategory.onMouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (RenderableCategory renderableCategory : renderableCategories)
            renderableCategory.onMouseReleased(mouseX, mouseY);

        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(1, 4, height - 30, 70, 20, "Configs"));
        timerUtil.doReset();
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        this.alpha = 0;
        timerUtil.doReset();
        FileUtil.instance.save();
        scaleAnimation.setValue(0);
        circleAnimation.setValue(0);
        for (RenderableCategory renderableCategory : renderableCategories) {
            renderableCategory.onReset();
        }
        super.onGuiClosed();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        keyInputUtil.typing = true;
        keyInputUtil.registerInput(typedChar, keyCode);
        final Trie trie = new Trie(names);
        final List<String> suggestedSort = trie.suggest(keyInputUtil.typedString.toLowerCase());

        if (suggestedSort.size() < 1) return;
        if (suggestedSort.get(0) != null && !selectedAllModules && keyCode == Keyboard.KEY_RETURN) {
            final Module module = Tired.INSTANCE.moduleManager.moduleBy(suggestedSort.get(0));
            module.setState(!module.state);
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void setAnimationDone(boolean animationDone) {

    }

}
