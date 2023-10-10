// 
// Decompiled by Procyon v0.5.36
// 

package com.maltaisn.msdfgdx.gen;

public final class MainKt
{
    public static final void main(final String[] args) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "args"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: new             Lcom/maltaisn/msdfgdx/gen/Parameters;
        //     9: dup            
        //    10: invokespecial   com/maltaisn/msdfgdx/gen/Parameters.<init>:()V
        //    13: astore_1        /* params */
        //    14: invokestatic    com/beust/jcommander/JCommander.newBuilder:()Lcom/beust/jcommander/JCommander$Builder;
        //    17: aload_1         /* params */
        //    18: invokevirtual   com/beust/jcommander/JCommander$Builder.addObject:(Ljava/lang/Object;)Lcom/beust/jcommander/JCommander$Builder;
        //    21: invokevirtual   com/beust/jcommander/JCommander$Builder.build:()Lcom/beust/jcommander/JCommander;
        //    24: dup            
        //    25: astore_2        /* commander */
        //    26: dup            
        //    27: ldc             "commander"
        //    29: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //    32: ldc             "msdfgen-bmfont"
        //    34: invokevirtual   com/beust/jcommander/JCommander.setProgramName:(Ljava/lang/String;)V
        //    37: aload_2         /* commander */
        //    38: aload_0         /* args */
        //    39: dup            
        //    40: arraylength    
        //    41: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //    44: checkcast       [Ljava/lang/String;
        //    47: invokevirtual   com/beust/jcommander/JCommander.parse:([Ljava/lang/String;)V
        //    50: goto            64
        //    53: dup            
        //    54: astore_0       
        //    55: invokevirtual   com/beust/jcommander/ParameterException.getMessage:()Ljava/lang/String;
        //    58: invokestatic    com/badlogic/gdx/graphics/Pixmap.paramError:(Ljava/lang/Object;)Ljava/lang/Void;
        //    61: pop            
        //    62: aconst_null    
        //    63: athrow         
        //    64: aload_1         /* params */
        //    65: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.getHelp:()Z
        //    68: ifeq            92
        //    71: aload_2         /* commander */
        //    72: invokevirtual   com/beust/jcommander/JCommander.usage:()V
        //    75: iconst_1       
        //    76: invokestatic    java/lang/System.exit:(I)V
        //    79: new             Ljava/lang/RuntimeException;
        //    82: dup            
        //    83: ldc             "System.exit returned normally, while it was supposed to halt JVM."
        //    85: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //    88: checkcast       Ljava/lang/Throwable;
        //    91: athrow         
        //    92: aload_1         /* params */
        //    93: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.validate:()V
        //    96: invokestatic    java/text/DecimalFormat.getInstance:()Ljava/text/NumberFormat;
        //    99: dup            
        //   100: astore_0        /* numberFmt */
        //   101: dup            
        //   102: ldc             "numberFmt"
        //   104: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   107: iconst_1       
        //   108: invokevirtual   java/text/NumberFormat.setMinimumFractionDigits:(I)V
        //   111: aload_0         /* numberFmt */
        //   112: iconst_1       
        //   113: invokevirtual   java/text/NumberFormat.setMaximumFractionDigits:(I)V
        //   116: aload_1         /* params */
        //   117: invokevirtual   com/maltaisn/msdfgdx/gen/Parameters.getParams:()Ljava/util/List;
        //   120: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   125: astore          4
        //   127: aload           4
        //   129: invokeinterface java/util/Iterator.hasNext:()Z
        //   134: ifeq            299
        //   137: aload           4
        //   139: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   144: checkcast       Ljava/lang/String;
        //   147: astore_3        /* fontPath */
        //   148: invokestatic    java/lang/System.currentTimeMillis:()J
        //   151: lstore          startTime
        //   153: new             Lkotlin/jvm/internal/Ref$LongRef;
        //   156: dup            
        //   157: invokespecial   kotlin/jvm/internal/Ref$LongRef.<init>:()V
        //   160: dup            
        //   161: astore          5
        //   163: lconst_0       
        //   164: putfield        kotlin/jvm/internal/Ref$LongRef.element:J
        //   167: new             Ljava/io/File;
        //   170: dup            
        //   171: aload_3         /* fontPath */
        //   172: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   175: astore_3        /* fontFile */
        //   176: new             Lcom/maltaisn/msdfgdx/gen/BMFont;
        //   179: dup            
        //   180: aload_3         /* fontFile */
        //   181: aload_1         /* params */
        //   182: invokespecial   com/maltaisn/msdfgdx/gen/BMFont.<init>:(Ljava/io/File;Lcom/maltaisn/msdfgdx/gen/Parameters;)V
        //   185: astore          bmfont
        //   187: new             Ljava/lang/StringBuilder;
        //   190: dup            
        //   191: ldc             "Generating distance field font for '"
        //   193: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   196: aload_3         /* fontFile */
        //   197: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   200: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   203: ldc             "'."
        //   205: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   208: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   211: astore_3       
        //   212: getstatic       java/lang/System.out:Ljava/io/PrintStream;
        //   215: aload_3        
        //   216: invokevirtual   java/io/PrintStream.println:(Ljava/lang/Object;)V
        //   219: new             Lkotlin/jvm/internal/Ref$IntRef;
        //   222: dup            
        //   223: invokespecial   kotlin/jvm/internal/Ref$IntRef.<init>:()V
        //   226: dup            
        //   227: astore_3       
        //   228: iconst_m1      
        //   229: putfield        kotlin/jvm/internal/Ref$IntRef.element:I
        //   232: aload           bmfont
        //   234: new             Lcom/maltaisn/msdfgdx/gen/MainKt$main$1;
        //   237: dup            
        //   238: aload_3         /* lastPercentProgress */
        //   239: aload           stepStartTime
        //   241: aload_0         /* numberFmt */
        //   242: invokespecial   com/maltaisn/msdfgdx/gen/MainKt$main$1.<init>:(Lkotlin/jvm/internal/Ref$IntRef;Lkotlin/jvm/internal/Ref$LongRef;Ljava/text/NumberFormat;)V
        //   245: checkcast       Lkotlin/jvm/functions/Function2;
        //   248: invokevirtual   com/maltaisn/msdfgdx/gen/BMFont.generate:(Lkotlin/jvm/functions/Function2;)V
        //   251: aload_0         /* numberFmt */
        //   252: invokestatic    java/lang/System.currentTimeMillis:()J
        //   255: lload           startTime
        //   257: lsub           
        //   258: l2d            
        //   259: ldc2_w          1000.0
        //   262: ddiv           
        //   263: invokevirtual   java/text/NumberFormat.format:(D)Ljava/lang/String;
        //   266: astore_3        /* durationStr */
        //   267: new             Ljava/lang/StringBuilder;
        //   270: dup            
        //   271: ldc             "DONE in "
        //   273: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   276: aload_3         /* durationStr */
        //   277: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   280: ldc             " s\n"
        //   282: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   285: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   288: astore_3       
        //   289: getstatic       java/lang/System.out:Ljava/io/PrintStream;
        //   292: aload_3        
        //   293: invokevirtual   java/io/PrintStream.println:(Ljava/lang/Object;)V
        //   296: goto            127
        //   299: iconst_0       
        //   300: invokestatic    java/lang/System.exit:(I)V
        //   303: new             Ljava/lang/RuntimeException;
        //   306: dup            
        //   307: ldc             "System.exit returned normally, while it was supposed to halt JVM."
        //   309: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   312: checkcast       Ljava/lang/Throwable;
        //   315: athrow         
        //   316: astore_0        /* e */
        //   317: new             Ljava/lang/StringBuilder;
        //   320: dup            
        //   321: ldc             "ERROR: "
        //   323: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   326: aload_0         /* e */
        //   327: invokevirtual   com/maltaisn/msdfgdx/gen/ParameterException.getMessage:()Ljava/lang/String;
        //   330: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   333: bipush          10
        //   335: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   338: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   341: astore_3       
        //   342: getstatic       java/lang/System.out:Ljava/io/PrintStream;
        //   345: aload_3        
        //   346: invokevirtual   java/io/PrintStream.println:(Ljava/lang/Object;)V
        //   349: aload_2         /* commander */
        //   350: invokevirtual   com/beust/jcommander/JCommander.usage:()V
        //   353: iconst_1       
        //   354: invokestatic    java/lang/System.exit:(I)V
        //   357: new             Ljava/lang/RuntimeException;
        //   360: dup            
        //   361: ldc             "System.exit returned normally, while it was supposed to halt JVM."
        //   363: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   366: checkcast       Ljava/lang/Throwable;
        //   369: athrow         
        //    StackMapTable: 00 06 FF 00 35 00 03 00 00 07 00 0D 00 01 07 00 0F FF 00 0A 00 03 00 07 00 14 07 00 0D 00 00 1B FF 00 22 00 05 07 00 1E 07 00 14 07 00 0D 00 07 00 20 00 00 FF 00 AB 00 03 00 00 07 00 0D 00 00 50 07 00 13
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                         
        //  -----  -----  -----  -----  ---------------------------------------------
        //  37     50     53     64     Lcom/beust/jcommander/ParameterException;
        //  37     316    316    370    Lcom/maltaisn/msdfgdx/gen/ParameterException;
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
}
