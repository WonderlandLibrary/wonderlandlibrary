package optifine;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class PropertiesOrdered extends Properties {
   private Set keysOrdered = new LinkedHashSet();

   public synchronized Object put(Object var1, Object var2) {
      this.keysOrdered.add(var1);
      return super.put(var1, var2);
   }

   public Set keySet() {
      Set var1 = super.keySet();
      this.keysOrdered.retainAll(var1);
      return Collections.unmodifiableSet(this.keysOrdered);
   }

   public synchronized Enumeration keys() {
      return Collections.enumeration(this.keySet());
   }
}
