/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.AccessHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Effect {
    public static final Effect DefaultInput = null;
    private final List<Effect> inputs;
    private final List<Effect> unmodifiableInputs;
    private final int maxInputs;

    protected Effect() {
        this.inputs = Collections.emptyList();
        this.unmodifiableInputs = this.inputs;
        this.maxInputs = 0;
    }

    protected Effect(Effect effect) {
        this.inputs = new ArrayList<Effect>(1);
        this.unmodifiableInputs = Collections.unmodifiableList(this.inputs);
        this.maxInputs = 1;
        this.setInput(0, effect);
    }

    protected Effect(Effect effect, Effect effect2) {
        this.inputs = new ArrayList<Effect>(2);
        this.unmodifiableInputs = Collections.unmodifiableList(this.inputs);
        this.maxInputs = 2;
        this.setInput(0, effect);
        this.setInput(1, effect2);
    }

    Object getState() {
        return null;
    }

    public int getNumInputs() {
        return this.inputs.size();
    }

    public final List<Effect> getInputs() {
        return this.unmodifiableInputs;
    }

    protected void setInput(int n, Effect effect) {
        if (n < 0 || n >= this.maxInputs) {
            throw new IllegalArgumentException("Index must be within allowable range");
        }
        if (n < this.inputs.size()) {
            this.inputs.set(n, effect);
        } else {
            this.inputs.add(effect);
        }
    }

    public static BaseBounds combineBounds(BaseBounds ... arrbaseBounds) {
        BaseBounds baseBounds = null;
        if (arrbaseBounds.length == 1) {
            baseBounds = arrbaseBounds[0];
        } else {
            for (int i = 0; i < arrbaseBounds.length; ++i) {
                BaseBounds baseBounds2 = arrbaseBounds[i];
                if (baseBounds2 == null || baseBounds2.isEmpty()) continue;
                if (baseBounds == null) {
                    baseBounds = new RectBounds();
                    baseBounds = baseBounds.deriveWithNewBounds(baseBounds2);
                    continue;
                }
                baseBounds = baseBounds.deriveWithUnion(baseBounds2);
            }
        }
        if (baseBounds == null) {
            baseBounds = new RectBounds();
        }
        return baseBounds;
    }

    public static Rectangle combineBounds(Rectangle ... arrrectangle) {
        Rectangle rectangle = null;
        if (arrrectangle.length == 1) {
            rectangle = arrrectangle[0];
        } else {
            for (int i = 0; i < arrrectangle.length; ++i) {
                Rectangle rectangle2 = arrrectangle[i];
                if (rectangle2 == null || rectangle2.isEmpty()) continue;
                if (rectangle == null) {
                    rectangle = new Rectangle(rectangle2);
                    continue;
                }
                rectangle.add(rectangle2);
            }
        }
        if (rectangle == null) {
            rectangle = new Rectangle();
        }
        return rectangle;
    }

    public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        int n = arrimageData.length;
        Rectangle[] arrrectangle = new Rectangle[n];
        for (int i = 0; i < n; ++i) {
            arrrectangle[i] = arrimageData[i].getTransformedBounds(rectangle);
        }
        Rectangle rectangle2 = Effect.combineBounds(arrrectangle);
        return rectangle2;
    }

    public abstract ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5);

    public static BaseBounds transformBounds(BaseTransform baseTransform, BaseBounds baseBounds) {
        if (baseTransform == null || baseTransform.isIdentity()) {
            return baseBounds;
        }
        BaseBounds baseBounds2 = new RectBounds();
        baseBounds2 = baseTransform.transform(baseBounds, baseBounds2);
        return baseBounds2;
    }

    protected ImageData ensureTransform(FilterContext filterContext, ImageData imageData, BaseTransform baseTransform, Rectangle rectangle) {
        if (baseTransform == null || baseTransform.isIdentity()) {
            return imageData;
        }
        if (!imageData.validate(filterContext)) {
            imageData.unref();
            return new ImageData(filterContext, null, new Rectangle());
        }
        return imageData.transform(baseTransform);
    }

    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        DirtyRegionContainer dirtyRegionContainer = null;
        for (int i = 0; i < this.inputs.size(); ++i) {
            DirtyRegionContainer dirtyRegionContainer2 = this.getDefaultedInput(i, effect).getDirtyRegions(effect, dirtyRegionPool);
            if (dirtyRegionContainer == null) {
                dirtyRegionContainer = dirtyRegionContainer2;
                continue;
            }
            dirtyRegionContainer.merge(dirtyRegionContainer2);
            dirtyRegionPool.checkIn(dirtyRegionContainer2);
        }
        if (dirtyRegionContainer == null) {
            dirtyRegionContainer = dirtyRegionPool.checkOut();
        }
        return dirtyRegionContainer;
    }

    Effect getDefaultedInput(int n, Effect effect) {
        return Effect.getDefaultedInput(this.inputs.get(n), effect);
    }

    static Effect getDefaultedInput(Effect effect, Effect effect2) {
        return effect == null ? effect2 : effect;
    }

    public abstract BaseBounds getBounds(BaseTransform var1, Effect var2);

    public Point2D transform(Point2D point2D, Effect effect) {
        return point2D;
    }

    public Point2D untransform(Point2D point2D, Effect effect) {
        return point2D;
    }

    public static Filterable createCompatibleImage(FilterContext filterContext, int n, int n2) {
        return Renderer.getRenderer(filterContext).createCompatibleImage(n, n2);
    }

    public static Filterable getCompatibleImage(FilterContext filterContext, int n, int n2) {
        return Renderer.getRenderer(filterContext).getCompatibleImage(n, n2);
    }

    public static void releaseCompatibleImage(FilterContext filterContext, Filterable filterable) {
        Renderer.getRenderer(filterContext).releaseCompatibleImage(filterable);
    }

    public abstract boolean reducesOpaquePixels();

    public abstract AccelType getAccelType(FilterContext var1);

    static {
        AccessHelper.setStateAccessor(effect -> effect.getState());
    }

    public static final class AccelType
    extends Enum<AccelType> {
        public static final /* enum */ AccelType INTRINSIC = new AccelType("Intrinsic");
        public static final /* enum */ AccelType NONE = new AccelType("CPU/Java");
        public static final /* enum */ AccelType SIMD = new AccelType("CPU/SIMD");
        public static final /* enum */ AccelType FIXED = new AccelType("CPU/Fixed");
        public static final /* enum */ AccelType OPENGL = new AccelType("OpenGL");
        public static final /* enum */ AccelType DIRECT3D = new AccelType("Direct3D");
        private String text;
        private static final /* synthetic */ AccelType[] $VALUES;

        public static AccelType[] values() {
            return (AccelType[])$VALUES.clone();
        }

        public static AccelType valueOf(String string) {
            return Enum.valueOf(AccelType.class, string);
        }

        private AccelType(String string2) {
            this.text = string2;
        }

        public String toString() {
            return this.text;
        }

        private static /* synthetic */ AccelType[] $values() {
            return new AccelType[]{INTRINSIC, NONE, SIMD, FIXED, OPENGL, DIRECT3D};
        }

        static {
            $VALUES = AccelType.$values();
        }
    }
}

