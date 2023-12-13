package wtf.diablo.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import wtf.diablo.gui.mainMenu.MainMenuButton;
import wtf.diablo.utils.chat.NameUtil;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.glsl.GLSLSandboxShader;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.util.ArrayList;

public final class GuiAltManager extends GuiScreen {
    private GuiTextField password;
    private long initTime = System.currentTimeMillis();
    private final GuiScreen previousScreen;
    private GuiTextField username;
    private AltLoginThread thread;
    private String crackedStatus;
    private GLSLSandboxShader backgroundShader;
    private boolean generated = false;
    public ArrayList<MainMenuButton> buttons = new ArrayList<MainMenuButton>();

    public GuiAltManager(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
        try {
            this.backgroundShader = new GLSLSandboxShader("/assets/minecraft/diablo/shader/background.fsh");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void drawScreen(int x, int y2, float z) {
        final FontRenderer font = mc.fontRendererObj;
        ScaledResolution scaledResolution = new ScaledResolution(mc);


        GlStateManager.enableAlpha();
        GlStateManager.disableCull();

        try {
            this.backgroundShader.useShader(width, height, x, y2, (System.currentTimeMillis() - initTime) / 1000f);
        } catch (Exception e){
            this.drawDefaultBackground();
        }

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        // Unbind shader
        GL20.glUseProgram(0);


        username.drawTextBox();
        password.drawTextBox();
        password.setMaxStringLength(128);

        Fonts.Arial18.drawCenteredString("Account Login", (int) (width / 2.0F), 18, -1);
        if (!generated) { Fonts.Arial18.drawCenteredStringWithShadow("Currently logged in as §b" + mc.getSession().getUsername(), (int) (width / 2F), 39, -7829368);}
        Fonts.Arial18.drawCenteredString(thread == null ? (crackedStatus == null ? EnumChatFormatting.GRAY + "Idle" : EnumChatFormatting.GREEN + crackedStatus) : thread.getStatus(), (int) (width / 2.0F), 29, -1);
        if (username.getText().isEmpty()) {
            font.drawStringWithShadow("Username", width / 2.0F - 96, 66, -7829368);
        }
        if (password.getText().isEmpty()) {
            font.drawStringWithShadow("Password", width / 2.0F - 96, 106, -7829368);
        }
        for(MainMenuButton but : buttons){
            but.drawScreen(x,y2, this.width > 500);
        }
        super.drawScreen(x, y2, z);
    }

    @Override
    public void initGui() {
        buttons.clear();
        addButtons(height - 60);
        initTime = System.currentTimeMillis();
    }

    private void addButtons(int y) {
        double number = (width /4f) - 8;
        int var3 = height / 4 + 24;
        username = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        password = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        username.setFocused(true);
        Keyboard.enableRepeatEvents(true);


        buttons.add(new MainMenuButton(0,"Login", 10, y, number,40));
        buttons.add(new MainMenuButton(3,"Generate Cracked", 15 + number, y, number,40));
        buttons.add(new MainMenuButton(2,"Clipboard", 20 + number * 2, y, number,40));
        buttons.add(new MainMenuButton(1,"Cancel", 25 + number * 3, y, number,40));
    }


    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!username.isFocused() && !password.isFocused()) {
                username.setFocused(true);
            } else {
                username.setFocused(password.isFocused());
                password.setFocused(!username.isFocused());
            }
        }
        if (character == '\r') {
            actionPerformed(buttons.get(0));
        }
        username.textboxKeyTyped(character, key);
        password.textboxKeyTyped(character, key);
    }
    protected void actionPerformed(MainMenuButton button) {
        try {
            generated = true;
            switch (button.id) {
                case 1:
                    mc.displayGuiScreen(previousScreen);
                    break;
                case 0:
                    thread = new AltLoginThread(username.getText(), password.getText());
                    thread.start();
                    break;
                case 2:
                    String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (!data.contains(":")) break;
                    String[] credentials = data.split(":");
                    username.setText(credentials[0]);
                    password.setText(credentials[1]);
                    break;
                case 3:
                    thread = null;
                    thread = new AltLoginThread(NameUtil.generateName(), "");
                    thread.start();
                    break;
                default:
                    break;
            }
        } catch (Throwable ignored) {
        }
        //REMOVE ME LATER: throw new RuntimeException();
    }

    @Override
    protected void mouseClicked(int x, int y2, int button) {
        try {
            if(button == 0)
                for(MainMenuButton but : buttons){
                    if(but.mouseClicked(x,y2)){
                        actionPerformed(but);
                    }
                }
            super.mouseClicked(x, y2, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        username.mouseClicked(x, y2, button);
        password.mouseClicked(x, y2, button);
    }


    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        username.updateCursorCounter();
        password.updateCursorCounter();
    }

    @SuppressWarnings("all")
    public String getCrackedStatus() {
        return this.crackedStatus;
    }

    @SuppressWarnings("all")
    public void setCrackedStatus(final String crackedStatus) {
        this.crackedStatus = crackedStatus;
    }
}
