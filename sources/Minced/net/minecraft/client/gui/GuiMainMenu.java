// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.IOException;
import ru.tuskevich.ui.altmanager.GuiAltManager;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.shader.GaussianBlur;
import ru.tuskevich.util.shader.StencilUtil;
import net.minecraft.client.renderer.GlStateManager;
import ru.tuskevich.util.render.RenderUtility;
import net.minecraft.util.ResourceLocation;
import ru.tuskevich.ui.buttons.GuiMainMenuButton;
import java.awt.Color;
import java.util.Date;
import ru.tuskevich.util.math.animbackground;

public class GuiMainMenu extends GuiScreen
{
    private int width;
    private int height;
    private animbackground backgroundShader;
    public static int bg;
    Date d;
    int offset;
    private Color gradientColor1;
    private Color gradientColor2;
    private Color gradientColor3;
    private Color gradientColor4;
    
    public GuiMainMenu() {
        this.d = new Date();
        this.offset = 50;
        this.gradientColor1 = Color.WHITE;
        this.gradientColor2 = Color.WHITE;
        this.gradientColor3 = Color.WHITE;
        this.gradientColor4 = Color.WHITE;
    }
    
    @Override
    public void initGui() {
        final ScaledResolution sr = new ScaledResolution(GuiMainMenu.mc);
        this.width = sr.getScaledWidth();
        this.height = sr.getScaledHeight();
        this.buttonList.add(new GuiMainMenuButton(0, this.width / 2 - 60, this.height / 2 - 40, 120, 15, "singleplayer"));
        this.buttonList.add(new GuiMainMenuButton(1, this.width / 2 - 60, this.height / 2 - 20, 120, 15, "multiplayer"));
        this.buttonList.add(new GuiMainMenuButton(2, this.width / 2 - 60, this.height / 2 + 0, 120, 15, "altmanager"));
        this.buttonList.add(new GuiMainMenuButton(3, this.width / 2 - 60, this.height / 2 + 20, 120, 15, "settings"));
        this.buttonList.add(new GuiMainMenuButton(4, this.width / 2 - 60, this.height / 2 + 40, 120, 15, "exit"));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(GuiMainMenu.mc);
        RenderUtility.drawImage(new ResourceLocation("client/images/mainmenu/pesun6.png"), 0.0f, 0.0f, (float)sr.getScaledWidth(), (float)sr.getScaledHeight(), new Color(255, 255, 255));
        GlStateManager.disableCull();
        StencilUtil.initStencilToWrite();
        RenderUtility.drawRound(this.width / 2 - 72.5f, (float)(this.height / 2 - 65), 145.0f, 130.0f, 0.0f, new Color(0, 0, 0, 130));
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(15.0f);
        StencilUtil.uninitStencilBuffer();
        RenderUtility.drawRound(this.width / 2 - 72.5f, (float)(this.height / 2 - 65), 145.0f, 130.0f, 0.0f, new Color(0, 0, 0, 130));
        Fonts.vog26.drawCenteredString("minced beta", (float)(sr.getScaledWidth() / 2), (float)(sr.getScaledHeight() / 2 - 8 - this.offset), -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                GuiMainMenu.mc.displayGuiScreen(new GuiWorldSelection(this));
                break;
            }
            case 1: {
                GuiMainMenu.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 2: {
                GuiMainMenu.mc.displayGuiScreen(new GuiAltManager());
                break;
            }
            case 3: {
                GuiMainMenu.mc.displayGuiScreen(new GuiOptions(this, GuiMainMenu.mc.gameSettings));
                break;
            }
            case 4: {
                System.exit(0);
                break;
            }
        }
        super.actionPerformed(button);
    }
    
    static {
        GuiMainMenu.bg = 0;
    }
}
