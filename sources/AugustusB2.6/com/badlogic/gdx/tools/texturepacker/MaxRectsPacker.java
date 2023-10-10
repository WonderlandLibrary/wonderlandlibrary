// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.tools.texturepacker;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import java.util.Comparator;
import com.badlogic.gdx.utils.Sort;

public final class MaxRectsPacker implements TexturePacker.Packer
{
    final TexturePacker.Settings settings;
    private final int[] methods$4bc364a8;
    private final MaxRects maxRects;
    private final Sort sort;
    private final Comparator<TexturePacker.Rect> rectComparator;
    
    public MaxRectsPacker(final TexturePacker.Settings settings) {
        this.methods$4bc364a8 = FreeRectChoiceHeuristic.values$1f578be9();
        this.maxRects = new MaxRects();
        this.sort = new Sort();
        this.rectComparator = new Comparator<TexturePacker.Rect>() {};
        this.settings = settings;
        if (settings.minWidth > settings.maxWidth) {
            throw new RuntimeException("Page min width cannot be higher than max width.");
        }
        if (settings.minHeight > settings.maxHeight) {
            throw new RuntimeException("Page min height cannot be higher than max height.");
        }
    }
    
    @Override
    public final Array<TexturePacker.Page> pack(final TexturePacker.ProgressListener progress, Array<TexturePacker.Rect> inputRects) {
        final int n = inputRects.size;
        for (int i = 0; i < n; ++i) {
            final TexturePacker.Rect rect;
            final TexturePacker.Rect rect2 = rect = inputRects.get(i);
            rect2.width += this.settings.paddingX;
            final TexturePacker.Rect rect3 = rect;
            rect3.height += this.settings.paddingY;
        }
        final Array<TexturePacker.Page> pages = new Array<TexturePacker.Page>();
        while (inputRects.size > 0) {
            progress.count = n - inputRects.size + 1;
            if (progress.update(progress.count, n)) {
                break;
            }
            final Array<TexturePacker.Rect> array = inputRects;
            inputRects = (Array<TexturePacker.Rect>)this;
            final int paddingX = this.settings.paddingX;
            final int paddingY = ((MaxRectsPacker)inputRects).settings.paddingY;
            float n2 = (float)((MaxRectsPacker)inputRects).settings.maxWidth;
            float n3 = (float)((MaxRectsPacker)inputRects).settings.maxHeight;
            boolean b = false;
            boolean b2 = false;
            if (((MaxRectsPacker)inputRects).settings.edgePadding) {
                n2 -= paddingX << 1;
                n3 -= paddingY << 1;
                b = (paddingX > 0);
                b2 = (paddingY > 0);
            }
            int min = Integer.MAX_VALUE;
            int min2 = Integer.MAX_VALUE;
            for (int j = 0; j < array.size; ++j) {
                final TexturePacker.Rect rect4;
                final int k = (rect4 = array.get(j)).width - paddingX;
                final int l = rect4.height - paddingY;
                min = Math.min(min, k);
                min2 = Math.min(min2, l);
                if (k > n2) {
                    throw new RuntimeException("Image does not fit with max page width " + ((MaxRectsPacker)inputRects).settings.maxWidth + (b ? (" and X edge padding " + paddingX + "*2") : "") + ": " + rect4.name + "[" + k + "," + l + "]");
                }
                if (l > n3) {
                    throw new RuntimeException("Image does not fit in max page height " + ((MaxRectsPacker)inputRects).settings.maxHeight + (b2 ? (" and Y edge padding " + paddingY + "*2") : "") + ": " + rect4.name + "[" + k + "," + l + "]");
                }
            }
            final int max = Math.max(min, ((MaxRectsPacker)inputRects).settings.minWidth);
            final int max2 = Math.max(min2, ((MaxRectsPacker)inputRects).settings.minHeight);
            int n4 = paddingX;
            int n5 = paddingY;
            if (((MaxRectsPacker)inputRects).settings.edgePadding) {
                n4 -= paddingX << 1;
                n5 -= paddingY << 1;
            }
            if (!((MaxRectsPacker)inputRects).settings.silent) {
                System.out.print("Packing");
            }
            TexturePacker.Page result2 = null;
            final BinarySearch binarySearch = new BinarySearch(max, ((MaxRectsPacker)inputRects).settings.maxWidth, 15, ((MaxRectsPacker)inputRects).settings.pot, false);
            final BinarySearch binarySearch2 = new BinarySearch(max2, ((MaxRectsPacker)inputRects).settings.maxHeight, 15, ((MaxRectsPacker)inputRects).settings.pot, false);
            int n6 = binarySearch.reset();
            int n7 = 0;
            int n8 = binarySearch2.reset();
            while (true) {
                TexturePacker.Page best = null;
                while (n6 != -1) {
                    final TexturePacker.Page packAtSize = ((MaxRectsPacker)inputRects).packAtSize(true, n6 + n4, n8 + n5, array);
                    if (!((MaxRectsPacker)inputRects).settings.silent) {
                        if (++n7 % 70 == 0) {
                            System.out.println();
                        }
                        System.out.print(".");
                    }
                    best = getBest(best, packAtSize);
                    n6 = binarySearch.next(packAtSize == null);
                }
                result2 = getBest(result2, best);
                if ((n8 = binarySearch2.next(best == null)) == -1) {
                    break;
                }
                n6 = binarySearch.reset();
            }
            if (!((MaxRectsPacker)inputRects).settings.silent) {
                System.out.println();
            }
            if (result2 == null) {
                result2 = ((MaxRectsPacker)inputRects).packAtSize(false, ((MaxRectsPacker)inputRects).settings.maxWidth + n4, ((MaxRectsPacker)inputRects).settings.maxHeight + n5, array);
            }
            ((MaxRectsPacker)inputRects).sort.sort(result2.outputRects, ((MaxRectsPacker)inputRects).rectComparator);
            final TexturePacker.Page page = result2;
            page.width -= paddingX;
            final TexturePacker.Page page2 = result2;
            page2.height -= paddingY;
            final TexturePacker.Page result = result2;
            pages.add(result);
            inputRects = result.remainingRects;
        }
        return pages;
    }
    
