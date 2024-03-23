package xyz.northclient.login;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import xyz.northclient.util.shader.RectUtil;

import java.awt.*;
import java.io.IOException;
import java.util.UUID;

public final class GuiAltLogin
extends GuiScreen {
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;
    private GuiTextField token;
    private String status;

    private long init;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
        init = System.currentTimeMillis();
    }



    @Override
    public void drawScreen(int x2, int y2, float z2) {
        drawBG();

        ScaledResolution sr = new ScaledResolution(mc);
        int x = (sr.getScaledWidth()/2);
        int y = (sr.getScaledHeight()/2);

        RectUtil.drawRoundedRect(x-50,y-35,x+50,y-15,5,new Color(34,34,34));
        Gui.drawRect(0,0,0,0,0);
        mc.fontRendererObj.drawString("Login",x-50 + (50 - (mc.fontRendererObj.getStringWidth("Login")/2)),y-35 + (10 - (mc.fontRendererObj.FONT_HEIGHT/2)),-1);

        RectUtil.drawRoundedRect(x-50,y-10,x+50,y+10,5,new Color(34,34,34));
        Gui.drawRect(0,0,0,0,0);
        mc.fontRendererObj.drawString("Back",x-50 + (50 - (mc.fontRendererObj.getStringWidth("Back")/2)),y-10 + (10 - (mc.fontRendererObj.FONT_HEIGHT/2)),-1);

        this.username.drawTextBox();
        this.token.drawTextBox();
        mc.fontRendererObj.drawStringWithShadow("Alt Login", width / 2 - mc.fontRendererObj.getStringWidth("Alt Login") / 2, 20, -1);
        mc.fontRendererObj.drawStringWithShadow(status, width / 2 - mc.fontRendererObj.getStringWidth(status) / 2, 70, -1);
        if (this.username.getText().isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow("username(:password)", width / 2 - 96, 106, -7829368);
        }
        mc.fontRendererObj.drawStringWithShadow("Or with token", width / 2 - mc.fontRendererObj.getStringWidth("Or with token") / 2, 126, -1);
        if (this.token.getText().isEmpty()) {
            mc.fontRendererObj.drawStringWithShadow("username:uuid:token", width / 2 - 96, 146, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    private void drawBG() {
        Gui.drawRect(0,0,0,0,0);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 44;
        int var4 = height / 4 + 64;

        this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.token = new GuiTextField(var4, this.mc.fontRendererObj, width / 2 - 100, 140, 200, 20);
        this.token.setMaxStringLength(42069);
        this.username.setMaxStringLength(42069);
        this.username.setFocused(true);
        this.status = "Session: " + EnumChatFormatting.GREEN + mc.getSession().getUsername();
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        super.keyTyped(character, key);

        this.username.textboxKeyTyped(character, key);
        this.token.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) throws IOException {

        ScaledResolution sr = new ScaledResolution(mc);
        int x = (sr.getScaledWidth()/2);
        int y = (sr.getScaledHeight()/2);
        if(x2 > x-50 && y2 > y-35 && x2 < x+50 && y2 < y-15) {
            if (!this.username.getText().isEmpty() && this.token.getText().isEmpty()) {
                if(this.username.getText().contains(":")) {
                    try {
                        String[] args = this.username.getText().split(":");
                        this.thread = new AltLoginThread(args[0], args[1]);
                        this.status = "Session (Not sure): " + EnumChatFormatting.YELLOW + this.username.getText();
                    } catch (Exception e) {
                        this.status = "Error! " + e.getMessage();
                        e.printStackTrace();
                    }
                } else {
                    try {
                        this.thread = new AltLoginThread(this.username.getText(), "");
                        this.status = "Session (Cracked): " + EnumChatFormatting.GREEN + this.username.getText();
                    } catch (Exception e) {
                        this.status = "Error! " + e.getMessage();
                        e.printStackTrace();
                    }
                }
                this.thread.start();
            } else if (this.username.getText().isEmpty() && !this.token.getText().isEmpty()) {
                try {
                    String[] args = this.token.getText().split(":");
                    String name = args[0];
                    UUID uuid = UUID.fromString(args[1].replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
                    String token = args[2];
                    try {
                        this.mc.getSessionService().joinServer(new GameProfile(uuid, name), token, uuid.toString());
                    } catch (AuthenticationUnavailableException var7) {
                        this.status = "Authentication unavailable";
                        return;
                    } catch (InvalidCredentialsException var8) {
                        this.status = "Invalid credentials";
                        return;
                    } catch (AuthenticationException authenticationexception) {
                        this.status = "Authentication failed";
                        return;
                    }
                    this.mc.session = new Session(args[0], args[1], args[2], "mojang");
                    this.status = "Session(Not sure): " + EnumChatFormatting.YELLOW + name;
                } catch (Exception e) {
                    this.status = "Error! " + e.getMessage();
                    e.printStackTrace();
                }
            } else {
                this.status = "Error!";
            }
        }
        if(x2 > x-50 && y2 > y-10 && x2 < x+50 && y2 < y+10) {
            mc.displayGuiScreen(previousScreen);
        }

        super.mouseClicked(x2, y2, button);

        this.username.mouseClicked(x2, y2, button);
        this.token.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.token.updateCursorCounter();
    }
}

