package paulscode.sound;

import java.net.URL;
import javax.sound.sampled.AudioFormat;

public interface ICodec {
   void reverseByteOrder(boolean var1);

   boolean initialize(URL var1);

   boolean initialized();

   SoundBuffer read();

   SoundBuffer readAll();

   boolean endOfStream();

   void cleanup();

   AudioFormat getAudioFormat();
}
