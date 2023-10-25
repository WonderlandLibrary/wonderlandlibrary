/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.util;

import java.util.concurrent.ConcurrentSkipListMap;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlConstructor
extends SafeConstructor {
    public YamlConstructor() {
        this.yamlClassConstructors.put(NodeId.mapping, new SafeConstructor.ConstructYamlMap());
        this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
    }

    class ConstructYamlOmap
    extends SafeConstructor.ConstructYamlOmap {
        ConstructYamlOmap() {
        }

        @Override
        public Object construct(Node node) {
            Object o = super.construct(node);
            if (o instanceof Map && !(o instanceof ConcurrentSkipListMap)) {
                return new ConcurrentSkipListMap((java.util.Map)o);
            }
            return o;
        }
    }

    class Map
    extends SafeConstructor.ConstructYamlMap {
        Map() {
        }

        @Override
        public Object construct(Node node) {
            Object o = super.construct(node);
            if (o instanceof Map && !(o instanceof ConcurrentSkipListMap)) {
                return new ConcurrentSkipListMap((java.util.Map)o);
            }
            return o;
        }
    }
}

