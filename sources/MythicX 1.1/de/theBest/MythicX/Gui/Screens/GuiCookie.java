package de.theBest.MythicX.Gui.Screens;





import de.theBest.MythicX.utils.auth.cookie.Account;
import de.theBest.MythicX.utils.auth.cookie.MicrosoftLogin;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GuiCookie extends GuiScreen {



    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.id == 0) {


            try {
                MicrosoftLogin.getRefreshToken(refreshToken -> {
                    System.out.println("Token: " + refreshToken);
                    if (refreshToken != null) {
                        System.out.println("Token: " + refreshToken);
                        new Thread(() -> {
                            MicrosoftLogin.LoginData loginData = loginWithRefreshToken(refreshToken);
                            Account account = new Account(loginData.username, "************");
                            account.setUsername(loginData.username);
                            account.setRefreshToken(loginData.newRefreshToken);
                        }).start();
                    }
                });
            } catch (Exception e) {

            }

        }
    }

    @Override
    public void drawScreen(final int x2, final int y2, final float z2) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 50 - 10, this.height / 2 - 20, 120, 20, I18n.format("Login (Cookie)", new Object[0])));

    }




    @Override
    protected void mouseClicked(final int x2, final int y2, final int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onGuiClosed() {
        mc.entityRenderer.loadEntityShader(null);
        Keyboard.enableRepeatEvents(false);
    }


    private MicrosoftLogin.LoginData loginWithRefreshToken(String refreshToken) {
        final MicrosoftLogin.LoginData loginData = MicrosoftLogin.login(refreshToken);
        mc.session = new Session(loginData.username, loginData.uuid, loginData.mcToken, "microsoft");
        return loginData;
    }


}