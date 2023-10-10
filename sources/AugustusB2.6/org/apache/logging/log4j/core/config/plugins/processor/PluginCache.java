// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.processor;

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Enumeration;
import java.io.IOException;
import java.util.Iterator;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.TreeMap;
import java.util.Map;

public class PluginCache
{
    private final Map<String, Map<String, PluginEntry>> categories;
    
    public PluginCache() {
        this.categories = new TreeMap<String, Map<String, PluginEntry>>();
    }
    
    public Map<String, Map<String, PluginEntry>> getAllCategories() {
        return this.categories;
    }
    
    public Map<String, PluginEntry> getCategory(final String category) {
        final String key = category.toLowerCase();
        return this.categories.computeIfAbsent(key, ignored -> new TreeMap());
    }
    
    public void writeCache(final OutputStream os) throws IOException {
        try (final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(os))) {
            out.writeInt(this.categories.size());
            for (final Map.Entry<String, Map<String, PluginEntry>> category : this.categories.entrySet()) {
                out.writeUTF(category.getKey());
                final Map<String, PluginEntry> m = category.getValue();
                out.writeInt(m.size());
                for (final Map.Entry<String, PluginEntry> entry : m.entrySet()) {
                    final PluginEntry plugin = entry.getValue();
                    out.writeUTF(plugin.getKey());
                    out.writeUTF(plugin.getClassName());
                    out.writeUTF(plugin.getName());
                    out.writeBoolean(plugin.isPrintable());
                    out.writeBoolean(plugin.isDefer());
                }
            }
        }
    }
    
    public void loadCacheFiles(final Enumeration<URL> resources) throws IOException {
        this.categories.clear();
        while (resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            try (final DataInputStream in = new DataInputStream(new BufferedInputStream(url.openStream()))) {
                for (int count = in.readInt(), i = 0; i < count; ++i) {
                    final String category = in.readUTF();
                    final Map<String, PluginEntry> m = this.getCategory(category);
                    for (int entries = in.readInt(), j = 0; j < entries; ++j) {
                        final String key = in.readUTF();
                        final String className = in.readUTF();
                        final String name = in.readUTF();
                        final boolean printable = in.readBoolean();
                        final boolean defer = in.readBoolean();
                        final PluginEntry entry;
                        final String className2;
                        final String name2;
                        final boolean printable2;
                        final boolean defer2;
                        final String category2;
                        m.computeIfAbsent(key, k -> {
                            entry = new PluginEntry();
                            entry.setKey(k);
                            entry.setClassName(className2);
                            entry.setName(name2);
                            entry.setPrintable(printable2);
                            entry.setDefer(defer2);
                            entry.setCategory(category2);
                            return entry;
                        });
                    }
                }
            }
        }
    }
    
    public int size() {
        return this.categories.size();
    }
}
