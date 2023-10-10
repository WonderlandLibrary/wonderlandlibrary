// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;

class StringsKt__StringNumberConversionsKt extends StringsKt__StringBuilderKt
{
    public static final Integer toIntOrNull(String $this$toIntOrNull) {
        Intrinsics.checkParameterIsNotNull($this$toIntOrNull, "$this$toIntOrNull");
        $this$toIntOrNull = $this$toIntOrNull;
        Intrinsics.checkParameterIsNotNull($this$toIntOrNull, "$this$toIntOrNull");
        CharsKt__CharJVMKt.checkRadix(10);
        final int length;
        if ((length = $this$toIntOrNull.length()) == 0) {
            return null;
        }
        final char char1;
        int n;
        boolean b;
        int n2;
        if ((char1 = $this$toIntOrNull.charAt(0)) < '0') {
            if (length == 1) {
                return null;
            }
            n = 1;
            if (char1 == '-') {
                b = true;
                n2 = Integer.MIN_VALUE;
            }
            else {
                if (char1 != '+') {
                    return null;
                }
                b = false;
                n2 = -2147483647;
            }
        }
        else {
            n = 0;
            b = false;
            n2 = -2147483647;
        }
        int i = 0;
        int index = n;
        final int n3 = length - 1;
        Label_0179: {
            if (index <= n3) {
                int digit;
                while ((digit = Character.digit((int)$this$toIntOrNull.charAt(index), 10)) >= 0) {
                    if (i < -214748364) {
                        return null;
                    }
                    final int n4;
                    if ((n4 = i * 10) < n2 + digit) {
                        return null;
                    }
                    i = n4 - digit;
                    if (index == n3) {
                        break Label_0179;
                    }
                    ++index;
                }
                return null;
            }
        }
        if (b) {
            return i;
        }
        return -i;
    }
    
    public static final Long toLongOrNull(String $this$toLongOrNull) {
        Intrinsics.checkParameterIsNotNull($this$toLongOrNull, "$this$toLongOrNull");
        $this$toLongOrNull = $this$toLongOrNull;
        Intrinsics.checkParameterIsNotNull($this$toLongOrNull, "$this$toLongOrNull");
        CharsKt__CharJVMKt.checkRadix(10);
        final int length;
        if ((length = $this$toLongOrNull.length()) == 0) {
            return null;
        }
        final char char1;
        int n;
        boolean b;
        long n2;
        if ((char1 = $this$toLongOrNull.charAt(0)) < '0') {
            if (length == 1) {
                return null;
            }
            n = 1;
            if (char1 == '-') {
                b = true;
                n2 = Long.MIN_VALUE;
            }
            else {
                if (char1 != '+') {
                    return null;
                }
                b = false;
                n2 = -9223372036854775807L;
            }
        }
        else {
            n = 0;
            b = false;
            n2 = -9223372036854775807L;
        }
        long l = 0L;
        int index = n;
        final int n3 = length - 1;
        Label_0188: {
            if (index <= n3) {
                int digit;
                while ((digit = Character.digit((int)$this$toLongOrNull.charAt(index), 10)) >= 0) {
                    if (l < -922337203685477580L) {
                        return null;
                    }
                    final long n4;
                    if ((n4 = l * 10L) < n2 + digit) {
                        return null;
                    }
                    l = n4 - digit;
                    if (index == n3) {
                        break Label_0188;
                    }
                    ++index;
                }
                return null;
            }
        }
        if (b) {
            return l;
        }
        return -l;
    }
    
