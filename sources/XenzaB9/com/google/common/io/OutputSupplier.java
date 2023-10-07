// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.io;

import java.io.IOException;

@Deprecated
public interface OutputSupplier<T>
{
    T getOutput() throws IOException;
}
