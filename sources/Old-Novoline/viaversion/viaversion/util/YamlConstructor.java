package viaversion.viaversion.util;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.concurrent.ConcurrentSkipListMap;

public class YamlConstructor extends SafeConstructor {
    public YamlConstructor() {
        super();
        yamlClassConstructors.put(NodeId.mapping, new ConstructYamlMap());
        yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
    }

    class Map extends ConstructYamlMap {
        @Override
        public Object construct(Node node) {
            Object o = super.construct(node);
            if (o instanceof Map && !(o instanceof ConcurrentSkipListMap)) {
                return new ConcurrentSkipListMap<>((java.util.Map<?, ?>) o);
            }
            return o;
        }
    }

    class ConstructYamlOmap extends Constructor.ConstructYamlOmap {
        public Object construct(Node node) {
            Object o = super.construct(node);
            if (o instanceof Map && !(o instanceof ConcurrentSkipListMap)) {
                return new ConcurrentSkipListMap<>((java.util.Map<?, ?>) o);
            }
            return o;
        }
    }
}
