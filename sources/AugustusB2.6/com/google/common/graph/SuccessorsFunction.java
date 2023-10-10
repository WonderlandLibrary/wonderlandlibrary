// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Implement with a lambda, or use GraphBuilder to build a Graph with the desired edges")
@ElementTypesAreNonnullByDefault
@Beta
public interface SuccessorsFunction<N>
{
    Iterable<? extends N> successors(final N p0);
}
