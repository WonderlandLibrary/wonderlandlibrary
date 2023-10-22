package com.sun.glass.ui;

import com.sun.glass.ui.delegate.ClipboardDelegate;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Clipboard {
   public static final String TEXT_TYPE = "text/plain";
   public static final String HTML_TYPE = "text/html";
   public static final String RTF_TYPE = "text/rtf";
   public static final String URI_TYPE = "text/uri-list";
   public static final String FILE_LIST_TYPE = "application/x-java-file-list";
   public static final String RAW_IMAGE_TYPE = "application/x-java-rawimage";
   public static final String DRAG_IMAGE = "application/x-java-drag-image";
   public static final String DRAG_IMAGE_OFFSET = "application/x-java-drag-image-offset";
   public static final String IE_URL_SHORTCUT_FILENAME = "text/ie-shortcut-filename";
   public static final int ACTION_NONE = 0;
   public static final int ACTION_COPY = 1;
   public static final int ACTION_MOVE = 2;
   public static final int ACTION_REFERENCE = 1073741824;
   public static final int ACTION_COPY_OR_MOVE = 3;
   public static final int ACTION_ANY = 1342177279;
   public static final String DND = "DND";
   public static final String SYSTEM = "SYSTEM";
   public static final String SELECTION = "SELECTION";
   private static final Map clipboards = new HashMap();
   private static final ClipboardDelegate delegate = PlatformFactory.getPlatformFactory().createClipboardDelegate();
   private final HashSet assistants = new HashSet();
   private final String name;
   private final Object localDataProtector = new Object();
   private HashMap localSharedData;
   private ClipboardAssistance dataSource;
   protected int supportedActions = 1;

   protected Clipboard(String var1) {
      Application.checkEventThread();
      this.name = var1;
   }

   public void add(ClipboardAssistance var1) {
      Application.checkEventThread();
      synchronized(this.assistants) {
         this.assistants.add(var1);
      }
   }

   public void remove(ClipboardAssistance var1) {
      Application.checkEventThread();
      synchronized(this.localDataProtector) {
         if (var1 == this.dataSource) {
            this.dataSource = null;
         }
      }

      boolean var2;
      synchronized(this.assistants) {
         this.assistants.remove(var1);
         var2 = this.assistants.isEmpty();
      }

      if (var2) {
         synchronized(clipboards) {
            clipboards.remove(this.name);
         }

         this.close();
      }

   }

   protected void setSharedData(ClipboardAssistance var1, HashMap var2, int var3) {
      Application.checkEventThread();
      synchronized(this.localDataProtector) {
         this.localSharedData = (HashMap)var2.clone();
         this.supportedActions = var3;
         this.dataSource = var1;
      }
   }

   public void flush(ClipboardAssistance var1, HashMap var2, int var3) {
      Application.checkEventThread();
      this.setSharedData(var1, var2, var3);
      this.contentChanged();
   }

   public int getSupportedSourceActions() {
      Application.checkEventThread();
      return this.supportedActions;
   }

   public void setTargetAction(int var1) {
      Application.checkEventThread();
      this.actionPerformed(var1);
   }

   public void contentChanged() {
      Application.checkEventThread();
      HashSet var1;
      synchronized(this.assistants) {
         var1 = (HashSet)this.assistants.clone();
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ClipboardAssistance var3 = (ClipboardAssistance)var2.next();
         var3.contentChanged();
      }

   }

   public void actionPerformed(int var1) {
      Application.checkEventThread();
      synchronized(this.localDataProtector) {
         if (null != this.dataSource) {
            this.dataSource.actionPerformed(var1);
         }

      }
   }

   public Object getData(String var1) {
      Application.checkEventThread();
      synchronized(this.localDataProtector) {
         if (this.localSharedData == null) {
            return null;
         } else {
            Object var3 = this.localSharedData.get(var1);
            return var3 instanceof DelayedCallback ? ((DelayedCallback)var3).providedData() : var3;
         }
      }
   }

   public String[] getMimeTypes() {
      Application.checkEventThread();
      synchronized(this.localDataProtector) {
         if (this.localSharedData == null) {
            return null;
         } else {
            Set var2 = this.localSharedData.keySet();
            String[] var3 = new String[var2.size()];
            int var4 = 0;

            String var6;
            for(Iterator var5 = var2.iterator(); var5.hasNext(); var3[var4++] = var6) {
               var6 = (String)var5.next();
            }

            return var3;
         }
      }
   }

   protected static Clipboard get(String var0) {
      Application.checkEventThread();
      synchronized(clipboards) {
         if (!clipboards.keySet().contains(var0)) {
            Clipboard var2 = delegate.createClipboard(var0);
            if (var2 == null) {
               var2 = new Clipboard(var0);
            }

            clipboards.put(var0, var2);
         }

         return (Clipboard)clipboards.get(var0);
      }
   }

   public Pixels getPixelsForRawImage(byte[] var1) {
      Application.checkEventThread();
      ByteBuffer var2 = ByteBuffer.wrap(var1, 0, 8);
      int var3 = var2.getInt();
      int var4 = var2.getInt();
      ByteBuffer var5 = ByteBuffer.wrap(var1, 8, var1.length - 8);
      return Application.GetApplication().createPixels(var3, var4, var5.slice());
   }

   public String toString() {
      return "Clipboard: " + this.name + "@" + this.hashCode();
   }

   protected void close() {
      Application.checkEventThread();
      synchronized(this.localDataProtector) {
         this.dataSource = null;
      }
   }

   public String getName() {
      Application.checkEventThread();
      return this.name;
   }

   public static String getActionString(int var0) {
      Application.checkEventThread();
      StringBuilder var1 = new StringBuilder("");
      int[] var2 = new int[]{1, 2, 1073741824};
      String[] var3 = new String[]{"copy", "move", "link"};

      for(int var4 = 0; var4 < 3; ++var4) {
         if ((var2[var4] & var0) > 0) {
            if (var1.length() > 0) {
               var1.append(",");
            }

            var1.append(var3[var4]);
         }
      }

      return var1.toString();
   }
}
