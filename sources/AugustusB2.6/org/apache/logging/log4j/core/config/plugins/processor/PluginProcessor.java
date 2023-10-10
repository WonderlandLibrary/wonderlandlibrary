// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.processor;

import java.util.ArrayList;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import javax.lang.model.util.SimpleElementVisitor7;
import java.io.IOException;
import java.io.OutputStream;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.util.Map;
import java.util.Iterator;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.util.Elements;
import java.util.Collection;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import javax.tools.Diagnostic;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import javax.lang.model.SourceVersion;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.AbstractProcessor;

@SupportedAnnotationTypes({ "org.apache.logging.log4j.core.config.plugins.*" })
public class PluginProcessor extends AbstractProcessor
{
    public static final String PLUGIN_CACHE_FILE = "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat";
    private final PluginCache pluginCache;
    
    public PluginProcessor() {
        this.pluginCache = new PluginCache();
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
    
    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        final Messager messager = this.processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Processing Log4j annotations");
        try {
            final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Plugin.class);
            if (elements.isEmpty()) {
                messager.printMessage(Diagnostic.Kind.NOTE, "No elements to process");
                return false;
            }
            this.collectPlugins(elements);
            this.writeCacheFile((Element[])elements.toArray(new Element[elements.size()]));
            messager.printMessage(Diagnostic.Kind.NOTE, "Annotations processed");
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            this.error(ex.getMessage());
            return false;
        }
    }
    
    private void error(final CharSequence message) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }
    
    private void collectPlugins(final Iterable<? extends Element> elements) {
        final Elements elementUtils = this.processingEnv.getElementUtils();
        final ElementVisitor<PluginEntry, Plugin> pluginVisitor = new PluginElementVisitor(elementUtils);
        final ElementVisitor<Collection<PluginEntry>, Plugin> pluginAliasesVisitor = new PluginAliasesElementVisitor(elementUtils);
        for (final Element element : elements) {
            final Plugin plugin = element.getAnnotation(Plugin.class);
            if (plugin == null) {
                continue;
            }
            final PluginEntry entry = element.accept(pluginVisitor, plugin);
            final Map<String, PluginEntry> category = this.pluginCache.getCategory(entry.getCategory());
            category.put(entry.getKey(), entry);
            final Collection<PluginEntry> entries = element.accept(pluginAliasesVisitor, plugin);
            for (final PluginEntry pluginEntry : entries) {
                category.put(pluginEntry.getKey(), pluginEntry);
            }
        }
    }
    
    private void writeCacheFile(final Element... elements) throws IOException {
        final FileObject fileObject = this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat", elements);
        try (final OutputStream out = fileObject.openOutputStream()) {
            this.pluginCache.writeCache(out);
        }
    }
    
    private static class PluginElementVisitor extends SimpleElementVisitor7<PluginEntry, Plugin>
    {
        private final Elements elements;
        
        private PluginElementVisitor(final Elements elements) {
            this.elements = elements;
        }
        
        @Override
        public PluginEntry visitType(final TypeElement e, final Plugin plugin) {
            Objects.requireNonNull(plugin, "Plugin annotation is null.");
            final PluginEntry entry = new PluginEntry();
            entry.setKey(plugin.name().toLowerCase(Locale.US));
            entry.setClassName(this.elements.getBinaryName(e).toString());
            entry.setName("".equals(plugin.elementType()) ? plugin.name() : plugin.elementType());
            entry.setPrintable(plugin.printObject());
            entry.setDefer(plugin.deferChildren());
            entry.setCategory(plugin.category());
            return entry;
        }
    }
    
    private static class PluginAliasesElementVisitor extends SimpleElementVisitor7<Collection<PluginEntry>, Plugin>
    {
        private final Elements elements;
        
        private PluginAliasesElementVisitor(final Elements elements) {
            super(Collections.emptyList());
            this.elements = elements;
        }
        
        @Override
        public Collection<PluginEntry> visitType(final TypeElement e, final Plugin plugin) {
            final PluginAliases aliases = e.getAnnotation(PluginAliases.class);
            if (aliases == null) {
                return (Collection<PluginEntry>)this.DEFAULT_VALUE;
            }
            final Collection<PluginEntry> entries = new ArrayList<PluginEntry>(aliases.value().length);
            for (final String alias : aliases.value()) {
                final PluginEntry entry = new PluginEntry();
                entry.setKey(alias.toLowerCase(Locale.US));
                entry.setClassName(this.elements.getBinaryName(e).toString());
                entry.setName("".equals(plugin.elementType()) ? alias : plugin.elementType());
                entry.setPrintable(plugin.printObject());
                entry.setDefer(plugin.deferChildren());
                entry.setCategory(plugin.category());
                entries.add(entry);
            }
            return entries;
        }
    }
}