    private TexturePacker.Page packAtSize(final boolean fully, final int width, final int height, final Array<TexturePacker.Rect> inputRects) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          bestResult
        //     3: iconst_0       
        //     4: istore          i
        //     6: aload_0         /* this */
        //     7: getfield        com/badlogic/gdx/tools/texturepacker/MaxRectsPacker.methods$4bc364a8:[I
        //    10: arraylength    
        //    11: istore          n
        //    13: iload           i
        //    15: iload           n
        //    17: if_icmpge       91
        //    20: aload_0         /* this */
        //    21: getfield        com/badlogic/gdx/tools/texturepacker/MaxRectsPacker.maxRects:Lcom/badlogic/gdx/tools/texturepacker/MaxRectsPacker$MaxRects;
        //    24: iload_2         /* width */
        //    25: iload_3         /* height */
        //    26: invokevirtual   com/badlogic/gdx/tools/texturepacker/MaxRectsPacker$MaxRects.init:(II)V
        //    29: aload_0         /* this */
        //    30: getfield        com/badlogic/gdx/tools/texturepacker/MaxRectsPacker.maxRects:Lcom/badlogic/gdx/tools/texturepacker/MaxRectsPacker$MaxRects;
        //    33: aload           inputRects
        //    35: aload_0         /* this */
        //    36: getfield        com/badlogic/gdx/tools/texturepacker/MaxRectsPacker.methods$4bc364a8:[I
        //    39: iload           i
        //    41: iaload         
        //    42: invokevirtual   com/badlogic/gdx/tools/texturepacker/MaxRectsPacker$MaxRects.pack$2cb3cad9:(Lcom/badlogic/gdx/utils/Array;I)Lcom/badlogic/gdx/tools/texturepacker/TexturePacker$Page;
        //    45: astore          8
        //    47: goto            50
        //    50: iload_1         /* fully */
        //    51: ifeq            65
        //    54: aload           result
        //    56: getfield        com/badlogic/gdx/tools/texturepacker/TexturePacker$Page.remainingRects:Lcom/badlogic/gdx/utils/Array;
        //    59: getfield        com/badlogic/gdx/utils/Array.size:I
        //    62: ifgt            85
        //    65: aload           result
        //    67: getfield        com/badlogic/gdx/tools/texturepacker/TexturePacker$Page.outputRects:Lcom/badlogic/gdx/utils/Array;
        //    70: getfield        com/badlogic/gdx/utils/Array.size:I
        //    73: ifeq            85
        //    76: aload           bestResult
        //    78: aload           result
        //    80: invokestatic    com/badlogic/gdx/tools/texturepacker/MaxRectsPacker.getBest:(Lcom/badlogic/gdx/tools/texturepacker/TexturePacker$Page;Lcom/badlogic/gdx/tools/texturepacker/TexturePacker$Page;)Lcom/badlogic/gdx/tools/texturepacker/TexturePacker$Page;
        //    83: astore          bestResult
        //    85: iinc            i, 1
        //    88: goto            13
        //    91: aload           bestResult
        //    93: areturn        
        //    Signature:
        //  (ZIILcom/badlogic/gdx/utils/Array<Lcom/badlogic/gdx/tools/texturepacker/TexturePacker$Rect;>;)Lcom/badlogic/gdx/tools/texturepacker/TexturePacker$Page;
        //    StackMapTable: 00 05 FE 00 0D 07 00 18 01 01 FC 00 24 07 00 18 0E FA 00 13 FF 00 05 00 06 00 00 00 00 00 07 00 18 00 00
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
    
    private static TexturePacker.Page getBest(final TexturePacker.Page result1, final TexturePacker.Page result2) {
        if (result1 == null) {
            return result2;
        }
        if (result2 == null) {
            return result1;
        }
        if (result1.occupancy > result2.occupancy) {
            return result1;
        }
        return result2;
    }
    
    static final class BinarySearch
    {
        private boolean pot;
        private boolean mod4;
        private int min;
        private int max;
        private int fuzziness;
        private int low;
        private int high;
        private int current;
        
        public BinarySearch(final int min, final int max, final int fuzziness, final boolean pot, final boolean mod4) {
            if (pot) {
                this.min = (int)(Math.log(MathUtils.nextPowerOfTwo(min)) / Math.log(2.0));
                this.max = (int)(Math.log(MathUtils.nextPowerOfTwo(max)) / Math.log(2.0));
            }
            else if (mod4) {
                this.min = ((min % 4 == 0) ? min : (min + 4 - min % 4));
                this.max = ((max % 4 == 0) ? max : (max + 4 - max % 4));
            }
            else {
                this.min = min;
                this.max = max;
            }
            this.fuzziness = (pot ? 0 : fuzziness);
            this.pot = pot;
            this.mod4 = mod4;
        }
        
        public final int reset() {
            this.low = this.min;
            this.high = this.max;
            this.current = this.low + this.high >>> 1;
            if (this.pot) {
                return (int)Math.pow(2.0, this.current);
            }
            if (!this.mod4) {
                return this.current;
            }
            if (this.current % 4 == 0) {
                return this.current;
            }
            return this.current + 4 - this.current % 4;
        }
        
        public final int next(final boolean result) {
            if (this.low >= this.high) {
                return -1;
            }
            if (result) {
                this.low = this.current + 1;
            }
            else {
                this.high = this.current - 1;
            }
            this.current = this.low + this.high >>> 1;
            if (Math.abs(this.low - this.high) < this.fuzziness) {
                return -1;
            }
            if (this.pot) {
                return (int)Math.pow(2.0, this.current);
            }
            if (!this.mod4) {
                return this.current;
            }
            if (this.current % 4 == 0) {
                return this.current;
            }
            return this.current + 4 - this.current % 4;
        }
    }
    
    final class MaxRects
    {
        private int binWidth;
        private int binHeight;
        private final Array<TexturePacker.Rect> usedRectangles;
        private final Array<TexturePacker.Rect> freeRectangles;
        
