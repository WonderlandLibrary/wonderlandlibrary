package me.aquavit.liquidsense.ui.client.gui.elements;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.login.MinecraftAccount;
import me.aquavit.liquidsense.utils.login.UserUtils;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

import java.awt.*;

public abstract class GuiButtonSlot extends MinecraftInstance {

    public int width;
    public int height;
    /** The top of the slot container. Affects the overlays and scrolling. */
    public int top;
    /** The bottom of the slot container. Affects the overlays and scrolling. */
    public int bottom;
    public int right;
    public int left;
    /** The height of a slot. */
    public final int slotHeight;
    /** The buttonID of the button used to scroll up */
    private int scrollUpButtonID;
    /** The buttonID of the button used to scroll down */
    private int scrollDownButtonID;
    protected int mouseX;
    protected int mouseY;
    /** Where the mouse was in the window when you first clicked to scroll */
    protected int initialClickY = -2;
    /**
     * What to multiply the amount you moved your mouse by (used for slowing down scrolling when over the items and not
     * on the scroll bar)
     */
    protected float scrollMultiplier;
    /** How far down this slot has been scrolled */
    protected float amountScrolled;
    /** The element in the list that was selected */
    protected int selectedElement = -1;
    /** The time when this button was last clicked. */
    protected long lastClicked;
    protected boolean field_178041_q = true;
    /** Set to true if a selected element in this gui will show an outline box */
    protected boolean showSelectionBox = true;
    protected boolean hasListHeader;
    public int headerPadding;
    private boolean enabled = true;

    public GuiButtonSlot(int width, int height, int topIn, int bottomIn, int slotHeightIn) {
        this.width = width;
        this.height = height;
        this.top = topIn;
        this.bottom = bottomIn;
        this.slotHeight = slotHeightIn;
        this.left = 0;
        this.right = width;
    }

    protected int getSize() {
        return LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size();
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected abstract void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY);

    /**
     * Returns true if the element passed in is currently selected
     */
    protected abstract boolean isSelected(int slotIndex);

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }

    protected abstract void drawBackground();

    protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_)
    {
    }

    /**
     * Handles drawing a list's header row.
     */
    protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_)
    {
    }

    protected void func_148132_a(int p_148132_1_, int p_148132_2_)
    {
    }

    protected void func_148142_b(int p_148142_1_, int p_148142_2_)
    {
    }

    /**
     * Registers the IDs that can be used for the scrollbar's up/down buttons.
     */
    public void registerScrollButtons(int scrollUpButtonIDIn, int scrollDownButtonIDIn)
    {
        this.scrollUpButtonID = scrollUpButtonIDIn;
        this.scrollDownButtonID = scrollDownButtonIDIn;
    }

    /**
     * Stop the thing from scrolling out of bounds
     */
    protected void bindAmountScrolled()
    {
        this.amountScrolled = MathHelper.clamp_float(this.amountScrolled, 0.0F, (float)this.func_148135_f());
    }

    public int func_148135_f()
    {
        return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
    }

    public boolean isMouseYWithinSlotBounds(int p_148141_1_)
    {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }

    /**
     * Scrolls the slot by the given amount. A positive value scrolls down, and a negative value scrolls up.
     */
    public void scrollBy(int amount)
    {
        this.amountScrolled += (float)amount;
        this.bindAmountScrolled();
        this.initialClickY = -2;
    }

    public void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == this.scrollUpButtonID)
            {
                this.amountScrolled -= (float)(this.slotHeight * 2 / 3);
                this.initialClickY = -2;
                this.bindAmountScrolled();
            }
            else if (button.id == this.scrollDownButtonID)
            {
                this.amountScrolled += (float)(this.slotHeight * 2 / 3);
                this.initialClickY = -2;
                this.bindAmountScrolled();
            }
        }
    }

    public void handleMouseInput()
    {
        if (this.isMouseYWithinSlotBounds(this.mouseY))
        {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom)
            {
                int i = (this.width - this.getListWidth()) / 2;
                int j = (this.width + this.getListWidth()) / 2;
                int k = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                int l = k / this.slotHeight;

                if (l < this.getSize() && this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0)
                {
                    this.elementClicked(l, false, this.mouseX, this.mouseY);
                    this.selectedElement = l;
                }
                else if (this.mouseX >= i && this.mouseX <= j && k < 0)
                {
                    this.func_148132_a(this.mouseX - i, this.mouseY - this.top + (int)this.amountScrolled - 4);
                }
            }

            if (Mouse.isButtonDown(0) && this.getEnabled())
            {
                if (this.initialClickY == -1)
                {
                    boolean flag1 = true;

                    if (this.mouseY >= this.top && this.mouseY <= this.bottom)
                    {
                        int j2 = (this.width - this.getListWidth()) / 2;
                        int k2 = (this.width + this.getListWidth()) / 2;
                        int l2 = this.mouseY - this.top - this.headerPadding + (int)this.amountScrolled - 4;
                        int i1 = l2 / this.slotHeight;

                        if (i1 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i1 >= 0 && l2 >= 0)
                        {
                            boolean flag = i1 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(i1, flag, this.mouseX, this.mouseY);
                            this.selectedElement = i1;
                            this.lastClicked = Minecraft.getSystemTime();
                        }
                        else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0)
                        {
                            this.func_148132_a(this.mouseX - j2, this.mouseY - this.top + (int)this.amountScrolled - 4);
                            flag1 = false;
                        }

                        int i3 = this.getScrollBarX();
                        int j1 = i3 + 6;

                        if (this.mouseX >= i3 && this.mouseX <= j1)
                        {
                            this.scrollMultiplier = -1.0F;
                            int k1 = this.func_148135_f();

                            if (k1 < 1)
                            {
                                k1 = 1;
                            }

                            int l1 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
                            l1 = MathHelper.clamp_int(l1, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (float)(this.bottom - this.top - l1) / (float)k1;
                        }
                        else
                        {
                            this.scrollMultiplier = 1.0F;
                        }

                        if (flag1)
                        {
                            this.initialClickY = this.mouseY;
                        }
                        else
                        {
                            this.initialClickY = -2;
                        }
                    }
                    else
                    {
                        this.initialClickY = -2;
                    }
                }
                else if (this.initialClickY >= 0)
                {
                    this.amountScrolled -= (float)(this.mouseY - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = this.mouseY;
                }
            }
            else
            {
                this.initialClickY = -1;
            }

            int i2 = Mouse.getEventDWheel();

            if (i2 != 0)
            {
                if (i2 > 0)
                {
                    i2 = -1;
                }
                else if (i2 < 0)
                {
                    i2 = 1;
                }

                this.amountScrolled += (float)(i2 * this.slotHeight / 2);
            }
        }
    }

    public void setEnabled(boolean enabledIn)
    {
        this.enabled = enabledIn;
    }

    public boolean getEnabled()
    {
        return this.enabled;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return 220;
    }

    public void drawScreen(int mouseXIn, int mouseYIn, float p_148128_3_) {
        if (this.field_178041_q) {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int l = this.top + 4 - (int)this.amountScrolled;

            if (this.hasListHeader)
            {
                this.drawListHeader(k, l, tessellator);
            }
            Fonts.font30.drawStringWithShadow(LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size() + " Alts",(width / 2) - 110, l - 30, Color.WHITE.getRGB());

            Fonts.font16.drawStringWithShadow("Name: " + (mc.getSession().getUsername()),
                    (width / 2) - 110 + 20 +Fonts.font30.getStringWidth(LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size() + " Alts"),
                    l - 23, Color.WHITE.getRGB());

            Fonts.font16.drawStringWithShadow("Type: " + (UserUtils.isValidTokenOffline(mc.getSession().getToken()) ? "§aOnline" : "§7Cracked"),
                    (width / 2) - 110 + 40 +Fonts.font30.getStringWidth(LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size() + " Alts") + Fonts.font16.getStringWidth("Name: " + (mc.getSession().getUsername())),
                    l - 23, Color.WHITE.getRGB());

            this.drawSelectionBox(k, l, mouseXIn, mouseYIn);

            this.func_148142_b(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    /**
     * Draws the selection box around the selected slot element.
     */
    protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int mouseXIn, int mouseYIn) {
        for (int j = 0; j < LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.size(); ++j) {
            final MinecraftAccount minecraftAccount = LiquidSense.fileManager.accountsConfig.altManagerMinecraftAccounts.get(j);
            int k = p_148120_2_ + j * this.slotHeight + this.headerPadding;
            int l = this.slotHeight - 4;

            if (k > this.bottom || k + l < this.top) {
                this.func_178040_a(j, p_148120_1_, k);
            }
            int i1 = this.left + (this.width / 2 - this.getListWidth() / 2);
            int j1 = this.left + this.width / 2 + this.getListWidth() / 2;
            RenderUtils.drawRect(j1 - 1, k + l + 2, i1 + 1, k - 2, new Color(1,1,1, 80));
            RenderUtils.drawRect(i1 - 1, k + l + 2, i1 + 1, k - 2, new Color(17, 211,255, 255));
            Fonts.font20.drawCenteredString(minecraftAccount.getAccountName() == null ? minecraftAccount.getName() : minecraftAccount.getAccountName(), (width / 2), k + 2, Color.WHITE.getRGB(), true);
            Fonts.font20.drawCenteredString(minecraftAccount.isCracked() ? "Cracked" : (minecraftAccount.getAccountName() == null ? "Premium" : minecraftAccount.getName()), (width / 2), k + 15, Color.WHITE.getRGB(), true);
            if (this.showSelectionBox && this.isSelected(j)) {
                RenderUtils.drawRect(j1 - 1, k + l + 2, i1 + 1, k - 2, new Color(255,255,255, 120));
                RenderUtils.drawRect(i1 - 1, k + l + 2, i1 + 1, k - 2, new Color(129, 147,255, 255));
                Fonts.font20.drawCenteredString(minecraftAccount.getAccountName() == null ? minecraftAccount.getName() : minecraftAccount.getAccountName(), (width / 2), k + 2, new Color(150, 150, 150).darker().getRGB(), true);
                Fonts.font20.drawCenteredString(minecraftAccount.isCracked() ? "Cracked" : (minecraftAccount.getAccountName() == null ? "Premium" : minecraftAccount.getName()), (width / 2), k + 15, new Color(150, 150, 150).darker().getRGB(), true);
            }


            this.drawSlot(j, p_148120_1_, k, l, mouseXIn, mouseYIn);

        }
    }

    protected void drawSlot(int id, int x, int y, int var4, int var5, int var6) {
    }

    protected int getScrollBarX()
    {
        return this.width / 2 + 124;
    }

}
