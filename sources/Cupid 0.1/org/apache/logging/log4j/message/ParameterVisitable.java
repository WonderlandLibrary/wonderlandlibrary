package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({"allocation"})
public interface ParameterVisitable {
  <S> void forEachParameter(ParameterConsumer<S> paramParameterConsumer, S paramS);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\ParameterVisitable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */