// 
// Decompiled by Procyon v0.5.36
// 

package com.maltaisn.msdfgdx.gen;

import java.awt.geom.AffineTransform;
import java.awt.Canvas;
import kotlin.Pair;
import java.awt.FontFormatException;
import java.util.Iterator;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import java.util.Map;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.AwaitKt;
import kotlin.coroutines.Continuation;
import java.io.InputStream;
import java.io.FileInputStream;
import com.badlogic.gdx.tools.hiero.Kerning;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import java.io.File;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.awt.font.FontRenderContext;
import java.awt.FontMetrics;
import java.util.SortedMap;
import java.awt.Font;

public final class BMFont
{
    private final Font font;
    private final SortedMap<Character, FontGlyph> glyphs;
    private final FontMetrics fontMetrics;
    private final FontRenderContext fontRenderContext;
    private TextureAtlas.TextureAtlasData atlasData;
    private final File fontFile;
    private final Parameters params;
    
    public final void generate(final Function2<? super GenerationStep, ? super Float, Unit> progressListener) {
        Intrinsics.checkParameterIsNotNull(progressListener, "progressListener");
        progressListener.invoke(GenerationStep.GLYPH, 0.0f);
        final AtomicInteger atomicInteger = new AtomicInteger();
        final Kerning kerning;
        (kerning = new Kerning()).load(new FileInputStream(this.fontFile), this.params.getFontSize());
        AwaitKt.runBlocking$default(null, (Function2)new BMFont$generateGlyphs.BMFont$generateGlyphs$1(this, kerning.getKernings(), this.params.getDistanceRange() / 2.0f, (Function2)progressListener, atomicInteger, (Continuation)null), 1, null);
        this.pack(progressListener);
        this.generateFontFile(progressListener);
        this.compress(progressListener);
    }
    
