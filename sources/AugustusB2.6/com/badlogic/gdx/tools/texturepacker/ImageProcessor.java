// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.tools.texturepacker;

import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.awt.image.WritableRaster;
import java.util.regex.Matcher;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Image;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.awt.image.BufferedImage;

public final class ImageProcessor
{
    private static final BufferedImage emptyImage;
    private static Pattern indexPattern;
    private final TexturePacker.Settings settings;
    private final HashMap<String, TexturePacker.Rect> crcs;
    private final Array<TexturePacker.Rect> rects;
    private float scale;
    private TexturePacker.Resampling resampling;
    
    public ImageProcessor(final TexturePacker.Settings settings) {
        this.crcs = new HashMap<String, TexturePacker.Rect>();
        this.rects = new Array<TexturePacker.Rect>();
        this.scale = 1.0f;
        this.resampling = TexturePacker.Resampling.bicubic;
        this.settings = settings;
    }
    
    public final TexturePacker.Rect addImage(final BufferedImage image, String name) {
        final TexturePacker.Rect rect;
        if ((rect = this.processImage(image, name)) == null) {
            if (!this.settings.silent) {
                System.out.println("Ignoring blank input image: " + name);
            }
            return null;
        }
        if (this.settings.alias) {
            name = hash(rect.getImage(this));
            final TexturePacker.Rect existing;
            if ((existing = this.crcs.get(name)) != null) {
                if (!this.settings.silent) {
                    name = rect.name + ((rect.index != -1) ? ("_" + rect.index) : "");
                    final String existingName = existing.name + ((existing.index != -1) ? ("_" + existing.index) : "");
                    System.out.println(name + " (alias of " + existingName + ")");
                }
                existing.aliases.add(new TexturePacker.Alias(rect));
                return null;
            }
            this.crcs.put(name, rect);
        }
        this.rects.add(rect);
        return rect;
    }
    
    public final void setScale(final float scale) {
        this.scale = scale;
    }
    
    public final void setResampling(final TexturePacker.Resampling resampling) {
        this.resampling = resampling;
    }
    
    public final Array<TexturePacker.Rect> getImages() {
        return this.rects;
    }
    
    public final void clear() {
        this.rects.clear();
        this.crcs.clear();
    }
    
