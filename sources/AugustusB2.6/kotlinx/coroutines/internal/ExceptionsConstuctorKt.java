// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import java.lang.reflect.Field;
import com.badlogic.gdx.graphics.Pixmap;
import java.lang.reflect.Modifier;
import kotlin.Result;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function1;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ExceptionsConstuctorKt
{
    private static final int throwableFields;
    private static final ReentrantReadWriteLock cacheLock;
    private static final WeakHashMap<Class<? extends Throwable>, Function1<Throwable, Throwable>> exceptionCtors;
    
    public static final <E extends Throwable> E tryCopyException(final E exception) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "exception"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkParameterIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_0         /* exception */
        //     7: instanceof      Lkotlinx/coroutines/CopyableThrowable;
        //    10: ifeq            66
        //    13: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //    16: pop            
        //    17: aload_0         /* exception */
        //    18: checkcast       Lkotlinx/coroutines/CopyableThrowable;
        //    21: invokeinterface kotlinx/coroutines/CopyableThrowable.createCopy:()Ljava/lang/Throwable;
        //    26: dup            
        //    27: astore_2       
        //    28: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //    31: astore_2       
        //    32: goto            48
        //    35: astore_2       
        //    36: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //    39: pop            
        //    40: aload_2        
        //    41: invokestatic    com/badlogic/gdx/graphics/Pixmap.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //    44: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //    47: astore_2       
        //    48: aload_2        
        //    49: dup            
        //    50: astore_1       
        //    51: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //    54: ifeq            61
        //    57: aconst_null    
        //    58: goto            62
        //    61: aload_1        
        //    62: checkcast       Ljava/lang/Throwable;
        //    65: areturn        
        //    66: getstatic       kotlinx/coroutines/internal/ExceptionsConstuctorKt.cacheLock:Ljava/util/concurrent/locks/ReentrantReadWriteLock;
        //    69: dup            
        //    70: astore_1       
        //    71: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock.readLock:()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
        //    74: dup            
        //    75: astore_2       
        //    76: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock.lock:()V
        //    79: getstatic       kotlinx/coroutines/internal/ExceptionsConstuctorKt.exceptionCtors:Ljava/util/WeakHashMap;
        //    82: aload_0         /* exception */
        //    83: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    86: invokevirtual   java/util/WeakHashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    89: checkcast       Lkotlin/jvm/functions/Function1;
        //    92: astore_3       
        //    93: aload_2        
        //    94: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock.unlock:()V
        //    97: aload_3        
        //    98: goto            108
        //   101: astore_3       
        //   102: aload_2        
        //   103: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock.unlock:()V
        //   106: aload_3        
        //   107: athrow         
        //   108: dup            
        //   109: ifnull          126
        //   112: dup            
        //   113: astore_1       
        //   114: dup            
        //   115: astore_3       
        //   116: aload_0         /* exception */
        //   117: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   122: checkcast       Ljava/lang/Throwable;
        //   125: areturn        
        //   126: pop            
        //   127: getstatic       kotlinx/coroutines/internal/ExceptionsConstuctorKt.throwableFields:I
        //   130: aload_0         /* exception */
        //   131: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   134: iconst_0       
        //   135: invokestatic    kotlinx/coroutines/internal/ExceptionsConstuctorKt.fieldsCountOrDefault:(Ljava/lang/Class;I)I
        //   138: if_icmpeq       284
        //   141: getstatic       kotlinx/coroutines/internal/ExceptionsConstuctorKt.cacheLock:Ljava/util/concurrent/locks/ReentrantReadWriteLock;
        //   144: dup            
        //   145: astore_1       
        //   146: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock.readLock:()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
        //   149: astore_2       
        //   150: aload_1        
        //   151: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock.getWriteHoldCount:()I
        //   154: ifne            164
        //   157: aload_1        
        //   158: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock.getReadHoldCount:()I
        //   161: goto            165
        //   164: iconst_0       
        //   165: istore_3       
        //   166: iconst_0       
        //   167: istore          5
        //   169: iload_3        
        //   170: istore          6
        //   172: iload           5
        //   174: iload           6
        //   176: if_icmpge       189
        //   179: aload_2        
        //   180: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock.unlock:()V
        //   183: iinc            5, 1
        //   186: goto            172
        //   189: aload_1        
        //   190: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock.writeLock:()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
        //   193: dup            
        //   194: astore          4
        //   196: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock.lock:()V
        //   199: getstatic       kotlinx/coroutines/internal/ExceptionsConstuctorKt.exceptionCtors:Ljava/util/WeakHashMap;
        //   202: checkcast       Ljava/util/Map;
        //   205: aload_0         /* exception */
        //   206: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   209: getstatic       kotlinx/coroutines/internal/ExceptionsConstuctorKt$tryCopyException$4$1.INSTANCE:Lkotlinx/coroutines/internal/ExceptionsConstuctorKt$tryCopyException$4$1;
        //   212: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   217: pop            
        //   218: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   221: pop            
        //   222: iconst_0       
        //   223: istore          7
        //   225: iload_3        
        //   226: istore_3       
        //   227: iload           7
        //   229: iload_3        
        //   230: if_icmpge       243
        //   233: aload_2        
        //   234: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock.lock:()V
        //   237: iinc            7, 1
        //   240: goto            227
        //   243: aload           4
        //   245: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock.unlock:()V
        //   248: goto            282
        //   251: astore          5
        //   253: iconst_0       
        //   254: istore          7
        //   256: iload_3        
        //   257: istore_3       
        //   258: iload           7
        //   260: iload_3        
        //   261: if_icmpge       274
        //   264: aload_2        
        //   265: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock.lock:()V
        //   268: iinc            7, 1
        //   271: goto            258
        //   274: aload           4
        //   276: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock.unlock:()V
        //   279: aload           5
        //   281: athrow         
        //   282: aconst_null    
        //   283: areturn        
        //   284: aconst_null    
        //   285: astore_1        /* ctor */
        //   286: aload_0         /* exception */
        //   287: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   290: invokevirtual   java/lang/Class.getConstructors:()[Ljava/lang/reflect/Constructor;
        //   293: dup            
        //   294: ldc             "exception.javaClass.constructors"
        //   296: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   299: dup            
        //   300: astore_2       
        //   301: astore          4
        //   303: new             Lkotlinx/coroutines/internal/ExceptionsConstuctorKt$tryCopyException$$inlined$sortedByDescending$1;
        //   306: dup            
        //   307: invokespecial   kotlinx/coroutines/internal/ExceptionsConstuctorKt$tryCopyException$$inlined$sortedByDescending$1.<init>:()V
        //   310: checkcast       Ljava/util/Comparator;
        //   313: astore          6
        //   315: aload           4
        //   317: aload           6
        //   319: invokestatic    kotlin/collections/ArraysKt.sortedWith:([Ljava/lang/Object;Ljava/util/Comparator;)Ljava/util/List;
        //   322: dup            
        //   323: astore_2       
        //   324: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   329: astore_3       
        //   330: aload_3        
        //   331: invokeinterface java/util/Iterator.hasNext:()Z
        //   336: ifeq            509
        //   339: aload_3        
        //   340: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   345: checkcast       Ljava/lang/reflect/Constructor;
        //   348: dup            
        //   349: astore_2       
        //   350: dup            
        //   351: ldc             "constructor"
        //   353: invokestatic    kotlin/jvm/internal/Intrinsics.checkExpressionValueIsNotNull:(Ljava/lang/Object;Ljava/lang/String;)V
        //   356: dup            
        //   357: astore_1        /* ctor */
        //   358: invokevirtual   java/lang/reflect/Constructor.getParameterTypes:()[Ljava/lang/Class;
        //   361: dup            
        //   362: astore_2       
        //   363: arraylength    
        //   364: tableswitch {
        //                0: 486
        //                1: 432
        //                2: 392
        //          default: 500
        //        }
        //   392: aload_2        
        //   393: iconst_0       
        //   394: aaload         
        //   395: ldc             Ljava/lang/String;.class
        //   397: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   400: ifeq            428
        //   403: aload_2        
        //   404: iconst_1       
        //   405: aaload         
        //   406: ldc             Ljava/lang/Throwable;.class
        //   408: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   411: ifeq            428
        //   414: new             Lkotlinx/coroutines/internal/ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$1;
        //   417: dup            
        //   418: aload_1         /* ctor */
        //   419: invokespecial   kotlinx/coroutines/internal/ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$1.<init>:(Ljava/lang/reflect/Constructor;)V
        //   422: checkcast       Lkotlin/jvm/functions/Function1;
        //   425: goto            501
        //   428: aconst_null    
        //   429: goto            501
        //   432: aload_2        
        //   433: iconst_0       
        //   434: aaload         
        //   435: dup            
        //   436: astore_2       
        //   437: ldc             Ljava/lang/Throwable;.class
        //   439: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   442: ifeq            459
        //   445: new             Lkotlinx/coroutines/internal/ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$2;
        //   448: dup            
        //   449: aload_1         /* ctor */
        //   450: invokespecial   kotlinx/coroutines/internal/ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$2.<init>:(Ljava/lang/reflect/Constructor;)V
        //   453: checkcast       Lkotlin/jvm/functions/Function1;
        //   456: goto            501
        //   459: aload_2        
        //   460: ldc             Ljava/lang/String;.class
        //   462: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   465: ifeq            482
        //   468: new             Lkotlinx/coroutines/internal/ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$3;
        //   471: dup            
        //   472: aload_1         /* ctor */
        //   473: invokespecial   kotlinx/coroutines/internal/ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$3.<init>:(Ljava/lang/reflect/Constructor;)V
        //   476: checkcast       Lkotlin/jvm/functions/Function1;
        //   479: goto            501
        //   482: aconst_null    
        //   483: goto            501
        //   486: new             Lkotlinx/coroutines/internal/ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$4;
        //   489: dup            
        //   490: aload_1         /* ctor */
        //   491: invokespecial   kotlinx/coroutines/internal/ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$4.<init>:(Ljava/lang/reflect/Constructor;)V
        //   494: checkcast       Lkotlin/jvm/functions/Function1;
        //   497: goto            501
        //   500: aconst_null    
        //   501: dup            
        //   502: astore_1        /* ctor */
        //   503: ifnonnull       509
        //   506: goto            330
        //   509: getstatic       kotlinx/coroutines/internal/ExceptionsConstuctorKt.cacheLock:Ljava/util/concurrent/locks/ReentrantReadWriteLock;
        //   512: dup            
        //   513: astore_2       
        //   514: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock.readLock:()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
        //   517: astore          4
        //   519: aload_2        
        //   520: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock.getWriteHoldCount:()I
        //   523: ifne            533
        //   526: aload_2        
        //   527: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock.getReadHoldCount:()I
        //   530: goto            534
        //   533: iconst_0       
        //   534: istore          5
        //   536: iconst_0       
        //   537: istore          7
        //   539: iload           5
        //   541: istore_3       
        //   542: iload           7
        //   544: iload_3        
        //   545: if_icmpge       559
        //   548: aload           4
        //   550: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock.unlock:()V
        //   553: iinc            7, 1
        //   556: goto            542
        //   559: aload_2        
        //   560: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock.writeLock:()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
        //   563: dup            
        //   564: astore          6
        //   566: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock.lock:()V
        //   569: getstatic       kotlinx/coroutines/internal/ExceptionsConstuctorKt.exceptionCtors:Ljava/util/WeakHashMap;
        //   572: checkcast       Ljava/util/Map;
        //   575: aload_0         /* exception */
        //   576: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   579: aload_1         /* ctor */
        //   580: dup            
        //   581: ifnonnull       591
        //   584: pop            
        //   585: getstatic       kotlinx/coroutines/internal/ExceptionsConstuctorKt$tryCopyException$5$1.INSTANCE:Lkotlinx/coroutines/internal/ExceptionsConstuctorKt$tryCopyException$5$1;
        //   588: checkcast       Lkotlin/jvm/functions/Function1;
        //   591: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   596: pop            
        //   597: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   600: pop            
        //   601: iconst_0       
        //   602: istore_2       
        //   603: iload           5
        //   605: istore_3       
        //   606: iload_2        
        //   607: iload_3        
        //   608: if_icmpge       622
        //   611: aload           4
        //   613: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock.lock:()V
        //   616: iinc            2, 1
        //   619: goto            606
        //   622: aload           6
        //   624: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock.unlock:()V
        //   627: goto            661
        //   630: astore          7
        //   632: iconst_0       
        //   633: istore_2       
        //   634: iload           5
        //   636: istore_3       
        //   637: iload_2        
        //   638: iload_3        
        //   639: if_icmpge       653
        //   642: aload           4
        //   644: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock.lock:()V
        //   647: iinc            2, 1
        //   650: goto            637
        //   653: aload           6
        //   655: invokevirtual   java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock.unlock:()V
        //   658: aload           7
        //   660: athrow         
        //   661: aload_1         /* ctor */
        //   662: dup            
        //   663: ifnull          676
        //   666: aload_0         /* exception */
        //   667: invokeinterface kotlin/jvm/functions/Function1.invoke:(Ljava/lang/Object;)Ljava/lang/Object;
        //   672: checkcast       Ljava/lang/Throwable;
        //   675: areturn        
        //   676: pop            
        //   677: aconst_null    
        //   678: areturn        
        //    Signature:
        //  <E:Ljava/lang/Throwable;>(TE;)TE;
        //    StackMapTable: 00 29 FF 00 23 00 00 00 01 07 00 0F FE 00 0C 00 00 07 00 0D FF 00 0C 00 02 00 07 00 0D 00 00 FF 00 00 00 00 00 01 07 00 0D FC 00 03 07 00 0F FF 00 22 00 03 00 00 07 00 19 00 01 07 00 0F FF 00 06 00 01 07 00 0F 00 01 07 00 1E 51 07 00 1E FD 00 25 07 00 18 07 00 19 40 01 FF 00 06 00 07 07 00 0F 07 00 18 07 00 19 01 00 01 01 00 00 F8 00 10 FF 00 25 00 08 00 00 07 00 19 01 07 00 1A 00 00 01 00 00 FF 00 0F 00 05 00 00 00 00 07 00 1A 00 00 FF 00 07 00 05 00 00 07 00 19 01 07 00 1A 00 01 07 00 0F FE 00 06 07 00 0F 00 01 FF 00 0F 00 06 00 00 00 00 07 00 1A 07 00 0F 00 00 FF 00 07 00 00 00 00 FC 00 01 07 00 0F FE 00 2D 07 00 1E 00 07 00 14 FF 00 3D 00 04 07 00 0F 07 00 10 07 00 07 07 00 14 00 00 FF 00 23 00 04 07 00 0F 00 00 07 00 14 00 00 FF 00 03 00 04 07 00 0F 07 00 10 07 00 07 07 00 14 00 00 FF 00 1A 00 04 07 00 0F 07 00 10 07 00 0A 07 00 14 00 00 FF 00 16 00 04 07 00 0F 00 00 07 00 14 00 00 FF 00 03 00 04 07 00 0F 07 00 10 00 07 00 14 00 00 FF 00 0D 00 04 07 00 0F 00 00 07 00 14 00 00 40 07 00 1E FF 00 07 00 02 07 00 0F 07 00 1E 00 00 FE 00 17 07 00 18 00 07 00 19 40 01 FF 00 07 00 08 07 00 0F 07 00 1E 07 00 18 01 07 00 19 01 00 01 00 00 FF 00 10 00 06 07 00 0F 07 00 1E 07 00 18 00 07 00 19 01 00 00 FF 00 1F 00 07 07 00 0F 07 00 1E 00 00 07 00 19 01 07 00 1A 00 03 07 00 16 07 00 0A 07 00 1E FF 00 0E 00 07 07 00 0F 07 00 1E 01 01 07 00 19 00 07 00 1A 00 00 FF 00 0F 00 07 07 00 0F 07 00 1E 00 00 00 00 07 00 1A 00 00 FF 00 07 00 07 00 00 00 00 07 00 19 01 07 00 1A 00 01 07 00 0F FF 00 06 00 08 00 00 01 01 07 00 19 00 07 00 1A 07 00 0F 00 00 FF 00 0F 00 08 00 00 00 00 00 00 07 00 1A 07 00 0F 00 00 FF 00 07 00 02 07 00 0F 07 00 1E 00 00 FF 00 0E 00 00 00 01 07 00 1E
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  13     32     35     48     Ljava/lang/Throwable;
        //  79     93     101    108    Any
        //  199    222    251    282    Any
        //  569    601    630    661    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.remove(ArrayList.java:535)
        //     at com.strobel.assembler.ir.StackMappingVisitor.pop(StackMappingVisitor.java:267)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:543)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
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
    
    private static final int fieldsCountOrDefault(Class<?> $this$fieldsCountOrDefault, final int defaultValue) {
        Intrinsics.checkParameterIsNotNull($this$fieldsCountOrDefault, "$this$kotlin");
        Reflection.getOrCreateKotlinClass($this$fieldsCountOrDefault);
        Object o;
        try {
            final Result.Companion companion = Result.Companion;
            int n = 0;
            $this$fieldsCountOrDefault = $this$fieldsCountOrDefault;
            int j;
            while (true) {
                final Field[] declaredFields = $this$fieldsCountOrDefault.getDeclaredFields();
                Intrinsics.checkExpressionValueIsNotNull(declaredFields, "declaredFields");
                final Field[] array = declaredFields;
                int n2 = 0;
                Field[] array2;
                for (int length = (array2 = array).length, i = 0; i < length; ++i) {
                    final Field value = array2[i];
                    Intrinsics.checkExpressionValueIsNotNull(value, "it");
                    if (!Modifier.isStatic(value.getModifiers())) {
                        ++n2;
                    }
                }
                j = n + n2;
                final Class<?> superclass = $this$fieldsCountOrDefault.getSuperclass();
                if (superclass == null) {
                    break;
                }
                n = j;
                $this$fieldsCountOrDefault = superclass;
            }
            o = Result.constructor-impl(j);
        }
        catch (Throwable exception) {
            final Result.Companion companion2 = Result.Companion;
            o = Result.constructor-impl(Pixmap.createFailure(exception));
        }
        final Object $this = o;
        final Integer value2 = defaultValue;
        return ((Integer)(Result.isFailure-impl($this) ? value2 : $this)).intValue();
    }
    
    static {
        throwableFields = fieldsCountOrDefault(Throwable.class, -1);
        cacheLock = new ReentrantReadWriteLock();
        exceptionCtors = new WeakHashMap<Class<? extends Throwable>, Function1<Throwable, Throwable>>();
    }
}