    private final void pack(final Function2<? super GenerationStep, ? super Float, Unit> progressListener) {
        final File file = new File(this.params.getOutputDir());
        final TexturePacker.Settings settings = new TexturePacker.Settings();
        final File rootDir = file;
        final TexturePacker.Settings $this$apply;
        ($this$apply = settings).paddingX = this.params.getPadding();
        $this$apply.paddingY = this.params.getPadding();
        $this$apply.maxWidth = this.params.getTextureSize().get(0).intValue();
        $this$apply.maxHeight = this.params.getTextureSize().get(1).intValue();
        $this$apply.alias = false;
        $this$apply.ignoreBlankImages = false;
        $this$apply.silent = true;
        $this$apply.format = (this.params.getHasAlphaChannel() ? Pixmap.Format.RGBA8888 : Pixmap.Format.RGB888);
        final TexturePacker packer = new TexturePacker(rootDir, settings);
        final File atlasFile = new File(this.params.getOutputDir(), FilesKt__FileTreeWalkKt.getNameWithoutExtension(this.fontFile) + ".atlas");
        progressListener.invoke(GenerationStep.PACK, 0.0f);
        packer.setProgressListener((TexturePacker.ProgressListener)new BMFont$pack.BMFont$pack$1((Function2)progressListener));
        final Iterator<Map.Entry<Character, FontGlyph>> iterator = this.glyphs.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Character, FontGlyph> entry;
            final Character char1 = (entry = iterator.next()).getKey();
            final FontGlyph glyph = entry.getValue();
            packer.addImage(glyph.getImage(), String.valueOf((int)char1));
        }
        new File(this.params.getOutputDir(), FilesKt__FileTreeWalkKt.getNameWithoutExtension(this.fontFile) + ".atlas").delete();
        File pageFile;
        for (int pageIndex = 0; (pageFile = this.getTextureAtlasFile(pageIndex)).exists(); ++pageIndex) {
            pageFile.delete();
        }
        packer.pack(new File(this.params.getOutputDir()), FilesKt__FileTreeWalkKt.getNameWithoutExtension(this.fontFile));
        final TextureAtlas.TextureAtlasData atlasData;
        final Iterator<TextureAtlas.TextureAtlasData.Region> iterator2 = (atlasData = new TextureAtlas.TextureAtlasData(new LwjglFileHandle(atlasFile, Files.FileType.Absolute), new LwjglFileHandle("", Files.FileType.Absolute), (boolean)(0 != 0))).getRegions().iterator();
        while (iterator2.hasNext()) {
            final TextureAtlas.TextureAtlasData.Region region;
            final String name = (region = (TextureAtlas.TextureAtlasData.Region)iterator2.next()).name;
            Intrinsics.checkExpressionValueIsNotNull(name, "region.name");
            final char char2 = (char)Integer.parseInt(name);
            final FontGlyph fontGlyph = this.glyphs.get(char2);
            if (fontGlyph == null) {
                continue;
            }
            final FontGlyph glyph2 = fontGlyph;
            fontGlyph.setPage(atlasData.getPages().indexOf(region.page, true));
            glyph2.setX(region.left);
            glyph2.setY(region.top);
        }
        this.atlasData = atlasData;
        atlasFile.delete();
    }
    
    private final void generateFontFile(final Function2<? super GenerationStep, ? super Float, Unit> progressListener) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getstatic       com/maltaisn/msdfgdx/gen/BMFont$GenerationStep.FONT_FILE:Lcom/maltaisn/msdfgdx/gen/BMFont$GenerationStep;
        //     4: fconst_0       
        //     5: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //     8: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    13: pop            
        //    14: new             Ljava/lang/StringBuilder;
        //    17: dup            
        //    18: invokespecial   java/lang/StringBuilder.<init>:()V
        //    21: astore_2        /* bmfont */
        //    22: aload_0         /* this */
        //    23: getfield        com/maltaisn/msdfgdx/gen/BMFont.atlasData:Lcom/badlogic/gdx/graphics/g2d/TextureAtlas$TextureAtlasData;
        //    26: dup            
        //    27: astore          4
        //    29: ifnonnull       52
        //    32: ldc             "Required value was null."
        //    34: astore          6
        //    36: new             Ljava/lang/IllegalStateException;
        //    39: dup            
        //    40: aload           6
        //    42: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    45: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //    48: checkcast       Ljava/lang/Throwable;
        //    51: athrow         
        //    52: aload           4
        //    54: astore_3        /* atlasData */
        //    55: aload_2         /* bmfont */
        //    56: astore          4
        //    58: new             Ljava/lang/StringBuilder;
        //    61: dup            
        //    62: ldc             "info face=\""
        //    64: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    67: aload_0         /* this */
        //    68: getfield        com/maltaisn/msdfgdx/gen/BMFont.font:Ljava/awt/Font;
        //    71: dup            
        //    72: ldc             "font"
        //    74: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    77: invokevirtual   java/awt/Font.getFontName:()Ljava/lang/String;
        //    80: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    83: ldc             "\" size="
        //    85: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    88: aload_0         /* this */
        //    89: getfield        com/maltaisn/msdfgdx/gen/BMFont.params:Lcom/maltaisn/msdfgdx/gen/Parameters;
        //    92: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.getFontSize:()I
        //    95: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    98: bipush          32
        //   100: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   103: ldc             "bold="
        //   105: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   108: aload_0         /* this */
        //   109: getfield        com/maltaisn/msdfgdx/gen/BMFont.font:Ljava/awt/Font;
        //   112: dup            
        //   113: ldc             "font"
        //   115: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   118: invokevirtual   java/awt/Font.isBold:()Z
        //   121: ifeq            128
        //   124: iconst_1       
        //   125: goto            129
        //   128: iconst_0       
        //   129: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   132: ldc             " italic="
        //   134: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   137: aload_0         /* this */
        //   138: getfield        com/maltaisn/msdfgdx/gen/BMFont.font:Ljava/awt/Font;
        //   141: dup            
        //   142: ldc             "font"
        //   144: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   147: invokevirtual   java/awt/Font.isItalic:()Z
        //   150: ifeq            157
        //   153: iconst_1       
        //   154: goto            158
        //   157: iconst_0       
        //   158: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   161: bipush          32
        //   163: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   166: ldc             "charset=\"\" unicode=1 stretchH=100 smooth=1 aa=1 padding=0,0,0,0 spacing=0,0 outline=0"
        //   168: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   171: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   174: astore          5
        //   176: aload           4
        //   178: aload           5
        //   180: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   183: dup            
        //   184: ldc             "append(value)"
        //   186: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   189: invokestatic    kotlin/text/StringsKt___StringsKt.appendln:(Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
        //   192: pop            
        //   193: aload_2         /* bmfont */
        //   194: astore          4
        //   196: new             Ljava/lang/StringBuilder;
        //   199: dup            
        //   200: ldc             "common lineHeight="
        //   202: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   205: aload_0         /* this */
        //   206: getfield        com/maltaisn/msdfgdx/gen/BMFont.fontMetrics:Ljava/awt/FontMetrics;
        //   209: dup            
        //   210: ldc             "fontMetrics"
        //   212: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   215: invokevirtual   java/awt/FontMetrics.getHeight:()I
        //   218: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   221: bipush          32
        //   223: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   226: ldc             "base="
        //   228: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   231: aload_0         /* this */
        //   232: getfield        com/maltaisn/msdfgdx/gen/BMFont.fontMetrics:Ljava/awt/FontMetrics;
        //   235: dup            
        //   236: ldc             "fontMetrics"
        //   238: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   241: invokevirtual   java/awt/FontMetrics.getAscent:()I
        //   244: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   247: ldc             " scaleW="
        //   249: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   252: aload_0         /* this */
        //   253: getfield        com/maltaisn/msdfgdx/gen/BMFont.params:Lcom/maltaisn/msdfgdx/gen/Parameters;
        //   256: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.getTextureSize:()Ljava/util/List;
        //   259: iconst_0       
        //   260: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   265: checkcast       Ljava/lang/Number;
        //   268: invokevirtual   java/lang/Number.intValue:()I
        //   271: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   274: bipush          32
        //   276: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   279: ldc             "scaleH="
        //   281: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   284: aload_0         /* this */
        //   285: getfield        com/maltaisn/msdfgdx/gen/BMFont.params:Lcom/maltaisn/msdfgdx/gen/Parameters;
        //   288: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.getTextureSize:()Ljava/util/List;
        //   291: iconst_1       
        //   292: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   297: checkcast       Ljava/lang/Number;
        //   300: invokevirtual   java/lang/Number.intValue:()I
        //   303: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   306: ldc             " pages="
        //   308: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   311: aload_3         /* atlasData */
        //   312: invokevirtual   com/badlogic/gdx/graphics/g2d/TextureAtlas$TextureAtlasData.getPages:()Lcom/badlogic/gdx/utils/Array;
        //   315: getfield        com/badlogic/gdx/utils/Array.size:I
        //   318: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   321: bipush          32
        //   323: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   326: ldc             "packed=0 alphaChnl=0 redChnl=0 greenChnl=0 blueChnl=0 distanceRange="
        //   328: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   331: aload_0         /* this */
        //   332: getfield        com/maltaisn/msdfgdx/gen/BMFont.params:Lcom/maltaisn/msdfgdx/gen/Parameters;
        //   335: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.getDistanceRange:()I
        //   338: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   341: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   344: astore          5
        //   346: aload           4
        //   348: aload           5
        //   350: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   353: dup            
        //   354: ldc             "append(value)"
        //   356: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   359: invokestatic    kotlin/text/StringsKt___StringsKt.appendln:(Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
        //   362: pop            
        //   363: iconst_0       
        //   364: istore          4
        //   366: aload_3         /* atlasData */
        //   367: invokevirtual   com/badlogic/gdx/graphics/g2d/TextureAtlas$TextureAtlasData.getPages:()Lcom/badlogic/gdx/utils/Array;
        //   370: getfield        com/badlogic/gdx/utils/Array.size:I
        //   373: istore          5
        //   375: iload           4
        //   377: iload           5
        //   379: if_icmpge       447
        //   382: aload_2         /* bmfont */
        //   383: astore_3       
        //   384: new             Ljava/lang/StringBuilder;
        //   387: dup            
        //   388: ldc             "page id="
        //   390: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   393: iload           i
        //   395: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   398: ldc             " file=\""
        //   400: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   403: aload_0         /* this */
        //   404: iload           i
        //   406: invokespecial   com/maltaisn/msdfgdx/gen/BMFont.getTextureAtlasFile:(I)Ljava/io/File;
        //   409: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   412: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   415: bipush          34
        //   417: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   420: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   423: astore          6
        //   425: aload_3        
        //   426: aload           6
        //   428: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   431: dup            
        //   432: ldc             "append(value)"
        //   434: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   437: invokestatic    kotlin/text/StringsKt___StringsKt.appendln:(Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
        //   440: pop            
        //   441: iinc            i, 1
        //   444: goto            375
        //   447: aload_0         /* this */
        //   448: getfield        com/maltaisn/msdfgdx/gen/BMFont.glyphs:Ljava/util/SortedMap;
        //   451: invokeinterface java/util/SortedMap.values:()Ljava/util/Collection;
        //   456: dup            
        //   457: ldc             "glyphs.values"
        //   459: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   462: checkcast       Ljava/lang/Iterable;
        //   465: dup            
        //   466: astore          $this$map$iv
        //   468: astore          6
        //   470: new             Ljava/util/ArrayList;
        //   473: dup            
        //   474: aload           $this$map$iv
        //   476: bipush          10
        //   478: istore_3       
        //   479: dup            
        //   480: astore_3       
        //   481: ldc             "$this$collectionSizeOrDefault"
        //   483: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   486: aload_3        
        //   487: instanceof      Ljava/util/Collection;
        //   490: ifeq            505
        //   493: aload_3        
        //   494: checkcast       Ljava/util/Collection;
        //   497: invokeinterface java/util/Collection.size:()I
        //   502: goto            507
        //   505: bipush          10
        //   507: invokespecial   java/util/ArrayList.<init>:(I)V
        //   510: checkcast       Ljava/util/Collection;
        //   513: astore          destination$iv$iv
        //   515: aload           $this$mapTo$iv$iv
        //   517: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   522: astore          9
        //   524: aload           9
        //   526: invokeinterface java/util/Iterator.hasNext:()Z
        //   531: ifeq            687
        //   534: aload           9
        //   536: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   541: astore          item$iv$iv
        //   543: aload           destination$iv$iv
        //   545: aload           item$iv$iv
        //   547: checkcast       Lcom/maltaisn/msdfgdx/gen/FontGlyph;
        //   550: astore          11
        //   552: astore          5
        //   554: aload           it
        //   556: invokevirtual   com/maltaisn/msdfgdx/gen/FontGlyph.getKernings:()Ljava/util/Map;
        //   559: invokeinterface java/util/Map.values:()Ljava/util/Collection;
        //   564: checkcast       Ljava/lang/Iterable;
        //   567: dup            
        //   568: astore          $this$count$iv
        //   570: instanceof      Ljava/util/Collection;
        //   573: ifeq            593
        //   576: aload           $this$count$iv
        //   578: checkcast       Ljava/util/Collection;
        //   581: invokeinterface java/util/Collection.isEmpty:()Z
        //   586: ifeq            593
        //   589: iconst_0       
        //   590: goto            671
        //   593: iconst_0       
        //   594: istore          count$iv
        //   596: aload           $this$count$iv
        //   598: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   603: astore          4
        //   605: aload           4
        //   607: invokeinterface java/util/Iterator.hasNext:()Z
        //   612: ifeq            669
        //   615: aload           4
        //   617: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   622: dup            
        //   623: astore_3       
        //   624: checkcast       Ljava/lang/Number;
        //   627: invokevirtual   java/lang/Number.intValue:()I
        //   630: dup            
        //   631: istore_3       
        //   632: ifeq            639
        //   635: iconst_1       
        //   636: goto            640
        //   639: iconst_0       
        //   640: ifeq            666
        //   643: iinc            count$iv, 1
        //   646: iload           count$iv
        //   648: dup            
        //   649: istore_3       
        //   650: ifge            666
        //   653: new             Ljava/lang/ArithmeticException;
        //   656: dup            
        //   657: ldc             "Count overflow has happened."
        //   659: invokespecial   java/lang/ArithmeticException.<init>:(Ljava/lang/String;)V
        //   662: checkcast       Ljava/lang/Throwable;
        //   665: athrow         
        //   666: goto            605
        //   669: iload           count$iv
        //   671: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   674: astore_3       
        //   675: aload           5
        //   677: aload_3        
        //   678: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   683: pop            
        //   684: goto            524
        //   687: aload           destination$iv$iv
        //   689: checkcast       Ljava/util/List;
        //   692: checkcast       Ljava/lang/Iterable;
        //   695: invokestatic    kotlin/collections/CollectionsKt.sumOfInt:(Ljava/lang/Iterable;)I
        //   698: istore          kerningsCount
        //   700: aload_0         /* this */
        //   701: getfield        com/maltaisn/msdfgdx/gen/BMFont.glyphs:Ljava/util/SortedMap;
        //   704: invokeinterface java/util/SortedMap.size:()I
        //   709: iload           kerningsCount
        //   711: iadd           
        //   712: istore          elementsCount
        //   714: fconst_0       
        //   715: fstore_3        /* elementsDone */
        //   716: aload_2         /* bmfont */
        //   717: astore          6
        //   719: new             Ljava/lang/StringBuilder;
        //   722: dup            
        //   723: ldc             "chars count="
        //   725: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   728: aload_0         /* this */
        //   729: getfield        com/maltaisn/msdfgdx/gen/BMFont.glyphs:Ljava/util/SortedMap;
        //   732: invokeinterface java/util/SortedMap.size:()I
        //   737: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   740: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   743: astore          7
        //   745: aload           6
        //   747: aload           7
        //   749: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   752: dup            
        //   753: ldc             "append(value)"
        //   755: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   758: invokestatic    kotlin/text/StringsKt___StringsKt.appendln:(Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
        //   761: pop            
        //   762: aload_0         /* this */
        //   763: getfield        com/maltaisn/msdfgdx/gen/BMFont.params:Lcom/maltaisn/msdfgdx/gen/Parameters;
        //   766: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.getHasAlphaChannel:()Z
        //   769: ifeq            777
        //   772: bipush          7
        //   774: goto            779
        //   777: bipush          15
        //   779: istore          channels
        //   781: aload_0         /* this */
        //   782: getfield        com/maltaisn/msdfgdx/gen/BMFont.glyphs:Ljava/util/SortedMap;
        //   785: checkcast       Ljava/util/Map;
        //   788: dup            
        //   789: astore          9
        //   791: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //   796: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   801: astore          8
        //   803: aload           8
        //   805: invokeinterface java/util/Iterator.hasNext:()Z
        //   810: ifeq            1050
        //   813: aload           8
        //   815: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   820: checkcast       Ljava/util/Map$Entry;
        //   823: dup            
        //   824: astore          7
        //   826: dup            
        //   827: astore          11
        //   829: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   834: checkcast       Ljava/lang/Character;
        //   837: astore          char
        //   839: aload           7
        //   841: dup            
        //   842: astore          11
        //   844: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   849: checkcast       Lcom/maltaisn/msdfgdx/gen/FontGlyph;
        //   852: astore          glyph
        //   854: aload_2         /* bmfont */
        //   855: astore          11
        //   857: new             Ljava/lang/StringBuilder;
        //   860: dup            
        //   861: ldc             "char id="
        //   863: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   866: aload           char
        //   868: invokevirtual   java/lang/Character.charValue:()C
        //   871: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   874: ldc             " x="
        //   876: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   879: aload           glyph
        //   881: invokevirtual   com/maltaisn/msdfgdx/gen/FontGlyph.getX:()I
        //   884: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   887: ldc             " y="
        //   889: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   892: aload           glyph
        //   894: invokevirtual   com/maltaisn/msdfgdx/gen/FontGlyph.getY:()I
        //   897: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   900: bipush          32
        //   902: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   905: ldc             "width="
        //   907: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   910: aload           glyph
        //   912: invokevirtual   com/maltaisn/msdfgdx/gen/FontGlyph.getWidth:()I
        //   915: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   918: ldc             " height="
        //   920: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   923: aload           glyph
        //   925: invokevirtual   com/maltaisn/msdfgdx/gen/FontGlyph.getHeight:()I
        //   928: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   931: bipush          32
        //   933: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   936: ldc             "xoffset="
        //   938: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   941: aload           glyph
        //   943: invokevirtual   com/maltaisn/msdfgdx/gen/FontGlyph.getXOffset:()I
        //   946: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   949: ldc             " yoffset="
        //   951: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   954: aload           glyph
        //   956: invokevirtual   com/maltaisn/msdfgdx/gen/FontGlyph.getYOffset:()I
        //   959: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   962: bipush          32
        //   964: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   967: ldc             "xadvance="
        //   969: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   972: aload           glyph
        //   974: invokevirtual   com/maltaisn/msdfgdx/gen/FontGlyph.getXAdvance:()I
        //   977: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   980: ldc             " page="
        //   982: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   985: aload           glyph
        //   987: invokevirtual   com/maltaisn/msdfgdx/gen/FontGlyph.getPage:()I
        //   990: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   993: ldc             " chnl="
        //   995: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   998: iload           channels
        //  1000: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1003: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1006: astore          7
        //  1008: aload           11
        //  1010: aload           7
        //  1012: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1015: dup            
        //  1016: ldc             "append(value)"
        //  1018: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1021: invokestatic    kotlin/text/StringsKt___StringsKt.appendln:(Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
        //  1024: pop            
        //  1025: fload_3         /* elementsDone */
        //  1026: fconst_1       
        //  1027: fadd           
        //  1028: fstore_3        /* elementsDone */
        //  1029: aload_1         /* progressListener */
        //  1030: getstatic       com/maltaisn/msdfgdx/gen/BMFont$GenerationStep.FONT_FILE:Lcom/maltaisn/msdfgdx/gen/BMFont$GenerationStep;
        //  1033: fload_3         /* elementsDone */
        //  1034: iload           elementsCount
        //  1036: i2f            
        //  1037: fdiv           
        //  1038: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //  1041: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1046: pop            
        //  1047: goto            803
        //  1050: aload_2         /* bmfont */
        //  1051: astore          7
        //  1053: new             Ljava/lang/StringBuilder;
        //  1056: dup            
        //  1057: ldc             "kernings count="
        //  1059: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //  1062: iload           kerningsCount
        //  1064: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1067: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1070: astore          8
        //  1072: aload           7
        //  1074: aload           8
        //  1076: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1079: dup            
        //  1080: ldc             "append(value)"
        //  1082: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1085: invokestatic    kotlin/text/StringsKt___StringsKt.appendln:(Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
        //  1088: pop            
        //  1089: aload_0         /* this */
        //  1090: getfield        com/maltaisn/msdfgdx/gen/BMFont.glyphs:Ljava/util/SortedMap;
        //  1093: checkcast       Ljava/util/Map;
        //  1096: dup            
        //  1097: astore          9
        //  1099: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //  1104: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //  1109: astore          8
        //  1111: aload           8
        //  1113: invokeinterface java/util/Iterator.hasNext:()Z
        //  1118: ifeq            1332
        //  1121: aload           8
        //  1123: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  1128: checkcast       Ljava/util/Map$Entry;
        //  1131: dup            
        //  1132: astore          7
        //  1134: dup            
        //  1135: astore          11
        //  1137: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //  1142: checkcast       Ljava/lang/Character;
        //  1145: astore          char
        //  1147: aload           7
        //  1149: dup            
        //  1150: astore          11
        //  1152: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //  1157: checkcast       Lcom/maltaisn/msdfgdx/gen/FontGlyph;
        //  1160: dup            
        //  1161: astore          10
        //  1163: invokevirtual   com/maltaisn/msdfgdx/gen/FontGlyph.getKernings:()Ljava/util/Map;
        //  1166: dup            
        //  1167: astore          4
        //  1169: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //  1174: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //  1179: astore          7
        //  1181: aload           7
        //  1183: invokeinterface java/util/Iterator.hasNext:()Z
        //  1188: ifeq            1329
        //  1191: aload           7
        //  1193: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  1198: checkcast       Ljava/util/Map$Entry;
        //  1201: dup            
        //  1202: astore          11
        //  1204: dup            
        //  1205: astore          10
        //  1207: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //  1212: checkcast       Ljava/lang/Character;
        //  1215: invokevirtual   java/lang/Character.charValue:()C
        //  1218: istore          other
        //  1220: aload           11
        //  1222: dup            
        //  1223: astore          10
        //  1225: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //  1230: checkcast       Ljava/lang/Number;
        //  1233: invokevirtual   java/lang/Number.intValue:()I
        //  1236: dup            
        //  1237: istore          kerning
        //  1239: ifeq            1326
        //  1242: aload_2         /* bmfont */
        //  1243: astore          10
        //  1245: new             Ljava/lang/StringBuilder;
        //  1248: dup            
        //  1249: ldc             "kerning first="
        //  1251: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //  1254: aload           char
        //  1256: invokevirtual   java/lang/Character.charValue:()C
        //  1259: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1262: ldc             " second="
        //  1264: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1267: iload           other
        //  1269: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1272: ldc             " amount="
        //  1274: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1277: iload           kerning
        //  1279: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1282: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1285: astore          4
        //  1287: aload           10
        //  1289: aload           4
        //  1291: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1294: dup            
        //  1295: ldc             "append(value)"
        //  1297: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1300: invokestatic    kotlin/text/StringsKt___StringsKt.appendln:(Ljava/lang/StringBuilder;)Ljava/lang/StringBuilder;
        //  1303: pop            
        //  1304: fload_3         /* elementsDone */
        //  1305: fconst_1       
        //  1306: fadd           
        //  1307: fstore_3        /* elementsDone */
        //  1308: aload_1         /* progressListener */
        //  1309: getstatic       com/maltaisn/msdfgdx/gen/BMFont$GenerationStep.FONT_FILE:Lcom/maltaisn/msdfgdx/gen/BMFont$GenerationStep;
        //  1312: fload_3         /* elementsDone */
        //  1313: iload           elementsCount
        //  1315: i2f            
        //  1316: fdiv           
        //  1317: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //  1320: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1325: pop            
        //  1326: goto            1181
        //  1329: goto            1111
        //  1332: new             Ljava/io/File;
        //  1335: dup            
        //  1336: aload_0         /* this */
        //  1337: getfield        com/maltaisn/msdfgdx/gen/BMFont.params:Lcom/maltaisn/msdfgdx/gen/Parameters;
        //  1340: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.getOutputDir:()Ljava/lang/String;
        //  1343: new             Ljava/lang/StringBuilder;
        //  1346: dup            
        //  1347: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1350: aload_0         /* this */
        //  1351: getfield        com/maltaisn/msdfgdx/gen/BMFont.fontFile:Ljava/io/File;
        //  1354: invokestatic    kotlin/io/FilesKt.getNameWithoutExtension:(Ljava/io/File;)Ljava/lang/String;
        //  1357: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1360: ldc             ".fnt"
        //  1362: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1365: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1368: invokespecial   java/io/File.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //  1371: aload_2         /* bmfont */
        //  1372: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1375: dup            
        //  1376: ldc             "bmfont.toString()"
        //  1378: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1381: aconst_null    
        //  1382: iconst_2       
        //  1383: invokestatic    kotlin/io/FilesKt.writeText$default$1b43c825:(Ljava/io/File;Ljava/lang/String;Ljava/nio/charset/Charset;I)V
        //  1386: return         
        //    Signature:
        //  (Lkotlin/jvm/functions/Function2<-Lcom/maltaisn/msdfgdx/gen/BMFont$GenerationStep;-Ljava/lang/Float;Lkotlin/Unit;>;)V
        //    StackMapTable: 00 1B FE 00 34 07 00 56 00 07 00 33 FF 00 4B 00 05 07 00 3C 07 00 67 07 00 56 07 00 33 07 00 56 00 01 07 00 56 FF 00 00 00 05 07 00 3C 07 00 67 07 00 56 07 00 33 07 00 56 00 02 07 00 56 01 5B 07 00 56 FF 00 00 00 05 07 00 3C 07 00 67 07 00 56 07 00 33 07 00 56 00 02 07 00 56 01 FF 00 D8 00 06 07 00 3C 07 00 67 07 00 56 00 01 01 00 00 F8 00 47 FF 00 39 00 07 07 00 3C 07 00 67 07 00 56 00 00 00 07 00 52 00 02 08 01 D6 08 01 D6 FF 00 01 00 07 07 00 3C 07 00 67 07 00 56 00 00 00 07 00 52 00 03 08 01 D6 08 01 D6 01 FF 00 10 00 0A 07 00 3C 07 00 67 07 00 56 00 00 00 00 07 00 59 00 07 00 5A 00 00 FF 00 44 00 0A 07 00 3C 07 00 67 07 00 56 00 07 00 52 07 00 59 00 07 00 59 00 07 00 5A 00 00 FF 00 0B 00 0B 07 00 3C 07 00 67 07 00 56 00 07 00 5A 07 00 59 00 07 00 59 00 07 00 5A 01 00 00 21 40 01 19 FF 00 02 00 0B 07 00 3C 07 00 67 07 00 56 00 00 07 00 59 00 07 00 59 00 07 00 5A 01 00 00 FF 00 01 00 0A 07 00 3C 07 00 67 07 00 56 00 00 07 00 59 00 07 00 59 00 07 00 5A 00 01 01 FF 00 0F 00 08 07 00 3C 07 00 67 07 00 56 00 00 00 00 07 00 59 00 00 FF 00 59 00 06 07 00 3C 07 00 67 07 00 56 02 01 01 00 00 41 01 FE 00 17 01 00 07 00 5A F8 00 F6 FF 00 3C 00 09 07 00 3C 07 00 67 07 00 56 02 00 01 00 00 07 00 5A 00 00 FF 00 45 00 0A 07 00 3C 07 00 67 07 00 56 02 00 01 00 07 00 5A 07 00 5A 07 00 4E 00 00 FB 00 90 FF 00 02 00 09 07 00 3C 07 00 67 07 00 56 02 00 01 00 00 07 00 5A 00 00 FF 00 02 00 03 07 00 3C 00 07 00 56 00 00
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
    
    private final void compress(final Function2<? super GenerationStep, ? super Float, Unit> progressListener) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/maltaisn/msdfgdx/gen/BMFont.params:Lcom/maltaisn/msdfgdx/gen/Parameters;
        //     4: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.getCompressionLevel:()I
        //     7: ifne            11
        //    10: return         
        //    11: aload_1         /* progressListener */
        //    12: getstatic       com/maltaisn/msdfgdx/gen/BMFont$GenerationStep.COMPRESS:Lcom/maltaisn/msdfgdx/gen/BMFont$GenerationStep;
        //    15: fconst_0       
        //    16: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //    19: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    24: pop            
        //    25: aload_0         /* this */
        //    26: getfield        com/maltaisn/msdfgdx/gen/BMFont.atlasData:Lcom/badlogic/gdx/graphics/g2d/TextureAtlas$TextureAtlasData;
        //    29: dup            
        //    30: astore_3       
        //    31: ifnonnull       54
        //    34: ldc             "Required value was null."
        //    36: astore          6
        //    38: new             Ljava/lang/IllegalStateException;
        //    41: dup            
        //    42: aload           6
        //    44: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    47: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //    50: checkcast       Ljava/lang/Throwable;
        //    53: athrow         
        //    54: aload_3        
        //    55: astore_2        /* atlasData */
        //    56: new             Lcom/googlecode/pngtastic/core/PngOptimizer;
        //    59: dup            
        //    60: invokespecial   com/googlecode/pngtastic/core/PngOptimizer.<init>:()V
        //    63: astore_3        /* pngOptimizer */
        //    64: iconst_0       
        //    65: istore          4
        //    67: aload_2         /* atlasData */
        //    68: invokevirtual   com/badlogic/gdx/graphics/g2d/TextureAtlas$TextureAtlasData.getPages:()Lcom/badlogic/gdx/utils/Array;
        //    71: getfield        com/badlogic/gdx/utils/Array.size:I
        //    74: istore          5
        //    76: iload           4
        //    78: iload           5
        //    80: if_icmpge       225
        //    83: aload_0         /* this */
        //    84: iload           i
        //    86: invokespecial   com/maltaisn/msdfgdx/gen/BMFont.getTextureAtlasFile:(I)Ljava/io/File;
        //    89: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    92: astore          file
        //    94: new             Ljava/io/BufferedInputStream;
        //    97: dup            
        //    98: new             Ljava/io/FileInputStream;
        //   101: dup            
        //   102: aload           file
        //   104: invokespecial   java/io/FileInputStream.<init>:(Ljava/lang/String;)V
        //   107: checkcast       Ljava/io/InputStream;
        //   110: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //   113: dup            
        //   114: astore          inputStream
        //   116: checkcast       Ljava/io/Closeable;
        //   119: astore          8
        //   121: aconst_null    
        //   122: astore          9
        //   124: new             Lcom/googlecode/pngtastic/core/PngImage;
        //   127: dup            
        //   128: aload           inputStream
        //   130: checkcast       Ljava/io/InputStream;
        //   133: invokespecial   com/googlecode/pngtastic/core/PngImage.<init>:(Ljava/io/InputStream;)V
        //   136: dup            
        //   137: astore          pngImage
        //   139: aload           file
        //   141: invokevirtual   com/googlecode/pngtastic/core/PngImage.setFileName:(Ljava/lang/String;)V
        //   144: aload_3         /* pngOptimizer */
        //   145: aload           pngImage
        //   147: aload           file
        //   149: iconst_0       
        //   150: aload_0         /* this */
        //   151: getfield        com/maltaisn/msdfgdx/gen/BMFont.params:Lcom/maltaisn/msdfgdx/gen/Parameters;
        //   154: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.getCompressionLevel:()I
        //   157: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   160: invokevirtual   com/googlecode/pngtastic/core/PngOptimizer.optimize:(Lcom/googlecode/pngtastic/core/PngImage;Ljava/lang/String;ZLjava/lang/Integer;)V
        //   163: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   166: pop            
        //   167: aload           8
        //   169: aconst_null    
        //   170: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   173: goto            192
        //   176: dup            
        //   177: astore_1        /* progressListener */
        //   178: astore          9
        //   180: aload_1         /* progressListener */
        //   181: athrow         
        //   182: astore_1        /* progressListener */
        //   183: aload           8
        //   185: aload           9
        //   187: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   190: aload_1         /* progressListener */
        //   191: athrow         
        //   192: aload_1         /* progressListener */
        //   193: getstatic       com/maltaisn/msdfgdx/gen/BMFont$GenerationStep.COMPRESS:Lcom/maltaisn/msdfgdx/gen/BMFont$GenerationStep;
        //   196: iload           i
        //   198: iconst_1       
        //   199: iadd           
        //   200: i2f            
        //   201: aload_2         /* atlasData */
        //   202: invokevirtual   com/badlogic/gdx/graphics/g2d/TextureAtlas$TextureAtlasData.getPages:()Lcom/badlogic/gdx/utils/Array;
        //   205: getfield        com/badlogic/gdx/utils/Array.size:I
        //   208: i2f            
        //   209: fdiv           
        //   210: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
        //   213: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   218: pop            
        //   219: iinc            i, 1
        //   222: goto            76
        //   225: return         
        //    Signature:
        //  (Lkotlin/jvm/functions/Function2<-Lcom/maltaisn/msdfgdx/gen/BMFont$GenerationStep;-Ljava/lang/Float;Lkotlin/Unit;>;)V
        //    StackMapTable: 00 07 0B FD 00 2A 00 07 00 33 FF 00 15 00 06 07 00 3C 07 00 67 07 00 33 07 00 3B 01 01 00 00 FF 00 63 00 0A 00 00 00 00 00 00 00 00 07 00 49 05 00 01 07 00 57 FF 00 05 00 0A 00 00 00 00 00 00 00 00 07 00 49 07 00 57 00 01 07 00 57 FF 00 09 00 06 07 00 3C 07 00 67 07 00 33 07 00 3B 01 01 00 00 FF 00 20 00 00 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  124    167    176    182    Ljava/lang/Throwable;
        //  124    167    182    192    Any
        //  176    182    182    192    Any
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
    
    private final File getTextureAtlasFile(final int pageIndex) {
        return new File(this.params.getOutputDir(), FilesKt__FileTreeWalkKt.getNameWithoutExtension(this.fontFile) + ((pageIndex == 0) ? "" : String.valueOf(pageIndex + 1)) + ".png");
    }
    
    public BMFont(final File fontFile, final Parameters params) {
        Intrinsics.checkParameterIsNotNull(fontFile, "fontFile");
        Intrinsics.checkParameterIsNotNull(params, "params");
        this.fontFile = fontFile;
        this.params = params;
        Font deriveFont;
        try {
            deriveFont = Font.createFont(0, this.fontFile).deriveFont((float)this.params.getFontSize());
        }
        catch (FontFormatException e) {
            Pixmap.paramError("ERROR: Could not load font file: " + e.getMessage());
            throw null;
        }
        this.font = deriveFont;
        this.glyphs = ArraysKt__ArraysJVMKt.sortedMapOf((Pair<? extends Character, ? extends FontGlyph>[])new Pair[0]);
        this.fontMetrics = new Canvas().getFontMetrics(this.font);
        this.fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
    }
    
    public enum GenerationStep
    {
        GLYPH, 
        PACK, 
        FONT_FILE, 
        COMPRESS;
    }
}