        MaxRects() {
            this.usedRectangles = new Array<TexturePacker.Rect>();
            this.freeRectangles = new Array<TexturePacker.Rect>();
        }
        
        public final void init(final int width, final int height) {
            this.binWidth = width;
            this.binHeight = height;
            this.usedRectangles.clear();
            this.freeRectangles.clear();
            final TexturePacker.Rect n;
            (n = new TexturePacker.Rect()).x = 0;
            n.y = 0;
            n.width = width;
            n.height = height;
            this.freeRectangles.add(n);
        }
        
        public final TexturePacker.Page pack$2cb3cad9(Array<TexturePacker.Rect> rects, final int method) {
            rects = new Array<TexturePacker.Rect>(rects);
            while (rects.size > 0) {
                int bestRectIndex = -1;
                final TexturePacker.Rect bestNode;
                (bestNode = new TexturePacker.Rect()).score1 = Integer.MAX_VALUE;
                bestNode.score2 = Integer.MAX_VALUE;
                for (int i = 0; i < rects.size; ++i) {
                    final TexturePacker.Rect newNode;
                    if ((newNode = this.scoreRect$7e02e2be(rects.get(i), method)).score1 < bestNode.score1 || (newNode.score1 == bestNode.score1 && newNode.score2 < bestNode.score2)) {
                        bestNode.set(rects.get(i));
                        bestNode.score1 = newNode.score1;
                        bestNode.score2 = newNode.score2;
                        bestNode.x = newNode.x;
                        bestNode.y = newNode.y;
                        bestNode.width = newNode.width;
                        bestNode.height = newNode.height;
                        bestNode.rotated = newNode.rotated;
                        bestRectIndex = i;
                    }
                }
                if (bestRectIndex == -1) {
                    break;
                }
                final TexturePacker.Rect rect = bestNode;
                for (int size = this.freeRectangles.size, j = 0; j < size; ++j) {
                    if (this.splitFreeNode(this.freeRectangles.get(j), rect)) {
                        this.freeRectangles.removeIndex(j);
                        --j;
                        --size;
                    }
                }
                this.pruneFreeList();
                this.usedRectangles.add(rect);
                rects.removeIndex(bestRectIndex);
            }
            int max = 0;
            int max2 = 0;
            for (int k = 0; k < this.usedRectangles.size; ++k) {
                final TexturePacker.Rect rect2 = this.usedRectangles.get(k);
                max = Math.max(max, rect2.x + rect2.width);
                max2 = Math.max(max2, rect2.y + rect2.height);
            }
            final TexturePacker.Page page;
            (page = new TexturePacker.Page()).outputRects = new Array<TexturePacker.Rect>(this.usedRectangles);
            final TexturePacker.Page page2 = page;
            int n = 0;
            for (int l = 0; l < this.usedRectangles.size; ++l) {
                n += this.usedRectangles.get(l).width * this.usedRectangles.get(l).height;
            }
            page2.occupancy = n / (float)(this.binWidth * this.binHeight);
            page.width = max;
            page.height = max2;
            final TexturePacker.Page result;
            (result = page).remainingRects = rects;
            return result;
        }
        
        private TexturePacker.Rect scoreRect$7e02e2be(final TexturePacker.Rect rect, final int method) {
            final int width = rect.width;
            final int height;
            final int rotatedWidth = (height = rect.height) - MaxRectsPacker.this.settings.paddingY + MaxRectsPacker.this.settings.paddingX;
            final int rotatedHeight = width - MaxRectsPacker.this.settings.paddingX + MaxRectsPacker.this.settings.paddingY;
            TexturePacker.Rect newNode = null;
            switch (MaxRectsPacker$4.$SwitchMap$com$badlogic$gdx$tools$texturepacker$MaxRectsPacker$FreeRectChoiceHeuristic[method - 1]) {
                case 1: {
                    newNode = this.findPositionForNewNodeBestShortSideFit(width, height, rotatedWidth, rotatedHeight, false);
                    break;
                }
                case 2: {
                    newNode = this.findPositionForNewNodeBottomLeft(width, height, rotatedWidth, rotatedHeight, false);
                    break;
                }
                case 3: {
                    final TexturePacker.Rect rect2 = newNode = this.findPositionForNewNodeContactPoint(width, height, rotatedWidth, rotatedHeight, (boolean)(0 != 0));
                    rect2.score1 = -newNode.score1;
                    break;
                }
                case 4: {
                    newNode = this.findPositionForNewNodeBestLongSideFit(width, height, rotatedWidth, rotatedHeight, false);
                    break;
                }
                case 5: {
                    newNode = this.findPositionForNewNodeBestAreaFit(width, height, rotatedWidth, rotatedHeight, false);
                    break;
                }
            }
            if (newNode.height == 0) {
                newNode.score1 = Integer.MAX_VALUE;
                newNode.score2 = Integer.MAX_VALUE;
            }
            return newNode;
        }
        
