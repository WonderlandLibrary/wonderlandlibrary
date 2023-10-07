package net.minecraft.client.gui;

import cc.novoline.Novoline;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.visual.HUD;
import cc.novoline.modules.visual.Radar;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.ScaleUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.*;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiChat extends GuiScreen {

    private String historyBuffer = "";
    private boolean dragTH, dragInv, dragRadar, dragTL;
    private int x2, y2, x3, y3, x4, y4, x5, y5;

    /**
     * keeps position of which chat message you will select when you press up, (does not increase for duplicated
     * messages sent immediately after each other)
     */
    private int sentHistoryCursor = -1;
    private boolean playerNamesFound;
    private boolean waitingOnAutocomplete;
    private int autocompleteIndex;
    private final List<String> foundPlayerNames = Lists.newArrayList();

    /**
     * Chat entry field
     */
    protected GuiTextField inputField;

    /**
     * is the text that appears when you press the chat key and the input box appears pre-filled
     */
    private String defaultInputFieldText = "";

    public GuiChat() {
    }

    public GuiChat(String defaultText) {
        this.defaultInputFieldText = defaultText;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        this.inputField = new GuiTextField(0, mc.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
        this.inputField.setMaxStringLength(100);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        this.inputField.updateCursorCounter();
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.waitingOnAutocomplete = false;

        if (keyCode == 15) {
            this.autocompletePlayerNames();
        } else {
            this.playerNamesFound = false;
        }

        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        } else if (keyCode != 28 && keyCode != 156) {
            switch (keyCode) {
                case 200:
                    this.getSentHistory(-1);
                    break;
                case 208:
                    this.getSentHistory(1);
                    break;
                case 201:
                    this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
                    break;
                case 209:
                    this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
                    break;
                default:
                    this.inputField.textboxKeyTyped(typedChar, keyCode);
                    break;
            }
        } else {
            String s = this.inputField.getText().trim();

            if (!s.isEmpty()) {
                this.sendChatMessage(s);
            }

            this.mc.displayGuiScreen(null);
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0) {
            if (i > 1) {
                i = 1;
            } else if (i < -1) {
                i = -1;
            }

            if (!isShiftKeyDown()) {
                i *= 7;
            }

            this.mc.ingameGUI.getChatGUI().scroll(i);
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {

            int x = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[0];
            int y = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[1];

            HUD hud = Novoline.getInstance().getModuleManager().getModule(HUD.class);
            Radar radar = Novoline.getInstance().getModuleManager().getModule(Radar.class);
            if (hover(x, y)) {
                KillAura aura = Novoline.getInstance().getModuleManager().getModule(KillAura.class);
                x2 = aura.getThx().get() - x;
                y2 = aura.getThy().get() - y;
                dragTH = true;
            } else if (hover(x, y, hud) && hud.getHudElements().get().contains("Inventory")) {
                x3 = hud.getInvx().get() - x;
                y3 = hud.getInvy().get() - y;
                dragInv = true;
            } else if (hover(x, y, radar) && radar.isEnabled()) {
                x4 = radar.getX().get() - x;
                y4 = radar.getY().get() - y;
                dragRadar = true;
            } else if (hoverTL(x, y, hud) && hud.getHudElements().get().contains("TargetsList")) {
                x5 = hud.getTlx().get() - x;
                y5 = hud.getTly().get() - y;
                dragTL = true;
            }

            final IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY() - 15);

            if (this.handleComponentClick(ichatcomponent)) {
                return;
            }


        }

        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Sets the text of the chat
     */
    protected void setText(String newChatText, boolean shouldOverwrite) {
        if (shouldOverwrite) {
            this.inputField.setText(newChatText);
        } else {
            this.inputField.writeText(newChatText);
        }
    }

    public void autocompletePlayerNames() {
        if (this.playerNamesFound) {
            this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());

            if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
                this.autocompleteIndex = 0;
            }
        } else {
            final int i = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
            this.foundPlayerNames.clear();
            this.autocompleteIndex = 0;
            final String s = this.inputField.getText().substring(i).toLowerCase();
            final String s1 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
            this.sendAutocompleteRequest(s1, s);

            if (this.foundPlayerNames.isEmpty()) {
                return;
            }

            this.playerNamesFound = true;
            this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
        }

        if (this.foundPlayerNames.size() > 1) {
            final StringBuilder stringbuilder = new StringBuilder();

            for (String s2 : this.foundPlayerNames) {
                if (stringbuilder.length() > 0) {
                    stringbuilder.append(", ");
                }

                stringbuilder.append(s2);
            }

            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
        }

        this.inputField.writeText(this.foundPlayerNames.get(this.autocompleteIndex++));
    }

    private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_) {
        if (p_146405_1_.length() >= 1) {
            BlockPos blockpos = null;

            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                blockpos = this.mc.objectMouseOver.getBlockPos();
            }

            this.mc.player.connection.sendPacket(new C14PacketTabComplete(p_146405_1_, blockpos));
            this.waitingOnAutocomplete = true;
        }
    }

    /**
     * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
     * message from the current cursor position
     */
    public void getSentHistory(int msgPos) {
        int i = this.sentHistoryCursor + msgPos;
        final int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        i = MathHelper.clamp_int(i, 0, j);

        if (i != this.sentHistoryCursor) {
            if (i == j) {
                this.sentHistoryCursor = j;
                this.inputField.setText(this.historyBuffer);
            } else {
                if (this.sentHistoryCursor == j) {
                    this.historyBuffer = this.inputField.getText();
                }

                this.inputField.setText(this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
                this.sentHistoryCursor = i;
            }
        }
    }

    public float getLength(EntityPlayer player) {
        return 35 + mc.fontRendererCrack.getStringWidth(player.getName()) + 34;
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);

        this.inputField.drawTextBox();
        final IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY() - 15);

        if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null) {
            this.handleComponentHover(ichatcomponent, mouseX, mouseY - 5);
        }

        KillAura aura = Novoline.getInstance().getModuleManager().getModule(KillAura.class);

        ScaledResolution sr = new ScaledResolution(mc);
        if (aura.getTarget() == null || aura.getTarget() instanceof EntityPlayer) {

            int x = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[0];
            int y = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[1];

            if (dragTH) {
                aura.getThx().set(MathHelper.clamp_int(x2 + x, 1, (int) (aura.getThx().getMaximum() -
                        getLength(aura.shouldAttack() || aura.shouldBlock() ? (EntityPlayer) aura.getTarget() : mc.player))));
                aura.getThy().set(MathHelper.clamp_int(y2 + y, 1, aura.getThy().getMaximum() - 42));
            }

            if (aura.getTargetHud().get() && (aura.shouldAttack() || aura.shouldBlock() || mc.player != null)) {
                RenderUtils.renderTHUD(aura, aura.shouldAttack() || aura.shouldBlock() ? (EntityPlayer) aura.getTarget() : mc.player);

                if (hover(x, y)) {

                    if (aura.getThx().get() > sr.getScaledWidthStatic(mc) - 50) {
                        aura.getThx().set(sr.getScaledWidthStatic(mc) - 50);
                    }

                    if (aura.getThy().get() > sr.getScaledHeightStatic(mc) - 50) {
                        aura.getThy().set(sr.getScaledHeightStatic(mc) - 50);
                    }

                    ScaleUtils.scale(mc);
                    RenderUtils.drawBorderedRect(aura.getThx().get(), aura.getThy().get() - 1.5f, aura.getThx().get() + getLength(aura.shouldAttack() || aura.shouldBlock() ? (EntityPlayer) aura.getTarget() : mc.player),
                            aura.getThy().get() + 37.5, 2, 0xffffffff, new Color(0, 0, 0, 150).getRGB());
                }
            }
        }

        HUD hud = Novoline.getInstance().getModuleManager().getModule(HUD.class);
        if (hud.isEnabled()) {
            if (hud.getHudElements().get().contains("Inventory")) {
                ScaleUtils.scale(mc);
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.enableDepth();
                int x = hud.getInvx().get();
                int y = hud.getInvy().get();
                Gui.drawRect(x, y, x + 167, y + 73, new Color(29, 29, 29, 255).getRGB());
                Gui.drawRect(x + 1, y + 13, x + 166, y + 72, new Color(40, 40, 40, 255).getRGB());
                mc.fontRendererCrack.drawString("Your Inventory", x + 3, y + 3, 0xffffffff, true);

                boolean hasStacks = false;
                for (int i1 = 9; i1 < mc.player.inventoryContainer.inventorySlots.size() - 9; ++i1) {
                    Slot slot = mc.player.inventoryContainer.inventorySlots.get(i1);
                    if (slot.getHasStack()) hasStacks = true;
                    int i = slot.xDisplayPosition;
                    int j = slot.yDisplayPosition;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(slot.getStack(), x + i - 4, y + j - 68);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererCrack, slot.getStack(), x + i - 4, y + j - 68, null);
                }
                if (!hasStacks) {
                    mc.fontRendererCrack.drawString("Empty...",
                            x + 167 / 2 - mc.fontRendererCrack.getStringWidth("Empty...") / 2,
                            y + 72 / 2,
                            0xffffffff,
                            true);
                }
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableDepth();

                int mouseCoordinatex = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[0];
                int mouseCoordinatey = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[1];

                if (dragInv) {
                    hud.getInvx().set(MathHelper.clamp_int(x3 + mouseCoordinatex, 1, hud.getInvx().getMaximum() - 167));
                    hud.getInvy().set(MathHelper.clamp_int(y3 + mouseCoordinatey, 1, hud.getInvy().getMaximum() - 73));
                }

                if (hover(mouseCoordinatex, mouseCoordinatey, hud)) {

                    if (hud.getInvx().get() > sr.getScaledWidthStatic(mc) - 50) {
                        hud.getInvx().set(sr.getScaledWidthStatic(mc) - 50);
                    }

                    if (hud.getInvy().get() > sr.getScaledHeightStatic(mc) - 50) {
                        hud.getInvy().set(sr.getScaledHeightStatic(mc) - 50);
                    }

                    ScaleUtils.scale(mc);
                    RenderUtils.drawBorderedRect(hud.getInvx().get(), hud.getInvy().get(), hud.getInvx().get() + 167,
                            hud.getInvy().get() + 73, 2, 0xffffffff, new Color(0, 0, 0, 150).getRGB());
                }
            }

            if (hud.getHudElements().get().contains("TargetsList")) {
                ScaleUtils.scale(mc);
                hud.renderTargetsList();

                int x = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[0];
                int y = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[1];

                if (dragTL) {
                    hud.getTlx().set(MathHelper.clamp_int(x5 + x, 1, (int) (hud.getTlx().getMaximum() - hud.getTargetListWidthAndHeight()[0])));
                    hud.getTly().set(MathHelper.clamp_int(y5 + y, 1, (int) (hud.getTly().getMaximum() - hud.getTargetListWidthAndHeight()[1])));
                }

                if (hoverTL(x, y, hud)) {
                    if (hud.getTlx().get() > sr.getScaledWidthStatic(mc) - 50) {
                        hud.getTlx().set(sr.getScaledWidthStatic(mc) - 50);
                    }

                    if (hud.getTly().get() > sr.getScaledHeightStatic(mc) - 50) {
                        hud.getTly().set(sr.getScaledHeightStatic(mc) - 50);
                    }

                    ScaleUtils.scale(mc);
                    RenderUtils.drawBorderedRect(hud.getTlx().get(), hud.getTly().get() - 13, hud.getTlx().get() + hud.getTargetListWidthAndHeight()[0],
                            hud.getTly().get() + hud.getTargetListWidthAndHeight()[1], 2, 0xffffffff, new Color(0, 0, 0, 150).getRGB());
                }
            }

        }

        Radar radar = Novoline.getInstance().getModuleManager().getModule(Radar.class);
        if (radar.isEnabled()) {
            radar.renderRadar();

            int x = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[0];
            int y = ScaleUtils.getScaledMouseCoordinates(mc, mouseX, mouseY)[1];

            if (dragRadar) {
                radar.getX().set(MathHelper.clamp_int(x4 + x, 1, radar.getX().getMaximum() - radar.getSize().get()));
                radar.getY().set(MathHelper.clamp_int(y4 + y, 1, radar.getY().getMaximum() - radar.getSize().get()));
            }

            if (hover(x, y, radar)) {

                if (radar.getX().get() > sr.getScaledWidthStatic(mc) - 50) {
                    radar.getX().set(sr.getScaledWidthStatic(mc) - 50);
                }

                if (radar.getY().get() > sr.getScaledHeightStatic(mc) - 50) {
                    radar.getY().set(sr.getScaledHeightStatic(mc) - 50);
                }

                ScaleUtils.scale(mc);
                RenderUtils.drawBorderedRect(radar.getX().get(), radar.getY().get(), radar.getX().get() + radar.getSize().get(),
                        radar.getY().get() + radar.getSize().get(), 2, 0xffffffff, new Color(0, 0, 0, 150).getRGB());
            }

        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private boolean hover(int mouseX, int mouseY) {
        KillAura aura = Novoline.getInstance().getModuleManager().getModule(KillAura.class);
        return mouseX >= aura.getThx().get() && mouseX <= aura.getThx().get() + getLength(mc.player) && mouseY >= aura.getThy().get() - 1.5 && mouseY <= aura.getThy().get() + 37.5;
    }

    private boolean hover(int mouseX, int mouseY, HUD hud) {
        return mouseX >= hud.getInvx().get() && mouseX <= hud.getInvx().get() + 167 && mouseY >= hud.getInvy().get() && mouseY <= hud.getInvy().get() + 73;
    }

    private boolean hover(int mouseX, int mouseY, Radar radar) {
        return mouseX >= radar.getX().get() && mouseX <= radar.getX().get() + radar.getSize().get() && mouseY >= radar.getY().get() && mouseY <= radar.getY().get() + radar.getSize().get();
    }

    private boolean hoverTL(int mouseX, int mouseY, HUD hud) {
        return mouseX >= hud.getTlx().get() && mouseX <= hud.getTlx().get() + hud.getTargetListWidthAndHeight()[0] && mouseY >= hud.getTly().get() - 13 && mouseY <= hud.getTly().get() + hud.getTargetListWidthAndHeight()[1];
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            dragTH = false;
            dragInv = false;
            dragRadar = false;
            dragTL = false;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    public void onAutocompleteResponse(String[] p_146406_1_) {
        if (this.waitingOnAutocomplete) {
            this.playerNamesFound = false;
            this.foundPlayerNames.clear();

            for (String s : p_146406_1_) {
                if (!s.isEmpty()) {
                    this.foundPlayerNames.add(s);
                }
            }

            final String s1 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
            final String s2 = StringUtils.getCommonPrefix(p_146406_1_);

            if (!s2.isEmpty() && !s1.equalsIgnoreCase(s2)) {
                this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
                this.inputField.writeText(s2);
            } else if (!this.foundPlayerNames.isEmpty()) {
                this.playerNamesFound = true;
                this.autocompletePlayerNames();
            }
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }
}
