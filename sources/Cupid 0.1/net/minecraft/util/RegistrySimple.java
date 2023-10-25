package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrySimple<K, V> implements IRegistry<K, V> {
  private static final Logger logger = LogManager.getLogger();
  
  protected final Map<K, V> registryObjects = createUnderlyingMap();
  
  protected Map<K, V> createUnderlyingMap() {
    return Maps.newHashMap();
  }
  
  public V getObject(K name) {
    return this.registryObjects.get(name);
  }
  
  public void putObject(K p_82595_1_, V p_82595_2_) {
    Validate.notNull(p_82595_1_);
    Validate.notNull(p_82595_2_);
    if (this.registryObjects.containsKey(p_82595_1_))
      logger.debug("Adding duplicate key '" + p_82595_1_ + "' to registry"); 
    this.registryObjects.put(p_82595_1_, p_82595_2_);
  }
  
  public Set<K> getKeys() {
    return Collections.unmodifiableSet(this.registryObjects.keySet());
  }
  
  public boolean containsKey(K p_148741_1_) {
    return this.registryObjects.containsKey(p_148741_1_);
  }
  
  public Iterator<V> iterator() {
    return this.registryObjects.values().iterator();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\RegistrySimple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */