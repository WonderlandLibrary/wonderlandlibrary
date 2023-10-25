package org.apache.logging.log4j.core.config.plugins.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor7;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;

@SupportedAnnotationTypes({"org.apache.logging.log4j.core.config.plugins.*"})
public class PluginProcessor extends AbstractProcessor {
  public static final String PLUGIN_CACHE_FILE = "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat";
  
  private final PluginCache pluginCache = new PluginCache();
  
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }
  
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Messager messager = this.processingEnv.getMessager();
    messager.printMessage(Diagnostic.Kind.NOTE, "Processing Log4j annotations");
    try {
      Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith((Class)Plugin.class);
      if (elements.isEmpty()) {
        messager.printMessage(Diagnostic.Kind.NOTE, "No elements to process");
        return false;
      } 
      collectPlugins(elements);
      writeCacheFile(elements.<Element>toArray(new Element[elements.size()]));
      messager.printMessage(Diagnostic.Kind.NOTE, "Annotations processed");
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      error(ex.getMessage());
      return false;
    } 
  }
  
  private void error(CharSequence message) {
    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
  }
  
  private void collectPlugins(Iterable<? extends Element> elements) {
    Elements elementUtils = this.processingEnv.getElementUtils();
    ElementVisitor<PluginEntry, Plugin> pluginVisitor = new PluginElementVisitor(elementUtils);
    ElementVisitor<Collection<PluginEntry>, Plugin> pluginAliasesVisitor = new PluginAliasesElementVisitor(elementUtils);
    for (Element element : elements) {
      Plugin plugin = element.<Plugin>getAnnotation(Plugin.class);
      if (plugin == null)
        continue; 
      PluginEntry entry = element.<PluginEntry, Plugin>accept(pluginVisitor, plugin);
      Map<String, PluginEntry> category = this.pluginCache.getCategory(entry.getCategory());
      category.put(entry.getKey(), entry);
      Collection<PluginEntry> entries = element.<Collection<PluginEntry>, Plugin>accept(pluginAliasesVisitor, plugin);
      for (PluginEntry pluginEntry : entries)
        category.put(pluginEntry.getKey(), pluginEntry); 
    } 
  }
  
  private void writeCacheFile(Element... elements) throws IOException {
    FileObject fileObject = this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat", elements);
    try (OutputStream out = fileObject.openOutputStream()) {
      this.pluginCache.writeCache(out);
    } 
  }
  
  private static class PluginElementVisitor extends SimpleElementVisitor7<PluginEntry, Plugin> {
    private final Elements elements;
    
    private PluginElementVisitor(Elements elements) {
      this.elements = elements;
    }
    
    public PluginEntry visitType(TypeElement e, Plugin plugin) {
      Objects.requireNonNull(plugin, "Plugin annotation is null.");
      PluginEntry entry = new PluginEntry();
      entry.setKey(plugin.name().toLowerCase(Locale.US));
      entry.setClassName(this.elements.getBinaryName(e).toString());
      entry.setName("".equals(plugin.elementType()) ? plugin.name() : plugin.elementType());
      entry.setPrintable(plugin.printObject());
      entry.setDefer(plugin.deferChildren());
      entry.setCategory(plugin.category());
      return entry;
    }
  }
  
  private static class PluginAliasesElementVisitor extends SimpleElementVisitor7<Collection<PluginEntry>, Plugin> {
    private final Elements elements;
    
    private PluginAliasesElementVisitor(Elements elements) {
      super(Collections.emptyList());
      this.elements = elements;
    }
    
    public Collection<PluginEntry> visitType(TypeElement e, Plugin plugin) {
      PluginAliases aliases = e.<PluginAliases>getAnnotation(PluginAliases.class);
      if (aliases == null)
        return this.DEFAULT_VALUE; 
      Collection<PluginEntry> entries = new ArrayList<>((aliases.value()).length);
      for (String alias : aliases.value()) {
        PluginEntry entry = new PluginEntry();
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\processor\PluginProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */