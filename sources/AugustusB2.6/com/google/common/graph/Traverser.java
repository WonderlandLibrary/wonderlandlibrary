// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.collect.AbstractIterator;
import java.util.ArrayDeque;
import javax.annotation.CheckForNull;
import java.util.Objects;
import java.util.Deque;
import java.util.Set;
import java.util.HashSet;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Call forGraph or forTree, passing a lambda or a Graph with the desired edges (built with GraphBuilder)")
@ElementTypesAreNonnullByDefault
@Beta
public abstract class Traverser<N>
{
    private final SuccessorsFunction<N> successorFunction;
    
    private Traverser(final SuccessorsFunction<N> successorFunction) {
        this.successorFunction = Preconditions.checkNotNull(successorFunction);
    }
    
    public static <N> Traverser<N> forGraph(final SuccessorsFunction<N> graph) {
        return new Traverser<N>(graph) {
            @Override
            Traversal<N> newTraversal() {
                return Traversal.inGraph(graph);
            }
        };
    }
    
    public static <N> Traverser<N> forTree(final SuccessorsFunction<N> tree) {
        if (tree instanceof BaseGraph) {
            Preconditions.checkArgument(((BaseGraph)tree).isDirected(), (Object)"Undirected graphs can never be trees.");
        }
        if (tree instanceof Network) {
            Preconditions.checkArgument(((Network)tree).isDirected(), (Object)"Undirected networks can never be trees.");
        }
        return new Traverser<N>(tree) {
            @Override
            Traversal<N> newTraversal() {
                return Traversal.inTree(tree);
            }
        };
    }
    
    public final Iterable<N> breadthFirst(final N startNode) {
        return this.breadthFirst((Iterable<? extends N>)ImmutableSet.of(startNode));
    }
    
    public final Iterable<N> breadthFirst(final Iterable<? extends N> startNodes) {
        final ImmutableSet<N> validated = this.validate(startNodes);
        return new Iterable<N>() {
            @Override
            public Iterator<N> iterator() {
                return Traverser.this.newTraversal().breadthFirst(validated.iterator());
            }
        };
    }
    
    public final Iterable<N> depthFirstPreOrder(final N startNode) {
        return this.depthFirstPreOrder((Iterable<? extends N>)ImmutableSet.of(startNode));
    }
    
    public final Iterable<N> depthFirstPreOrder(final Iterable<? extends N> startNodes) {
        final ImmutableSet<N> validated = this.validate(startNodes);
        return new Iterable<N>() {
            @Override
            public Iterator<N> iterator() {
                return Traverser.this.newTraversal().preOrder(validated.iterator());
            }
        };
    }
    
    public final Iterable<N> depthFirstPostOrder(final N startNode) {
        return this.depthFirstPostOrder((Iterable<? extends N>)ImmutableSet.of(startNode));
    }
    
    public final Iterable<N> depthFirstPostOrder(final Iterable<? extends N> startNodes) {
        final ImmutableSet<N> validated = this.validate(startNodes);
        return new Iterable<N>() {
            @Override
            public Iterator<N> iterator() {
                return Traverser.this.newTraversal().postOrder(validated.iterator());
            }
        };
    }
    
    abstract Traversal<N> newTraversal();
    
    private ImmutableSet<N> validate(final Iterable<? extends N> startNodes) {
        final ImmutableSet<N> copy = ImmutableSet.copyOf(startNodes);
        for (final N node : copy) {
            this.successorFunction.successors(node);
        }
        return copy;
    }
    
    private abstract static class Traversal<N>
    {
        final SuccessorsFunction<N> successorFunction;
        
        Traversal(final SuccessorsFunction<N> successorFunction) {
            this.successorFunction = successorFunction;
        }
        
        static <N> Traversal<N> inGraph(final SuccessorsFunction<N> graph) {
            final Set<N> visited = new HashSet<N>();
            return new Traversal<N>(graph) {
                @CheckForNull
                @Override
                N visitNext(final Deque<Iterator<? extends N>> horizon) {
                    final Iterator<? extends N> top = horizon.getFirst();
                    while (top.hasNext()) {
                        final N element = (N)top.next();
                        Objects.requireNonNull(element);
                        if (visited.add(element)) {
                            return element;
                        }
                    }
                    horizon.removeFirst();
                    return null;
                }
            };
        }
        
        static <N> Traversal<N> inTree(final SuccessorsFunction<N> tree) {
            return new Traversal<N>(tree) {
                @CheckForNull
                @Override
                N visitNext(final Deque<Iterator<? extends N>> horizon) {
                    final Iterator<? extends N> top = horizon.getFirst();
                    if (top.hasNext()) {
                        return Preconditions.checkNotNull((N)top.next());
                    }
                    horizon.removeFirst();
                    return null;
                }
            };
        }
        
        final Iterator<N> breadthFirst(final Iterator<? extends N> startNodes) {
            return this.topDown(startNodes, InsertionOrder.BACK);
        }
        
        final Iterator<N> preOrder(final Iterator<? extends N> startNodes) {
            return this.topDown(startNodes, InsertionOrder.FRONT);
        }
        
        private Iterator<N> topDown(final Iterator<? extends N> startNodes, final InsertionOrder order) {
            final Deque<Iterator<? extends N>> horizon = new ArrayDeque<Iterator<? extends N>>();
            horizon.add(startNodes);
            return new AbstractIterator<N>() {
                @CheckForNull
                @Override
                protected N computeNext() {
                    do {
                        final N next = Traversal.this.visitNext(horizon);
                        if (next != null) {
                            final Iterator<? extends N> successors = Traversal.this.successorFunction.successors(next).iterator();
                            if (successors.hasNext()) {
                                order.insertInto(horizon, successors);
                            }
                            return next;
                        }
                    } while (!horizon.isEmpty());
                    return this.endOfData();
                }
            };
        }
        
        final Iterator<N> postOrder(final Iterator<? extends N> startNodes) {
            final Deque<N> ancestorStack = new ArrayDeque<N>();
            final Deque<Iterator<? extends N>> horizon = new ArrayDeque<Iterator<? extends N>>();
            horizon.add(startNodes);
            return new AbstractIterator<N>() {
                @CheckForNull
                @Override
                protected N computeNext() {
                    for (N next = Traversal.this.visitNext(horizon); next != null; next = Traversal.this.visitNext(horizon)) {
                        final Iterator<? extends N> successors = Traversal.this.successorFunction.successors(next).iterator();
                        if (!successors.hasNext()) {
                            return next;
                        }
                        horizon.addFirst(successors);
                        ancestorStack.push(next);
                    }
                    if (!ancestorStack.isEmpty()) {
                        return ancestorStack.pop();
                    }
                    return this.endOfData();
                }
            };
        }
        
        @CheckForNull
        abstract N visitNext(final Deque<Iterator<? extends N>> p0);
    }
    
    private enum InsertionOrder
    {
        FRONT(0) {
            @Override
             <T> void insertInto(final Deque<T> deque, final T value) {
                deque.addFirst(value);
            }
        }, 
        BACK(1) {
            @Override
             <T> void insertInto(final Deque<T> deque, final T value) {
                deque.addLast(value);
            }
        };
        
        abstract <T> void insertInto(final Deque<T> p0, final T p1);
        
        private static /* synthetic */ InsertionOrder[] $values() {
            return new InsertionOrder[] { InsertionOrder.FRONT, InsertionOrder.BACK };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
