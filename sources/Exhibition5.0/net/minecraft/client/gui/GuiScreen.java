// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tv.twitch.chat.ChatUserInfo;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import java.io.File;
import java.net.URISyntaxException;
import net.minecraft.event.ClickEvent;
import net.minecraft.stats.StatBase;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.IChatComponent;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Arrays;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import org.apache.commons.lang3.StringUtils;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import java.io.IOException;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.List;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.Minecraft;
import com.google.common.base.Splitter;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public abstract class GuiScreen extends Gui implements GuiYesNoCallback
{
    private static final Logger field_175287_a;
    private static final Set field_175284_f;
    private static final Splitter field_175285_g;
    protected Minecraft mc;
    protected RenderItem itemRender;
    public int width;
    public int height;
    protected List buttonList;
    protected List labelList;
    public boolean allowUserInput;
    protected FontRenderer fontRendererObj;
    private GuiButton selectedButton;
    public int eventButton;
    private long lastMouseEvent;
    private int touchValue;
    private URI field_175286_t;
    private static final String __OBFID = "CL_00000710";
    
    public GuiScreen() {
        this.buttonList = Lists.newArrayList();
        this.labelList = Lists.newArrayList();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        for (int var4 = 0; var4 < this.buttonList.size(); ++var4) {
            this.buttonList.get(var4).drawButton(this.mc, mouseX, mouseY);
        }
        for (int var4 = 0; var4 < this.labelList.size(); ++var4) {
            this.labelList.get(var4).drawLabel(this.mc, mouseX, mouseY);
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }
    
    public static String getClipboardString() {
        try {
            final Transferable var0 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (var0 != null && var0.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String)var0.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (Exception ex) {}
        return "";
    }
    
    public static void setClipboardString(final String copyText) {
        if (!StringUtils.isEmpty((CharSequence)copyText)) {
            try {
                final StringSelection var1 = new StringSelection(copyText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(var1, null);
            }
            catch (Exception ex) {}
        }
    }
    
    protected void renderToolTip(final ItemStack itemIn, final int x, final int y) {
        final List var4 = itemIn.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
        for (int var5 = 0; var5 < var4.size(); ++var5) {
            if (var5 == 0) {
                var4.set(var5, itemIn.getRarity().rarityColor + var4.get(var5));
            }
            else {
                var4.set(var5, EnumChatFormatting.GRAY + var4.get(var5));
            }
        }
        this.drawHoveringText(var4, x, y);
    }
    
    protected void drawCreativeTabHoveringText(final String tabName, final int mouseX, final int mouseY) {
        this.drawHoveringText(Arrays.asList(tabName), mouseX, mouseY);
    }
    
    protected void drawHoveringText(final List textLines, final int x, final int y) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int var4 = 0;
            for (final String var6 : textLines) {
                final int var7 = this.fontRendererObj.getStringWidth(var6);
                if (var7 > var4) {
                    var4 = var7;
                }
            }
            int var8 = x + 12;
            int var9 = y - 12;
            int var10 = 8;
            if (textLines.size() > 1) {
                var10 += 2 + (textLines.size() - 1) * 10;
            }
            if (var8 + var4 > this.width) {
                var8 -= 28 + var4;
            }
            if (var9 + var10 + 6 > this.height) {
                var9 = this.height - var10 - 6;
            }
            this.zLevel = 300.0f;
            this.itemRender.zLevel = 300.0f;
            final int var11 = -267386864;
            this.drawGradientRect(var8 - 3, var9 - 4, var8 + var4 + 3, var9 - 3, var11, var11);
            this.drawGradientRect(var8 - 3, var9 + var10 + 3, var8 + var4 + 3, var9 + var10 + 4, var11, var11);
            this.drawGradientRect(var8 - 3, var9 - 3, var8 + var4 + 3, var9 + var10 + 3, var11, var11);
            this.drawGradientRect(var8 - 4, var9 - 3, var8 - 3, var9 + var10 + 3, var11, var11);
            this.drawGradientRect(var8 + var4 + 3, var9 - 3, var8 + var4 + 4, var9 + var10 + 3, var11, var11);
            final int var12 = 1347420415;
            final int var13 = (var12 & 0xFEFEFE) >> 1 | (var12 & 0xFF000000);
            this.drawGradientRect(var8 - 3, var9 - 3 + 1, var8 - 3 + 1, var9 + var10 + 3 - 1, var12, var13);
            this.drawGradientRect(var8 + var4 + 2, var9 - 3 + 1, var8 + var4 + 3, var9 + var10 + 3 - 1, var12, var13);
            this.drawGradientRect(var8 - 3, var9 - 3, var8 + var4 + 3, var9 - 3 + 1, var12, var12);
            this.drawGradientRect(var8 - 3, var9 + var10 + 2, var8 + var4 + 3, var9 + var10 + 3, var13, var13);
            for (int var14 = 0; var14 < textLines.size(); ++var14) {
                final String var15 = textLines.get(var14);
                this.fontRendererObj.drawStringWithShadow(var15, var8, var9, -1);
                if (var14 == 0) {
                    var9 += 2;
                }
                var9 += 10;
            }
            this.zLevel = 0.0f;
            this.itemRender.zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
    
    protected void func_175272_a(final IChatComponent p_175272_1_, final int p_175272_2_, final int p_175272_3_) {
        if (p_175272_1_ != null && p_175272_1_.getChatStyle().getChatHoverEvent() != null) {
            final HoverEvent var4 = p_175272_1_.getChatStyle().getChatHoverEvent();
            if (var4.getAction() == HoverEvent.Action.SHOW_ITEM) {
                ItemStack var5 = null;
                try {
                    final NBTTagCompound var6 = JsonToNBT.func_180713_a(var4.getValue().getUnformattedText());
                    if (var6 instanceof NBTTagCompound) {
                        var5 = ItemStack.loadItemStackFromNBT(var6);
                    }
                }
                catch (NBTException ex) {}
                if (var5 != null) {
                    this.renderToolTip(var5, p_175272_2_, p_175272_3_);
                }
                else {
                    this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", p_175272_2_, p_175272_3_);
                }
            }
            else if (var4.getAction() == HoverEvent.Action.SHOW_ENTITY) {
                if (this.mc.gameSettings.advancedItemTooltips) {
                    try {
                        final NBTTagCompound var7 = JsonToNBT.func_180713_a(var4.getValue().getUnformattedText());
                        if (var7 instanceof NBTTagCompound) {
                            final ArrayList var8 = Lists.newArrayList();
                            final NBTTagCompound var9 = var7;
                            var8.add(var9.getString("name"));
                            if (var9.hasKey("type", 8)) {
                                final String var10 = var9.getString("type");
                                var8.add("Type: " + var10 + " (" + EntityList.func_180122_a(var10) + ")");
                            }
                            var8.add(var9.getString("id"));
                            this.drawHoveringText(var8, p_175272_2_, p_175272_3_);
                        }
                        else {
                            this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", p_175272_2_, p_175272_3_);
                        }
                    }
                    catch (NBTException var15) {
                        this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", p_175272_2_, p_175272_3_);
                    }
                }
            }
            else if (var4.getAction() == HoverEvent.Action.SHOW_TEXT) {
                this.drawHoveringText(GuiScreen.field_175285_g.splitToList((CharSequence)var4.getValue().getFormattedText()), p_175272_2_, p_175272_3_);
            }
            else if (var4.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
                final StatBase var11 = StatList.getOneShotStat(var4.getValue().getUnformattedText());
                if (var11 != null) {
                    final IChatComponent var12 = var11.getStatName();
                    final ChatComponentTranslation var13 = new ChatComponentTranslation("stats.tooltip.type." + (var11.isAchievement() ? "achievement" : "statistic"), new Object[0]);
                    var13.getChatStyle().setItalic(true);
                    final String var10 = (var11 instanceof Achievement) ? ((Achievement)var11).getDescription() : null;
                    final ArrayList var14 = Lists.newArrayList((Object[])new String[] { var12.getFormattedText(), var13.getFormattedText() });
                    if (var10 != null) {
                        var14.addAll(this.fontRendererObj.listFormattedStringToWidth(var10, 150));
                    }
                    this.drawHoveringText(var14, p_175272_2_, p_175272_3_);
                }
                else {
                    this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", p_175272_2_, p_175272_3_);
                }
            }
            GlStateManager.disableLighting();
        }
    }
    
    protected void func_175274_a(final String p_175274_1_, final boolean p_175274_2_) {
    }
    
    protected boolean func_175276_a(final IChatComponent p_175276_1_) {
        if (p_175276_1_ == null) {
            return false;
        }
        final ClickEvent var2 = p_175276_1_.getChatStyle().getChatClickEvent();
        if (isShiftKeyDown()) {
            if (p_175276_1_.getChatStyle().getInsertion() != null) {
                this.func_175274_a(p_175276_1_.getChatStyle().getInsertion(), false);
            }
        }
        else if (var2 != null) {
            if (var2.getAction() == ClickEvent.Action.OPEN_URL) {
                if (!this.mc.gameSettings.chatLinks) {
                    return false;
                }
                try {
                    final URI var3 = new URI(var2.getValue());
                    if (!GuiScreen.field_175284_f.contains(var3.getScheme().toLowerCase())) {
                        throw new URISyntaxException(var2.getValue(), "Unsupported protocol: " + var3.getScheme().toLowerCase());
                    }
                    if (this.mc.gameSettings.chatLinksPrompt) {
                        this.field_175286_t = var3;
                        this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, var2.getValue(), 31102009, false));
                    }
                    else {
                        this.func_175282_a(var3);
                    }
                }
                catch (URISyntaxException var4) {
                    GuiScreen.field_175287_a.error("Can't open url for " + var2, (Throwable)var4);
                }
            }
            else if (var2.getAction() == ClickEvent.Action.OPEN_FILE) {
                final URI var3 = new File(var2.getValue()).toURI();
                this.func_175282_a(var3);
            }
            else if (var2.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                this.func_175274_a(var2.getValue(), true);
            }
            else if (var2.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.func_175281_b(var2.getValue(), false);
            }
            else if (var2.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
                final ChatUserInfo var5 = this.mc.getTwitchStream().func_152926_a(var2.getValue());
                if (var5 != null) {
                    this.mc.displayGuiScreen(new GuiTwitchUserMode(this.mc.getTwitchStream(), var5));
                }
                else {
                    GuiScreen.field_175287_a.error("Tried to handle twitch user but couldn't find them!");
                }
            }
            else {
                GuiScreen.field_175287_a.error("Don't know how to handle " + var2);
            }
            return true;
        }
        return false;
    }
    
    public void func_175275_f(final String p_175275_1_) {
        this.func_175281_b(p_175275_1_, true);
    }
    
    public void func_175281_b(final String p_175281_1_, final boolean p_175281_2_) {
        if (p_175281_2_) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(p_175281_1_);
        }
        this.mc.thePlayer.sendChatMessage(p_175281_1_);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (int var4 = 0; var4 < this.buttonList.size(); ++var4) {
                final GuiButton var5 = this.buttonList.get(var4);
                if (var5.mousePressed(this.mc, mouseX, mouseY)) {
                    (this.selectedButton = var5).playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(var5);
                }
            }
        }
    }
    
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.selectedButton != null && state == 0) {
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
    }
    
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
    }
    
    protected void actionPerformed(final GuiButton button) throws IOException {
    }
    
    public void setWorldAndResolution(final Minecraft mc, final int width, final int height) {
        this.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRendererObj = mc.fontRendererObj;
        this.width = width;
        this.height = height;
        this.buttonList.clear();
        this.initGui();
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
        final int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
        final int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        final int var3 = Mouse.getEventButton();
        if (Mouse.getEventButtonState()) {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }
            this.eventButton = var3;
            this.lastMouseEvent = Minecraft.getSystemTime();
            this.mouseClicked(var1, var2, this.eventButton);
        }
        else if (var3 != -1) {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }
            this.eventButton = -1;
            this.mouseReleased(var1, var2, var3);
        }
        else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            final long var4 = Minecraft.getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(var1, var2, this.eventButton, var4);
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
    
    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }
    
    public void drawWorldBackground(final int tint) {
        if (this.mc.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        }
        else {
            this.drawBackground(tint);
        }
    }
    
    public void drawBackground(final int tint) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        final Tessellator var2 = Tessellator.getInstance();
        final WorldRenderer var3 = var2.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(GuiScreen.optionsBackground);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float var4 = 32.0f;
        var3.startDrawingQuads();
        var3.func_178991_c(4210752);
        var3.addVertexWithUV(0.0, this.height, 0.0, 0.0, this.height / var4 + tint);
        var3.addVertexWithUV(this.width, this.height, 0.0, this.width / var4, this.height / var4 + tint);
        var3.addVertexWithUV(this.width, 0.0, 0.0, this.width / var4, tint);
        var3.addVertexWithUV(0.0, 0.0, 0.0, 0.0, tint);
        var2.draw();
    }
    
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (id == 31102009) {
            if (result) {
                this.func_175282_a(this.field_175286_t);
            }
            this.field_175286_t = null;
            this.mc.displayGuiScreen(this);
        }
    }
    
    private void func_175282_a(final URI p_175282_1_) {
        try {
            final Class var2 = Class.forName("java.awt.Desktop");
            final Object var3 = var2.getMethod("getDesktop", (Class[])new Class[0]).invoke(null, new Object[0]);
            var2.getMethod("browse", URI.class).invoke(var3, p_175282_1_);
        }
        catch (Throwable var4) {
            GuiScreen.field_175287_a.error("Couldn't open link", var4);
        }
    }
    
    public static boolean isCtrlKeyDown() {
        return Minecraft.isRunningOnMac ? (Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220)) : (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157));
    }
    
    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }
    
    public static boolean func_175283_s() {
        return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
    }
    
    public static boolean func_175277_d(final int p_175277_0_) {
        return p_175277_0_ == 45 && isCtrlKeyDown();
    }
    
    public static boolean func_175279_e(final int p_175279_0_) {
        return p_175279_0_ == 47 && isCtrlKeyDown();
    }
    
    public static boolean func_175280_f(final int p_175280_0_) {
        return p_175280_0_ == 46 && isCtrlKeyDown();
    }
    
    public static boolean func_175278_g(final int p_175278_0_) {
        return p_175278_0_ == 30 && isCtrlKeyDown();
    }
    
    public void func_175273_b(final Minecraft mcIn, final int p_175273_2_, final int p_175273_3_) {
        this.setWorldAndResolution(mcIn, p_175273_2_, p_175273_3_);
    }
    
    static {
        field_175287_a = LogManager.getLogger();
        field_175284_f = Sets.newHashSet((Object[])new String[] { "http", "https" });
        field_175285_g = Splitter.on('\n');
    }
}
