// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.tools.texturepacker;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.FloatArray;
import java.awt.RenderingHints;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;
import java.io.Writer;
import java.util.Iterator;
import com.badlogic.gdx.graphics.Texture;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.metadata.IIOMetadata;
import java.util.List;
import java.awt.image.RenderedImage;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.image.ImageObserver;
import java.awt.Image;
import com.badlogic.gdx.files.FileHandle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import com.badlogic.gdx.math.MathUtils;
import java.io.File;
import com.badlogic.gdx.utils.Array;

public final class TexturePacker
{
    private String rootPath;
    private final Settings settings;
    private Packer packer;
    private final ImageProcessor imageProcessor;
    private final Array<InputImage> inputImages;
    private ProgressListener progress;
    
    public TexturePacker(File rootDir, Settings settings) {
        this.inputImages = new Array<InputImage>();
        this.settings = settings;
        if (settings.pot) {
            if (settings.maxWidth != MathUtils.nextPowerOfTwo(settings.maxWidth)) {
                throw new RuntimeException("If pot is true, maxWidth must be a power of two: " + settings.maxWidth);
            }
            if (settings.maxHeight != MathUtils.nextPowerOfTwo(settings.maxHeight)) {
                throw new RuntimeException("If pot is true, maxHeight must be a power of two: " + settings.maxHeight);
            }
        }
        this.packer = new MaxRectsPacker(settings);
        settings = settings;
        this.imageProcessor = new ImageProcessor((Settings)settings);
        rootDir = rootDir;
        settings = this;
        if (rootDir == null) {
            ((TexturePacker)settings).rootPath = null;
            return;
        }
        try {
            ((TexturePacker)settings).rootPath = rootDir.getCanonicalPath();
        }
        catch (IOException ex) {
            ((TexturePacker)settings).rootPath = rootDir.getAbsolutePath();
        }
        final Object o = settings;
        ((TexturePacker)o).rootPath = ((TexturePacker)o).rootPath.replace('\\', '/');
        if (!((TexturePacker)settings).rootPath.endsWith("/")) {
            final StringBuilder sb = new StringBuilder();
            final Object o2 = settings;
            ((TexturePacker)o2).rootPath = sb.append(((TexturePacker)o2).rootPath).append("/").toString();
        }
    }
    
    public final void addImage(final BufferedImage image, final String name) {
        final InputImage inputImage;
        (inputImage = new InputImage()).image = image;
        inputImage.name = name;
        this.inputImages.add(inputImage);
    }
    
    public final void pack(final File outputDir, String packFileName) {
        if (packFileName.endsWith(this.settings.atlasExtension)) {
            packFileName = packFileName.substring(0, packFileName.length() - this.settings.atlasExtension.length());
        }
        outputDir.mkdirs();
        if (this.progress == null) {
            this.progress = new ProgressListener(this) {
                @Override
                public final void progress(final float progress) {
                }
            };
        }
        this.progress.start(1.0f);
        for (int n = this.settings.scale.length, i = 0; i < n; ++i) {
            this.progress.start(1.0f / n);
            this.imageProcessor.setScale(this.settings.scale[i]);
            if (this.settings.scaleResampling != null && this.settings.scaleResampling.length > i && this.settings.scaleResampling[i] != null) {
                this.imageProcessor.setResampling(this.settings.scaleResampling[i]);
            }
            this.progress.start(0.35f);
            this.progress.count = 0;
            ProgressListener progress;
            for (int ii = 0, nn = this.inputImages.size; ii < nn; ++ii, progress = this.progress, ++progress.count) {
                final InputImage inputImage = this.inputImages.get(ii);
                this.imageProcessor.addImage(inputImage.image, inputImage.name);
                if (this.progress.update(ii + 1, nn)) {
                    return;
                }
            }
            this.progress.end();
            this.progress.start(0.19f);
            this.progress.count = 0;
            this.imageProcessor.getImages();
            final Array<Page> pages = this.packer.pack(this.progress, this.imageProcessor.getImages());
            this.progress.end();
            this.progress.start(0.45f);
            this.progress.count = 0;
            final Settings settings = this.settings;
            final String s = packFileName;
            final int n2 = i;
            String s2 = s;
            final Settings settings2 = settings;
            if (settings.scaleSuffix[n2].length() > 0) {
                s2 += settings2.scaleSuffix[n2];
            }
            else {
                final float f = settings2.scale[n2];
                if (settings2.scale.length != 1) {
                    final StringBuilder sb = new StringBuilder();
                    final float n3 = f;
                    s2 = sb.append((n3 == (int)n3) ? Integer.toString((int)f) : Float.toString(f)).append("/").append(s2).toString();
                }
            }
            final String scaledPackFileName = s2;
            this.writeImages(outputDir, scaledPackFileName, pages);
            this.progress.end();
            this.progress.start(0.01f);
            try {
                this.writePackFile(outputDir, scaledPackFileName, pages);
            }
            catch (IOException ex) {
                throw new RuntimeException("Error writing pack file.", ex);
            }
            this.imageProcessor.clear();
            this.progress.end();
            this.progress.end();
            if (this.progress.update(i + 1, n)) {
                return;
            }
        }
        this.progress.end();
    }
    