        private TexturePacker.Rect findPositionForNewNodeBottomLeft(final int width, final int height, final int rotatedWidth, final int rotatedHeight, final boolean rotate) {
            final TexturePacker.Rect bestNode;
            (bestNode = new TexturePacker.Rect()).score1 = Integer.MAX_VALUE;
            for (int i = 0; i < this.freeRectangles.size; ++i) {
                int topSideY;
                if (this.freeRectangles.get(i).width >= width && this.freeRectangles.get(i).height >= height && ((topSideY = this.freeRectangles.get(i).y + height) < bestNode.score1 || (topSideY == bestNode.score1 && this.freeRectangles.get(i).x < bestNode.score2))) {
                    bestNode.x = this.freeRectangles.get(i).x;
                    bestNode.y = this.freeRectangles.get(i).y;
                    bestNode.width = width;
                    bestNode.height = height;
                    bestNode.score1 = topSideY;
                    bestNode.score2 = this.freeRectangles.get(i).x;
                    bestNode.rotated = false;
                }
                if (rotate && this.freeRectangles.get(i).width >= rotatedWidth && this.freeRectangles.get(i).height >= rotatedHeight && ((topSideY = this.freeRectangles.get(i).y + rotatedHeight) < bestNode.score1 || (topSideY == bestNode.score1 && this.freeRectangles.get(i).x < bestNode.score2))) {
                    bestNode.x = this.freeRectangles.get(i).x;
                    bestNode.y = this.freeRectangles.get(i).y;
                    bestNode.width = rotatedWidth;
                    bestNode.height = rotatedHeight;
                    bestNode.score1 = topSideY;
                    bestNode.score2 = this.freeRectangles.get(i).x;
                    bestNode.rotated = true;
                }
            }
            return bestNode;
        }
        
        private TexturePacker.Rect findPositionForNewNodeBestShortSideFit(final int width, final int height, final int rotatedWidth, final int rotatedHeight, final boolean rotate) {
            final TexturePacker.Rect bestNode;
            (bestNode = new TexturePacker.Rect()).score1 = Integer.MAX_VALUE;
            for (int i = 0; i < this.freeRectangles.size; ++i) {
                if (this.freeRectangles.get(i).width >= width && this.freeRectangles.get(i).height >= height) {
                    final int leftoverHoriz = Math.abs(this.freeRectangles.get(i).width - width);
                    final int leftoverVert = Math.abs(this.freeRectangles.get(i).height - height);
                    final int shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                    final int longSideFit = Math.max(leftoverHoriz, leftoverVert);
                    if (shortSideFit < bestNode.score1 || (shortSideFit == bestNode.score1 && longSideFit < bestNode.score2)) {
                        bestNode.x = this.freeRectangles.get(i).x;
                        bestNode.y = this.freeRectangles.get(i).y;
                        bestNode.width = width;
                        bestNode.height = height;
                        bestNode.score1 = shortSideFit;
                        bestNode.score2 = longSideFit;
                        bestNode.rotated = false;
                    }
                }
                if (rotate && this.freeRectangles.get(i).width >= rotatedWidth && this.freeRectangles.get(i).height >= rotatedHeight) {
                    final int flippedLeftoverHoriz = Math.abs(this.freeRectangles.get(i).width - rotatedWidth);
                    final int flippedLeftoverVert = Math.abs(this.freeRectangles.get(i).height - rotatedHeight);
                    final int flippedShortSideFit = Math.min(flippedLeftoverHoriz, flippedLeftoverVert);
                    final int flippedLongSideFit = Math.max(flippedLeftoverHoriz, flippedLeftoverVert);
                    if (flippedShortSideFit < bestNode.score1 || (flippedShortSideFit == bestNode.score1 && flippedLongSideFit < bestNode.score2)) {
                        bestNode.x = this.freeRectangles.get(i).x;
                        bestNode.y = this.freeRectangles.get(i).y;
                        bestNode.width = rotatedWidth;
                        bestNode.height = rotatedHeight;
                        bestNode.score1 = flippedShortSideFit;
                        bestNode.score2 = flippedLongSideFit;
                        bestNode.rotated = true;
                    }
                }
            }
            return bestNode;
        }
        
