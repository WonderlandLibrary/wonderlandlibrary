/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Ordering;
import com.google.common.math.IntMath;
import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

@Beta
public final class MinMaxPriorityQueue<E>
extends AbstractQueue<E> {
    private final Heap minHeap;
    private final Heap maxHeap;
    @VisibleForTesting
    final int maximumSize;
    private Object[] queue;
    private int size;
    private int modCount;
    private static final int EVEN_POWERS_OF_TWO = 0x55555555;
    private static final int ODD_POWERS_OF_TWO = -1431655766;
    private static final int DEFAULT_CAPACITY = 11;

    public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create() {
        return new Builder(Ordering.natural()).create();
    }

    public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create(Iterable<? extends E> initialContents) {
        return new Builder(Ordering.natural()).create(initialContents);
    }

    public static <B> Builder<B> orderedBy(Comparator<B> comparator) {
        return new Builder(comparator);
    }

    public static Builder<Comparable> expectedSize(int expectedSize) {
        return new Builder(Ordering.natural()).expectedSize(expectedSize);
    }

    public static Builder<Comparable> maximumSize(int maximumSize) {
        return new Builder(Ordering.natural()).maximumSize(maximumSize);
    }

    private MinMaxPriorityQueue(Builder<? super E> builder, int queueSize) {
        Ordering ordering = ((Builder)builder).ordering();
        this.minHeap = new Heap(ordering);
        this.minHeap.otherHeap = this.maxHeap = new Heap(ordering.reverse());
        this.maxHeap.otherHeap = this.minHeap;
        this.maximumSize = ((Builder)builder).maximumSize;
        this.queue = new Object[queueSize];
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean add(E element) {
        this.offer(element);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> newElements) {
        boolean modified = false;
        for (E element : newElements) {
            this.offer(element);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean offer(E element) {
        Preconditions.checkNotNull(element);
        ++this.modCount;
        int insertIndex = this.size++;
        this.growIfNeeded();
        this.heapForIndex(insertIndex).bubbleUp(insertIndex, element);
        return this.size <= this.maximumSize || this.pollLast() != element;
    }

    @Override
    public E poll() {
        return this.isEmpty() ? null : (E)this.removeAndGet(0);
    }

    E elementData(int index) {
        return (E)this.queue[index];
    }

    @Override
    public E peek() {
        return this.isEmpty() ? null : (E)this.elementData(0);
    }

    private int getMaxElementIndex() {
        switch (this.size) {
            case 1: {
                return 0;
            }
            case 2: {
                return 1;
            }
        }
        return this.maxHeap.compareElements(1, 2) <= 0 ? 1 : 2;
    }

    public E pollFirst() {
        return this.poll();
    }

    public E removeFirst() {
        return this.remove();
    }

    public E peekFirst() {
        return this.peek();
    }

    public E pollLast() {
        return this.isEmpty() ? null : (E)this.removeAndGet(this.getMaxElementIndex());
    }

    public E removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeAndGet(this.getMaxElementIndex());
    }

    public E peekLast() {
        return this.isEmpty() ? null : (E)this.elementData(this.getMaxElementIndex());
    }

    @VisibleForTesting
    MoveDesc<E> removeAt(int index) {
        Preconditions.checkPositionIndex(index, this.size);
        ++this.modCount;
        --this.size;
        if (this.size == index) {
            this.queue[this.size] = null;
            return null;
        }
        E actualLastElement = this.elementData(this.size);
        int lastElementAt = this.heapForIndex(this.size).getCorrectLastElement(actualLastElement);
        E toTrickle = this.elementData(this.size);
        this.queue[this.size] = null;
        MoveDesc<E> changes = this.fillHole(index, toTrickle);
        if (lastElementAt < index) {
            if (changes == null) {
                return new MoveDesc<E>(actualLastElement, toTrickle);
            }
            return new MoveDesc<E>(actualLastElement, changes.replaced);
        }
        return changes;
    }

    private MoveDesc<E> fillHole(int index, E toTrickle) {
        int vacated;
        Heap heap = this.heapForIndex(index);
        int bubbledTo = heap.bubbleUpAlternatingLevels(vacated = heap.fillHoleAt(index), toTrickle);
        if (bubbledTo == vacated) {
            return heap.tryCrossOverAndBubbleUp(index, vacated, toTrickle);
        }
        return bubbledTo < index ? new MoveDesc<E>(toTrickle, this.elementData(index)) : null;
    }

    private E removeAndGet(int index) {
        E value = this.elementData(index);
        this.removeAt(index);
        return value;
    }

    private Heap heapForIndex(int i2) {
        return MinMaxPriorityQueue.isEvenLevel(i2) ? this.minHeap : this.maxHeap;
    }

    @VisibleForTesting
    static boolean isEvenLevel(int index) {
        int oneBased = index + 1;
        Preconditions.checkState(oneBased > 0, "negative index");
        return (oneBased & 0x55555555) > (oneBased & 0xAAAAAAAA);
    }

    @VisibleForTesting
    boolean isIntact() {
        for (int i2 = 1; i2 < this.size; ++i2) {
            if (this.heapForIndex(i2).verifyIndex(i2)) continue;
            return false;
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    @Override
    public void clear() {
        for (int i2 = 0; i2 < this.size; ++i2) {
            this.queue[i2] = null;
        }
        this.size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] copyTo = new Object[this.size];
        System.arraycopy(this.queue, 0, copyTo, 0, this.size);
        return copyTo;
    }

    public Comparator<? super E> comparator() {
        return this.minHeap.ordering;
    }

    @VisibleForTesting
    int capacity() {
        return this.queue.length;
    }

    @VisibleForTesting
    static int initialQueueSize(int configuredExpectedSize, int maximumSize, Iterable<?> initialContents) {
        int result;
        int n2 = result = configuredExpectedSize == -1 ? 11 : configuredExpectedSize;
        if (initialContents instanceof Collection) {
            int initialSize = ((Collection)initialContents).size();
            result = Math.max(result, initialSize);
        }
        return MinMaxPriorityQueue.capAtMaximumSize(result, maximumSize);
    }

    private void growIfNeeded() {
        if (this.size > this.queue.length) {
            int newCapacity = this.calculateNewCapacity();
            Object[] newQueue = new Object[newCapacity];
            System.arraycopy(this.queue, 0, newQueue, 0, this.queue.length);
            this.queue = newQueue;
        }
    }

    private int calculateNewCapacity() {
        int oldCapacity = this.queue.length;
        int newCapacity = oldCapacity < 64 ? (oldCapacity + 1) * 2 : IntMath.checkedMultiply(oldCapacity / 2, 3);
        return MinMaxPriorityQueue.capAtMaximumSize(newCapacity, this.maximumSize);
    }

    private static int capAtMaximumSize(int queueSize, int maximumSize) {
        return Math.min(queueSize - 1, maximumSize) + 1;
    }

    private class QueueIterator
    implements Iterator<E> {
        private int cursor = -1;
        private int expectedModCount = MinMaxPriorityQueue.access$700(MinMaxPriorityQueue.this);
        private Queue<E> forgetMeNot;
        private List<E> skipMe;
        private E lastFromForgetMeNot;
        private boolean canRemove;

        private QueueIterator() {
        }

        @Override
        public boolean hasNext() {
            this.checkModCount();
            return this.nextNotInSkipMe(this.cursor + 1) < MinMaxPriorityQueue.this.size() || this.forgetMeNot != null && !this.forgetMeNot.isEmpty();
        }

        @Override
        public E next() {
            this.checkModCount();
            int tempCursor = this.nextNotInSkipMe(this.cursor + 1);
            if (tempCursor < MinMaxPriorityQueue.this.size()) {
                this.cursor = tempCursor;
                this.canRemove = true;
                return MinMaxPriorityQueue.this.elementData(this.cursor);
            }
            if (this.forgetMeNot != null) {
                this.cursor = MinMaxPriorityQueue.this.size();
                this.lastFromForgetMeNot = this.forgetMeNot.poll();
                if (this.lastFromForgetMeNot != null) {
                    this.canRemove = true;
                    return this.lastFromForgetMeNot;
                }
            }
            throw new NoSuchElementException("iterator moved past last element in queue.");
        }

        @Override
        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            this.checkModCount();
            this.canRemove = false;
            ++this.expectedModCount;
            if (this.cursor < MinMaxPriorityQueue.this.size()) {
                MoveDesc moved = MinMaxPriorityQueue.this.removeAt(this.cursor);
                if (moved != null) {
                    if (this.forgetMeNot == null) {
                        this.forgetMeNot = new ArrayDeque();
                        this.skipMe = new ArrayList(3);
                    }
                    this.forgetMeNot.add(moved.toTrickle);
                    this.skipMe.add(moved.replaced);
                }
                --this.cursor;
            } else {
                Preconditions.checkState(this.removeExact(this.lastFromForgetMeNot));
                this.lastFromForgetMeNot = null;
            }
        }

        private boolean containsExact(Iterable<E> elements, E target) {
            for (Object element : elements) {
                if (element != target) continue;
                return true;
            }
            return false;
        }

        boolean removeExact(Object target) {
            for (int i2 = 0; i2 < MinMaxPriorityQueue.this.size; ++i2) {
                if (MinMaxPriorityQueue.this.queue[i2] != target) continue;
                MinMaxPriorityQueue.this.removeAt(i2);
                return true;
            }
            return false;
        }

        void checkModCount() {
            if (MinMaxPriorityQueue.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        private int nextNotInSkipMe(int c2) {
            if (this.skipMe != null) {
                while (c2 < MinMaxPriorityQueue.this.size() && this.containsExact(this.skipMe, MinMaxPriorityQueue.this.elementData(c2))) {
                    ++c2;
                }
            }
            return c2;
        }
    }

    private class Heap {
        final Ordering<E> ordering;
        Heap otherHeap;

        Heap(Ordering<E> ordering) {
            this.ordering = ordering;
        }

        int compareElements(int a2, int b2) {
            return this.ordering.compare(MinMaxPriorityQueue.this.elementData(a2), MinMaxPriorityQueue.this.elementData(b2));
        }

        MoveDesc<E> tryCrossOverAndBubbleUp(int removeIndex, int vacated, E toTrickle) {
            int crossOver = this.crossOver(vacated, toTrickle);
            if (crossOver == vacated) {
                return null;
            }
            Object parent = crossOver < removeIndex ? MinMaxPriorityQueue.this.elementData(removeIndex) : MinMaxPriorityQueue.this.elementData(this.getParentIndex(removeIndex));
            if (this.otherHeap.bubbleUpAlternatingLevels(crossOver, toTrickle) < removeIndex) {
                return new MoveDesc(toTrickle, parent);
            }
            return null;
        }

        void bubbleUp(int index, E x2) {
            Heap heap;
            int crossOver = this.crossOverUp(index, x2);
            if (crossOver == index) {
                heap = this;
            } else {
                index = crossOver;
                heap = this.otherHeap;
            }
            heap.bubbleUpAlternatingLevels(index, x2);
        }

        int bubbleUpAlternatingLevels(int index, E x2) {
            int grandParentIndex;
            Object e2;
            while (index > 2 && this.ordering.compare(e2 = MinMaxPriorityQueue.this.elementData(grandParentIndex = this.getGrandparentIndex(index)), x2) > 0) {
                ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[index] = e2;
                index = grandParentIndex;
            }
            ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[index] = x2;
            return index;
        }

        int findMin(int index, int len) {
            if (index >= MinMaxPriorityQueue.this.size) {
                return -1;
            }
            Preconditions.checkState(index > 0);
            int limit = Math.min(index, MinMaxPriorityQueue.this.size - len) + len;
            int minIndex = index;
            for (int i2 = index + 1; i2 < limit; ++i2) {
                if (this.compareElements(i2, minIndex) >= 0) continue;
                minIndex = i2;
            }
            return minIndex;
        }

        int findMinChild(int index) {
            return this.findMin(this.getLeftChildIndex(index), 2);
        }

        int findMinGrandChild(int index) {
            int leftChildIndex = this.getLeftChildIndex(index);
            if (leftChildIndex < 0) {
                return -1;
            }
            return this.findMin(this.getLeftChildIndex(leftChildIndex), 4);
        }

        int crossOverUp(int index, E x2) {
            Object uncleElement;
            int grandparentIndex;
            int uncleIndex;
            if (index == 0) {
                ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[0] = x2;
                return 0;
            }
            int parentIndex = this.getParentIndex(index);
            Object parentElement = MinMaxPriorityQueue.this.elementData(parentIndex);
            if (parentIndex != 0 && (uncleIndex = this.getRightChildIndex(grandparentIndex = this.getParentIndex(parentIndex))) != parentIndex && this.getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size && this.ordering.compare(uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex), parentElement) < 0) {
                parentIndex = uncleIndex;
                parentElement = uncleElement;
            }
            if (this.ordering.compare(parentElement, x2) < 0) {
                ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[index] = parentElement;
                ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[parentIndex] = x2;
                return parentIndex;
            }
            ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[index] = x2;
            return index;
        }

        int getCorrectLastElement(E actualLastElement) {
            Object uncleElement;
            int grandparentIndex;
            int uncleIndex;
            int parentIndex = this.getParentIndex(MinMaxPriorityQueue.this.size);
            if (parentIndex != 0 && (uncleIndex = this.getRightChildIndex(grandparentIndex = this.getParentIndex(parentIndex))) != parentIndex && this.getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size && this.ordering.compare(uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex), actualLastElement) < 0) {
                ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[uncleIndex] = actualLastElement;
                ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[((MinMaxPriorityQueue)MinMaxPriorityQueue.this).size] = uncleElement;
                return uncleIndex;
            }
            return MinMaxPriorityQueue.this.size;
        }

        int crossOver(int index, E x2) {
            int minChildIndex = this.findMinChild(index);
            if (minChildIndex > 0 && this.ordering.compare(MinMaxPriorityQueue.this.elementData(minChildIndex), x2) < 0) {
                ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[index] = MinMaxPriorityQueue.this.elementData(minChildIndex);
                ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[minChildIndex] = x2;
                return minChildIndex;
            }
            return this.crossOverUp(index, x2);
        }

        int fillHoleAt(int index) {
            int minGrandchildIndex;
            while ((minGrandchildIndex = this.findMinGrandChild(index)) > 0) {
                ((MinMaxPriorityQueue)MinMaxPriorityQueue.this).queue[index] = MinMaxPriorityQueue.this.elementData(minGrandchildIndex);
                index = minGrandchildIndex;
            }
            return index;
        }

        private boolean verifyIndex(int i2) {
            if (this.getLeftChildIndex(i2) < MinMaxPriorityQueue.this.size && this.compareElements(i2, this.getLeftChildIndex(i2)) > 0) {
                return false;
            }
            if (this.getRightChildIndex(i2) < MinMaxPriorityQueue.this.size && this.compareElements(i2, this.getRightChildIndex(i2)) > 0) {
                return false;
            }
            if (i2 > 0 && this.compareElements(i2, this.getParentIndex(i2)) > 0) {
                return false;
            }
            return i2 <= 2 || this.compareElements(this.getGrandparentIndex(i2), i2) <= 0;
        }

        private int getLeftChildIndex(int i2) {
            return i2 * 2 + 1;
        }

        private int getRightChildIndex(int i2) {
            return i2 * 2 + 2;
        }

        private int getParentIndex(int i2) {
            return (i2 - 1) / 2;
        }

        private int getGrandparentIndex(int i2) {
            return this.getParentIndex(this.getParentIndex(i2));
        }
    }

    static class MoveDesc<E> {
        final E toTrickle;
        final E replaced;

        MoveDesc(E toTrickle, E replaced) {
            this.toTrickle = toTrickle;
            this.replaced = replaced;
        }
    }

    @Beta
    public static final class Builder<B> {
        private static final int UNSET_EXPECTED_SIZE = -1;
        private final Comparator<B> comparator;
        private int expectedSize = -1;
        private int maximumSize = Integer.MAX_VALUE;

        private Builder(Comparator<B> comparator) {
            this.comparator = Preconditions.checkNotNull(comparator);
        }

        public Builder<B> expectedSize(int expectedSize) {
            Preconditions.checkArgument(expectedSize >= 0);
            this.expectedSize = expectedSize;
            return this;
        }

        public Builder<B> maximumSize(int maximumSize) {
            Preconditions.checkArgument(maximumSize > 0);
            this.maximumSize = maximumSize;
            return this;
        }

        public <T extends B> MinMaxPriorityQueue<T> create() {
            return this.create(Collections.emptySet());
        }

        public <T extends B> MinMaxPriorityQueue<T> create(Iterable<? extends T> initialContents) {
            MinMaxPriorityQueue<T> queue = new MinMaxPriorityQueue<T>(this, MinMaxPriorityQueue.initialQueueSize(this.expectedSize, this.maximumSize, initialContents));
            for (T element : initialContents) {
                queue.offer(element);
            }
            return queue;
        }

        private <T extends B> Ordering<T> ordering() {
            return Ordering.from(this.comparator);
        }
    }
}

