/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui;

import cc.hyperium.utils.HyperiumFontRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tk.rektsky.Client;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.Setting;

public class ClickGuiOLD
extends GuiScreen {
    private Module seletedModule;
    private static Category currentTab = Category.COMBAT;
    private ResourceLocation left = new ResourceLocation("rektsky/clickgui/left.png");
    private ResourceLocation right = new ResourceLocation("rektsky/clickgui/right.png");
    private ResourceLocation icon = new ResourceLocation("rektsky/icons/icon.png");
    private HyperiumFontRenderer fontRendererObj = Client.getHDFont();
    Color bgCol = new Color(35, 35, 35);

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean enabledBlend = GL11.glIsEnabled(3042);
        if (!enabledBlend) {
            GL11.glEnable(3042);
        }
        ClickGuiOLD.drawRect(this.getResizedX(289), this.getResizedY(113), this.getResizedX(545), this.getResizedY(998), -13487566);
        ClickGuiOLD.drawRect(this.getResizedX(818), this.getResizedY(136), this.getResizedX(1600), this.getResizedY(944), -12632257);
        ClickGuiOLD.drawRect(this.getResizedX(545), this.getResizedY(128), this.getResizedX(818), this.getResizedY(956), -13092808);
        GlStateManager.color(255.0f, 255.0f, 255.0f, 255.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("rektsky/clickgui/icon.png"));
        ClickGuiOLD.drawModalRectWithCustomSizedTexture(this.getResizedX(290), this.getResizedY(130), 0.0f, 0.0f, this.getResizedX(100), this.getResizedY(100), this.getResizedX(100), this.getResizedY(100));
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.getResizedX(388), this.getResizedY(137), 0.0f);
        GlStateManager.scale(1.3f, 1.3f, 0.0f);
        this.fontRendererObj.drawString("RektSky", 0.0f, 0.0f, 0xFFFFFF);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.getResizedX(396), this.getResizedY(170), 0.0f);
        GlStateManager.scale(0.7f, 0.7f, 0.0f);
        this.fontRendererObj.drawString("Version: B5", 0.0f, 0.0f, 0xFFFFFF);
        this.fontRendererObj.drawString("Private", 0.0f, 10.0f, 0xFFFFFF);
        this.fontRendererObj.drawString("User: " + Client.userName, 0.0f, 20.0f, 0xFFFFFF);
        this.fontRendererObj.drawString("Role: " + Client.role, 0.0f, 30.0f, 0xFFFFFF);
        GlStateManager.popMatrix();
        ClickGuiOLD.drawRect(this.getResizedX(290), this.getResizedY(262), this.getResizedX(545), this.getResizedY(264), -9408400);
        List<Category> categories = Arrays.asList(Category.values());
        for (int i2 = 1; i2 <= 7; ++i2) {
            if (mouseX >= this.getResizedX(290) && mouseX <= this.getResizedX(545) && mouseY >= this.getResizedY(288 + 67 * (i2 - 1)) && mouseY <= this.getResizedY(288 + 67 * i2)) {
                ClickGuiOLD.drawRect(this.getResizedX(290), this.getResizedY(288 + 67 * (i2 - 1)), this.getResizedX(545), this.getResizedY(288 + 67 * i2), -10197916);
            }
            int c2 = 0xA8A8A8;
            if (currentTab == categories.get(i2 - 1)) {
                c2 = 0xFFFFFF;
            }
            this.fontRendererObj.drawString(categories.get(i2 - 1).getName(), this.getResizedX(368), this.getResizedY(317 + 67 * (i2 - 1)), c2);
            Minecraft.getMinecraft().getTextureManager().bindTexture(categories.get(i2 - 1).getIcon());
            ClickGuiOLD.drawModalRectWithCustomSizedTexture(this.getResizedX(305), this.getResizedY(300 + 67 * (i2 - 1)), 0.0f, 0.0f, this.getResizedX(45), this.getResizedY(45), this.getResizedX(45), this.getResizedY(45));
        }
        ArrayList<Module> modulesToRender = new ArrayList<Module>();
        if (currentTab != null) {
            for (Module module : ModulesManager.getModules()) {
                if (module.category != currentTab) continue;
                modulesToRender.add(module);
            }
        }
        int i3 = 0;
        for (Module module : modulesToRender) {
            int c3 = 0x797979;
            if (mouseX >= this.getResizedX(560) && mouseY >= this.getResizedY(154 + 46 * i3)) {
                StringBuilder stringBuilder = new StringBuilder();
                if ((float)mouseX <= (float)this.getResizedX(560) + this.fontRendererObj.getWidth(stringBuilder.append("- ").append(module.name).toString()) && mouseY <= this.getResizedY(154 + 46 * i3 + this.fontRendererObj.FONT_HEIGHT)) {
                    c3 = 0xAAAAAA;
                }
            }
            if (module.isToggled()) {
                c3 = 0xFFFFFF;
            }
            this.fontRendererObj.drawString("- " + module.name, this.getResizedX(560), this.getResizedY(154 + 46 * i3), c3);
            ++i3;
        }
        i3 = 0;
        if (!enabledBlend) {
            GL11.glDisable(3042);
        }
        if (this.seletedModule == null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.getResizedX(1200), this.getResizedY(506), 0.0f);
            GlStateManager.scale(3.0f, 3.0f, 0.0f);
            this.fontRendererObj.drawString("Welcome to RektSky!", -this.fontRendererObj.getWidth("Welcome to RektSky!") / 2.0f, 0.0f, 0xFFFFFF);
            GlStateManager.popMatrix();
            this.fontRendererObj.drawString("Right click a module to edit settings", (float)this.getResizedX(1200) - this.fontRendererObj.getWidth("Right click a module to edit settings") / 2.0f, this.getResizedY(506) + this.fontRendererObj.FONT_HEIGHT * 3, 0xFFFFFF);
        } else {
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.getResizedX(862), this.getResizedY(172), 0.0f);
            GlStateManager.scale(2.0f, 2.0f, 0.0f);
            this.fontRendererObj.drawString(this.seletedModule.name, 0.0f, 0.0f, 0xFFFFFF);
            GlStateManager.popMatrix();
            this.fontRendererObj.drawString(this.seletedModule.description, this.getResizedX(892), this.getResizedY(172) + this.fontRendererObj.FONT_HEIGHT * 2, 0xFFFFFF);
            int y2 = 0;
            for (Setting setting : this.seletedModule.settings) {
                this.fontRendererObj.drawString(setting.name, this.getResizedX(900), this.getResizedY(253 + y2), 0xFFFFFF);
                if (setting instanceof BooleanSetting) {
                    Gui.drawRect(this.getResizedX(900) - 64, this.getResizedY(253 + y2), this.getResizedX(900) - 32, this.getResizedY(253) + 32, this.bgCol.getRGB());
                }
                y2 = (int)((float)y2 + (this.fontRendererObj.getHeight(setting.name) + 15.0f));
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        List<Category> categories = Arrays.asList(Category.values());
        for (int i2 = 1; i2 <= 7; ++i2) {
            if (mouseX < this.getResizedX(290) || mouseX > this.getResizedX(545) || mouseY < this.getResizedY(288 + 67 * (i2 - 1)) || mouseY > this.getResizedY(288 + 67 * i2)) continue;
            currentTab = categories.get(i2 - 1);
            return;
        }
        ArrayList<Module> modulesToRender = new ArrayList<Module>();
        if (currentTab != null) {
            for (Module module : ModulesManager.getModules()) {
                if (module.category != currentTab) continue;
                modulesToRender.add(module);
            }
        }
        int i3 = 0;
        for (Module module : modulesToRender) {
            if (mouseX >= this.getResizedX(560) && mouseY >= this.getResizedY(154 + 46 * i3)) {
                StringBuilder stringBuilder = new StringBuilder();
                if ((float)mouseX <= (float)this.getResizedX(560) + this.fontRendererObj.getWidth(stringBuilder.append("- ").append(module.name).toString()) && mouseY <= this.getResizedY(154 + 46 * i3 + this.fontRendererObj.FONT_HEIGHT)) {
                    if (mouseButton == 0) {
                        module.toggle();
                    }
                    if (mouseButton == 1) {
                        this.seletedModule = module;
                    }
                }
            }
            ++i3;
        }
        i3 = 0;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    }

    @Override
    public void initGui() {
        this.seletedModule = null;
    }

    public int getResizedX(int x2) {
        return Math.round((float)x2 / 2.0f);
    }

    public int getResizedY(int y2) {
        return Math.round((float)y2 / 2.0f);
    }
}

