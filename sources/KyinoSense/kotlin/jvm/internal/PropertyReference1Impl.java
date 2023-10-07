/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.PropertyReference1;
import kotlin.reflect.KDeclarationContainer;

public class PropertyReference1Impl
extends PropertyReference1 {
    private final KDeclarationContainer owner;
    private final String name;
    private final String signature;

    public PropertyReference1Impl(KDeclarationContainer owner, String name, String signature) {
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

    public Object get(Object receiver) {
        return this.getGetter().call(receiver);
    }
}

