package today.flux.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import today.flux.Flux;
import today.flux.gui.clickgui.classic.BlurBuffer;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.Render.Hud;
import today.flux.utility.AnimationTimer;
import today.flux.utility.DelayTimer;

import java.awt.*;

public class TabGuiRenderer {
    private float index;
    private DelayTimer delayTimer = new DelayTimer();

    private float x;
    private float y;
    private float width;
    private float height;

    private int categoryIndex;
    private int moduleIndex;
    private float renderCategoryIndex;
    private float renderModuleIndex;

    private Section section = Section.Category;

    private DelayTimer animationTimer = new DelayTimer();

    private AnimationTimer tabAnimation = new AnimationTimer(8);

    private final float EXPAND_BOX = 15f;

    public TabGuiRenderer() {
        this.x = 3;
        this.y = 3;
        this.width = this.getMaxCategoryWidth() + EXPAND_BOX;
        this.height = this.getTotalCategoryHeight(2f) + 2f;
    }

    public void render() {
        if (index > 1.0f)
            index = 0.0f;

        index += 0.001f * (delayTimer.getPassed() * 0.4f);
        this.x = (!Hud.waterMarkMode.isCurrentMode("None") ? Hud.waterMarkMode.isCurrentMode("Flux Sense") ? 5 : 4 : 4);
        this.y = 3 + (!Hud.waterMarkMode.isCurrentMode("None") ? Hud.waterMarkMode.isCurrentMode("Flux Sense") ? 20 : 15 : 0);

        this.drawBackGround();
        this.drawCategories();

        //render modules (with crop)
        if (this.drawModulesBackGround()) //including crop
            this.drawModules();
        GuiRenderUtils.endCrop();

        tabAnimation.update(section == Section.Module);

        //increment for animation
        if (this.categoryIndex != this.renderCategoryIndex && this.animationTimer.hasPassed(10)) {
            this.renderCategoryIndex += this.renderCategoryIndex < this.categoryIndex ?
                    Math.min(0.15f, this.categoryIndex - this.renderCategoryIndex) : Math.max(-0.15f, this.categoryIndex - this.renderCategoryIndex);
            this.animationTimer.reset();
        }

        if (this.moduleIndex != this.renderModuleIndex && this.animationTimer.hasPassed(10)) {
            this.renderModuleIndex += this.renderModuleIndex < this.moduleIndex ?
                    Math.min(0.15f, this.moduleIndex - this.renderModuleIndex) : Math.max(-0.15f, this.moduleIndex - this.renderModuleIndex);
            this.animationTimer.reset();
        }

        this.onTick();
    }

    private void drawBackGround() {
        if (OpenGlHelper.shadersSupported && Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && !Hud.Theme.isCurrentMode("Nostalgia")) {
            if (!Hud.NoShader.getValue()) {
                BlurBuffer.blurArea((int) x, (int) y, (int) width, (int) height - 12, true);
            }
        }

        if (!Hud.Theme.isCurrentMode("Nostalgia")) {
            GuiRenderUtils.drawRect(x, y, width, height - 12, (Hud.isLightMode ? new Color(255, 255, 255, 200) : new Color(0, 0, 0, 150)));
        } else {
            GuiRenderUtils.drawBorderedRect(x - 2, y + 22, width + 25, height - 19, 1, new Color(30, 30, 30, 255), new Color(0, 0, 0, 255));
        }
    }

