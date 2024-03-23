package xyz.northclient.features.ui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import xyz.northclient.UIHook;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.GLSL;
import xyz.northclient.util.shader.RectUtil;

import java.awt.*;
import java.io.IOException;

public class AltManagerLogin extends GuiScreen {




    @Override
    public void initGui() {
        super.initGui();
        int x = new ScaledResolution(mc).getScaledWidth()/2;
        int y = new ScaledResolution(mc).getScaledHeight()/2;
        crackusername = new CustomTextField(345,mc.fontRendererObj,x-50,y,100,20);
        microsoftusername = new CustomTextField(345,mc.fontRendererObj,x-50,y,100,20);
        microsoftuuid = new CustomTextField(346,mc.fontRendererObj,x-50,y+40,100,20);
        microsoftaccesstoken = new CustomTextField(347,mc.fontRendererObj,x-50,y+80,100,20);

        crackusername.setMaxStringLength(42069);
        microsoftusername.setMaxStringLength(42069);
        microsoftuuid.setMaxStringLength(42069);
        microsoftaccesstoken.setMaxStringLength(42069);
    }

    public boolean microsoft = false;
    public boolean selected  = false;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GLSL.COLORS.useShader(mc.displayWidth,mc.displayHeight,mouseX,mouseY, (System.currentTimeMillis()- UIHook.SHADER_TIME)/1000f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0,0);
        GL11.glVertex2f(0,mc.displayHeight);
        GL11.glVertex2f(mc.displayWidth,mc.displayHeight);
        GL11.glVertex2f(mc.displayWidth,0);
        GL11.glEnd();
        GL20.glUseProgram(0);
        if(!selected) {
            int x = new ScaledResolution(mc).getScaledWidth()/2;
            int y = new ScaledResolution(mc).getScaledHeight()/2;
            FontUtil.Default.drawString("Select Alt Type",x - (FontUtil.Default.getStringWidth("Select Alt Type")/2),y-40,-1);

            RectUtil.drawRoundedRect(x-(70/2),y,70,20,5,new Color(255,255,255,50));
            FontUtil.DefaultSmall.drawString("Microsoft",x - (FontUtil.DefaultSmall.getStringWidth("Microsoft")/2),y+7,-1);

            RectUtil.drawRoundedRect(x-(70/2),y+40,70,20,5,new Color(255,255,255,50));
            FontUtil.DefaultSmall.drawString("Crack",x - (FontUtil.DefaultSmall.getStringWidth("Crack")/2),y+7+40,-1);
        } else if(microsoft) {
            microsoft();
        } else {
            crack();
        }
    }



    public boolean first = true;
    public CustomTextField crackusername;
    public CustomTextField microsoftusername;
    public CustomTextField microsoftuuid;
    public CustomTextField microsoftaccesstoken;

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int x = new ScaledResolution(mc).getScaledWidth()/2;
        int y = new ScaledResolution(mc).getScaledHeight()/2;

        if(selected && microsoft) {
            microsoftusername.mouseClicked(mouseX,mouseY,mouseButton);
            microsoftuuid.mouseClicked(mouseX,mouseY,mouseButton);
            microsoftaccesstoken.mouseClicked(mouseX,mouseY,mouseButton);

            if(mouseX > x-(70/2) && mouseY > y+120 && mouseX < x-(70/2)+70 && mouseY < y+20+120) {
                AltManager.addAlt(new AltManager.Alt(AltManager.AltType.MICROSOFT,microsoftusername.getText(),microsoftuuid.getText().replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"),microsoftaccesstoken.getText(),null));
                mc.displayGuiScreen(new AltManager());
            }
        } else if(selected) {
            crackusername.mouseClicked(mouseX,mouseY,mouseButton);

            if(mouseX > x-(70/2) && mouseY > y+40 && mouseX < x-(70/2)+70 && mouseY < y+20+40) {
                AltManager.addAlt(new AltManager.Alt(AltManager.AltType.CRACK,crackusername.getText(),null,null,null));
                mc.displayGuiScreen(new AltManager());
            }
        }

        if(!selected) {
            if(mouseX > x-(70/2) && mouseY > y && mouseX < x-(70/2)+70 && mouseY < y+20) {
                microsoft = true;
                selected = true;
            }

            if(mouseX > x-(70/2) && mouseY > y+40 && mouseX < x-(70/2)+70 && mouseY < y+20+40) {
                microsoft = false;
                selected = true;
            }
        }

    }

    public void microsoft() {
        int x = new ScaledResolution(mc).getScaledWidth()/2;
        int y = new ScaledResolution(mc).getScaledHeight()/2;
        if(first) {
            first = false;
        }

        FontUtil.DefaultSmall.drawString("Username",x-48,y-9,-1);
        microsoftusername.drawTextBox();

        FontUtil.DefaultSmall.drawString("UUID",x-48,y-9+40,-1);
        microsoftuuid.drawTextBox();

        FontUtil.DefaultSmall.drawString("Access Token",x-48,y-9+80,-1);
        microsoftaccesstoken.drawTextBox();

        RectUtil.drawRoundedRect(x-(70/2),y+120,70,20,5,new Color(255,255,255,50));
        FontUtil.DefaultSmall.drawString("Add",x - (FontUtil.DefaultSmall.getStringWidth("Add")/2),y+7+120,-1);
    }

    public void crack() {
        int x = new ScaledResolution(mc).getScaledWidth()/2;
        int y = new ScaledResolution(mc).getScaledHeight()/2;
        if(first) {
            first = false;
        }

        FontUtil.DefaultSmall.drawString("Username",x-48,y-9,-1);
        crackusername.drawTextBox();

        RectUtil.drawRoundedRect(x-(70/2),y+40,70,20,5,new Color(255,255,255,50));
        FontUtil.DefaultSmall.drawString("Add",x - (FontUtil.DefaultSmall.getStringWidth("Add")/2),y+7+40,-1);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if(microsoft) {
            microsoftusername.textboxKeyTyped(typedChar,keyCode);
            microsoftuuid.textboxKeyTyped(typedChar,keyCode);
            microsoftaccesstoken.textboxKeyTyped(typedChar,keyCode);
        } else {
            crackusername.textboxKeyTyped(typedChar, keyCode);
        }
    }
}
