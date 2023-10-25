/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiChat
extends GuiScreen {
    private boolean playerNamesFound;
    private int sentHistoryCursor = -1;
    private int autocompleteIndex;
    protected GuiTextField inputField;
    private boolean waitingOnAutocomplete;
    private List<String> foundPlayerNames = Lists.newArrayList();
    private String defaultInputFieldText = "";
    private String historyBuffer = "";
    private static final Logger logger = LogManager.getLogger();

    public GuiChat() {
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        this.inputField = new GuiTextField(0, this.fontRendererObj, 4, height - 12, width - 4, 12);
        this.inputField.setMaxStringLength(100);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void onAutocompleteResponse(String[] stringArray) {
        if (this.waitingOnAutocomplete) {
            String string;
            this.playerNamesFound = false;
            this.foundPlayerNames.clear();
            String[] stringArray2 = stringArray;
            int n = stringArray.length;
            int n2 = 0;
            while (n2 < n) {
                string = stringArray2[n2];
                if (string.length() > 0) {
                    this.foundPlayerNames.add(string);
                }
                ++n2;
            }
            string = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
            String string2 = StringUtils.getCommonPrefix((String[])stringArray);
            if (string2.length() > 0 && !string.equalsIgnoreCase(string2)) {
                this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
                this.inputField.writeText(string2);
            } else if (this.foundPlayerNames.size() > 0) {
                this.playerNamesFound = true;
                this.autocompletePlayerNames();
            }
        }
    }

    public void autocompletePlayerNames() {
        if (this.playerNamesFound) {
            this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
            if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
                this.autocompleteIndex = 0;
            }
        } else {
            int n = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
            this.foundPlayerNames.clear();
            this.autocompleteIndex = 0;
            String string = this.inputField.getText().substring(n).toLowerCase();
            String string2 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
            this.sendAutocompleteRequest(string2, string);
            if (this.foundPlayerNames.isEmpty()) {
                return;
            }
            this.playerNamesFound = true;
            this.inputField.deleteFromCursor(n - this.inputField.getCursorPosition());
        }
        if (this.foundPlayerNames.size() > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String string : this.foundPlayerNames) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(string);
            }
            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringBuilder.toString()), 1);
        }
        this.inputField.writeText(this.foundPlayerNames.get(this.autocompleteIndex++));
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }

    public GuiChat(String string) {
        this.defaultInputFieldText = string;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        GuiChat.drawRect(2.0, height - 14, width - 2, height - 2, Integer.MIN_VALUE);
        this.inputField.drawTextBox();
        IChatComponent iChatComponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (iChatComponent != null && iChatComponent.getChatStyle().getChatHoverEvent() != null) {
            this.handleComponentHover(iChatComponent, n, n2);
        }
        super.drawScreen(n, n2, f);
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        IChatComponent iChatComponent;
        if (n3 == 0 && this.handleComponentClick(iChatComponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY()))) {
            return;
        }
        this.inputField.mouseClicked(n, n2, n3);
        super.mouseClicked(n, n2, n3);
    }

    @Override
    protected void setText(String string, boolean bl) {
        if (bl) {
            this.inputField.setText(string);
        } else {
            this.inputField.writeText(string);
        }
    }

    private void sendAutocompleteRequest(String string, String string2) {
        if (string.length() >= 1) {
            BlockPos blockPos = null;
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                blockPos = this.mc.objectMouseOver.getBlockPos();
            }
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(string, blockPos));
            this.waitingOnAutocomplete = true;
        }
    }

    public void getSentHistory(int n) {
        int n2 = this.sentHistoryCursor + n;
        int n3 = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        if ((n2 = MathHelper.clamp_int(n2, 0, n3)) != this.sentHistoryCursor) {
            if (n2 == n3) {
                this.sentHistoryCursor = n3;
                this.inputField.setText(this.historyBuffer);
            } else {
                if (this.sentHistoryCursor == n3) {
                    this.historyBuffer = this.inputField.getText();
                }
                this.inputField.setText(this.mc.ingameGUI.getChatGUI().getSentMessages().get(n2));
                this.sentHistoryCursor = n2;
            }
        }
    }

    @Override
    public void updateScreen() {
        this.inputField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        this.waitingOnAutocomplete = false;
        if (n == 15) {
            this.autocompletePlayerNames();
        } else {
            this.playerNamesFound = false;
        }
        if (n == 1) {
            this.mc.displayGuiScreen(null);
        } else if (n != 28 && n != 156) {
            if (n == 200) {
                this.getSentHistory(-1);
            } else if (n == 208) {
                this.getSentHistory(1);
            } else if (n == 201) {
                this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
            } else if (n == 209) {
                this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
            } else {
                this.inputField.textboxKeyTyped(c, n);
            }
        } else {
            String string = this.inputField.getText().trim();
            if (string.length() > 0) {
                this.sendChatMessage(string);
            }
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int n = Mouse.getEventDWheel();
        if (n != 0) {
            if (n > 1) {
                n = 1;
            }
            if (n < -1) {
                n = -1;
            }
            if (!GuiChat.isShiftKeyDown()) {
                n *= 7;
            }
            this.mc.ingameGUI.getChatGUI().scroll(n);
        }
    }
}

