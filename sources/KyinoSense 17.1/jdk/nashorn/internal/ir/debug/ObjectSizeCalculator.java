/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir.debug;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jdk.nashorn.internal.ir.debug.ClassHistogramElement;

public final class ObjectSizeCalculator {
    private final int arrayHeaderSize;
    private final int objectHeaderSize;
    private final int objectPadding;
    private final int referenceSize;
    private final int superclassFieldPadding;
    private final Map<Class<?>, ClassSizeInfo> classSizeInfos = new IdentityHashMap();
    private final Map<Object, Object> alreadyVisited = new IdentityHashMap<Object, Object>();
    private final Map<Class<?>, ClassHistogramElement> histogram = new IdentityHashMap();
    private final Deque<Object> pending = new ArrayDeque<Object>(16384);
    private long size;
    static Class<?> managementFactory = null;
    static Class<?> memoryPoolMXBean = null;
    static Class<?> memoryUsage = null;
    static Method getMemoryPoolMXBeans = null;
    static Method getUsage = null;
    static Method getMax = null;

    public static long getObjectSize(Object obj) throws UnsupportedOperationException {
        return obj == null ? 0L : new ObjectSizeCalculator(CurrentLayout.SPEC).calculateObjectSize(obj);
    }

    public ObjectSizeCalculator(MemoryLayoutSpecification memoryLayoutSpecification) {
        Objects.requireNonNull(memoryLayoutSpecification);
        this.arrayHeaderSize = memoryLayoutSpecification.getArrayHeaderSize();
        this.objectHeaderSize = memoryLayoutSpecification.getObjectHeaderSize();
        this.objectPadding = memoryLayoutSpecification.getObjectPadding();
        this.referenceSize = memoryLayoutSpecification.getReferenceSize();
        this.superclassFieldPadding = memoryLayoutSpecification.getSuperclassFieldPadding();
    }

    public synchronized long calculateObjectSize(Object obj) {
        this.histogram.clear();
        try {
            Object o = obj;
            while (true) {
                this.visit(o);
                if (this.pending.isEmpty()) {
                    long l = this.size;
                    return l;
                }
                o = this.pending.removeFirst();
            }
        }
        finally {
            this.alreadyVisited.clear();
            this.pending.clear();
            this.size = 0L;
        }
    }

    public List<ClassHistogramElement> getClassHistogram() {
        return new ArrayList<ClassHistogramElement>(this.histogram.values());
    }

    private ClassSizeInfo getClassSizeInfo(Class<?> clazz) {
        ClassSizeInfo csi = this.classSizeInfos.get(clazz);
        if (csi == null) {
            csi = new ClassSizeInfo(clazz);
            this.classSizeInfos.put(clazz, csi);
        }
        return csi;
    }

    private void visit(Object obj) {
        if (this.alreadyVisited.containsKey(obj)) {
            return;
        }
        Class<?> clazz = obj.getClass();
        if (clazz == ArrayElementsVisitor.class) {
            ((ArrayElementsVisitor)obj).visit(this);
        } else {
            this.alreadyVisited.put(obj, obj);
            if (clazz.isArray()) {
                this.visitArray(obj);
            } else {
                this.getClassSizeInfo(clazz).visit(obj, this);
            }
        }
    }

    private void visitArray(Object array) {
        Class<?> arrayClass = array.getClass();
        Class<?> componentType = arrayClass.getComponentType();
        int length = Array.getLength(array);
        if (componentType.isPrimitive()) {
            this.increaseByArraySize(arrayClass, length, ObjectSizeCalculator.getPrimitiveFieldSize(componentType));
        } else {
            this.increaseByArraySize(arrayClass, length, this.referenceSize);
            switch (length) {
                case 0: {
                    break;
                }
                case 1: {
                    this.enqueue(Array.get(array, 0));
                    break;
                }
                default: {
                    this.enqueue(new ArrayElementsVisitor((Object[])array));
                }
            }
        }
    }

    private void increaseByArraySize(Class<?> clazz, int length, long elementSize) {
        this.increaseSize(clazz, ObjectSizeCalculator.roundTo((long)this.arrayHeaderSize + (long)length * elementSize, this.objectPadding));
    }

    void enqueue(Object obj) {
        if (obj != null) {
            this.pending.addLast(obj);
        }
    }

    void increaseSize(Class<?> clazz, long objectSize) {
        ClassHistogramElement he = this.histogram.get(clazz);
        if (he == null) {
            he = new ClassHistogramElement(clazz);
            this.histogram.put(clazz, he);
        }
        he.addInstance(objectSize);
        this.size += objectSize;
    }

    static long roundTo(long x, int multiple) {
        return (x + (long)multiple - 1L) / (long)multiple * (long)multiple;
    }

    private static long getPrimitiveFieldSize(Class<?> type) {
        if (type == Boolean.TYPE || type == Byte.TYPE) {
            return 1L;
        }
        if (type == Character.TYPE || type == Short.TYPE) {
            return 2L;
        }
        if (type == Integer.TYPE || type == Float.TYPE) {
            return 4L;
        }
        if (type == Long.TYPE || type == Double.TYPE) {
            return 8L;
        }
        throw new AssertionError((Object)("Encountered unexpected primitive type " + type.getName()));
    }

