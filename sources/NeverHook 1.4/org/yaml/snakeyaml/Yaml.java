/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.composer.Composer;
/*     */ import org.yaml.snakeyaml.constructor.BaseConstructor;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.emitter.Emitable;
/*     */ import org.yaml.snakeyaml.emitter.Emitter;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.parser.Parser;
/*     */ import org.yaml.snakeyaml.parser.ParserImpl;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ import org.yaml.snakeyaml.reader.UnicodeReader;
/*     */ import org.yaml.snakeyaml.representer.Representer;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
/*     */ import org.yaml.snakeyaml.serializer.Serializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Yaml
/*     */ {
/*     */   protected final Resolver resolver;
/*     */   private String name;
/*     */   protected BaseConstructor constructor;
/*     */   protected Representer representer;
/*     */   protected DumperOptions dumperOptions;
/*     */   protected LoaderOptions loadingConfig;
/*     */   
/*     */   public Yaml() {
/*  66 */     this((BaseConstructor)new Constructor(), new Representer(), new DumperOptions(), new LoaderOptions(), new Resolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(DumperOptions dumperOptions) {
/*  76 */     this((BaseConstructor)new Constructor(), new Representer(dumperOptions), dumperOptions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(LoaderOptions loadingConfig) {
/*  85 */     this((BaseConstructor)new Constructor(loadingConfig), new Representer(), new DumperOptions(), loadingConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(Representer representer) {
/*  94 */     this((BaseConstructor)new Constructor(), representer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(BaseConstructor constructor) {
/* 103 */     this(constructor, new Representer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(BaseConstructor constructor, Representer representer) {
/* 113 */     this(constructor, representer, initDumperOptions(representer));
/*     */   }
/*     */   
/*     */   private static DumperOptions initDumperOptions(Representer representer) {
/* 117 */     DumperOptions dumperOptions = new DumperOptions();
/* 118 */     dumperOptions.setDefaultFlowStyle(representer.getDefaultFlowStyle());
/* 119 */     dumperOptions.setDefaultScalarStyle(representer.getDefaultScalarStyle());
/* 120 */     dumperOptions.setAllowReadOnlyProperties(representer.getPropertyUtils().isAllowReadOnlyProperties());
/* 121 */     dumperOptions.setTimeZone(representer.getTimeZone());
/* 122 */     return dumperOptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(Representer representer, DumperOptions dumperOptions) {
/* 133 */     this((BaseConstructor)new Constructor(), representer, dumperOptions, new LoaderOptions(), new Resolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions) {
/* 145 */     this(constructor, representer, dumperOptions, new LoaderOptions(), new Resolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, LoaderOptions loadingConfig) {
/* 159 */     this(constructor, representer, dumperOptions, loadingConfig, new Resolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, Resolver resolver) {
/* 173 */     this(constructor, representer, dumperOptions, new LoaderOptions(), resolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, LoaderOptions loadingConfig, Resolver resolver) {
/* 188 */     if (!constructor.isExplicitPropertyUtils()) {
/* 189 */       constructor.setPropertyUtils(representer.getPropertyUtils());
/* 190 */     } else if (!representer.isExplicitPropertyUtils()) {
/* 191 */       representer.setPropertyUtils(constructor.getPropertyUtils());
/*     */     } 
/* 193 */     this.constructor = constructor;
/* 194 */     this.constructor.setAllowDuplicateKeys(loadingConfig.isAllowDuplicateKeys());
/* 195 */     this.constructor.setWrappedToRootException(loadingConfig.isWrappedToRootException());
/* 196 */     if (!dumperOptions.getIndentWithIndicator() && dumperOptions.getIndent() <= dumperOptions.getIndicatorIndent()) {
/* 197 */       throw new YAMLException("Indicator indent must be smaller then indent.");
/*     */     }
/* 199 */     representer.setDefaultFlowStyle(dumperOptions.getDefaultFlowStyle());
/* 200 */     representer.setDefaultScalarStyle(dumperOptions.getDefaultScalarStyle());
/* 201 */     representer.getPropertyUtils().setAllowReadOnlyProperties(dumperOptions.isAllowReadOnlyProperties());
/*     */     
/* 203 */     representer.setTimeZone(dumperOptions.getTimeZone());
/* 204 */     this.representer = representer;
/* 205 */     this.dumperOptions = dumperOptions;
/* 206 */     this.loadingConfig = loadingConfig;
/* 207 */     this.resolver = resolver;
/* 208 */     this.name = "Yaml:" + System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dump(Object data) {
/* 218 */     List<Object> list = new ArrayList(1);
/* 219 */     list.add(data);
/* 220 */     return dumpAll(list.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node represent(Object data) {
/* 232 */     return this.representer.represent(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dumpAll(Iterator<? extends Object> data) {
/* 242 */     StringWriter buffer = new StringWriter();
/* 243 */     dumpAll(data, buffer, null);
/* 244 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dump(Object data, Writer output) {
/* 254 */     List<Object> list = new ArrayList(1);
/* 255 */     list.add(data);
/* 256 */     dumpAll(list.iterator(), output, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dumpAll(Iterator<? extends Object> data, Writer output) {
/* 266 */     dumpAll(data, output, null);
/*     */   }
/*     */   
/*     */   private void dumpAll(Iterator<? extends Object> data, Writer output, Tag rootTag) {
/* 270 */     Serializer serializer = new Serializer((Emitable)new Emitter(output, this.dumperOptions), this.resolver, this.dumperOptions, rootTag);
/*     */     
/*     */     try {
/* 273 */       serializer.open();
/* 274 */       while (data.hasNext()) {
/* 275 */         Node node = this.representer.represent(data.next());
/* 276 */         serializer.serialize(node);
/*     */       } 
/* 278 */       serializer.close();
/* 279 */     } catch (IOException e) {
/* 280 */       throw new YAMLException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dumpAs(Object data, Tag rootTag, DumperOptions.FlowStyle flowStyle) {
/* 321 */     DumperOptions.FlowStyle oldStyle = this.representer.getDefaultFlowStyle();
/* 322 */     if (flowStyle != null) {
/* 323 */       this.representer.setDefaultFlowStyle(flowStyle);
/*     */     }
/* 325 */     List<Object> list = new ArrayList(1);
/* 326 */     list.add(data);
/* 327 */     StringWriter buffer = new StringWriter();
/* 328 */     dumpAll(list.iterator(), buffer, rootTag);
/* 329 */     this.representer.setDefaultFlowStyle(oldStyle);
/* 330 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dumpAsMap(Object data) {
/* 352 */     return dumpAs(data, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serialize(Node node, Writer output) {
/* 362 */     Serializer serializer = new Serializer((Emitable)new Emitter(output, this.dumperOptions), this.resolver, this.dumperOptions, null);
/*     */     
/*     */     try {
/* 365 */       serializer.open();
/* 366 */       serializer.serialize(node);
/* 367 */       serializer.close();
/* 368 */     } catch (IOException e) {
/* 369 */       throw new YAMLException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Event> serialize(Node data) {
/* 381 */     SilentEmitter emitter = new SilentEmitter();
/* 382 */     Serializer serializer = new Serializer(emitter, this.resolver, this.dumperOptions, null);
/*     */     try {
/* 384 */       serializer.open();
/* 385 */       serializer.serialize(data);
/* 386 */       serializer.close();
/* 387 */     } catch (IOException e) {
/* 388 */       throw new YAMLException(e);
/*     */     } 
/* 390 */     return emitter.getEvents();
/*     */   }
/*     */   
/*     */   private static class SilentEmitter implements Emitable {
/* 394 */     private List<Event> events = new ArrayList<>(100);
/*     */     
/*     */     public List<Event> getEvents() {
/* 397 */       return this.events;
/*     */     }
/*     */ 
/*     */     
/*     */     public void emit(Event event) throws IOException {
/* 402 */       this.events.add(event);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private SilentEmitter() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T load(String yaml) {
/* 416 */     return (T)loadFromReader(new StreamReader(yaml), Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T load(InputStream io) {
/* 429 */     return (T)loadFromReader(new StreamReader((Reader)new UnicodeReader(io)), Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T load(Reader io) {
/* 442 */     return (T)loadFromReader(new StreamReader(io), Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T loadAs(Reader io, Class<T> type) {
/* 456 */     return (T)loadFromReader(new StreamReader(io), type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T loadAs(String yaml, Class<T> type) {
/* 470 */     return (T)loadFromReader(new StreamReader(yaml), type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T loadAs(InputStream input, Class<T> type) {
/* 484 */     return (T)loadFromReader(new StreamReader((Reader)new UnicodeReader(input)), type);
/*     */   }
/*     */   
/*     */   private Object loadFromReader(StreamReader sreader, Class<?> type) {
/* 488 */     Composer composer = new Composer((Parser)new ParserImpl(sreader), this.resolver, this.loadingConfig);
/* 489 */     this.constructor.setComposer(composer);
/* 490 */     return this.constructor.getSingleData(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<Object> loadAll(Reader yaml) {
/* 502 */     Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver, this.loadingConfig);
/* 503 */     this.constructor.setComposer(composer);
/* 504 */     Iterator<Object> result = new Iterator()
/*     */       {
/*     */         public boolean hasNext() {
/* 507 */           return Yaml.this.constructor.checkData();
/*     */         }
/*     */ 
/*     */         
/*     */         public Object next() {
/* 512 */           return Yaml.this.constructor.getData();
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 517 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/* 520 */     return new YamlIterable(result);
/*     */   }
/*     */   
/*     */   private static class YamlIterable implements Iterable<Object> {
/*     */     private Iterator<Object> iterator;
/*     */     
/*     */     public YamlIterable(Iterator<Object> iterator) {
/* 527 */       this.iterator = iterator;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Object> iterator() {
/* 532 */       return this.iterator;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<Object> loadAll(String yaml) {
/* 546 */     return loadAll(new StringReader(yaml));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<Object> loadAll(InputStream yaml) {
/* 558 */     return loadAll((Reader)new UnicodeReader(yaml));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node compose(Reader yaml) {
/* 571 */     Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver, this.loadingConfig);
/* 572 */     return composer.getSingleNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<Node> composeAll(Reader yaml) {
/* 584 */     final Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver, this.loadingConfig);
/* 585 */     Iterator<Node> result = new Iterator<Node>()
/*     */       {
/*     */         public boolean hasNext() {
/* 588 */           return composer.checkNode();
/*     */         }
/*     */ 
/*     */         
/*     */         public Node next() {
/* 593 */           Node node = composer.getNode();
/* 594 */           if (node != null) {
/* 595 */             return node;
/*     */           }
/* 597 */           throw new NoSuchElementException("No Node is available.");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void remove() {
/* 603 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/* 606 */     return new NodeIterable(result);
/*     */   }
/*     */   
/*     */   private static class NodeIterable implements Iterable<Node> {
/*     */     private Iterator<Node> iterator;
/*     */     
/*     */     public NodeIterable(Iterator<Node> iterator) {
/* 613 */       this.iterator = iterator;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Node> iterator() {
/* 618 */       return this.iterator;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
/* 632 */     this.resolver.addImplicitResolver(tag, regexp, first);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 637 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 648 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 657 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<Event> parse(Reader yaml) {
/* 668 */     final ParserImpl parser = new ParserImpl(new StreamReader(yaml));
/* 669 */     Iterator<Event> result = new Iterator<Event>()
/*     */       {
/*     */         public boolean hasNext() {
/* 672 */           return (parser.peekEvent() != null);
/*     */         }
/*     */ 
/*     */         
/*     */         public Event next() {
/* 677 */           Event event = parser.getEvent();
/* 678 */           if (event != null) {
/* 679 */             return event;
/*     */           }
/* 681 */           throw new NoSuchElementException("No Event is available.");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void remove() {
/* 687 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/* 690 */     return new EventIterable(result);
/*     */   }
/*     */   
/*     */   private static class EventIterable implements Iterable<Event> {
/*     */     private Iterator<Event> iterator;
/*     */     
/*     */     public EventIterable(Iterator<Event> iterator) {
/* 697 */       this.iterator = iterator;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Event> iterator() {
/* 702 */       return this.iterator;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBeanAccess(BeanAccess beanAccess) {
/* 707 */     this.constructor.getPropertyUtils().setBeanAccess(beanAccess);
/* 708 */     this.representer.getPropertyUtils().setBeanAccess(beanAccess);
/*     */   }
/*     */   
/*     */   public void addTypeDescription(TypeDescription td) {
/* 712 */     this.constructor.addTypeDescription(td);
/* 713 */     this.representer.addTypeDescription(td);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\Yaml.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */