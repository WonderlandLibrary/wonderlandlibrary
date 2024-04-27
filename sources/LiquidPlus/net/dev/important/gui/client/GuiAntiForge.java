/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 */
package net.dev.important.gui.client;

import java.io.IOException;
import net.dev.important.Client;
import net.dev.important.modules.command.other.AntiForge;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiAntiForge
extends GuiScreen {
    private final GuiScreen prevGui;
    private GuiButton enabledButton;
    private GuiButton fmlButton;
    private GuiButton proxyButton;
    private GuiButton payloadButton;

    public GuiAntiForge(GuiScreen prevGui) {
        this.prevGui = prevGui;
    }

    public void func_73866_w_() {
        this.enabledButton = new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 35, "Enabled (" + (AntiForge.enabled ? "On" : "Off") + ")");
        this.field_146292_n.add(this.enabledButton);
        this.fmlButton = new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 50 + 25, "Block FML (" + (AntiForge.blockFML ? "On" : "Off") + ")");
        this.field_146292_n.add(this.fmlButton);
        this.proxyButton = new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 50 + 50, "Block FML Proxy Packet (" + (AntiForge.blockProxyPacket ? "On" : "Off") + ")");
        this.field_146292_n.add(this.proxyButton);
        this.payloadButton = new GuiButton(4, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 50 + 75, "Block Payload Packets (" + (AntiForge.blockPayloadPackets ? "On" : "Off") + ")");
        this.field_146292_n.add(this.payloadButton);
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 55 + 100 + 5, "Back"));
    }

    protected void func_146284_a(GuiButton button) {
        switch (button.field_146127_k) {
            case 1: {
                AntiForge.enabled = !AntiForge.enabled;
                this.enabledButton.field_146126_j = "Enabled (" + (AntiForge.enabled ? "On" : "Off") + ")";
                Client.fileManager.saveConfig(Client.fileManager.valuesConfig);
                break;
            }
            case 2: {
                AntiForge.blockFML = !AntiForge.blockFML;
                this.fmlButton.field_146126_j = "Block FML (" + (AntiForge.blockFML ? "On" : "Off") + ")";
                Client.fileManager.saveConfig(Client.fileManager.valuesConfig);
                break;
            }
            case 3: {
                AntiForge.blockProxyPacket = !AntiForge.blockProxyPacket;
                this.proxyButton.field_146126_j = "Block FML Proxy Packet (" + (AntiForge.blockProxyPacket ? "On" : "Off") + ")";
                Client.fileManager.saveConfig(Client.fileManager.valuesConfig);
                break;
            }
            case 4: {
                AntiForge.blockPayloadPackets = !AntiForge.blockPayloadPackets;
                this.payloadButton.field_146126_j = "Block Payload Packets (" + (AntiForge.blockPayloadPackets ? "On" : "Off") + ")";
                Client.fileManager.saveConfig(Client.fileManager.valuesConfig);
                break;
            }
            case 0: {
                this.field_146297_k.func_147108_a(this.prevGui);
            }
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }
}

