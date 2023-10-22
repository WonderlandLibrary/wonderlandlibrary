package javafx.scene.web;

import com.sun.glass.ui.Application;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.web.Debugger;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.webkit.Accessor;
import com.sun.javafx.webkit.CursorManagerImpl;
import com.sun.javafx.webkit.EventLoopImpl;
import com.sun.javafx.webkit.ThemeClientImpl;
import com.sun.javafx.webkit.UIClientImpl;
import com.sun.javafx.webkit.UtilitiesImpl;
import com.sun.javafx.webkit.WebPageClientImpl;
import com.sun.javafx.webkit.prism.PrismGraphicsManager;
import com.sun.javafx.webkit.prism.PrismInvoker;
import com.sun.javafx.webkit.prism.theme.PrismRenderer;
import com.sun.javafx.webkit.theme.RenderThemeImpl;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.prism.Graphics;
import com.sun.webkit.CursorManager;
import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.EventLoop;
import com.sun.webkit.InspectorClient;
import com.sun.webkit.Invoker;
import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.PolicyClient;
import com.sun.webkit.ThemeClient;
import com.sun.webkit.Timer;
import com.sun.webkit.Utilities;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.network.URLs;
import com.sun.webkit.network.Util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageRange;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.util.Callback;
import org.w3c.dom.Document;

public final class WebEngine {
   private static final Logger logger;
   private static int instanceCount;
   private final ObjectProperty view;
   private final LoadWorker loadWorker;
   private final WebPage page;
   private final SelfDisposer disposer;
   private final DebuggerImpl debugger;
   private boolean userDataDirectoryApplied;
   private final DocumentProperty document;
   private final ReadOnlyStringWrapper location;
   private final ReadOnlyStringWrapper title;
   private BooleanProperty javaScriptEnabled;
   private StringProperty userStyleSheetLocation;
   private final ObjectProperty userDataDirectory;
   private StringProperty userAgent;
   private final ObjectProperty onAlert;
   private final ObjectProperty onStatusChanged;
   private final ObjectProperty onResized;
   private final ObjectProperty onVisibilityChanged;
   private final ObjectProperty createPopupHandler;
   private final ObjectProperty confirmHandler;
   private final ObjectProperty promptHandler;
   private final ObjectProperty onError;
   private final WebHistory history;

   public final Worker getLoadWorker() {
      return this.loadWorker;
   }

   public final Document getDocument() {
      return (Document)this.document.getValue();
   }

   public final ReadOnlyObjectProperty documentProperty() {
      return this.document;
   }

   public final String getLocation() {
      return this.location.getValue();
   }

   public final ReadOnlyStringProperty locationProperty() {
      return this.location.getReadOnlyProperty();
   }

   private void updateLocation(String var1) {
      this.location.set(var1);
      this.document.invalidate(false);
      this.title.set((String)null);
   }

   public final String getTitle() {
      return this.title.getValue();
   }

   public final ReadOnlyStringProperty titleProperty() {
      return this.title.getReadOnlyProperty();
   }

   private void updateTitle() {
      this.title.set(this.page.getTitle(this.page.getMainFrame()));
   }

   public final void setJavaScriptEnabled(boolean var1) {
      this.javaScriptEnabledProperty().set(var1);
   }

   public final boolean isJavaScriptEnabled() {
      return this.javaScriptEnabled == null ? true : this.javaScriptEnabled.get();
   }

   public final BooleanProperty javaScriptEnabledProperty() {
      if (this.javaScriptEnabled == null) {
         this.javaScriptEnabled = new BooleanPropertyBase(true) {
            public void invalidated() {
               WebEngine.checkThread();
               WebEngine.this.page.setJavaScriptEnabled(this.get());
            }

            public Object getBean() {
               return WebEngine.this;
            }

            public String getName() {
               return "javaScriptEnabled";
            }
         };
      }

      return this.javaScriptEnabled;
   }

   public final void setUserStyleSheetLocation(String var1) {
      this.userStyleSheetLocationProperty().set(var1);
   }

   public final String getUserStyleSheetLocation() {
      return this.userStyleSheetLocation == null ? null : (String)this.userStyleSheetLocation.get();
   }

