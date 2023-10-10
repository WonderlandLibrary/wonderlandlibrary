// 
// Decompiled by Procyon v0.5.36
// 

package com.maltaisn.msdfgdx.gen;

import java.awt.geom.PathIterator;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.ContinuationInterceptor;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.awt.geom.GeneralPath;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function1;
import java.util.List;

public final class Shape
{
    private final List<Contour> contours;
    public static final Companion Companion;
    
    @Override
    public final String toString() {
        return CollectionsKt___CollectionsJvmKt.joinToString$default$1494b5c(this.contours, " ", null, null, 0, null, null, 62);
    }
    
    public Shape(final List<Contour> contours) {
        Intrinsics.checkParameterIsNotNull(contours, "contours");
        this.contours = contours;
    }
    
    static {
        Companion = new Companion((byte)0);
    }
    
    public static final class Companion
    {
        public static Shape fromPath(final GeneralPath path) {
            Intrinsics.checkParameterIsNotNull(path, "path");
            final List contours = new ArrayList();
            final PathIterator iterator = path.getPathIterator(new AffineTransform());
            final List points = new ArrayList();
            final float[] coords = new float[6];
            while (true) {
                final PathIterator value = iterator;
                Intrinsics.checkExpressionValueIsNotNull(value, "iterator");
                if (value.isDone()) {
                    break;
                }
                switch (iterator.currentSegment(coords)) {
                    case 0:
                    case 1: {
                        points.add(new ContinuationInterceptor.DefaultImpls(coords[0], -coords[1]));
                        break;
                    }
                    case 2: {
                        points.add(new CoroutineContext.DefaultImpls(coords[0], -coords[1], coords[2], -coords[3]));
                        break;
                    }
                    case 3: {
                        points.add(new Pixmap(coords[0], -coords[1], coords[2], -coords[3], coords[4], -coords[5]));
                        break;
                    }
                    case 4: {
                        contours.add(new Contour(CollectionsKt___CollectionsJvmKt.toList((Iterable<?>)points)));
                        points.clear();
                        break;
                    }
                }
                iterator.next();
            }
            return new Shape(contours);
        }
        
        private Companion() {
        }
    }
}
