/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.AccessOrderDeque;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.FrequencySketch;
import com.github.benmanes.caffeine.cache.MpscGrowableArrayQueue;
import com.github.benmanes.caffeine.cache.Node;
import com.github.benmanes.caffeine.cache.WS;

class WSMS<K, V>
extends WS<K, V> {
    long maximum;
    long weightedSize;
    long windowMaximum;
    long windowWeightedSize;
    long mainProtectedMaximum;
    long mainProtectedWeightedSize;
    double stepSize;
    long adjustment;
    int hitsInSample;
    int missesInSample;
    double previousSampleHitRate;
    final FrequencySketch<K> sketch = new FrequencySketch();
    final AccessOrderDeque<Node<K, V>> accessOrderWindowDeque;
    final AccessOrderDeque<Node<K, V>> accessOrderProbationDeque;
    final AccessOrderDeque<Node<K, V>> accessOrderProtectedDeque;
    final MpscGrowableArrayQueue<Runnable> writeBuffer;

    WSMS(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
        super(builder, cacheLoader, async);
        if (builder.hasInitialCapacity()) {
            long capacity = Math.min(builder.getMaximum(), (long)builder.getInitialCapacity());
            this.sketch.ensureCapacity(capacity);
        }
        this.accessOrderWindowDeque = builder.evicts() || builder.expiresAfterAccess() ? new AccessOrderDeque() : null;
        this.accessOrderProbationDeque = new AccessOrderDeque();
        this.accessOrderProtectedDeque = new AccessOrderDeque();
        this.writeBuffer = new MpscGrowableArrayQueue(4, WRITE_BUFFER_MAX);
    }

    @Override
    protected final boolean evicts() {
        return true;
    }

    @Override
    protected final long maximum() {
        return this.maximum;
    }

    @Override
    protected final void setMaximum(long maximum) {
        this.maximum = maximum;
    }

    @Override
    protected final long weightedSize() {
        return this.weightedSize;
    }

    @Override
    protected final void setWeightedSize(long weightedSize) {
        this.weightedSize = weightedSize;
    }

    @Override
    protected final long windowMaximum() {
        return this.windowMaximum;
    }

    @Override
    protected final void setWindowMaximum(long windowMaximum) {
        this.windowMaximum = windowMaximum;
    }

    @Override
    protected final long windowWeightedSize() {
        return this.windowWeightedSize;
    }

    @Override
    protected final void setWindowWeightedSize(long windowWeightedSize) {
        this.windowWeightedSize = windowWeightedSize;
    }

    @Override
    protected final long mainProtectedMaximum() {
        return this.mainProtectedMaximum;
    }

    @Override
    protected final void setMainProtectedMaximum(long mainProtectedMaximum) {
        this.mainProtectedMaximum = mainProtectedMaximum;
    }

    @Override
    protected final long mainProtectedWeightedSize() {
        return this.mainProtectedWeightedSize;
    }

    @Override
    protected final void setMainProtectedWeightedSize(long mainProtectedWeightedSize) {
        this.mainProtectedWeightedSize = mainProtectedWeightedSize;
    }

    @Override
    protected final double stepSize() {
        return this.stepSize;
    }

    @Override
    protected final void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }

    @Override
    protected final long adjustment() {
        return this.adjustment;
    }

    @Override
    protected final void setAdjustment(long adjustment) {
        this.adjustment = adjustment;
    }

    @Override
    protected final int hitsInSample() {
        return this.hitsInSample;
    }

    @Override
    protected final void setHitsInSample(int hitsInSample) {
        this.hitsInSample = hitsInSample;
    }

    @Override
    protected final int missesInSample() {
        return this.missesInSample;
    }

    @Override
    protected final void setMissesInSample(int missesInSample) {
        this.missesInSample = missesInSample;
    }

    @Override
    protected final double previousSampleHitRate() {
        return this.previousSampleHitRate;
    }

    @Override
    protected final void setPreviousSampleHitRate(double previousSampleHitRate) {
        this.previousSampleHitRate = previousSampleHitRate;
    }

    @Override
    protected final FrequencySketch<K> frequencySketch() {
        return this.sketch;
    }

    @Override
    protected final AccessOrderDeque<Node<K, V>> accessOrderWindowDeque() {
        return this.accessOrderWindowDeque;
    }

    @Override
    protected final AccessOrderDeque<Node<K, V>> accessOrderProbationDeque() {
        return this.accessOrderProbationDeque;
    }

    @Override
    protected final AccessOrderDeque<Node<K, V>> accessOrderProtectedDeque() {
        return this.accessOrderProtectedDeque;
    }

    @Override
    protected final MpscGrowableArrayQueue<Runnable> writeBuffer() {
        return this.writeBuffer;
    }

    @Override
    protected final boolean buffersWrites() {
        return true;
    }
}