   private byte[] readFully(BufferedInputStream var1) throws IOException {
      int var3 = 0;
      ArrayList var4 = new ArrayList();
      byte[] var5 = new byte[4096];

      while(true) {
         int var6 = var1.read(var5);
         if (var6 < 0) {
            byte[] var10 = new byte[var3];
            int var11 = 0;

            byte[] var9;
            for(Iterator var8 = var4.iterator(); var8.hasNext(); var11 += var9.length) {
               var9 = (byte[])var8.next();
               System.arraycopy(var9, 0, var10, var11, var9.length);
            }

            return var10;
         }

         byte[] var7;
         if (var6 == var5.length) {
            var7 = var5;
            var5 = new byte[4096];
         } else {
            var7 = new byte[var6];
            System.arraycopy(var5, 0, var7, 0, var6);
         }

         var4.add(var7);
         var3 += var6;
      }
   }

   public final StringProperty userStyleSheetLocationProperty() {
      if (this.userStyleSheetLocation == null) {
         this.userStyleSheetLocation = new StringPropertyBase((String)null) {
            private static final String DATA_PREFIX = "data:text/css;charset=utf-8;base64,";

            public void invalidated() {
               WebEngine.checkThread();
               String var1 = this.get();
               String var2;
               if (var1 != null && var1.length() > 0) {
                  if (var1.startsWith("data:text/css;charset=utf-8;base64,")) {
                     var2 = var1;
                  } else {
                     if (!var1.startsWith("file:") && !var1.startsWith("jar:") && !var1.startsWith("data:")) {
                        throw new IllegalArgumentException("Invalid stylesheet URL");
                     }

                     try {
                        URLConnection var3 = URLs.newURL(var1).openConnection();
                        var3.connect();
                        BufferedInputStream var4 = new BufferedInputStream(var3.getInputStream());
                        byte[] var5 = WebEngine.this.readFully(var4);
                        String var6 = Base64.getMimeEncoder().encodeToString(var5);
                        var2 = "data:text/css;charset=utf-8;base64," + var6;
                     } catch (IOException var7) {
                        throw new RuntimeException(var7);
                     }
                  }
               } else {
                  var2 = null;
               }

               WebEngine.this.page.setUserStyleSheetLocation(var2);
            }

            public Object getBean() {
               return WebEngine.this;
            }

            public String getName() {
               return "userStyleSheetLocation";
            }
         };
      }

      return this.userStyleSheetLocation;
   }

   public final File getUserDataDirectory() {
      return (File)this.userDataDirectory.get();
   }

   public final void setUserDataDirectory(File var1) {
      this.userDataDirectory.set(var1);
   }

   public final ObjectProperty userDataDirectoryProperty() {
      return this.userDataDirectory;
   }

   public final void setUserAgent(String var1) {
      this.userAgentProperty().set(var1);
   }

   public final String getUserAgent() {
      return this.userAgent == null ? this.page.getUserAgent() : (String)this.userAgent.get();
   }

   public final StringProperty userAgentProperty() {
      if (this.userAgent == null) {
         this.userAgent = new StringPropertyBase(this.page.getUserAgent()) {
            public void invalidated() {
               WebEngine.checkThread();
               WebEngine.this.page.setUserAgent(this.get());
            }

            public Object getBean() {
               return WebEngine.this;
            }

            public String getName() {
               return "userAgent";
            }
         };
      }

      return this.userAgent;
   }

   public final EventHandler getOnAlert() {
      return (EventHandler)this.onAlert.get();
   }

   public final void setOnAlert(EventHandler var1) {
      this.onAlert.set(var1);
   }

   public final ObjectProperty onAlertProperty() {
      return this.onAlert;
   }

   public final EventHandler getOnStatusChanged() {
      return (EventHandler)this.onStatusChanged.get();
   }

   public final void setOnStatusChanged(EventHandler var1) {
      this.onStatusChanged.set(var1);
   }

   public final ObjectProperty onStatusChangedProperty() {
      return this.onStatusChanged;
   }

