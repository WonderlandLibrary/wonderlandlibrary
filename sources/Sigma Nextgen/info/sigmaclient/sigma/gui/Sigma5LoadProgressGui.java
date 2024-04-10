package info.sigmaclient.sigma.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import info.sigmaclient.sigma.sigma5.utils.ImageUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.DynamicTexture;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.gui.mainmenu.SigmaGuiMainMenu;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ResourceLoadProgressGui;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.resources.IAsyncReloader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.optifine.Config;
import net.optifine.shaders.config.ShaderPackParser;
import net.optifine.util.PropertiesOrdered;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;

import static info.sigmaclient.sigma.sigma5.utils.SigmaRenderUtils.䬹鞞葫Ꮀ待湗;

public class Sigma5LoadProgressGui extends ResourceLoadProgressGui
{


    static DynamicTexture background_blur = ImageUtils.blurImage("", 0.25f, 25);

    public Sigma5LoadProgressGui(Minecraft p_i225928_1_, IAsyncReloader p_i225928_2_, Consumer<Optional<Throwable>> p_i225928_3_, boolean p_i225928_4_)
    {
        super(p_i225928_1_, p_i225928_2_, p_i225928_3_, p_i225928_4_);
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        float f3 = this.asyncReloader.estimateExecutionSpeed();
        this.progress = MathHelper.clamp(this.progress * 0.95F + f3 * 0.050000012F, 0.0F, 1.0F);

        int i = this.mc.getMainWindow().getScaledWidth();
        int j = this.mc.getMainWindow().getScaledHeight();
        long k = Util.milliTime();

        if (this.reloading && (this.asyncReloader.asyncPartDone() || this.mc.currentScreen != null) && this.fadeInStart == -1L) {
            this.fadeInStart = k;
        }

        float f = this.fadeOutStart > -1L ? (float) (k - this.fadeOutStart) / 1000.0F : -1.0F;
        float f1 = this.fadeInStart > -1L ? (float) (k - this.fadeInStart) / 500.0F : -1.0F;

        if (f >= 1.0F) {
            this.fadeOut = true;

            if (this.mc.currentScreen != null) {
                this.mc.currentScreen.render(matrixStack, 0, 0, partialTicks);
            }

            int l = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
            fill(matrixStack, 0, 0, i, j, this.colorBackground | l << 24);
        } else if (this.reloading) {
            if (this.mc.currentScreen != null && f1 < 1.0F) {
                this.mc.currentScreen.render(matrixStack, mouseX, mouseY, partialTicks);
            }

            int i2 = MathHelper.ceil(MathHelper.clamp((double) f1, 0.15D, 1.0D) * 255.0D);
            fill(matrixStack, 0, 0, i, j, this.colorBackground | i2 << 24);
        } else {
            fill(matrixStack, 0, 0, i, j, this.colorBackground | -16777216);
        }

        GL11.glPushMatrix();
        float n9 = 1111.0f;
        if (this.mc.getMainWindow().getWidth() != 0) {
            n9 = (float)(this.mc.getMainWindow().getFramebufferWidth() / this.mc.getMainWindow().getWidth());
        }
        final float n10 = new ScaledResolution(mc).scaleFactor * n9;
//        GL11.glScalef(1.0f / n10, 1.0f / n10, 0.0f);
        draw(1.0f, progress);
        GL11.glPopMatrix();
        // SIGMA 5.0

        if (f >= 1.0F) {
            if (!SigmaNG.init) {
                SigmaNG.staticInitTitle();
                SigmaNG.init = true;
                if(SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA)
                    Minecraft.getInstance().displayGuiScreen(new SigmaGuiMainMenu());
                else
                    Minecraft.getInstance().displayGuiScreen(new MainMenuScreen());
            }
        }

        if (f >= 2.0F) {
            this.mc.setLoadingGui(null);
        }

        if (this.fadeOutStart == -1L && this.asyncReloader.fullyDone() && (!this.reloading || f1 >= 2.0F)) {
            this.fadeOutStart = Util.milliTime();

            try {
                this.asyncReloader.join();
                this.completedCallback.accept(Optional.empty());
            } catch (Throwable throwable) {
                this.completedCallback.accept(Optional.of(throwable));
            }

            if (this.mc.currentScreen != null) {
                this.mc.currentScreen.init(this.mc, this.mc.getMainWindow().getScaledWidth(), this.mc.getMainWindow().getScaledHeight());
            }
        }
    }
    public void draw(final float n, final float n2){
        GL11.glEnable(3008);
        GL11.glEnable(3042);
        ScaledResolution sr = new ScaledResolution(mc);
        background_blur.bindTexture();
        RenderUtils.drawTextureCustom(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 1);

        RenderUtils.drawRect(0.0f, 0.0f, sr.getScaledWidth(), sr.getScaledHeight(), 霥瀳놣㠠釒(0, 0.75f));
        final int n3 = 455;
        final int n4 = 78;
        final int n5 = (sr.getScaledWidth() - n3) / 2;
        final int round = Math.round((sr.getScaledHeight() - n4) / 2f - 14.0f * n);
        final float n6 = 0.5f;
        GL11.glPushMatrix();
        RenderUtils.drawTextureLocationZoom((float)n5 + n3 * 0.25f, (float)round + n4 * 0.25f, (float)n3 * 0.5f, (float)n4 * 0.5f, "logo2", new Color(霥瀳놣㠠釒(-65794, n)));
        GL11.glTranslatef((float)(sr.getScaledWidth() / 2), (float)(sr.getScaledHeight() / 2), 0.0f);
        GL11.glScalef(n6, n6, 0.0f);
        GL11.glTranslatef((float)(-sr.getScaledWidth() / 2), (float)(-sr.getScaledHeight() / 2), 0.0f);
        final float min = Math.min(1.0f, n2 * 1.02f);
        final int n7 = 80;
//        if (n == 1.0f) {
            䬹鞞葫Ꮀ待湗((float)n5, (float)(round + n4 + n7), (float)n3, 20.0f, 10.0f, 霥瀳놣㠠釒(-65794, 0.3f * n));
            䬹鞞葫Ꮀ待湗((float)(n5 + 1), (float)(round + n4 + n7 + 1), (float)(n3 - 2), 18.0f, 9.0f, 霥瀳놣㠠釒(0, 1.0f * n));
//        }
        䬹鞞葫Ꮀ待湗((float)(n5 + 2), (float)(round + n4 + n7 + 2), (float)(int)((n3 - 4) * min), 16.0f, 8.0f, 霥瀳놣㠠釒(-65794, 0.9f * n));
        GL11.glPopMatrix();
    }
    public boolean isPauseScreen()
    {
        return true;
    }

