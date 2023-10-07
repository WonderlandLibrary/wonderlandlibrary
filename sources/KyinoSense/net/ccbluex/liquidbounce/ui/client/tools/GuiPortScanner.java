/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
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
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.TabUtils;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

public class GuiPortScanner
extends GuiScreen {
    private final GuiScreen prevGui;
    private GuiTextField hostField;
    private GuiTextField minPortField;
    private GuiTextField maxPortField;
    private GuiTextField threadsField;
    private GuiButton buttonToggle;
    private boolean running;
    private String status = "\u00a77Wating...";
    private String host;
    private int currentPort;
    private int maxPort;
    private int minPort;
    private int checkedPort;
    private final List<Integer> ports = new ArrayList<Integer>();

    public GuiPortScanner(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    public void func_73866_w_() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.hostField = new GuiTextField(0, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 60, 200, 20);
        this.hostField.func_146195_b(true);
        this.hostField.func_146203_f(Integer.MAX_VALUE);
        this.hostField.func_146180_a("localhost");
        this.minPortField = new GuiTextField(1, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 90, 90, 20);
        this.minPortField.func_146203_f(5);
        this.minPortField.func_146180_a(String.valueOf(1));
        this.maxPortField = new GuiTextField(2, (FontRenderer)Fonts.font40, this.field_146294_l / 2 + 10, 90, 90, 20);
        this.maxPortField.func_146203_f(5);
        this.maxPortField.func_146180_a(String.valueOf(65535));
        this.threadsField = new GuiTextField(3, (FontRenderer)Fonts.font40, this.field_146294_l / 2 - 100, 120, 200, 20);
        this.threadsField.func_146203_f(Integer.MAX_VALUE);
        this.threadsField.func_146180_a(String.valueOf(500));
        this.buttonToggle = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 95, this.running ? "Stop" : "Start");
        this.field_146292_n.add(this.buttonToggle);
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, "Back"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 155, "Export"));
        super.func_73866_w_();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        this.func_73732_a(Fonts.font40, "Port Scanner", this.field_146294_l / 2, 34, 0xFFFFFF);
        this.func_73732_a(Fonts.font35, this.running ? "\u00a77" + this.checkedPort + " \u00a78/ \u00a77" + this.maxPort : (this.status == null ? "" : this.status), this.field_146294_l / 2, this.field_146295_m / 4 + 80, 0xFFFFFF);
        this.buttonToggle.field_146126_j = this.running ? "Stop" : "Start";
        this.hostField.func_146194_f();
        this.minPortField.func_146194_f();
        this.maxPortField.func_146194_f();
        this.threadsField.func_146194_f();
        this.func_73731_b(Fonts.font40, "\u00a7c\u00a7lPorts:", 2, 2, Color.WHITE.hashCode());
        List<Integer> list = this.ports;
        synchronized (list) {
            int i = 12;
            for (Integer integer : this.ports) {
                this.func_73731_b(Fonts.font35, String.valueOf(integer), 2, i, Color.WHITE.hashCode());
                i += Fonts.font35.field_78288_b;
            }
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_146284_a(GuiButton button) throws IOException {
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
                break;
            }
            case 1: {
                if (this.running) {
                    this.running = false;
                } else {
                    int threads;
                    this.host = this.hostField.func_146179_b();
                    if (this.host.isEmpty()) {
                        this.status = "\u00a7cInvalid host";
                        return;
                    }
                    try {
                        this.minPort = Integer.parseInt(this.minPortField.func_146179_b());
                    }
                    catch (NumberFormatException e) {
                        this.status = "\u00a7cInvalid min port";
                        return;
                    }
                    try {
                        this.maxPort = Integer.parseInt(this.maxPortField.func_146179_b());
                    }
                    catch (NumberFormatException e) {
                        this.status = "\u00a7cInvalid max port";
                        return;
                    }
                    try {
                        threads = Integer.parseInt(this.threadsField.func_146179_b());
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
                                this.buttonToggle.field_146126_j = "Start";
                            }
                            catch (Exception e) {
                                this.status = "\u00a7a\u00a7l" + e.getClass().getSimpleName() + ": \u00a7c" + e.getMessage();
                            }
                        }).start();
                    }
                    this.running = true;
                }
                this.buttonToggle.field_146126_j = this.running ? "Stop" : "Start";
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
                    JOptionPane.showMessageDialog(null, "Exported successful!", "Port Scanner", 1);
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    MiscUtils.showErrorPopup("Error", "Exception class: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                }
            }
        }
        super.func_146284_a(button);
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        if (15 == keyCode) {
            TabUtils.tab(this.hostField, this.minPortField, this.maxPortField);
        }
        if (this.running) {
            return;
        }
        if (this.hostField.func_146206_l()) {
            this.hostField.func_146201_a(typedChar, keyCode);
        }
        if (this.minPortField.func_146206_l() && !Character.isLetter(typedChar)) {
            this.minPortField.func_146201_a(typedChar, keyCode);
        }
        if (this.maxPortField.func_146206_l() && !Character.isLetter(typedChar)) {
            this.maxPortField.func_146201_a(typedChar, keyCode);
        }
        if (this.threadsField.func_146206_l() && !Character.isLetter(typedChar)) {
            this.threadsField.func_146201_a(typedChar, keyCode);
        }
        super.func_73869_a(typedChar, keyCode);
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.hostField.func_146192_a(mouseX, mouseY, mouseButton);
        this.minPortField.func_146192_a(mouseX, mouseY, mouseButton);
        this.maxPortField.func_146192_a(mouseX, mouseY, mouseButton);
        this.threadsField.func_146192_a(mouseX, mouseY, mouseButton);
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public void func_73876_c() {
        this.hostField.func_146178_a();
        this.minPortField.func_146178_a();
        this.maxPortField.func_146178_a();
        this.threadsField.func_146178_a();
        super.func_73876_c();
    }

    public void func_146281_b() {
        Keyboard.enableRepeatEvents((boolean)false);
        this.running = false;
        super.func_146281_b();
    }
}