        private TexturePacker.Rect findPositionForNewNodeBestLongSideFit(final int width, final int height, final int rotatedWidth, final int rotatedHeight, final boolean rotate) {
            final TexturePacker.Rect bestNode;
            (bestNode = new TexturePacker.Rect()).score2 = Integer.MAX_VALUE;
            for (int i = 0; i < this.freeRectangles.size; ++i) {
                if (this.freeRectangles.get(i).width >= width && this.freeRectangles.get(i).height >= height) {
                    final int leftoverHoriz = Math.abs(this.freeRectangles.get(i).width - width);
                    final int leftoverVert = Math.abs(this.freeRectangles.get(i).height - height);
                    final int shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                    final int longSideFit;
                    if ((longSideFit = Math.max(leftoverHoriz, leftoverVert)) < bestNode.score2 || (longSideFit == bestNode.score2 && shortSideFit < bestNode.score1)) {
                        bestNode.x = this.freeRectangles.get(i).x;
                        bestNode.y = this.freeRectangles.get(i).y;
                        bestNode.width = width;
                        bestNode.height = height;
                        bestNode.score1 = shortSideFit;
                        bestNode.score2 = longSideFit;
                        bestNode.rotated = false;
                    }
                }
                if (rotate && this.freeRectangles.get(i).width >= rotatedWidth && this.freeRectangles.get(i).height >= rotatedHeight) {
                    final int leftoverHoriz = Math.abs(this.freeRectangles.get(i).width - rotatedWidth);
                    final int leftoverVert = Math.abs(this.freeRectangles.get(i).height - rotatedHeight);
                    final int shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                    final int longSideFit;
                    if ((longSideFit = Math.max(leftoverHoriz, leftoverVert)) < bestNode.score2 || (longSideFit == bestNode.score2 && shortSideFit < bestNode.score1)) {
                        bestNode.x = this.freeRectangles.get(i).x;
                        bestNode.y = this.freeRectangles.get(i).y;
                        bestNode.width = rotatedWidth;
                        bestNode.height = rotatedHeight;
                        bestNode.score1 = shortSideFit;
                        bestNode.score2 = longSideFit;
                        bestNode.rotated = true;
                    }
                }
            }
            return bestNode;
        }
        
