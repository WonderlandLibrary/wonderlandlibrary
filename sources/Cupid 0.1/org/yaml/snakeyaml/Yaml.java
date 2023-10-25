package org.yaml.snakeyaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.serializer.Serializer;

public class Yaml {
  protected final Resolver resolver;
  
  private String name;
  
  protected BaseConstructor constructor;
  
  protected Representer representer;
  
  protected DumperOptions dumperOptions;
  
  protected LoaderOptions loadingConfig;
  
  public Yaml() {
    this((BaseConstructor)new Constructor(), new Representer(), new DumperOptions(), new LoaderOptions(), new Resolver());
  }
  
  public Yaml(DumperOptions dumperOptions) {
    this((BaseConstructor)new Constructor(), new Representer(dumperOptions), dumperOptions);
  }
  
  public Yaml(LoaderOptions loadingConfig) {
    this((BaseConstructor)new Constructor(loadingConfig), new Representer(), new DumperOptions(), loadingConfig);
  }
  
  public Yaml(Representer representer) {
    this((BaseConstructor)new Constructor(), representer);
  }
  
  public Yaml(BaseConstructor constructor) {
    this(constructor, new Representer());
  }
  
  public Yaml(BaseConstructor constructor, Representer representer) {
    this(constructor, representer, initDumperOptions(representer));
  }
  
  private static DumperOptions initDumperOptions(Representer representer) {
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setDefaultFlowStyle(representer.getDefaultFlowStyle());
    dumperOptions.setDefaultScalarStyle(representer.getDefaultScalarStyle());
    dumperOptions.setAllowReadOnlyProperties(representer.getPropertyUtils().isAllowReadOnlyProperties());
    dumperOptions.setTimeZone(representer.getTimeZone());
    return dumperOptions;
  }
  
  public Yaml(Representer representer, DumperOptions dumperOptions) {
    this((BaseConstructor)new Constructor(), representer, dumperOptions, new LoaderOptions(), new Resolver());
  }
  
