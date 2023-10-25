package org.apache.logging.log4j.core.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive
public class AppenderControlArraySet {
  private static final AtomicReferenceFieldUpdater<AppenderControlArraySet, AppenderControl[]> appenderArrayUpdater = (AtomicReferenceFieldUpdater)AtomicReferenceFieldUpdater.newUpdater(AppenderControlArraySet.class, (Class)AppenderControl[].class, "appenderArray");
  
  private volatile AppenderControl[] appenderArray = AppenderControl.EMPTY_ARRAY;
  
  public boolean add(AppenderControl control) {
    while (true) {
      AppenderControl[] original = this.appenderArray;
      for (AppenderControl existing : original) {
        if (existing.equals(control))
          return false; 
      } 
      AppenderControl[] copy = Arrays.<AppenderControl>copyOf(original, original.length + 1);
      copy[copy.length - 1] = control;
      boolean success = appenderArrayUpdater.compareAndSet(this, original, copy);
      if (success)
        return true; 
    } 
  }
  
  public AppenderControl remove(String name) {
    while (true) {
      boolean success = true;
      AppenderControl[] original = this.appenderArray;
      for (int i = 0; i < original.length; i++) {
        AppenderControl appenderControl = original[i];
        if (Objects.equals(name, appenderControl.getAppenderName())) {
          AppenderControl[] copy = removeElementAt(i, original);
          if (appenderArrayUpdater.compareAndSet(this, original, copy))
            return appenderControl; 
          success = false;
          break;
        } 
      } 
      if (success)
        return null; 
    } 
  }
  
  private AppenderControl[] removeElementAt(int i, AppenderControl[] array) {
    AppenderControl[] result = Arrays.<AppenderControl>copyOf(array, array.length - 1);
    System.arraycopy(array, i + 1, result, i, result.length - i);
    return result;
  }
  
  public Map<String, Appender> asMap() {
    Map<String, Appender> result = new HashMap<>();
    for (AppenderControl appenderControl : this.appenderArray)
      result.put(appenderControl.getAppenderName(), appenderControl.getAppender()); 
    return result;
  }
  
  public AppenderControl[] clear() {
    return appenderArrayUpdater.getAndSet(this, AppenderControl.EMPTY_ARRAY);
  }
  
  public boolean isEmpty() {
    return (this.appenderArray.length == 0);
  }
  
  public AppenderControl[] get() {
    return this.appenderArray;
  }
  
  public String toString() {
    return "AppenderControlArraySet [appenderArray=" + Arrays.toString((Object[])this.appenderArray) + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\AppenderControlArraySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */