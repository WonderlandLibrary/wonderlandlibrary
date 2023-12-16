// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.text;

import java.util.ArrayList;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import moonsense.utils.CustomFontRenderer;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.ui.utils.GuiUtils;
import moonsense.MoonsenseClient;
import java.util.function.Consumer;
import moonsense.ui.screen.AbstractGuiScreen;
import net.minecraft.util.ResourceLocation;
import moonsense.ui.elements.Element;

public class ElementModuleSearch extends Element
{
    private boolean focused;
    private final String placeholder;
    private String text;
    private int maxLength;
    private int cursorPos;
    private final ResourceLocation SEARCH;
    private AllowedCharacters allowedCharacters;
    
    public ElementModuleSearch(final int x, final int y, final int width, final int height, final AbstractGuiScreen parent) {
        super(x, y, width, height, false, null, parent);
        this.maxLength = Integer.MAX_VALUE;
        this.text = "";
        this.SEARCH = new ResourceLocation("streamlined/icons/search.png");
        this.placeholder = "Search";
    }
    
    public void allowedCharacters(final char... chars) {
        if (chars.length == 0) {
            this.allowedCharacters = null;
            return;
        }
        this.allowedCharacters = AllowedCharacters.from(chars);
    }
    
    public void allowedCharacters(final String s) {
        if (s == null) {
            this.allowedCharacters = null;
            return;
        }
        this.allowedCharacters = AllowedCharacters.from(s);
    }
    
    public void setConsumer(final Consumer<Element> consumer) {
        this.consumer = consumer;
    }
    
    public void setMaxLength(final int length) {
        this.maxLength = length;
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        if (this.text != null) {
            MoonsenseClient.textRenderer.drawString(this.text, this.getX() + 2, this.getY() + 2, 16777215);
        }
        if (this.focused) {
            final float width = MoonsenseClient.textRenderer.getWidth(this.text.substring(0, this.cursorPos));
            GuiUtils.drawRect(this.getX() + width + 2.0f, (float)(this.getY() + 2), this.getX() + width + 3.0f, (float)(this.getY() + this.height - 2), Integer.MAX_VALUE);
        }
        if (!this.focused && this.placeholder != null && !this.placeholder.isEmpty() && (this.text == null || this.text.isEmpty())) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(0.5f, 0.5f, 0.5f, 1.0f);
            this.mc.getTextureManager().bindTexture(this.SEARCH);
            final int b = 9;
            GuiUtils.drawModalRectWithCustomSizedTexture((float)(this.getX() + 1), this.getY() + this.height / 2 - 4 - 0.5f, 0.0f, 0.0f, 9, 9, 9.0f, 9.0f);
            final CustomFontRenderer titleRenderer = MoonsenseClient.titleRenderer;
            final String placeholder = this.placeholder;
            final int x = this.getX() + 10;
            final int n = this.getY() + this.height / 2;
            MoonsenseClient.titleRenderer.getClass();
            titleRenderer.drawString(placeholder, x, n - 9 / 2 - 1, this.hovered ? new Color(175, 175, 175, 110).getRGB() : new Color(150, 150, 150, 100).getRGB());
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 2.0f, this.hovered ? new Color(0, 0, 0, 125).getRGB() : new Color(0, 0, 0, 75).getRGB());
        GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 2.0f, 2.0f, MoonsenseClient.getMainColor(125));
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.focused) {
            if (keyCode == 14) {
                if (!this.text.isEmpty()) {
                    if (this.text.length() == 1) {
                        this.text = "";
                    }
                    else {
                        this.text = this.text.substring(0, this.text.length() - 1);
                    }
                    --this.cursorPos;
                }
                return;
            }
            if (Keyboard.isKeyDown(29) && keyCode == 30) {
                this.text = "";
                this.cursorPos = 0;
                return;
            }
            if (Keyboard.isKeyDown(29) && keyCode == 31) {
                this.text = String.valueOf(this.text) + "\u00c2§";
                ++this.cursorPos;
                return;
            }
            if (this.text.length() < this.maxLength) {
                if (this.allowedCharacters != null) {
                    if (this.allowedCharacters.contains(typedChar)) {
                        ++this.cursorPos;
                        this.text = String.valueOf(this.text) + typedChar;
                    }
                }
                else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    ++this.cursorPos;
                    this.text = String.valueOf(this.text) + typedChar;
                }
            }
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.focused = (this.enabled && this.hovered);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public boolean isFocused() {
        return this.focused;
    }
    
    public void setFocused(final boolean focused) {
        this.focused = focused;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    public int getCursorPos() {
        return this.cursorPos;
    }
    
    public void setCursorPos(final int pos) {
        this.cursorPos = pos;
    }
    
    public static class AllowedCharacters extends ArrayList<Character>
    {
        public static AllowedCharacters from(final char... chars) {
            final AllowedCharacters instance = new AllowedCharacters();
            for (final char c : chars) {
                instance.add(c);
            }
            return instance;
        }
        
        public static AllowedCharacters from(final String s) {
            final AllowedCharacters instance = new AllowedCharacters();
            for (int i = 0; i < s.length(); ++i) {
                instance.add(s.charAt(i));
            }
            return instance;
        }
    }
}
