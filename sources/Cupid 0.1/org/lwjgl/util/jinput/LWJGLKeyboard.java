package org.lwjgl.util.jinput;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Event;
import net.java.games.input.Keyboard;
import org.lwjgl.input.Keyboard;

final class LWJGLKeyboard extends Keyboard {
  LWJGLKeyboard() {
    super("LWJGLKeyboard", createComponents(), new net.java.games.input.Controller[0], new net.java.games.input.Rumbler[0]);
  }
  
  private static Component[] createComponents() {
    List<Key> components = new ArrayList<Key>();
    Field[] vkey_fields = Keyboard.class.getFields();
    for (Field vkey_field : vkey_fields) {
      try {
        if (Modifier.isStatic(vkey_field.getModifiers()) && vkey_field.getType() == int.class && vkey_field.getName().startsWith("KEY_")) {
          int vkey_code = vkey_field.getInt(null);
          Component.Identifier.Key key_id = KeyMap.map(vkey_code);
          if (key_id != Component.Identifier.Key.UNKNOWN)
            components.add(new Key(key_id, vkey_code)); 
        } 
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } 
    } 
    return components.<Component>toArray(new Component[components.size()]);
  }
  
  public synchronized void pollDevice() throws IOException {
    if (!Keyboard.isCreated())
      return; 
    Keyboard.poll();
    for (Component component : getComponents()) {
      Key key = (Key)component;
      key.update();
    } 
  }
  
  protected synchronized boolean getNextDeviceEvent(Event event) throws IOException {
    if (!Keyboard.isCreated())
      return false; 
    if (!Keyboard.next())
      return false; 
    int lwjgl_key = Keyboard.getEventKey();
    if (lwjgl_key == 0)
      return false; 
    Component.Identifier.Key key_id = KeyMap.map(lwjgl_key);
    if (key_id == null)
      return false; 
    Component key = getComponent((Component.Identifier)key_id);
    if (key == null)
      return false; 
    float value = Keyboard.getEventKeyState() ? 1.0F : 0.0F;
    event.set(key, value, Keyboard.getEventNanoseconds());
    return true;
  }
  
  private static final class Key extends AbstractComponent {
    private final int lwjgl_key;
    
    private float value;
    
    Key(Component.Identifier.Key key_id, int lwjgl_key) {
      super(key_id.getName(), (Component.Identifier)key_id);
      this.lwjgl_key = lwjgl_key;
    }
    
    public void update() {
      this.value = Keyboard.isKeyDown(this.lwjgl_key) ? 1.0F : 0.0F;
    }
    
    protected float poll() {
      return this.value;
    }
    
    public boolean isRelative() {
      return false;
    }
    
    public boolean isAnalog() {
      return false;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\jinput\LWJGLKeyboard.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */