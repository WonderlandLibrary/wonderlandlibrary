package org.apache.logging.log4j.core.config;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class OrderComparator implements Comparator<Class<?>>, Serializable {
  private static final long serialVersionUID = 1L;
  
  private static final Comparator<Class<?>> INSTANCE = new OrderComparator();
  
  public static Comparator<Class<?>> getInstance() {
    return INSTANCE;
  }
  
  public int compare(Class<?> lhs, Class<?> rhs) {
    Order lhsOrder = (Order)((Class)Objects.<Class<?>>requireNonNull(lhs, "lhs")).getAnnotation(Order.class);
    Order rhsOrder = (Order)((Class)Objects.<Class<?>>requireNonNull(rhs, "rhs")).getAnnotation(Order.class);
    if (lhsOrder == null && rhsOrder == null)
      return 0; 
    if (rhsOrder == null)
      return -1; 
    if (lhsOrder == null)
      return 1; 
    return Integer.signum(rhsOrder.value() - lhsOrder.value());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\OrderComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */