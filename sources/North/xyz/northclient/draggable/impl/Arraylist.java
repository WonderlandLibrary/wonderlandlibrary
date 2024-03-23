package xyz.northclient.draggable.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import xyz.northclient.UIHook;
import xyz.northclient.draggable.AbstractDraggable;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.theme.ColorUtil;
import xyz.northclient.util.animations.util.Easing;
import xyz.northclient.util.animations.util.Easings;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.RectUtil;
import xyz.northclient.util.shader.StencilUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Arraylist extends AbstractDraggable {
    public ModeValue fontMode = new ModeValue("Font",this)
            .add(new Watermark.StringMode("Product Sans",this))
            .add(new Watermark.StringMode("Minecraft",this))
            .add(new Watermark.StringMode("Exhibition",this))
            .add(new Watermark.StringMode("SF",this))
            .setDefault("Minecraft");

    public static ModeValue mode;

    public ModeValue animationDirection = new ModeValue("Animation Direction",this)
            .add(new Watermark.StringMode("Right",this))
            .add(new Watermark.StringMode("Up",this))
            .setDefault("Right");

    public Arraylist() {
        super(true);
        mode = new ModeValue("Mode",this)
                .add(new Watermark.StringMode("Plain",this))
                .add(new Watermark.StringMode("Astolfo",this))
                .add(new Watermark.StringMode("Astolfo Shadow",this))
                .add(new Watermark.StringMode("Bloom",this))
                .setDefault("Plain");
    }

    @Override
    public void Init() {
        //Initable position
        X = 0;
        Y = 0;
        AllowRender = true;
    }


    //<inheritdoc /> (From AbstractDraggable)
    @Override
    public Vector2f Render() {
        UIHook.ArraylistFont font = new UIHook.MinecraftArraylistFont(mc.exhiFontRendererObj);
        switch(fontMode.get().getName()) {
            case "Product Sans":
                font = new UIHook.CustomArraylistFont(FontUtil.DefaultSmall);
                break;
            case "Minecraft":
                font = new UIHook.MinecraftArraylistFont(mc.fontRendererObj);
                break;
            case "Exhibition":
                font = new UIHook.MinecraftArraylistFont(mc.exhiFontRendererObj);
                break;
            case "SF":
                font = new UIHook.CustomArraylistFont(FontUtil.SFProRegular);
                break;
            default:
                new UIHook.MinecraftArraylistFont(mc.exhiFontRendererObj);
                break;
        }


        setSuffix(mode.get().getName());

        switch (mode.get().getName()) {
            case "Astolfo":
                astolfo(font);
                break;
            case "Astolfo Shadow":
                astolfoBloom(font);
                break;
            case "Plain":
                plain(font);
                break;
            case "Bloom":
                bloom(font);
                break;
        }

        return new Vector2f(0,0);
    }

    @Override
    public String getDraggableName() {
        return "Arraylist";
    }



    public void plain(UIHook.ArraylistFont font) {
        List<AbstractModule> modules = new ArrayList<>(north.getModules().getModules());
        modules.sort(Comparator.comparingDouble((mod) -> font.getWidth(((AbstractModule)mod).getDisplayName())).reversed());


        int a = 0;
        long colorIndex = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        for(AbstractModule m : modules) {
            String name = m.getDisplayName();
            m.getY().update();
            if(animationDirection.get().getName().equalsIgnoreCase("Up")) {
                m.getY().animate(m.isEnabled() ? a : 0,0.15f);
            } else {
                m.getY().animate(a,0.15f);
            }
            m.getX().update();
            m.getX().animate(m.isEnabled() ? font.getWidth(name) : -5,0.15f);

            if (m.getX().getValue() > 0) {
                int x = (int) (new ScaledResolution(mc).getScaledWidth() - m.getX().getValue());
                int y = (int) m.getY().getValue();

                font.drawStringWithShadow(m.getDisplayName(),x-4+ font.getKerningX(),y+4+font.getKerningY(),ColorUtil.GetColor(colorIndex));
                a+=font.getHeight(m.getDisplayName())+1;
                colorIndex+=150;
            }
        }
    }

    public void bloom(UIHook.ArraylistFont font) {
        List<AbstractModule> modules = new ArrayList<>(north.getModules().getModules());
        modules.sort(Comparator.comparingDouble((mod) -> font.getWidth(((AbstractModule)mod).getDisplayName())).reversed());


        int a = 0;
        long colorIndex = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        for(AbstractModule m : modules) {
            String name = m.getDisplayName();
            m.getY().update();
            if(animationDirection.get().getName().equalsIgnoreCase("Up")) {
                m.getY().animate(m.isEnabled() ? a : 0,0.15f);
            } else {
                m.getY().animate(a,0.15f);
            }
            m.getX().update();
            m.getX().animate(m.isEnabled() ? font.getWidth(name) : -5,0.15f);

            if (m.getX().getValue() > 0) {
                int x = (int) (new ScaledResolution(mc).getScaledWidth() - m.getX().getValue());
                int y = (int) m.getY().getValue();

                Color color = new Color(ColorUtil.GetColor(colorIndex));
                color = new Color(color.getRed(),color.getGreen(),color.getBlue(),150);

                RectUtil.drawBloom((int) (x-4+ font.getKerningX()), (int) (y+4+font.getKerningY()), (int) font.getWidth(m.getDisplayName()), (int) font.getHeight(m.getDisplayName()),16,color);
                font.drawStringWithShadow(m.getDisplayName(),x-4+ font.getKerningX(),y+4+font.getKerningY(),ColorUtil.GetColor(colorIndex));
                a+=font.getHeight(m.getDisplayName())+1;
                colorIndex+=150;
            }
        }
    }

    public void astolfo(UIHook.ArraylistFont font) {
        List<AbstractModule> modules = new ArrayList<>(north.getModules().getModules());
        modules.sort(Comparator.comparingDouble((mod) -> font.getWidth(((AbstractModule)mod).getDisplayName())).reversed());


        int a = 0;
        long colorIndex = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        for(AbstractModule m : modules) {
            String name = m.getDisplayName();
            m.getY().update();
            if(animationDirection.get().getName().equalsIgnoreCase("Up")) {
                m.getY().animate(m.isEnabled() ? a : 0,0.15f);
            } else {
                m.getY().animate(a,0.15f);
            }
            m.getX().update();
            m.getX().animate(m.isEnabled() ? font.getWidth(name) : -5,0.15f);

            if (m.getX().getValue() > 0) {
                int x = (int) (new ScaledResolution(mc).getScaledWidth() - m.getX().getValue());
                int y = a;

                Gui.drawRect(x-6,y,sr.getScaledWidth()-2,y+10,new Color(0,0,0,69).getRGB());
                Gui.drawRect(sr.getScaledWidth()-2,y,sr.getScaledWidth(),y+10, ColorUtil.GetColor(colorIndex));
                font.drawStringWithShadow(m.getDisplayName(),x-4+ font.getKerningX(),y+2+font.getKerningY(),ColorUtil.GetColor(colorIndex));
                a+=10;
                colorIndex+=150;
            }
        }
    }
    public void astolfoBloom(UIHook.ArraylistFont font) {

        List<AbstractModule> modules = new ArrayList<>(north.getModules().getModules());
        modules.sort(Comparator.comparingDouble((mod) -> font.getWidth(((AbstractModule)mod).getDisplayName())).reversed());


        int a = 0;
        long colorIndex = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        for(AbstractModule m : modules) {
            String name = m.getDisplayName();
            m.getY().update();
            if(animationDirection.get().getName().equalsIgnoreCase("Up")) {
                m.getY().animate(m.isEnabled() ? a : 0,0.15f);
            } else {
                m.getY().animate(a,0.15f);
            }
            m.getX().update();
            m.getX().animate(m.isEnabled() ? font.getWidth(name) : -5,0.15f);

            if (m.getX().getValue() > 0) {
                int x = (int) (new ScaledResolution(mc).getScaledWidth() - m.getX().getValue());
                int y = a;

                RectUtil.drawBloom(x-6,y, (int) font.getWidth(m.getDisplayName()),10,16,new Color(0,0,0,130));

                Gui.drawRect(x-6,y,sr.getScaledWidth()-2,y+10,new Color(0,0,0,69).getRGB());
                Gui.drawRect(sr.getScaledWidth()-2,y,sr.getScaledWidth(),y+10, ColorUtil.GetColor(colorIndex));
                font.drawStringWithShadow(m.getDisplayName(),x-4+ font.getKerningX(),y+2+font.getKerningY(),ColorUtil.GetColor(colorIndex));
                a+=10;
                colorIndex+=150;
            }
        }
    }

}