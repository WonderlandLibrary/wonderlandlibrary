package gnu.trove.strategy;

import java.io.Serializable;

public interface HashingStrategy extends Serializable {
   long serialVersionUID = 5674097166776615540L;

   int computeHashCode(Object var1);

   boolean equals(Object var1, Object var2);
}
