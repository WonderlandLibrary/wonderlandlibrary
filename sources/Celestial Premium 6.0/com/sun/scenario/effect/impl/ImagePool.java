/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl;

import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.LockableResource;
import com.sun.scenario.effect.impl.PoolFilterable;
import com.sun.scenario.effect.impl.Renderer;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImagePool {
    public static long numEffects;
    static long numCreated;
    static long pixelsCreated;
    static long numAccessed;
    static long pixelsAccessed;
    static final int QUANT = 32;
    private final List<SoftReference<PoolFilterable>> unlocked = new ArrayList<SoftReference<PoolFilterable>>();
    private final List<SoftReference<PoolFilterable>> locked = new ArrayList<SoftReference<PoolFilterable>>();
    private final boolean usePurgatory = Boolean.getBoolean("decora.purgatory");
    private final List<Filterable> hardPurgatory = new ArrayList<Filterable>();
    private final List<SoftReference<PoolFilterable>> softPurgatory = new ArrayList<SoftReference<PoolFilterable>>();

    static void printStats() {
        System.out.println("effects executed:  " + numEffects);
        System.out.println("images created:    " + numCreated);
        System.out.println("pixels created:    " + pixelsCreated);
        System.out.println("images accessed:   " + numAccessed);
        System.out.println("pixels accessed:   " + pixelsAccessed);
        if (numEffects != 0L) {
            double d = (double)numAccessed / (double)numEffects;
            double d2 = (double)pixelsAccessed / (double)numEffects;
            System.out.println("images per effect: " + d);
            System.out.println("pixels per effect: " + d2);
        }
    }

    ImagePool() {
    }

    public synchronized PoolFilterable checkOut(Renderer renderer, int n, int n2) {
        Filterable filterable;
        SoftReference<PoolFilterable> softReference;
        if (n <= 0 || n2 <= 0) {
            n2 = 1;
            n = 1;
        }
        n = (n + 32 - 1) / 32 * 32;
        n2 = (n2 + 32 - 1) / 32 * 32;
        n = renderer.getCompatibleWidth(n);
        n2 = renderer.getCompatibleHeight(n2);
        ++numAccessed;
        pixelsAccessed += (long)n * (long)n2;
        SoftReference<PoolFilterable> softReference2 = null;
        LockableResource lockableResource = null;
        int n3 = Integer.MAX_VALUE;
        Iterator<SoftReference<PoolFilterable>> iterator = this.unlocked.iterator();
        while (iterator.hasNext()) {
            softReference = iterator.next();
            filterable = softReference.get();
            if (filterable == null) {
                iterator.remove();
                continue;
            }
            int n4 = filterable.getMaxContentWidth();
            int n5 = filterable.getMaxContentHeight();
            if (n4 < n || n5 < n2 || n4 * n5 / 2 > n * n2) continue;
            int n6 = (n4 - n) * (n5 - n2);
            if (softReference2 != null && n6 >= n3) continue;
            filterable.lock();
            if (filterable.isLost()) {
                iterator.remove();
                continue;
            }
            if (lockableResource != null) {
                lockableResource.unlock();
            }
            softReference2 = softReference;
            lockableResource = filterable;
            n3 = n6;
        }
        if (softReference2 != null) {
            this.unlocked.remove(softReference2);
            this.locked.add(softReference2);
            renderer.clearImage((Filterable)lockableResource);
            return lockableResource;
        }
        iterator = this.locked.iterator();
        while (iterator.hasNext()) {
            softReference = iterator.next();
            filterable = softReference.get();
            if (filterable != null) continue;
            iterator.remove();
        }
        softReference = null;
        try {
            softReference = renderer.createCompatibleImage(n, n2);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            // empty catch block
        }
        if (softReference == null) {
            this.pruneCache();
            try {
                softReference = renderer.createCompatibleImage(n, n2);
            }
            catch (OutOfMemoryError outOfMemoryError) {
                // empty catch block
            }
        }
        if (softReference != null) {
            softReference.setImagePool(this);
            this.locked.add(new SoftReference<Object>(softReference));
            ++numCreated;
            pixelsCreated += (long)n * (long)n2;
        }
        return softReference;
    }

    public synchronized void checkIn(PoolFilterable poolFilterable) {
        SoftReference<PoolFilterable> softReference = null;
        Filterable filterable = null;
        Iterator<SoftReference<PoolFilterable>> iterator = this.locked.iterator();
        while (iterator.hasNext()) {
            SoftReference<PoolFilterable> softReference2 = iterator.next();
            Filterable filterable2 = softReference2.get();
            if (filterable2 == null) {
                iterator.remove();
                continue;
            }
            if (filterable2 != poolFilterable) continue;
            softReference = softReference2;
            filterable = filterable2;
            poolFilterable.unlock();
            break;
        }
        if (softReference != null) {
            this.locked.remove(softReference);
            if (this.usePurgatory) {
                this.hardPurgatory.add(filterable);
                this.softPurgatory.add(softReference);
            } else {
                this.unlocked.add(softReference);
            }
        }
    }

    public synchronized void releasePurgatory() {
        if (this.usePurgatory && !this.softPurgatory.isEmpty()) {
            this.unlocked.addAll(this.softPurgatory);
            this.softPurgatory.clear();
            this.hardPurgatory.clear();
        }
    }

    private void pruneCache() {
        for (SoftReference<PoolFilterable> softReference : this.unlocked) {
            Filterable filterable = softReference.get();
            if (filterable == null) continue;
            filterable.flush();
        }
        this.unlocked.clear();
        System.gc();
        System.runFinalization();
        System.gc();
        System.runFinalization();
    }

    public synchronized void dispose() {
        for (SoftReference<PoolFilterable> softReference : this.unlocked) {
            Filterable filterable = softReference.get();
            if (filterable == null) continue;
            filterable.flush();
        }
        this.unlocked.clear();
        this.locked.clear();
    }

    static {
        Object object = AccessController.doPrivileged(() -> {
            if (System.getProperty("decora.showstats") != null) {
                Runtime.getRuntime().addShutdownHook(new Thread(){

                    @Override
                    public void run() {
                        ImagePool.printStats();
                    }
                });
            }
            return null;
        });
    }
}

