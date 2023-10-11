package cow.milkgod.cheese.ttf;

import net.minecraft.client.*;
import java.awt.image.*;
import java.io.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import java.awt.*;
import java.awt.geom.*;

public class CustomFont
{
    private int texID;
    private int[] xPos;
    private int[] yPos;
    private int startChar;
    private int endChar;
    private FontMetrics metrics;
    public String fontName;
    public int fSize;
    
    public CustomFont(final Minecraft minecraft, final Object obj, final int i) {
        this(minecraft, obj, i, 31, 127);
    }
    
    public CustomFont(final Minecraft minecraft, final Object obj, final int i, final int j, final int k) {
        this.startChar = j;
        this.endChar = k;
        this.xPos = new int[k - j];
        this.yPos = new int[k - j];
        final BufferedImage bufferedimage = new BufferedImage(256, 256, 2);
        final Graphics g = bufferedimage.getGraphics();
        this.fSize = i;
        try {
            final Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if (obj instanceof String) {
                final String s = (String)obj;
                this.fontName = s;
                if (s.contains("/")) {
                    g.setFont(Font.createFont(0, new File(s)).deriveFont(i));
                }
                else {
                    g.setFont(new Font(s, 0, i));
                }
            }
            else if (obj instanceof InputStream) {
                g.setFont(Font.createFont(0, (InputStream)obj).deriveFont(i));
            }
            else if (obj instanceof File) {
                g.setFont(Font.createFont(0, (File)obj).deriveFont(i));
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, 256, 256);
        g.setColor(Color.white);
        this.metrics = g.getFontMetrics();
        int l = 2;
        int i2 = 2;
        for (int j2 = j; j2 < k; ++j2) {
            g.drawString("" + (char)j2, l, i2 + g.getFontMetrics().getAscent());
            this.xPos[j2 - j] = l;
            this.yPos[j2 - j] = i2 - this.metrics.getMaxDescent();
            l += this.metrics.stringWidth("" + (char)j2) + 2;
            if (l >= 250 - this.metrics.getMaxAdvance()) {
                l = 2;
                i2 += this.metrics.getMaxAscent() + this.metrics.getMaxDescent() + i / 2;
            }
        }
    }
    
    public void drawStringS(final Gui gui, final String s, final int i, final int j) {
        this.drawString(gui, s, i + 2, j + 2, true);
        this.drawString(gui, s, i, j, false);
    }
    
    public void drawGoodStringWithOutScalingAndShit(final Gui g, final String s, int i, int j, final int colour) {
        final float red = (colour >> 16 & 0xFF) / 255.0f;
        final float green = (colour >> 8 & 0xFF) / 255.0f;
        final float blue = (colour & 0xFF) / 255.0f;
        final Color c = new Color(red, green, blue, 1.0f);
        i *= 2;
        j *= 2;
        j -= 5;
        GL11.glPushMatrix();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
        GL11.glColor4f(red, green, blue, 255.0f);
        this.drawString(g, s, i, j, false);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
    }
    
    public void drawGoodString(final Gui g, final String s, double i, double j, int colour, final boolean flag) {
        if (flag) {
            colour = ((colour & 0xFCFCFC) >> 2 | (colour & 0xFF000000));
        }
        final float red = (colour >> 16 & 0xFF) / 255.0f;
        final float green = (colour >> 8 & 0xFF) / 255.0f;
        final float blue = (colour & 0xFF) / 255.0f;
        i *= 2.0;
        j *= 2.0;
        GL11.glPushMatrix();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
        GL11.glColor4f(red, green, blue, 255.0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        this.drawString(g, s, i, j, false);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
        GL11.glPopMatrix();
    }
    
    public void drawGoodStringS(final Gui g, final String s, int i, int j, final int colour) {
        i *= 2;
        j *= 2;
        j -= 5;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        this.drawString(g, s, i + 2, j + 2, true);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        final float red = (colour >> 16 & 0xFF) / 255.0f;
        final float green = (colour >> 8 & 0xFF) / 255.0f;
        final float blue = (colour & 0xFF) / 255.0f;
        final Color c = new Color(red, green, blue, 1.0f);
        GL11.glPushMatrix();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
        GL11.glColor4f(red, green, blue, 255.0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        this.drawString(g, s, i, j, false);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
    }
    
    public void drawGoodString(final Gui g, final String s, int i, int j, final Color c) {
        i *= 2;
        j *= 2;
        j -= 5;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
        GL11.glPushMatrix();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
        GL11.glColor4f(c.getRed() / 255, c.getGreen() / 255, c.getBlue() / 255, 1.0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        this.drawString(g, s, i, j, false);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
        GL11.glPopMatrix();
    }
    
    public void drawGoodStringS(final Gui g, final String s, int i, int j, final Color c) {
        i *= 2;
        j *= 2;
        j -= 5;
        GL11.glEnable(3106);
        GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), 255.0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        this.drawStringS(g, s, i, j);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glDisable(3106);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 255.0f);
    }
    
    public void drawString(final Gui gui, final String s, double i, double j, final boolean flag) {
        GL11.glEnable(3553);
        GL11.glBindTexture(3553, this.texID);
        if (flag) {
            GL11.glColor4f(0.2f, 0.2f, 0.2f, 255.0f);
        }
        final double k = i;
        final double realY = j;
        try {
            for (int l = 0; l < s.length(); ++l) {
                j = realY;
                char c1 = s.charAt(l);
                if (c1 == '|') {
                    --j;
                }
                else if (c1 == '[') {
                    --j;
                }
                else if (c1 == ']') {
                    --j;
                }
                else {
                    j = realY;
                }
                if (c1 == '\n') {
                    j += this.metrics.getAscent() + 4;
                    i = k;
                    ++l;
                }
                else {
                    if (c1 == '\u00e1' || c1 == '\u00e0' || c1 == '\u00e2' || c1 == '\u00e4' || c1 == '\u00c1' || c1 == '\u00c0' || c1 == '\u00c2' || c1 == '\u00c4') {
                        c1 = 'a';
                    }
                    if (c1 == '\u00e9' || c1 == '\u00e8' || c1 == '\u00ea' || c1 == '\u00eb' || c1 == '\u00c9' || c1 == '\u00c8' || c1 == '\u00ca' || c1 == '\u00cb') {
                        c1 = 'e';
                    }
                    if (c1 == '\u00ed' || c1 == '\u00ec' || c1 == '\u00ee' || c1 == '\u00ef' || c1 == '\u00cd' || c1 == '\u00cc' || c1 == '\u00ce' || c1 == '\u00cf') {
                        c1 = 'i';
                    }
                    if (c1 == '\u00f3' || c1 == '\u00f2' || c1 == '\u00f4' || c1 == '\u00f6' || c1 == '\u00d3' || c1 == '\u00d2' || c1 == '\u00d4' || c1 == '\u00d6') {
                        c1 = 'o';
                    }
                    if (c1 == '\u00fa' || c1 == '\u00f9' || c1 == '\u00fb' || c1 == '\u00fc' || c1 == '\u00da' || c1 == '\u00d9' || c1 == '\u00db' || c1 == '\u00dc') {
                        c1 = 'u';
                    }
                    if (c1 == '§') {
                        ++l;
                        final char c2 = s.charAt(l);
                        if (c2 == 'a' || c2 == 'A' || c2 == 'g') {
                            if (!flag) {
                                GL11.glColor4f(0.25f, 1.0f, 0.25f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.05f, 0.2f, 0.05f, 1.0f);
                            }
                        }
                        if (c2 == 'b' || c2 == 'B') {
                            if (!flag) {
                                GL11.glColor4f(0.25f, 1.0f, 1.0f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.05f, 0.2f, 0.2f, 1.0f);
                            }
                        }
                        if (c2 == 'c' || c2 == 'C') {
                            if (!flag) {
                                GL11.glColor4f(1.0f, 0.25f, 0.25f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.2f, 0.05f, 0.05f, 1.0f);
                            }
                        }
                        if (c2 == 'd' || c2 == 'D') {
                            if (!flag) {
                                GL11.glColor4f(1.0f, 0.25f, 1.0f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.2f, 0.05f, 0.2f, 1.0f);
                            }
                        }
                        if (c2 == 'e' || c2 == 'E') {
                            if (!flag) {
                                GL11.glColor4f(1.0f, 1.0f, 0.25f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.2f, 0.2f, 0.05f, 1.0f);
                            }
                        }
                        if (c2 == 'f' || c2 == 'F') {
                            if (!flag) {
                                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
                            }
                        }
                        if (c2 == 'q') {
                            GL11.glColor4f(255.0f, 0.0f, 0.0f, 255.0f);
                        }
                        if (c2 == '0') {
                            GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
                        }
                        if (c2 == '1') {
                            if (!flag) {
                                GL11.glColor4f(0.0f, 0.0f, 0.75f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.0f, 0.0f, 0.1f, 1.0f);
                            }
                        }
                        if (c2 == '2') {
                            if (!flag) {
                                GL11.glColor4f(0.0f, 0.75f, 0.0f, 1.0f);
                            }
                            else {
                                GL11.glColor3f(0.0f, 0.1f, 0.0f);
                            }
                        }
                        if (c2 == '3') {
                            if (!flag) {
                                GL11.glColor4f(0.0f, 0.75f, 0.75f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.0f, 0.1f, 0.1f, 1.0f);
                            }
                        }
                        if (c2 == '4') {
                            if (!flag) {
                                GL11.glColor4f(0.75f, 0.0f, 0.0f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.1f, 0.0f, 0.0f, 1.0f);
                            }
                        }
                        if (c2 == '5') {
                            if (!flag) {
                                GL11.glColor4f(0.75f, 0.0f, 0.75f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.1f, 0.0f, 0.1f, 1.0f);
                            }
                        }
                        if (c2 == '6') {
                            if (!flag) {
                                GL11.glColor4f(1.0f, 0.75f, 0.0f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.1f, 0.1f, 0.0f, 1.0f);
                            }
                        }
                        if (c2 == '7') {
                            if (!flag) {
                                GL11.glColor4f(0.75f, 0.75f, 0.75f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
                            }
                        }
                        if (c2 == '8') {
                            if (!flag) {
                                GL11.glColor4f(0.25f, 0.25f, 0.25f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.05f, 0.05f, 0.05f, 1.0f);
                            }
                        }
                        if (c2 == '9') {
                            if (!flag) {
                                GL11.glColor4f(0.25f, 0.25f, 1.0f, 1.0f);
                            }
                            else {
                                GL11.glColor4f(0.05f, 0.05f, 0.2f, 1.0f);
                            }
                            if (c2 == 'x') {
                                if (!flag) {
                                    GL11.glColor4f(0.2f, 0.2f, 0.5f, 1.0f);
                                }
                                else {
                                    GL11.glColor4f(0.05f, 0.05f, 0.05f, 1.0f);
                                }
                            }
                        }
                    }
                    else if (c1 <= '~') {
                        this.drawChar(gui, c1, i, j);
                        i = (int)(i + this.metrics.getStringBounds("" + c1, null).getWidth());
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public FontMetrics getMetrics() {
        return this.metrics;
    }
    
    public int getStringWidth(final String s) {
        return (int)this.getBounds(s).getWidth();
    }
    
    public int getStringHeight(final String s) {
        return (int)this.getBounds(s).getHeight();
    }
    
    private Rectangle getBounds(final String s) {
        int i = 0;
        int j = 0;
        int k = 0;
        for (int l = 0; l < s.length(); ++l) {
            final char c = s.charAt(l);
            if (c == '\\') {
                final char c2 = s.charAt(l + 1);
                if (c2 == 'n') {
                    j += this.metrics.getAscent() + 2;
                    if (k > i) {
                        i = k;
                    }
                    k = 0;
                }
                ++l;
            }
            else {
                k += this.metrics.stringWidth("" + c);
            }
        }
        if (k > i) {
            i = k;
        }
        j += this.metrics.getAscent();
        return new Rectangle(0, 0, i, j);
    }
    
    private void drawChar(final Gui gui, final char c, final double i, final double j) {
        final Rectangle2D rectangle2d = this.metrics.getStringBounds("" + c, null);
    }
}
