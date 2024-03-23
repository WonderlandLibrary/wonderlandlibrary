package xyz.northclient.features.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import xyz.northclient.NorthSingleton;
import xyz.northclient.UIHook;
import xyz.northclient.login.GuiAltLogin;
import xyz.northclient.util.MicroshitLogin;
import xyz.northclient.util.animations.Animation;
import xyz.northclient.util.animations.util.Easings;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.GLSL;
import xyz.northclient.util.shader.RectUtil;

import java.awt.*;
import java.io.IOException;

public class GuiMainMenuNew extends GuiScreen implements GuiYesNoCallback
{

    @Override
    public void initGui() {
        super.initGui();
        animation = new Animation();
        NorthSingleton.INSTANCE.getDiscordRP().update("Playing as " + NorthSingleton.INSTANCE.getAuth().username + " | " + NorthSingleton.INSTANCE.getAuth().uid,"Main menu");
    }

    public Animation animation;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GLSL.COLORS.useShader(mc.displayWidth,mc.displayHeight,mouseX,mouseY, (System.currentTimeMillis()-UIHook.SHADER_TIME)/1000f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0,0);
        GL11.glVertex2f(0,mc.displayHeight);
        GL11.glVertex2f(mc.displayWidth,mc.displayHeight);
        GL11.glVertex2f(mc.displayWidth,0);
        GL11.glEnd();
        GL20.glUseProgram(0);

        int x = (int) ((new ScaledResolution(mc).getScaledWidth()/2.0f));
        animation.update();
        animation.animate((int) (new ScaledResolution(mc).getScaledHeight()/2.0f),0.2);
        int y = (int) animation.getValue();

        RectUtil.drawBloom((int) (x-(FontUtil.SFProMediumBig.getWidth("North")/2)),y-90,(int)FontUtil.SFProMediumBig.getWidth("North"),10,20,Color.white);
        FontUtil.SFProMediumBig.drawString("North",x-(FontUtil.SFProMediumBig.getWidth("North")/2),y-90,-1);

        RectUtil.drawRoundedRect(x-100,y-50,200,190,10,new Color(255,255,255,50));

        renderButton(x-90,y-40,"SINGLEPLAYER",mouseX,mouseY);
        renderButton(x-90,y-5,"MULTIPLAYER",mouseX,mouseY);
        renderButton(x-90,y+30,"ALT MANAGER",mouseX,mouseY);
        renderButton(x-90,y+65,"OPTIONS",mouseX,mouseY);

        renderButton(x-90,y+100,"LOGGED AS " + NorthSingleton.INSTANCE.auth.username.toUpperCase() + " | " + NorthSingleton.INSTANCE.auth.uid,mouseX,mouseY);


        RectUtil.drawBloom(mouseX-5,mouseY-5,10,10,16,new Color(255,255,255,120));

        FontUtil.DefaultSmall.drawStringWithShadow(NorthSingleton.GuiText,((float) new ScaledResolution(mc).getScaledWidth() /2)-(FontUtil.DefaultSmall.getStringWidth(NorthSingleton.GuiText)/2),new ScaledResolution(mc).getScaledHeight()-FontUtil.Default.getHeight()-5,-1);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int x = (int) ((new ScaledResolution(mc).getScaledWidth()/2.0f));
        int y = (int) animation.getValue();


        if(checkButton(mouseX,mouseY,x-90,y-40)) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if(checkButton(mouseX,mouseY,x-90,y-5)) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if(checkButton(mouseX,mouseY,x-90,y+30)) {
            mc.displayGuiScreen(new AltManager());
        }

        if(checkButton(mouseX,mouseY,x-90,y+65)) {
            mc.displayGuiScreen(new GuiOptions(this,mc.gameSettings));
        }

    }

    public boolean checkButton(int mouseX,int mouseY,int x, int y) {
        return mouseX >= x && mouseY >= y && mouseX <= x+180 && mouseY <= y+30;
    }


    public void renderButton(int x, int y, String text, int mouseX,int mouseY) {
        RectUtil.drawRoundedRect(x,y,180,30,6,new Color(255,255,255,50));
        FontUtil.DefaultSmall.drawString(text,x + 90 - (FontUtil.DefaultSmall.getStringWidth(text)/2),y + 15 - (FontUtil.DefaultSmall.getHeight()/2),new Color(255,255,255,checkButton(mouseX,mouseY,x,y) ? 120 : 80));
    }
}
