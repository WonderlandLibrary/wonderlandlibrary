package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.procedure.TObjectObjectProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;

public interface TMap extends Map {
   Object putIfAbsent(Object var1, Object var2);

   boolean forEachKey(TObjectProcedure var1);

   boolean forEachValue(TObjectProcedure var1);

   boolean forEachEntry(TObjectObjectProcedure var1);

   boolean retainEntries(TObjectObjectProcedure var1);

   void transformValues(TObjectFunction var1);
}
