package de.tired.base.guis.newclickgui.renderables;


import de.tired.base.guis.newclickgui.abstracts.ClickGUIHandler;
import de.tired.util.animation.Animation;
import de.tired.util.animation.Easings;
import de.tired.util.render.RenderUtil;
import de.tired.util.render.ScrollHandler;
import de.tired.util.render.StencilUtil;
import de.tired.base.font.FontManager;
import de.tired.util.render.ColorUtil;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectGradient;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import de.tired.Tired;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RenderableCategory extends ClickGUIHandler {

    public int x, y, dragX, dragY, mouseX, mouseY;

    private final ArrayList<RenderableModule> renderableModules = new ArrayList<>();

    private boolean dragging, isHovering;

    public int additionalHeight = 0;

    public ScrollHandler scrollHandler = new ScrollHandler();

    public final ModuleCategory category;

    private float scrollbarHeight = 0;

    private final Animation scrollPercentAnimation = new Animation();

    public Animation alphaAnimation = new Animation(), alphaAnimation2 = new Animation();

    /***
     * @param startX the first "X" position when the clickGUI is called.
     * @param startY the first "Y" position when the clickGUI is called.
     * @param category the given module category
     */

    public RenderableCategory(int startX, int startY, ModuleCategory category) {
        this.category = category;
        this.x = startX;
        this.y = startY;

        for (Module moduleFeature : Tired.INSTANCE.moduleManager.getModuleList()) {
            if (moduleFeature.getModuleCategory() != this.category)
                continue;
            renderableModules.add(new RenderableModule(moduleFeature));
        }

    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {
        for (final RenderableModule renderableModule : renderableModules) {
            renderableModule.onMouseReleased(mouseX, mouseY);
        }

        this.dragging = false;
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseKey) {
        for (final RenderableModule renderableModule : renderableModules)
            renderableModule.onMouseClicked(mouseX, mouseY, mouseKey);
        if (!isHovering) return;
        if (mouseKey == 0) {
            this.dragX = x - mouseX;
            this.dragY = y - mouseY;
            this.dragging = true;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int x, int y) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (this.dragging) {
            this.x = calculateMousePosition(mouseX, dragX);
            this.y = calculateMousePosition(mouseY, dragY);
        }

        this.isHovering = isHovered(mouseX, mouseY, x, y, width, height);


        RenderUtil.instance.doRenderShadow(Color.BLACK, x, y, width, MathHelper.clamp_double(height + additionalHeight - 16, 0, 320), 32);

        StencilUtil.initStencilToWrite();
        ShaderManager.shaderBy(RoundedRectGradient.class).drawRound(x, y - 4, width, height + additionalHeight < 300 ? height + additionalHeight - 13 : 320, 9, Color.BLACK, Color.BLACK);
        StencilUtil.readStencilBuffer(1);


        //Panel rectangle code Block
        {

            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x, y, width, height + additionalHeight - 16, 3, new Color(46, 46, 46));

            Gui.drawRect(x, y + 15, x + width, (y + 10) + 10, new Color(41, 41, 41, 255).getRGB());
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + .5f, y, width - 1, height, 3, new Color(41, 41, 41, 255));

            //    ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + 2, y + scrollHandler.getScroll() + 2, width - 4, height - 4, 3, new Color(31, 31, 31));

            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.resetColor();
        }

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + 3, y + 25, width - 6, additionalHeight - 26, 7, new Color(40, 40, 40));


        Color firstColor = ColorUtil.interpolateColorsBackAndForth(24, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
        Color secondColor = ColorUtil.interpolateColorsBackAndForth(43, 90, new Color(84, 51, 158), new Color(104, 127, 203).darker(), false);



        RenderUtil.instance.applyGradientHorizontal(calculateMiddle(category.displayName.toUpperCase(), FontManager.interSemiBold20, x, width), y, 120, 30, 1, firstColor, secondColor, () -> FontManager.interSemiBold20.drawString(category.displayName.toUpperCase(), calculateMiddle(category.displayName.toUpperCase(), FontManager.interSemiBold20, x, width), y + 8, -1));


        StencilUtil.uninitStencilBuffer();
        //Module rendering codeBlock
        StencilUtil.initStencilToWrite();
        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + 3, y + 25, width - 6, height + additionalHeight < 290 ? height + additionalHeight - 13 : 290, 7, new Color(40, 40, 40));
        StencilUtil.readStencilBuffer(1);

        {
            final AtomicInteger yAdditional = new AtomicInteger((int) (height + 9 + scrollHandler.getScroll()));


            for (final RenderableModule renderableModule : renderableModules) {
                renderableModule.extendAnimation.update();
                renderableModule.extendAnimation.animate(renderableModule.yAxisAdditional, .2, Easings.NONE);
                renderableModule.drawScreen(mouseX, mouseY, x, (int) ((int) (y + 2 + yAdditional.get())), scrollHandler);
                yAdditional.addAndGet((int) ((int) (renderableModule.height + Math.round(renderableModule.extendAnimation.getValue()) + 8)));
            }

            final int realHeight = height + additionalHeight < 290 ? height + additionalHeight - 13 : 290;

            scrollbarHeight = ((realHeight) / (renderableModules.size())) + (renderableModules.size());
            float hiddenHeight = (yAdditional.get() - scrollHandler.getScroll()) - 320;
            scrollHandler.setMaxScroll(Math.max(0, hiddenHeight));
            additionalHeight = yAdditional.get();


        }
        StencilUtil.uninitStencilBuffer();

        final double scrollPercent = Math.abs((scrollHandler.getScroll() / scrollHandler.maxScroll));
        final int realHeight = height + additionalHeight < 290 ? height + additionalHeight - 13 : 290;

        scrollPercentAnimation.update();
        scrollPercentAnimation.animate(scrollPercent, .2, Easings.NONE);
        final float scrollamount = (realHeight - scrollbarHeight) * scrollPercentAnimation.getValue();




        if (alphaAnimation.getValue() > 22 && additionalHeight > 290) {

            RenderUtil.instance.doRenderShadow(Color.BLACK, x + width + 14, y + 20, 4, realHeight, 22);

            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + width + 14, y + 20, 4, realHeight, 2, new Color(10, 10, 10, (int) alphaAnimation.getAnimationFromValue()));
            RenderUtil.instance.doRenderShadow(Color.BLACK, x + width + 14, y + 20 + scrollamount, 4, scrollbarHeight, 22);
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + width + 14, y + 20 + scrollamount, 4, scrollbarHeight, 1.5f, new Color(255, 255, 255, (int) alphaAnimation.getAnimationFromValue()));
        }
    }

    public void onReset() {
        for (final RenderableModule renderableModule : renderableModules)
            renderableModule.onReset();
    }
}
