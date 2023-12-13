package com.sun.javafx.webkit;

import com.sun.webkit.UIClient;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCRectangle;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.PromptData;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

public final class UIClientImpl implements UIClient {
   private final Accessor accessor;
   private FileChooser chooser;
   private static final Map fileExtensionMap = new HashMap();
   private static String[] chooseFiles = null;
   private ClipboardContent content;
   private static final DataFormat DF_DRAG_IMAGE;
   private static final DataFormat DF_DRAG_IMAGE_OFFSET;

   public UIClientImpl(Accessor var1) {
      this.accessor = var1;
   }

   private WebEngine getWebEngine() {
      return this.accessor.getEngine();
   }

   private AccessControlContext getAccessContext() {
      return this.accessor.getPage().getAccessControlContext();
   }

   public WebPage createPage(boolean var1, boolean var2, boolean var3, boolean var4) {
      WebEngine var5 = this.getWebEngine();
      if (var5 != null && var5.getCreatePopupHandler() != null) {
         PopupFeatures var6 = new PopupFeatures(var1, var2, var3, var4);
         WebEngine var7 = (WebEngine)AccessController.doPrivileged(() -> {
            return (WebEngine)var5.getCreatePopupHandler().call(var6);
         }, this.getAccessContext());
         return Accessor.getPageFor(var7);
      } else {
         return null;
      }
   }

   private void dispatchWebEvent(EventHandler var1, WebEvent var2) {
      AccessController.doPrivileged(() -> {
         var1.handle(var2);
         return null;
      }, this.getAccessContext());
   }

   private void notifyVisibilityChanged(boolean var1) {
      WebEngine var2 = this.getWebEngine();
      if (var2 != null && var2.getOnVisibilityChanged() != null) {
         this.dispatchWebEvent(var2.getOnVisibilityChanged(), new WebEvent(var2, WebEvent.VISIBILITY_CHANGED, var1));
      }

   }

   public void closePage() {
      this.notifyVisibilityChanged(false);
   }

   public void showView() {
      this.notifyVisibilityChanged(true);
   }

   public WCRectangle getViewBounds() {
      WebView var1 = this.accessor.getView();
      Window var2 = null;
      return var1 != null && var1.getScene() != null && (var2 = var1.getScene().getWindow()) != null ? new WCRectangle((float)var2.getX(), (float)var2.getY(), (float)var2.getWidth(), (float)var2.getHeight()) : null;
   }

   public void setViewBounds(WCRectangle var1) {
      WebEngine var2 = this.getWebEngine();
      if (var2 != null && var2.getOnResized() != null) {
         this.dispatchWebEvent(var2.getOnResized(), new WebEvent(var2, WebEvent.RESIZED, new Rectangle2D((double)var1.getX(), (double)var1.getY(), (double)var1.getWidth(), (double)var1.getHeight())));
      }

   }

   public void setStatusbarText(String var1) {
      WebEngine var2 = this.getWebEngine();
      if (var2 != null && var2.getOnStatusChanged() != null) {
         this.dispatchWebEvent(var2.getOnStatusChanged(), new WebEvent(var2, WebEvent.STATUS_CHANGED, var1));
      }

   }

   public void alert(String var1) {
      WebEngine var2 = this.getWebEngine();
      if (var2 != null && var2.getOnAlert() != null) {
         this.dispatchWebEvent(var2.getOnAlert(), new WebEvent(var2, WebEvent.ALERT, var1));
      }

   }

   public boolean confirm(String var1) {
      WebEngine var2 = this.getWebEngine();
      return var2 != null && var2.getConfirmHandler() != null ? (Boolean)AccessController.doPrivileged(() -> {
         return (Boolean)var2.getConfirmHandler().call(var1);
      }, this.getAccessContext()) : false;
   }

   public String prompt(String var1, String var2) {
      WebEngine var3 = this.getWebEngine();
      if (var3 != null && var3.getPromptHandler() != null) {
         PromptData var4 = new PromptData(var1, var2);
         return (String)AccessController.doPrivileged(() -> {
            return (String)var3.getPromptHandler().call(var4);
         }, this.getAccessContext());
      } else {
         return "";
      }
   }

   public boolean canRunBeforeUnloadConfirmPanel() {
      return false;
   }

   public boolean runBeforeUnloadConfirmPanel(String var1) {
      return false;
   }

   static void test_setChooseFiles(String[] var0) {
      chooseFiles = var0;
   }

   public String[] chooseFile(String var1, boolean var2, String var3) {
      if (chooseFiles != null) {
         return chooseFiles;
      } else {
         Window var4 = null;
         WebView var5 = this.accessor.getView();
         if (var5 != null && var5.getScene() != null) {
            var4 = var5.getScene().getWindow();
         }

         if (this.chooser == null) {
            this.chooser = new FileChooser();
         }

         this.chooser.getExtensionFilters().clear();
         if (var3 != null && !var3.isEmpty()) {
            this.addMimeFilters(this.chooser, var3);
         }

         this.chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", new String[]{"*.*"}));
         File var6;
         if (var1 != null) {
            for(var6 = new File(var1); var6 != null && !var6.isDirectory(); var6 = var6.getParentFile()) {
            }

            this.chooser.setInitialDirectory(var6);
         }

         if (!var2) {
            var6 = this.chooser.showOpenDialog(var4);
            return var6 != null ? new String[]{var6.getAbsolutePath()} : null;
         } else {
            List var10 = this.chooser.showOpenMultipleDialog(var4);
            if (var10 == null) {
               return null;
            } else {
               int var7 = var10.size();
               String[] var8 = new String[var7];

               for(int var9 = 0; var9 < var7; ++var9) {
                  var8[var9] = ((File)var10.get(var9)).getAbsolutePath();
               }

               return var8;
            }
         }
      }
   }

