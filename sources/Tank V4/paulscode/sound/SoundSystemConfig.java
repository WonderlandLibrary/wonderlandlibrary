package paulscode.sound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

public class SoundSystemConfig {
   public static final Object THREAD_SYNC = new Object();
   public static final int TYPE_NORMAL = 0;
   public static final int TYPE_STREAMING = 1;
   public static final int ATTENUATION_NONE = 0;
   public static final int ATTENUATION_ROLLOFF = 1;
   public static final int ATTENUATION_LINEAR = 2;
   public static String EXTENSION_MIDI = ".*[mM][iI][dD][iI]?$";
   public static String PREFIX_URL = "^[hH][tT][tT][pP]://.*";
   private static SoundSystemLogger logger = null;
   private static LinkedList libraries;
   private static LinkedList codecs = null;
   private static LinkedList streamListeners = null;
   private static final Object streamListenersLock = new Object();
   private static int numberNormalChannels = 28;
   private static int numberStreamingChannels = 4;
   private static float masterGain = 1.0F;
   private static int defaultAttenuationModel = 1;
   private static float defaultRolloffFactor = 0.03F;
   private static float dopplerFactor = 0.0F;
   private static float dopplerVelocity = 1.0F;
   private static float defaultFadeDistance = 1000.0F;
   private static String soundFilesPackage = "Sounds/";
   private static int streamingBufferSize = 131072;
   private static int numberStreamingBuffers = 3;
   private static boolean streamQueueFormatsMatch = false;
   private static int maxFileSize = 268435456;
   private static int fileChunkSize = 1048576;
   private static boolean midiCodec = false;
   private static String overrideMIDISynthesizer = "";

   public static void addLibrary(Class var0) throws SoundSystemException {
      if (var0 == null) {
         throw new SoundSystemException("Parameter null in method 'addLibrary'", 2);
      } else if (!Library.class.isAssignableFrom(var0)) {
         throw new SoundSystemException("The specified class does not extend class 'Library' in method 'addLibrary'");
      } else {
         if (libraries == null) {
            libraries = new LinkedList();
         }

         if (!libraries.contains(var0)) {
            libraries.add(var0);
         }

      }
   }

   public static void removeLibrary(Class var0) throws SoundSystemException {
      if (libraries != null && var0 != null) {
         libraries.remove(var0);
      }
   }

   public static LinkedList getLibraries() {
      return libraries;
   }

   public static boolean libraryCompatible(Class var0) {
      if (var0 == null) {
         errorMessage("Parameter 'libraryClass' null in method'librayCompatible'");
         return false;
      } else if (!Library.class.isAssignableFrom(var0)) {
         errorMessage("The specified class does not extend class 'Library' in method 'libraryCompatible'");
         return false;
      } else {
         Object var1 = runMethod(var0, "libraryCompatible", new Class[0], new Object[0]);
         if (var1 == null) {
            errorMessage("Method 'Library.libraryCompatible' returned 'null' in method 'libraryCompatible'");
            return false;
         } else {
            return (Boolean)var1;
         }
      }
   }

   public static String getLibraryTitle(Class var0) {
      if (var0 == null) {
         errorMessage("Parameter 'libraryClass' null in method'getLibrayTitle'");
         return null;
      } else if (!Library.class.isAssignableFrom(var0)) {
         errorMessage("The specified class does not extend class 'Library' in method 'getLibraryTitle'");
         return null;
      } else {
         Object var1 = runMethod(var0, "getTitle", new Class[0], new Object[0]);
         if (var1 == null) {
            errorMessage("Method 'Library.getTitle' returned 'null' in method 'getLibraryTitle'");
            return null;
         } else {
            return (String)var1;
         }
      }
   }

   public static String getLibraryDescription(Class var0) {
      if (var0 == null) {
         errorMessage("Parameter 'libraryClass' null in method'getLibrayDescription'");
         return null;
      } else if (!Library.class.isAssignableFrom(var0)) {
         errorMessage("The specified class does not extend class 'Library' in method 'getLibraryDescription'");
         return null;
      } else {
         Object var1 = runMethod(var0, "getDescription", new Class[0], new Object[0]);
         if (var1 == null) {
            errorMessage("Method 'Library.getDescription' returned 'null' in method 'getLibraryDescription'");
            return null;
         } else {
            return (String)var1;
         }
      }
   }

   public static boolean reverseByteOrder(Class var0) {
      if (var0 == null) {
         errorMessage("Parameter 'libraryClass' null in method'reverseByteOrder'");
         return false;
      } else if (!Library.class.isAssignableFrom(var0)) {
         errorMessage("The specified class does not extend class 'Library' in method 'reverseByteOrder'");
         return false;
      } else {
         Object var1 = runMethod(var0, "reversByteOrder", new Class[0], new Object[0]);
         if (var1 == null) {
            errorMessage("Method 'Library.reverseByteOrder' returned 'null' in method 'getLibraryDescription'");
            return false;
         } else {
            return (Boolean)var1;
         }
      }
   }