    protected final TexturePacker.Rect processImage(BufferedImage rect, String name) {
        if (this.scale <= 0.0f) {
            throw new IllegalArgumentException("scale cannot be <= 0: " + this.scale);
        }
        int width = var_1_13F.getWidth();
        int height = var_1_13F.getHeight();
        if (var_1_13F.getType() != 6) {
            final BufferedImage newImage;
            (newImage = new BufferedImage(width, height, 6)).getGraphics().drawImage(var_1_13F, 0, 0, null);
            var_1_13F = newImage;
        }
        final boolean isPatch = name.endsWith(".9");
        int[] splits = null;
        int[] pads = null;
        if (isPatch) {
            name = name.substring(0, name.length() - 2);
            splits = this.getSplits(var_1_13F, name);
            pads = this.getPads(var_1_13F, name, splits);
            width -= 2;
            height -= 2;
            final BufferedImage newImage2;
            (newImage2 = new BufferedImage(width, height, 6)).getGraphics().drawImage(var_1_13F, 0, 0, width, height, 1, 1, width + 1, height + 1, null);
            var_1_13F = newImage2;
        }
        if (this.scale != 1.0f) {
            width = Math.max(1, Math.round(width * this.scale));
            height = Math.max(1, Math.round(height * this.scale));
            final BufferedImage newImage2 = new BufferedImage(width, height, 6);
            if (this.scale < 1.0f) {
                newImage2.getGraphics().drawImage(var_1_13F.getScaledInstance(width, height, 16), 0, 0, null);
            }
            else {
                final Graphics2D g;
                (g = (Graphics2D)newImage2.getGraphics()).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, this.resampling.value);
                g.drawImage(var_1_13F, 0, 0, width, height, null);
            }
            var_1_13F = newImage2;
        }
        int index = -1;
        final Matcher matcher;
        if (this.settings.useIndexes && (matcher = ImageProcessor.indexPattern.matcher(name)).matches()) {
            name = matcher.group(1);
            index = Integer.parseInt(matcher.group(2));
        }
        if (isPatch) {
            (rect = new TexturePacker.Rect(var_1_13F, 0, 0, width, height, true)).splits = splits;
            rect.pads = pads;
            rect.canRotate = false;
        }
        else if ((rect = this.stripWhitespace$7a007150((BufferedImage)rect)) == null) {
            return null;
        }
        rect.name = name;
        rect.index = index;
        return rect;
    }
    
    private TexturePacker.Rect stripWhitespace$7a007150(final BufferedImage source) {
        source.getAlphaRaster();
        return new TexturePacker.Rect(source, 0, 0, source.getWidth(), source.getHeight(), false);
    }
    
    private static String splitError(final int x, final int y, final int[] rgba, final String name) {
        throw new RuntimeException("Invalid " + name + " ninepatch split pixel at " + x + ", " + y + ", rgba: " + rgba[0] + ", " + rgba[1] + ", " + rgba[2] + ", " + rgba[3]);
    }
    
    private int[] getSplits(final BufferedImage image, final String name) {
        final WritableRaster raster;
        int startX = getSplitPoint(raster = image.getRaster(), name, 1, 0, true, true);
        int endX = getSplitPoint(raster, name, startX, 0, false, true);
        int startY = getSplitPoint(raster, name, 0, 1, true, false);
        int endY = getSplitPoint(raster, name, 0, startY, false, false);
        getSplitPoint(raster, name, endX + 1, 0, true, true);
        getSplitPoint(raster, name, 0, endY + 1, true, false);
        if (startX == 0 && endX == 0 && startY == 0 && endY == 0) {
            return null;
        }
        if (startX != 0) {
            --startX;
            endX = raster.getWidth() - 2 - (endX - 1);
        }
        else {
            endX = raster.getWidth() - 2;
        }
        if (startY != 0) {
            --startY;
            endY = raster.getHeight() - 2 - (endY - 1);
        }
        else {
            endY = raster.getHeight() - 2;
        }
        if (this.scale != 1.0f) {
            startX = Math.round(startX * this.scale);
            endX = Math.round(endX * this.scale);
            startY = Math.round(startY * this.scale);
            endY = Math.round(endY * this.scale);
        }
        return new int[] { startX, endX, startY, endY };
    }
    
    private int[] getPads(final BufferedImage image, final String name, final int[] splits) {
        final WritableRaster raster;
        final int bottom = (raster = image.getRaster()).getHeight() - 1;
        final int right = raster.getWidth() - 1;
        int startX = getSplitPoint(raster, name, 1, bottom, true, true);
        int startY = getSplitPoint(raster, name, right, 1, true, false);
        int endX = 0;
        int endY = 0;
        if (startX != 0) {
            endX = getSplitPoint(raster, name, startX + 1, bottom, false, true);
        }
        if (startY != 0) {
            endY = getSplitPoint(raster, name, right, startY + 1, false, false);
        }
        getSplitPoint(raster, name, endX + 1, bottom, true, true);
        getSplitPoint(raster, name, right, endY + 1, true, false);
        if (startX == 0 && endX == 0 && startY == 0 && endY == 0) {
            return null;
        }
        if (startX == 0 && endX == 0) {
            startX = -1;
            endX = -1;
        }
        else if (startX > 0) {
            --startX;
            endX = raster.getWidth() - 2 - (endX - 1);
        }
        else {
            endX = raster.getWidth() - 2;
        }
        if (startY == 0 && endY == 0) {
            startY = -1;
            endY = -1;
        }
        else if (startY > 0) {
            --startY;
            endY = raster.getHeight() - 2 - (endY - 1);
        }
        else {
            endY = raster.getHeight() - 2;
        }
        if (this.scale != 1.0f) {
            startX = Math.round(startX * this.scale);
            endX = Math.round(endX * this.scale);
            startY = Math.round(startY * this.scale);
            endY = Math.round(endY * this.scale);
        }
        final int[] pads = { startX, endX, startY, endY };
        if (splits != null && Arrays.equals(pads, splits)) {
            return null;
        }
        return pads;
    }
    
    private static int getSplitPoint(final WritableRaster raster, final String name, int startX, int startY, final boolean startPoint, final boolean xAxis) {
        final int[] rgba = new int[4];
        int next = xAxis ? startX : startY;
        final int end = xAxis ? raster.getWidth() : raster.getHeight();
        final int breakA = startPoint ? 255 : 0;
        startX = startX;
        startY = startY;
        while (next != end) {
            if (xAxis) {
                startX = next;
            }
            else {
                startY = next;
            }
            raster.getPixel(startX, startY, rgba);
            if (rgba[3] == breakA) {
                return next;
            }
            if (!startPoint && (rgba[0] != 0 || rgba[1] != 0 || rgba[2] != 0 || rgba[3] != 255)) {
                splitError(startX, startY, rgba, name);
            }
            ++next;
        }
        return 0;
    }
    
    private static String hash(BufferedImage image) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA1");
            final int width = image.getWidth();
            final int height = image.getHeight();
            if (image.getType() != 2) {
                final BufferedImage newImage;
                (newImage = new BufferedImage(width, height, 2)).getGraphics().drawImage(image, 0, 0, null);
                image = newImage;
            }
            final WritableRaster raster = image.getRaster();
            final int[] pixels = new int[width];
            for (int y = 0; y < height; ++y) {
                raster.getDataElements(0, y, width, 1, pixels);
                for (int x = 0; x < width; ++x) {
                    hash(digest, pixels[x]);
                }
            }
            hash(digest, width);
            hash(digest, height);
            return new BigInteger(1, digest.digest()).toString(16);
        }
        catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static void hash(final MessageDigest digest, final int value) {
        digest.update((byte)(value >> 24));
        digest.update((byte)(value >> 16));
        digest.update((byte)(value >> 8));
        digest.update((byte)value);
    }
    
    static {
        emptyImage = new BufferedImage(1, 1, 6);
        ImageProcessor.indexPattern = Pattern.compile("(.+)_(\\d+)$");
    }
}
