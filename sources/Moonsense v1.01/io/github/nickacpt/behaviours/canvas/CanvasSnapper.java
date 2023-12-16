// 
// Decompiled by Procyon v0.5.36
// 

package io.github.nickacpt.behaviours.canvas;

import kotlin.reflect.KMutableProperty1;
import java.util.Iterator;
import java.util.List;
import io.github.nickacpt.behaviours.canvas.model.geometry.line.CanvasLineSide;
import io.github.nickacpt.behaviours.canvas.model.geometry.line.CanvasLine;
import java.util.EnumSet;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasPoint;
import kotlin.Pair;
import io.github.nickacpt.behaviours.canvas.model.geometry.CanvasRectangle;
import io.github.nickacpt.behaviours.canvas.model.CanvasAction;
import java.util.LinkedHashMap;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import java.util.Set;
import io.github.nickacpt.behaviours.canvas.model.geometry.line.CanvasLineDirection;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 7, 1 }, k = 1, xi = 48, d1 = { "\u0000V\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\"\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010%\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u00020\u0003B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005¢\u0006\u0002\u0010\u0006J,\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u001c\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u000b0\u00162\u0006\u0010\u0011\u001a\u00020\tH\u0002J\u001a\u0010\u0017\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\bH\u0002J\u0010\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u001bJ\u001e\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u0010R\"\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000b0\rX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0004" }, d2 = { "Lio/github/nickacpt/behaviours/canvas/CanvasSnapper;", "ElementType", "ColorType", "", "canvas", "Lio/github/nickacpt/behaviours/canvas/Canvas;", "(Lio/github/nickacpt/behaviours/canvas/Canvas;)V", "cachedSnapLines", "", "Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineDirection;", "", "", "mouseOffsetSnapState", "", "computeLine", "Lkotlin/Pair;", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasPoint;", "direction", "value", "canvasRect", "Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;", "computePosProperty", "Lkotlin/reflect/KMutableProperty1;", "computeSnapLines", "notifyStateChange", "", "newAction", "Lio/github/nickacpt/behaviours/canvas/model/CanvasAction;", "snap", "mousePosition", "elementRect", "outPoint" })
public final class CanvasSnapper<ElementType, ColorType>
{
    @NotNull
    private final Canvas<ElementType, ColorType> canvas;
    @Nullable
    private Map<CanvasLineDirection, ? extends Set<Float>> cachedSnapLines;
    @NotNull
    private final Map<CanvasLineDirection, Float> mouseOffsetSnapState;
    
    public CanvasSnapper(@NotNull final Canvas<ElementType, ColorType> canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        this.canvas = canvas;
        this.mouseOffsetSnapState = new LinkedHashMap<CanvasLineDirection, Float>();
    }
    
    private final Map<CanvasLineDirection, Set<Float>> computeSnapLines() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/github/nickacpt/behaviours/canvas/CanvasSnapper.canvas:Lio/github/nickacpt/behaviours/canvas/Canvas;
        //     4: invokevirtual   io/github/nickacpt/behaviours/canvas/Canvas.getAbstraction$canvas:()Lio/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction;
        //     7: invokeinterface io/github/nickacpt/behaviours/canvas/abstractions/CanvasAbstraction.getElements:()Ljava/util/Collection;
        //    12: checkcast       Ljava/lang/Iterable;
        //    15: invokestatic    kotlin/collections/CollectionsKt.asSequence:(Ljava/lang/Iterable;)Lkotlin/sequences/Sequence;
        //    18: new             Lio/github/nickacpt/behaviours/canvas/CanvasSnapper$computeSnapLines$rectangles$1;
        //    21: dup            
        //    22: aload_0         /* this */
        //    23: invokespecial   io/github/nickacpt/behaviours/canvas/CanvasSnapper$computeSnapLines$rectangles$1.<init>:(Lio/github/nickacpt/behaviours/canvas/CanvasSnapper;)V
        //    26: checkcast       Lkotlin/jvm/functions/Function1;
        //    29: invokestatic    kotlin/sequences/SequencesKt.filter:(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;
        //    32: new             Lio/github/nickacpt/behaviours/canvas/CanvasSnapper$computeSnapLines$rectangles$2;
        //    35: dup            
        //    36: aload_0         /* this */
        //    37: invokespecial   io/github/nickacpt/behaviours/canvas/CanvasSnapper$computeSnapLines$rectangles$2.<init>:(Lio/github/nickacpt/behaviours/canvas/CanvasSnapper;)V
        //    40: checkcast       Lkotlin/jvm/functions/Function1;
        //    43: invokestatic    kotlin/sequences/SequencesKt.map:(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;
        //    46: invokestatic    kotlin/sequences/SequencesKt.toMutableList:(Lkotlin/sequences/Sequence;)Ljava/util/List;
        //    49: astore_1        /* rectangles */
        //    50: aload_1         /* rectangles */
        //    51: aload_0         /* this */
        //    52: getfield        io/github/nickacpt/behaviours/canvas/CanvasSnapper.canvas:Lio/github/nickacpt/behaviours/canvas/Canvas;
        //    55: invokevirtual   io/github/nickacpt/behaviours/canvas/Canvas.getSafeZoneRectangle$canvas:()Lio/github/nickacpt/behaviours/canvas/model/geometry/CanvasRectangle;
        //    58: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    63: pop            
        //    64: aload_1         /* rectangles */
        //    65: checkcast       Ljava/lang/Iterable;
        //    68: invokestatic    kotlin/collections/CollectionsKt.asSequence:(Ljava/lang/Iterable;)Lkotlin/sequences/Sequence;
        //    71: new             Lio/github/nickacpt/behaviours/canvas/CanvasSnapper$computeSnapLines$1;
        //    74: dup            
        //    75: aload_0         /* this */
        //    76: invokespecial   io/github/nickacpt/behaviours/canvas/CanvasSnapper$computeSnapLines$1.<init>:(Lio/github/nickacpt/behaviours/canvas/CanvasSnapper;)V
        //    79: checkcast       Lkotlin/jvm/functions/Function1;
        //    82: invokestatic    kotlin/sequences/SequencesKt.flatMapIterable:(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;
        //    85: astore_2       
        //    86: nop            
        //    87: iconst_0       
        //    88: istore_3        /* $i$f$groupBy */
        //    89: aload_2         /* $this$groupBy$iv */
        //    90: astore          4
        //    92: new             Ljava/util/LinkedHashMap;
        //    95: dup            
        //    96: invokespecial   java/util/LinkedHashMap.<init>:()V
        //    99: checkcast       Ljava/util/Map;
        //   102: astore          destination$iv$iv
        //   104: iconst_0       
        //   105: istore          $i$f$groupByTo
        //   107: aload           $this$groupByTo$iv$iv
        //   109: invokeinterface kotlin/sequences/Sequence.iterator:()Ljava/util/Iterator;
        //   114: astore          7
        //   116: aload           7
        //   118: invokeinterface java/util/Iterator.hasNext:()Z
        //   123: ifeq            228
        //   126: aload           7
        //   128: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   133: astore          element$iv$iv
        //   135: aload           element$iv$iv
        //   137: checkcast       Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLine;
        //   140: astore          it
        //   142: iconst_0       
        //   143: istore          $i$a$-groupBy-CanvasSnapper$computeSnapLines$2
        //   145: aload           it
        //   147: invokevirtual   io/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLine.getDirection:()Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineDirection;
        //   150: astore          key$iv$iv
        //   152: aload           destination$iv$iv
        //   154: astore          $this$getOrPut$iv$iv$iv
        //   156: iconst_0       
        //   157: istore          $i$f$getOrPut
        //   159: aload           $this$getOrPut$iv$iv$iv
        //   161: aload           key$iv$iv
        //   163: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   168: astore          value$iv$iv$iv
        //   170: aload           value$iv$iv$iv
        //   172: ifnonnull       207
        //   175: iconst_0       
        //   176: istore          $i$a$-getOrPut-SequencesKt___SequencesKt$groupByTo$list$1$iv$iv
        //   178: new             Ljava/util/ArrayList;
        //   181: dup            
        //   182: invokespecial   java/util/ArrayList.<init>:()V
        //   185: checkcast       Ljava/util/List;
        //   188: astore          answer$iv$iv$iv
        //   190: aload           $this$getOrPut$iv$iv$iv
        //   192: aload           key$iv$iv
        //   194: aload           answer$iv$iv$iv
        //   196: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   201: pop            
        //   202: aload           answer$iv$iv$iv
        //   204: goto            209
        //   207: aload           value$iv$iv$iv
        //   209: nop            
        //   210: checkcast       Ljava/util/List;
        //   213: astore          list$iv$iv
        //   215: aload           list$iv$iv
        //   217: aload           element$iv$iv
        //   219: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   224: pop            
        //   225: goto            116
        //   228: aload           destination$iv$iv
        //   230: nop            
        //   231: astore_2        /* $this$groupBy$iv */
        //   232: nop            
        //   233: iconst_0       
        //   234: istore_3        /* $i$f$mapValues */
        //   235: aload_2         /* $this$mapValues$iv */
        //   236: astore          4
        //   238: new             Ljava/util/LinkedHashMap;
        //   241: dup            
        //   242: aload_2         /* $this$mapValues$iv */
        //   243: invokeinterface java/util/Map.size:()I
        //   248: invokestatic    kotlin/collections/MapsKt.mapCapacity:(I)I
        //   251: invokespecial   java/util/LinkedHashMap.<init>:(I)V
        //   254: checkcast       Ljava/util/Map;
        //   257: astore          destination$iv$iv
        //   259: iconst_0       
        //   260: istore          $i$f$mapValuesTo
        //   262: aload           $this$mapValuesTo$iv$iv
        //   264: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //   269: checkcast       Ljava/lang/Iterable;
        //   272: astore          $this$associateByTo$iv$iv$iv
        //   274: iconst_0       
        //   275: istore          $i$f$associateByTo
        //   277: aload           $this$associateByTo$iv$iv$iv
        //   279: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   284: astore          9
        //   286: aload           9
        //   288: invokeinterface java/util/Iterator.hasNext:()Z
        //   293: ifeq            475
        //   296: aload           9
        //   298: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   303: astore          element$iv$iv$iv
        //   305: aload           destination$iv$iv
        //   307: aload           element$iv$iv$iv
        //   309: checkcast       Ljava/util/Map$Entry;
        //   312: astore          11
        //   314: astore          12
        //   316: iconst_0       
        //   317: istore          $i$a$-associateByTo-MapsKt__MapsKt$mapValuesTo$1$iv$iv
        //   319: aload           it$iv$iv
        //   321: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   326: aload           12
        //   328: swap           
        //   329: aload           element$iv$iv$iv
        //   331: checkcast       Ljava/util/Map$Entry;
        //   334: astore          14
        //   336: astore          27
        //   338: astore          26
        //   340: iconst_0       
        //   341: istore          $i$a$-mapValues-CanvasSnapper$computeSnapLines$3
        //   343: aload           it
        //   345: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   350: checkcast       Ljava/lang/Iterable;
        //   353: astore          $this$map$iv
        //   355: iconst_0       
        //   356: istore          $i$f$map
        //   358: aload           $this$map$iv
        //   360: astore          18
        //   362: new             Ljava/util/ArrayList;
        //   365: dup            
        //   366: aload           $this$map$iv
        //   368: bipush          10
        //   370: invokestatic    kotlin/collections/CollectionsKt.collectionSizeOrDefault:(Ljava/lang/Iterable;I)I
        //   373: invokespecial   java/util/ArrayList.<init>:(I)V
        //   376: checkcast       Ljava/util/Collection;
        //   379: astore          destination$iv$iv
        //   381: iconst_0       
        //   382: istore          $i$f$mapTo
        //   384: aload           $this$mapTo$iv$iv
        //   386: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   391: astore          21
        //   393: aload           21
        //   395: invokeinterface java/util/Iterator.hasNext:()Z
        //   400: ifeq            446
        //   403: aload           21
        //   405: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   410: astore          item$iv$iv
        //   412: aload           destination$iv$iv
        //   414: aload           item$iv$iv
        //   416: checkcast       Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLine;
        //   419: astore          23
        //   421: astore          24
        //   423: iconst_0       
        //   424: istore          $i$a$-map-CanvasSnapper$computeSnapLines$3$1
        //   426: aload           v
        //   428: invokevirtual   io/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLine.getValue:()F
        //   431: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //   434: aload           24
        //   436: swap           
        //   437: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   442: pop            
        //   443: goto            393
        //   446: aload           destination$iv$iv
        //   448: checkcast       Ljava/util/List;
        //   451: nop            
        //   452: checkcast       Ljava/lang/Iterable;
        //   455: invokestatic    kotlin/collections/CollectionsKt.toSet:(Ljava/lang/Iterable;)Ljava/util/Set;
        //   458: astore          28
        //   460: aload           26
        //   462: aload           27
        //   464: aload           28
        //   466: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   471: pop            
        //   472: goto            286
        //   475: aload           destination$iv$iv
        //   477: nop            
        //   478: nop            
        //   479: invokestatic    kotlin/collections/MapsKt.toMap:(Ljava/util/Map;)Ljava/util/Map;
        //   482: areturn        
        //    Signature:
        //  ()Ljava/util/Map<Lio/github/nickacpt/behaviours/canvas/model/geometry/line/CanvasLineDirection;Ljava/util/Set<Ljava/lang/Float;>;>;
        //    StackMapTable: 00 08 FF 00 74 00 08 07 00 02 07 00 56 07 00 62 01 07 00 62 07 00 1C 01 07 00 68 00 00 FF 00 5A 00 0F 07 00 02 07 00 56 07 00 62 01 07 00 62 07 00 1C 01 07 00 68 07 00 05 07 00 72 01 07 00 DF 07 00 1C 01 07 00 05 00 00 41 07 00 05 FF 00 12 00 08 07 00 02 07 00 56 07 00 62 01 07 00 62 07 00 1C 01 07 00 68 00 00 FF 00 39 00 0A 07 00 02 07 00 56 07 00 1C 01 07 00 1C 07 00 1C 01 07 00 33 01 07 00 68 00 00 FF 00 6A 00 1C 07 00 02 07 00 56 07 00 1C 01 07 00 1C 07 00 1C 01 07 00 33 01 07 00 68 07 00 05 07 00 95 07 00 1C 01 07 00 95 01 07 00 33 01 07 00 33 07 00 A2 01 07 00 68 00 00 00 00 07 00 1C 07 00 05 00 00 34 FF 00 1C 00 0A 07 00 02 07 00 56 07 00 1C 01 07 00 1C 07 00 1C 01 07 00 33 01 07 00 68 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final void notifyStateChange(@NotNull final CanvasAction newAction) {
        Intrinsics.checkNotNullParameter(newAction, "newAction");
        if (newAction == CanvasAction.NONE) {
            this.mouseOffsetSnapState.clear();
        }
        this.cachedSnapLines = this.computeSnapLines();
    }
    
    private final Pair<CanvasPoint, CanvasPoint> computeLine(final CanvasLineDirection direction, final float value, final CanvasRectangle canvasRect) {
        final CanvasPoint p1 = (direction == CanvasLineDirection.HORIZONTAL) ? new CanvasPoint(0.0f, value) : new CanvasPoint(value, 0.0f);
        final CanvasPoint p2 = (direction == CanvasLineDirection.HORIZONTAL) ? new CanvasPoint(canvasRect.getWidth(), value) : new CanvasPoint(value, canvasRect.getHeight());
        return new Pair<CanvasPoint, CanvasPoint>(p1, p2);
    }
    
    public final void snap(@NotNull final CanvasPoint mousePosition, @NotNull final CanvasRectangle elementRect, @NotNull final CanvasPoint outPoint) {
        Intrinsics.checkNotNullParameter(mousePosition, "mousePosition");
        Intrinsics.checkNotNullParameter(elementRect, "elementRect");
        Intrinsics.checkNotNullParameter(outPoint, "outPoint");
        final CanvasRectangle canvasRect = this.canvas.getAbstraction$canvas().getCanvasRectangle();
        final List sides = elementRect.sides$canvas(this.canvas.getConfig$canvas().getElementsHaveMiddleSnapLines());
        final EnumSet snappedDirections = EnumSet.noneOf(CanvasLineDirection.class);
        for (final CanvasLine canvasLine : sides) {
            final float sidePos = canvasLine.component1();
            final CanvasLineDirection direction = canvasLine.component2();
            final CanvasLineSide side = canvasLine.component3();
            final KMutableProperty1 posProp = this.computePosProperty(direction);
            final Map<CanvasLineDirection, ? extends Set<Float>> cachedSnapLines = this.cachedSnapLines;
            if (cachedSnapLines != null) {
                final Set set = (Set)cachedSnapLines.get(direction);
                if (set == null) {
                    continue;
                }
                final Set snapLines = set;
                final Iterator iterator2 = snapLines.iterator();
                while (iterator2.hasNext()) {
                    final float line = iterator2.next().floatValue();
                    final float sideDistToSnapLine = Math.abs(sidePos - line);
                    if (sideDistToSnapLine <= this.canvas.getConfig$canvas().getSnapDistance()) {
                        float sideOffset = elementRect.getSideValue$canvas(direction, side) - elementRect.getSideValue$canvas(direction, CanvasLineSide.FIRST);
                        final float n;
                        final float mouseOffsetFromCorner = n = ((Number)posProp.get(mousePosition)).floatValue() - ((Number)posProp.get(elementRect.getTopLeft())).floatValue();
                        final Float n2 = this.mouseOffsetSnapState.get(direction);
                        final float mouseOffsetDistFromLastSnap = n - ((n2 != null) ? n2 : 0.0f);
                        final boolean isUnsnap = Math.abs(mouseOffsetDistFromLastSnap) > this.canvas.getConfig$canvas().getMouseExitSnapDistance();
                        if (isUnsnap) {
                            sideOffset -= mouseOffsetDistFromLastSnap;
                        }
                        final ColorType selectionBackground = this.canvas.getConfig$canvas().getColors().getSelectionBackground();
                        if (selectionBackground != null) {
                            final Object color = selectionBackground;
                            final int n3 = 0;
                            final Pair<CanvasPoint, CanvasPoint> computeLine = this.computeLine(direction, line, canvasRect);
                            final CanvasPoint p1 = computeLine.component1();
                            final CanvasPoint p2 = computeLine.component2();
                            this.canvas.getAbstraction$canvas().drawLine(p1, p2, (ColorType)color, this.canvas.getConfig$canvas().getSnapLineWidth());
                        }
                        if (!snappedDirections.contains(direction)) {
                            if (!isUnsnap) {
                                snappedDirections.add(direction);
                            }
                            posProp.set(outPoint, line - sideOffset);
                            if (!this.mouseOffsetSnapState.containsKey(direction)) {
                                this.mouseOffsetSnapState.put(direction, mouseOffsetFromCorner);
                            }
                            break;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    private final KMutableProperty1<CanvasPoint, Float> computePosProperty(final CanvasLineDirection direction) {
        return (KMutableProperty1<CanvasPoint, Float>)((direction == CanvasLineDirection.HORIZONTAL) ? CanvasSnapper$computePosProperty.CanvasSnapper$computePosProperty$1.INSTANCE : ((KMutableProperty1)CanvasSnapper$computePosProperty.CanvasSnapper$computePosProperty$2.INSTANCE));
    }
}
