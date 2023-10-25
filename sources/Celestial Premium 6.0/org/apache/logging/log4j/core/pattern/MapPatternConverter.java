/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import java.util.Map;
import java.util.TreeSet;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.message.MapMessage;

@Plugin(name="MapPatternConverter", category="Converter")
@ConverterKeys(value={"K", "map", "MAP"})
public final class MapPatternConverter
extends LogEventPatternConverter {
    private final String key;

    private MapPatternConverter(String[] options) {
        super(options != null && options.length > 0 ? "MAP{" + options[0] + "}" : "MAP", "map");
        this.key = options != null && options.length > 0 ? options[0] : null;
    }

    public static MapPatternConverter newInstance(String[] options) {
        return new MapPatternConverter(options);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        if (!(event.getMessage() instanceof MapMessage)) {
            return;
        }
        MapMessage msg = (MapMessage)event.getMessage();
        Map<String, String> map = msg.getData();
        if (this.key == null) {
            if (map.size() == 0) {
                toAppendTo.append("{}");
                return;
            }
            StringBuilder sb = new StringBuilder("{");
            TreeSet<String> keys = new TreeSet<String>(map.keySet());
            for (String key : keys) {
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                sb.append(key).append("=").append(map.get(key));
            }
            sb.append("}");
            toAppendTo.append((CharSequence)sb);
        } else {
            String val = map.get(this.key);
            if (val != null) {
                toAppendTo.append(val);
            }
        }
    }
}