   public static void setLogger(SoundSystemLogger var0) {
      logger = var0;
   }

   public static SoundSystemLogger getLogger() {
      return logger;
   }

   public static synchronized void setNumberNormalChannels(int var0) {
      numberNormalChannels = var0;
   }

   public static synchronized int getNumberNormalChannels() {
      return numberNormalChannels;
   }

   public static synchronized void setNumberStreamingChannels(int var0) {
      numberStreamingChannels = var0;
   }

   public static synchronized int getNumberStreamingChannels() {
      return numberStreamingChannels;
   }

   public static synchronized void setMasterGain(float var0) {
      masterGain = var0;
   }

   public static synchronized float getMasterGain() {
      return masterGain;
   }

   public static synchronized void setDefaultAttenuation(int var0) {
      defaultAttenuationModel = var0;
   }

   public static synchronized int getDefaultAttenuation() {
      return defaultAttenuationModel;
   }

   public static synchronized void setDefaultRolloff(float var0) {
      defaultRolloffFactor = var0;
   }

   public static synchronized float getDopplerFactor() {
      return dopplerFactor;
   }

   public static synchronized void setDopplerFactor(float var0) {
      dopplerFactor = var0;
   }

   public static synchronized float getDopplerVelocity() {
      return dopplerVelocity;
   }

   public static synchronized void setDopplerVelocity(float var0) {
      dopplerVelocity = var0;
   }

   public static synchronized float getDefaultRolloff() {
      return defaultRolloffFactor;
   }

   public static synchronized void setDefaultFadeDistance(float var0) {
      defaultFadeDistance = var0;
   }

   public static synchronized float getDefaultFadeDistance() {
      return defaultFadeDistance;
   }

   public static synchronized void setSoundFilesPackage(String var0) {
      soundFilesPackage = var0;
   }

   public static synchronized String getSoundFilesPackage() {
      return soundFilesPackage;
   }

   public static synchronized void setStreamingBufferSize(int var0) {
      streamingBufferSize = var0;
   }

   public static synchronized int getStreamingBufferSize() {
      return streamingBufferSize;
   }

   public static synchronized void setNumberStreamingBuffers(int var0) {
      numberStreamingBuffers = var0;
   }

   public static synchronized int getNumberStreamingBuffers() {
      return numberStreamingBuffers;
   }

   public static synchronized void setStreamQueueFormatsMatch(boolean var0) {
      streamQueueFormatsMatch = var0;
   }

   public static synchronized boolean getStreamQueueFormatsMatch() {
      return streamQueueFormatsMatch;
   }

   public static synchronized void setMaxFileSize(int var0) {
      maxFileSize = var0;
   }

   public static synchronized int getMaxFileSize() {
      return maxFileSize;
   }

   public static synchronized void setFileChunkSize(int var0) {
      fileChunkSize = var0;
   }

   public static synchronized int getFileChunkSize() {
      return fileChunkSize;
   }

   public static synchronized String getOverrideMIDISynthesizer() {
      return overrideMIDISynthesizer;
   }

   public static synchronized void setOverrideMIDISynthesizer(String var0) {
      overrideMIDISynthesizer = var0;
   }

   public static synchronized void setCodec(String var0, Class var1) throws SoundSystemException {
      if (var0 == null) {
         throw new SoundSystemException("Parameter 'extension' null in method 'setCodec'.", 2);
      } else if (var1 == null) {
         throw new SoundSystemException("Parameter 'iCodecClass' null in method 'setCodec'.", 2);
      } else if (!ICodec.class.isAssignableFrom(var1)) {
         throw new SoundSystemException("The specified class does not implement interface 'ICodec' in method 'setCodec'", 3);
      } else {
         if (codecs == null) {
            codecs = new LinkedList();
         }

         ListIterator var2 = codecs.listIterator();

         while(var2.hasNext()) {
            SoundSystemConfig.Codec var3 = (SoundSystemConfig.Codec)var2.next();
            if (var0.matches(var3.extensionRegX)) {
               var2.remove();
            }
         }

         codecs.add(new SoundSystemConfig.Codec(var0, var1));
         if (var0.matches(EXTENSION_MIDI)) {
            midiCodec = true;
         }

      }
   }

