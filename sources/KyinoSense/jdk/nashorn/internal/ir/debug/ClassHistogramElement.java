/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir.debug;

import java.util.Comparator;

public final class ClassHistogramElement {
    public static final Comparator<ClassHistogramElement> COMPARE_INSTANCES = new Comparator<ClassHistogramElement>(){

        @Override
        public int compare(ClassHistogramElement o1, ClassHistogramElement o2) {
            return (int)Math.abs(o1.instances - o2.instances);
        }
    };
    public static final Comparator<ClassHistogramElement> COMPARE_BYTES = new Comparator<ClassHistogramElement>(){

        @Override
        public int compare(ClassHistogramElement o1, ClassHistogramElement o2) {
            return (int)Math.abs(o1.bytes - o2.bytes);
        }
    };
    public static final Comparator<ClassHistogramElement> COMPARE_CLASSNAMES = new Comparator<ClassHistogramElement>(){

        @Override
        public int compare(ClassHistogramElement o1, ClassHistogramElement o2) {
            return o1.clazz.getCanonicalName().compareTo(o2.clazz.getCanonicalName());
        }
    };
    private final Class<?> clazz;
    private long instances;
    private long bytes;

    public ClassHistogramElement(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void addInstance(long sizeInBytes) {
        ++this.instances;
        this.bytes += sizeInBytes;
    }

    public long getBytes() {
        return this.bytes;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public long getInstances() {
        return this.instances;
    }

    public String toString() {
        return "ClassHistogramElement[class=" + this.clazz.getCanonicalName() + ", instances=" + this.instances + ", bytes=" + this.bytes + "]";
    }
}

