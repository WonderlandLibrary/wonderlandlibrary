package javafx.scene.media;

import com.sun.javafx.tk.Toolkit;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

class MediaPlayerShutdownHook implements Runnable {
   private static final List playerRefs = new ArrayList();
   private static boolean isShutdown = false;

   public static void addMediaPlayer(MediaPlayer var0) {
      synchronized(playerRefs) {
         if (isShutdown) {
            com.sun.media.jfxmedia.MediaPlayer var2 = var0.retrieveJfxPlayer();
            if (var2 != null) {
               var2.dispose();
            }
         } else {
            ListIterator var6 = playerRefs.listIterator();

            while(var6.hasNext()) {
               MediaPlayer var3 = (MediaPlayer)((WeakReference)var6.next()).get();
               if (var3 == null) {
                  var6.remove();
               }
            }

            playerRefs.add(new WeakReference(var0));
         }

      }
   }

   public void run() {
      synchronized(playerRefs) {
         ListIterator var2 = playerRefs.listIterator();

         while(var2.hasNext()) {
            MediaPlayer var3 = (MediaPlayer)((WeakReference)var2.next()).get();
            if (var3 != null) {
               var3.destroyMediaTimer();
               com.sun.media.jfxmedia.MediaPlayer var4 = var3.retrieveJfxPlayer();
               if (var4 != null) {
                  var4.dispose();
               }
            } else {
               var2.remove();
            }
         }

         isShutdown = true;
      }
   }

   static {
      Toolkit.getToolkit().addShutdownHook(new MediaPlayerShutdownHook());
   }
}