        private TexturePacker.Rect findPositionForNewNodeBestAreaFit(final int width, final int height, final int rotatedWidth, final int rotatedHeight, final boolean rotate) {
            final TexturePacker.Rect bestNode;
            (bestNode = new TexturePacker.Rect()).score1 = Integer.MAX_VALUE;
            for (int i = 0; i < this.freeRectangles.size; ++i) {
                final int areaFit = this.freeRectangles.get(i).width * this.freeRectangles.get(i).height - width * height;
                if (this.freeRectangles.get(i).width >= width && this.freeRectangles.get(i).height >= height) {
                    final int leftoverHoriz = Math.abs(this.freeRectangles.get(i).width - width);
                    final int leftoverVert = Math.abs(this.freeRectangles.get(i).height - height);
                    final int shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                    if (areaFit < bestNode.score1 || (areaFit == bestNode.score1 && shortSideFit < bestNode.score2)) {
                        bestNode.x = this.freeRectangles.get(i).x;
                        bestNode.y = this.freeRectangles.get(i).y;
                        bestNode.width = width;
                        bestNode.height = height;
                        bestNode.score2 = shortSideFit;
                        bestNode.score1 = areaFit;
                        bestNode.rotated = false;
                    }
                }
                if (rotate && this.freeRectangles.get(i).width >= rotatedWidth && this.freeRectangles.get(i).height >= rotatedHeight) {
                    final int leftoverHoriz = Math.abs(this.freeRectangles.get(i).width - rotatedWidth);
                    final int leftoverVert = Math.abs(this.freeRectangles.get(i).height - rotatedHeight);
                    final int shortSideFit = Math.min(leftoverHoriz, leftoverVert);
                    if (areaFit < bestNode.score1 || (areaFit == bestNode.score1 && shortSideFit < bestNode.score2)) {
                        bestNode.x = this.freeRectangles.get(i).x;
                        bestNode.y = this.freeRectangles.get(i).y;
                        bestNode.width = rotatedWidth;
                        bestNode.height = rotatedHeight;
                        bestNode.score2 = shortSideFit;
                        bestNode.score1 = areaFit;
                        bestNode.rotated = true;
                    }
                }
            }
            return bestNode;
        }
        
        private static int commonIntervalLength(final int i1start, final int i1end, final int i2start, final int i2end) {
            if (i1end < i2start || i2end < i1start) {
                return 0;
            }
            return Math.min(i1end, i2end) - Math.max(i1start, i2start);
        }
        
        private int contactPointScoreNode(final int x, final int y, final int width, final int height) {
            int score = 0;
            if (x == 0 || x + width == this.binWidth) {
                score = height + 0;
            }
            if (y == 0 || y + height == this.binHeight) {
                score += width;
            }
            final Array<TexturePacker.Rect> usedRectangles = this.usedRectangles;
            for (int i = 0, n = usedRectangles.size; i < n; ++i) {
                final TexturePacker.Rect rect;
                if ((rect = usedRectangles.get(i)).x == x + width || rect.x + rect.width == x) {
                    score += commonIntervalLength(rect.y, rect.y + rect.height, y, y + height);
                }
                if (rect.y == y + height || rect.y + rect.height == y) {
                    score += commonIntervalLength(rect.x, rect.x + rect.width, x, x + width);
                }
            }
            return score;
        }
        
        private TexturePacker.Rect findPositionForNewNodeContactPoint(final int width, final int height, final int rotatedWidth, final int rotatedHeight, final boolean rotate) {
            final TexturePacker.Rect bestNode;
            (bestNode = new TexturePacker.Rect()).score1 = -1;
            final Array<TexturePacker.Rect> freeRectangles = this.freeRectangles;
            for (int i = 0, n = freeRectangles.size; i < n; ++i) {
                final TexturePacker.Rect free;
                int score;
                if ((free = freeRectangles.get(i)).width >= width && free.height >= height && (score = this.contactPointScoreNode(free.x, free.y, width, height)) > bestNode.score1) {
                    bestNode.x = free.x;
                    bestNode.y = free.y;
                    bestNode.width = width;
                    bestNode.height = height;
                    bestNode.score1 = score;
                    bestNode.rotated = false;
                }
                if (rotate && free.width >= rotatedWidth && free.height >= rotatedHeight && (score = this.contactPointScoreNode(free.x, free.y, rotatedWidth, rotatedHeight)) > bestNode.score1) {
                    bestNode.x = free.x;
                    bestNode.y = free.y;
                    bestNode.width = rotatedWidth;
                    bestNode.height = rotatedHeight;
                    bestNode.score1 = score;
                    bestNode.rotated = true;
                }
            }
            return bestNode;
        }
        