    private void drawCategories() {
         float x = this.x + 1;
         float y = this.y + 1;

        if (Hud.Theme.isCurrentMode("Nostalgia")) {
             x = this.x + 0;
             y = this.y + 25;
        }


        String selected = Category.values()[categoryIndex].name();

        //current
        if(!Hud.Theme.isCurrentMode("Nostalgia")) {
            GuiRenderUtils.drawRect(x, y + (FontManager.normal2.getHeight(selected) + 2f) * renderCategoryIndex + 1f, 1.5f, FontManager.normal2.getHeight(selected) - 1f, !Hud.isLightMode ? new Color(206, 89, 255,255) : new Color(0xff1DA0FF));
        } else {
            GuiRenderUtils.drawRect(x - 1, y - 2 + (FontManager.normal2.getHeight(selected) + 1) * renderCategoryIndex, width + 23, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2, Color.MAGENTA);
        }

        //all
        final float textX = x + 3;
        float textY = y + 1;

        for (Category category : Category.values()) {
            if (category == Category.Global) continue;
            if(!Hud.Theme.isCurrentMode("Nostalgia")) {
                FontManager.normal2.drawString(category.name(), textX, textY, !Hud.isLightMode ? new Color(185, 185, 185, 255).getRGB() : 0xff727371);
                textY += FontManager.normal2.getHeight(category.name()) + 2f;
            } else {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(category.name(), textX - 1, textY - 1, new Color(240, 240, 240, 255).getRGB());
                textY += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
            }
        }
        //white text selected
        if(!Hud.Theme.isCurrentMode("Nostalgia")) {
            GuiRenderUtils.beginCropFixed(x, y + (FontManager.normal2.getHeight(selected) + 2f) * (renderCategoryIndex + 1), width - 2.0f, FontManager.normal2.getHeight(selected) + 2f);
        }

        textY = y + 1;
        for (Category category : Category.values()) {
            if (category == Category.Global) continue;
            if (!Hud.Theme.isCurrentMode("Nostalgia")) {
                FontManager.normal2.drawString(category.name(), textX, textY, !Hud.isLightMode ? new Color(255, 255, 255, 255).getRGB() : 0xff1DA0FF);
                textY += FontManager.normal2.getHeight(category.name()) + 2f;
            }
        }

        GuiRenderUtils.endCrop();
    }

    private static float moduleScreenSpace = 2f;

    private boolean drawModulesBackGround() {
        float width = this.getMaxModulesWidth(this.getCurrentCategory()) + EXPAND_BOX;
        if ((int) (width * tabAnimation.getValue()) == 0)
            return false;
        float height = this.getTotalModuleHeight(2f,
 this.getCurrentCategory()) + 2f;
        final float x = this.x + moduleScreenSpace + this.width;
        final float y = this.y + (FontManager.normal2.getHeight(this.getCurrentCategory().name()) + 2f) * categoryIndex;
        if (OpenGlHelper.shadersSupported && Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer && Hud.Theme.isCurrentMode("Nostalgia")) {
            if (!Hud.NoShader.getValue()) {
                BlurBuffer.blurArea((int) x, (int) y, (int) (width * tabAnimation.getValue()), (int) height, true);
            }
        }
        GuiRenderUtils.beginCrop(x, y + height, (float) (width * tabAnimation.getValue()), height, 2f);
        if(!Hud.Theme.isCurrentMode("Nostalgia")) {
            GuiRenderUtils.drawRect(x, y, width, height, (Hud.isLightMode ? new Color(255, 255, 255, 200) : new Color(0, 0, 0, 150)));
        } else {
            GuiRenderUtils.drawBorderedRect(x + 22, y + 22, width, height, 1, new Color(30, 30, 30, 255), new Color(0, 0, 0, 255));
        }
        return true;
    }

    private void drawModules() {
        final float x = this.x + 1 + this.width + moduleScreenSpace;
        final float y = this.y + 1 + (FontManager.normal2.getHeight(this.getCurrentCategory().name()) + 2f) * categoryIndex;

        java.util.List<Module> moduleList = Flux.INSTANCE.getModuleManager().getModulesByCategory(this.getCurrentCategory());
        Module selected = moduleList.get(this.moduleIndex);

        //current
        if (!Hud.Theme.isCurrentMode("Nostalgia")) {
            GuiRenderUtils.drawRect(x, y + (FontManager.normal2.getHeight(selected.getName()) + 2f) * renderModuleIndex + 1f, 1.5f, FontManager.normal2.getHeight(selected.getName()) - 1f, (Hud.isLightMode ? new Color(0xff4286f5) : new Color(206, 89, 255,255)));
        } else {
            GuiRenderUtils.drawRect(x + 22, y + 22 + (FontManager.normal2.getHeight(selected.getName()) + 1) * renderModuleIndex, width + EXPAND_BOX + this.getMaxModulesWidth(this.getCurrentCategory()) - 53, Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2f, Color.MAGENTA);
        }

        //all
        final float textX = x + 4;
        float textY = y + 1;

        for (Module module : moduleList) {
            if (!Hud.Theme.isCurrentMode("Nostalgia")) {
                FontManager.normal2.drawString(module.getName(), textX, textY, module.isEnabled() ? new Color(255, 255, 255, 255).getRGB() : new Color(185, 185, 185, 255).getRGB());
                textY += FontManager.normal.getHeight(module.getName()) + 2f;
            } else {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(module.getName(), textX + 22, textY + 24, module.isEnabled() ? new Color(255, 255, 255, 255).getRGB() : new Color(185, 185, 185, 255).getRGB());
                textY += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2f;
            }
        }
    }

