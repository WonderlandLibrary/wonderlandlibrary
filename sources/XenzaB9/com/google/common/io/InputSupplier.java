// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.io;

import java.io.IOException;

@Deprecated
public interface InputSupplier<T>
{
    T getInput() throws IOException;
}
