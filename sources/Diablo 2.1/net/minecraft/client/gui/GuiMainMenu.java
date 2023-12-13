package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.Project;
import wtf.diablo.Diablo;
import wtf.diablo.gui.GuiAltManager;
import wtf.diablo.gui.mainMenu.MainMenuButton;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.glsl.GLSLSandboxShader;
import wtf.diablo.utils.render.ChatColor;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    private long initTime = System.currentTimeMillis();
    private GLSLSandboxShader backgroundShader;
    public ArrayList<MainMenuButton> buttons = new ArrayList<MainMenuButton>();
    private boolean supportsGL = true;
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
    private ResourceLocation backgroundTexture;
    private DynamicTexture viewportTexture;
    private int panoramaTimer;
    private float updateCounter;
    private static final Random RANDOM = new Random();

    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};

    public GuiMainMenu()
    {
        try {
            this.backgroundShader = new GLSLSandboxShader("/assets/minecraft/diablo/shader/background.fsh");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.updateCounter = RANDOM.nextFloat();
    }



    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        buttons.clear();
        addButtons(height - 60);

        initTime = System.currentTimeMillis();
    }

    private void addButtons(int y) {
        double number = (width /5f) - 8;
        buttons.add(new MainMenuButton(0,"Singleplayer", 10, y, number,40));
        buttons.add(new MainMenuButton(1,"Multiplayer", 15 + number, y, number,40));
        buttons.add(new MainMenuButton(2,"Alt Manager", 20 + number * 2, y, number,40));
        buttons.add(new MainMenuButton(3,"Settings", 25 + number * 3, y, number,40));
        buttons.add(new MainMenuButton(4,"Exit", 30 + number * 4, y, number,40));
    }


    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(int buttonID) throws IOException {
        switch (buttonID) {
            case 0:
                mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 1:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 2:
                mc.displayGuiScreen(new GuiAltManager(this));
                break;
            case 3:
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                break;
            case 4:
                mc.shutdown();
                break;
        }
    }


    public void confirmClicked(boolean result, int id) {
    }
    private void rotateAndBlurSkybox(float p_73968_1_)
    {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;

        for (int j = 0; j < i; ++j)
        {
            float f = 1.0F / (float)(j + 1);
            int k = this.width;
            int l = this.height;
            float f1 = (float)(j - i / 2) / 256.0F;
            worldrenderer.pos((double)k, (double)l, (double)this.zLevel).tex((double)(0.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos((double)k, 0.0D, (double)this.zLevel).tex((double)(1.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, (double)this.zLevel).tex((double)(1.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, (double)l, (double)this.zLevel).tex((double)(0.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }
    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = 8;

        for (int j = 0; j < i * i; ++j)
        {
            GlStateManager.pushMatrix();
            float f = ((float)(j % i) / (float)i - 0.5F) / 64.0F;
            float f1 = ((float)(j / i) / (float)i - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k)
            {
                GlStateManager.pushMatrix();

                if (k == 1)
                {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 2)
                {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 3)
                {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 4)
                {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (k == 5)
                {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                float f3 = 0.0F;
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }
    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
    {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        float f = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float f1 = (float)this.height * f / 256.0F;
        float f2 = (float)this.width * f / 256.0F;
        int i = this.width;
        int j = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, (double)j, (double)this.zLevel).tex((double)(0.5F - f1), (double)(0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos((double)i, (double)j, (double)this.zLevel).tex((double)(0.5F - f1), (double)(0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos((double)i, 0.0D, (double)this.zLevel).tex((double)(0.5F + f1), (double)(0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, (double)this.zLevel).tex((double)(0.5F + f1), (double)(0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }




    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (supportsGL)
            try {
                GlStateManager.enableAlpha();
                GlStateManager.disableCull();
                this.backgroundShader.useShader(width, height, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);



                GL11.glBegin(GL11.GL_QUADS);

                GL11.glVertex2f(-1f, -1f);
                GL11.glVertex2f(-1f, 1f);
                GL11.glVertex2f(1f, 1f);
                GL11.glVertex2f(1f, -1f);

                GL11.glEnd();

                // Unbind shader
                GL20.glUseProgram(0);
            } catch (Exception ex) {
                ex.printStackTrace();

                GlStateManager.disableAlpha();
                this.renderSkybox(mouseX, mouseY, partialTicks);
                GlStateManager.enableAlpha();
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                int i = 274;
                int j = this.width / 2 - i / 2;
                int k = 30;
                this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
                this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
                this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                if ((double)this.updateCounter < 1.0E-4D)
                {
                    this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 99, 44);
                    this.drawTexturedModalRect(j + 99, k + 0, 129, 0, 27, 44);
                    this.drawTexturedModalRect(j + 99 + 26, k + 0, 126, 0, 3, 44);
                    this.drawTexturedModalRect(j + 99 + 26 + 3, k + 0, 99, 0, 26, 44);
                    this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
                }
                else
                {
                    this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 155, 44);
                    this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
                }

                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(this.width / 2 + 90), 70.0F, 0.0F);
                GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
                this.supportsGL = false;
            }
        //Fonts.SFReg45.drawStringWithShadow(Diablo.name + " " + Diablo.version, width / 2f - Fonts.SFReg45.getStringWidth(Diablo.name + " " + Diablo.version)/2f, height / 6f, 0xfFffffff);
        int imageSize = 100;
        if(height > 300)
            imageSize = 150;
        RenderUtil.drawImage(width / 2f - (imageSize / 2f), (height - 60) / 2f - (imageSize / 2f),imageSize,imageSize,new ResourceLocation("diablo/icons/logo.png"),1);

        String loggedInStr = ChatColor.WHITE + "Logged in as: " + ChatColor.GREEN + "Eclipse Ready by LuMi" + ChatColor.GRAY + " (" + "6969" + ChatColor.GRAY +")";

        mc.fontRendererObj.drawString(loggedInStr, (int) (width / 2f - (mc.fontRendererObj.getStringWidth(loggedInStr) / 2f)),height - 4 - mc.fontRendererObj.FONT_HEIGHT,-1);
        for(MainMenuButton but : buttons){
            but.drawScreen(mouseX,mouseY, this.width > 500);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    public void updateScreen()
    {
        ++this.panoramaTimer;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseButton == 0)
            for(MainMenuButton but : buttons){
                if(but.mouseClicked(mouseX,mouseY)){
                    actionPerformed(but.id);
                }
            }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
