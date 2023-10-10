/*
 * Decompiled with CFR 0.152.
 */
package javassist;

import javassist.ClassPath;

final class ClassPathList {
    ClassPathList next;
    ClassPath path;

    ClassPathList(ClassPath p2, ClassPathList n2) {
        this.next = n2;
        this.path = p2;
    }
}