   public final EventHandler getOnResized() {
      return (EventHandler)this.onResized.get();
   }

   public final void setOnResized(EventHandler var1) {
      this.onResized.set(var1);
   }

   public final ObjectProperty onResizedProperty() {
      return this.onResized;
   }

   public final EventHandler getOnVisibilityChanged() {
      return (EventHandler)this.onVisibilityChanged.get();
   }

   public final void setOnVisibilityChanged(EventHandler var1) {
      this.onVisibilityChanged.set(var1);
   }

   public final ObjectProperty onVisibilityChangedProperty() {
      return this.onVisibilityChanged;
   }

   public final Callback getCreatePopupHandler() {
      return (Callback)this.createPopupHandler.get();
   }

   public final void setCreatePopupHandler(Callback var1) {
      this.createPopupHandler.set(var1);
   }

   public final ObjectProperty createPopupHandlerProperty() {
      return this.createPopupHandler;
   }

   public final Callback getConfirmHandler() {
      return (Callback)this.confirmHandler.get();
   }

   public final void setConfirmHandler(Callback var1) {
      this.confirmHandler.set(var1);
   }

   public final ObjectProperty confirmHandlerProperty() {
      return this.confirmHandler;
   }

   public final Callback getPromptHandler() {
      return (Callback)this.promptHandler.get();
   }

   public final void setPromptHandler(Callback var1) {
      this.promptHandler.set(var1);
   }

   public final ObjectProperty promptHandlerProperty() {
      return this.promptHandler;
   }

   public final EventHandler getOnError() {
      return (EventHandler)this.onError.get();
   }

   public final void setOnError(EventHandler var1) {
      this.onError.set(var1);
   }

   public final ObjectProperty onErrorProperty() {
      return this.onError;
   }

   public WebEngine() {
      this((String)null, false);
   }

   public WebEngine(String var1) {
      this(var1, true);
   }

   private WebEngine(String var1, boolean var2) {
      this.view = new SimpleObjectProperty(this, "view");
      this.loadWorker = new LoadWorker();
      this.debugger = new DebuggerImpl();
      this.userDataDirectoryApplied = false;
      this.document = new DocumentProperty();
      this.location = new ReadOnlyStringWrapper(this, "location");
      this.title = new ReadOnlyStringWrapper(this, "title");
      this.userDataDirectory = new SimpleObjectProperty(this, "userDataDirectory");
      this.onAlert = new SimpleObjectProperty(this, "onAlert");
      this.onStatusChanged = new SimpleObjectProperty(this, "onStatusChanged");
      this.onResized = new SimpleObjectProperty(this, "onResized");
      this.onVisibilityChanged = new SimpleObjectProperty(this, "onVisibilityChanged");
      this.createPopupHandler = new SimpleObjectProperty(this, "createPopupHandler", (var1x) -> {
         return this;
      });
      this.confirmHandler = new SimpleObjectProperty(this, "confirmHandler");
      this.promptHandler = new SimpleObjectProperty(this, "promptHandler");
      this.onError = new SimpleObjectProperty(this, "onError");
      checkThread();
      AccessorImpl var3 = new AccessorImpl(this);
      this.page = new WebPage(new WebPageClientImpl(var3), new UIClientImpl(var3), (PolicyClient)null, new InspectorClientImpl(this), new ThemeClientImpl(var3), false);
      this.page.addLoadListenerClient(new PageLoadListener(this));
      this.history = new WebHistory(this.page);
      this.disposer = new SelfDisposer(this.page);
      Disposer.addRecord(this, this.disposer);
      if (var2) {
         this.load(var1);
      }

      if (instanceCount == 0 && Timer.getMode() == Timer.Mode.PLATFORM_TICKS) {
         WebEngine.PulseTimer.start();
      }

      ++instanceCount;
   }

   public void load(String var1) {
      checkThread();
      this.loadWorker.cancelAndReset();
      if (var1 != null && !var1.equals("") && !var1.equals("about:blank")) {
         try {
            var1 = Util.adjustUrlForWebKit(var1);
         } catch (MalformedURLException var3) {
            this.loadWorker.dispatchLoadEvent(this.getMainFrame(), 0, var1, (String)null, 0.0, 0);
            this.loadWorker.dispatchLoadEvent(this.getMainFrame(), 5, var1, (String)null, 0.0, 2);
            return;
         }
      } else {
         var1 = "";
      }

      this.applyUserDataDirectory();
      this.page.open(this.page.getMainFrame(), var1);
   }

