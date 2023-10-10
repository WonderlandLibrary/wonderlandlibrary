// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import me.gong.mcleaks.util.google.common.collect.Maps;
import me.gong.mcleaks.util.google.common.base.Function;
import java.util.Map;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
public abstract class AbstractValueGraph<N, V> extends AbstractGraph<N> implements ValueGraph<N, V>
{
    @Override
    public V edgeValue(final Object nodeU, final Object nodeV) {
        final V value = this.edgeValueOrDefault(nodeU, nodeV, null);
        if (value == null) {
            Preconditions.checkArgument(this.nodes().contains(nodeU), "Node %s is not an element of this graph.", nodeU);
            Preconditions.checkArgument(this.nodes().contains(nodeV), "Node %s is not an element of this graph.", nodeV);
            throw new IllegalArgumentException(String.format("Edge connecting %s to %s is not present in this graph.", nodeU, nodeV));
        }
        return value;
    }
    
    @Override
    public String toString() {
        final String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", this.isDirected(), this.allowsSelfLoops());
        return String.format("%s, nodes: %s, edges: %s", propertiesString, this.nodes(), this.edgeValueMap());
    }
    
    private Map<EndpointPair<N>, V> edgeValueMap() {
        final Function<EndpointPair<N>, V> edgeToValueFn = new Function<EndpointPair<N>, V>() {
            @Override
            public V apply(final EndpointPair<N> edge) {
                return AbstractValueGraph.this.edgeValue(edge.nodeU(), edge.nodeV());
            }
        };
        return Maps.asMap(this.edges(), edgeToValueFn);
    }
}