    private final DelayTimer downKeyPressTime = new DelayTimer();
    private final DelayTimer upKeyPressTime = new DelayTimer();

    public void onTick() {
        if (!Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            this.downKeyPressTime.reset();

        if (!Keyboard.isKeyDown(Keyboard.KEY_UP))
            this.upKeyPressTime.reset();

        if (this.downKeyPressTime.hasPassed(500)) {
            this.keyPress(Keyboard.KEY_DOWN);
            this.downKeyPressTime.reset(410);
        }

        if (this.upKeyPressTime.hasPassed(500)) {
            this.keyPress(Keyboard.KEY_UP);
            this.upKeyPressTime.reset(410);
        }
    }

    public void keyPress(int key) {
        if (!Hud.tabgui.getValue())
            return;

        if (section == Section.Category) {
            if (key == Keyboard.KEY_DOWN && this.categoryIndex < Category.values().length - 2) {
                this.categoryIndex++;
            }

            if (key == Keyboard.KEY_UP && this.categoryIndex > 0) {
                this.categoryIndex--;
            }

            if (key == Keyboard.KEY_RIGHT) {
                section = Section.Module;
                this.moduleIndex = 0;
                this.renderModuleIndex = 0;
                return;
            }
        }

        if (section == Section.Module) {
            if (key == Keyboard.KEY_DOWN && this.moduleIndex < Flux.INSTANCE.getModuleManager().getModulesByCategory(this.getCurrentCategory()).size() - 1) {
                this.moduleIndex++;
            }

            if (key == Keyboard.KEY_UP && this.moduleIndex > 0) {
                this.moduleIndex--;
            }

            if (key == Keyboard.KEY_LEFT) {
                section = Section.Category;
                this.moduleIndex = 0;
                return;
            }

            if (key == Keyboard.KEY_RIGHT) {
                this.getCurrentModule().toggle();
            }
        }
    }

    //utils
    private Category getCurrentCategory() {
        return Category.values()[categoryIndex];
    }

    private Module getCurrentModule() {
        return Flux.INSTANCE.getModuleManager().getModulesByCategory(this.getCurrentCategory()).get(moduleIndex);
    }

    private float getMaxModulesWidth(Category category) {
        float result = 0;
        for (Module module : Flux.INSTANCE.getModuleManager().getModulesByCategory(category))
            if(!Hud.Theme.isCurrentMode("Nostalgia")) {
                result = Math.max(result, FontManager.normal2.getStringWidth(module.getName()));
            } else {
                result = Math.max(result, Minecraft.getMinecraft().fontRendererObj.getStringWidth(module.getName()));
            }

        return result;
    }

    private float getTotalModuleHeight(final float margin, Category category) {
        float result = 0;
        for (Module module : Flux.INSTANCE.getModuleManager().getModulesByCategory(category))
            if(!Hud.Theme.isCurrentMode("Nostalgia")) {
                result += FontManager.normal2.getHeight(module.getName()) + margin;
            } else {
                result += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + margin;
            }

        return result;
    }

    private float getMaxCategoryWidth() {
        float result = 0;
        for (Category category : Category.values())
            if(Hud.Theme.isCurrentMode("Nostalgia")) {
                result = Math.max(result, FontManager.normal2.getStringWidth(category.name()));
            } else {
                result = Math.max(result, Minecraft.getMinecraft().fontRendererObj.getStringWidth(category.name()));
            }

        return result;
    }

    private float getTotalCategoryHeight(final float margin) {
        float result = 0;
        for (Category category : Category.values())

                if (!Hud.Theme.isCurrentMode("Nostalgia")) {
                    result += FontManager.normal2.getHeight(category.name()) + margin;
                } else {
                    result += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + margin;
                }

        return result;
    }
}

enum Section {
    Category, Module
}