   public void loadContent(String var1) {
      this.loadContent(var1, "text/html");
   }

   public void loadContent(String var1, String var2) {
      checkThread();
      this.loadWorker.cancelAndReset();
      this.applyUserDataDirectory();
      this.page.load(this.page.getMainFrame(), var1, var2);
   }

   public void reload() {
      checkThread();
      this.page.refresh(this.page.getMainFrame());
   }

   public WebHistory getHistory() {
      return this.history;
   }

   public Object executeScript(String var1) {
      checkThread();
      this.applyUserDataDirectory();
      return this.page.executeScript(this.page.getMainFrame(), var1);
   }

   private long getMainFrame() {
      return this.page.getMainFrame();
   }

   WebPage getPage() {
      return this.page;
   }

   void setView(WebView var1) {
      this.view.setValue(var1);
   }

   private void stop() {
      checkThread();
      this.page.stop(this.page.getMainFrame());
   }

   private void applyUserDataDirectory() {
      if (!this.userDataDirectoryApplied) {
         this.userDataDirectoryApplied = true;
         File var1 = this.getUserDataDirectory();

         while(true) {
            File var2;
            String var3;
            if (var1 == null) {
               var2 = defaultUserDataDirectory();
               var3 = String.format("null (%s)", var2);
            } else {
               var2 = var1;
               var3 = var1.toString();
            }

            logger.log(Level.FINE, "Trying to apply user data directory [{0}]", var3);

            String var4;
            EventType var5;
            Object var6;
            File var7;
            try {
               var2 = DirectoryLock.canonicalize(var2);
               var7 = new File(var2, "localstorage");
               File[] var8 = new File[]{var2, var7};
               File[] var9 = var8;
               int var10 = var8.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  File var12 = var9[var11];
                  createDirectories(var12);
                  File var13 = new File(var12, ".test");
                  if (var13.createNewFile()) {
                     var13.delete();
                  }
               }

               this.disposer.userDataDirectoryLock = new DirectoryLock(var2);
               this.page.setLocalStorageDatabasePath(var7.getPath());
               this.page.setLocalStorageEnabled(true);
               logger.log(Level.FINE, "User data directory [{0}] has been applied successfully", var3);
               return;
            } catch (DirectoryLock.DirectoryAlreadyInUseException var14) {
               var4 = "User data directory [%s] is already in use";
               var5 = WebErrorEvent.USER_DATA_DIRECTORY_ALREADY_IN_USE;
               var6 = var14;
            } catch (IOException var15) {
               var4 = "An I/O error occurred while setting up user data directory [%s]";
               var5 = WebErrorEvent.USER_DATA_DIRECTORY_IO_ERROR;
               var6 = var15;
            } catch (SecurityException var16) {
               var4 = "A security error occurred while setting up user data directory [%s]";
               var5 = WebErrorEvent.USER_DATA_DIRECTORY_SECURITY_ERROR;
               var6 = var16;
            }

            var4 = String.format(var4, var3);
            logger.log(Level.FINE, "{0}, calling error handler", var4);
            var7 = var1;
            this.fireError(var5, var4, (Throwable)var6);
            var1 = this.getUserDataDirectory();
            if (Objects.equals(var1, var7)) {
               logger.log(Level.FINE, "Error handler did not modify user data directory, continuing without user data directory");
               return;
            }

            logger.log(Level.FINE, "Error handler has set user data directory to [{0}], retrying", var1);
         }
      }
   }

   private static File defaultUserDataDirectory() {
      return new File(Application.GetApplication().getDataDirectory(), "webview");
   }

   private static void createDirectories(File var0) throws IOException {
      Path var1 = var0.toPath();

      try {
         Files.createDirectories(var1, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")));
      } catch (UnsupportedOperationException var3) {
         Files.createDirectories(var1);
      }

   }

   private void fireError(EventType var1, String var2, Throwable var3) {
      EventHandler var4 = this.getOnError();
      if (var4 != null) {
         var4.handle(new WebErrorEvent(this, var1, var2, var3));
      }

   }

   void dispose() {
      this.disposer.dispose();
   }

   static void checkThread() {
      Toolkit.getToolkit().checkFxUserThread();
   }

   /** @deprecated */
   @Deprecated
   public Debugger impl_getDebugger() {
      return this.debugger;
   }

   private static final boolean printStatusOK(PrinterJob var0) {
      switch (var0.getJobStatus()) {
         case NOT_STARTED:
         case PRINTING:
            return true;
         default:
            return false;
      }
   }

   public void print(PrinterJob var1) {
      if (printStatusOK(var1)) {
         PageLayout var2 = var1.getJobSettings().getPageLayout();
         float var3 = (float)var2.getPrintableWidth();
         float var4 = (float)var2.getPrintableHeight();
         int var5 = this.page.beginPrinting(var3, var4);
         JobSettings var6 = var1.getJobSettings();
         if (var6.getPageRanges() != null) {
            PageRange[] var7 = var6.getPageRanges();
            PageRange[] var8 = var7;
            int var9 = var7.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               PageRange var11 = var8[var10];

               for(int var12 = var11.getStartPage(); var12 <= var11.getEndPage() && var12 <= var5; ++var12) {
                  if (printStatusOK(var1)) {
                     Printable var13 = new Printable(var12 - 1, var3);
                     var1.printPage(var13);
                  }
               }
            }
         } else {
            for(int var14 = 0; var14 < var5; ++var14) {
               if (printStatusOK(var1)) {
                  Printable var15 = new Printable(var14, var3);
                  var1.printPage(var15);
               }
            }
         }

         this.page.endPrinting();
      }
   }

   static {
      Accessor.setPageAccessor((var0) -> {
         return var0 == null ? null : var0.getPage();
      });
      Invoker.setInvoker(new PrismInvoker());
      Renderer.setRenderer(new PrismRenderer());
      WCGraphicsManager.setGraphicsManager(new PrismGraphicsManager());
      CursorManager.setCursorManager(new CursorManagerImpl());
      EventLoop.setEventLoop(new EventLoopImpl());
      ThemeClient.setDefaultRenderTheme(new RenderThemeImpl());
      Utilities.setUtilities(new UtilitiesImpl());
      logger = Logger.getLogger(WebEngine.class.getName());
      instanceCount = 0;
   }

   final class Printable extends Node {
      private final NGNode peer;

      Printable(int var2, float var3) {
         this.peer = new Peer(var2, var3);
      }

      protected NGNode impl_createPeer() {
         return this.peer;
      }

      public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
         return null;
      }

      public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
         return var1;
      }

      protected boolean impl_computeContains(double var1, double var3) {
         return false;
      }

      private final class Peer extends NGNode {
         private final int pageIndex;
         private final float width;

         Peer(int var2, float var3) {
            this.pageIndex = var2;
            this.width = var3;
         }

         protected void renderContent(Graphics var1) {
            WCGraphicsContext var2 = WCGraphicsManager.getGraphicsManager().createGraphicsContext(var1);
            WebEngine.this.page.print(var2, this.pageIndex, this.width);
         }

         protected boolean hasOverlappingContents() {
            return false;
         }
      }
   }

   private static final class InspectorClientImpl implements InspectorClient {
      private final WeakReference engine;

      private InspectorClientImpl(WebEngine var1) {
         this.engine = new WeakReference(var1);
      }

      public boolean sendMessageToFrontend(String var1) {
         boolean var2 = false;
         WebEngine var3 = (WebEngine)this.engine.get();
         if (var3 != null) {
            Callback var4 = var3.debugger.messageCallback;
            if (var4 != null) {
               AccessController.doPrivileged(() -> {
                  var4.call(var1);
                  return null;
               }, var3.page.getAccessControlContext());
               var2 = true;
            }
         }

         return var2;
      }

      // $FF: synthetic method
      InspectorClientImpl(WebEngine var1, Object var2) {
         this(var1);
      }
   }

   private final class DebuggerImpl implements Debugger {
      private boolean enabled;
      private Callback messageCallback;

      private DebuggerImpl() {
      }

      public boolean isEnabled() {
         WebEngine.checkThread();
         return this.enabled;
      }

      public void setEnabled(boolean var1) {
         WebEngine.checkThread();
         if (var1 != this.enabled) {
            if (var1) {
               WebEngine.this.page.setDeveloperExtrasEnabled(true);
               WebEngine.this.page.connectInspectorFrontend();
            } else {
               WebEngine.this.page.disconnectInspectorFrontend();
               WebEngine.this.page.setDeveloperExtrasEnabled(false);
            }

            this.enabled = var1;
         }

      }

      public void sendMessage(String var1) {
         WebEngine.checkThread();
         if (!this.enabled) {
            throw new IllegalStateException("Debugger is not enabled");
         } else if (var1 == null) {
            throw new NullPointerException("message is null");
         } else {
            WebEngine.this.page.dispatchInspectorMessageFromFrontend(var1);
         }
      }

      public Callback getMessageCallback() {
         WebEngine.checkThread();
         return this.messageCallback;
      }

      public void setMessageCallback(Callback var1) {
         WebEngine.checkThread();
         this.messageCallback = var1;
      }

      // $FF: synthetic method
      DebuggerImpl(Object var2) {
         this();
      }
   }

   private final class DocumentProperty extends ReadOnlyObjectPropertyBase {
      private boolean available;
      private Document document;

      private DocumentProperty() {
      }

      private void invalidate(boolean var1) {
         if (this.available || var1) {
            this.available = var1;
            this.document = null;
            this.fireValueChangedEvent();
         }

      }

      public Document get() {
         if (!this.available) {
            return null;
         } else {
            if (this.document == null) {
               this.document = WebEngine.this.page.getDocument(WebEngine.this.page.getMainFrame());
               if (this.document == null) {
                  this.available = false;
               }
            }

            return this.document;
         }
      }

      public Object getBean() {
         return WebEngine.this;
      }

      public String getName() {
         return "document";
      }

      // $FF: synthetic method
      DocumentProperty(Object var2) {
         this();
      }
   }

   private final class LoadWorker implements Worker {
      private final ReadOnlyObjectWrapper state;
      private final ReadOnlyObjectWrapper value;
      private final ReadOnlyObjectWrapper exception;
      private final ReadOnlyDoubleWrapper workDone;
      private final ReadOnlyDoubleWrapper totalWorkToBeDone;
      private final ReadOnlyDoubleWrapper progress;
      private final ReadOnlyBooleanWrapper running;
      private final ReadOnlyStringWrapper message;
      private final ReadOnlyStringWrapper title;

      private LoadWorker() {
         this.state = new ReadOnlyObjectWrapper(this, "state", Worker.State.READY);
         this.value = new ReadOnlyObjectWrapper(this, "value", (Object)null);
         this.exception = new ReadOnlyObjectWrapper(this, "exception");
         this.workDone = new ReadOnlyDoubleWrapper(this, "workDone", -1.0);
         this.totalWorkToBeDone = new ReadOnlyDoubleWrapper(this, "totalWork", -1.0);
         this.progress = new ReadOnlyDoubleWrapper(this, "progress", -1.0);
         this.running = new ReadOnlyBooleanWrapper(this, "running", false);
         this.message = new ReadOnlyStringWrapper(this, "message", "");
         this.title = new ReadOnlyStringWrapper(this, "title", "WebEngine Loader");
      }

      public final Worker.State getState() {
         WebEngine.checkThread();
         return (Worker.State)this.state.get();
      }

      public final ReadOnlyObjectProperty stateProperty() {
         WebEngine.checkThread();
         return this.state.getReadOnlyProperty();
      }

      private void updateState(Worker.State var1) {
         WebEngine.checkThread();
         this.state.set(var1);
         this.running.set(var1 == Worker.State.SCHEDULED || var1 == Worker.State.RUNNING);
      }

      public final Void getValue() {
         WebEngine.checkThread();
         return (Void)this.value.get();
      }

      public final ReadOnlyObjectProperty valueProperty() {
         WebEngine.checkThread();
         return this.value.getReadOnlyProperty();
      }

      public final Throwable getException() {
         WebEngine.checkThread();
         return (Throwable)this.exception.get();
      }

      public final ReadOnlyObjectProperty exceptionProperty() {
         WebEngine.checkThread();
         return this.exception.getReadOnlyProperty();
      }

      public final double getWorkDone() {
         WebEngine.checkThread();
         return this.workDone.get();
      }

      public final ReadOnlyDoubleProperty workDoneProperty() {
         WebEngine.checkThread();
         return this.workDone.getReadOnlyProperty();
      }

      public final double getTotalWork() {
         WebEngine.checkThread();
         return this.totalWorkToBeDone.get();
      }

      public final ReadOnlyDoubleProperty totalWorkProperty() {
         WebEngine.checkThread();
         return this.totalWorkToBeDone.getReadOnlyProperty();
      }

      public final double getProgress() {
         WebEngine.checkThread();
         return this.progress.get();
      }

      public final ReadOnlyDoubleProperty progressProperty() {
         WebEngine.checkThread();
         return this.progress.getReadOnlyProperty();
      }

      private void updateProgress(double var1) {
         this.totalWorkToBeDone.set(100.0);
         this.workDone.set(var1 * 100.0);
         this.progress.set(var1);
      }

      public final boolean isRunning() {
         WebEngine.checkThread();
         return this.running.get();
      }

      public final ReadOnlyBooleanProperty runningProperty() {
         WebEngine.checkThread();
         return this.running.getReadOnlyProperty();
      }

      public final String getMessage() {
         return this.message.get();
      }

      public final ReadOnlyStringProperty messageProperty() {
         return this.message.getReadOnlyProperty();
      }

      public final String getTitle() {
         return this.title.get();
      }

      public final ReadOnlyStringProperty titleProperty() {
         return this.title.getReadOnlyProperty();
      }

      public boolean cancel() {
         if (this.isRunning()) {
            WebEngine.this.stop();
            return true;
         } else {
            return false;
         }
      }

      private void cancelAndReset() {
         this.cancel();
         this.exception.set((Object)null);
         this.message.set("");
         this.totalWorkToBeDone.set(-1.0);
         this.workDone.set(-1.0);
         this.progress.set(-1.0);
         this.updateState(Worker.State.READY);
         this.running.set(false);
      }

      private void dispatchLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8) {
         if (var1 == WebEngine.this.getMainFrame()) {
            switch (var3) {
               case 0:
                  this.message.set("Loading " + var4);
                  WebEngine.this.updateLocation(var4);
                  this.updateProgress(0.0);
                  this.updateState(Worker.State.SCHEDULED);
                  this.updateState(Worker.State.RUNNING);
                  break;
               case 1:
                  this.message.set("Loading complete");
                  this.updateProgress(1.0);
                  this.updateState(Worker.State.SUCCEEDED);
                  break;
               case 2:
                  this.message.set("Loading " + var4);
                  WebEngine.this.updateLocation(var4);
                  break;
               case 3:
                  this.message.set("Replaced " + var4);
                  WebEngine.this.location.set(var4);
               case 4:
               case 7:
               case 8:
               case 9:
               case 10:
               case 12:
               case 13:
               case 15:
               case 16:
               case 17:
               case 18:
               case 19:
               case 20:
               case 21:
               case 22:
               case 23:
               case 24:
               case 25:
               case 26:
               case 27:
               case 28:
               case 29:
               default:
                  break;
               case 5:
                  this.message.set("Loading failed");
                  this.exception.set(this.describeError(var8));
                  this.updateState(Worker.State.FAILED);
                  break;
               case 6:
                  this.message.set("Loading stopped");
                  this.updateState(Worker.State.CANCELLED);
                  break;
               case 11:
                  WebEngine.this.updateTitle();
                  break;
               case 14:
                  if (this.state.get() != Worker.State.RUNNING) {
                     this.dispatchLoadEvent(var1, 0, var4, var5, var6, var8);
                  }

                  WebEngine.this.document.invalidate(true);
                  break;
               case 30:
                  this.updateProgress(var6);
            }

         }
      }

      private Throwable describeError(int var1) {
         String var2 = "Unknown error";
         switch (var1) {
            case 1:
               var2 = "Unknown host";
               break;
            case 2:
               var2 = "Malformed URL";
               break;
            case 3:
               var2 = "SSL handshake failed";
               break;
            case 4:
               var2 = "Connection refused by server";
               break;
            case 5:
               var2 = "Connection reset by server";
               break;
            case 6:
               var2 = "No route to host";
               break;
            case 7:
               var2 = "Connection timed out";
               break;
            case 8:
               var2 = "Permission denied";
               break;
            case 9:
               var2 = "Invalid response from server";
               break;
            case 10:
               var2 = "Too many redirects";
               break;
            case 11:
               var2 = "File not found";
         }

         return new Throwable(var2);
      }

      // $FF: synthetic method
      LoadWorker(Object var2) {
         this();
      }
   }

   private static final class PageLoadListener implements LoadListenerClient {
      private final WeakReference engine;

      private PageLoadListener(WebEngine var1) {
         this.engine = new WeakReference(var1);
      }

      public void dispatchLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8) {
         WebEngine var9 = (WebEngine)this.engine.get();
         if (var9 != null) {
            var9.loadWorker.dispatchLoadEvent(var1, var3, var4, var5, var6, var8);
         }

      }

      public void dispatchResourceLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8) {
      }

      // $FF: synthetic method
      PageLoadListener(WebEngine var1, Object var2) {
         this(var1);
      }
   }

   private static final class PulseTimer {
      private static final AnimationTimer animation = new AnimationTimer() {
         public void handle(long var1) {
         }
      };
      private static final TKPulseListener listener = () -> {
         Platform.runLater(() -> {
            Timer.getTimer().notifyTick();
         });
      };

      private static void start() {
         Toolkit.getToolkit().addSceneTkPulseListener(listener);
         animation.start();
      }

      private static void stop() {
         Toolkit.getToolkit().removeSceneTkPulseListener(listener);
         animation.stop();
      }
   }

   private static final class AccessorImpl extends Accessor {
      private final WeakReference engine;

      private AccessorImpl(WebEngine var1) {
         this.engine = new WeakReference(var1);
      }

      public WebEngine getEngine() {
         return (WebEngine)this.engine.get();
      }

      public WebPage getPage() {
         WebEngine var1 = this.getEngine();
         return var1 == null ? null : var1.page;
      }

      public WebView getView() {
         WebEngine var1 = this.getEngine();
         return var1 == null ? null : (WebView)var1.view.get();
      }

      public void addChild(Node var1) {
         WebView var2 = this.getView();
         if (var2 != null) {
            var2.getChildren().add(var1);
         }

      }

      public void removeChild(Node var1) {
         WebView var2 = this.getView();
         if (var2 != null) {
            var2.getChildren().remove(var1);
         }

      }

      public void addViewListener(InvalidationListener var1) {
         WebEngine var2 = this.getEngine();
         if (var2 != null) {
            var2.view.addListener(var1);
         }

      }

      // $FF: synthetic method
      AccessorImpl(WebEngine var1, Object var2) {
         this(var1);
      }
   }

   private static final class SelfDisposer implements DisposerRecord {
      private WebPage page;
      private DirectoryLock userDataDirectoryLock;

      private SelfDisposer(WebPage var1) {
         this.page = var1;
      }

      public void dispose() {
         if (this.page != null) {
            this.page.dispose();
            this.page = null;
            if (this.userDataDirectoryLock != null) {
               this.userDataDirectoryLock.close();
            }

            WebEngine.instanceCount--;
            if (WebEngine.instanceCount == 0 && Timer.getMode() == Timer.Mode.PLATFORM_TICKS) {
               WebEngine.PulseTimer.stop();
            }

         }
      }

      // $FF: synthetic method
      SelfDisposer(WebPage var1, Object var2) {
         this(var1);
      }
   }
}
