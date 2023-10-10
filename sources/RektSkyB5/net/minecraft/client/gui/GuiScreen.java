/*
 * Decompiled with CFR 0.152.
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
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tk.rektsky.gui.GuiManager;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.rektsky.GuiBlur;
import tv.twitch.chat.ChatUserInfo;

public abstract class GuiScreen
extends Gui
implements GuiYesNoCallback {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<String> PROTOCOLS = Sets.newHashSet("http", "https");
    private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
    protected Minecraft mc;
    protected RenderItem itemRender;
    public int width;
    public int height;
    protected List<GuiButton> buttonList = Lists.newArrayList();
    protected List<GuiLabel> labelList = Lists.newArrayList();
    public boolean allowUserInput;
    protected FontRenderer fontRendererObj;
    private GuiButton selectedButton;
    private int eventButton;
    private long lastMouseEvent;
    private int touchValue;
    private URI clickedLinkURI;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (int i2 = 0; i2 < this.buttonList.size(); ++i2) {
            this.buttonList.get(i2).drawButton(this.mc, mouseX, mouseY);
        }
        for (int j2 = 0; j2 < this.labelList.size(); ++j2) {
            this.labelList.get(j2).drawLabel(this.mc, mouseX, mouseY);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
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

    public static void setClipboardString(String copyText) {
        if (!StringUtils.isEmpty(copyText)) {
            try {
                StringSelection stringselection = new StringSelection(copyText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    protected void renderToolTip(ItemStack stack, int x2, int y2) {
        List<String> list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            if (i2 == 0) {
                list.set(i2, (Object)((Object)stack.getRarity().rarityColor) + list.get(i2));
                continue;
            }
            list.set(i2, (Object)((Object)EnumChatFormatting.GRAY) + list.get(i2));
        }
        this.drawHoveringText(list, x2, y2);
    }

    protected void drawCreativeTabHoveringText(String tabName, int mouseX, int mouseY) {
        this.drawHoveringText(Arrays.asList(tabName), mouseX, mouseY);
    }

    protected void drawHoveringText(List<String> textLines, int x2, int y2) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i2 = 0;
            for (String s2 : textLines) {
                int j2 = this.fontRendererObj.getStringWidth(s2);
                if (j2 <= i2) continue;
                i2 = j2;
            }
            int l1 = x2 + 12;
            int i22 = y2 - 12;
            int k2 = 8;
            if (textLines.size() > 1) {
                k2 += 2 + (textLines.size() - 1) * 10;
            }
            if (l1 + i2 > this.width) {
                l1 -= 28 + i2;
            }
            if (i22 + k2 + 6 > this.height) {
                i22 = this.height - k2 - 6;
            }
            this.zLevel = 300.0f;
            this.itemRender.zLevel = 300.0f;
            int l2 = -267386864;
            this.drawGradientRect(l1 - 3, i22 - 4, l1 + i2 + 3, i22 - 3, l2, l2);
            this.drawGradientRect(l1 - 3, i22 + k2 + 3, l1 + i2 + 3, i22 + k2 + 4, l2, l2);
            this.drawGradientRect(l1 - 3, i22 - 3, l1 + i2 + 3, i22 + k2 + 3, l2, l2);
            this.drawGradientRect(l1 - 4, i22 - 3, l1 - 3, i22 + k2 + 3, l2, l2);
            this.drawGradientRect(l1 + i2 + 3, i22 - 3, l1 + i2 + 4, i22 + k2 + 3, l2, l2);
            int i1 = 0x505000FF;
            int j1 = (i1 & 0xFEFEFE) >> 1 | i1 & 0xFF000000;
            this.drawGradientRect(l1 - 3, i22 - 3 + 1, l1 - 3 + 1, i22 + k2 + 3 - 1, i1, j1);
            this.drawGradientRect(l1 + i2 + 2, i22 - 3 + 1, l1 + i2 + 3, i22 + k2 + 3 - 1, i1, j1);
            this.drawGradientRect(l1 - 3, i22 - 3, l1 + i2 + 3, i22 - 3 + 1, i1, i1);
            this.drawGradientRect(l1 - 3, i22 + k2 + 2, l1 + i2 + 3, i22 + k2 + 3, j1, j1);
            for (int k1 = 0; k1 < textLines.size(); ++k1) {
                String s1 = textLines.get(k1);
                this.fontRendererObj.drawStringWithShadow(s1, l1, i22, -1);
                if (k1 == 0) {
                    i22 += 2;
                }
                i22 += 10;
            }
            this.zLevel = 0.0f;
            this.itemRender.zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    protected void handleComponentHover(IChatComponent p_175272_1_, int p_175272_2_, int p_175272_3_) {
        if (p_175272_1_ != null && p_175272_1_.getChatStyle().getChatHoverEvent() != null) {
            block21: {
                HoverEvent hoverevent = p_175272_1_.getChatStyle().getChatHoverEvent();
                if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
                    ItemStack itemstack = null;
                    try {
                        NBTTagCompound nbtbase = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
                        if (nbtbase instanceof NBTTagCompound) {
                            itemstack = ItemStack.loadItemStackFromNBT(nbtbase);
                        }
                    }
                    catch (NBTException nbtbase) {
                        // empty catch block
                    }
                    if (itemstack != null) {
                        this.renderToolTip(itemstack, p_175272_2_, p_175272_3_);
                    } else {
                        this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Item!", p_175272_2_, p_175272_3_);
                    }
                } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
                    if (this.mc.gameSettings.advancedItemTooltips) {
                        try {
                            NBTTagCompound nbtbase1 = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
                            if (nbtbase1 instanceof NBTTagCompound) {
                                ArrayList<String> list1 = Lists.newArrayList();
                                NBTTagCompound nbttagcompound = nbtbase1;
                                list1.add(nbttagcompound.getString("name"));
                                if (nbttagcompound.hasKey("type", 8)) {
                                    String s2 = nbttagcompound.getString("type");
                                    list1.add("Type: " + s2 + " (" + EntityList.getIDFromString(s2) + ")");
                                }
                                list1.add(nbttagcompound.getString("id"));
                                this.drawHoveringText(list1, p_175272_2_, p_175272_3_);
                                break block21;
                            }
                            this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Entity!", p_175272_2_, p_175272_3_);
                        }
                        catch (NBTException var10) {
                            this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Entity!", p_175272_2_, p_175272_3_);
                        }
                    }
                } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
                    this.drawHoveringText(NEWLINE_SPLITTER.splitToList(hoverevent.getValue().getFormattedText()), p_175272_2_, p_175272_3_);
                } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
                    StatBase statbase = StatList.getOneShotStat(hoverevent.getValue().getUnformattedText());
                    if (statbase != null) {
                        IChatComponent ichatcomponent = statbase.getStatName();
                        ChatComponentTranslation ichatcomponent1 = new ChatComponentTranslation("stats.tooltip.type." + (statbase.isAchievement() ? "achievement" : "statistic"), new Object[0]);
                        ichatcomponent1.getChatStyle().setItalic(true);
                        String s1 = statbase instanceof Achievement ? ((Achievement)statbase).getDescription() : null;
                        ArrayList<String> list = Lists.newArrayList(ichatcomponent.getFormattedText(), ichatcomponent1.getFormattedText());
                        if (s1 != null) {
                            list.addAll(this.fontRendererObj.listFormattedStringToWidth(s1, 150));
                        }
                        this.drawHoveringText(list, p_175272_2_, p_175272_3_);
                    } else {
                        this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid statistic/achievement!", p_175272_2_, p_175272_3_);
                    }
                }
            }
            GlStateManager.disableLighting();
        }
    }

    protected void setText(String newChatText, boolean shouldOverwrite) {
    }

    protected boolean handleComponentClick(IChatComponent p_175276_1_) {
        if (p_175276_1_ == null) {
            return false;
        }
        ClickEvent clickevent = p_175276_1_.getChatStyle().getChatClickEvent();
        if (GuiScreen.isShiftKeyDown()) {
            if (p_175276_1_.getChatStyle().getInsertion() != null) {
                this.setText(p_175276_1_.getChatStyle().getInsertion(), false);
            }
        } else if (clickevent != null) {
            block23: {
                if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
                    if (!this.mc.gameSettings.chatLinks) {
                        return false;
                    }
                    try {
                        URI uri = new URI(clickevent.getValue());
                        String s2 = uri.getScheme();
                        if (s2 == null) {
                            throw new URISyntaxException(clickevent.getValue(), "Missing protocol");
                        }
                        if (!PROTOCOLS.contains(s2.toLowerCase())) {
                            throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s2.toLowerCase());
                        }
                        if (this.mc.gameSettings.chatLinksPrompt) {
                            this.clickedLinkURI = uri;
                            this.mc.displayGuiScreen(new GuiConfirmOpenLink((GuiYesNoCallback)this, clickevent.getValue(), 31102009, false));
                            break block23;
                        }
                        this.openWebLink(uri);
                    }
                    catch (URISyntaxException urisyntaxexception) {
                        LOGGER.error("Can't open url for " + clickevent, (Throwable)urisyntaxexception);
                    }
                } else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
                    URI uri1 = new File(clickevent.getValue()).toURI();
                    this.openWebLink(uri1);
                } else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                    this.setText(clickevent.getValue(), true);
                } else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    this.sendChatMessage(clickevent.getValue(), false);
                } else if (clickevent.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
                    ChatUserInfo chatuserinfo = this.mc.getTwitchStream().func_152926_a(clickevent.getValue());
                    if (chatuserinfo != null) {
                        this.mc.displayGuiScreen(new GuiTwitchUserMode(this.mc.getTwitchStream(), chatuserinfo));
                    } else {
                        LOGGER.error("Tried to handle twitch user but couldn't find them!");
                    }
                } else {
                    LOGGER.error("Don't know how to handle " + clickevent);
                }
            }
            return true;
        }
        return false;
    }

    public void sendChatMessage(String msg) {
        this.sendChatMessage(msg, true);
    }

    public void sendChatMessage(String msg, boolean addToChat) {
        if (addToChat) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
        this.mc.thePlayer.sendChatMessage(msg);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (int i2 = 0; i2 < this.buttonList.size(); ++i2) {
                GuiButton guibutton = this.buttonList.get(i2);
                if (!guibutton.mousePressed(this.mc, mouseX, mouseY)) continue;
                this.selectedButton = guibutton;
                guibutton.playPressSound(this.mc.getSoundHandler());
                this.actionPerformed(guibutton);
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.selectedButton != null && state == 0) {
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    }

    protected void actionPerformed(GuiButton button) throws IOException {
    }

    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        this.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRendererObj = mc.fontRendererObj;
        this.width = width;
        this.height = height;
        this.buttonList.clear();
        this.initGui();
        GuiBlur moduleByClass = ModulesManager.getModuleByClass(GuiBlur.class);
        if (moduleByClass == null || moduleByClass.isToggled()) {
            for (Class<? extends GuiScreen> gui : GuiManager.BLUR_GUI) {
                if (!gui.isInstance(this)) continue;
                mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }
        }
    }

    public void initGui() {
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

    public void handleMouseInput() throws IOException {
        int i2 = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int j2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int k2 = Mouse.getEventButton();
        if (Mouse.getEventButtonState()) {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }
            this.eventButton = k2;
            this.lastMouseEvent = Minecraft.getSystemTime();
            this.mouseClicked(i2, j2, this.eventButton);
        } else if (k2 != -1) {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }
            this.eventButton = -1;
            this.mouseReleased(i2, j2, k2);
        } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            long l2 = Minecraft.getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(i2, j2, this.eventButton, l2);
        }
    }

    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKeyState()) {
            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }
        this.mc.dispatchKeypresses();
    }

    public void updateScreen() {
    }

    public void onGuiClosed() {
    }

    public void closeGui() {
        this.mc.entityRenderer.stopUseShader();
        this.onGuiClosed();
    }

    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }

    public void drawWorldBackground(int tint) {
        if (this.mc.theWorld == null) {
            this.drawBackground(tint);
        }
    }

    public void drawBackground(int tint) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(optionsBackground);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float f2 = 32.0f;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, this.height, 0.0).tex(0.0, (float)this.height / 32.0f + (float)tint).color(64, 64, 64, 255).endVertex();
        worldrenderer.pos(this.width, this.height, 0.0).tex((float)this.width / 32.0f, (float)this.height / 32.0f + (float)tint).color(64, 64, 64, 255).endVertex();
        worldrenderer.pos(this.width, 0.0, 0.0).tex((float)this.width / 32.0f, tint).color(64, 64, 64, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, tint).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (id == 31102009) {
            if (result) {
                this.openWebLink(this.clickedLinkURI);
            }
            this.clickedLinkURI = null;
            this.mc.displayGuiScreen(this);
        }
    }

    private void openWebLink(URI p_175282_1_) {
        try {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            oclass.getMethod("browse", URI.class).invoke(object, p_175282_1_);
        }
        catch (Throwable throwable) {
            LOGGER.error("Couldn't open link", throwable);
        }
    }

    public static boolean isCtrlKeyDown() {
        return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }

    public static boolean isAltKeyDown() {
        return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
    }

    public static boolean isKeyComboCtrlX(int p_175277_0_) {
        return p_175277_0_ == 45 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public static boolean isKeyComboCtrlV(int p_175279_0_) {
        return p_175279_0_ == 47 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public static boolean isKeyComboCtrlC(int p_175280_0_) {
        return p_175280_0_ == 46 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public static boolean isKeyComboCtrlA(int p_175278_0_) {
        return p_175278_0_ == 30 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public void onResize(Minecraft mcIn, int p_175273_2_, int p_175273_3_) {
        this.setWorldAndResolution(mcIn, p_175273_2_, p_175273_3_);
    }
}