    public static int 霥瀳놣㠠釒(final int n, final float n2) {
        return (int)(n2 * 255.0f) << 24 | (n & 0xFFFFFF);
    }
    public void update()
    {
        this.colorBackground = field_238628_c_;
        this.colorBar = field_238628_c_;
        this.colorOutline = 16777215;
        this.colorProgress = 16777215;

        if (Config.isCustomColors())
        {
            try
            {
                String s = "optifine/color.properties";
                if(SigmaNG.betterResPack){
                    s = s.replace("optifine/", "mcpatcher/");
                }
                ResourceLocation resourcelocation = new ResourceLocation(s);

                if (!Config.hasResource(resourcelocation))
                {
                    return;
                }

                InputStream inputstream = Config.getResourceStream(resourcelocation);
                Config.dbg("Loading " + s);
                Properties properties = new PropertiesOrdered();
                properties.load(inputstream);
                inputstream.close();
                this.colorBackground = readColor(properties, "screen.loading", this.colorBackground);
                this.colorOutline = readColor(properties, "screen.loading.outline", this.colorOutline);
                this.colorBar = readColor(properties, "screen.loading.bar", this.colorBar);
                this.colorProgress = readColor(properties, "screen.loading.progress", this.colorProgress);
                this.blendState = ShaderPackParser.parseBlendState(properties.getProperty("screen.loading.blend"));
            }
            catch (Exception exception)
            {
                Config.warn("" + exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
    }

    private static int readColor(Properties p_readColor_0_, String p_readColor_1_, int p_readColor_2_)
    {
        String s = p_readColor_0_.getProperty(p_readColor_1_);

        if (s == null)
        {
            return p_readColor_2_;
        }
        else
        {
            s = s.trim();
            int i = parseColor(s, p_readColor_2_);

            if (i < 0)
            {
                Config.warn("Invalid color: " + p_readColor_1_ + " = " + s);
                return i;
            }
            else
            {
                Config.dbg(p_readColor_1_ + " = " + s);
                return i;
            }
        }
    }

    private static int parseColor(String p_parseColor_0_, int p_parseColor_1_)
    {
        if (p_parseColor_0_ == null)
        {
            return p_parseColor_1_;
        }
        else
        {
            p_parseColor_0_ = p_parseColor_0_.trim();

            try
            {
                return Integer.parseInt(p_parseColor_0_, 16) & 16777215;
            }
            catch (NumberFormatException numberformatexception)
            {
                return p_parseColor_1_;
            }
        }
    }
}
