package javafx.embed.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

class CachingTransferable implements Transferable {
   private Map mimeType2Data;

   CachingTransferable() {
      this.mimeType2Data = Collections.EMPTY_MAP;
   }

   public Object getTransferData(DataFlavor var1) throws UnsupportedEncodingException {
      String var2 = DataFlavorUtils.getFxMimeType(var1);
      return DataFlavorUtils.adjustFxData(var1, this.getData(var2));
   }

   public DataFlavor[] getTransferDataFlavors() {
      String[] var1 = this.getMimeTypes();
      return DataFlavorUtils.getDataFlavors(var1);
   }

   public boolean isDataFlavorSupported(DataFlavor var1) {
      return this.isMimeTypeAvailable(DataFlavorUtils.getFxMimeType(var1));
   }

   void updateData(Transferable var1, boolean var2) {
      Map var3 = DataFlavorUtils.adjustSwingDataFlavors(var1.getTransferDataFlavors());

      try {
         this.mimeType2Data = DataFlavorUtils.readAllData(var1, var3, var2);
      } catch (Exception var5) {
         this.mimeType2Data = Collections.EMPTY_MAP;
      }

   }

   void updateData(Clipboard var1, boolean var2) {
      this.mimeType2Data = new HashMap();
      Iterator var3 = var1.getContentTypes().iterator();

      while(var3.hasNext()) {
         DataFormat var4 = (DataFormat)var3.next();
         this.mimeType2Data.put(DataFlavorUtils.getMimeType(var4), var2 ? var1.getContent(var4) : null);
      }

   }

   public Object getData(String var1) {
      return this.mimeType2Data.get(var1);
   }

   public String[] getMimeTypes() {
      return (String[])this.mimeType2Data.keySet().toArray(new String[0]);
   }

   public boolean isMimeTypeAvailable(String var1) {
      return Arrays.asList(this.getMimeTypes()).contains(var1);
   }
}
