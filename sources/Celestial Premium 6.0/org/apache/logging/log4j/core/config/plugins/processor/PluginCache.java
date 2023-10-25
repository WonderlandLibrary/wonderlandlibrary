/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.processor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.core.config.plugins.processor.PluginEntry;

public class PluginCache {
    private final Map<String, Map<String, PluginEntry>> categories = new TreeMap<String, Map<String, PluginEntry>>();

    public Map<String, Map<String, PluginEntry>> getAllCategories() {
        return this.categories;
    }

    public Map<String, PluginEntry> getCategory(String category) {
        String key = category.toLowerCase();
        return this.categories.computeIfAbsent(key, ignored -> new TreeMap());
    }

    public void writeCache(OutputStream os) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(os));){
            out.writeInt(this.categories.size());
            for (Map.Entry<String, Map<String, PluginEntry>> category : this.categories.entrySet()) {
                out.writeUTF(category.getKey());
                Map<String, PluginEntry> m = category.getValue();
                out.writeInt(m.size());
                for (Map.Entry<String, PluginEntry> entry : m.entrySet()) {
                    PluginEntry plugin = entry.getValue();
                    out.writeUTF(plugin.getKey());
                    out.writeUTF(plugin.getClassName());
                    out.writeUTF(plugin.getName());
                    out.writeBoolean(plugin.isPrintable());
                    out.writeBoolean(plugin.isDefer());
                }
            }
        }
    }

    public void loadCacheFiles(Enumeration<URL> resources) throws IOException {
        this.categories.clear();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            DataInputStream in = new DataInputStream(new BufferedInputStream(url.openStream()));
            Throwable throwable = null;
            try {
                int count = in.readInt();
                for (int i = 0; i < count; ++i) {
                    String category = in.readUTF();
                    Map<String, PluginEntry> m = this.getCategory(category);
                    int entries = in.readInt();
                    for (int j = 0; j < entries; ++j) {
                        String key = in.readUTF();
                        String className = in.readUTF();
                        String name = in.readUTF();
                        boolean printable = in.readBoolean();
                        boolean defer = in.readBoolean();
                        m.computeIfAbsent(key, k -> {
                            PluginEntry entry = new PluginEntry();
                            entry.setKey((String)k);
                            entry.setClassName(className);
                            entry.setName(name);
                            entry.setPrintable(printable);
                            entry.setDefer(defer);
                            entry.setCategory(category);
                            return entry;
                        });
                    }
                }
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
            finally {
                if (in == null) continue;
                if (throwable != null) {
                    try {
                        in.close();
                    }
                    catch (Throwable throwable3) {
                        throwable.addSuppressed(throwable3);
                    }
                    continue;
                }
                in.close();
            }
        }
    }

    public int size() {
        return this.categories.size();
    }
}