        private boolean splitFreeNode(final TexturePacker.Rect freeNode, final TexturePacker.Rect usedNode) {
            if (usedNode.x >= freeNode.x + freeNode.width || usedNode.x + usedNode.width <= freeNode.x || usedNode.y >= freeNode.y + freeNode.height || usedNode.y + usedNode.height <= freeNode.y) {
                return false;
            }
            if (usedNode.x < freeNode.x + freeNode.width && usedNode.x + usedNode.width > freeNode.x) {
                if (usedNode.y > freeNode.y && usedNode.y < freeNode.y + freeNode.height) {
                    final TexturePacker.Rect newNode;
                    final TexturePacker.Rect rect = newNode = new TexturePacker.Rect(freeNode);
                    rect.height = usedNode.y - newNode.y;
                    this.freeRectangles.add(newNode);
                }
                if (usedNode.y + usedNode.height < freeNode.y + freeNode.height) {
                    final TexturePacker.Rect newNode;
                    (newNode = new TexturePacker.Rect(freeNode)).y = usedNode.y + usedNode.height;
                    newNode.height = freeNode.y + freeNode.height - (usedNode.y + usedNode.height);
                    this.freeRectangles.add(newNode);
                }
            }
            if (usedNode.y < freeNode.y + freeNode.height && usedNode.y + usedNode.height > freeNode.y) {
                if (usedNode.x > freeNode.x && usedNode.x < freeNode.x + freeNode.width) {
                    final TexturePacker.Rect newNode;
                    final TexturePacker.Rect rect2 = newNode = new TexturePacker.Rect(freeNode);
                    rect2.width = usedNode.x - newNode.x;
                    this.freeRectangles.add(newNode);
                }
                if (usedNode.x + usedNode.width < freeNode.x + freeNode.width) {
                    final TexturePacker.Rect newNode;
                    (newNode = new TexturePacker.Rect(freeNode)).x = usedNode.x + usedNode.width;
                    newNode.width = freeNode.x + freeNode.width - (usedNode.x + usedNode.width);
                    this.freeRectangles.add(newNode);
                }
            }
            return true;
        }
        
        private void pruneFreeList() {
            final Array<TexturePacker.Rect> freeRectangles = this.freeRectangles;
            for (int i = 0, n = freeRectangles.size; i < n; ++i) {
                for (int j = i + 1; j < n; ++j) {
                    final TexturePacker.Rect rect1 = freeRectangles.get(i);
                    final TexturePacker.Rect rect2 = freeRectangles.get(j);
                    if (isContainedIn(rect1, rect2)) {
                        freeRectangles.removeIndex(i);
                        --i;
                        --n;
                        break;
                    }
                    if (isContainedIn(rect2, rect1)) {
                        freeRectangles.removeIndex(j);
                        --j;
                        --n;
                    }
                }
            }
        }
        
        private static boolean isContainedIn(final TexturePacker.Rect a, final TexturePacker.Rect b) {
            return a.x >= b.x && a.y >= b.y && a.x + a.width <= b.x + b.width && a.y + a.height <= b.y + b.height;
        }
    }
    
    public enum FreeRectChoiceHeuristic
    {
        public static final int BestShortSideFit$684b6123;
        public static final int BestLongSideFit$684b6123;
        public static final int BestAreaFit$684b6123;
        public static final int BottomLeftRule$684b6123;
        public static final int ContactPointRule$684b6123;
        private static final /* synthetic */ int[] $VALUES$4bc364a8;
        
        public static int[] values$1f578be9() {
            return FreeRectChoiceHeuristic.$VALUES$4bc364a8.clone();
        }
        
        static {
            BestShortSideFit$684b6123 = 1;
            BestLongSideFit$684b6123 = 2;
            BestAreaFit$684b6123 = 3;
            BottomLeftRule$684b6123 = 4;
            ContactPointRule$684b6123 = 5;
            $VALUES$4bc364a8 = new int[] { FreeRectChoiceHeuristic.BestShortSideFit$684b6123, FreeRectChoiceHeuristic.BestLongSideFit$684b6123, FreeRectChoiceHeuristic.BestAreaFit$684b6123, FreeRectChoiceHeuristic.BottomLeftRule$684b6123, FreeRectChoiceHeuristic.ContactPointRule$684b6123 };
        }
    }
}
