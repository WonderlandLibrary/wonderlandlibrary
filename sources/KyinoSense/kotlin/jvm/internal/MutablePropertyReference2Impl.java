/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.MutablePropertyReference2;
import kotlin.reflect.KDeclarationContainer;

public class MutablePropertyReference2Impl
extends MutablePropertyReference2 {
    private final KDeclarationContainer owner;
    private final String name;
    private final String signature;

    public MutablePropertyReference2Impl(KDeclarationContainer owner, String name, String signature) {
        this.owner = owner;
        this.name = name;
        this.signature = signature;
    }

    @Override
    public KDeclarationContainer getOwner() {
        return this.owner;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSignature() {
        return this.signature;
    }

    @Override
    public Object get(Object receiver1, Object receiver2) {
        return this.getGetter().call(receiver1, receiver2);
    }

    public void set(Object receiver1, Object receiver2, Object value) {
        this.getSetter().call(receiver1, receiver2, value);
    }
}

