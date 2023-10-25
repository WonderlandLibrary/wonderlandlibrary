/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.gson.JsonParseException
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiScreenBook
extends GuiScreen {
    private boolean bookGettingSigned;
    private GuiButton buttonSign;
    private int field_175387_B = -1;
    private GuiButton buttonCancel;
    private int bookTotalPages = 1;
    private NextPageButton buttonPreviousPage;
    private int currPage;
    private GuiButton buttonFinalize;
    private int updateCount;
    private String bookTitle = "";
    private List<IChatComponent> field_175386_A;
    private final ItemStack bookObj;
    private static final Logger logger = LogManager.getLogger();
    private final EntityPlayer editingPlayer;
    private GuiButton buttonDone;
    private final boolean bookIsUnsigned;
    private NBTTagList bookPages;
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");
    private NextPageButton buttonNextPage;
    private boolean bookIsModified;

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        super.keyTyped(c, n);
        if (this.bookIsUnsigned) {
            if (this.bookGettingSigned) {
                this.keyTypedInTitle(c, n);
            } else {
                this.keyTypedInBook(c, n);
            }
        }
    }

    private void updateButtons() {
        this.buttonNextPage.visible = !this.bookGettingSigned && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned);
        this.buttonPreviousPage.visible = !this.bookGettingSigned && this.currPage > 0;
        boolean bl = this.buttonDone.visible = !this.bookIsUnsigned || !this.bookGettingSigned;
        if (this.bookIsUnsigned) {
            this.buttonSign.visible = !this.bookGettingSigned;
            this.buttonCancel.visible = this.bookGettingSigned;
            this.buttonFinalize.visible = this.bookGettingSigned;
            this.buttonFinalize.enabled = this.bookTitle.trim().length() > 0;
        }
    }

    private void pageInsertIntoCurrent(String string) {
        String string2 = this.pageGetCurrent();
        String string3 = String.valueOf(string2) + string;
        int n = this.fontRendererObj.splitStringWidth(String.valueOf(string3) + (Object)((Object)EnumChatFormatting.BLACK) + "_", 118);
        if (n <= 128 && string3.length() < 256) {
            this.pageSetCurrent(string3);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        ++this.updateCount;
    }

    @Override
    protected boolean handleComponentClick(IChatComponent iChatComponent) {
        ClickEvent clickEvent;
        ClickEvent clickEvent2 = clickEvent = iChatComponent == null ? null : iChatComponent.getChatStyle().getChatClickEvent();
        if (clickEvent == null) {
            return false;
        }
        if (clickEvent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            String string = clickEvent.getValue();
            try {
                int n = Integer.parseInt(string) - 1;
                if (n >= 0 && n < this.bookTotalPages && n != this.currPage) {
                    this.currPage = n;
                    this.updateButtons();
                    return true;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            return false;
        }
        boolean bl = super.handleComponentClick(iChatComponent);
        if (bl && clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
            this.mc.displayGuiScreen(null);
        }
        return bl;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    private void pageSetCurrent(String string) {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
            this.bookPages.set(this.currPage, new NBTTagString(string));
            this.bookIsModified = true;
        }
    }

    private void addNewPage() {
        if (this.bookPages != null && this.bookPages.tagCount() < 50) {
            this.bookPages.appendTag(new NBTTagString(""));
            ++this.bookTotalPages;
            this.bookIsModified = true;
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(bookGuiTextures);
        int n3 = (width - this.bookImageWidth) / 2;
        int n4 = 2;
        this.drawTexturedModalRect(n3, n4, 0, 0, this.bookImageWidth, this.bookImageHeight);
        if (this.bookGettingSigned) {
            String string = this.bookTitle;
            if (this.bookIsUnsigned) {
                string = this.updateCount / 6 % 2 == 0 ? String.valueOf(string) + (Object)((Object)EnumChatFormatting.BLACK) + "_" : String.valueOf(string) + (Object)((Object)EnumChatFormatting.GRAY) + "_";
            }
            String string2 = I18n.format("book.editTitle", new Object[0]);
            int n5 = this.fontRendererObj.getStringWidth(string2);
            this.fontRendererObj.drawString(string2, n3 + 36 + (116 - n5) / 2, n4 + 16 + 16, 0);
            int n6 = this.fontRendererObj.getStringWidth(string);
            this.fontRendererObj.drawString(string, n3 + 36 + (116 - n6) / 2, n4 + 48, 0);
            String string3 = I18n.format("book.byAuthor", this.editingPlayer.getName());
            int n7 = this.fontRendererObj.getStringWidth(string3);
            this.fontRendererObj.drawString((Object)((Object)EnumChatFormatting.DARK_GRAY) + string3, n3 + 36 + (116 - n7) / 2, n4 + 48 + 10, 0);
            String string4 = I18n.format("book.finalizeWarning", new Object[0]);
            this.fontRendererObj.drawSplitString(string4, n3 + 36, n4 + 80, 116, 0);
        } else {
            String string = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
            String string5 = "";
            if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
                string5 = this.bookPages.getStringTagAt(this.currPage);
            }
            if (this.bookIsUnsigned) {
                string5 = this.fontRendererObj.getBidiFlag() ? String.valueOf(string5) + "_" : (this.updateCount / 6 % 2 == 0 ? String.valueOf(string5) + (Object)((Object)EnumChatFormatting.BLACK) + "_" : String.valueOf(string5) + (Object)((Object)EnumChatFormatting.GRAY) + "_");
            } else if (this.field_175387_B != this.currPage) {
                IChatComponent iChatComponent;
                if (ItemEditableBook.validBookTagContents(this.bookObj.getTagCompound())) {
                    try {
                        iChatComponent = IChatComponent.Serializer.jsonToComponent(string5);
                        this.field_175386_A = iChatComponent != null ? GuiUtilRenderComponents.func_178908_a(iChatComponent, 116, this.fontRendererObj, true, true) : null;
                    }
                    catch (JsonParseException jsonParseException) {
                        this.field_175386_A = null;
                    }
                } else {
                    iChatComponent = new ChatComponentText(String.valueOf(EnumChatFormatting.DARK_RED.toString()) + "* Invalid book tag *");
                    this.field_175386_A = Lists.newArrayList((Iterable)iChatComponent);
                }
                this.field_175387_B = this.currPage;
            }
            int n8 = this.fontRendererObj.getStringWidth(string);
            this.fontRendererObj.drawString(string, n3 - n8 + this.bookImageWidth - 44, n4 + 16, 0);
            if (this.field_175386_A == null) {
                this.fontRendererObj.drawSplitString(string5, n3 + 36, n4 + 16 + 16, 116, 0);
            } else {
                int n9 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());
                int n10 = 0;
                while (n10 < n9) {
                    IChatComponent iChatComponent = this.field_175386_A.get(n10);
                    this.fontRendererObj.drawString(iChatComponent.getUnformattedText(), n3 + 36, n4 + 16 + 16 + n10 * this.fontRendererObj.FONT_HEIGHT, 0);
                    ++n10;
                }
                IChatComponent iChatComponent = this.func_175385_b(n, n2);
                if (iChatComponent != null) {
                    this.handleComponentHover(iChatComponent, n, n2);
                }
            }
        }
        super.drawScreen(n, n2, f);
    }

    private void keyTypedInTitle(char c, int n) throws IOException {
        switch (n) {
            case 14: {
                if (!this.bookTitle.isEmpty()) {
                    this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
                    this.updateButtons();
                }
                return;
            }
            case 28: 
            case 156: {
                if (!this.bookTitle.isEmpty()) {
                    this.sendBookToServer(true);
                    this.mc.displayGuiScreen(null);
                }
                return;
            }
        }
        if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(c)) {
            this.bookTitle = String.valueOf(this.bookTitle) + Character.toString(c);
            this.updateButtons();
            this.bookIsModified = true;
        }
    }

    private void keyTypedInBook(char c, int n) {
        if (GuiScreen.isKeyComboCtrlV(n)) {
            this.pageInsertIntoCurrent(GuiScreen.getClipboardString());
        } else {
            switch (n) {
                case 14: {
                    String string = this.pageGetCurrent();
                    if (string.length() > 0) {
                        this.pageSetCurrent(string.substring(0, string.length() - 1));
                    }
                    return;
                }
                case 28: 
                case 156: {
                    this.pageInsertIntoCurrent("\n");
                    return;
                }
            }
            if (ChatAllowedCharacters.isAllowedCharacter(c)) {
                this.pageInsertIntoCurrent(Character.toString(c));
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            if (guiButton.id == 0) {
                this.mc.displayGuiScreen(null);
                this.sendBookToServer(false);
            } else if (guiButton.id == 3 && this.bookIsUnsigned) {
                this.bookGettingSigned = true;
            } else if (guiButton.id == 1) {
                if (this.currPage < this.bookTotalPages - 1) {
                    ++this.currPage;
                } else if (this.bookIsUnsigned) {
                    this.addNewPage();
                    if (this.currPage < this.bookTotalPages - 1) {
                        ++this.currPage;
                    }
                }
            } else if (guiButton.id == 2) {
                if (this.currPage > 0) {
                    --this.currPage;
                }
            } else if (guiButton.id == 5 && this.bookGettingSigned) {
                this.sendBookToServer(true);
                this.mc.displayGuiScreen(null);
            } else if (guiButton.id == 4 && this.bookGettingSigned) {
                this.bookGettingSigned = false;
            }
            this.updateButtons();
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents((boolean)true);
        if (this.bookIsUnsigned) {
            this.buttonSign = new GuiButton(3, width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.signButton", new Object[0]));
            this.buttonList.add(this.buttonSign);
            this.buttonDone = new GuiButton(0, width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.done", new Object[0]));
            this.buttonList.add(this.buttonDone);
            this.buttonFinalize = new GuiButton(5, width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.finalizeButton", new Object[0]));
            this.buttonList.add(this.buttonFinalize);
            this.buttonCancel = new GuiButton(4, width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.cancel", new Object[0]));
            this.buttonList.add(this.buttonCancel);
        } else {
            this.buttonDone = new GuiButton(0, width / 2 - 100, 4 + this.bookImageHeight, 200, 20, I18n.format("gui.done", new Object[0]));
            this.buttonList.add(this.buttonDone);
        }
        int n = (width - this.bookImageWidth) / 2;
        int n2 = 2;
        this.buttonNextPage = new NextPageButton(1, n + 120, n2 + 154, true);
        this.buttonList.add(this.buttonNextPage);
        this.buttonPreviousPage = new NextPageButton(2, n + 38, n2 + 154, false);
        this.buttonList.add(this.buttonPreviousPage);
        this.updateButtons();
    }

    private String pageGetCurrent() {
        return this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount() ? this.bookPages.getStringTagAt(this.currPage) : "";
    }

    public IChatComponent func_175385_b(int n, int n2) {
        if (this.field_175386_A == null) {
            return null;
        }
        int n3 = n - (width - this.bookImageWidth) / 2 - 36;
        int n4 = n2 - 2 - 16 - 16;
        if (n3 >= 0 && n4 >= 0) {
            int n5 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());
            if (n3 <= 116 && n4 < Minecraft.fontRendererObj.FONT_HEIGHT * n5 + n5) {
                int n6 = n4 / Minecraft.fontRendererObj.FONT_HEIGHT;
                if (n6 >= 0 && n6 < this.field_175386_A.size()) {
                    IChatComponent iChatComponent = this.field_175386_A.get(n6);
                    int n7 = 0;
                    for (IChatComponent iChatComponent2 : iChatComponent) {
                        if (!(iChatComponent2 instanceof ChatComponentText) || (n7 += Minecraft.fontRendererObj.getStringWidth(((ChatComponentText)iChatComponent2).getChatComponentText_TextValue())) <= n3) continue;
                        return iChatComponent2;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        IChatComponent iChatComponent;
        if (n3 == 0 && this.handleComponentClick(iChatComponent = this.func_175385_b(n, n2))) {
            return;
        }
        super.mouseClicked(n, n2, n3);
    }

    public GuiScreenBook(EntityPlayer entityPlayer, ItemStack itemStack, boolean bl) {
        this.editingPlayer = entityPlayer;
        this.bookObj = itemStack;
        this.bookIsUnsigned = bl;
        if (itemStack.hasTagCompound()) {
            NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
            this.bookPages = nBTTagCompound.getTagList("pages", 8);
            if (this.bookPages != null) {
                this.bookPages = (NBTTagList)this.bookPages.copy();
                this.bookTotalPages = this.bookPages.tagCount();
                if (this.bookTotalPages < 1) {
                    this.bookTotalPages = 1;
                }
            }
        }
        if (this.bookPages == null && bl) {
            this.bookPages = new NBTTagList();
            this.bookPages.appendTag(new NBTTagString(""));
            this.bookTotalPages = 1;
        }
    }

    private void sendBookToServer(boolean bl) throws IOException {
        if (this.bookIsUnsigned && this.bookIsModified && this.bookPages != null) {
            Object object;
            while (this.bookPages.tagCount() > 1) {
                object = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1);
                if (((String)object).length() != 0) break;
                this.bookPages.removeTag(this.bookPages.tagCount() - 1);
            }
            if (this.bookObj.hasTagCompound()) {
                object = this.bookObj.getTagCompound();
                ((NBTTagCompound)object).setTag("pages", this.bookPages);
            } else {
                this.bookObj.setTagInfo("pages", this.bookPages);
            }
            object = "MC|BEdit";
            if (bl) {
                object = "MC|BSign";
                this.bookObj.setTagInfo("author", new NBTTagString(this.editingPlayer.getName()));
                this.bookObj.setTagInfo("title", new NBTTagString(this.bookTitle.trim()));
                int n = 0;
                while (n < this.bookPages.tagCount()) {
                    String string = this.bookPages.getStringTagAt(n);
                    ChatComponentText chatComponentText = new ChatComponentText(string);
                    string = IChatComponent.Serializer.componentToJson(chatComponentText);
                    this.bookPages.set(n, new NBTTagString(string));
                    ++n;
                }
                this.bookObj.setItem(Items.written_book);
            }
            PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeItemStackToBuffer(this.bookObj);
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload((String)object, packetBuffer));
        }
    }

    static class NextPageButton
    extends GuiButton {
        private final boolean field_146151_o;

        @Override
        public void drawButton(Minecraft minecraft, int n, int n2) {
            if (this.visible) {
                boolean bl = n >= this.xPosition && n2 >= this.yPosition && n < this.xPosition + this.width && n2 < this.yPosition + this.height;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                minecraft.getTextureManager().bindTexture(bookGuiTextures);
                int n3 = 0;
                int n4 = 192;
                if (bl) {
                    n3 += 23;
                }
                if (!this.field_146151_o) {
                    n4 += 13;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, n3, n4, 23, 13);
            }
        }

        public NextPageButton(int n, int n2, int n3, boolean bl) {
            super(n, n2, n3, 23, 13, "");
            this.field_146151_o = bl;
        }
    }
}

