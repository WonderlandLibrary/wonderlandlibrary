/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.tools;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.TabUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import org.lwjgl.input.Keyboard;

public class GuiPortScanner
extends WrappedGuiScreen {
    private final IGuiScreen prevGui;
    private final List<Integer> ports = new ArrayList<Integer>();
    private IGuiTextField hostField;
    private IGuiTextField minPortField;
    private IGuiTextField maxPortField;
    private IGuiTextField threadsField;
    private IGuiButton buttonToggle;
    private boolean running;
    private String status = "\u00a77Waiting...";
    private String host;
    private int currentPort;
    private int maxPort;
    private int minPort;
    private int checkedPort;

    public GuiPortScanner(IGuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.hostField = classProvider.createGuiTextField(0, Fonts.font40, this.representedScreen.getWidth() / 2 - 100, 60, 200, 20);
        this.hostField.setFocused(true);
        this.hostField.setMaxStringLength(Integer.MAX_VALUE);
        this.hostField.setText("localhost");
        this.minPortField = classProvider.createGuiTextField(1, Fonts.font40, this.representedScreen.getHeight() / 2 - 100, 90, 90, 20);
        this.minPortField.setMaxStringLength(5);
        this.minPortField.setText(String.valueOf(1));
        this.maxPortField = classProvider.createGuiTextField(2, Fonts.font40, this.representedScreen.getWidth() / 2 + 10, 90, 90, 20);
        this.maxPortField.setMaxStringLength(5);
        this.maxPortField.setText(String.valueOf(65535));
        this.threadsField = classProvider.createGuiTextField(3, Fonts.font40, this.representedScreen.getWidth() / 2 - 100, 120, 200, 20);
        this.threadsField.setMaxStringLength(Integer.MAX_VALUE);
        this.threadsField.setText(String.valueOf(500));
        this.buttonToggle = classProvider.createGuiButton(1, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 95, this.running ? "Stop" : "Start");
        this.representedScreen.getButtonList().add(this.buttonToggle);
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(0, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 120, "Back"));
        this.representedScreen.getButtonList().add(classProvider.createGuiButton(2, this.representedScreen.getWidth() / 2 - 100, this.representedScreen.getHeight() / 4 + 155, "Export"));
        super.initGui();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.representedScreen.drawBackground(0);
        Fonts.font40.drawCenteredString("Port Scanner", (float)this.representedScreen.getWidth() / 2.0f, 34.0f, 0xFFFFFF);
        Fonts.font35.drawCenteredString(this.running ? "\u00a77" + this.checkedPort + " \u00a78/ \u00a77" + this.maxPort : (this.status == null ? "" : this.status), (float)this.representedScreen.getWidth() / 2.0f, (float)this.representedScreen.getHeight() / 4.0f + 80.0f, 0xFFFFFF);
        this.buttonToggle.setDisplayString(this.running ? "Stop" : "Start");
        this.hostField.drawTextBox();
        this.minPortField.drawTextBox();
        this.maxPortField.drawTextBox();
        this.threadsField.drawTextBox();
        Fonts.font40.drawString("\u00a7c\u00a7lPorts:", 2, 2, Color.WHITE.hashCode());
        List<Integer> list = this.ports;
        synchronized (list) {
            int i = 12;
            for (Integer integer : this.ports) {
                Fonts.font35.drawString(String.valueOf(integer), 2, i, Color.WHITE.hashCode());
                i += Fonts.font35.getFontHeight();
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(IGuiButton button) throws IOException {
        switch (button.getId()) {
            case 0: {
                mc.displayGuiScreen(this.prevGui);
                break;
            }
            case 1: {
                if (this.running) {
                    this.running = false;
                } else {
                    int threads;
                    this.host = this.hostField.getText();
                    if (this.host.isEmpty()) {
                        this.status = "\u00a7cInvalid host";
                        return;
                    }
                    try {
                        this.minPort = Integer.parseInt(this.minPortField.getText());
                    }
                    catch (NumberFormatException e) {
                        this.status = "\u00a7cInvalid min port";
                        return;
                    }
                    try {
                        this.maxPort = Integer.parseInt(this.maxPortField.getText());
                    }
                    catch (NumberFormatException e) {
                        this.status = "\u00a7cInvalid max port";
                        return;
                    }
                    try {
                        threads = Integer.parseInt(this.threadsField.getText());
                    }
                    catch (NumberFormatException e) {
                        this.status = "\u00a7cInvalid threads";
                        return;
                    }
                    this.ports.clear();
                    this.currentPort = this.minPort - 1;
                    this.checkedPort = this.minPort;
                    for (int i = 0; i < threads; ++i) {
                        new Thread(() -> {
                            try {
                                while (this.running && this.currentPort < this.maxPort) {
                                    int port = ++this.currentPort;
                                    try {
                                        Socket socket = new Socket();
                                        socket.connect(new InetSocketAddress(this.host, port), 500);
                                        socket.close();
                                        List<Integer> list = this.ports;
                                        synchronized (list) {
                                            if (!this.ports.contains(port)) {
                                                this.ports.add(port);
                                            }
                                        }
                                    }
                                    catch (Exception exception) {
                                        // empty catch block
                                    }
                                    if (this.checkedPort >= port) continue;
                                    this.checkedPort = port;
                                }
                                this.running = false;
                                this.buttonToggle.setDisplayString("Start");
                            }
                            catch (Exception e) {
                                this.status = "\u00a7a\u00a7l" + e.getClass().getSimpleName() + ": \u00a7c" + e.getMessage();
                            }
                        }).start();
                    }
                    this.running = true;
                }
                this.buttonToggle.setDisplayString(this.running ? "Stop" : "Start");
                break;
            }
            case 2: {
                File selectedFile = MiscUtils.saveFileChooser();
                if (selectedFile == null || selectedFile.isDirectory()) {
                    return;
                }
                try {
                    if (!selectedFile.exists()) {
                        selectedFile.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(selectedFile);
                    fileWriter.write("Portscan\r\n");
                    fileWriter.write("Host: " + this.host + "\r\n\r\n");
                    fileWriter.write("Ports (" + this.minPort + " - " + this.maxPort + "):\r\n");
                    for (Integer integer : this.ports) {
                        fileWriter.write(integer + "\r\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    JOptionPane.showMessageDialog(null, "Exported successfully!", "Port Scanner", 1);
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                }
            }
        }
        super.actionPerformed(button);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (1 == keyCode) {
            mc.displayGuiScreen(this.prevGui);
            return;
        }
        if (15 == keyCode) {
            TabUtils.tab(this.hostField, this.minPortField, this.maxPortField);
        }
        if (this.running) {
            return;
        }
        if (this.hostField.isFocused()) {
            this.hostField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.minPortField.isFocused() && !Character.isLetter(typedChar)) {
            this.minPortField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.maxPortField.isFocused() && !Character.isLetter(typedChar)) {
            this.maxPortField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.threadsField.isFocused() && !Character.isLetter(typedChar)) {
            this.threadsField.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.hostField.mouseClicked(mouseX, mouseY, mouseButton);
        this.minPortField.mouseClicked(mouseX, mouseY, mouseButton);
        this.maxPortField.mouseClicked(mouseX, mouseY, mouseButton);
        this.threadsField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.hostField.updateCursorCounter();
        this.minPortField.updateCursorCounter();
        this.maxPortField.updateCursorCounter();
        this.threadsField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        this.running = false;
        super.onGuiClosed();
    }
}

