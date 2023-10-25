/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.ManagedResource;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ResourcePool;
import java.lang.ref.WeakReference;

public abstract class BaseResourcePool<T>
implements ResourcePool<T> {
    private static final int FOREVER = 1024;
    private static final int RECENTLY_USEFUL = 100;
    private static final int RECENT = 10;
    private static final Predicate[] stageTesters = new Predicate[6];
    private static final String[] stageReasons = new String[6];
    long managedSize;
    final long origTarget;
    long curTarget;
    final long maxSize;
    final ResourcePool<T> sharedParent;
    private final Thread managerThread;
    private WeakLinkedList<T> resourceHead = new WeakLinkedList();

    protected BaseResourcePool(long l, long l2) {
        this(null, l, l2);
    }

    protected BaseResourcePool(ResourcePool<T> resourcePool) {
        this(resourcePool, resourcePool.target(), resourcePool.max());
    }

    protected BaseResourcePool(ResourcePool<T> resourcePool, long l, long l2) {
        this.sharedParent = resourcePool;
        this.origTarget = this.curTarget = l;
        this.maxSize = resourcePool == null ? l2 : Math.min(resourcePool.max(), l2);
        this.managerThread = Thread.currentThread();
    }

    /*
     * Exception decompiling
     */
    public boolean cleanup(long var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 6[FORLOOP]
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:429)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:478)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
         * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
         * org.benf.cfr.reader.Main.main(Main.java:49)
         */
        throw new IllegalStateException(Decompilation failed);
    }

    private void pruneLastChance(boolean bl) {
        System.gc();
        if (bl) {
            try {
                Thread.sleep(20L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        Disposer.cleanUp();
        if (PrismSettings.poolDebug) {
            if (bl) {
                System.err.print("Last chance pruning");
            } else {
                System.err.print("Pruning everything");
            }
            System.err.println(" in pool: " + this);
        }
        this.cleanup(managedResource -> true);
    }

    private void cleanup(Predicate predicate) {
        WeakLinkedList<T> weakLinkedList = this.resourceHead;
        WeakLinkedList weakLinkedList2 = weakLinkedList.next;
        while (weakLinkedList2 != null) {
            ManagedResource managedResource = weakLinkedList2.getResource();
            if (ManagedResource._isgone(managedResource)) {
                if (PrismSettings.poolDebug) {
                    BaseResourcePool.showLink("unlinking", weakLinkedList2, false);
                }
                this.recordFree(weakLinkedList2.size);
                weakLinkedList2 = weakLinkedList2.next;
                weakLinkedList.next = weakLinkedList2;
                continue;
            }
            if (!managedResource.isPermanent() && !managedResource.isLocked() && predicate.test(managedResource)) {
                if (PrismSettings.poolDebug) {
                    BaseResourcePool.showLink("pruning", weakLinkedList2, true);
                }
                managedResource.free();
                managedResource.resource = null;
                this.recordFree(weakLinkedList2.size);
                weakLinkedList2 = weakLinkedList2.next;
                weakLinkedList.next = weakLinkedList2;
                continue;
            }
            weakLinkedList = weakLinkedList2;
            weakLinkedList2 = weakLinkedList2.next;
        }
    }

    static void showLink(String string, WeakLinkedList<?> weakLinkedList, boolean bl) {
        ManagedResource<?> managedResource = weakLinkedList.getResource();
        System.err.printf("%s: %s (size=%,d)", string, managedResource, weakLinkedList.size);
        if (managedResource != null) {
            if (bl) {
                System.err.printf(" (age=%d)", managedResource.getAge());
            }
            if (managedResource.isPermanent()) {
                System.err.print(" perm");
            }
            if (managedResource.isLocked()) {
                System.err.print(" lock");
            }
            if (managedResource.isInteresting()) {
                System.err.print(" int");
            }
        }
        System.err.println();
    }

    @Override
    public void freeDisposalRequestedAndCheckResources(boolean bl) {
        boolean bl2 = false;
        WeakLinkedList<T> weakLinkedList = this.resourceHead;
        WeakLinkedList weakLinkedList2 = weakLinkedList.next;
        while (weakLinkedList2 != null) {
            ManagedResource managedResource = weakLinkedList2.getResource();
            if (ManagedResource._isgone(managedResource)) {
                this.recordFree(weakLinkedList2.size);
                weakLinkedList2 = weakLinkedList2.next;
                weakLinkedList.next = weakLinkedList2;
                continue;
            }
            if (!managedResource.isPermanent()) {
                if (managedResource.isLocked() && !managedResource.wasMismatched()) {
                    if (bl) {
                        managedResource.unlockall();
                    } else {
                        managedResource.setMismatched();
                        bl2 = true;
                    }
                }
                managedResource.bumpAge(1024);
            }
            weakLinkedList = weakLinkedList2;
            weakLinkedList2 = weakLinkedList2.next;
        }
        if (PrismSettings.poolStats || bl2) {
            if (bl2) {
                System.err.println("Outstanding resource locks detected:");
            }
            this.printSummary(true);
            System.err.println();
        }
    }

    static String commas(long l) {
        return String.format("%,d", l);
    }

    public void printSummary(boolean bl) {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        long l = 0L;
        int n7 = 0;
        boolean bl2 = false;
        double d = (double)this.used() * 100.0 / (double)this.max();
        double d2 = (double)this.target() * 100.0 / (double)this.max();
        System.err.printf("%s: %,d used (%.1f%%), %,d target (%.1f%%), %,d max\n", this, this.used(), d, this.target(), d2, this.max());
        WeakLinkedList weakLinkedList = this.resourceHead.next;
        while (weakLinkedList != null) {
            ManagedResource managedResource = weakLinkedList.getResource();
            ++n7;
            if (managedResource == null || !managedResource.isValid() || managedResource.isDisposalRequested()) {
                ++n;
            } else {
                int n8 = managedResource.getAge();
                l += (long)n8;
                if (n8 >= 1024) {
                    ++n6;
                }
                if (managedResource.wasMismatched()) {
                    ++n5;
                }
                if (managedResource.isPermanent()) {
                    ++n3;
                } else if (managedResource.isLocked()) {
                    ++n2;
                    if (bl2 && bl) {
                        for (Throwable throwable : managedResource.lockedFrom) {
                            throwable.printStackTrace(System.err);
                        }
                        managedResource.lockedFrom.clear();
                    }
                }
                if (managedResource.isInteresting()) {
                    ++n4;
                }
            }
            weakLinkedList = weakLinkedList.next;
        }
        double d3 = (double)l / (double)n7;
        System.err.println(n7 + " total resources being managed");
        System.err.printf("average resource age is %.1f frames\n", d3);
        BaseResourcePool.printpoolpercent(n6, n7, "at maximum supported age");
        BaseResourcePool.printpoolpercent(n3, n7, "marked permanent");
        BaseResourcePool.printpoolpercent(n5, n7, "have had mismatched locks");
        BaseResourcePool.printpoolpercent(n2, n7, "locked");
        BaseResourcePool.printpoolpercent(n4, n7, "contain interesting data");
        BaseResourcePool.printpoolpercent(n, n7, "disappeared");
    }

    private static void printpoolpercent(int n, int n2, String string) {
        double d = (double)n * 100.0 / (double)n2;
        System.err.printf("%,d resources %s (%.1f%%)\n", n, string, d);
    }

    @Override
    public boolean isManagerThread() {
        return Thread.currentThread() == this.managerThread;
    }

    @Override
    public final long managed() {
        return this.managedSize;
    }

    @Override
    public long used() {
        if (this.sharedParent != null) {
            return this.sharedParent.used();
        }
        return this.managedSize;
    }

    @Override
    public final long max() {
        return this.maxSize;
    }

    @Override
    public final long origTarget() {
        return this.origTarget;
    }

    @Override
    public final long target() {
        return this.curTarget;
    }

    @Override
    public final void setTarget(long l) {
        if (l > this.maxSize) {
            throw new IllegalArgumentException("New target " + l + " larger than max " + this.maxSize);
        }
        if (l < this.origTarget) {
            throw new IllegalArgumentException("New target " + l + " smaller than initial target " + this.origTarget);
        }
        this.curTarget = l;
    }

    @Override
    public boolean prepareForAllocation(long l) {
        return this.cleanup(l);
    }

    @Override
    public final void recordAllocated(long l) {
        this.managedSize += l;
    }

    @Override
    public final void resourceManaged(ManagedResource<T> managedResource) {
        long l = this.size(managedResource.resource);
        this.resourceHead.insert(managedResource, l);
        this.recordAllocated(l);
    }

    @Override
    public final void resourceFreed(ManagedResource<T> managedResource) {
        WeakLinkedList<T> weakLinkedList = this.resourceHead;
        WeakLinkedList weakLinkedList2 = weakLinkedList.next;
        while (weakLinkedList2 != null) {
            ManagedResource managedResource2 = weakLinkedList2.getResource();
            if (managedResource2 == null || managedResource2 == managedResource) {
                this.recordFree(weakLinkedList2.size);
                weakLinkedList2 = weakLinkedList2.next;
                weakLinkedList.next = weakLinkedList2;
                if (managedResource2 != managedResource) continue;
                return;
            }
            weakLinkedList = weakLinkedList2;
            weakLinkedList2 = weakLinkedList2.next;
        }
        if (PrismSettings.poolDebug) {
            System.err.println("Warning: unmanaged resource " + managedResource + " freed from pool: " + this);
        }
    }

    @Override
    public final void recordFree(long l) {
        this.managedSize -= l;
        if (this.managedSize < 0L) {
            throw new IllegalStateException("Negative resource amount");
        }
    }

    private static /* synthetic */ boolean lambda$cleanup$6(ManagedResource managedResource) {
        return false;
    }

    static {
        BaseResourcePool.stageTesters[0] = managedResource -> !managedResource.isInteresting() && managedResource.getAge() > 1024;
        BaseResourcePool.stageReasons[0] = "Pruning unuseful older than 1024";
        BaseResourcePool.stageTesters[1] = managedResource -> !managedResource.isInteresting() && managedResource.getAge() > 512;
        BaseResourcePool.stageReasons[1] = "Pruning unuseful older than 512";
        BaseResourcePool.stageTesters[2] = managedResource -> !managedResource.isInteresting() && managedResource.getAge() > 10;
        BaseResourcePool.stageReasons[2] = "Pruning unuseful older than 10";
        BaseResourcePool.stageTesters[3] = managedResource -> managedResource.getAge() > 1024;
        BaseResourcePool.stageReasons[3] = "Pruning all older than 1024";
        BaseResourcePool.stageTesters[4] = managedResource -> managedResource.getAge() > 512;
        BaseResourcePool.stageReasons[4] = "Pruning all older than 512";
        BaseResourcePool.stageTesters[5] = managedResource -> managedResource.getAge() > 100;
        BaseResourcePool.stageReasons[5] = "Pruning all older than 100";
    }

    static class WeakLinkedList<T> {
        final WeakReference<ManagedResource<T>> theResourceRef;
        final long size;
        WeakLinkedList<T> next;

        WeakLinkedList() {
            this.theResourceRef = null;
            this.size = 0L;
        }

        WeakLinkedList(ManagedResource<T> managedResource, long l, WeakLinkedList<T> weakLinkedList) {
            this.theResourceRef = new WeakReference<ManagedResource<ManagedResource<T>>>(managedResource);
            this.size = l;
            this.next = weakLinkedList;
        }

        void insert(ManagedResource<T> managedResource, long l) {
            this.next = new WeakLinkedList<T>(managedResource, l, this.next);
        }

        ManagedResource<T> getResource() {
            return (ManagedResource)this.theResourceRef.get();
        }
    }

    static interface Predicate {
        public boolean test(ManagedResource<?> var1);
    }
}

