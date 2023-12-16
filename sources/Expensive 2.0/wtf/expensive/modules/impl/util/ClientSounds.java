package wtf.expensive.modules.impl.util;

import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * @author dedinside
 * @since 27.06.2023
 */
@FunctionAnnotation(name = "Client Sounds", type = Type.Util)
public class ClientSounds extends Function {

    @Override
    public void onEvent(Event event) {

    }

}
