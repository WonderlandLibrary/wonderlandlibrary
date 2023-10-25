/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.marlin.DPathConsumer2D;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinProperties;
import com.sun.marlin.MarlinRenderer;
import com.sun.marlin.MarlinUtils;
import com.sun.marlin.RendererContext;
import com.sun.marlin.TransformingPathConsumer2D;
import com.sun.prism.BasicStroke;
import java.util.Arrays;

public final class DMarlinPrismUtils {
    private static final boolean FORCE_NO_AA = false;
    static final boolean DISABLE_2ND_STROKER_CLIPPING = true;
    static final boolean DO_TRACE_PATH = false;
    static final boolean DO_CLIP = MarlinProperties.isDoClip();
    static final boolean DO_CLIP_FILL = true;
    static final boolean DO_CLIP_RUNTIME_ENABLE = MarlinProperties.isDoClipRuntimeFlag();
    static final float UPPER_BND = 1.7014117E38f;
    static final float LOWER_BND = -1.7014117E38f;

    private DMarlinPrismUtils() {
    }

    private static DPathConsumer2D initStroker(RendererContext rendererContext, BasicStroke basicStroke, float f, BaseTransform baseTransform, DPathConsumer2D dPathConsumer2D) {
        BaseTransform baseTransform2 = null;
        int n = -1;
        boolean bl = false;
        double d = f;
        float[] arrf = basicStroke.getDashArray();
        double[] arrd = null;
        double d2 = basicStroke.getDashPhase();
        if (arrf != null) {
            bl = true;
            n = arrf.length;
            arrd = rendererContext.dasher.copyDashArray(arrf);
        }
        if (baseTransform != null && !baseTransform.isIdentity()) {
            double d3;
            double d4;
            double d5;
            double d6 = baseTransform.getMxx();
            if (DMarlinPrismUtils.nearZero(d6 * (d5 = baseTransform.getMxy()) + (d4 = baseTransform.getMyx()) * (d3 = baseTransform.getMyy())) && DMarlinPrismUtils.nearZero(d6 * d6 + d4 * d4 - (d5 * d5 + d3 * d3))) {
                double d7 = Math.sqrt(d6 * d6 + d4 * d4);
                if (arrd != null) {
                    int n2 = 0;
                    while (n2 < n) {
                        int n3 = n2++;
                        arrd[n3] = arrd[n3] * d7;
                    }
                    d2 *= d7;
                }
                d *= d7;
            } else {
                baseTransform2 = baseTransform;
            }
        } else {
            baseTransform = null;
        }
        DPathConsumer2D dPathConsumer2D2 = dPathConsumer2D;
        TransformingPathConsumer2D transformingPathConsumer2D = rendererContext.transformerPC2D;
        if (MarlinConst.USE_SIMPLIFIER) {
            dPathConsumer2D2 = rendererContext.simplifier.init(dPathConsumer2D2);
        }
        dPathConsumer2D2 = transformingPathConsumer2D.deltaTransformConsumer(dPathConsumer2D2, baseTransform2);
        dPathConsumer2D2 = rendererContext.stroker.init(dPathConsumer2D2, d, basicStroke.getEndCap(), basicStroke.getLineJoin(), basicStroke.getMiterLimit(), arrd == null);
        rendererContext.monotonizer.init(d);
        if (arrd != null) {
            dPathConsumer2D2 = rendererContext.dasher.init(dPathConsumer2D2, arrd, n, d2, bl);
            rendererContext.stroker.disableClipping();
        } else if (rendererContext.doClip && basicStroke.getEndCap() != 0) {
            dPathConsumer2D2 = transformingPathConsumer2D.detectClosedPath(dPathConsumer2D2);
        }
        dPathConsumer2D2 = transformingPathConsumer2D.inverseDeltaTransformConsumer(dPathConsumer2D2, baseTransform2);
        return dPathConsumer2D2;
    }

    private static boolean nearZero(double d) {
        return Math.abs(d) < 2.0 * Math.ulp(d);
    }

    private static DPathConsumer2D initRenderer(RendererContext rendererContext, BasicStroke basicStroke, BaseTransform baseTransform, Rectangle rectangle, int n, MarlinRenderer marlinRenderer) {
        if (DO_CLIP || DO_CLIP_RUNTIME_ENABLE && MarlinProperties.isDoClipAtRuntime()) {
            double[] arrd = rendererContext.clipRect;
            double d = marlinRenderer.getOffsetX();
            double d2 = marlinRenderer.getOffsetY();
            arrd[0] = (double)rectangle.y - 0.001 + d2;
            arrd[1] = (double)(rectangle.y + rectangle.height) + 0.001 + d2;
            arrd[2] = (double)rectangle.x - 0.001 + d;
            arrd[3] = (double)(rectangle.x + rectangle.width) + 0.001 + d;
            if (MarlinConst.DO_LOG_CLIP) {
                MarlinUtils.logInfo("clipRect (clip): " + Arrays.toString(rendererContext.clipRect));
            }
            rendererContext.doClip = true;
        }
        if (basicStroke != null) {
            marlinRenderer.init(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 1);
            return DMarlinPrismUtils.initStroker(rendererContext, basicStroke, basicStroke.getLineWidth(), baseTransform, marlinRenderer);
        }
        int n2 = n == 0 ? 0 : 1;
        marlinRenderer.init(rectangle.x, rectangle.y, rectangle.width, rectangle.height, n2);
        DPathConsumer2D dPathConsumer2D = marlinRenderer;
        TransformingPathConsumer2D transformingPathConsumer2D = rendererContext.transformerPC2D;
        if (rendererContext.doClip) {
            dPathConsumer2D = rendererContext.transformerPC2D.pathClipper(dPathConsumer2D);
        }
        return dPathConsumer2D;
    }

    public static MarlinRenderer setupRenderer(RendererContext rendererContext, Shape shape, BasicStroke basicStroke, BaseTransform baseTransform, Rectangle rectangle, boolean bl) {
        MarlinRenderer marlinRenderer;
        BaseTransform baseTransform2 = baseTransform != null && !baseTransform.isIdentity() ? baseTransform : null;
        MarlinRenderer marlinRenderer2 = marlinRenderer = bl ? rendererContext.renderer : rendererContext.getRendererNoAA();
        if (shape instanceof Path2D) {
            Path2D path2D = (Path2D)shape;
            DPathConsumer2D dPathConsumer2D = DMarlinPrismUtils.initRenderer(rendererContext, basicStroke, baseTransform2, rectangle, path2D.getWindingRule(), marlinRenderer);
            DMarlinPrismUtils.feedConsumer(rendererContext, path2D, baseTransform2, dPathConsumer2D);
        } else {
            PathIterator pathIterator = shape.getPathIterator(baseTransform2);
            DPathConsumer2D dPathConsumer2D = DMarlinPrismUtils.initRenderer(rendererContext, basicStroke, baseTransform2, rectangle, pathIterator.getWindingRule(), marlinRenderer);
            DMarlinPrismUtils.feedConsumer(rendererContext, pathIterator, dPathConsumer2D);
        }
        return marlinRenderer;
    }

    public static void strokeTo(RendererContext rendererContext, Shape shape, BasicStroke basicStroke, float f, DPathConsumer2D dPathConsumer2D) {
        DPathConsumer2D dPathConsumer2D2 = DMarlinPrismUtils.initStroker(rendererContext, basicStroke, f, null, dPathConsumer2D);
        if (shape instanceof Path2D) {
            DMarlinPrismUtils.feedConsumer(rendererContext, (Path2D)shape, null, dPathConsumer2D2);
        } else {
            DMarlinPrismUtils.feedConsumer(rendererContext, shape.getPathIterator(null), dPathConsumer2D2);
        }
    }

