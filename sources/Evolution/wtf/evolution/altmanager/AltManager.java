package wtf.evolution.altmanager;

import com.github.javafaker.Faker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;


import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static wtf.evolution.command.impl.BotCommand.getRandomString;

public class AltManager extends GuiScreen {
public boolean login;
public String loginString = "";

public Session ss = new Session(RandomStringUtils.randomAlphabetic(5));
public double mouseX1;

public double yAnimated;




public static ArrayList<Session> sessions = new ArrayList<>();

@Override
public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.drawDefaultBackground();
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    RenderUtil.drawRect(0,0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(24, 24, 25, 255).getRGB());
    RenderUtil.drawBlurredShadow(45, 85, 120, 30, 15, Color.BLACK);
    RoundedUtil.drawRound(45, 85, 120, 30, 3, new Color(19, 19, 19));

    Fonts.RUB16.drawString("Login", 55, 90, new Color(103, 103, 103, 255).getRGB());
    RoundedUtil.drawRound(50, 100, 110, 10, 2, new Color(34, 34, 35, 255));
    Fonts.RUB16.drawString(loginString + (login ? (System.currentTimeMillis() % 1000 >= 500 ? "" : "_") : ""), 55, 103, new Color(103, 103, 103, 255).getRGB());
    RenderUtil.drawBlurredShadow(500, 50, 400, 400, 15, Color.BLACK);
    GL11.glPushMatrix();

    GL11.glEnable(GL11.GL_SCISSOR_TEST);
    RenderUtil.prepareScissorBox(500, 50, 500 + 400, 50 + 400);
    RoundedUtil.drawRound(500, 50, 400, 400, 10, new Color(34, 34, 35, 255));
    mouseX1 += Integer.compare(Mouse.getDWheel(), 0) * 15;
    double x = 505;
    double y = 60 + mouseX1;
    int count = 0;
    for (Session s : sessions) {
        if (s == ss)
            RenderUtil.drawBlurredShadow((float) x, (float) y, 90, 30, 15, Color.BLACK);
        RoundedUtil.drawRound((float) x, (float) y, 90, 30, 5, new Color(50, 50, 50, 255));
        Fonts.RUB16.drawString(s.nick, (float) x + 85 - Fonts.RUB16.getStringWidth(s.nick), (float) y + 5, new Color(103, 103, 103, 255).getRGB());
        Fonts.RUB16.drawString(s == ss ? "Joined." : "", (float) x + 85 - Fonts.RUB16.getStringWidth("Joined."), (float) y + 15, new Color(255, 255, 255, 200).getRGB());
        RenderUtil.downloadImage("https://minotar.net/avatar/" + s.nick + "/100.png", (float) x + 3, (float) y + 3, 23, 23);
        if (x >= sr.getScaledWidth() - 200) {
            x = 505;
            y += 40;

            count ++;


        } else {
            x += 100;
        }


    }
    if (mouseX1 > count) {
        mouseX1 = count;
    }
    if (mouseX1 < -y * count / 10) {
        mouseX1 = -y * count / 10;
    }




    GL11.glDisable(GL11.GL_SCISSOR_TEST);
    GL11.glPopMatrix();
    Fonts.RUB16.drawCenteredString(sessions.size() + " - alts.", sr.getScaledWidth() / 2, sr.getScaledHeight() - 30, new Color(103, 103, 103, 255).getRGB());
    Fonts.RUB12.drawCenteredString("press enter to generate alt.", sr.getScaledWidth() / 2, sr.getScaledHeight() - 20, new Color(103, 103, 103, 255).getRGB());
    Fonts.RUB12.drawCenteredString("logined at " + ss.nick + ".", sr.getScaledWidth() / 2, sr.getScaledHeight() - 10, new Color(103, 103, 103, 255).getRGB());
}
public final static File file = new File(Minecraft.getMinecraft().gameDir, "evolution"+File.separator+"alts.txt");

public AltManager() {
    try {
        if(!file.exists())  return;
        FileReader fr= new FileReader(file);
        Scanner scan = new Scanner(fr);



        while (scan.hasNextLine()) {
            String[] line = scan.nextLine().split(":");
            if (sessions.contains(new Session(line[0]))) continue;
            sessions.add(new Session(line[0]));
        }
    } catch (Exception ex) {

    }
    ss.session();
}



@Override
protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    double x = 505;
    double y = 60 + mouseX1;
    for (Session s : sessions) {
        if (RenderUtil.isHovered(mouseX, mouseY, (float) x, (float) y, 90, 30) && mouseButton == 1) {
            sessions.remove(s);

            break;
        }
        if (RenderUtil.isHovered(mouseX, mouseY, (float) x, (float) y, 90, 30) && mouseButton == 0) {
            ss = s;
            s.session();
        }
        if (x >= sr.getScaledWidth() - 200) {
            x = 505;
            y += 40;
        } else {
            x += 100;
        }
    }



    if (RenderUtil.isHovered(mouseX, mouseY, 50, 100, 110, 10)) {
        this.login = !this.login;
    }
}

@Override
protected void keyTyped(char typedChar, int keyCode) throws IOException {
    super.keyTyped(typedChar, keyCode);
    if (keyCode == Keyboard.KEY_RETURN) {
        if (loginString.length() > 0) {
            Session s = new Session(this.loginString);
            ss = s;
            sessions.add(s);

            this.loginString = "";
            this.login = false;
        }
        else {
            Session s = new Session(new Faker().name().firstName().toLowerCase() + getRandomString(MathHelper.getRandomNumberBetween(3, 5)));
            ss = s;
            sessions.add(s);

            this.loginString = "";
            this.login = false;
        }
    }
    if (login) {
        if (keyCode == Keyboard.KEY_BACK) {
            if (loginString.length() > 0) {
                loginString = loginString.substring(0, loginString.length() - 1);
            }
        }
        this.loginString += ChatAllowedCharacters.filterAllowedCharacters(String.valueOf(typedChar));
    }
}
}