    private void writeImages(File imageName, String scaledPackFileName, final Array<Page> pages) {
        packDir = (outputDir = new File(outputDir, scaledPackFileName)).getParentFile();
        imageName = outputDir.getName();
        int fileIndex = 0;
        for (int p = 0, pn = pages.size; p < pn; ++p) {
            final Page page;
            int width = (page = pages.get(p)).width;
            int height = page.height;
            if (this.settings.edgePadding) {
                final int edgePadX = this.settings.paddingX;
                final int edgePadY = this.settings.paddingY;
                page.x = edgePadX;
                page.y = edgePadY;
                width += edgePadX << 1;
                height += edgePadY << 1;
            }
            if (this.settings.pot) {
                width = MathUtils.nextPowerOfTwo(width);
                height = MathUtils.nextPowerOfTwo(height);
            }
            width = Math.max(this.settings.minWidth, width);
            height = Math.max(this.settings.minHeight, height);
            page.imageWidth = width;
            page.imageHeight = height;
            File outputFile;
            while ((outputFile = new File((File)packDir, (String)imageName + ((fileIndex++ == 0) ? "" : Integer.valueOf(fileIndex)) + "." + this.settings.outputFormat)).exists()) {}
            new FileHandle(outputFile).parent().mkdirs();
            page.imageName = outputFile.getName();
            final int width2 = width;
            final int height2 = height;
            int imageType = 0;
            switch (this.settings.format) {
                case RGBA8888:
                case RGBA4444: {
                    imageType = 2;
                    break;
                }
                case RGB565:
                case RGB888: {
                    imageType = 1;
                    break;
                }
                case Alpha: {
                    imageType = 10;
                    break;
                }
                default: {
                    throw new RuntimeException("Unsupported format: " + this.settings.format);
                }
            }
            BufferedImage canvas;
            (canvas = new BufferedImage(width2, height2, imageType)).getGraphics();
            if (!this.settings.silent) {
                System.out.println("Writing " + canvas.getWidth() + "x" + canvas.getHeight() + ": " + outputFile);
            }
            this.progress.start(1.0f / pn);
            for (int r = 0, rn = page.outputRects.size; r < rn; ++r) {
                final Rect rect;
                final BufferedImage image;
                final int iw = (image = (rect = page.outputRects.get(r)).getImage(this.imageProcessor)).getWidth();
                final int ih = image.getHeight();
                final int rectX = page.x + rect.x;
                final int rectY = page.y + page.height - rect.y - (rect.height - this.settings.paddingY);
                copy(image, 0, 0, iw, ih, canvas, rectX, rectY, rect.rotated);
                if (this.progress.update(r + 1, rn)) {
                    return;
                }
            }
            this.progress.end();
            if (this.settings.bleed && !this.settings.outputFormat.equalsIgnoreCase("jpg") && !this.settings.outputFormat.equalsIgnoreCase("jpeg")) {
                (canvas = new ColorBleedEffect().processImage(canvas, this.settings.bleedIterations)).getGraphics();
            }
            ImageOutputStream ios = null;
            try {
                if (this.settings.outputFormat.equalsIgnoreCase("jpg") || this.settings.outputFormat.equalsIgnoreCase("jpeg")) {
                    final BufferedImage newImage;
                    (newImage = new BufferedImage(canvas.getWidth(), canvas.getHeight(), 5)).getGraphics().drawImage(canvas, 0, 0, null);
                    canvas = newImage;
                    final ImageWriter writer;
                    final ImageWriteParam param;
                    (param = (writer = ImageIO.getImageWritersByFormatName("jpg").next()).getDefaultWriteParam()).setCompressionMode(2);
                    param.setCompressionQuality(this.settings.jpegQuality);
                    ios = ImageIO.createImageOutputStream(outputFile);
                    writer.setOutput(ios);
                    writer.write(null, new IIOImage(canvas, null, null), param);
                }
                else {
                    ImageIO.write(canvas, "png", outputFile);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException("Error writing file: " + outputFile, ex);
            }
            finally {
                if (ios != null) {
                    try {
                        ios.close();
                    }
                    catch (Exception ex2) {}
                }
            }
            if (this.progress.update(p + 1, pn)) {
                return;
            }
            final ProgressListener progress = this.progress;
            ++progress.count;
        }
    }
    
    private static void plot(final BufferedImage dst, final int x, final int y, final int argb) {
        if (x >= 0 && x < dst.getWidth() && y >= 0 && y < dst.getHeight()) {
            dst.setRGB(x, y, argb);
        }
    }
    
    private static void copy(final BufferedImage src, final int x, final int y, final int w, final int h, final BufferedImage dst, final int dx, final int dy, final boolean rotated) {
        if (rotated) {
            for (int i = 0; i < w; ++i) {
                for (int j = 0; j < h; ++j) {
                    plot(dst, dx + j, dy + w - i - 1, src.getRGB(x + i, y + j));
                }
            }
            return;
        }
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                plot(dst, dx + i, dy + j, src.getRGB(x + i, y + j));
            }
        }
    }
    
    private void writePackFile(File outputDir, final String scaledPackFileName, final Array<Page> pages) throws IOException {
        (outputDir = new File(outputDir, scaledPackFileName + this.settings.atlasExtension)).getParentFile().mkdirs();
        if (outputDir.exists()) {
            final TextureAtlas.TextureAtlasData textureAtlasData = new TextureAtlas.TextureAtlasData(new FileHandle(outputDir), new FileHandle(outputDir), false);
            final Iterator<Page> iterator = pages.iterator();
            while (iterator.hasNext()) {
                final Iterator<Rect> iterator2 = iterator.next().outputRects.iterator();
                while (iterator2.hasNext()) {
                    final Rect rect;
                    final String rectName = Rect.getAtlasName((rect = iterator2.next()).name, false);
                    final Iterator<TextureAtlas.TextureAtlasData.Region> iterator3 = textureAtlasData.getRegions().iterator();
                    while (iterator3.hasNext()) {
                        if (iterator3.next().name.equals(rectName)) {
                            throw new GdxRuntimeException("A region with the name \"" + rectName + "\" has already been packed: " + rect.name);
                        }
                    }
                }
            }
        }
        final Writer writer = new OutputStreamWriter(new FileOutputStream(outputDir, true), "UTF-8");
        for (final Page page : pages) {
            writer.write("\n" + page.imageName + "\n");
            writer.write("size: " + page.imageWidth + "," + page.imageHeight + "\n");
            writer.write("format: " + this.settings.format + "\n");
            writer.write("filter: " + this.settings.filterMin + "," + this.settings.filterMag + "\n");
            writer.write("repeat: " + ((this.settings.wrapX == Texture.TextureWrap.Repeat && this.settings.wrapY == Texture.TextureWrap.Repeat) ? "xy" : ((this.settings.wrapX == Texture.TextureWrap.Repeat && this.settings.wrapY == Texture.TextureWrap.ClampToEdge) ? "x" : ((this.settings.wrapX == Texture.TextureWrap.ClampToEdge && this.settings.wrapY == Texture.TextureWrap.Repeat) ? "y" : "none"))) + "\n");
            page.outputRects.sort();
            for (final Rect rect : page.outputRects) {
                final Writer writer2 = writer;
                final Page page2 = page;
                final Rect rect2 = rect;
                this.writeRect(writer2, page2, rect2, rect2.name);
                final Array<Alias> aliases;
                (aliases = new Array<Alias>((Alias[])rect.aliases.toArray())).sort();
                for (final Alias alias : aliases) {
                    final Rect aliasRect;
                    (aliasRect = new Rect()).set(rect);
                    final Alias alias2 = alias;
                    final Rect rect3 = aliasRect;
                    final Alias alias3 = alias2;
                    rect3.name = alias3.name;
                    rect3.index = alias3.index;
                    rect3.splits = alias3.splits;
                    rect3.pads = alias3.pads;
                    rect3.offsetX = alias3.offsetX;
                    rect3.offsetY = alias3.offsetY;
                    rect3.originalWidth = alias3.originalWidth;
                    rect3.originalHeight = alias3.originalHeight;
                    this.writeRect(writer, page, aliasRect, alias.name);
                }
            }
        }
        writer.close();
    }
    
    private void writeRect(final Writer writer, final Page page, final Rect rect, final String name) throws IOException {
        writer.write(Rect.getAtlasName(name, false) + "\n");
        writer.write("  rotate: " + rect.rotated + "\n");
        writer.write("  xy: " + (page.x + rect.x) + ", " + (page.y + page.height - rect.y - (rect.height - this.settings.paddingY)) + "\n");
        writer.write("  size: " + rect.regionWidth + ", " + rect.regionHeight + "\n");
        if (rect.splits != null) {
            writer.write("  split: " + rect.splits[0] + ", " + rect.splits[1] + ", " + rect.splits[2] + ", " + rect.splits[3] + "\n");
        }
        if (rect.pads != null) {
            if (rect.splits == null) {
                writer.write("  split: 0, 0, 0, 0\n");
            }
            writer.write("  pad: " + rect.pads[0] + ", " + rect.pads[1] + ", " + rect.pads[2] + ", " + rect.pads[3] + "\n");
        }
        writer.write("  orig: " + rect.originalWidth + ", " + rect.originalHeight + "\n");
        writer.write("  offset: " + rect.offsetX + ", " + (rect.originalHeight - rect.regionHeight - rect.offsetY) + "\n");
        writer.write("  index: " + rect.index + "\n");
    }
    
    public final void setProgressListener(final ProgressListener progressListener) {
        this.progress = progressListener;
    }
    
    public static final class Page
    {
        public String imageName;
        public Array<Rect> outputRects;
        public Array<Rect> remainingRects;
        public float occupancy;
        public int x;
        public int y;
        public int width;
        public int height;
        public int imageWidth;
        public int imageHeight;
    }
    
    public static final class Alias implements Comparable<Alias>
    {
        public String name;
        public int index;
        public int[] splits;
        public int[] pads;
        public int offsetX;
        public int offsetY;
        public int originalWidth;
        public int originalHeight;
        
        public Alias(final Rect rect) {
            this.name = rect.name;
            this.index = rect.index;
            this.splits = rect.splits;
            this.pads = rect.pads;
            this.offsetX = rect.offsetX;
            this.offsetY = rect.offsetY;
            this.originalWidth = rect.originalWidth;
            this.originalHeight = rect.originalHeight;
        }
    }
    
    public static final class Rect implements Comparable<Rect>
    {
        public String name;
        public int offsetX;
        public int offsetY;
        public int regionWidth;
        public int regionHeight;
        public int originalWidth;
        public int originalHeight;
        public int x;
        public int y;
        public int width;
        public int height;
        public int index;
        public boolean rotated;
        public Set<Alias> aliases;
        public int[] splits;
        public int[] pads;
        public boolean canRotate;
        private boolean isPatch;
        private BufferedImage image;
        private File file;
        int score1;
        int score2;
        
        public Rect(final BufferedImage source, final int left, final int top, final int newWidth, final int newHeight, final boolean isPatch) {
            this.aliases = new HashSet<Alias>();
            this.canRotate = true;
            this.image = new BufferedImage(source.getColorModel(), source.getRaster().createWritableChild(left, top, newWidth, newHeight, 0, 0, null), source.getColorModel().isAlphaPremultiplied(), null);
            this.offsetX = left;
            this.offsetY = top;
            this.regionWidth = newWidth;
            this.regionHeight = newHeight;
            this.originalWidth = source.getWidth();
            this.originalHeight = source.getHeight();
            this.width = newWidth;
            this.height = newHeight;
            this.isPatch = isPatch;
        }
        
        public final BufferedImage getImage(ImageProcessor imageProcessor) {
            while (this.image == null) {
                BufferedImage image;
                try {
                    image = ImageIO.read(this.file);
                }
                catch (IOException ex) {
                    throw new RuntimeException("Error reading image: " + this.file, ex);
                }
                if (image == null) {
                    throw new RuntimeException("Unable to read image: " + this.file);
                }
                String name = this.name;
                if (this.isPatch) {
                    name += ".9";
                }
                final Rect processImage = imageProcessor.processImage(image, name);
                imageProcessor = null;
                this = processImage;
            }
            return this.image;
        }
        
        Rect() {
            this.aliases = new HashSet<Alias>();
            this.canRotate = true;
        }
        
        Rect(final Rect rect) {
            this.aliases = new HashSet<Alias>();
            this.canRotate = true;
            this.x = rect.x;
            this.y = rect.y;
            this.width = rect.width;
            this.height = rect.height;
        }
        
        final void set(final Rect rect) {
            this.name = rect.name;
            this.image = rect.image;
            this.offsetX = rect.offsetX;
            this.offsetY = rect.offsetY;
            this.regionWidth = rect.regionWidth;
            this.regionHeight = rect.regionHeight;
            this.originalWidth = rect.originalWidth;
            this.originalHeight = rect.originalHeight;
            this.x = rect.x;
            this.y = rect.y;
            this.width = rect.width;
            this.height = rect.height;
            this.index = rect.index;
            this.rotated = rect.rotated;
            this.aliases = rect.aliases;
            this.splits = rect.splits;
            this.pads = rect.pads;
            this.canRotate = rect.canRotate;
            this.score1 = rect.score1;
            this.score2 = rect.score2;
            this.file = rect.file;
            this.isPatch = rect.isPatch;
        }
        
        @Override
        public final boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final Rect other = (Rect)obj;
            if (this.name == null) {
                if (other.name != null) {
                    return false;
                }
            }
            else if (!this.name.equals(other.name)) {
                return false;
            }
            return true;
        }
        
        @Override
        public final String toString() {
            return this.name + ((this.index != -1) ? ("_" + this.index) : "") + "[" + this.x + "," + this.y + " " + this.width + "x" + this.height + "]";
        }
        
        public static String getAtlasName(final String name, final boolean flattenPaths) {
            if (flattenPaths) {
                return new FileHandle(name).name();
            }
            return name;
        }
    }
    
    public enum Resampling
    {
        nearest(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR), 
        bilinear(RenderingHints.VALUE_INTERPOLATION_BILINEAR), 
        bicubic(RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        final Object value;
        
        private Resampling(final Object value) {
            this.value = value;
        }
    }
    
    static final class InputImage
    {
        String name;
        BufferedImage image;
    }
    
    public abstract static class ProgressListener
    {
        private float scale;
        private float lastUpdate;
        private final FloatArray portions;
        private volatile boolean cancel;
        int count;
        
        public ProgressListener() {
            this.scale = 1.0f;
            this.portions = new FloatArray(8);
        }
        
        public final void start(final float portion) {
            if (portion == 0.0f) {
                throw new IllegalArgumentException("portion cannot be 0.");
            }
            this.portions.add(this.lastUpdate);
            this.portions.add(this.scale * portion);
            this.portions.add(this.scale);
            this.scale *= portion;
        }
        
        public final boolean update(final int count, final int total) {
            this.update((total == 0) ? 0.0f : (count / (float)total));
            return this.cancel;
        }
        
        private void update(final float percent) {
            this.progress(this.lastUpdate = this.portions.get(this.portions.size - 3) + this.portions.get(this.portions.size - 2) * percent);
        }
        
        public final void end() {
            this.scale = this.portions.pop();
            final float portion = this.portions.pop();
            this.progress(this.lastUpdate = this.portions.pop() + portion);
        }
        
        public abstract void progress(final float p0);
    }
    
    public static final class Settings
    {
        public boolean pot;
        public int paddingX;
        public int paddingY;
        public boolean edgePadding;
        public boolean duplicatePadding;
        public int minWidth;
        public int minHeight;
        public int maxWidth;
        public int maxHeight;
        public boolean square;
        public Texture.TextureFilter filterMin;
        public Texture.TextureFilter filterMag;
        public Texture.TextureWrap wrapX;
        public Texture.TextureWrap wrapY;
        public Pixmap.Format format;
        public boolean alias;
        public String outputFormat;
        public float jpegQuality;
        public boolean ignoreBlankImages;
        public boolean silent;
        public boolean useIndexes;
        public boolean bleed;
        public int bleedIterations;
        public boolean limitMemory;
        public float[] scale;
        public String[] scaleSuffix;
        public Resampling[] scaleResampling;
        public String atlasExtension;
        
        public Settings() {
            this.pot = true;
            this.paddingX = 2;
            this.paddingY = 2;
            this.edgePadding = true;
            this.duplicatePadding = false;
            this.minWidth = 16;
            this.minHeight = 16;
            this.maxWidth = 1024;
            this.maxHeight = 1024;
            this.square = false;
            this.filterMin = Texture.TextureFilter.Nearest;
            this.filterMag = Texture.TextureFilter.Nearest;
            this.wrapX = Texture.TextureWrap.ClampToEdge;
            this.wrapY = Texture.TextureWrap.ClampToEdge;
            this.format = Pixmap.Format.RGBA8888;
            this.alias = true;
            this.outputFormat = "png";
            this.jpegQuality = 0.9f;
            this.ignoreBlankImages = true;
            this.useIndexes = true;
            this.bleed = true;
            this.bleedIterations = 2;
            this.limitMemory = true;
            this.scale = new float[] { 1.0f };
            this.scaleSuffix = new String[] { "" };
            this.scaleResampling = new Resampling[] { Resampling.bicubic };
            this.atlasExtension = ".atlas";
        }
    }
    
    public interface Packer
    {
        Array<Page> pack(final ProgressListener p0, final Array<Rect> p1);
    }
}
