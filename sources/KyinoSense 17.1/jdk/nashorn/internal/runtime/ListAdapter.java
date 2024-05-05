/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.util.AbstractList;
import java.util.Deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.concurrent.Callable;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

public class ListAdapter
extends AbstractList<Object>
implements RandomAccess,
Deque<Object> {
    private static final Callable<MethodHandle> ADD_INVOKER_CREATOR = ListAdapter.invokerCreator(Void.TYPE, Object.class, JSObject.class, Object.class);
    private static final Object PUSH = new Object();
    private static final Object UNSHIFT = new Object();
    private static final Callable<MethodHandle> REMOVE_INVOKER_CREATOR = ListAdapter.invokerCreator(Object.class, Object.class, JSObject.class);
    private static final Object POP = new Object();
    private static final Object SHIFT = new Object();
    private static final Object SPLICE_ADD = new Object();
    private static final Callable<MethodHandle> SPLICE_ADD_INVOKER_CREATOR = ListAdapter.invokerCreator(Void.TYPE, Object.class, JSObject.class, Integer.TYPE, Integer.TYPE, Object.class);
    private static final Object SPLICE_REMOVE = new Object();
    private static final Callable<MethodHandle> SPLICE_REMOVE_INVOKER_CREATOR = ListAdapter.invokerCreator(Void.TYPE, Object.class, JSObject.class, Integer.TYPE, Integer.TYPE);
    final JSObject obj;
    private final Global global;

    ListAdapter(JSObject obj, Global global) {
        if (global == null) {
            throw new IllegalStateException(ECMAErrors.getMessage("list.adapter.null.global", new String[0]));
        }
        this.obj = obj;
        this.global = global;
    }

    public static ListAdapter create(Object obj) {
        Global global = Context.getGlobal();
        return new ListAdapter(ListAdapter.getJSObject(obj, global), global);
    }

    private static JSObject getJSObject(Object obj, Global global) {
        if (obj instanceof ScriptObject) {
            return (JSObject)ScriptObjectMirror.wrap(obj, global);
        }
        if (obj instanceof JSObject) {
            return (JSObject)obj;
        }
        throw new IllegalArgumentException("ScriptObject or JSObject expected");
    }

    @Override
    public final Object get(int index) {
        this.checkRange(index);
        return this.getAt(index);
    }

    private Object getAt(int index) {
        return this.obj.getSlot(index);
    }

    @Override
    public Object set(int index, Object element) {
        this.checkRange(index);
        Object prevValue = this.getAt(index);
        this.obj.setSlot(index, element);
        return prevValue;
    }

    private void checkRange(int index) {
        if (index < 0 || index >= this.size()) {
            throw ListAdapter.invalidIndex(index);
        }
    }

    @Override
    public int size() {
        return JSType.toInt32(this.obj.getMember("length"));
    }

    @Override
    public final void push(Object e) {
        this.addFirst(e);
    }

    @Override
    public final boolean add(Object e) {
        this.addLast(e);
        return true;
    }

    @Override
    public final void addFirst(Object e) {
        try {
            this.getDynamicInvoker(UNSHIFT, ADD_INVOKER_CREATOR).invokeExact(this.getFunction("unshift"), this.obj, e);
        }
        catch (Error | RuntimeException ex) {
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public final void addLast(Object e) {
        try {
            this.getDynamicInvoker(PUSH, ADD_INVOKER_CREATOR).invokeExact(this.getFunction("push"), this.obj, e);
        }
        catch (Error | RuntimeException ex) {
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public final void add(int index, Object e) {
        block7: {
            try {
                if (index < 0) {
                    throw ListAdapter.invalidIndex(index);
                }
                if (index == 0) {
                    this.addFirst(e);
                    break block7;
                }
                int size = this.size();
                if (index < size) {
                    this.getDynamicInvoker(SPLICE_ADD, SPLICE_ADD_INVOKER_CREATOR).invokeExact(this.obj.getMember("splice"), this.obj, index, 0, e);
                    break block7;
                }
                if (index == size) {
                    this.addLast(e);
                    break block7;
                }
                throw ListAdapter.invalidIndex(index);
            }
            catch (Error | RuntimeException ex) {
                throw ex;
            }
            catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    private Object getFunction(String name) {
        Object fn = this.obj.getMember(name);
        if (!Bootstrap.isCallable(fn)) {
            throw new UnsupportedOperationException("The script object doesn't have a function named " + name);
        }
        return fn;
    }

    private static IndexOutOfBoundsException invalidIndex(int index) {
        return new IndexOutOfBoundsException(String.valueOf(index));
    }

    @Override
    public final boolean offer(Object e) {
        return this.offerLast(e);
    }

    @Override
    public final boolean offerFirst(Object e) {
        this.addFirst(e);
        return true;
    }

    @Override
    public final boolean offerLast(Object e) {
        this.addLast(e);
        return true;
    }

    @Override
    public final Object pop() {
        return this.removeFirst();
    }

    @Override
    public final Object remove() {
        return this.removeFirst();
    }

    @Override
    public final Object removeFirst() {
        this.checkNonEmpty();
        return this.invokeShift();
    }

    @Override
    public final Object removeLast() {
        this.checkNonEmpty();
        return this.invokePop();
    }

    private void checkNonEmpty() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public final Object remove(int index) {
        if (index < 0) {
            throw ListAdapter.invalidIndex(index);
        }
        if (index == 0) {
            return this.invokeShift();
        }
        int maxIndex = this.size() - 1;
        if (index < maxIndex) {
            Object prevValue = this.get(index);
            this.invokeSpliceRemove(index, 1);
            return prevValue;
        }
        if (index == maxIndex) {
            return this.invokePop();
        }
        throw ListAdapter.invalidIndex(index);
    }

    private Object invokeShift() {
        try {
            return this.getDynamicInvoker(SHIFT, REMOVE_INVOKER_CREATOR).invokeExact(this.getFunction("shift"), this.obj);
        }
        catch (Error | RuntimeException ex) {
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private Object invokePop() {
        try {
            return this.getDynamicInvoker(POP, REMOVE_INVOKER_CREATOR).invokeExact(this.getFunction("pop"), this.obj);
        }
        catch (Error | RuntimeException ex) {
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    protected final void removeRange(int fromIndex, int toIndex) {
        this.invokeSpliceRemove(fromIndex, toIndex - fromIndex);
    }

    private void invokeSpliceRemove(int fromIndex, int count) {
        try {
            this.getDynamicInvoker(SPLICE_REMOVE, SPLICE_REMOVE_INVOKER_CREATOR).invokeExact(this.getFunction("splice"), this.obj, fromIndex, count);
        }
        catch (Error | RuntimeException ex) {
            throw ex;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public final Object poll() {
        return this.pollFirst();
    }

    @Override
    public final Object pollFirst() {
        return this.isEmpty() ? null : this.invokeShift();
    }

    @Override
    public final Object pollLast() {
        return this.isEmpty() ? null : this.invokePop();
    }

    @Override
    public final Object peek() {
        return this.peekFirst();
    }

    @Override
    public final Object peekFirst() {
        return this.isEmpty() ? null : this.get(0);
    }

    @Override
    public final Object peekLast() {
        return this.isEmpty() ? null : this.get(this.size() - 1);
    }

    @Override
    public final Object element() {
        return this.getFirst();
    }

    @Override
    public final Object getFirst() {
        this.checkNonEmpty();
        return this.get(0);
    }

    @Override
    public final Object getLast() {
        this.checkNonEmpty();
        return this.get(this.size() - 1);
    }

    @Override
    public final Iterator<Object> descendingIterator() {
        final ListIterator it = this.listIterator(this.size());
        return new Iterator<Object>(){

            @Override
            public boolean hasNext() {
                return it.hasPrevious();
            }

            @Override
            public Object next() {
                return it.previous();
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }

    @Override
    public final boolean removeFirstOccurrence(Object o) {
        return ListAdapter.removeOccurrence(o, this.iterator());
    }

    @Override
    public final boolean removeLastOccurrence(Object o) {
        return ListAdapter.removeOccurrence(o, this.descendingIterator());
    }

    private static boolean removeOccurrence(Object o, Iterator<Object> it) {
        while (it.hasNext()) {
            if (!Objects.equals(o, it.next())) continue;
            it.remove();
            return true;
        }
        return false;
    }

    private static Callable<MethodHandle> invokerCreator(final Class<?> rtype, final Class<?> ... ptypes) {
        return new Callable<MethodHandle>(){

            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", rtype, ptypes);
            }
        };
    }

    private MethodHandle getDynamicInvoker(Object key, Callable<MethodHandle> creator) {
        return this.global.getDynamicInvoker(key, creator);
    }
}

