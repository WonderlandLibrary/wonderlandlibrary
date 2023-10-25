/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  tv.twitch.chat.ChatUserInfo
 */
package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityList;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tv.twitch.chat.ChatUserInfo;

public abstract class GuiScreen
extends Gui
implements GuiYesNoCallback {
    public static int height;
    private static final Splitter NEWLINE_SPLITTER;
    protected RenderItem itemRender;
    protected List<GuiButton> buttonList = Lists.newArrayList();
    protected List<GuiLabel> labelList = Lists.newArrayList();
    public static int width;
    private static final Set<String> PROTOCOLS;
    private GuiButton selectedButton;
    private static final Logger LOGGER;
    protected FontRenderer fontRendererObj;
    protected Minecraft mc;
    private long lastMouseEvent;
    private URI clickedLinkURI;
    protected int eventButton;
    private int touchValue;
    public boolean allowUserInput;

    public void setWorldAndResolution(Minecraft minecraft, int n, int n2) {
        this.mc = minecraft;
        this.itemRender = minecraft.getRenderItem();
        this.fontRendererObj = Minecraft.fontRendererObj;
        width = n;
        height = n2;
        this.buttonList.clear();
        this.initGui();
    }

    protected void mouseClickMove(int n, int n2, int n3, long l) {
    }

    public void initGui() {
    }

    public void updateScreen() {
    }

    public static boolean isKeyComboCtrlC(int n) {
        return n == 46 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public void drawWorldBackground(int n) {
        if (Minecraft.theWorld != null) {
            GuiScreen.drawGradientRect(0.0, 0.0, width, height, -1072689136, -804253680);
        } else {
            this.drawBackground(n);
        }
    }

    public static boolean isKeyComboCtrlV(int n) {
        return n == 47 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public static boolean isAltKeyDown() {
        return Keyboard.isKeyDown((int)56) || Keyboard.isKeyDown((int)184);
    }

    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown((int)42) || Keyboard.isKeyDown((int)54);
    }

    public void onGuiClosed() {
    }

    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }

    public void sendChatMessage(String string, boolean bl) {
        if (bl) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(string);
        }
        Minecraft.thePlayer.sendChatMessage(string);
    }

    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        if (n3 == 0) {
            int n4 = 0;
            while (n4 < this.buttonList.size()) {
                GuiButton guiButton = this.buttonList.get(n4);
                if (guiButton.mousePressed(this.mc, n, n2)) {
                    this.selectedButton = guiButton;
                    guiButton.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(guiButton);
                }
                ++n4;
            }
        }
    }

    public void handleInput() throws IOException {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                this.handleMouseInput();
            }
        }
        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                this.handleKeyboardInput();
            }
        }
    }

    private void openWebLink(URI uRI) {
        try {
            Class<?> clazz = Class.forName("java.awt.Desktop");
            Object object = clazz.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            clazz.getMethod("browse", URI.class).invoke(object, uRI);
        }
        catch (Throwable throwable) {
            LOGGER.error("Couldn't open link", throwable);
        }
    }

    public static void setClipboardString(String string) {
        if (!StringUtils.isEmpty((CharSequence)string)) {
            try {
                StringSelection stringSelection = new StringSelection(string);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public void drawScreen(int n, int n2, float f) {
        int n3 = 0;
        while (n3 < this.buttonList.size()) {
            this.buttonList.get(n3).drawButton(this.mc, n, n2);
            ++n3;
        }
        n3 = 0;
        while (n3 < this.labelList.size()) {
            this.labelList.get(n3).drawLabel(this.mc, n, n2);
            ++n3;
        }
    }

    protected void actionPerformed(GuiButton guiButton) throws IOException {
    }

    public void handleMouseInput() throws IOException {
        int n = Mouse.getEventX() * width / this.mc.displayWidth;
        int n2 = height - Mouse.getEventY() * height / Minecraft.displayHeight - 1;
        int n3 = Mouse.getEventButton();
        if (Mouse.getEventButtonState()) {
            if (Minecraft.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }
            this.eventButton = n3;
            this.lastMouseEvent = Minecraft.getSystemTime();
            this.mouseClicked(n, n2, this.eventButton);
        } else if (n3 != -1) {
            if (Minecraft.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }
            this.eventButton = -1;
            this.mouseReleased(n, n2, n3);
        } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            long l = Minecraft.getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(n, n2, this.eventButton, l);
        }
    }

    @Override
    public void confirmClicked(boolean bl, int n) {
        if (n == 31102009) {
            if (bl) {
                this.openWebLink(this.clickedLinkURI);
            }
            this.clickedLinkURI = null;
            this.mc.displayGuiScreen(this);
        }
    }

    public static boolean isKeyComboCtrlA(int n) {
        return n == 30 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public static String getClipboardString() {
        try {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String)transferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return "";
    }

    protected void drawHoveringText(List<String> list, int n, int n2) {
        if (!list.isEmpty()) {
            int n3;
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int n4 = 0;
            for (String string : list) {
                n3 = this.fontRendererObj.getStringWidth(string);
                if (n3 <= n4) continue;
                n4 = n3;
            }
            int n5 = n + 12;
            int n6 = n2 - 12;
            n3 = 8;
            if (list.size() > 1) {
                n3 += 2 + (list.size() - 1) * 10;
            }
            if (n5 + n4 > width) {
                n5 -= 28 + n4;
            }
            if (n6 + n3 + 6 > height) {
                n6 = height - n3 - 6;
            }
            zLevel = 300.0f;
            this.itemRender.zLevel = 300.0f;
            int n7 = -267386864;
            GuiScreen.drawGradientRect(n5 - 3, n6 - 4, n5 + n4 + 3, n6 - 3, n7, n7);
            GuiScreen.drawGradientRect(n5 - 3, n6 + n3 + 3, n5 + n4 + 3, n6 + n3 + 4, n7, n7);
            GuiScreen.drawGradientRect(n5 - 3, n6 - 3, n5 + n4 + 3, n6 + n3 + 3, n7, n7);
            GuiScreen.drawGradientRect(n5 - 4, n6 - 3, n5 - 3, n6 + n3 + 3, n7, n7);
            GuiScreen.drawGradientRect(n5 + n4 + 3, n6 - 3, n5 + n4 + 4, n6 + n3 + 3, n7, n7);
            int n8 = 0x505000FF;
            int n9 = (n8 & 0xFEFEFE) >> 1 | n8 & 0xFF000000;
            GuiScreen.drawGradientRect(n5 - 3, n6 - 3 + 1, n5 - 3 + 1, n6 + n3 + 3 - 1, n8, n9);
            GuiScreen.drawGradientRect(n5 + n4 + 2, n6 - 3 + 1, n5 + n4 + 3, n6 + n3 + 3 - 1, n8, n9);
            GuiScreen.drawGradientRect(n5 - 3, n6 - 3, n5 + n4 + 3, n6 - 3 + 1, n8, n8);
            GuiScreen.drawGradientRect(n5 - 3, n6 + n3 + 2, n5 + n4 + 3, n6 + n3 + 3, n9, n9);
            int n10 = 0;
            while (n10 < list.size()) {
                String string = list.get(n10);
                this.fontRendererObj.drawStringWithShadow(string, n5, n6, -1);
                if (n10 == 0) {
                    n6 += 2;
                }
                n6 += 10;
                ++n10;
            }
            zLevel = 0.0f;
            this.itemRender.zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    public static boolean isKeyComboCtrlX(int n) {
        return n == 45 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    protected void mouseReleased(int n, int n2, int n3) {
        if (this.selectedButton != null && n3 == 0) {
            this.selectedButton.mouseReleased(n, n2);
            this.selectedButton = null;
        }
    }

    static {
        LOGGER = LogManager.getLogger();
        PROTOCOLS = Sets.newHashSet((Object[])new String[]{"http", "https"});
        NEWLINE_SPLITTER = Splitter.on((char)'\n');
    }

    public void sendChatMessage(String string) {
        this.sendChatMessage(string, true);
    }

    public void onResize(Minecraft minecraft, int n, int n2) {
        this.setWorldAndResolution(minecraft, n, n2);
    }

    public static boolean isCtrlKeyDown() {
        return Minecraft.isRunningOnMac ? Keyboard.isKeyDown((int)219) || Keyboard.isKeyDown((int)220) : Keyboard.isKeyDown((int)29) || Keyboard.isKeyDown((int)157);
    }

    protected void drawCreativeTabHoveringText(String string, int n, int n2) {
        this.drawHoveringText(Arrays.asList(string), n, n2);
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    protected void setText(String string, boolean bl) {
    }

    protected void handleComponentHover(IChatComponent iChatComponent, int n, int n2) {
        if (iChatComponent != null && iChatComponent.getChatStyle().getChatHoverEvent() != null) {
            block21: {
                HoverEvent hoverEvent = iChatComponent.getChatStyle().getChatHoverEvent();
                if (hoverEvent.getAction() == HoverEvent.Action.SHOW_ITEM) {
                    ItemStack itemStack = null;
                    try {
                        NBTTagCompound nBTTagCompound = JsonToNBT.getTagFromJson(hoverEvent.getValue().getUnformattedText());
                        if (nBTTagCompound instanceof NBTTagCompound) {
                            itemStack = ItemStack.loadItemStackFromNBT(nBTTagCompound);
                        }
                    }
                    catch (NBTException nBTException) {
                        // empty catch block
                    }
                    if (itemStack != null) {
                        this.renderToolTip(itemStack, n, n2);
                    } else {
                        this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Item!", n, n2);
                    }
                } else if (hoverEvent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
                    if (Minecraft.gameSettings.advancedItemTooltips) {
                        try {
                            NBTTagCompound nBTTagCompound = JsonToNBT.getTagFromJson(hoverEvent.getValue().getUnformattedText());
                            if (nBTTagCompound instanceof NBTTagCompound) {
                                ArrayList arrayList = Lists.newArrayList();
                                NBTTagCompound nBTTagCompound2 = nBTTagCompound;
                                arrayList.add(nBTTagCompound2.getString("name"));
                                if (nBTTagCompound2.hasKey("type", 8)) {
                                    String string = nBTTagCompound2.getString("type");
                                    arrayList.add("Type: " + string + " (" + EntityList.getIDFromString(string) + ")");
                                }
                                arrayList.add(nBTTagCompound2.getString("id"));
                                this.drawHoveringText(arrayList, n, n2);
                                break block21;
                            }
                            this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Entity!", n, n2);
                        }
                        catch (NBTException nBTException) {
                            this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Entity!", n, n2);
                        }
                    }
                } else if (hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT) {
                    this.drawHoveringText(NEWLINE_SPLITTER.splitToList((CharSequence)hoverEvent.getValue().getFormattedText()), n, n2);
                } else if (hoverEvent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
                    StatBase statBase = StatList.getOneShotStat(hoverEvent.getValue().getUnformattedText());
                    if (statBase != null) {
                        IChatComponent iChatComponent2 = statBase.getStatName();
                        ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("stats.tooltip.type." + (statBase.isAchievement() ? "achievement" : "statistic"), new Object[0]);
                        chatComponentTranslation.getChatStyle().setItalic(true);
                        String string = statBase instanceof Achievement ? ((Achievement)statBase).getDescription() : null;
                        ArrayList arrayList = Lists.newArrayList((Object[])new String[]{iChatComponent2.getFormattedText(), chatComponentTranslation.getFormattedText()});
                        if (string != null) {
                            arrayList.addAll(this.fontRendererObj.listFormattedStringToWidth(string, 150));
                        }
                        this.drawHoveringText(arrayList, n, n2);
                    } else {
                        this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid statistic/achievement!", n, n2);
                    }
                }
            }
            GlStateManager.disableLighting();
        }
    }

    public void drawBackground(int n) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(optionsBackground);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float f = 32.0f;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldRenderer.pos(0.0, height, 0.0).tex(0.0, (float)height / 32.0f + (float)n).color(64, 64, 64, 255).endVertex();
        worldRenderer.pos(width, height, 0.0).tex((float)width / 32.0f, (float)height / 32.0f + (float)n).color(64, 64, 64, 255).endVertex();
        worldRenderer.pos(width, 0.0, 0.0).tex((float)width / 32.0f, n).color(64, 64, 64, 255).endVertex();
        worldRenderer.pos(0.0, 0.0, 0.0).tex(0.0, n).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }

    protected void keyTyped(char c, int n) throws IOException {
        if (n == 1) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    protected void renderToolTip(ItemStack itemStack, int n, int n2) {
        List<String> list = itemStack.getTooltip(Minecraft.thePlayer, Minecraft.gameSettings.advancedItemTooltips);
        int n3 = 0;
        while (n3 < list.size()) {
            if (n3 == 0) {
                list.set(n3, (Object)((Object)itemStack.getRarity().rarityColor) + list.get(n3));
            } else {
                list.set(n3, (Object)((Object)EnumChatFormatting.GRAY) + list.get(n3));
            }
            ++n3;
        }
        this.drawHoveringText(list, n, n2);
    }

    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKeyState()) {
            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }
        this.mc.dispatchKeypresses();
    }

    protected boolean handleComponentClick(IChatComponent iChatComponent) {
        if (iChatComponent == null) {
            return false;
        }
        ClickEvent clickEvent = iChatComponent.getChatStyle().getChatClickEvent();
        if (GuiScreen.isShiftKeyDown()) {
            if (iChatComponent.getChatStyle().getInsertion() != null) {
                this.setText(iChatComponent.getChatStyle().getInsertion(), false);
            }
        } else if (clickEvent != null) {
            block23: {
                if (clickEvent.getAction() == ClickEvent.Action.OPEN_URL) {
                    if (!Minecraft.gameSettings.chatLinks) {
                        return false;
                    }
                    try {
                        URI uRI = new URI(clickEvent.getValue());
                        String string = uRI.getScheme();
                        if (string == null) {
                            throw new URISyntaxException(clickEvent.getValue(), "Missing protocol");
                        }
                        if (!PROTOCOLS.contains(string.toLowerCase())) {
                            throw new URISyntaxException(clickEvent.getValue(), "Unsupported protocol: " + string.toLowerCase());
                        }
                        if (Minecraft.gameSettings.chatLinksPrompt) {
                            this.clickedLinkURI = uRI;
                            this.mc.displayGuiScreen(new GuiConfirmOpenLink((GuiYesNoCallback)this, clickEvent.getValue(), 31102009, false));
                            break block23;
                        }
                        this.openWebLink(uRI);
                    }
                    catch (URISyntaxException uRISyntaxException) {
                        LOGGER.error("Can't open url for " + clickEvent, (Throwable)uRISyntaxException);
                    }
                } else if (clickEvent.getAction() == ClickEvent.Action.OPEN_FILE) {
                    URI uRI = new File(clickEvent.getValue()).toURI();
                    this.openWebLink(uRI);
                } else if (clickEvent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                    this.setText(clickEvent.getValue(), true);
                } else if (clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    this.sendChatMessage(clickEvent.getValue(), false);
                } else if (clickEvent.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
                    ChatUserInfo chatUserInfo = this.mc.getTwitchStream().func_152926_a(clickEvent.getValue());
                    if (chatUserInfo != null) {
                        this.mc.displayGuiScreen(new GuiTwitchUserMode(this.mc.getTwitchStream(), chatUserInfo));
                    } else {
                        LOGGER.error("Tried to handle twitch user but couldn't find them!");
                    }
                } else {
                    LOGGER.error("Don't know how to handle " + clickEvent);
                }
            }
            return true;
        }
        return false;
    }
}

