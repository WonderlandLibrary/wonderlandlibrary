/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.TreeTraverser;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.Consumer;

@Beta
@GwtCompatible
public abstract class BinaryTreeTraverser<T>
extends TreeTraverser<T> {
    public abstract Optional<T> leftChild(T var1);

    public abstract Optional<T> rightChild(T var1);

    @Override
    public final Iterable<T> children(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>(){

            @Override
            public Iterator<T> iterator() {
                return new AbstractIterator<T>(){
                    boolean doneLeft;
                    boolean doneRight;

                    @Override
                    protected T computeNext() {
                        if (!this.doneLeft) {
                            this.doneLeft = true;
                            Optional<Object> left = BinaryTreeTraverser.this.leftChild(root);
                            if (left.isPresent()) {
                                return left.get();
                            }
                        }
                        if (!this.doneRight) {
                            this.doneRight = true;
                            Optional<Object> right = BinaryTreeTraverser.this.rightChild(root);
                            if (right.isPresent()) {
                                return right.get();
                            }
                        }
                        return this.endOfData();
                    }
                };
            }

            @Override
            public void forEach(Consumer<? super T> action) {
                BinaryTreeTraverser.acceptIfPresent(action, BinaryTreeTraverser.this.leftChild(root));
                BinaryTreeTraverser.acceptIfPresent(action, BinaryTreeTraverser.this.rightChild(root));
            }
        };
    }

    @Override
    UnmodifiableIterator<T> preOrderIterator(T root) {
        return new PreOrderIterator(root);
    }

    @Override
    UnmodifiableIterator<T> postOrderIterator(T root) {
        return new PostOrderIterator(root);
    }

    public final FluentIterable<T> inOrderTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>(){

            @Override
            public UnmodifiableIterator<T> iterator() {
                return new InOrderIterator(root);
            }

            @Override
            public void forEach(final Consumer<? super T> action) {
                Preconditions.checkNotNull(action);
                new Consumer<T>(){

                    @Override
                    public void accept(T t) {
                        BinaryTreeTraverser.acceptIfPresent(this, BinaryTreeTraverser.this.leftChild(t));
                        action.accept(t);
                        BinaryTreeTraverser.acceptIfPresent(this, BinaryTreeTraverser.this.rightChild(t));
                    }
                }.accept(root);
            }
        };
    }

    private static <T> void pushIfPresent(Deque<T> stack, Optional<T> node) {
        if (node.isPresent()) {
            stack.addLast(node.get());
        }
    }

    private static <T> void acceptIfPresent(Consumer<? super T> action, Optional<T> node) {
        if (node.isPresent()) {
            action.accept(node.get());
        }
    }

    private final class InOrderIterator
    extends AbstractIterator<T> {
        private final Deque<T> stack = new ArrayDeque(8);
        private final BitSet hasExpandedLeft = new BitSet();

        InOrderIterator(T root) {
            this.stack.addLast(root);
        }

        @Override
        protected T computeNext() {
            while (!this.stack.isEmpty()) {
                Object node = this.stack.getLast();
                if (this.hasExpandedLeft.get(this.stack.size() - 1)) {
                    this.stack.removeLast();
                    this.hasExpandedLeft.clear(this.stack.size());
                    BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(node));
                    return node;
                }
                this.hasExpandedLeft.set(this.stack.size() - 1);
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(node));
            }
            return this.endOfData();
        }
    }

    private final class PostOrderIterator
    extends UnmodifiableIterator<T> {
        private final Deque<T> stack = new ArrayDeque(8);
        private final BitSet hasExpanded;

        PostOrderIterator(T root) {
            this.stack.addLast(root);
            this.hasExpanded = new BitSet();
        }

        @Override
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        @Override
        public T next() {
            while (true) {
                Object node = this.stack.getLast();
                boolean expandedNode = this.hasExpanded.get(this.stack.size() - 1);
                if (expandedNode) {
                    this.stack.removeLast();
                    this.hasExpanded.clear(this.stack.size());
                    return node;
                }
                this.hasExpanded.set(this.stack.size() - 1);
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(node));
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(node));
            }
        }
    }

    private final class PreOrderIterator
    extends UnmodifiableIterator<T>
    implements PeekingIterator<T> {
        private final Deque<T> stack = new ArrayDeque(8);

        PreOrderIterator(T root) {
            this.stack.addLast(root);
        }

        @Override
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        @Override
        public T next() {
            Object result = this.stack.removeLast();
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(result));
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(result));
            return result;
        }

        @Override
        public T peek() {
            return this.stack.getLast();
        }
    }
}