    private static void feedConsumer(RendererContext rendererContext, PathIterator pathIterator, DPathConsumer2D dPathConsumer2D) {
        if (MarlinConst.USE_PATH_SIMPLIFIER) {
            dPathConsumer2D = rendererContext.pathSimplifier.init(dPathConsumer2D);
        }
        rendererContext.dirty = true;
        float[] arrf = rendererContext.float6;
        boolean bl = false;
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf)) {
                case 0: {
                    if (!(arrf[0] < 1.7014117E38f) || !(arrf[0] > -1.7014117E38f) || !(arrf[1] < 1.7014117E38f) || !(arrf[1] > -1.7014117E38f)) break;
                    dPathConsumer2D.moveTo(arrf[0], arrf[1]);
                    bl = true;
                    break;
                }
                case 1: {
                    if (!(arrf[0] < 1.7014117E38f) || !(arrf[0] > -1.7014117E38f) || !(arrf[1] < 1.7014117E38f) || !(arrf[1] > -1.7014117E38f)) break;
                    if (bl) {
                        dPathConsumer2D.lineTo(arrf[0], arrf[1]);
                        break;
                    }
                    dPathConsumer2D.moveTo(arrf[0], arrf[1]);
                    bl = true;
                    break;
                }
                case 2: {
                    if (!(arrf[2] < 1.7014117E38f) || !(arrf[2] > -1.7014117E38f) || !(arrf[3] < 1.7014117E38f) || !(arrf[3] > -1.7014117E38f)) break;
                    if (bl) {
                        if (arrf[0] < 1.7014117E38f && arrf[0] > -1.7014117E38f && arrf[1] < 1.7014117E38f && arrf[1] > -1.7014117E38f) {
                            dPathConsumer2D.quadTo(arrf[0], arrf[1], arrf[2], arrf[3]);
                            break;
                        }
                        dPathConsumer2D.lineTo(arrf[2], arrf[3]);
                        break;
                    }
                    dPathConsumer2D.moveTo(arrf[2], arrf[3]);
                    bl = true;
                    break;
                }
                case 3: {
                    if (!(arrf[4] < 1.7014117E38f) || !(arrf[4] > -1.7014117E38f) || !(arrf[5] < 1.7014117E38f) || !(arrf[5] > -1.7014117E38f)) break;
                    if (bl) {
                        if (arrf[0] < 1.7014117E38f && arrf[0] > -1.7014117E38f && arrf[1] < 1.7014117E38f && arrf[1] > -1.7014117E38f && arrf[2] < 1.7014117E38f && arrf[2] > -1.7014117E38f && arrf[3] < 1.7014117E38f && arrf[3] > -1.7014117E38f) {
                            dPathConsumer2D.curveTo(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5]);
                            break;
                        }
                        dPathConsumer2D.lineTo(arrf[4], arrf[5]);
                        break;
                    }
                    dPathConsumer2D.moveTo(arrf[4], arrf[5]);
                    bl = true;
                    break;
                }
                case 4: {
                    if (!bl) break;
                    dPathConsumer2D.closePath();
                    break;
                }
            }
            pathIterator.next();
        }
        dPathConsumer2D.pathDone();
        rendererContext.dirty = false;
    }

    private static void feedConsumer(RendererContext rendererContext, Path2D path2D, BaseTransform baseTransform, DPathConsumer2D dPathConsumer2D) {
        if (MarlinConst.USE_PATH_SIMPLIFIER) {
            dPathConsumer2D = rendererContext.pathSimplifier.init(dPathConsumer2D);
        }
        rendererContext.dirty = true;
        float[] arrf = rendererContext.float6;
        boolean bl = false;
        float[] arrf2 = path2D.getFloatCoordsNoClone();
        byte[] arrby = path2D.getCommandsNoClone();
        int n = path2D.getNumCommands();
        int n2 = 0;
        block7: for (int i = 0; i < n; ++i) {
            switch (arrby[i]) {
                case 0: {
                    if (baseTransform == null) {
                        arrf[0] = arrf2[n2];
                        arrf[1] = arrf2[n2 + 1];
                    } else {
                        baseTransform.transform(arrf2, n2, arrf, 0, 1);
                    }
                    n2 += 2;
                    if (!(arrf[0] < 1.7014117E38f) || !(arrf[0] > -1.7014117E38f) || !(arrf[1] < 1.7014117E38f) || !(arrf[1] > -1.7014117E38f)) continue block7;
                    dPathConsumer2D.moveTo(arrf[0], arrf[1]);
                    bl = true;
                    continue block7;
                }
                case 1: {
                    if (baseTransform == null) {
                        arrf[0] = arrf2[n2];
                        arrf[1] = arrf2[n2 + 1];
                    } else {
                        baseTransform.transform(arrf2, n2, arrf, 0, 1);
                    }
                    n2 += 2;
                    if (!(arrf[0] < 1.7014117E38f) || !(arrf[0] > -1.7014117E38f) || !(arrf[1] < 1.7014117E38f) || !(arrf[1] > -1.7014117E38f)) continue block7;
                    if (bl) {
                        dPathConsumer2D.lineTo(arrf[0], arrf[1]);
                        continue block7;
                    }
                    dPathConsumer2D.moveTo(arrf[0], arrf[1]);
                    bl = true;
                    continue block7;
                }
                case 2: {
                    if (baseTransform == null) {
                        arrf[0] = arrf2[n2];
                        arrf[1] = arrf2[n2 + 1];
                        arrf[2] = arrf2[n2 + 2];
                        arrf[3] = arrf2[n2 + 3];
                    } else {
                        baseTransform.transform(arrf2, n2, arrf, 0, 2);
                    }
                    n2 += 4;
                    if (!(arrf[2] < 1.7014117E38f) || !(arrf[2] > -1.7014117E38f) || !(arrf[3] < 1.7014117E38f) || !(arrf[3] > -1.7014117E38f)) continue block7;
                    if (bl) {
                        if (arrf[0] < 1.7014117E38f && arrf[0] > -1.7014117E38f && arrf[1] < 1.7014117E38f && arrf[1] > -1.7014117E38f) {
                            dPathConsumer2D.quadTo(arrf[0], arrf[1], arrf[2], arrf[3]);
                            continue block7;
                        }
                        dPathConsumer2D.lineTo(arrf[2], arrf[3]);
                        continue block7;
                    }
                    dPathConsumer2D.moveTo(arrf[2], arrf[3]);
                    bl = true;
                    continue block7;
                }
                case 3: {
                    if (baseTransform == null) {
                        arrf[0] = arrf2[n2];
                        arrf[1] = arrf2[n2 + 1];
                        arrf[2] = arrf2[n2 + 2];
                        arrf[3] = arrf2[n2 + 3];
                        arrf[4] = arrf2[n2 + 4];
                        arrf[5] = arrf2[n2 + 5];
                    } else {
                        baseTransform.transform(arrf2, n2, arrf, 0, 3);
                    }
                    n2 += 6;
                    if (!(arrf[4] < 1.7014117E38f) || !(arrf[4] > -1.7014117E38f) || !(arrf[5] < 1.7014117E38f) || !(arrf[5] > -1.7014117E38f)) continue block7;
                    if (bl) {
                        if (arrf[0] < 1.7014117E38f && arrf[0] > -1.7014117E38f && arrf[1] < 1.7014117E38f && arrf[1] > -1.7014117E38f && arrf[2] < 1.7014117E38f && arrf[2] > -1.7014117E38f && arrf[3] < 1.7014117E38f && arrf[3] > -1.7014117E38f) {
                            dPathConsumer2D.curveTo(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5]);
                            continue block7;
                        }
                        dPathConsumer2D.lineTo(arrf[4], arrf[5]);
                        continue block7;
                    }
                    dPathConsumer2D.moveTo(arrf[4], arrf[5]);
                    bl = true;
                    continue block7;
                }
                case 4: {
                    if (!bl) continue block7;
                    dPathConsumer2D.closePath();
                    continue block7;
                }
            }
        }
        dPathConsumer2D.pathDone();
        rendererContext.dirty = false;
    }
}

