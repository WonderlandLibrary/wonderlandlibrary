package xyz.northclient;

import com.madgag.gif.fmsware.GifDecoder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Modules;
import xyz.northclient.features.events.Render2DEvent;
import xyz.northclient.theme.ColorUtil;
import xyz.northclient.theme.Themes;
import xyz.northclient.util.font.CustomFont;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.RectUtil;
import xyz.northclient.util.shader.ShaderUtil;
import xyz.northclient.util.shader.impl.Shader;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public class UIHook implements InstanceAccess{

    public static long SHADER_TIME = 0;

    public interface ArraylistFont {
        float getWidth(String text);
        float getHeight(String text);
        void drawString(String text,float x, float y, int color);
        void drawStringWithShadow(String text,float x, float y, int color);
        float getKerningX();
        float getKerningY();
    }

    public static class MinecraftArraylistFont implements ArraylistFont {
        private FontRenderer fontRenderer;
        public MinecraftArraylistFont(FontRenderer renderer) {
            this.fontRenderer = renderer;
        }

        @Override
        public float getWidth(String text) {
            return fontRenderer.getStringWidth(text);
        }

        @Override
        public float getHeight(String text) {
            return fontRenderer.FONT_HEIGHT;
        }

        @Override
        public void drawString(String text, float x, float y, int color) {
            fontRenderer.drawString(text, (int) x, (int) y,color);
        }

        @Override
        public void drawStringWithShadow(String text, float x, float y, int color) {
            fontRenderer.drawStringWithShadow(text,x,y,color);
        }

        @Override
        public float getKerningX() {
            return 0;
        }
        @Override
        public float getKerningY() {
            return -1;
        }
    }

    public static class CustomArraylistFont implements ArraylistFont{
        private CustomFont font;
        public CustomArraylistFont(CustomFont font) {
            this.font = font;
        }

        @Override
        public float getWidth(String text) {
            return font.getStringWidth(text);
        }

        @Override
        public float getHeight(String text) {
            return font.getHeight();
        }

        @Override
        public void drawString(String text, float x, float y, int color) {
            font.drawString(text,x,y,color);
        }

        @Override
        public void drawStringWithShadow(String text, float x, float y, int color) {
            font.drawStringWithShadow(text,x,y,color);
        }

        @Override
        public float getKerningX() {
            return 0;
        }
        @Override
        public float getKerningY() {
            if(font == FontUtil.DefaultSmall) {
                return -0.4f;
            }
            return 0;
        }
    }

    private static Minecraft mc = Minecraft.getMinecraft();
    @Getter @Setter
    private Themes theme = Themes.NORTH;

    public void render(float partialTicks) {
        north.getDraggableManager().Render();
        NorthSingleton.INSTANCE.getEventBus().post(new Render2DEvent(partialTicks));
        NorthSingleton.INSTANCE.auth.renderAuthStatus();
        String text = (NorthSingleton.BETA ? "Beta" : "Release") + " Build - " + NorthSingleton.INSTANCE.getAuth().username + " - " + NorthSingleton.INSTANCE.getAuth().uid;

        FontUtil.DefaultSmall.drawStringWithShadow(text,new ScaledResolution(mc).getScaledWidth()-FontUtil.DefaultSmall.getStringWidth(text)-5,new ScaledResolution(mc).getScaledHeight()-FontUtil.DefaultSmall.getHeight()-5,-1);
    }
}
