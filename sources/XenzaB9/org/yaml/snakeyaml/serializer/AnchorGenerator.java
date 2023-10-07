// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.serializer;

import org.yaml.snakeyaml.nodes.Node;

public interface AnchorGenerator
{
    String nextAnchor(final Node p0);
}
