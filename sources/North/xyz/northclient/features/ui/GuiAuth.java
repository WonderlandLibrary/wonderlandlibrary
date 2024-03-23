package xyz.northclient.features.ui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import xyz.northclient.NorthSingleton;
import xyz.northclient.UIHook;
import xyz.northclient.auth.Auth;
import xyz.northclient.util.animations.Animation;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.GLSL;
import xyz.northclient.util.shader.RectUtil;

import java.awt.*;
import java.io.IOException;

public class GuiAuth extends GuiScreen {


    @Override
    public void initGui() {
        super.initGui();
        UIHook.SHADER_TIME = System.currentTimeMillis();

        int x = (int) ((new ScaledResolution(mc).getScaledWidth()/2.0f) - 50);
        int y = (int) (new ScaledResolution(mc).getScaledHeight()/2.0f)-20;
        username = new CustomTextField(345,mc.fontRendererObj,x,y,100,20);
        animation = new Animation();
        animation.setFromValue(-50);
    }

    public CustomTextField username;
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
        animation.animate((int) (new ScaledResolution(mc).getScaledHeight()/2.0f)-60,0.2);
        int y = (int) animation.getValue();
        RectUtil.drawBloom((int) (x-(FontUtil.SFProMediumBig.getWidth("North")/2)),y,(int)FontUtil.SFProMediumBig.getWidth("North"),10,20,Color.white);
        FontUtil.SFProMediumBig.drawString("North",x-(FontUtil.SFProMediumBig.getWidth("North")/2),y,-1);

        username.drawTextBox();



        FontUtil.DefaultSmall.drawString("Login",x-35 - (FontUtil.DefaultSmall.getStringWidth("Login")/2) + 35,y+80 + 7f,-1);
        RectUtil.drawRoundedRect(x-35,y+80,70,20,5,new Color(255,255,255,50));

        FontUtil.DefaultSmall.drawStringWithShadow(NorthSingleton.GuiText,((float) new ScaledResolution(mc).getScaledWidth() /2)-(FontUtil.DefaultSmall.getStringWidth(NorthSingleton.GuiText)/2),new ScaledResolution(mc).getScaledHeight()-FontUtil.Default.getHeight()-5,-1);

        //Cursor
        RectUtil.drawBloom(mouseX-5,mouseY-5,10,10,16,new Color(255,255,255,120));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        username.mouseClicked(mouseX,mouseY,mouseButton);
        int x = (int) ((new ScaledResolution(mc).getScaledWidth()/2.0f));
        int y = (int) animation.getValue();

        if(mouseX >= x-35 && mouseY >=y+80 && mouseX <= x-35+70 && mouseY <= y+80+20) {
            try {
                NorthSingleton.INSTANCE.auth = new Auth();
                NorthSingleton.INSTANCE.auth.start(username.getText(),Auth.hwid());
            }catch (Exception e) {
                System.exit(-1);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        username.textboxKeyTyped(typedChar,keyCode);
    }
}