    public static boolean regionMatches(final String $this$regionMatches, final int thisOffset, final String other, final int otherOffset, final int length, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$regionMatches, "$this$regionMatches");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if (!ignoreCase) {
            return $this$regionMatches.regionMatches(0, other, otherOffset, length);
        }
        return $this$regionMatches.regionMatches(ignoreCase, 0, other, otherOffset, length);
    }
    
    public static String repeat(final CharSequence $this$repeat, final int n) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "$this$repeat"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: iload_1         /* n */
        //     7: iflt            14
        //    10: iconst_1       
        //    11: goto            15
        //    14: iconst_0       
        //    15: dup            
        //    16: istore_2       
        //    17: ifne            57
        //    20: new             Ljava/lang/StringBuilder;
        //    23: dup            
        //    24: ldc             "Count 'n' must be non-negative, but was "
        //    26: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    29: iload_1         /* n */
        //    30: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    33: bipush          46
        //    35: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //    38: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    41: astore_1        /* n */
        //    42: new             Ljava/lang/IllegalArgumentException;
        //    45: dup            
        //    46: aload_1         /* n */
        //    47: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    50: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //    53: checkcast       Ljava/lang/Throwable;
        //    56: athrow         
        //    57: iload_1         /* n */
        //    58: tableswitch {
        //                0: 80
        //                1: 83
        //          default: 88
        //        }
        //    80: ldc             ""
        //    82: areturn        
        //    83: aload_0         /* $this$repeat */
        //    84: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    87: areturn        
        //    88: aload_0         /* $this$repeat */
        //    89: invokeinterface java/lang/CharSequence.length:()I
        //    94: tableswitch {
        //                0: 116
        //                1: 119
        //          default: 168
        //        }
        //   116: ldc             ""
        //   118: areturn        
        //   119: aload_0         /* $this$repeat */
        //   120: iconst_0       
        //   121: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   126: istore_2       
        //   127: iload_1         /* n */
        //   128: dup            
        //   129: istore_0        /* $this$repeat */
        //   130: newarray        C
        //   132: astore_1        /* n */
        //   133: iconst_0       
        //   134: istore_3       
        //   135: iload_3        
        //   136: iload_0         /* $this$repeat */
        //   137: if_icmpge       157
        //   140: aload_1         /* n */
        //   141: iload_3        
        //   142: istore          4
        //   144: dup            
        //   145: astore          5
        //   147: iload           4
        //   149: iload_2        
        //   150: castore        
        //   151: iinc            3, 1
        //   154: goto            135
        //   157: aload_1         /* n */
        //   158: astore_0        /* $this$repeat */
        //   159: new             Ljava/lang/String;
        //   162: dup            
        //   163: aload_0         /* $this$repeat */
        //   164: invokespecial   java/lang/String.<init>:([C)V
        //   167: areturn        
        //   168: new             Ljava/lang/StringBuilder;
        //   171: dup            
        //   172: iload_1         /* n */
        //   173: aload_0         /* $this$repeat */
        //   174: invokeinterface java/lang/CharSequence.length:()I
        //   179: imul           
        //   180: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //   183: astore_2        /* sb */
        //   184: iconst_1       
        //   185: istore_3       
        //   186: iload_1         /* n */
        //   187: istore_1        /* n */
        //   188: iload_1        
        //   189: ifle            209
        //   192: aload_2         /* sb */
        //   193: aload_0         /* $this$repeat */
        //   194: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/CharSequence;)Ljava/lang/StringBuilder;
        //   197: pop            
        //   198: iload_3         /* i */
        //   199: iload_1        
        //   200: if_icmpeq       209
        //   203: iinc            i, 1
        //   206: goto            192
        //   209: aload_2         /* sb */
        //   210: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   213: dup            
        //   214: ldc             "sb.toString()"
        //   216: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   219: areturn        
        //    StackMapTable: 00 0D 0E 40 01 29 F9 00 16 FC 00 02 07 00 0F FC 00 04 01 F9 00 1B FD 00 02 07 00 0F 01 FF 00 0F 00 04 01 07 00 0E 01 01 00 00 FF 00 15 00 02 00 07 00 0E 00 00 FF 00 0A 00 02 07 00 0F 01 00 00 FD 00 17 07 00 16 01 FF 00 10 00 03 00 00 07 00 16 00 00
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
