// 
// Decompiled by Procyon v0.5.36
// 

package com.maltaisn.msdfgdx.gen;

import kotlin.jvm.internal.Intrinsics;
import java.awt.image.BufferedImage;

public final class MsdfGen
{
    private final String msdfgen;
    private int width;
    private int height;
    private int distanceRange;
    private String shapeDescr;
    
    public final BufferedImage generateImage(final String fieldType) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "fieldType"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: new             Ljava/lang/ProcessBuilder;
        //     9: dup            
        //    10: iconst_0       
        //    11: anewarray       Ljava/lang/String;
        //    14: invokespecial   java/lang/ProcessBuilder.<init>:([Ljava/lang/String;)V
        //    17: bipush          12
        //    19: anewarray       Ljava/lang/String;
        //    22: dup            
        //    23: iconst_0       
        //    24: aload_0         /* this */
        //    25: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.msdfgen:Ljava/lang/String;
        //    28: aastore        
        //    29: dup            
        //    30: iconst_1       
        //    31: ldc             "-format"
        //    33: aastore        
        //    34: dup            
        //    35: iconst_2       
        //    36: ldc             "text"
        //    38: aastore        
        //    39: dup            
        //    40: iconst_3       
        //    41: ldc             "-stdout"
        //    43: aastore        
        //    44: dup            
        //    45: iconst_4       
        //    46: ldc             "-size"
        //    48: aastore        
        //    49: dup            
        //    50: iconst_5       
        //    51: aload_0         /* this */
        //    52: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.width:I
        //    55: invokestatic    java/lang/String.valueOf:(I)Ljava/lang/String;
        //    58: aastore        
        //    59: dup            
        //    60: bipush          6
        //    62: aload_0         /* this */
        //    63: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.height:I
        //    66: invokestatic    java/lang/String.valueOf:(I)Ljava/lang/String;
        //    69: aastore        
        //    70: dup            
        //    71: bipush          7
        //    73: ldc             "-pxrange"
        //    75: aastore        
        //    76: dup            
        //    77: bipush          8
        //    79: aload_0         /* this */
        //    80: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.distanceRange:I
        //    83: invokestatic    java/lang/String.valueOf:(I)Ljava/lang/String;
        //    86: aastore        
        //    87: dup            
        //    88: bipush          9
        //    90: ldc             "-defineshape"
        //    92: aastore        
        //    93: dup            
        //    94: bipush          10
        //    96: aload_0         /* this */
        //    97: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.shapeDescr:Ljava/lang/String;
        //   100: aastore        
        //   101: dup            
        //   102: bipush          11
        //   104: aload_1         /* fieldType */
        //   105: aastore        
        //   106: invokevirtual   java/lang/ProcessBuilder.command:([Ljava/lang/String;)Ljava/lang/ProcessBuilder;
        //   109: invokevirtual   java/lang/ProcessBuilder.start:()Ljava/lang/Process;
        //   112: astore_1        /* process */
        //   113: new             Ljava/io/BufferedReader;
        //   116: dup            
        //   117: new             Ljava/io/InputStreamReader;
        //   120: dup            
        //   121: aload_1         /* process */
        //   122: dup            
        //   123: ldc             "process"
        //   125: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   128: invokevirtual   java/lang/Process.getInputStream:()Ljava/io/InputStream;
        //   131: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   134: checkcast       Ljava/io/Reader;
        //   137: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //   140: astore_1        /* reader */
        //   141: aload_0         /* this */
        //   142: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.width:I
        //   145: aload_0         /* this */
        //   146: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.height:I
        //   149: imul           
        //   150: newarray        I
        //   152: astore_2        /* pixels */
        //   153: aload_1         /* reader */
        //   154: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   157: dup            
        //   158: astore_3        /* line */
        //   159: dup            
        //   160: ifnonnull       166
        //   163: invokestatic    kotlin/jvm/internal/Intrinsics.throwNpe:()V
        //   166: invokevirtual   java/lang/String.length:()I
        //   169: iconst_1       
        //   170: iadd           
        //   171: aload_0         /* this */
        //   172: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.width:I
        //   175: idiv           
        //   176: iconst_3       
        //   177: idiv           
        //   178: dup            
        //   179: istore          channels
        //   181: iconst_1       
        //   182: if_icmpeq       191
        //   185: iload           channels
        //   187: iconst_3       
        //   188: if_icmpne       195
        //   191: iconst_1       
        //   192: goto            196
        //   195: iconst_0       
        //   196: dup            
        //   197: istore          5
        //   199: ifne            222
        //   202: ldc             "msdfgen generated image with unsupported channels count."
        //   204: astore          7
        //   206: new             Ljava/lang/IllegalStateException;
        //   209: dup            
        //   210: aload           7
        //   212: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   215: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   218: checkcast       Ljava/lang/Throwable;
        //   221: athrow         
        //   222: aload_2         /* pixels */
        //   223: arraylength    
        //   224: istore          i
        //   226: aload_3         /* line */
        //   227: ifnull          480
        //   230: iload           i
        //   232: aload_0         /* this */
        //   233: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.width:I
        //   236: isub           
        //   237: istore          i
        //   239: iconst_0       
        //   240: istore          6
        //   242: aload_0         /* this */
        //   243: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.width:I
        //   246: istore          7
        //   248: iload           6
        //   250: iload           7
        //   252: if_icmpge       472
        //   255: iload           j
        //   257: iload           channels
        //   259: imul           
        //   260: iconst_3       
        //   261: imul           
        //   262: istore          8
        //   264: iload           channels
        //   266: iconst_1       
        //   267: if_icmpne       317
        //   270: aload_3         /* line */
        //   271: astore          11
        //   273: iload           8
        //   275: iconst_2       
        //   276: iadd           
        //   277: istore          12
        //   279: aload           11
        //   281: iload           8
        //   283: iload           12
        //   285: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   288: dup            
        //   289: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   291: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   294: dup            
        //   295: astore          11
        //   297: bipush          16
        //   299: invokestatic    kotlin/text/CharsKt.checkRadix:(I)I
        //   302: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;I)I
        //   305: dup            
        //   306: istore          r
        //   308: istore          g
        //   310: iload           r
        //   312: istore          b
        //   314: goto            442
        //   317: aload_3         /* line */
        //   318: astore          11
        //   320: iload           b
        //   322: iconst_2       
        //   323: iadd           
        //   324: istore          12
        //   326: aload           11
        //   328: iload           b
        //   330: iload           12
        //   332: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   335: dup            
        //   336: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   338: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   341: dup            
        //   342: astore          11
        //   344: bipush          16
        //   346: invokestatic    kotlin/text/CharsKt.checkRadix:(I)I
        //   349: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;I)I
        //   352: istore          r
        //   354: aload_3         /* line */
        //   355: astore          11
        //   357: iload           b
        //   359: iconst_3       
        //   360: iadd           
        //   361: istore          12
        //   363: iload           b
        //   365: iconst_5       
        //   366: iadd           
        //   367: istore          13
        //   369: aload           11
        //   371: iload           12
        //   373: iload           13
        //   375: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   378: dup            
        //   379: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   381: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   384: dup            
        //   385: astore          11
        //   387: bipush          16
        //   389: invokestatic    kotlin/text/CharsKt.checkRadix:(I)I
        //   392: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;I)I
        //   395: istore          g
        //   397: aload_3         /* line */
        //   398: astore          11
        //   400: iload           b
        //   402: bipush          6
        //   404: iadd           
        //   405: istore          12
        //   407: iload           b
        //   409: bipush          8
        //   411: iadd           
        //   412: istore          13
        //   414: aload           11
        //   416: iload           12
        //   418: iload           13
        //   420: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   423: dup            
        //   424: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   426: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   429: dup            
        //   430: astore          11
        //   432: bipush          16
        //   434: invokestatic    kotlin/text/CharsKt.checkRadix:(I)I
        //   437: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;I)I
        //   440: istore          b
        //   442: aload_2         /* pixels */
        //   443: iload           i
        //   445: iload           j
        //   447: iadd           
        //   448: ldc             -16777216
        //   450: iload           r
        //   452: bipush          16
        //   454: ishl           
        //   455: ior            
        //   456: iload           g
        //   458: bipush          8
        //   460: ishl           
        //   461: ior            
        //   462: iload           b
        //   464: ior            
        //   465: iastore        
        //   466: iinc            j, 1
        //   469: goto            248
        //   472: aload_1         /* reader */
        //   473: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   476: astore_3        /* line */
        //   477: goto            226
        //   480: new             Ljava/awt/image/BufferedImage;
        //   483: dup            
        //   484: aload_0         /* this */
        //   485: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.width:I
        //   488: aload_0         /* this */
        //   489: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.height:I
        //   492: iconst_2       
        //   493: invokespecial   java/awt/image/BufferedImage.<init>:(III)V
        //   496: dup            
        //   497: astore          image
        //   499: iconst_0       
        //   500: iconst_0       
        //   501: aload_0         /* this */
        //   502: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.width:I
        //   505: aload_0         /* this */
        //   506: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.height:I
        //   509: aload_2         /* pixels */
        //   510: iconst_0       
        //   511: aload_0         /* this */
        //   512: getfield        com/maltaisn/msdfgdx/gen/MsdfGen.width:I
        //   515: invokevirtual   java/awt/image/BufferedImage.setRGB:(IIII[III)V
        //   518: aload           image
        //   520: areturn        
        //    StackMapTable: 00 0B FF 00 A6 00 04 07 00 0F 07 00 11 07 00 0E 07 00 19 00 01 07 00 19 FC 00 18 01 03 40 01 19 FC 00 03 01 FD 00 15 01 01 FC 00 44 01 FD 00 7C 01 01 FF 00 1D 00 06 07 00 0F 07 00 11 07 00 0E 00 01 01 00 00 FF 00 07 00 03 07 00 0F 00 07 00 0E 00 00
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
    
    public MsdfGen(final String msdfgen, final int width, final int height, final int distanceRange, final String shapeDescr) {
        Intrinsics.checkParameterIsNotNull(msdfgen, "msdfgen");
        Intrinsics.checkParameterIsNotNull(shapeDescr, "shapeDescr");
        this.msdfgen = msdfgen;
        this.width = width;
        this.height = height;
        this.distanceRange = distanceRange;
        this.shapeDescr = shapeDescr;
    }
}
