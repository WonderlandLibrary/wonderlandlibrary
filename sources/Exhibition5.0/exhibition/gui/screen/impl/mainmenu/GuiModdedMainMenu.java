// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.screen.impl.mainmenu;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import exhibition.management.ColorManager;
import net.minecraft.client.gui.Gui;
import exhibition.util.render.ColorContainer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import exhibition.gui.altmanager.GuiAltManager;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiButton;
import exhibition.gui.screen.component.GuiMenuButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiMainMenu;

public class GuiModdedMainMenu extends GuiMainMenu
{
    private ResourceLocation uparrow;
    
    public GuiModdedMainMenu() {
        this.uparrow = new ResourceLocation("textures/skeetchainmail.png");
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        final String strSSP = I18n.format("Single", new Object[0]);
        final String strSMP = I18n.format("Multi", new Object[0]);
        final String strOptions = I18n.format("Options", new Object[0]);
        final String strQuit = I18n.format("Exit Game", new Object[0]);
        final String strLang = I18n.format("Language", new Object[0]);
        final String strAccounts = "Accounts";
        final int initHeight = this.height / 4 + 48;
        final int objHeight = 17;
        final int objWidth = 63;
        final int objPadding = 4;
        final int xMid = this.width / 2 - objWidth / 2;
        this.buttonList.add(new GuiMenuButton(0, xMid, initHeight, objWidth, objHeight, strSSP));
        this.buttonList.add(new GuiMenuButton(1, xMid, initHeight + 20, objWidth, objHeight, strSMP));
        this.buttonList.add(new GuiMenuButton(2, xMid, initHeight + 40, objWidth, objHeight, strOptions));
        this.buttonList.add(new GuiMenuButton(3, xMid, initHeight + 60, objWidth, objHeight, strLang));
        this.buttonList.add(new GuiMenuButton(4, xMid, initHeight + 80, objWidth, objHeight, strAccounts));
        this.buttonList.add(new GuiMenuButton(5, xMid, initHeight + 100, objWidth, objHeight, strQuit));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        else if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        else if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        else if (button.id == 3) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        else if (button.id == 4) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }
        else if (button.id == 5) {
            this.mc.shutdown();
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.renderSkybox(mouseX, mouseY, partialTicks);
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        RenderingUtil.rectangleBordered(sr.getScaledWidth() / 2 - 62 - 0.5, this.height / 4 + 30 - 0.3, sr.getScaledWidth() / 2 + 62 + 0.5, this.height / 4 + 175 + 0.3, 0.5, Colors.getColor(60), Colors.getColor(10));
        RenderingUtil.rectangleBordered(sr.getScaledWidth() / 2 - 62 + 0.5, this.height / 4 + 30 + 0.6, sr.getScaledWidth() / 2 + 62 - 0.5, this.height / 4 + 175 - 0.6, 1.3, Colors.getColor(60), Colors.getColor(40));
        RenderingUtil.rectangleBordered(sr.getScaledWidth() / 2 - 62 + 2.5, this.height / 4 + 30 + 2.5, sr.getScaledWidth() / 2 + 62 - 2.5, this.height / 4 + 175 - 2.5, 0.5, Colors.getColor(22), Colors.getColor(12));
        GlStateManager.pushMatrix();
        GlStateManager.color(2.0f, 2.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(this.uparrow);
        GlStateManager.translate(sr.getScaledWidth() / 2 - 60, this.height / 4 + 30, 0.0f);
        this.drawIcon(1.0, 3.0, 0.0f, 0.0f, 118.0, 139.0, 325.0f, 275.0f);
        GlStateManager.popMatrix();
        RenderingUtil.drawGradientSideways(sr.getScaledWidth() / 2 - 62 + 3, this.height / 4 + 30 + 3, sr.getScaledWidth() / 2, this.height / 4 + 30 + 4, Colors.getColor(55, 177, 218), Colors.getColor(204, 77, 198));
        RenderingUtil.drawGradientSideways(sr.getScaledWidth() / 2, this.height / 4 + 30 + 3, sr.getScaledWidth() / 2 + 62 - 3, this.height / 4 + 30 + 4, Colors.getColor(204, 77, 198), Colors.getColor(204, 227, 53));
        RenderingUtil.rectangle(sr.getScaledWidth() / 2 - 62 + 3, this.height / 4 + 30 + 3.5, sr.getScaledWidth() / 2 + 62 - 3, this.height / 4 + 30 + 4, Colors.getColor(0, 110));
        RenderingUtil.rectangleBordered(sr.getScaledWidth() / 2 - 62 + 6, this.height / 4 + 30 + 8, sr.getScaledWidth() / 2 + 62 - 6.5, this.height / 4 + 169, 0.3, Colors.getColor(48), Colors.getColor(10));
        RenderingUtil.rectangle(sr.getScaledWidth() / 2 - 62 + 6 + 1, this.height / 4 + 30 + 9, sr.getScaledWidth() / 2 + 62 - 7.5, this.height / 4 + 169 - 1, Colors.getColor(17));
        RenderingUtil.rectangle(sr.getScaledWidth() / 2 - 62 + 6 + 4, this.height / 4 + 30 + 8, sr.getScaledWidth() / 2 - 62 + 36, this.height / 4 + 30 + 9, Colors.getColor(17));
        GlStateManager.pushMatrix();
        GlStateManager.translate(sr.getScaledWidth() / 2 - 62 + 6 + 5, this.height / 4 + 30 + 8, 0.0f);
        GlStateManager.scale(0.5, 0.5, 0.5);
        this.mc.fontRendererObj.drawStringWithShadow("Main Menu", 0.0f, 0.0f, -1);
        GlStateManager.popMatrix();
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        final int width = 150;
        final int hei = 26;
        final boolean override = false;
        for (int i = 0; i < this.buttonList.size(); ++i) {
            final GuiButton g = this.buttonList.get(i);
            if (override) {
                final int x = g.xPosition;
                final int y = g.yPosition;
                final boolean over = mouseX >= x && mouseY >= y && mouseX < x + g.getButtonWidth() && mouseY < y + hei;
                if (over) {
                    Gui.fillHorizontalGrad(x, y, width, hei, new ColorContainer(5, 40, 85, 255), new ColorContainer(0, 0, 0, 0));
                }
                else {
                    Gui.fillHorizontalGrad(x, y, width, hei, new ColorContainer(0, 0, 0, 255), new ColorContainer(0, 0, 0, 0));
                }
                this.fontRendererObj.drawString(g.displayString, g.xPosition + 10, g.yPosition + hei / 2 - 3, -1);
            }
            else {
                g.drawButton(this.mc, mouseX, mouseY);
            }
        }
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.width / 2 - this.mc.fontRendererObj.getStringWidth("Exhibition") * 2, 50.0f, 0.0f);
        GlStateManager.scale(4.0f, 4.0f, 4.0f);
        this.mc.fontRendererObj.drawStringWithShadow("Exhibition", 0.0f, 0.0f, ColorManager.hudColor.getColorInt());
        GlStateManager.popMatrix();
    }
    
    private void drawIcon(final double x, final double y, final float u, final float v, final double width, final double height, final float textureWidth, final float textureHeight) {
        final float var8 = 1.0f / textureWidth;
        final float var9 = 1.0f / textureHeight;
        final Tessellator var10 = Tessellator.getInstance();
        final WorldRenderer var11 = var10.getWorldRenderer();
        var11.startDrawingQuads();
        var11.addVertexWithUV(x, y + height, 0.0, u * var8, (v + (float)height) * var9);
        var11.addVertexWithUV(x + width, y + height, 0.0, (u + (float)width) * var8, (v + (float)height) * var9);
        var11.addVertexWithUV(x + width, y, 0.0, (u + (float)width) * var8, v * var9);
        var11.addVertexWithUV(x, y, 0.0, u * var8, v * var9);
        var10.draw();
    }
}
