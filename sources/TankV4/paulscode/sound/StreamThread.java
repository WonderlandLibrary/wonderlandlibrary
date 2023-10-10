package paulscode.sound;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class StreamThread extends SimpleThread {
   private SoundSystemLogger logger = SoundSystemConfig.getLogger();
   private List streamingSources = new LinkedList();
   private final Object listLock = new Object();

   protected void cleanup() {
      this.kill();
      super.cleanup();
   }

   public void run() {
      this.snooze(3600000L);

      while(!this.dying()) {
         while(!this.dying() && !this.streamingSources.isEmpty()) {
            Object var3;
            synchronized(var3 = this.listLock){}
            ListIterator var1 = this.streamingSources.listIterator();

            while(true) {
               while(!this.dying() && var1.hasNext()) {
                  Source var2 = (Source)var1.next();
                  if (var2 == null) {
                     var1.remove();
                  } else if (var2.stopped()) {
                     if (!var2.rawDataStream) {
                        var1.remove();
                     }
                  } else if (var2.active()) {
                     if (!var2.paused()) {
                        var2.checkFadeOut();
                        if (!var2.stream() && !var2.rawDataStream && (var2.channel == null || !var2.channel.processBuffer())) {
                           if (var2.nextCodec == null) {
                              var2.readBuffersFromNextSoundInSequence();
                           }

                           if (var2.toLoop) {
                              if (!var2.playing()) {
                                 SoundSystemConfig.notifyEOS(var2.sourcename, var2.getSoundSequenceQueueSize());
                                 if (var2.checkFadeOut()) {
                                    var2.preLoad = true;
                                 } else {
                                    var2.incrementSoundSequence();
                                    var2.preLoad = true;
                                 }
                              }
                           } else if (!var2.playing()) {
                              SoundSystemConfig.notifyEOS(var2.sourcename, var2.getSoundSequenceQueueSize());
                              if (!var2.checkFadeOut()) {
                                 if (var2.incrementSoundSequence()) {
                                    var2.preLoad = true;
                                 } else {
                                    var1.remove();
                                 }
                              }
                           }
                        }
                     }
                  } else {
                     if (var2.toLoop || var2.rawDataStream) {
                        var2.toPlay = true;
                     }

                     var1.remove();
                  }
               }

               if (!this.dying() && !this.streamingSources.isEmpty()) {
                  this.snooze(20L);
               }
               break;
            }
         }

         if (!this.dying() && this.streamingSources.isEmpty()) {
            this.snooze(3600000L);
         }
      }

      this.cleanup();
   }

   public void watch(Source var1) {
      if (var1 != null) {
         if (!this.streamingSources.contains(var1)) {
            Object var4;
            synchronized(var4 = this.listLock){}
            ListIterator var2 = this.streamingSources.listIterator();

            while(var2.hasNext()) {
               Source var3 = (Source)var2.next();
               if (var3 == null) {
                  var2.remove();
               } else if (var1.channel == var3.channel) {
                  var3.stop();
                  var2.remove();
               }
            }

            this.streamingSources.add(var1);
         }
      }
   }

   private void message(String var1) {
      this.logger.message(var1, 0);
   }

   private void importantMessage(String var1) {
      this.logger.importantMessage(var1, 0);
   }

   private boolean errorCheck(boolean var1, String var2) {
      return this.logger.errorCheck(var1, "StreamThread", var2, 0);
   }

   private void errorMessage(String var1) {
      this.logger.errorMessage("StreamThread", var1, 0);
   }
}
