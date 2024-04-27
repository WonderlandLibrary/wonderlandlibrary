/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.ChatAllowedCharacters
 *  org.lwjgl.opengl.GL11
 */
package cn.hanabi.musicplayer.ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

public class CustomTextField {
    public String textString;
    public float x;
    public float y;
    public boolean isFocused;
    public boolean isTyping;
    public boolean back;
    public int ticks = 0;
    public int selectedChar;
    public float offset;
    public float newTextWidth;
    public float oldTextWidth;
    public float charWidth;
    public String oldString;
    public StringBuilder stringBuilder;

    public CustomTextField(String text) {
        this.textString = text;
        this.selectedChar = this.textString.length();
    }

    public void draw(float x, float y) {
        this.x = x;
        this.y = y;
        if (this.selectedChar > this.textString.length()) {
            this.selectedChar = this.textString.length();
        } else if (this.selectedChar < 0) {
            this.selectedChar = 0;
        }
        int selectedChar = this.selectedChar;
        float f = this.x;
        float f2 = this.y + 3.0f;
        float f3 = this.x + 115.0f;
        float f4 = this.y + 15.0f;
        int n = 0;
        GL11.glPushMatrix();
        int n2 = (int)this.x + 1;
        int n3 = (int)this.y + 3;
        int n4 = 0;
        Fonts.posterama35.drawString(this.textString, this.x + 1.5f - this.offset, this.y + 4.0f, Color.GRAY.getRGB());
        if (this.isFocused) {
            float width = Fonts.posterama35.getStringWidth(this.textString.substring(0, selectedChar)) + 4;
            float posX = this.x + width - this.offset;
            RenderUtils.drawRect(posX - 0.5f, this.y + 5.5f, posX, this.y + 12.5f, RenderUtils.reAlpha(Color.GRAY.getRGB(), this.ticks / 500 % 2 == 0 ? 1.0f : 0.0f));
        }
        GL11.glPopMatrix();
        this.tick();
    }

    public void tick() {
        this.ticks = this.isFocused ? ++this.ticks : 0;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseID) {
        boolean hovering = RenderUtils.isHovering(mouseX, mouseY, this.x, this.y + 3.0f, this.x + 115.0f, this.y + 15.0f);
        if (hovering && mouseID == 0 && !this.isFocused) {
            this.isFocused = true;
            this.selectedChar = this.textString.length();
        } else if (!hovering) {
            this.isFocused = false;
            this.isTyping = false;
        }
    }

    public void keyPressed(int key) {
        if (key == 1) {
            this.isFocused = false;
            this.isTyping = false;
        }
        if (this.isFocused) {
            if (GuiScreen.func_175279_e((int)key)) {
                this.textString = GuiScreen.func_146277_j();
                return;
            }
            switch (key) {
                case 28: {
                    this.isFocused = false;
                    this.isTyping = false;
                    this.ticks = 0;
                    break;
                }
                case 210: {
                    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
                    Transferable clipTf = sysClip.getContents(null);
                    if (clipTf != null && clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        try {
                            this.textString = (String)clipTf.getTransferData(DataFlavor.stringFlavor);
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    this.selectedChar = this.textString.length();
                    float width = Fonts.posterama35.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2;
                    float barOffset = width - this.offset;
                    if (!(barOffset > 111.0f)) break;
                    this.offset += barOffset - 111.0f;
                    break;
                }
                case 14: {
                    try {
                        if (this.selectedChar <= 0 || this.textString.length() == 0) break;
                        this.oldString = this.textString;
                        this.stringBuilder = new StringBuilder(this.oldString);
                        this.stringBuilder.charAt(this.selectedChar - 1);
                        this.stringBuilder.deleteCharAt(this.selectedChar - 1);
                        this.textString = ChatAllowedCharacters.func_71565_a((String)this.stringBuilder.toString());
                        --this.selectedChar;
                        if ((float)(Fonts.posterama35.getStringWidth(this.oldString) + 2) > 111.0f && this.offset > 0.0f) {
                            this.newTextWidth = Fonts.posterama35.getStringWidth(this.textString) + 2;
                            this.oldTextWidth = Fonts.posterama35.getStringWidth(this.oldString) + 2;
                            this.charWidth = this.newTextWidth - this.oldTextWidth;
                            if (this.newTextWidth <= 111.0f && this.oldTextWidth - 111.0f > this.charWidth) {
                                this.charWidth = 111.0f - this.oldTextWidth;
                            }
                            this.offset += this.charWidth;
                        }
                        if (this.selectedChar > this.textString.length()) {
                            this.selectedChar = this.textString.length();
                        }
                        this.ticks = 0;
                    }
                    catch (Exception exception) {}
                    break;
                }
                case 199: {
                    this.selectedChar = 0;
                    this.offset = 0.0f;
                    this.ticks = 0;
                    break;
                }
                case 203: {
                    if (this.selectedChar > 0) {
                        --this.selectedChar;
                    }
                    float width = Fonts.posterama35.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2;
                    float barOffset = width - this.offset;
                    if ((barOffset -= 2.0f) < 0.0f) {
                        this.offset += barOffset;
                    }
                    this.ticks = 0;
                    break;
                }
                case 205: {
                    float width;
                    float barOffset;
                    if (this.selectedChar < this.textString.length()) {
                        ++this.selectedChar;
                    }
                    if ((barOffset = (width = (float)(Fonts.posterama35.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2)) - this.offset) > 111.0f) {
                        this.offset += barOffset - 111.0f;
                    }
                    this.ticks = 0;
                    break;
                }
                case 207: {
                    this.selectedChar = this.textString.length();
                    float width = Fonts.posterama35.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2;
                    float barOffset = width - this.offset;
                    if (barOffset > 111.0f) {
                        this.offset += barOffset - 111.0f;
                    }
                    this.ticks = 0;
                }
            }
        }
    }

    public void charTyped(char c) {
        if (this.isFocused && ChatAllowedCharacters.func_71566_a((char)c)) {
            if (!this.isTyping) {
                this.isTyping = true;
            }
            this.oldString = this.textString;
            this.stringBuilder = new StringBuilder(this.oldString);
            this.stringBuilder.insert(this.selectedChar, c);
            this.textString = ChatAllowedCharacters.func_71565_a((String)this.stringBuilder.toString());
            if (this.selectedChar > this.textString.length()) {
                this.selectedChar = this.textString.length();
            } else if (this.selectedChar == this.oldString.length() && this.textString.startsWith(this.oldString)) {
                this.selectedChar += this.textString.length() - this.oldString.length();
            } else {
                ++this.selectedChar;
                float width = Fonts.posterama35.getStringWidth(this.textString.substring(0, this.selectedChar)) + 2;
                this.newTextWidth = width - this.offset;
                if (this.newTextWidth > 111.0f) {
                    this.offset += this.newTextWidth - 111.0f;
                }
            }
            this.newTextWidth = Fonts.posterama35.getStringWidth(this.textString) + 2;
            this.oldTextWidth = Fonts.posterama35.getStringWidth(this.oldString) + 2;
            if (this.newTextWidth > 111.0f) {
                if (this.oldTextWidth < 111.0f) {
                    this.oldTextWidth = 111.0f;
                }
                this.charWidth = this.newTextWidth - this.oldTextWidth;
                if (this.selectedChar == this.textString.length()) {
                    this.offset += this.charWidth;
                }
            }
            this.ticks = 0;
        }
    }
}