   public static synchronized ICodec getCodec(String var0) {
      if (codecs == null) {
         return null;
      } else {
         ListIterator var1 = codecs.listIterator();

         SoundSystemConfig.Codec var2;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            var2 = (SoundSystemConfig.Codec)var1.next();
         } while(!var0.matches(var2.extensionRegX));

         return var2.getInstance();
      }
   }

   public static boolean midiCodec() {
      return midiCodec;
   }

   public static void addStreamListener(IStreamListener var0) {
      Object var1;
      synchronized(var1 = streamListenersLock){}
      if (streamListeners == null) {
         streamListeners = new LinkedList();
      }

      if (!streamListeners.contains(var0)) {
         streamListeners.add(var0);
      }

   }

   public static void removeStreamListener(IStreamListener var0) {
      Object var1;
      synchronized(var1 = streamListenersLock){}
      if (streamListeners == null) {
         streamListeners = new LinkedList();
      }

      if (streamListeners.contains(var0)) {
         streamListeners.remove(var0);
      }

   }

   public static void notifyEOS(String var0, int var1) {
      Object var2;
      synchronized(var2 = streamListenersLock){}
      if (streamListeners != null) {
         (new Thread(var0, var1) {
            final String val$srcName;
            final int val$qSize;

            {
               this.val$srcName = var1;
               this.val$qSize = var2;
            }

            public void run() {
               Object var1;
               synchronized(var1 = SoundSystemConfig.access$000()){}
               if (SoundSystemConfig.access$100() != null) {
                  ListIterator var2 = SoundSystemConfig.access$100().listIterator();

                  while(var2.hasNext()) {
                     IStreamListener var3 = (IStreamListener)var2.next();
                     if (var3 == null) {
                        var2.remove();
                     } else {
                        var3.endOfStream(this.val$srcName, this.val$qSize);
                     }
                  }

               }
            }
         }).start();
      }
   }

   private static void errorMessage(String var0) {
      if (logger != null) {
         logger.errorMessage("SoundSystemConfig", var0, 0);
      }

   }

   private static Object runMethod(Class var0, String var1, Class[] var2, Object[] var3) {
      Method var4 = null;

      try {
         var4 = var0.getMethod(var1, var2);
      } catch (NoSuchMethodException var12) {
         errorMessage("NoSuchMethodException thrown when attempting to call method '" + var1 + "' in " + "method 'runMethod'");
         return null;
      } catch (SecurityException var13) {
         errorMessage("Access denied when attempting to call method '" + var1 + "' in method 'runMethod'");
         return null;
      } catch (NullPointerException var14) {
         errorMessage("NullPointerException thrown when attempting to call method '" + var1 + "' in " + "method 'runMethod'");
         return null;
      }

      if (var4 == null) {
         errorMessage("Method '" + var1 + "' not found for the class " + "specified in method 'runMethod'");
         return null;
      } else {
         Object var5 = null;

         try {
            var5 = var4.invoke((Object)null, var3);
            return var5;
         } catch (IllegalAccessException var7) {
            errorMessage("IllegalAccessException thrown when attempting to invoke method '" + var1 + "' in " + "method 'runMethod'");
            return null;
         } catch (IllegalArgumentException var8) {
            errorMessage("IllegalArgumentException thrown when attempting to invoke method '" + var1 + "' in " + "method 'runMethod'");
            return null;
         } catch (InvocationTargetException var9) {
            errorMessage("InvocationTargetException thrown while attempting to invoke method 'Library.getTitle' in method 'getLibraryTitle'");
            return null;
         } catch (NullPointerException var10) {
            errorMessage("NullPointerException thrown when attempting to invoke method '" + var1 + "' in " + "method 'runMethod'");
            return null;
         } catch (ExceptionInInitializerError var11) {
            errorMessage("ExceptionInInitializerError thrown when attempting to invoke method '" + var1 + "' in " + "method 'runMethod'");
            return null;
         }
      }
   }

   static Object access$000() {
      return streamListenersLock;
   }

   static LinkedList access$100() {
      return streamListeners;
   }

   static void access$200(String var0) {
      errorMessage(var0);
   }

   private static class Codec {
      public String extensionRegX = "";
      public Class iCodecClass;

      public Codec(String var1, Class var2) {
         if (var1 != null && var1.length() > 0) {
            this.extensionRegX = ".*";

            for(int var4 = 0; var4 < var1.length(); ++var4) {
               String var3 = var1.substring(var4, var4 + 1);
               this.extensionRegX = this.extensionRegX + "[" + var3.toLowerCase(Locale.ENGLISH) + var3.toUpperCase(Locale.ENGLISH) + "]";
            }

            this.extensionRegX = this.extensionRegX + "$";
         }

         this.iCodecClass = var2;
      }

      public ICodec getInstance() {
         if (this.iCodecClass == null) {
            return null;
         } else {
            Object var1 = null;

            try {
               var1 = this.iCodecClass.newInstance();
            } catch (InstantiationException var3) {
               this.instantiationErrorMessage();
               return null;
            } catch (IllegalAccessException var4) {
               this.instantiationErrorMessage();
               return null;
            } catch (ExceptionInInitializerError var5) {
               this.instantiationErrorMessage();
               return null;
            } catch (SecurityException var6) {
               this.instantiationErrorMessage();
               return null;
            }

            if (var1 == null) {
               this.instantiationErrorMessage();
               return null;
            } else {
               return (ICodec)var1;
            }
         }
      }

      private void instantiationErrorMessage() {
         SoundSystemConfig.access$200("Unrecognized ICodec implementation in method 'getInstance'.  Ensure that the implementing class has one public, parameterless constructor.");
      }
   }
}
