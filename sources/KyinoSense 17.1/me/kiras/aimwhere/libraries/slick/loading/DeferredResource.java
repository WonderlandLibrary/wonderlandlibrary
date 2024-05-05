/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.loading;

import java.io.IOException;

public interface DeferredResource {
    public void load() throws IOException;

    public String getDescription();
}