    public static MemoryLayoutSpecification getEffectiveMemoryLayoutSpecification() {
        String vmName = System.getProperty("java.vm.name");
        if (vmName == null || !vmName.startsWith("Java HotSpot(TM) ")) {
            throw new UnsupportedOperationException("ObjectSizeCalculator only supported on HotSpot VM");
        }
        String dataModel = System.getProperty("sun.arch.data.model");
        if ("32".equals(dataModel)) {
            return new MemoryLayoutSpecification(){

                @Override
                public int getArrayHeaderSize() {
                    return 12;
                }

                @Override
                public int getObjectHeaderSize() {
                    return 8;
                }

                @Override
                public int getObjectPadding() {
                    return 8;
                }

                @Override
                public int getReferenceSize() {
                    return 4;
                }

                @Override
                public int getSuperclassFieldPadding() {
                    return 4;
                }
            };
        }
        if (!"64".equals(dataModel)) {
            throw new UnsupportedOperationException("Unrecognized value '" + dataModel + "' of sun.arch.data.model system property");
        }
        String strVmVersion = System.getProperty("java.vm.version");
        int vmVersion = Integer.parseInt(strVmVersion.substring(0, strVmVersion.indexOf(46)));
        if (vmVersion >= 17) {
            long maxMemory = 0L;
            if (getMemoryPoolMXBeans == null) {
                throw new AssertionError((Object)"java.lang.management not available in compact 1");
            }
            try {
                List memoryPoolMXBeans = (List)getMemoryPoolMXBeans.invoke(managementFactory, new Object[0]);
                for (Object mp : memoryPoolMXBeans) {
                    Object usage = getUsage.invoke(mp, new Object[0]);
                    Object max = getMax.invoke(usage, new Object[0]);
                    maxMemory += ((Long)max).longValue();
                }
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new AssertionError((Object)"java.lang.management not available in compact 1");
            }
            if (maxMemory < 0x780000000L) {
                return new MemoryLayoutSpecification(){

                    @Override
                    public int getArrayHeaderSize() {
                        return 16;
                    }

                    @Override
                    public int getObjectHeaderSize() {
                        return 12;
                    }

                    @Override
                    public int getObjectPadding() {
                        return 8;
                    }

                    @Override
                    public int getReferenceSize() {
                        return 4;
                    }

                    @Override
                    public int getSuperclassFieldPadding() {
                        return 4;
                    }
                };
            }
        }
        return new MemoryLayoutSpecification(){

            @Override
            public int getArrayHeaderSize() {
                return 24;
            }

            @Override
            public int getObjectHeaderSize() {
                return 16;
            }

            @Override
            public int getObjectPadding() {
                return 8;
            }

            @Override
            public int getReferenceSize() {
                return 8;
            }

            @Override
            public int getSuperclassFieldPadding() {
                return 8;
            }
        };
    }

    static {
        try {
            managementFactory = Class.forName("java.lang.management.ManagementFactory");
            memoryPoolMXBean = Class.forName("java.lang.management.MemoryPoolMXBean");
            memoryUsage = Class.forName("java.lang.management.MemoryUsage");
            getMemoryPoolMXBeans = managementFactory.getMethod("getMemoryPoolMXBeans", new Class[0]);
            getUsage = memoryPoolMXBean.getMethod("getUsage", new Class[0]);
            getMax = memoryUsage.getMethod("getMax", new Class[0]);
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException exception) {
            // empty catch block
        }
    }

    private class ClassSizeInfo {
        private final long objectSize;
        private final long fieldsSize;
        private final Field[] referenceFields;

        public ClassSizeInfo(Class<?> clazz) {
            long newFieldsSize = 0L;
            LinkedList<Field> newReferenceFields = new LinkedList<Field>();
            for (Field f : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) continue;
                Class<?> type = f.getType();
                if (type.isPrimitive()) {
                    newFieldsSize += ObjectSizeCalculator.getPrimitiveFieldSize(type);
                    continue;
                }
                f.setAccessible(true);
                newReferenceFields.add(f);
                newFieldsSize += (long)ObjectSizeCalculator.this.referenceSize;
            }
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                ClassSizeInfo superClassInfo = ObjectSizeCalculator.this.getClassSizeInfo(superClass);
                newFieldsSize += ObjectSizeCalculator.roundTo(superClassInfo.fieldsSize, ObjectSizeCalculator.this.superclassFieldPadding);
                newReferenceFields.addAll(Arrays.asList(superClassInfo.referenceFields));
            }
            this.fieldsSize = newFieldsSize;
            this.objectSize = ObjectSizeCalculator.roundTo((long)ObjectSizeCalculator.this.objectHeaderSize + newFieldsSize, ObjectSizeCalculator.this.objectPadding);
            this.referenceFields = newReferenceFields.toArray(new Field[newReferenceFields.size()]);
        }

        void visit(Object obj, ObjectSizeCalculator calc) {
            calc.increaseSize(obj.getClass(), this.objectSize);
            this.enqueueReferencedObjects(obj, calc);
        }

        public void enqueueReferencedObjects(Object obj, ObjectSizeCalculator calc) {
            for (Field f : this.referenceFields) {
                try {
                    calc.enqueue(f.get(obj));
                }
                catch (IllegalAccessException e) {
                    AssertionError ae = new AssertionError((Object)("Unexpected denial of access to " + f));
                    ((Throwable)((Object)ae)).initCause(e);
                    throw ae;
                }
            }
        }
    }

    private static class ArrayElementsVisitor {
        private final Object[] array;

        ArrayElementsVisitor(Object[] array) {
            this.array = array;
        }

        public void visit(ObjectSizeCalculator calc) {
            for (Object elem : this.array) {
                if (elem == null) continue;
                calc.visit(elem);
            }
        }
    }

    private static class CurrentLayout {
        private static final MemoryLayoutSpecification SPEC = ObjectSizeCalculator.getEffectiveMemoryLayoutSpecification();

        private CurrentLayout() {
        }
    }

    public static interface MemoryLayoutSpecification {
        public int getArrayHeaderSize();

        public int getObjectHeaderSize();

        public int getObjectPadding();

        public int getReferenceSize();

        public int getSuperclassFieldPadding();
    }
}

