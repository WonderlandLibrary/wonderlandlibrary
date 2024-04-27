/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.utils;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u0086\u0001\u0018\u0000 \u00152\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0015B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J(\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eH&R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014\u00a8\u0006\u0016"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;", "", "typeName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getTypeName", "()Ljava/lang/String;", "performRendering", "", "x", "", "y", "rad", "col", "Ljava/awt/Color;", "SOLID_CIRCLE", "CIRCLE", "SOLID_RECT", "RECT", "SOLID_TRIANGLE", "TRIANGLE", "Companion", "LiKingSense"})
public abstract class ShapeType
extends Enum<ShapeType> {
    public static final /* enum */ ShapeType SOLID_CIRCLE;
    public static final /* enum */ ShapeType CIRCLE;
    public static final /* enum */ ShapeType SOLID_RECT;
    public static final /* enum */ ShapeType RECT;
    public static final /* enum */ ShapeType SOLID_TRIANGLE;
    public static final /* enum */ ShapeType TRIANGLE;
    private static final /* synthetic */ ShapeType[] $VALUES;
    @NotNull
    private final String typeName;
    public static final Companion Companion;

    static {
        ShapeType[] shapeTypeArray = new ShapeType[6];
        ShapeType[] shapeTypeArray2 = shapeTypeArray;
        shapeTypeArray[0] = SOLID_CIRCLE = new SOLID_CIRCLE("SOLID_CIRCLE", 0);
        shapeTypeArray[1] = CIRCLE = new CIRCLE("CIRCLE", 1);
        shapeTypeArray[2] = SOLID_RECT = new SOLID_RECT("SOLID_RECT", 2);
        shapeTypeArray[3] = RECT = new RECT("RECT", 3);
        shapeTypeArray[4] = SOLID_TRIANGLE = new SOLID_TRIANGLE("SOLID_TRIANGLE", 4);
        shapeTypeArray[5] = TRIANGLE = new TRIANGLE("TRIANGLE", 5);
        $VALUES = shapeTypeArray;
        Companion = new Companion(null);
    }

    public abstract void performRendering(float var1, float var2, float var3, @NotNull Color var4);

    @NotNull
    public final String getTypeName() {
        return this.typeName;
    }

    private ShapeType(String typeName) {
        this.typeName = typeName;
    }

    public /* synthetic */ ShapeType(String $enum_name_or_ordinal$0, int $enum_name_or_ordinal$1, String typeName, DefaultConstructorMarker $constructor_marker) {
        this(typeName);
    }

    public static ShapeType[] values() {
        return (ShapeType[])$VALUES.clone();
    }

    public static ShapeType valueOf(String string) {
        return Enum.valueOf(ShapeType.class, string);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u0016\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType$SOLID_CIRCLE;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;", "performRendering", "", "x", "", "y", "rad", "col", "Ljava/awt/Color;", "LiKingSense"})
    static final class SOLID_CIRCLE
    extends ShapeType {
        @Override
        public void performRendering(float x, float y, float rad, @NotNull Color col) {
            Intrinsics.checkParameterIsNotNull((Object)col, (String)"col");
            RenderUtils.drawFilledCircle(x, y, rad, col);
        }

        /*
         * WARNING - void declaration
         */
        SOLID_CIRCLE() {
            void var1_1;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u0016\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType$CIRCLE;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;", "performRendering", "", "x", "", "y", "rad", "col", "Ljava/awt/Color;", "LiKingSense"})
    static final class CIRCLE
    extends ShapeType {
        /*
         * Exception decompiling
         */
        @Override
        public void performRendering(float x, float y, float rad, @NotNull Color col) {
            /*
             * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
             * 
             * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl9 : INVOKESTATIC - null : Stack underflow
             *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
             *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
             *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
             *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
             *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:923)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
             *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
             *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
             *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
             *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
             *     at org.benf.cfr.reader.Main.main(Main.java:54)
             */
            throw new IllegalStateException("Decompilation failed");
        }

        /*
         * WARNING - void declaration
         */
        CIRCLE() {
            void var1_1;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u0016\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType$SOLID_RECT;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;", "performRendering", "", "x", "", "y", "rad", "col", "Ljava/awt/Color;", "LiKingSense"})
    static final class SOLID_RECT
    extends ShapeType {
        @Override
        public void performRendering(float x, float y, float rad, @NotNull Color col) {
            Intrinsics.checkParameterIsNotNull((Object)col, (String)"col");
            RenderUtils.drawRect(x - rad / 2.0f, y - rad / 2.0f, x + rad / 2.0f, y + rad / 2.0f, col.getRGB());
        }

        /*
         * WARNING - void declaration
         */
        SOLID_RECT() {
            void var1_1;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u0016\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType$RECT;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;", "performRendering", "", "x", "", "y", "rad", "col", "Ljava/awt/Color;", "LiKingSense"})
    static final class RECT
    extends ShapeType {
        @Override
        public void performRendering(float x, float y, float rad, @NotNull Color col) {
            Intrinsics.checkParameterIsNotNull((Object)col, (String)"col");
            RenderUtils.drawBorder(x - rad / 2.0f, y - rad / 2.0f, x + rad / 2.0f, y + rad / 2.0f, 0.5f, col.getRGB());
        }

        /*
         * WARNING - void declaration
         */
        RECT() {
            void var1_1;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u0016\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType$SOLID_TRIANGLE;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;", "performRendering", "", "x", "", "y", "rad", "col", "Ljava/awt/Color;", "LiKingSense"})
    static final class SOLID_TRIANGLE
    extends ShapeType {
        @Override
        public void performRendering(float x, float y, float rad, @NotNull Color col) {
            Intrinsics.checkParameterIsNotNull((Object)col, (String)"col");
            RenderUtils.drawTriAngle(x, y, rad, 3.0f, col, true);
        }

        /*
         * WARNING - void declaration
         */
        SOLID_TRIANGLE() {
            void var1_1;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0001\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u0016\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType$TRIANGLE;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;", "performRendering", "", "x", "", "y", "rad", "col", "Ljava/awt/Color;", "LiKingSense"})
    static final class TRIANGLE
    extends ShapeType {
        @Override
        public void performRendering(float x, float y, float rad, @NotNull Color col) {
            Intrinsics.checkParameterIsNotNull((Object)col, (String)"col");
            RenderUtils.drawTriAngle(x, y, rad, 3.0f, col, false);
        }

        /*
         * WARNING - void declaration
         */
        TRIANGLE() {
            void var1_1;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType$Companion;", "", "()V", "getTypeFromName", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/targets/utils/ShapeType;", "name", "", "LiKingSense"})
    public static final class Companion {
        @Nullable
        public final ShapeType getTypeFromName(@NotNull String name) {
            ShapeType shapeType;
            block1: {
                Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
                ShapeType[] shapeTypeArray = ShapeType.values();
                boolean bl = false;
                ShapeType[] shapeTypeArray2 = shapeTypeArray;
                boolean bl2 = false;
                ShapeType[] shapeTypeArray3 = shapeTypeArray2;
                int n = shapeTypeArray3.length;
                for (int i = 0; i < n; ++i) {
                    ShapeType shapeType2;
                    ShapeType it = shapeType2 = shapeTypeArray3[i];
                    boolean bl3 = false;
                    if (!StringsKt.equals((String)it.getTypeName(), (String)name, (boolean)true)) continue;
                    shapeType = shapeType2;
                    break block1;
                }
                shapeType = null;
            }
            return shapeType;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