   private void addSpecificFilters(FileChooser var1, String var2) {
      if (var2.contains("/")) {
         String[] var3 = var2.split("/");
         String var4 = var3[0];
         String var5 = var3[1];
         FileExtensionInfo var6 = (FileExtensionInfo)fileExtensionMap.get(var4);
         if (var6 != null) {
            FileChooser.ExtensionFilter var7 = var6.getExtensionFilter(var5);
            if (var7 != null) {
               var1.getExtensionFilters().addAll(var7);
            }
         }
      }

   }

   private void addMimeFilters(FileChooser var1, String var2) {
      if (var2.contains(",")) {
         String[] var3 = var2.split(",");
         String[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            this.addSpecificFilters(var1, var7);
         }
      } else {
         this.addSpecificFilters(var1, var2);
      }

   }

   public void print() {
   }

   private static DataFormat getDataFormat(String var0) {
      Class var1 = DataFormat.class;
      synchronized(DataFormat.class) {
         DataFormat var2 = DataFormat.lookupMimeType(var0);
         if (var2 == null) {
            var2 = new DataFormat(new String[]{var0});
         }

         return var2;
      }
   }

   public void startDrag(WCImage var1, int var2, int var3, int var4, int var5, String[] var6, Object[] var7, boolean var8) {
      this.content = new ClipboardContent();

      for(int var9 = 0; var9 < var6.length; ++var9) {
         if (var7[var9] != null) {
            try {
               this.content.put(getDataFormat(var6[var9]), "text/ie-shortcut-filename".equals(var6[var9]) ? ByteBuffer.wrap(((String)var7[var9]).getBytes("UTF-16LE")) : var7[var9]);
            } catch (UnsupportedEncodingException var17) {
            }
         }
      }

      if (var1 != null && !var1.isNull()) {
         ByteBuffer var18 = ByteBuffer.allocate(8);
         var18.rewind();
         var18.putInt(var2);
         var18.putInt(var3);
         this.content.put(DF_DRAG_IMAGE_OFFSET, var18);
         int var10 = var1.getWidth();
         int var11 = var1.getHeight();
         ByteBuffer var12 = var1.getPixelBuffer();
         ByteBuffer var13 = ByteBuffer.allocate(8 + var10 * var11 * 4);
         var13.putInt(var10);
         var13.putInt(var11);
         var13.put(var12);
         this.content.put(DF_DRAG_IMAGE, var13);
         if (var8) {
            String var14 = var1.getFileExtension();

            try {
               File var15 = File.createTempFile("jfx", "." + var14);
               var15.deleteOnExit();
               ImageIO.write(var1.toBufferedImage(), var14, var15);
               this.content.put(DataFormat.FILES, Arrays.asList(var15));
            } catch (SecurityException | IOException var16) {
            }
         }
      }

   }

   public void confirmStartDrag() {
      WebView var1 = this.accessor.getView();
      if (var1 != null && this.content != null) {
         Dragboard var2 = var1.startDragAndDrop(TransferMode.ANY);
         var2.setContent(this.content);
      }

      this.content = null;
   }

   public boolean isDragConfirmed() {
      return this.accessor.getView() != null && this.content != null;
   }

   static {
      UIClientImpl.FileExtensionInfo.add("video", "Video Files", "*.webm", "*.mp4", "*.ogg");
      UIClientImpl.FileExtensionInfo.add("audio", "Audio Files", "*.mp3", "*.aac", "*.wav");
      UIClientImpl.FileExtensionInfo.add("text", "Text Files", "*.txt", "*.csv", "*.text", "*.ttf", "*.sdf", "*.srt", "*.htm", "*.html");
      UIClientImpl.FileExtensionInfo.add("image", "Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg");
      DF_DRAG_IMAGE = getDataFormat("application/x-java-drag-image");
      DF_DRAG_IMAGE_OFFSET = getDataFormat("application/x-java-drag-image-offset");
   }

   private static class FileExtensionInfo {
      private String description;
      private List extensions;

      static void add(String var0, String var1, String... var2) {
         FileExtensionInfo var3 = new FileExtensionInfo();
         var3.description = var1;
         var3.extensions = Arrays.asList(var2);
         UIClientImpl.fileExtensionMap.put(var0, var3);
      }

      private FileChooser.ExtensionFilter getExtensionFilter(String var1) {
         String var2 = "*." + var1;
         String var3 = this.description + " ";
         if (var1.equals("*")) {
            var3 = var3 + (String)this.extensions.stream().collect(Collectors.joining(", ", "(", ")"));
            return new FileChooser.ExtensionFilter(var3, this.extensions);
         } else if (this.extensions.contains(var2)) {
            var3 = var3 + "(" + var2 + ")";
            return new FileChooser.ExtensionFilter(var3, new String[]{var2});
         } else {
            return null;
         }
      }
   }
}