  public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions) {
    this(constructor, representer, dumperOptions, new LoaderOptions(), new Resolver());
  }
  
  public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, LoaderOptions loadingConfig) {
    this(constructor, representer, dumperOptions, loadingConfig, new Resolver());
  }
  
  public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, Resolver resolver) {
    this(constructor, representer, dumperOptions, new LoaderOptions(), resolver);
  }
  
  public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, LoaderOptions loadingConfig, Resolver resolver) {
    if (!constructor.isExplicitPropertyUtils()) {
      constructor.setPropertyUtils(representer.getPropertyUtils());
    } else if (!representer.isExplicitPropertyUtils()) {
      representer.setPropertyUtils(constructor.getPropertyUtils());
    } 
    this.constructor = constructor;
    this.constructor.setAllowDuplicateKeys(loadingConfig.isAllowDuplicateKeys());
    this.constructor.setWrappedToRootException(loadingConfig.isWrappedToRootException());
    if (!dumperOptions.getIndentWithIndicator() && dumperOptions.getIndent() <= dumperOptions.getIndicatorIndent())
      throw new YAMLException("Indicator indent must be smaller then indent."); 
    representer.setDefaultFlowStyle(dumperOptions.getDefaultFlowStyle());
    representer.setDefaultScalarStyle(dumperOptions.getDefaultScalarStyle());
    representer.getPropertyUtils().setAllowReadOnlyProperties(dumperOptions.isAllowReadOnlyProperties());
    representer.setTimeZone(dumperOptions.getTimeZone());
    this.representer = representer;
    this.dumperOptions = dumperOptions;
    this.loadingConfig = loadingConfig;
    this.resolver = resolver;
    this.name = "Yaml:" + System.identityHashCode(this);
  }
  
  public String dump(Object data) {
    List<Object> list = new ArrayList(1);
    list.add(data);
    return dumpAll(list.iterator());
  }
  
  public Node represent(Object data) {
    return this.representer.represent(data);
  }
  
  public String dumpAll(Iterator<? extends Object> data) {
    StringWriter buffer = new StringWriter();
    dumpAll(data, buffer, null);
    return buffer.toString();
  }
  
  public void dump(Object data, Writer output) {
    List<Object> list = new ArrayList(1);
    list.add(data);
    dumpAll(list.iterator(), output, null);
  }
  
  public void dumpAll(Iterator<? extends Object> data, Writer output) {
    dumpAll(data, output, null);
  }
  
  private void dumpAll(Iterator<? extends Object> data, Writer output, Tag rootTag) {
    Serializer serializer = new Serializer((Emitable)new Emitter(output, this.dumperOptions), this.resolver, this.dumperOptions, rootTag);
    try {
      serializer.open();
      while (data.hasNext()) {
        Node node = this.representer.represent(data.next());
        serializer.serialize(node);
      } 
      serializer.close();
    } catch (IOException e) {
      throw new YAMLException(e);
    } 
  }
  
  public String dumpAs(Object data, Tag rootTag, DumperOptions.FlowStyle flowStyle) {
    DumperOptions.FlowStyle oldStyle = this.representer.getDefaultFlowStyle();
    if (flowStyle != null)
      this.representer.setDefaultFlowStyle(flowStyle); 
    List<Object> list = new ArrayList(1);
    list.add(data);
    StringWriter buffer = new StringWriter();
    dumpAll(list.iterator(), buffer, rootTag);
    this.representer.setDefaultFlowStyle(oldStyle);
    return buffer.toString();
  }
  
  public String dumpAsMap(Object data) {
    return dumpAs(data, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
  }
  
  public void serialize(Node node, Writer output) {
    Serializer serializer = new Serializer((Emitable)new Emitter(output, this.dumperOptions), this.resolver, this.dumperOptions, null);
    try {
      serializer.open();
      serializer.serialize(node);
      serializer.close();
    } catch (IOException e) {
      throw new YAMLException(e);
    } 
  }
  
  public List<Event> serialize(Node data) {
    SilentEmitter emitter = new SilentEmitter();
    Serializer serializer = new Serializer(emitter, this.resolver, this.dumperOptions, null);
    try {
      serializer.open();
      serializer.serialize(data);
      serializer.close();
    } catch (IOException e) {
      throw new YAMLException(e);
    } 
    return emitter.getEvents();
  }
  
  private static class SilentEmitter implements Emitable {
    private List<Event> events = new ArrayList<>(100);
    
    public List<Event> getEvents() {
      return this.events;
    }
    
    public void emit(Event event) throws IOException {
      this.events.add(event);
    }
    
    private SilentEmitter() {}
  }
  
  public <T> T load(String yaml) {
    return (T)loadFromReader(new StreamReader(yaml), Object.class);
  }
  
  public <T> T load(InputStream io) {
    return (T)loadFromReader(new StreamReader((Reader)new UnicodeReader(io)), Object.class);
  }
  
  public <T> T load(Reader io) {
    return (T)loadFromReader(new StreamReader(io), Object.class);
  }
  
  public <T> T loadAs(Reader io, Class<T> type) {
    return (T)loadFromReader(new StreamReader(io), type);
  }
  
  public <T> T loadAs(String yaml, Class<T> type) {
    return (T)loadFromReader(new StreamReader(yaml), type);
  }
  
  public <T> T loadAs(InputStream input, Class<T> type) {
    return (T)loadFromReader(new StreamReader((Reader)new UnicodeReader(input)), type);
  }
  
  private Object loadFromReader(StreamReader sreader, Class<?> type) {
    Composer composer = new Composer((Parser)new ParserImpl(sreader), this.resolver, this.loadingConfig);
    this.constructor.setComposer(composer);
    return this.constructor.getSingleData(type);
  }
  
  public Iterable<Object> loadAll(Reader yaml) {
    Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver, this.loadingConfig);
    this.constructor.setComposer(composer);
    Iterator<Object> result = new Iterator() {
        public boolean hasNext() {
          return Yaml.this.constructor.checkData();
        }
        
        public Object next() {
          return Yaml.this.constructor.getData();
        }
        
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    return new YamlIterable(result);
  }
  
  private static class YamlIterable implements Iterable<Object> {
    private Iterator<Object> iterator;
    
    public YamlIterable(Iterator<Object> iterator) {
      this.iterator = iterator;
    }
    
    public Iterator<Object> iterator() {
      return this.iterator;
    }
  }
  
  public Iterable<Object> loadAll(String yaml) {
    return loadAll(new StringReader(yaml));
  }
  
  public Iterable<Object> loadAll(InputStream yaml) {
    return loadAll((Reader)new UnicodeReader(yaml));
  }
  
  public Node compose(Reader yaml) {
    Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver, this.loadingConfig);
    return composer.getSingleNode();
  }
  
  public Iterable<Node> composeAll(Reader yaml) {
    final Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver, this.loadingConfig);
    Iterator<Node> result = new Iterator<Node>() {
        public boolean hasNext() {
          return composer.checkNode();
        }
        
        public Node next() {
          Node node = composer.getNode();
          if (node != null)
            return node; 
          throw new NoSuchElementException("No Node is available.");
        }
        
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    return new NodeIterable(result);
  }
  
  private static class NodeIterable implements Iterable<Node> {
    private Iterator<Node> iterator;
    
    public NodeIterable(Iterator<Node> iterator) {
      this.iterator = iterator;
    }
    
    public Iterator<Node> iterator() {
      return this.iterator;
    }
  }
  
  public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
    this.resolver.addImplicitResolver(tag, regexp, first);
  }
  
  public String toString() {
    return this.name;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Iterable<Event> parse(Reader yaml) {
    final ParserImpl parser = new ParserImpl(new StreamReader(yaml));
    Iterator<Event> result = new Iterator<Event>() {
        public boolean hasNext() {
          return (parser.peekEvent() != null);
        }
        
        public Event next() {
          Event event = parser.getEvent();
          if (event != null)
            return event; 
          throw new NoSuchElementException("No Event is available.");
        }
        
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    return new EventIterable(result);
  }
  
  private static class EventIterable implements Iterable<Event> {
    private Iterator<Event> iterator;
    
    public EventIterable(Iterator<Event> iterator) {
      this.iterator = iterator;
    }
    
    public Iterator<Event> iterator() {
      return this.iterator;
    }
  }
  
  public void setBeanAccess(BeanAccess beanAccess) {
    this.constructor.getPropertyUtils().setBeanAccess(beanAccess);
    this.representer.getPropertyUtils().setBeanAccess(beanAccess);
  }
  
  public void addTypeDescription(TypeDescription td) {
    this.constructor.addTypeDescription(td);
    this.representer.addTypeDescription(td);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\Yaml.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */