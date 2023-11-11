package me.nyan.flush.event;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Created by Hexeption on 18/12/2016.
 */
@SuppressWarnings("unchecked")
public class ArrayHelper<T> implements Iterable<T> {
    private T[] elements;

    public ArrayHelper() {
        this.elements = (T[]) new Object[0];
    }

    public void add(final T t) {
        if (t != null) {
            final Object[] array = new Object[this.size() + 1];

            for (int i = 0; i < array.length; i++) {
                if (i < size())
                    array[i] = get(i);
                else
                    array[i] = t;
            }

            set((T[]) array);
        }
    }

    public boolean contains(final T t) {
        Object[] array;

        for (int lenght = (array = this.array()).length, i = 0; i < lenght; i++) {
            final T entry = (T) array[i];

            if (entry.equals(t))
                return true;
        }

        return false;
    }

    public void remove(final T t) {
        if (contains(t)) {
            final Object[] array = new Object[size() - 1];
            boolean b = true;

            for (int i = 0; i < size(); i++) {
                if (b && get(i).equals(t))
                    b = false;
                else
                    array[b ? i : (i - 1)] = get(i);
            }

            set((T[]) array);
        }
    }

    public T[] array() {
        return elements;
    }

    public int size() {
        return array().length;
    }

    public void set(final T[] array) {
        elements = array;
    }

    public T get(final int index) {
        return array()[index];
    }

    public void clear() {
        elements = (T[]) new Object[0];
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < ArrayHelper.this.size() && ArrayHelper.this.get(index) != null;
            }

            @Override
            public T next() {
                return ArrayHelper.this.get(index++);
            }

            @Override
            public void remove() {
                ArrayHelper.this.remove(ArrayHelper.this.get(index));
            }
        };
    }
}