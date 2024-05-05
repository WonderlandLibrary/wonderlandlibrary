package me.aidanmees.trivia.client.alts;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.lwjgl.input.Keyboard;

import com.mojang.authlib.exceptions.AuthenticationException;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.tools.FontUtils;
import me.aidanmees.trivia.client.tools.Timer1;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.CustomButton.GuiButtonDark;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;

public class GuiAltChecker
extends GuiScreen {
    private GuiScreen prevMenu;
    public static GuiTextField ServerIP;
    public static ArrayList<String> alts;
    public static ArrayList<String> workingalts;
    private ServerData selectedServer;
    private String serverToConnectTo = null;
    FontUtils fu_default = new FontUtils("Audiowide", Font.PLAIN, 50);
    FontUtils fu_default2 = new FontUtils("Audiowide", Font.PLAIN, 25);
    public static boolean wasLoginSuccessfull;
    Timer1 time = new Timer1();

    static {
        alts = new ArrayList();
        workingalts = new ArrayList();
        wasLoginSuccessfull = false;
    }

    public GuiAltChecker(GuiScreen parent) {
        this.prevMenu = parent;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        ServerIP = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, width / 2 - 100, height / 2 - 30, 200, 20);
        ServerIP.setMaxStringLength(250);
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height - 22, "Back"));
        this.buttonList.add(new GuiButton(2, width / 2 - 100, height - 42, "Choose File"));
        this.buttonList.add(new GuiButton(3, width / 2 - 100, height - 82, "Check"));
        this.buttonList.add(new GuiButton(4, width / 2 - 100, height - 62, "Save"));
        Keyboard.enableRepeatEvents((boolean)true);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        JFileChooser chooser;
        if (button.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
        }
        if (button.id == 2 && (chooser = new JFileChooser()).showOpenDialog(null) == 0) {
            Scanner scanner = new Scanner(new File(chooser.getSelectedFile().getPath()));
            while (scanner.hasNextLine()) {
                alts.add(scanner.nextLine());
            }
            scanner.close();
        }
        if (button.id == 3) {
            this.serverToConnectTo = ServerIP.getText();
            for (String alt2check : alts) {
                String email = alt2check.split(":")[0];
                String pw = alt2check.split(":")[1];
                try {
					Login.login(email, pw);
				} catch (AuthenticationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                this.selectedServer = new ServerData("", this.serverToConnectTo, true);
                this.connectToServer(this.selectedServer);
                if (!wasLoginSuccessfull) continue;
                workingalts.add(alt2check);
                wasLoginSuccessfull = false;
                Minecraft.getMinecraft().displayGuiScreen(new GuiAltChecker(this));
            }
        }
        if (button.id == 4) {
            for (String checkedalt : workingalts) {
                File f = new File(String.valueOf(System.getProperty("user.home")) + "/Desktop" + "CheckedAlts.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File(String.valueOf(System.getProperty("user.home")) + "/Desktop" + "CheckedAlts.txt")));
                bw.write(checkedalt);
                bw.write("\r\n");
                bw.close();
            }
        }
    }

    public void connectToServer(ServerData server) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(this, Minecraft.getMinecraft(), server));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        ServerIP.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ServerIP.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        ServerIP.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	
        Minecraft.getMinecraft().getTextureManager().bindTexture(trivia.triviaImage2);
        this.drawTexturedModalRect(0, 0, 0, 0, width, height);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        GuiAltChecker.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, width, height, width, height, width, height);
        GuiAltChecker.drawRect(width / 2 - 120, 0.0, width / 2 + 120, height, Integer.MIN_VALUE);
        GuiAltChecker.drawRect((float)(width / 2) - fu_default.getStringWidth("Account Checker") / 2.0f - 6.0f, 0.0, (float)(width / 2) + fu_default.getStringWidth("Alt-Checker") / 2.0f + 6.0f, 19.0, 0x0ffffff);
       
        fu_default.drawString("Account Checker", (float)(width / 2 + 1) - fu_default.getStringWidth("Account Checker") / 2.0f, 2.0f, 0x0ffffff);
        fu_default2.drawString("Total Alts: " + alts.size(), (float)(width / 1) - 100, 15.0f, 0x0ffffff);
        fu_default2.drawString("Working Alts: " + workingalts.size() + "/" + alts.size(), (float)(width / 1) - 105, 1.1f, 0x0ffffff);
        ServerIP.drawTextBox();
        
        	fu_default.drawString("ServerIP", sr.getScaledWidth() / 2 - 26, height / 4 + 10 , 0x0ffffff);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

