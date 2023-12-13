/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

@Beta
@GwtCompatible(emulated=true)
public abstract class TreeTraverser<T> {
    public abstract Iterable<T> children(T var1);

    public final FluentIterable<T> preOrderTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>(){

            @Override
            public UnmodifiableIterator<T> iterator() {
                return TreeTraverser.this.preOrderIterator(root);
            }
        };
    }

    UnmodifiableIterator<T> preOrderIterator(T root) {
        return new PreOrderIterator(root);
    }

    public final FluentIterable<T> postOrderTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>(){

            @Override
            public UnmodifiableIterator<T> iterator() {
                return TreeTraverser.this.postOrderIterator(root);
            }
        };
    }

    UnmodifiableIterator<T> postOrderIterator(T root) {
        return new PostOrderIterator(root);
    }

    public final FluentIterable<T> breadthFirstTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>(){

            @Override
            public UnmodifiableIterator<T> iterator() {
                return new BreadthFirstIterator(root);
            }
        };
    }

    private final class BreadthFirstIterator
    extends UnmodifiableIterator<T>
    implements PeekingIterator<T> {
        private final Queue<T> queue = new ArrayDeque();

        BreadthFirstIterator(T root) {
            this.queue.add(root);
        }

        @Override
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override
        public T peek() {
            return this.queue.element();
        }

        @Override
        public T next() {
            Object result = this.queue.remove();
            Iterables.addAll(this.queue, TreeTraverser.this.children(result));
            return result;
        }
    }

    private final class PostOrderIterator
    extends AbstractIterator<T> {
        private final ArrayDeque<PostOrderNode<T>> stack = new ArrayDeque();

        PostOrderIterator(T root) {
            this.stack.addLast(this.expand(root));
        }

        @Override
        protected T computeNext() {
            while (!this.stack.isEmpty()) {
                PostOrderNode top = this.stack.getLast();
                if (top.childIterator.hasNext()) {
                    Object child = top.childIterator.next();
                    this.stack.addLast(this.expand(child));
                    continue;
                }
                this.stack.removeLast();
                return top.root;
            }
            return this.endOfData();
        }

        private PostOrderNode<T> expand(T t2) {
            return new PostOrderNode(t2, TreeTraverser.this.children(t2).iterator());
        }
    }

    private static final class PostOrderNode<T> {
        final T root;
        final Iterator<T> childIterator;

        PostOrderNode(T root, Iterator<T> childIterator) {
            this.root = Preconditions.checkNotNull(root);
            this.childIterator = Preconditions.checkNotNull(childIterator);
        }
    }

    private final class PreOrderIterator
    extends UnmodifiableIterator<T> {
        private final Deque<Iterator<T>> stack = new ArrayDeque();

        PreOrderIterator(T root) {
            this.stack.addLast(Iterators.singletonIterator(Preconditions.checkNotNull(root)));
        }

        @Override
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        @Override
        public T next() {
            Iterator childItr;
            Iterator itr = this.stack.getLast();
            Object result = Preconditions.checkNotNull(itr.next());
            if (!itr.hasNext()) {
                this.stack.removeLast();
            }
            if ((childItr = TreeTraverser.this.children(result).iterator()).hasNext()) {
                this.stack.addLast(childItr);
            }
            return result;
        }
    }
}

