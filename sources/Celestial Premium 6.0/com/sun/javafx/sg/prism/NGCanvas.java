/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.VPos
 *  javafx.scene.text.Font
 *  javafx.scene.text.FontSmoothingType
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.scene.text.FontHelper;
import com.sun.javafx.sg.prism.GrowableDataBuffer;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.sg.prism.NGText;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.ScreenConfigurationAccessor;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrTexture;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;

public class NGCanvas
extends NGNode {
    public static final byte ATTR_BASE = 0;
    public static final byte GLOBAL_ALPHA = 0;
    public static final byte COMP_MODE = 1;
    public static final byte FILL_PAINT = 2;
    public static final byte STROKE_PAINT = 3;
    public static final byte LINE_WIDTH = 4;
    public static final byte LINE_CAP = 5;
    public static final byte LINE_JOIN = 6;
    public static final byte MITER_LIMIT = 7;
    public static final byte FONT = 8;
    public static final byte TEXT_ALIGN = 9;
    public static final byte TEXT_BASELINE = 10;
    public static final byte TRANSFORM = 11;
    public static final byte EFFECT = 12;
    public static final byte PUSH_CLIP = 13;
    public static final byte POP_CLIP = 14;
    public static final byte ARC_TYPE = 15;
    public static final byte FILL_RULE = 16;
    public static final byte DASH_ARRAY = 17;
    public static final byte DASH_OFFSET = 18;
    public static final byte FONT_SMOOTH = 19;
    public static final byte IMAGE_SMOOTH = 20;
    public static final byte OP_BASE = 25;
    public static final byte FILL_RECT = 25;
    public static final byte STROKE_RECT = 26;
    public static final byte CLEAR_RECT = 27;
    public static final byte STROKE_LINE = 28;
    public static final byte FILL_OVAL = 29;
    public static final byte STROKE_OVAL = 30;
    public static final byte FILL_ROUND_RECT = 31;
    public static final byte STROKE_ROUND_RECT = 32;
    public static final byte FILL_ARC = 33;
    public static final byte STROKE_ARC = 34;
    public static final byte FILL_TEXT = 35;
    public static final byte STROKE_TEXT = 36;
    public static final byte PATH_BASE = 40;
    public static final byte PATHSTART = 40;
    public static final byte MOVETO = 41;
    public static final byte LINETO = 42;
    public static final byte QUADTO = 43;
    public static final byte CUBICTO = 44;
    public static final byte CLOSEPATH = 45;
    public static final byte PATHEND = 46;
    public static final byte FILL_PATH = 47;
    public static final byte STROKE_PATH = 48;
    public static final byte IMG_BASE = 50;
    public static final byte DRAW_IMAGE = 50;
    public static final byte DRAW_SUBIMAGE = 51;
    public static final byte PUT_ARGB = 52;
    public static final byte PUT_ARGBPRE_BUF = 53;
    public static final byte FX_BASE = 60;
    public static final byte FX_APPLY_EFFECT = 60;
    public static final byte UTIL_BASE = 70;
    public static final byte RESET = 70;
    public static final byte SET_DIMS = 71;
    public static final byte CAP_BUTT = 0;
    public static final byte CAP_ROUND = 1;
    public static final byte CAP_SQUARE = 2;
    public static final byte JOIN_MITER = 0;
    public static final byte JOIN_ROUND = 1;
    public static final byte JOIN_BEVEL = 2;
    public static final byte ARC_OPEN = 0;
    public static final byte ARC_CHORD = 1;
    public static final byte ARC_PIE = 2;
    public static final byte SMOOTH_GRAY = (byte)FontSmoothingType.GRAY.ordinal();
    public static final byte SMOOTH_LCD = (byte)FontSmoothingType.LCD.ordinal();
    public static final byte ALIGN_LEFT = 0;
    public static final byte ALIGN_CENTER = 1;
    public static final byte ALIGN_RIGHT = 2;
    public static final byte ALIGN_JUSTIFY = 3;
    public static final byte BASE_TOP = 0;
    public static final byte BASE_MIDDLE = 1;
    public static final byte BASE_ALPHABETIC = 2;
    public static final byte BASE_BOTTOM = 3;
    public static final byte FILL_RULE_NON_ZERO = 0;
    public static final byte FILL_RULE_EVEN_ODD = 1;
    private static Blend BLENDER = new MyBlend(Blend.Mode.SRC_OVER, null, null);
    private GrowableDataBuffer thebuf;
    private final float highestPixelScale;
    private int tw;
    private int th;
    private int cw;
    private int ch;
    private RenderBuf cv;
    private RenderBuf temp;
    private RenderBuf clip;
    private float globalAlpha;
    private Blend.Mode blendmode;
    private Paint fillPaint;
    private Paint strokePaint;
    private float linewidth;
    private int linecap;
    private int linejoin;
    private float miterlimit;
    private double[] dashes;
    private float dashOffset;
    private BasicStroke stroke;
    private Path2D path;
    private NGText ngtext;
    private PrismTextLayout textLayout;
    private PGFont pgfont;
    private int smoothing;
    private boolean imageSmoothing;
    private int align;
    private int baseline;
    private Affine2D transform;
    private Affine2D inverseTransform;
    private boolean inversedirty;
    private LinkedList<Path2D> clipStack;
    private int clipsRendered;
    private boolean clipIsRect;
    private Rectangle clipRect;
    private Effect effect;
    private int arctype;
    static float[] TEMP_COORDS = new float[6];
    private static Arc2D TEMP_ARC = new Arc2D();
    private static RectBounds TEMP_RECTBOUNDS = new RectBounds();
    static final Affine2D TEMP_PATH_TX = new Affine2D();
    static final int[] numCoords = new int[]{2, 2, 4, 6, 0};
    Shape untransformedPath = new Shape(){

        @Override
        public RectBounds getBounds() {
            if (NGCanvas.this.transform.isTranslateOrIdentity()) {
                RectBounds rectBounds = NGCanvas.this.path.getBounds();
                if (NGCanvas.this.transform.isIdentity()) {
                    return rectBounds;
                }
                float f = (float)NGCanvas.this.transform.getMxt();
                float f2 = (float)NGCanvas.this.transform.getMyt();
                return new RectBounds(rectBounds.getMinX() - f, rectBounds.getMinY() - f2, rectBounds.getMaxX() - f, rectBounds.getMaxY() - f2);
            }
            float f = Float.POSITIVE_INFINITY;
            float f3 = Float.POSITIVE_INFINITY;
            float f4 = Float.NEGATIVE_INFINITY;
            float f5 = Float.NEGATIVE_INFINITY;
            PathIterator pathIterator = NGCanvas.this.path.getPathIterator(NGCanvas.this.getInverseTransform());
            while (!pathIterator.isDone()) {
                int n = numCoords[pathIterator.currentSegment(TEMP_COORDS)];
                for (int i = 0; i < n; i += 2) {
                    if (f > TEMP_COORDS[i + 0]) {
                        f = TEMP_COORDS[i + 0];
                    }
                    if (f4 < TEMP_COORDS[i + 0]) {
                        f4 = TEMP_COORDS[i + 0];
                    }
                    if (f3 > TEMP_COORDS[i + 1]) {
                        f3 = TEMP_COORDS[i + 1];
                    }
                    if (!(f5 < TEMP_COORDS[i + 1])) continue;
                    f5 = TEMP_COORDS[i + 1];
                }
                pathIterator.next();
            }
            return new RectBounds(f, f3, f4, f5);
        }

        @Override
        public boolean contains(float f, float f2) {
            NGCanvas.TEMP_COORDS[0] = f;
            NGCanvas.TEMP_COORDS[1] = f2;
            NGCanvas.this.transform.transform(TEMP_COORDS, 0, TEMP_COORDS, 0, 1);
            f = TEMP_COORDS[0];
            f2 = TEMP_COORDS[1];
            return NGCanvas.this.path.contains(f, f2);
        }

        @Override
        public boolean intersects(float f, float f2, float f3, float f4) {
            if (NGCanvas.this.transform.isTranslateOrIdentity()) {
                f = (float)((double)f + NGCanvas.this.transform.getMxt());
                f2 = (float)((double)f2 + NGCanvas.this.transform.getMyt());
                return NGCanvas.this.path.intersects(f, f2, f3, f4);
            }
            PathIterator pathIterator = NGCanvas.this.path.getPathIterator(NGCanvas.this.getInverseTransform());
            int n = Shape.rectCrossingsForPath(pathIterator, f, f2, f + f3, f2 + f4);
            return n != 0;
        }

        @Override
        public boolean contains(float f, float f2, float f3, float f4) {
            if (NGCanvas.this.transform.isTranslateOrIdentity()) {
                f = (float)((double)f + NGCanvas.this.transform.getMxt());
                f2 = (float)((double)f2 + NGCanvas.this.transform.getMyt());
                return NGCanvas.this.path.contains(f, f2, f3, f4);
            }
            PathIterator pathIterator = NGCanvas.this.path.getPathIterator(NGCanvas.this.getInverseTransform());
            int n = Shape.rectCrossingsForPath(pathIterator, f, f2, f + f3, f2 + f4);
            return n != Integer.MIN_VALUE && n != 0;
        }

        public BaseTransform getCombinedTransform(BaseTransform baseTransform) {
            if (NGCanvas.this.transform.isIdentity()) {
                return baseTransform;
            }
            if (NGCanvas.this.transform.equals(baseTransform)) {
                return null;
            }
            Affine2D affine2D = NGCanvas.this.getInverseTransform();
            if (baseTransform == null || baseTransform.isIdentity()) {
                return affine2D;
            }
            TEMP_PATH_TX.setTransform(baseTransform);
            TEMP_PATH_TX.concatenate(affine2D);
            return TEMP_PATH_TX;
        }

        @Override
        public PathIterator getPathIterator(BaseTransform baseTransform) {
            return NGCanvas.this.path.getPathIterator(this.getCombinedTransform(baseTransform));
        }

        @Override
        public PathIterator getPathIterator(BaseTransform baseTransform, float f) {
            return NGCanvas.this.path.getPathIterator(this.getCombinedTransform(baseTransform), f);
        }

        @Override
        public Shape copy() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    private static final float CLIPRECT_TOLERANCE = 0.00390625f;
    private static final Rectangle TEMP_RECT = new Rectangle();
    private static final int[] prcaps = new int[]{0, 1, 2};
    private static final int[] prjoins = new int[]{0, 1, 2};
    private static final int[] prbases = new int[]{VPos.TOP.ordinal(), VPos.CENTER.ordinal(), VPos.BASELINE.ordinal(), VPos.BOTTOM.ordinal()};
    private static final Affine2D TEMP_TX = new Affine2D();

    public NGCanvas() {
        Toolkit toolkit = Toolkit.getToolkit();
        ScreenConfigurationAccessor screenConfigurationAccessor = toolkit.getScreenConfigurationAccessor();
        float f = 1.0f;
        for (Object obj : toolkit.getScreens()) {
            f = Math.max(screenConfigurationAccessor.getRecommendedOutputScaleX(obj), f);
            f = Math.max(screenConfigurationAccessor.getRecommendedOutputScaleY(obj), f);
        }
        this.highestPixelScale = (float)Math.ceil(f);
        this.cv = new RenderBuf(InitType.PRESERVE_UPPER_LEFT);
        this.temp = new RenderBuf(InitType.CLEAR);
        this.clip = new RenderBuf(InitType.FILL_WHITE);
        this.path = new Path2D();
        this.ngtext = new NGText();
        this.textLayout = new PrismTextLayout();
        this.transform = new Affine2D();
        this.clipStack = new LinkedList();
        this.initAttributes();
    }

    private void initAttributes() {
        this.globalAlpha = 1.0f;
        this.blendmode = Blend.Mode.SRC_OVER;
        this.fillPaint = Color.BLACK;
        this.strokePaint = Color.BLACK;
        this.linewidth = 1.0f;
        this.linecap = 2;
        this.linejoin = 0;
        this.miterlimit = 10.0f;
        this.dashes = null;
        this.dashOffset = 0.0f;
        this.stroke = null;
        this.path.setWindingRule(1);
        this.pgfont = (PGFont)FontHelper.getNativeFont(Font.getDefault());
        this.smoothing = SMOOTH_GRAY;
        this.imageSmoothing = true;
        this.align = 0;
        this.baseline = VPos.BASELINE.ordinal();
        this.transform.setToScale(this.highestPixelScale, this.highestPixelScale);
        this.clipStack.clear();
        this.resetClip(false);
    }

    private Affine2D getInverseTransform() {
        if (this.inverseTransform == null) {
            this.inverseTransform = new Affine2D();
            this.inversedirty = true;
        }
        if (this.inversedirty) {
            this.inverseTransform.setTransform(this.transform);
            try {
                this.inverseTransform.invert();
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                this.inverseTransform.setToScale(0.0, 0.0);
            }
            this.inversedirty = false;
        }
        return this.inverseTransform;
    }

    @Override
    protected boolean hasOverlappingContents() {
        return true;
    }

    private static void shapebounds(Shape shape, RectBounds rectBounds, BaseTransform baseTransform) {
        NGCanvas.TEMP_COORDS[1] = Float.POSITIVE_INFINITY;
        NGCanvas.TEMP_COORDS[0] = Float.POSITIVE_INFINITY;
        NGCanvas.TEMP_COORDS[3] = Float.NEGATIVE_INFINITY;
        NGCanvas.TEMP_COORDS[2] = Float.NEGATIVE_INFINITY;
        Shape.accumulate(TEMP_COORDS, shape, baseTransform);
        rectBounds.setBounds(TEMP_COORDS[0], TEMP_COORDS[1], TEMP_COORDS[2], TEMP_COORDS[3]);
    }

    private static void strokebounds(BasicStroke basicStroke, Shape shape, RectBounds rectBounds, BaseTransform baseTransform) {
        NGCanvas.TEMP_COORDS[1] = Float.POSITIVE_INFINITY;
        NGCanvas.TEMP_COORDS[0] = Float.POSITIVE_INFINITY;
        NGCanvas.TEMP_COORDS[3] = Float.NEGATIVE_INFINITY;
        NGCanvas.TEMP_COORDS[2] = Float.NEGATIVE_INFINITY;
        basicStroke.accumulateShapeBounds(TEMP_COORDS, shape, baseTransform);
        rectBounds.setBounds(TEMP_COORDS[0], TEMP_COORDS[1], TEMP_COORDS[2], TEMP_COORDS[3]);
    }

    private static void runOnRenderThread(Runnable runnable) {
        if (Thread.currentThread().getName().startsWith("QuantumRenderer")) {
            runnable.run();
        } else {
            FutureTask<Object> futureTask = new FutureTask<Object>(runnable, null);
            Toolkit.getToolkit().addRenderJob(new RenderJob(futureTask));
            try {
                futureTask.get();
            }
            catch (ExecutionException executionException) {
                throw new AssertionError((Object)executionException);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }

    private boolean printedCanvas(Graphics graphics) {
        RTTexture rTTexture = this.cv.tex;
        if (!(graphics instanceof PrinterGraphics) || rTTexture == null) {
            return false;
        }
        ResourceFactory resourceFactory = graphics.getResourceFactory();
        boolean bl = resourceFactory.isCompatibleTexture(rTTexture);
        if (bl) {
            return false;
        }
        int n = rTTexture.getContentWidth();
        int n2 = rTTexture.getContentHeight();
        RTTexture rTTexture2 = resourceFactory.createRTTexture(n, n2, Texture.WrapMode.CLAMP_TO_ZERO);
        Graphics graphics2 = rTTexture2.createGraphics();
        graphics2.setCompositeMode(CompositeMode.SRC);
        if (this.cv.savedPixelData == null) {
            PixelData pixelData = new PixelData(this.cw, this.ch);
            NGCanvas.runOnRenderThread(() -> {
                pixelData.save(rTTexture);
                pixelData.restore(graphics2, n, n2);
            });
        } else {
            this.cv.savedPixelData.restore(graphics2, n, n2);
        }
        graphics.drawTexture(rTTexture2, 0.0f, 0.0f, n, n2);
        rTTexture2.unlock();
        rTTexture2.dispose();
        return true;
    }

    @Override
    protected void renderContent(Graphics graphics) {
        if (this.printedCanvas(graphics)) {
            return;
        }
        this.initCanvas(graphics);
        if (this.cv.tex != null) {
            if (this.thebuf != null) {
                this.renderStream(this.thebuf);
                GrowableDataBuffer.returnBuffer(this.thebuf);
                this.thebuf = null;
            }
            float f = (float)this.tw / this.highestPixelScale;
            float f2 = (float)this.th / this.highestPixelScale;
            graphics.drawTexture(this.cv.tex, 0.0f, 0.0f, f, f2, 0.0f, 0.0f, this.tw, this.th);
            this.cv.save(this.tw, this.th);
        }
        this.cv.g = null;
        this.clip.g = null;
        this.temp.g = null;
    }

    @Override
    public void renderForcedContent(Graphics graphics) {
        if (this.thebuf != null) {
            this.initCanvas(graphics);
            if (this.cv.tex != null) {
                this.renderStream(this.thebuf);
                GrowableDataBuffer.returnBuffer(this.thebuf);
                this.thebuf = null;
                this.cv.save(this.tw, this.th);
            }
            this.cv.g = null;
            this.clip.g = null;
            this.temp.g = null;
        }
    }

    private void initCanvas(Graphics graphics) {
        if (this.tw <= 0 || this.th <= 0) {
            this.cv.dispose();
            return;
        }
        if (this.cv.validate(graphics, this.tw, this.th)) {
            this.cv.tex.contentsUseful();
            this.cv.tex.makePermanent();
            this.cv.tex.lock();
        }
    }

    private void clearCanvas(int n, int n2, int n3, int n4) {
        this.cv.g.setCompositeMode(CompositeMode.CLEAR);
        this.cv.g.setTransform(BaseTransform.IDENTITY_TRANSFORM);
        this.cv.g.fillQuad(n, n2, n + n3, n2 + n4);
        this.cv.g.setCompositeMode(CompositeMode.SRC_OVER);
    }

    private void resetClip(boolean bl) {
        if (bl) {
            this.clip.dispose();
        }
        this.clipsRendered = 0;
        this.clipIsRect = true;
        this.clipRect = null;
    }

    private boolean initClip() {
        boolean bl;
        if (this.clipIsRect) {
            bl = false;
        } else {
            bl = true;
            if (this.clip.validate(this.cv.g, this.tw, this.th)) {
                this.clip.tex.contentsUseful();
                this.resetClip(false);
            }
        }
        int n = this.clipStack.size();
        while (this.clipsRendered < n) {
            Path2D path2D = this.clipStack.get(this.clipsRendered++);
            if (this.clipIsRect) {
                if (path2D.checkAndGetIntRect(TEMP_RECT, 0.00390625f)) {
                    if (this.clipRect == null) {
                        this.clipRect = new Rectangle(TEMP_RECT);
                        continue;
                    }
                    this.clipRect.intersectWith(TEMP_RECT);
                    continue;
                }
                this.clipIsRect = false;
                if (!bl) {
                    bl = true;
                    if (this.clip.validate(this.cv.g, this.tw, this.th)) {
                        this.clip.tex.contentsUseful();
                    }
                }
                if (this.clipRect != null) {
                    this.renderClip(new RoundRectangle2D(this.clipRect.x, this.clipRect.y, this.clipRect.width, this.clipRect.height, 0.0f, 0.0f));
                }
            }
            NGCanvas.shapebounds(path2D, TEMP_RECTBOUNDS, BaseTransform.IDENTITY_TRANSFORM);
            TEMP_RECT.setBounds(TEMP_RECTBOUNDS);
            if (this.clipRect == null) {
                this.clipRect = new Rectangle(TEMP_RECT);
            } else {
                this.clipRect.intersectWith(TEMP_RECT);
            }
            this.renderClip(path2D);
        }
        if (bl && this.clipIsRect) {
            this.clip.tex.unlock();
        }
        return !this.clipIsRect;
    }

    private void renderClip(Shape shape) {
        this.temp.validate(this.cv.g, this.tw, this.th);
        this.temp.g.setPaint(Color.WHITE);
        this.temp.g.setTransform(BaseTransform.IDENTITY_TRANSFORM);
        this.temp.g.fill(shape);
        this.blendAthruBintoC(this.temp, Blend.Mode.SRC_IN, this.clip, null, CompositeMode.SRC, this.clip);
        this.temp.tex.unlock();
    }

    private Rectangle applyEffectOnAintoC(Effect effect, Effect effect2, BaseTransform baseTransform, Rectangle rectangle, CompositeMode compositeMode, RenderBuf renderBuf) {
        PrFilterContext prFilterContext = PrFilterContext.getInstance(renderBuf.tex.getAssociatedScreen());
        ImageData imageData = effect2.filter(prFilterContext, baseTransform, rectangle, null, effect);
        Rectangle rectangle2 = imageData.getUntransformedBounds();
        Filterable filterable = imageData.getUntransformedImage();
        Object t = ((PrTexture)((Object)filterable)).getTextureObject();
        renderBuf.g.setTransform(imageData.getTransform());
        renderBuf.g.setCompositeMode(compositeMode);
        renderBuf.g.drawTexture((Texture)t, rectangle2.x, rectangle2.y, rectangle2.width, rectangle2.height);
        renderBuf.g.setTransform(BaseTransform.IDENTITY_TRANSFORM);
        renderBuf.g.setCompositeMode(CompositeMode.SRC_OVER);
        Rectangle rectangle3 = imageData.getTransformedBounds(rectangle);
        imageData.unref();
        return rectangle3;
    }

    private void blendAthruBintoC(RenderBuf renderBuf, Blend.Mode mode, RenderBuf renderBuf2, RectBounds rectBounds, CompositeMode compositeMode, RenderBuf renderBuf3) {
        BLENDER.setTopInput(renderBuf.input);
        BLENDER.setBottomInput(renderBuf2.input);
        BLENDER.setMode(mode);
        Rectangle rectangle = rectBounds != null ? new Rectangle(rectBounds) : null;
        this.applyEffectOnAintoC(null, BLENDER, BaseTransform.IDENTITY_TRANSFORM, rectangle, compositeMode, renderBuf3);
    }

    private void setupFill(Graphics graphics) {
        graphics.setPaint(this.fillPaint);
    }

    private BasicStroke getStroke() {
        if (this.stroke == null) {
            this.stroke = new BasicStroke(this.linewidth, this.linecap, this.linejoin, this.miterlimit, this.dashes, this.dashOffset);
        }
        return this.stroke;
    }

    private void setupStroke(Graphics graphics) {
        graphics.setStroke(this.getStroke());
        graphics.setPaint(this.strokePaint);
    }

    private void renderStream(GrowableDataBuffer growableDataBuffer) {
        block41: while (growableDataBuffer.hasValues()) {
            byte by = growableDataBuffer.getByte();
            switch (by) {
                case 70: {
                    this.initAttributes();
                    this.cw = this.tw;
                    this.ch = this.th;
                    this.clearCanvas(0, 0, this.tw, this.th);
                    continue block41;
                }
                case 71: {
                    int n = (int)Math.ceil(growableDataBuffer.getFloat() * this.highestPixelScale);
                    int n2 = (int)Math.ceil(growableDataBuffer.getFloat() * this.highestPixelScale);
                    int n3 = Math.min(n, this.cw);
                    int n4 = Math.min(n2, this.ch);
                    if (n3 < this.tw) {
                        this.clearCanvas(n3, 0, this.tw - n3, this.th);
                    }
                    if (n4 < this.th) {
                        this.clearCanvas(0, n4, this.tw, this.th - n4);
                    }
                    this.cw = n;
                    this.ch = n2;
                    continue block41;
                }
                case 40: {
                    this.path.reset();
                    continue block41;
                }
                case 41: {
                    this.path.moveTo(growableDataBuffer.getFloat(), growableDataBuffer.getFloat());
                    continue block41;
                }
                case 42: {
                    this.path.lineTo(growableDataBuffer.getFloat(), growableDataBuffer.getFloat());
                    continue block41;
                }
                case 43: {
                    this.path.quadTo(growableDataBuffer.getFloat(), growableDataBuffer.getFloat(), growableDataBuffer.getFloat(), growableDataBuffer.getFloat());
                    continue block41;
                }
                case 44: {
                    this.path.curveTo(growableDataBuffer.getFloat(), growableDataBuffer.getFloat(), growableDataBuffer.getFloat(), growableDataBuffer.getFloat(), growableDataBuffer.getFloat(), growableDataBuffer.getFloat());
                    continue block41;
                }
                case 45: {
                    this.path.closePath();
                    continue block41;
                }
                case 46: {
                    if (this.highestPixelScale == 1.0f) continue block41;
                    TEMP_TX.setToScale(this.highestPixelScale, this.highestPixelScale);
                    this.path.transform(TEMP_TX);
                    continue block41;
                }
                case 13: {
                    Object object3 = (Path2D)growableDataBuffer.getObject();
                    if (this.highestPixelScale != 1.0f) {
                        TEMP_TX.setToScale(this.highestPixelScale, this.highestPixelScale);
                        ((Path2D)object3).transform(TEMP_TX);
                    }
                    this.clipStack.addLast((Path2D)object3);
                    continue block41;
                }
                case 14: {
                    this.resetClip(true);
                    this.clipStack.removeLast();
                    continue block41;
                }
                case 15: {
                    byte by2 = growableDataBuffer.getByte();
                    switch (by2) {
                        case 0: {
                            this.arctype = 0;
                            break;
                        }
                        case 1: {
                            this.arctype = 1;
                            break;
                        }
                        case 2: {
                            this.arctype = 2;
                        }
                    }
                    continue block41;
                }
                case 52: {
                    float f = growableDataBuffer.getInt();
                    float f2 = growableDataBuffer.getInt();
                    int n = growableDataBuffer.getInt();
                    Object object2 = this.cv.g;
                    object2.setExtraAlpha(1.0f);
                    object2.setCompositeMode(CompositeMode.SRC);
                    object2.setTransform(BaseTransform.IDENTITY_TRANSFORM);
                    float f3 = (float)(n >>> 24) / 255.0f;
                    float f4 = (float)(n >> 16 & 0xFF) / 255.0f;
                    float f5 = (float)(n >> 8 & 0xFF) / 255.0f;
                    float f6 = (float)(n & 0xFF) / 255.0f;
                    object2.setPaint(new Color(f4, f5, f6, f3));
                    object2.fillQuad(f *= this.highestPixelScale, f2 *= this.highestPixelScale, f + this.highestPixelScale, f2 + this.highestPixelScale);
                    object2.setCompositeMode(CompositeMode.SRC_OVER);
                    continue block41;
                }
                case 53: {
                    float f = growableDataBuffer.getInt();
                    float f7 = growableDataBuffer.getInt();
                    int n = growableDataBuffer.getInt();
                    int n5 = growableDataBuffer.getInt();
                    byte[] arrby = (byte[])growableDataBuffer.getObject();
                    Image image = Image.fromByteBgraPreData(arrby, n, n5);
                    Graphics graphics = this.cv.g;
                    ResourceFactory resourceFactory = graphics.getResourceFactory();
                    Texture texture = resourceFactory.getCachedTexture(image, Texture.WrapMode.CLAMP_TO_EDGE);
                    graphics.setTransform(BaseTransform.IDENTITY_TRANSFORM);
                    graphics.setCompositeMode(CompositeMode.SRC);
                    float f8 = f + (float)n;
                    float f9 = f7 + (float)n5;
                    graphics.drawTexture(texture, f *= this.highestPixelScale, f7 *= this.highestPixelScale, f8 *= this.highestPixelScale, f9 *= this.highestPixelScale, 0.0f, 0.0f, n, n5);
                    texture.contentsNotUseful();
                    texture.unlock();
                    graphics.setCompositeMode(CompositeMode.SRC_OVER);
                    continue block41;
                }
                case 11: {
                    double d = growableDataBuffer.getDouble() * (double)this.highestPixelScale;
                    double d2 = growableDataBuffer.getDouble() * (double)this.highestPixelScale;
                    double d3 = growableDataBuffer.getDouble() * (double)this.highestPixelScale;
                    double d4 = growableDataBuffer.getDouble() * (double)this.highestPixelScale;
                    double d5 = growableDataBuffer.getDouble() * (double)this.highestPixelScale;
                    double d6 = growableDataBuffer.getDouble() * (double)this.highestPixelScale;
                    this.transform.setTransform(d, d4, d2, d5, d3, d6);
                    this.inversedirty = true;
                    continue block41;
                }
                case 0: {
                    this.globalAlpha = growableDataBuffer.getFloat();
                    continue block41;
                }
                case 16: {
                    if (growableDataBuffer.getByte() == 0) {
                        this.path.setWindingRule(1);
                        continue block41;
                    }
                    this.path.setWindingRule(0);
                    continue block41;
                }
                case 1: {
                    this.blendmode = (Blend.Mode)((Object)growableDataBuffer.getObject());
                    continue block41;
                }
                case 2: {
                    this.fillPaint = (Paint)growableDataBuffer.getObject();
                    continue block41;
                }
                case 3: {
                    this.strokePaint = (Paint)growableDataBuffer.getObject();
                    continue block41;
                }
                case 4: {
                    this.linewidth = growableDataBuffer.getFloat();
                    this.stroke = null;
                    continue block41;
                }
                case 5: {
                    this.linecap = prcaps[growableDataBuffer.getUByte()];
                    this.stroke = null;
                    continue block41;
                }
                case 6: {
                    this.linejoin = prjoins[growableDataBuffer.getUByte()];
                    this.stroke = null;
                    continue block41;
                }
                case 7: {
                    this.miterlimit = growableDataBuffer.getFloat();
                    this.stroke = null;
                    continue block41;
                }
                case 17: {
                    this.dashes = (double[])growableDataBuffer.getObject();
                    this.stroke = null;
                    continue block41;
                }
                case 18: {
                    this.dashOffset = growableDataBuffer.getFloat();
                    this.stroke = null;
                    continue block41;
                }
                case 8: {
                    this.pgfont = (PGFont)growableDataBuffer.getObject();
                    continue block41;
                }
                case 19: {
                    this.smoothing = growableDataBuffer.getUByte();
                    continue block41;
                }
                case 20: {
                    this.imageSmoothing = growableDataBuffer.getBoolean();
                    continue block41;
                }
                case 9: {
                    this.align = growableDataBuffer.getUByte();
                    continue block41;
                }
                case 10: {
                    this.baseline = prbases[growableDataBuffer.getUByte()];
                    continue block41;
                }
                case 60: {
                    BaseTransform baseTransform;
                    RenderBuf renderBuf;
                    Object object3 = (Effect)growableDataBuffer.getObject();
                    RenderBuf renderBuf2 = renderBuf = this.clipStack.isEmpty() ? this.cv : this.temp;
                    if (this.highestPixelScale != 1.0f) {
                        TEMP_TX.setToScale(this.highestPixelScale, this.highestPixelScale);
                        baseTransform = TEMP_TX;
                        this.cv.input.setPixelScale(this.highestPixelScale);
                    } else {
                        baseTransform = BaseTransform.IDENTITY_TRANSFORM;
                    }
                    this.applyEffectOnAintoC(this.cv.input, (Effect)object3, baseTransform, null, CompositeMode.SRC, renderBuf);
                    this.cv.input.setPixelScale(1.0f);
                    if (renderBuf == this.cv) continue block41;
                    this.blendAthruBintoC(renderBuf, Blend.Mode.SRC_IN, this.clip, null, CompositeMode.SRC, this.cv);
                    continue block41;
                }
                case 12: {
                    this.effect = (Effect)growableDataBuffer.getObject();
                    continue block41;
                }
                case 25: 
                case 26: 
                case 27: 
                case 28: 
                case 29: 
                case 30: 
                case 31: 
                case 32: 
                case 33: 
                case 34: 
                case 35: 
                case 36: 
                case 47: 
                case 48: 
                case 50: 
                case 51: {
                    Object object;
                    boolean bl;
                    Object object2;
                    Object object3;
                    boolean bl2 = this.initClip();
                    if (bl2) {
                        this.temp.validate(this.cv.g, this.tw, this.th);
                        bl = true;
                        object3 = this.temp;
                    } else if (this.blendmode != Blend.Mode.SRC_OVER) {
                        this.temp.validate(this.cv.g, this.tw, this.th);
                        bl = true;
                        object3 = this.temp;
                    } else {
                        bl = false;
                        object3 = this.cv;
                    }
                    if (this.effect != null) {
                        growableDataBuffer.save();
                        this.handleRenderOp(by, growableDataBuffer, null, TEMP_RECTBOUNDS);
                        object2 = new RenderInput(by, growableDataBuffer, this.transform, TEMP_RECTBOUNDS);
                        object = this.applyEffectOnAintoC((Effect)object2, this.effect, this.transform, this.clipRect, CompositeMode.SRC_OVER, (RenderBuf)object3);
                        if (object3 != this.cv) {
                            TEMP_RECTBOUNDS.setBounds(((Rectangle)object).x, ((Rectangle)object).y, ((Rectangle)object).x + ((Rectangle)object).width, ((Rectangle)object).y + ((Rectangle)object).height);
                        }
                    } else {
                        object2 = ((RenderBuf)object3).g;
                        object2.setExtraAlpha(this.globalAlpha);
                        object2.setTransform(this.transform);
                        object2.setClipRect(this.clipRect);
                        object = object3 != this.cv ? TEMP_RECTBOUNDS : null;
                        this.handleRenderOp(by, growableDataBuffer, (Graphics)object2, (RectBounds)object);
                        object2.setClipRect(null);
                    }
                    if (bl2) {
                        if (this.blendmode == Blend.Mode.SRC_OVER) {
                            object3 = this.cv;
                            object2 = CompositeMode.SRC_OVER;
                        } else {
                            object2 = CompositeMode.SRC;
                        }
                        if (this.clipRect != null) {
                            TEMP_RECTBOUNDS.intersectWith(this.clipRect);
                        }
                        if (!TEMP_RECTBOUNDS.isEmpty()) {
                            if (object3 == this.cv && this.cv.g instanceof MaskTextureGraphics) {
                                object = (MaskTextureGraphics)this.cv.g;
                                int n = (int)Math.floor(TEMP_RECTBOUNDS.getMinX());
                                int n6 = (int)Math.floor(TEMP_RECTBOUNDS.getMinY());
                                int n7 = (int)Math.ceil(TEMP_RECTBOUNDS.getMaxX()) - n;
                                int n8 = (int)Math.ceil(TEMP_RECTBOUNDS.getMaxY()) - n6;
                                object.drawPixelsMasked(this.temp.tex, this.clip.tex, n, n6, n7, n8, n, n6, n, n6);
                            } else {
                                this.blendAthruBintoC(this.temp, Blend.Mode.SRC_IN, this.clip, TEMP_RECTBOUNDS, (CompositeMode)((Object)object2), (RenderBuf)object3);
                            }
                        }
                    }
                    if (this.blendmode != Blend.Mode.SRC_OVER) {
                        if (this.clipRect != null) {
                            TEMP_RECTBOUNDS.intersectWith(this.clipRect);
                        }
                        this.blendAthruBintoC(this.temp, this.blendmode, this.cv, TEMP_RECTBOUNDS, CompositeMode.SRC, this.cv);
                    }
                    if (bl2) {
                        this.clip.tex.unlock();
                    }
                    if (!bl) continue block41;
                    this.temp.tex.unlock();
                    continue block41;
                }
            }
            throw new InternalError("Unrecognized PGCanvas token: " + by);
        }
    }

    public void handleRenderOp(int n, GrowableDataBuffer growableDataBuffer, Graphics graphics, RectBounds rectBounds) {
        float f;
        boolean bl = false;
        boolean bl2 = false;
        block0 : switch (n) {
            case 47: {
                if (rectBounds != null) {
                    NGCanvas.shapebounds(this.path, rectBounds, BaseTransform.IDENTITY_TRANSFORM);
                }
                if (graphics == null) break;
                this.setupFill(graphics);
                graphics.fill(this.untransformedPath);
                break;
            }
            case 48: {
                if (rectBounds != null) {
                    NGCanvas.strokebounds(this.getStroke(), this.untransformedPath, rectBounds, this.transform);
                }
                if (graphics == null) break;
                this.setupStroke(graphics);
                graphics.draw(this.untransformedPath);
                break;
            }
            case 28: {
                float f2 = growableDataBuffer.getFloat();
                f = growableDataBuffer.getFloat();
                float f3 = growableDataBuffer.getFloat();
                float f4 = growableDataBuffer.getFloat();
                if (rectBounds != null) {
                    rectBounds.setBoundsAndSort(f2, f, f3, f4);
                    bl = true;
                    bl2 = true;
                }
                if (graphics == null) break;
                this.setupStroke(graphics);
                graphics.drawLine(f2, f, f3, f4);
                break;
            }
            case 26: 
            case 30: {
                bl = true;
            }
            case 25: 
            case 27: 
            case 29: {
                float f2 = growableDataBuffer.getFloat();
                f = growableDataBuffer.getFloat();
                float f5 = growableDataBuffer.getFloat();
                float f6 = growableDataBuffer.getFloat();
                if (rectBounds != null) {
                    rectBounds.setBounds(f2, f, f2 + f5, f + f6);
                    bl2 = true;
                }
                if (graphics == null) break;
                switch (n) {
                    case 25: {
                        this.setupFill(graphics);
                        graphics.fillRect(f2, f, f5, f6);
                        break block0;
                    }
                    case 29: {
                        this.setupFill(graphics);
                        graphics.fillEllipse(f2, f, f5, f6);
                        break block0;
                    }
                    case 26: {
                        this.setupStroke(graphics);
                        graphics.drawRect(f2, f, f5, f6);
                        break block0;
                    }
                    case 30: {
                        this.setupStroke(graphics);
                        graphics.drawEllipse(f2, f, f5, f6);
                        break block0;
                    }
                    case 27: {
                        graphics.setCompositeMode(CompositeMode.CLEAR);
                        graphics.fillRect(f2, f, f5, f6);
                        graphics.setCompositeMode(CompositeMode.SRC_OVER);
                    }
                }
                break;
            }
            case 32: {
                bl = true;
            }
            case 31: {
                float f2 = growableDataBuffer.getFloat();
                f = growableDataBuffer.getFloat();
                float f7 = growableDataBuffer.getFloat();
                float f8 = growableDataBuffer.getFloat();
                float f9 = growableDataBuffer.getFloat();
                float f10 = growableDataBuffer.getFloat();
                if (rectBounds != null) {
                    rectBounds.setBounds(f2, f, f2 + f7, f + f8);
                    bl2 = true;
                }
                if (graphics == null) break;
                if (n == 31) {
                    this.setupFill(graphics);
                    graphics.fillRoundRect(f2, f, f7, f8, f9, f10);
                    break;
                }
                this.setupStroke(graphics);
                graphics.drawRoundRect(f2, f, f7, f8, f9, f10);
                break;
            }
            case 33: 
            case 34: {
                float f2 = growableDataBuffer.getFloat();
                f = growableDataBuffer.getFloat();
                float f11 = growableDataBuffer.getFloat();
                float f12 = growableDataBuffer.getFloat();
                float f13 = growableDataBuffer.getFloat();
                float f14 = growableDataBuffer.getFloat();
                TEMP_ARC.setArc(f2, f, f11, f12, f13, f14, this.arctype);
                if (n == 33) {
                    if (rectBounds != null) {
                        NGCanvas.shapebounds(TEMP_ARC, rectBounds, this.transform);
                    }
                    if (graphics == null) break;
                    this.setupFill(graphics);
                    graphics.fill(TEMP_ARC);
                    break;
                }
                if (rectBounds != null) {
                    NGCanvas.strokebounds(this.getStroke(), TEMP_ARC, rectBounds, this.transform);
                }
                if (graphics == null) break;
                this.setupStroke(graphics);
                graphics.draw(TEMP_ARC);
                break;
            }
            case 50: 
            case 51: {
                float f15;
                float f16;
                float f17;
                float f18;
                float f2 = growableDataBuffer.getFloat();
                f = growableDataBuffer.getFloat();
                float f19 = growableDataBuffer.getFloat();
                float f20 = growableDataBuffer.getFloat();
                Image image = (Image)growableDataBuffer.getObject();
                if (n == 50) {
                    f18 = 0.0f;
                    f17 = 0.0f;
                    f16 = image.getWidth();
                    f15 = image.getHeight();
                } else {
                    f17 = growableDataBuffer.getFloat();
                    f18 = growableDataBuffer.getFloat();
                    f16 = growableDataBuffer.getFloat();
                    f15 = growableDataBuffer.getFloat();
                    float f21 = image.getPixelScale();
                    if (f21 != 1.0f) {
                        f17 *= f21;
                        f18 *= f21;
                        f16 *= f21;
                        f15 *= f21;
                    }
                }
                if (rectBounds != null) {
                    rectBounds.setBounds(f2, f, f2 + f19, f + f20);
                    bl2 = true;
                }
                if (graphics == null) break;
                ResourceFactory resourceFactory = graphics.getResourceFactory();
                Texture texture = resourceFactory.getCachedTexture(image, Texture.WrapMode.CLAMP_TO_EDGE);
                boolean bl3 = texture.getLinearFiltering();
                if (this.imageSmoothing != bl3) {
                    texture.setLinearFiltering(this.imageSmoothing);
                }
                graphics.drawTexture(texture, f2, f, f2 + f19, f + f20, f17, f18, f17 + f16, f18 + f15);
                if (this.imageSmoothing != bl3) {
                    texture.setLinearFiltering(bl3);
                }
                texture.unlock();
                break;
            }
            case 35: 
            case 36: {
                float f2 = growableDataBuffer.getFloat();
                f = growableDataBuffer.getFloat();
                float f22 = growableDataBuffer.getFloat();
                boolean bl4 = growableDataBuffer.getBoolean();
                String string = (String)growableDataBuffer.getObject();
                int n2 = bl4 ? 2048 : 1024;
                this.textLayout.setContent(string, this.pgfont);
                this.textLayout.setAlignment(this.align);
                this.textLayout.setDirection(n2);
                float f23 = 0.0f;
                float f24 = 0.0f;
                BaseBounds baseBounds = this.textLayout.getBounds();
                float f25 = baseBounds.getWidth();
                float f26 = baseBounds.getHeight();
                switch (this.align) {
                    case 2: {
                        f23 = f25;
                        break;
                    }
                    case 1: {
                        f23 = f25 / 2.0f;
                    }
                }
                switch (this.baseline) {
                    case 2: {
                        f24 = -baseBounds.getMinY();
                        break;
                    }
                    case 1: {
                        f24 = f26 / 2.0f;
                        break;
                    }
                    case 3: {
                        f24 = f26;
                    }
                }
                float f27 = 1.0f;
                float f28 = 0.0f;
                float f29 = f - f24;
                if ((double)f22 > 0.0 && f25 > f22) {
                    float f30 = f22 / f25;
                    if (bl4) {
                        f28 = -((f2 + f22) / f30 - f23);
                        f27 = -f30;
                    } else {
                        f28 = f2 / f30 - f23;
                        f27 = f30;
                    }
                } else if (bl4) {
                    f28 = -(f2 - f23 + f25);
                    f27 = -1.0f;
                } else {
                    f28 = f2 - f23;
                }
                if (rectBounds != null) {
                    this.computeTextLayoutBounds(rectBounds, this.transform, f27, f28, f29, n);
                }
                if (graphics == null) break;
                if (f27 != 1.0f) {
                    graphics.scale(f27, 1.0f);
                }
                this.ngtext.setLayoutLocation(-f28, -f29);
                if (n == 35) {
                    this.ngtext.setMode(NGShape.Mode.FILL);
                    this.ngtext.setFillPaint(this.fillPaint);
                    if (this.fillPaint.isProportional() || this.smoothing == SMOOTH_LCD) {
                        RectBounds rectBounds2 = new RectBounds();
                        this.computeTextLayoutBounds(rectBounds2, BaseTransform.IDENTITY_TRANSFORM, 1.0f, f28, f29, n);
                        this.ngtext.setContentBounds(rectBounds2);
                    }
                } else {
                    if (this.strokePaint.isProportional()) {
                        RectBounds rectBounds3 = new RectBounds();
                        this.computeTextLayoutBounds(rectBounds3, BaseTransform.IDENTITY_TRANSFORM, 1.0f, f28, f29, n);
                        this.ngtext.setContentBounds(rectBounds3);
                    }
                    this.ngtext.setMode(NGShape.Mode.STROKE);
                    this.ngtext.setDrawStroke(this.getStroke());
                    this.ngtext.setDrawPaint(this.strokePaint);
                }
                this.ngtext.setFont(this.pgfont);
                this.ngtext.setFontSmoothingType(this.smoothing);
                this.ngtext.setGlyphs(this.textLayout.getRuns());
                this.ngtext.renderContent(graphics);
                break;
            }
            default: {
                throw new InternalError("Unrecognized PGCanvas rendering token: " + n);
            }
        }
        if (rectBounds != null) {
            BasicStroke basicStroke;
            if (bl && (basicStroke = this.getStroke()).getType() != 1) {
                f = basicStroke.getLineWidth();
                if (basicStroke.getType() == 0) {
                    f /= 2.0f;
                }
                rectBounds.grow(f, f);
            }
            if (bl2) {
                NGCanvas.txBounds(rectBounds, this.transform);
            }
        }
    }

    void computeTextLayoutBounds(RectBounds rectBounds, BaseTransform baseTransform, float f, float f2, float f3, int n) {
        this.textLayout.getBounds(null, rectBounds);
        TEMP_TX.setTransform(baseTransform);
        TEMP_TX.scale(f, 1.0);
        TEMP_TX.translate(f2, f3);
        TEMP_TX.transform(rectBounds, rectBounds);
        if (n == 36) {
            int n2 = 1;
            Shape shape = this.textLayout.getShape(n2, null);
            RectBounds rectBounds2 = new RectBounds();
            NGCanvas.strokebounds(this.getStroke(), shape, rectBounds2, TEMP_TX);
            rectBounds.unionWith(rectBounds2);
        }
    }

    static void txBounds(RectBounds rectBounds, BaseTransform baseTransform) {
        switch (baseTransform.getType()) {
            case 0: {
                break;
            }
            case 1: {
                float f = (float)baseTransform.getMxt();
                float f2 = (float)baseTransform.getMyt();
                rectBounds.setBounds(rectBounds.getMinX() + f, rectBounds.getMinY() + f2, rectBounds.getMaxX() + f, rectBounds.getMaxY() + f2);
                break;
            }
            default: {
                BaseBounds baseBounds = baseTransform.transform(rectBounds, rectBounds);
                if (baseBounds == rectBounds) break;
                rectBounds.setBounds(baseBounds.getMinX(), baseBounds.getMinY(), baseBounds.getMaxX(), baseBounds.getMaxY());
            }
        }
    }

    static void inverseTxBounds(RectBounds rectBounds, BaseTransform baseTransform) {
        switch (baseTransform.getType()) {
            case 0: {
                break;
            }
            case 1: {
                float f = (float)baseTransform.getMxt();
                float f2 = (float)baseTransform.getMyt();
                rectBounds.setBounds(rectBounds.getMinX() - f, rectBounds.getMinY() - f2, rectBounds.getMaxX() - f, rectBounds.getMaxY() - f2);
                break;
            }
            default: {
                try {
                    BaseBounds baseBounds = baseTransform.inverseTransform(rectBounds, rectBounds);
                    if (baseBounds == rectBounds) break;
                    rectBounds.setBounds(baseBounds.getMinX(), baseBounds.getMinY(), baseBounds.getMaxX(), baseBounds.getMaxY());
                    break;
                }
                catch (NoninvertibleTransformException noninvertibleTransformException) {
                    rectBounds.makeEmpty();
                }
            }
        }
    }

    public void updateBounds(float f, float f2) {
        this.tw = (int)Math.ceil(f * this.highestPixelScale);
        this.th = (int)Math.ceil(f2 * this.highestPixelScale);
        this.geometryChanged();
    }

    public boolean updateRendering(GrowableDataBuffer growableDataBuffer) {
        GrowableDataBuffer growableDataBuffer2;
        boolean bl;
        if (growableDataBuffer.isEmpty()) {
            GrowableDataBuffer.returnBuffer(growableDataBuffer);
            return this.thebuf != null;
        }
        boolean bl2 = bl = growableDataBuffer.peekByte(0) == 70;
        if (bl || this.thebuf == null) {
            growableDataBuffer2 = this.thebuf;
            this.thebuf = growableDataBuffer;
        } else {
            this.thebuf.append(growableDataBuffer);
            growableDataBuffer2 = growableDataBuffer;
        }
        this.geometryChanged();
        if (growableDataBuffer2 != null) {
            GrowableDataBuffer.returnBuffer(growableDataBuffer2);
            return true;
        }
        return false;
    }

    static class RenderBuf {
        final InitType init_type;
        RTTexture tex;
        Graphics g;
        EffectInput input;
        private PixelData savedPixelData = null;

        public RenderBuf(InitType initType) {
            this.init_type = initType;
        }

        public void dispose() {
            if (this.tex != null) {
                this.tex.dispose();
            }
            this.tex = null;
            this.g = null;
            this.input = null;
        }

        public boolean validate(Graphics graphics, int n, int n2) {
            boolean bl;
            int n3;
            int n4;
            if (this.tex == null) {
                n4 = 0;
                n3 = 0;
                bl = true;
            } else {
                n3 = this.tex.getContentWidth();
                n4 = this.tex.getContentHeight();
                this.tex.lock();
                boolean bl2 = bl = this.tex.isSurfaceLost() || n3 < n || n4 < n2;
            }
            if (bl) {
                RTTexture rTTexture;
                RTTexture rTTexture2 = this.tex;
                ResourceFactory resourceFactory = graphics == null ? GraphicsPipeline.getDefaultResourceFactory() : graphics.getResourceFactory();
                this.tex = rTTexture = resourceFactory.createRTTexture(n, n2, Texture.WrapMode.CLAMP_TO_ZERO);
                this.g = rTTexture.createGraphics();
                this.input = new EffectInput(rTTexture);
                if (rTTexture2 != null) {
                    if (this.init_type == InitType.PRESERVE_UPPER_LEFT) {
                        this.g.setCompositeMode(CompositeMode.SRC);
                        if (rTTexture2.isSurfaceLost()) {
                            if (this.savedPixelData != null) {
                                this.savedPixelData.restore(this.g, n3, n4);
                            }
                        } else {
                            this.g.drawTexture(rTTexture2, 0.0f, 0.0f, n3, n4);
                        }
                        this.g.setCompositeMode(CompositeMode.SRC_OVER);
                    }
                    rTTexture2.unlock();
                    rTTexture2.dispose();
                }
                if (this.init_type == InitType.FILL_WHITE) {
                    this.g.clear(Color.WHITE);
                }
                return true;
            }
            if (this.g == null) {
                this.g = this.tex.createGraphics();
                if (this.g == null) {
                    this.tex.dispose();
                    ResourceFactory resourceFactory = graphics == null ? GraphicsPipeline.getDefaultResourceFactory() : graphics.getResourceFactory();
                    this.tex = resourceFactory.createRTTexture(n, n2, Texture.WrapMode.CLAMP_TO_ZERO);
                    this.g = this.tex.createGraphics();
                    this.input = new EffectInput(this.tex);
                    if (this.savedPixelData != null) {
                        this.g.setCompositeMode(CompositeMode.SRC);
                        this.savedPixelData.restore(this.g, n, n2);
                        this.g.setCompositeMode(CompositeMode.SRC_OVER);
                    } else if (this.init_type == InitType.FILL_WHITE) {
                        this.g.clear(Color.WHITE);
                    }
                    return true;
                }
            }
            if (this.init_type == InitType.CLEAR) {
                this.g.clear();
            }
            return false;
        }

        private void save(int n, int n2) {
            if (this.tex.isVolatile()) {
                if (this.savedPixelData == null) {
                    this.savedPixelData = new PixelData(n, n2);
                }
                this.savedPixelData.save(this.tex);
            }
        }
    }

    static final class InitType
    extends Enum<InitType> {
        public static final /* enum */ InitType CLEAR = new InitType();
        public static final /* enum */ InitType FILL_WHITE = new InitType();
        public static final /* enum */ InitType PRESERVE_UPPER_LEFT = new InitType();
        private static final /* synthetic */ InitType[] $VALUES;

        public static InitType[] values() {
            return (InitType[])$VALUES.clone();
        }

        public static InitType valueOf(String string) {
            return Enum.valueOf(InitType.class, string);
        }

        private static /* synthetic */ InitType[] $values() {
            return new InitType[]{CLEAR, FILL_WHITE, PRESERVE_UPPER_LEFT};
        }

        static {
            $VALUES = InitType.$values();
        }
    }

    private static class PixelData {
        private IntBuffer pixels = null;
        private boolean validPixels = false;
        private int cw;
        private int ch;

        private PixelData(int n, int n2) {
            this.cw = n;
            this.ch = n2;
            this.pixels = IntBuffer.allocate(n * n2);
        }

        private void save(RTTexture rTTexture) {
            int n = rTTexture.getContentWidth();
            int n2 = rTTexture.getContentHeight();
            if (this.cw < n || this.ch < n2) {
                this.cw = n;
                this.ch = n2;
                this.pixels = IntBuffer.allocate(this.cw * this.ch);
            }
            this.pixels.rewind();
            rTTexture.readPixels(this.pixels);
            this.validPixels = true;
        }

        private void restore(Graphics graphics, int n, int n2) {
            if (this.validPixels) {
                Image image = Image.fromIntArgbPreData(this.pixels, n, n2);
                ResourceFactory resourceFactory = graphics.getResourceFactory();
                Texture texture = resourceFactory.createTexture(image, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
                graphics.drawTexture(texture, 0.0f, 0.0f, n, n2);
                texture.dispose();
            }
        }
    }

    static class EffectInput
    extends Effect {
        RTTexture tex;
        float pixelscale;

        EffectInput(RTTexture rTTexture) {
            this.tex = rTTexture;
            this.pixelscale = 1.0f;
        }

        public void setPixelScale(float f) {
            this.pixelscale = f;
        }

        @Override
        public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
            PrDrawable prDrawable = PrDrawable.create(filterContext, this.tex);
            Rectangle rectangle2 = new Rectangle(this.tex.getContentWidth(), this.tex.getContentHeight());
            prDrawable.lock();
            ImageData imageData = new ImageData(filterContext, prDrawable, rectangle2);
            imageData.setReusable(true);
            if (this.pixelscale != 1.0f || !baseTransform.isIdentity()) {
                Affine2D affine2D = new Affine2D();
                affine2D.scale(1.0f / this.pixelscale, 1.0f / this.pixelscale);
                affine2D.concatenate(baseTransform);
                imageData = imageData.transform(affine2D);
            }
            return imageData;
        }

        @Override
        public Effect.AccelType getAccelType(FilterContext filterContext) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
            Rectangle rectangle = new Rectangle(this.tex.getContentWidth(), this.tex.getContentHeight());
            return EffectInput.transformBounds(baseTransform, new RectBounds(rectangle));
        }

        @Override
        public boolean reducesOpaquePixels() {
            return false;
        }

        @Override
        public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
            return null;
        }
    }

    class RenderInput
    extends Effect {
        float x;
        float y;
        float w;
        float h;
        int token;
        GrowableDataBuffer buf;
        Affine2D savedBoundsTx = new Affine2D();

        public RenderInput(int n, GrowableDataBuffer growableDataBuffer, BaseTransform baseTransform, RectBounds rectBounds) {
            this.token = n;
            this.buf = growableDataBuffer;
            this.savedBoundsTx.setTransform(baseTransform);
            this.x = rectBounds.getMinX();
            this.y = rectBounds.getMinY();
            this.w = rectBounds.getWidth();
            this.h = rectBounds.getHeight();
        }

        @Override
        public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
            PrDrawable prDrawable;
            BaseBounds baseBounds = this.getBounds(baseTransform, effect);
            if (rectangle != null) {
                baseBounds.intersectWith(rectangle);
            }
            Rectangle rectangle2 = new Rectangle(baseBounds);
            if (rectangle2.width < 1) {
                rectangle2.width = 1;
            }
            if (rectangle2.height < 1) {
                rectangle2.height = 1;
            }
            if ((prDrawable = (PrDrawable)Effect.getCompatibleImage(filterContext, rectangle2.width, rectangle2.height)) != null) {
                Graphics graphics = prDrawable.createGraphics();
                graphics.setExtraAlpha(NGCanvas.this.globalAlpha);
                graphics.translate(-rectangle2.x, -rectangle2.y);
                if (baseTransform != null) {
                    graphics.transform(baseTransform);
                }
                this.buf.restore();
                NGCanvas.this.handleRenderOp(this.token, this.buf, graphics, null);
            }
            return new ImageData(filterContext, prDrawable, rectangle2);
        }

        @Override
        public Effect.AccelType getAccelType(FilterContext filterContext) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
            RectBounds rectBounds = new RectBounds(this.x, this.y, this.x + this.w, this.y + this.h);
            if (!baseTransform.equals(this.savedBoundsTx)) {
                NGCanvas.inverseTxBounds(rectBounds, this.savedBoundsTx);
                NGCanvas.txBounds(rectBounds, baseTransform);
            }
            return rectBounds;
        }

        @Override
        public boolean reducesOpaquePixels() {
            return false;
        }

        @Override
        public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
            return null;
        }
    }

    static class MyBlend
    extends Blend {
        public MyBlend(Blend.Mode mode, Effect effect, Effect effect2) {
            super(mode, effect, effect2);
        }

        @Override
        public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
            Rectangle rectangle2 = super.getResultBounds(baseTransform, rectangle, arrimageData);
            rectangle2.intersectWith(rectangle);
            return rectangle2;
        }
    }
}

