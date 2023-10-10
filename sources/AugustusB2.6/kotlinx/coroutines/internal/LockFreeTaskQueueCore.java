// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlinx.coroutines.DebugKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public final class LockFreeTaskQueueCore<E>
{
    private final int mask;
    private volatile Object _next;
    private static final AtomicReferenceFieldUpdater _next$FU;
    public volatile /* synthetic */ long _state$internal;
    public static final /* synthetic */ AtomicLongFieldUpdater _state$FU$internal;
    public /* synthetic */ AtomicReferenceArray array$internal;
    private final int capacity;
    private final boolean singleConsumer;
    public static final CoroutineContext.Element.DefaultImpls REMOVE_FROZEN$4fdbb1f;
    public static final Companion Companion;
    
    public final boolean isEmpty() {
        final long $this$withState$iv;
        final int head$iv = (int)(($this$withState$iv = this._state$internal) & 0x3FFFFFFFL);
        final int tail$iv = (int)(($this$withState$iv & 0xFFFFFFFC0000000L) >> 30);
        return head$iv == tail$iv;
    }
    
    public final boolean close() {
        final LockFreeTaskQueueCore $this$update$iv = this;
        long cur$iv;
        while (((cur$iv = $this$update$iv._state$internal) & 0x2000000000000000L) == 0x0L) {
            if ((cur$iv & 0x1000000000000000L) != 0x0L) {
                return false;
            }
            final long upd$iv = cur$iv | 0x2000000000000000L;
            if (!LockFreeTaskQueueCore._state$FU$internal.compareAndSet($this$update$iv, cur$iv, upd$iv)) {
                continue;
            }
            return true;
        }
        return true;
    }
    
    public final int addLast(final E element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        long state;
        while (((state = this._state$internal) & 0x3000000000000000L) == 0x0L) {
            final int head$iv = (int)(state & 0x3FFFFFFFL);
            final int tail$iv = (int)((state & 0xFFFFFFFC0000000L) >> 30);
            final int mask = this.mask;
            if ((tail$iv + 2 & mask) == (head$iv & mask)) {
                return 1;
            }
            if (!this.singleConsumer && this.array$internal.get(tail$iv & mask) != null) {
                if (this.capacity < 1024 || (tail$iv - head$iv & 0x3FFFFFFF) > this.capacity >> 1) {
                    return 1;
                }
                continue;
            }
            else {
                final int newTail = tail$iv + 1 & 0x3FFFFFFF;
                final AtomicLongFieldUpdater state$FU$internal = LockFreeTaskQueueCore._state$FU$internal;
                final long n = state;
                final Companion companion = LockFreeTaskQueueCore.Companion;
                if (state$FU$internal.compareAndSet(this, n, (state & ~0xFFFFFFFC0000000L) | (long)newTail << 30)) {
                    this.array$internal.set(tail$iv & mask, element);
                    LockFreeTaskQueueCore lockFreeTaskQueueCore2;
                    for (LockFreeTaskQueueCore cur = this; (cur._state$internal & 0x1000000000000000L) != 0x0L; cur = lockFreeTaskQueueCore2) {
                        final LockFreeTaskQueueCore next = cur.next();
                        final int n2 = tail$iv;
                        final LockFreeTaskQueueCore lockFreeTaskQueueCore = next;
                        final Object value;
                        LockFreeTaskQueueCore lockFreeTaskQueueCore3;
                        if ((value = next.array$internal.get(n2 & lockFreeTaskQueueCore.mask)) instanceof Placeholder && ((Placeholder)value).index == n2) {
                            lockFreeTaskQueueCore.array$internal.set(n2 & lockFreeTaskQueueCore.mask, element);
                            lockFreeTaskQueueCore2 = (lockFreeTaskQueueCore3 = lockFreeTaskQueueCore);
                        }
                        else {
                            lockFreeTaskQueueCore2 = (lockFreeTaskQueueCore3 = null);
                        }
                        if (lockFreeTaskQueueCore3 == null) {
                            break;
                        }
                    }
                    return 0;
                }
                continue;
            }
        }
        if ((state & 0x2000000000000000L) != 0x0L) {
            return 2;
        }
        return 1;
    }
    
    private final LockFreeTaskQueueCore<E> removeSlowPath(final int oldHead, final int newHead) {
        final LockFreeTaskQueueCore $this$loop$iv = this;
        while (true) {
            final long state;
            final int head$iv = (int)((state = $this$loop$iv._state$internal) & 0x3FFFFFFFL);
            if (DebugKt.getASSERTIONS_ENABLED() && head$iv != oldHead) {
                throw new AssertionError();
            }
            if ((state & 0x1000000000000000L) != 0x0L) {
                return this.next();
            }
            if (LockFreeTaskQueueCore._state$FU$internal.compareAndSet(this, state, LockFreeTaskQueueCore.Companion.updateHead(state, newHead))) {
                this.array$internal.set(head$iv & this.mask, null);
                return null;
            }
        }
    }
    
    public final LockFreeTaskQueueCore<E> next() {
        return this.allocateOrGetNextCopy(this.markFrozen());
    }
    
    private final long markFrozen() {
        final LockFreeTaskQueueCore $this$updateAndGet$iv = this;
        long cur$iv;
        while (((cur$iv = $this$updateAndGet$iv._state$internal) & 0x1000000000000000L) == 0x0L) {
            final long upd$iv = cur$iv | 0x1000000000000000L;
            if (LockFreeTaskQueueCore._state$FU$internal.compareAndSet($this$updateAndGet$iv, cur$iv, upd$iv)) {
                return upd$iv;
            }
        }
        return cur$iv;
    }
    
    private final LockFreeTaskQueueCore<E> allocateOrGetNextCopy(final long state) {
        final LockFreeTaskQueueCore $this$loop$iv = this;
        LockFreeTaskQueueCore next;
        while ((next = (LockFreeTaskQueueCore)$this$loop$iv._next) == null) {
            LockFreeTaskQueueCore._next$FU.compareAndSet(this, null, this.allocateNextCopy(state));
        }
        return next;
    }
    
    private final LockFreeTaskQueueCore<E> allocateNextCopy(final long state) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0         /* this */
        //     5: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore.capacity:I
        //     8: iconst_1       
        //     9: ishl           
        //    10: aload_0         /* this */
        //    11: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore.singleConsumer:Z
        //    14: invokespecial   kotlinx/coroutines/internal/LockFreeTaskQueueCore.<init>:(IZ)V
        //    17: astore_3        /* next */
        //    18: lload_1         /* state */
        //    19: dup2           
        //    20: lstore          $this$withState$iv
        //    22: ldc2_w          1073741823
        //    25: land           
        //    26: l2i            
        //    27: istore          head$iv
        //    29: lload           $this$withState$iv
        //    31: ldc2_w          1152921503533105152
        //    34: land           
        //    35: bipush          30
        //    37: lshr           
        //    38: l2i            
        //    39: istore          tail$iv
        //    41: iload           head$iv
        //    43: iload           tail$iv
        //    45: istore          null
        //    47: dup            
        //    48: istore          5
        //    50: istore          index
        //    52: iload           index
        //    54: aload_0         /* this */
        //    55: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore.mask:I
        //    58: iand           
        //    59: iload           tail
        //    61: aload_0         /* this */
        //    62: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore.mask:I
        //    65: iand           
        //    66: if_icmpeq       121
        //    69: aload_0         /* this */
        //    70: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore.array$internal:Ljava/util/concurrent/atomic/AtomicReferenceArray;
        //    73: iload           index
        //    75: aload_0         /* this */
        //    76: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore.mask:I
        //    79: iand           
        //    80: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.get:(I)Ljava/lang/Object;
        //    83: dup            
        //    84: ifnonnull       97
        //    87: pop            
        //    88: new             Lkotlinx/coroutines/internal/LockFreeTaskQueueCore$Placeholder;
        //    91: dup            
        //    92: iload           index
        //    94: invokespecial   kotlinx/coroutines/internal/LockFreeTaskQueueCore$Placeholder.<init>:(I)V
        //    97: astore          value
        //    99: aload_3         /* next */
        //   100: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore.array$internal:Ljava/util/concurrent/atomic/AtomicReferenceArray;
        //   103: iload           index
        //   105: aload_3         /* next */
        //   106: getfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore.mask:I
        //   109: iand           
        //   110: aload           value
        //   112: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.set:(ILjava/lang/Object;)V
        //   115: iinc            index, 1
        //   118: goto            52
        //   121: aload_3         /* next */
        //   122: lload_1         /* state */
        //   123: ldc2_w          1152921504606846976
        //   126: lstore          14
        //   128: dup2           
        //   129: lstore          12
        //   131: lload           14
        //   133: ldc2_w          -1
        //   136: lxor           
        //   137: land           
        //   138: putfield        kotlinx/coroutines/internal/LockFreeTaskQueueCore._state$internal:J
        //   141: aload_3         /* next */
        //   142: areturn        
        //    Signature:
        //  (J)Lkotlinx/coroutines/internal/LockFreeTaskQueueCore<TE;>;
        //    StackMapTable: 00 03 FE 00 34 07 00 11 01 01 6C 07 00 09 FF 00 17 00 03 00 04 07 00 11 00 00
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
    
    public LockFreeTaskQueueCore(int capacity, final boolean singleConsumer) {
        this.capacity = capacity;
        this.singleConsumer = singleConsumer;
        this.mask = this.capacity - 1;
        this._next = null;
        this._state$internal = 0L;
        this.array$internal = new AtomicReferenceArray(this.capacity);
        if ((capacity = ((this.mask <= 1073741823) ? 1 : 0)) == 0) {
            throw new IllegalStateException("Check failed.".toString());
        }
        if ((capacity = (((this.capacity & this.mask) == 0x0) ? 1 : 0)) == 0) {
            throw new IllegalStateException("Check failed.".toString());
        }
    }
    
    static {
        Companion = new Companion((byte)0);
        REMOVE_FROZEN$4fdbb1f = new CoroutineContext.Element.DefaultImpls("REMOVE_FROZEN");
        _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeTaskQueueCore.class, Object.class, "_next");
        _state$FU$internal = AtomicLongFieldUpdater.newUpdater(LockFreeTaskQueueCore.class, "_state$internal");
    }
    
    public static final class Placeholder
    {
        public final int index;
        
        public Placeholder(final int index) {
            this.index = index;
        }
    }
    
    public static final class Companion
    {
        public final long updateHead(final long $this$updateHead, final int newHead) {
            return ($this$updateHead & ~0x3FFFFFFFL) | (long)newHead;
        }
        
        private Companion() {
        }
        
        public static int getAVAILABLE_PROCESSORS() {
            return SystemPropsKt__SystemPropsKt.getAVAILABLE_PROCESSORS();
        }
        
        public static String systemProp(final String propertyName) {
            return SystemPropsKt__SystemPropsKt.systemProp(propertyName);
        }
        
        public static boolean systemProp(String propertyName, final boolean defaultValue) {
            final boolean b = true;
            propertyName = propertyName;
            Intrinsics.checkParameterIsNotNull(propertyName, "propertyName");
            final String systemProp = SystemPropsKt__SystemPropsKt.systemProp(propertyName = propertyName);
            if (systemProp != null) {
                propertyName = systemProp;
                return Boolean.parseBoolean(systemProp);
            }
            return b;
        }
        
        public static long systemProp(final String propertyName, final long defaultValue, final long minValue, final long maxValue) {
            return SystemPropsKt__SystemProps_commonKt.systemProp(propertyName, defaultValue, minValue, maxValue);
        }
    }
}
