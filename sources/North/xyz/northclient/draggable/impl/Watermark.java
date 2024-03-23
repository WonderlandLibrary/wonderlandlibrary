package xyz.northclient.draggable.impl;


import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.util.vector.Vector2f;
import xyz.northclient.NorthSingleton;
import xyz.northclient.UIHook;
import xyz.northclient.draggable.AbstractDraggable;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.modules.Animations;
import xyz.northclient.features.values.Mode;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.theme.ColorUtil;
import xyz.northclient.theme.Themes;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.RectUtil;
import xyz.northclient.util.shader.RenderUtil;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Watermark extends AbstractDraggable {

    public ModeValue fontMode = new ModeValue("Font",this)
            .add(new StringMode("Product Sans",this))
            .add(new StringMode("Minecraft",this))
            .add(new StringMode("Exhibition",this))
            .add(new StringMode("SF",this))
            .setDefault("Minecraft");

    public ModeValue watermarkMode = new ModeValue("Water Mark",this)
            .add(new StringMode("Normal",this))
            .add(new StringMode("Onetap",this))
            .add(new StringMode("Csgo",this))
            .setDefault("Onetap");
    public ModeValue themeMode;
    public Watermark() {
        super(true);
        themeMode = new ModeValue("Global Theme",this);

        for(Themes theme : Themes.values()) {
            themeMode.add(new StringMode(theme.getName(),this));
        }
    }

    @Override
    public void Init() {
        //Initable position
        X = 5;
        Y = 5;
        AllowRender = true;
    }

    //<inheritdoc /> (From AbstractDraggable)
    @Override
    public Vector2f Render() {
        for(Themes theme : Themes.values()) {
            if(theme.getName().equalsIgnoreCase(themeMode.get().getName())) {
                NorthSingleton.INSTANCE.getUiHook().setTheme(theme);
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(X,Y,0);

        setSuffix(themeMode.get().getName());

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
        switch(watermarkMode.get().getName()) {
            case "Normal":
            font.drawStringWithShadow("N", 5, 5, ColorUtil.GetColor(0));
            font.drawStringWithShadow("orth", 5 + font.getWidth("N"), 5, -1);
            GlStateManager.popMatrix();

             return new Vector2f(font.getWidth("North"),font.getHeight("North"));
            case "Csgo":
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date currentTime = new Date();
                String formattedTime = sdf.format(currentTime);

                int offset = 0;

                    switch(fontMode.get().getName()) {
                        case "Minecraft":
                        case "Exhibition":
                            offset = -5;
                            break;
                    }

                float width = font.getWidth("NorthNorthXD" + NorthSingleton.INSTANCE.auth.username + "21:37:69") + offset;
                RectUtil.drawRoundedBlur(1 + 1,1 +1,(int)width + 5,11 + ((int)font.getHeight("NORTH")) -3,4,5);
                RectUtil.drawRoundedBloom(1 + 1,1 +1,(int)width -1,11 + ((int)font.getHeight("NORTH")) -3,4,5,north.getUiHook().getTheme().getMainColor());
                RectUtil.drawRoundedRect(1,1,width,10 + font.getHeight("NORTH"),4,new Color(43,43,43,255));
                font.drawStringWithShadow("N", 6, 6, ColorUtil.GetColor(0));
                font.drawStringWithShadow("orth", 6 + font.getWidth("N"), 6, -1);
                RectUtil.drawRoundedRect(font.getWidth("NorthXD"),font.getHeight("NORTH") /2 + 1,2,(font.getHeight("NORTH") + 10)/2,1,new Color(164, 158, 158));
                font.drawStringWithShadow(NorthSingleton.INSTANCE.auth.username, 6 + font.getWidth("NorthXD"), 6, -1);
                RectUtil.drawRoundedRect(font.getWidth("NorthXDXD" + NorthSingleton.INSTANCE.auth.username),font.getHeight("NORTH") /2 + 1,2,(font.getHeight("NORTH") + 10)/2,1,new Color(164, 158, 158));



                font.drawStringWithShadow(formattedTime,  6+font.getWidth("NorthXDXD" + NorthSingleton.INSTANCE.auth.username) , 6, -1);

                GlStateManager.popMatrix();

                return new Vector2f(width,10 + font.getHeight("NORTH"));
            case "Onetap":
                int x2 = (int) FontUtil.DefaultSmall.getWidth(ChatFormatting.GRAY + "| " + ChatFormatting.WHITE + NorthSingleton.INSTANCE.getAuth().username + ChatFormatting.GRAY + " | " + ChatFormatting.WHITE + mc.getDebugFPS() + ChatFormatting.GRAY + " | " + ChatFormatting.WHITE + ServerData.serverIP + "ms");
                RectUtil.drawRoundedBloom(0, 0,x2+ 22, 20, 5,4, new Color(0,0,0,200));
                //Gui.drawRect(0,0,x2+32,21,new Color(12,12,12).getRGB());
                RectUtil.drawRoundedRect(0,0,x2+22,21,5,new Color(12,12,12));
                FontUtil.DefaultSmall.drawString("North" + ChatFormatting.GRAY + " | " + ChatFormatting.WHITE + NorthSingleton.INSTANCE.getAuth().username + ChatFormatting.GRAY + " | " + ChatFormatting.WHITE + mc.getDebugFPS() + ChatFormatting.GRAY + " | " + ChatFormatting.WHITE + ServerData.serverIP, 5f, 8.2f, -1);
                GlStateManager.popMatrix();
                return new Vector2f(x2+26,17);
        }
       return new Vector2f(0,0);
    }

    @Override
    public String getDraggableName() {
        return "Watermark";
    }

    public static class StringMode extends Mode<Watermark> {

        public StringMode(String name, AbstractModule parent) {
            super(name, parent);
        }
    }
}
