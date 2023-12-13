package optifine;

import java.util.ArrayList;

public class CompactArrayList {
    private final ArrayList<Object> list;
    private final int initialCapacity;
    private final float loadFactor;
    private int countValid;

    public CompactArrayList() {
        this(10, 0.75F);
    }

    public CompactArrayList(int capacity) {
        this(capacity, 0.75F);
    }

    public CompactArrayList(int capacity, float loadFactor) {
        countValid = 0;
        list = new ArrayList<>(capacity);
        initialCapacity = capacity;
        this.loadFactor = loadFactor;
    }

    public void add(int index, Object o) {
        if (o != null) countValid++;
        list.add(index, o);
    }

    public boolean add(Object o) {
        if (o != null) countValid++;
        return list.add(o);
    }

    public Object set(int index, Object o) {
        Object object = list.set(index, o);

        if (o != object) {
            if (object == null) countValid++;
            if (o == null) countValid--;
        }

        return object;
    }

    public Object remove(int index) {
        Object object = list.remove(index);
        if (object != null) countValid--;
        return object;
    }

    public void clear() {
        list.clear();
        countValid = 0;
    }

    public void compact() {
        if (countValid <= 0 && list.size() <= 0) {
            clear();
            return;
        }

        if (list.size() > initialCapacity) {
            float f = (float) countValid / (float) list.size();

            if (f <= loadFactor) {
                int i = 0;

                for (int j = 0; j < list.size(); ++j) {
                    Object object = list.get(j);
                    if (object != null) {
                        if (j != i)
                            list.set(i, object);

                        ++i;
                    }
                }

                if (list.size() > i)
                    list.subList(i, list.size()).clear();
            }
        }
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public int getCountValid() {
        return countValid;
    }
}
