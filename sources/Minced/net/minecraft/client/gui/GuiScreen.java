// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.io.File;
import java.util.Locale;
import java.net.URISyntaxException;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.ITextComponent;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Arrays;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.util.ITooltipFlag;
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
import ru.tuskevich.util.animation.Animation;
import java.util.List;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.Minecraft;
import com.google.common.base.Splitter;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public abstract class GuiScreen extends Gui implements GuiYesNoCallback
{
    private static final Logger LOGGER;
    private static final Set<String> PROTOCOLS;
    private static final Splitter NEWLINE_SPLITTER;
    protected static Minecraft mc;
    protected RenderItem itemRender;
    public int width;
    public int height;
    protected List<GuiButton> buttonList;
    protected List<GuiLabel> labelList;
    public boolean allowUserInput;
    protected FontRenderer fontRenderer;
    protected GuiButton selectedButton;
    public int eventButton;
    private long lastMouseEvent;
    public static Animation animation;
    private int touchValue;
    private URI clickedLinkURI;
    private boolean focused;
    
    public GuiScreen() {
        this.buttonList = (List<GuiButton>)Lists.newArrayList();
        this.labelList = (List<GuiLabel>)Lists.newArrayList();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        for (int i = 0; i < this.buttonList.size(); ++i) {
            this.buttonList.get(i).drawButton(GuiScreen.mc, mouseX, mouseY, partialTicks);
        }
        for (int j = 0; j < this.labelList.size(); ++j) {
            this.labelList.get(j).drawLabel(GuiScreen.mc, mouseX, mouseY);
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            GuiScreen.mc.displayGuiScreen(null);
            if (GuiScreen.mc.currentScreen == null) {
                GuiScreen.mc.setIngameFocus();
            }
        }
    }
    
    protected <T extends GuiButton> T addButton(final T buttonIn) {
        this.buttonList.add(buttonIn);
        return buttonIn;
    }
    
    public static String getClipboardString() {
        try {
            final Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String)transferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (Exception ex) {}
        return "";
    }
    
    public static void setClipboardString(final String copyText) {
        if (!StringUtils.isEmpty((CharSequence)copyText)) {
            try {
                final StringSelection stringselection = new StringSelection(copyText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
            }
            catch (Exception ex) {}
        }
    }
    
    protected void renderToolTip(final ItemStack stack, final int x, final int y) {
        this.drawHoveringText(this.getItemToolTip(stack), x, y);
    }
    
    public List<String> getItemToolTip(final ItemStack p_191927_1_) {
        final Minecraft mc = GuiScreen.mc;
        final List<String> list = p_191927_1_.getTooltip(Minecraft.player, GuiScreen.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, p_191927_1_.getRarity().color + list.get(i));
            }
            else {
                list.set(i, TextFormatting.GRAY + list.get(i));
            }
        }
        return list;
    }
    
    public void drawHoveringText(final String text, final int x, final int y) {
        this.drawHoveringText(Arrays.asList(text), x, y);
    }
    
    public void setFocused(final boolean hasFocusedControlIn) {
        this.focused = hasFocusedControlIn;
    }
    
    public boolean isFocused() {
        return this.focused;
    }
    
    public void drawHoveringText(final List<String> textLines, final int x, final int y) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;
            for (final String s : textLines) {
                final int j = this.fontRenderer.getStringWidth(s);
                if (j > i) {
                    i = j;
                }
            }
            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;
            if (textLines.size() > 1) {
                k += 2 + (textLines.size() - 1) * 10;
            }
            if (l1 + i > this.width) {
                l1 -= 28 + i;
            }
            if (i2 + k + 6 > this.height) {
                i2 = this.height - k - 6;
            }
            this.zLevel = 300.0f;
            this.itemRender.zLevel = 300.0f;
            final int m = -267386864;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
            this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
            final int i3 = 1347420415;
            final int j2 = 1344798847;
            this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
            this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
            this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);
            for (int k2 = 0; k2 < textLines.size(); ++k2) {
                final String s2 = textLines.get(k2);
                this.fontRenderer.drawStringWithShadow(s2, (float)l1, (float)i2, -1);
                if (k2 == 0) {
                    i2 += 2;
                }
                i2 += 10;
            }
            this.zLevel = 0.0f;
            this.itemRender.zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
    
    protected void handleComponentHover(final ITextComponent component, final int x, final int y) {
        if (component != null && component.getStyle().getHoverEvent() != null) {
            final HoverEvent hoverevent = component.getStyle().getHoverEvent();
            if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
                ItemStack itemstack = ItemStack.EMPTY;
                try {
                    final NBTBase nbtbase = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
                    if (nbtbase instanceof NBTTagCompound) {
                        itemstack = new ItemStack((NBTTagCompound)nbtbase);
                    }
                }
                catch (NBTException ex) {}
                if (itemstack.isEmpty()) {
                    this.drawHoveringText(TextFormatting.RED + "Invalid Item!", x, y);
                }
                else {
                    this.renderToolTip(itemstack, x, y);
                }
            }
            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
                if (GuiScreen.mc.gameSettings.advancedItemTooltips) {
                    try {
                        final NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
                        final List<String> list = (List<String>)Lists.newArrayList();
                        list.add(nbttagcompound.getString("name"));
                        if (nbttagcompound.hasKey("type", 8)) {
                            final String s = nbttagcompound.getString("type");
                            list.add("Type: " + s);
                        }
                        list.add(nbttagcompound.getString("id"));
                        this.drawHoveringText(list, x, y);
                    }
                    catch (NBTException var8) {
                        this.drawHoveringText(TextFormatting.RED + "Invalid Entity!", x, y);
                    }
                }
            }
            else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
                this.drawHoveringText(GuiScreen.mc.fontRenderer.listFormattedStringToWidth(hoverevent.getValue().getFormattedText(), Math.max(this.width / 2, 200)), x, y);
            }
            GlStateManager.disableLighting();
        }
    }
    
    protected void setText(final String newChatText, final boolean shouldOverwrite) {
    }
    
    public boolean handleComponentClick(final ITextComponent component) {
        if (component == null) {
            return false;
        }
        final ClickEvent clickevent = component.getStyle().getClickEvent();
        if (isShiftKeyDown()) {
            if (component.getStyle().getInsertion() != null) {
                this.setText(component.getStyle().getInsertion(), false);
            }
        }
        else if (clickevent != null) {
            if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
                if (!GuiScreen.mc.gameSettings.chatLinks) {
                    return false;
                }
                try {
                    final URI uri = new URI(clickevent.getValue());
                    final String s = uri.getScheme();
                    if (s == null) {
                        throw new URISyntaxException(clickevent.getValue(), "Missing protocol");
                    }
                    if (!GuiScreen.PROTOCOLS.contains(s.toLowerCase(Locale.ROOT))) {
                        throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s.toLowerCase(Locale.ROOT));
                    }
                    if (GuiScreen.mc.gameSettings.chatLinksPrompt) {
                        this.clickedLinkURI = uri;
                        GuiScreen.mc.displayGuiScreen(new GuiConfirmOpenLink(this, clickevent.getValue(), 31102009, false));
                    }
                    else {
                        this.openWebLink(uri);
                    }
                }
                catch (URISyntaxException urisyntaxexception) {
                    GuiScreen.LOGGER.error("Can't open url for {}", (Object)clickevent, (Object)urisyntaxexception);
                }
            }
            else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
                final URI uri2 = new File(clickevent.getValue()).toURI();
                this.openWebLink(uri2);
            }
            else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                this.setText(clickevent.getValue(), true);
            }
            else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.sendChatMessage(clickevent.getValue(), false);
            }
            else {
                GuiScreen.LOGGER.error("Don't know how to handle {}", (Object)clickevent);
            }
            return true;
        }
        return false;
    }
    
    public void sendChatMessage(final String msg) {
        this.sendChatMessage(msg, true);
    }
    
    public void sendChatMessage(final String msg, final boolean addToChat) {
        if (addToChat) {
            GuiScreen.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
        final Minecraft mc = GuiScreen.mc;
        Minecraft.player.sendChatMessage(msg);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (int i = 0; i < this.buttonList.size(); ++i) {
                final GuiButton guibutton = this.buttonList.get(i);
                if (guibutton.mousePressed(GuiScreen.mc, mouseX, mouseY)) {
                    (this.selectedButton = guibutton).playPressSound(GuiScreen.mc.getSoundHandler());
                    this.actionPerformed(guibutton);
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
        GuiScreen.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRenderer = mc.fontRenderer;
        this.width = width;
        this.height = height;
        this.buttonList.clear();
        this.initGui();
    }
    
    public void setGuiSize(final int w, final int h) {
        this.width = w;
        this.height = h;
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
        final int i = Mouse.getEventX() * this.width / GuiScreen.mc.displayWidth;
        final int j = this.height - Mouse.getEventY() * this.height / GuiScreen.mc.displayHeight - 1;
        final int k = Mouse.getEventButton();
        if (Mouse.getEventButtonState()) {
            if (GuiScreen.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }
            this.eventButton = k;
            this.lastMouseEvent = Minecraft.getSystemTime();
            this.mouseClicked(i, j, this.eventButton);
        }
        else if (k != -1) {
            if (GuiScreen.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }
            this.eventButton = -1;
            this.mouseReleased(i, j, k);
        }
        else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            final long l = Minecraft.getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(i, j, this.eventButton, l);
        }
    }
    
    public void handleKeyboardInput() throws IOException {
        final char c0 = Keyboard.getEventCharacter();
        if ((Keyboard.getEventKey() == 0 && c0 >= ' ') || Keyboard.getEventKeyState()) {
            this.keyTyped(c0, Keyboard.getEventKey());
        }
        GuiScreen.mc.dispatchKeypresses();
    }
    
    public void updateScreen() {
    }
    
    public void onGuiClosed() {
    }
    
    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }
    
    public void drawWorldBackground(final int tint) {
        if (GuiScreen.mc.world != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        }
        else {
            this.drawBackground(tint);
        }
    }
    
    public void drawBackground(final int tint) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GuiScreen.mc.getTextureManager().bindTexture(GuiScreen.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float f = 32.0f;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0, this.height, 0.0).tex(0.0, this.height / 32.0f + tint).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(this.width, this.height, 0.0).tex(this.width / 32.0f, this.height / 32.0f + tint).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(this.width, 0.0, 0.0).tex(this.width / 32.0f, tint).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).tex(0.0, tint).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }
    
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (id == 31102009) {
            if (result) {
                this.openWebLink(this.clickedLinkURI);
            }
            this.clickedLinkURI = null;
            GuiScreen.mc.displayGuiScreen(this);
        }
    }
    
    private void openWebLink(final URI url) {
        try {
            final Class<?> oclass = Class.forName("java.awt.Desktop");
            final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            oclass.getMethod("browse", URI.class).invoke(object, url);
        }
        catch (Throwable throwable2) {
            final Throwable throwable = throwable2.getCause();
            GuiScreen.LOGGER.error("Couldn't open link: {}", (Object)((throwable == null) ? "<UNKNOWN>" : throwable.getMessage()));
        }
    }
    
    public static boolean isCtrlKeyDown() {
        if (Minecraft.IS_RUNNING_ON_MAC) {
            return Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220);
        }
        return Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }
    
    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }
    
    public static boolean isAltKeyDown() {
        return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
    }
    
    public static boolean isKeyComboCtrlX(final int keyID) {
        return keyID == 45 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }
    
    public static boolean isKeyComboCtrlV(final int keyID) {
        return keyID == 47 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }
    
    public static boolean isKeyComboCtrlC(final int keyID) {
        return keyID == 46 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }
    
    public static boolean isKeyComboCtrlA(final int keyID) {
        return keyID == 30 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
    }
    
    public void onResize(final Minecraft mcIn, final int w, final int h) {
        this.setWorldAndResolution(mcIn, w, h);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        PROTOCOLS = Sets.newHashSet((Object[])new String[] { "http", "https" });
        NEWLINE_SPLITTER = Splitter.on('\n');
        GuiScreen.animation = new Animation();
    }
}
