package com.github.creeper123123321.viafabric.util;

import java.util.concurrent.Future;
import us.myles.ViaVersion.api.platform.TaskId;

public class FutureTaskId implements TaskId {
  private final Future<?> object;
  
  public FutureTaskId(Future<?> object) {
    this.object = object;
  }
  
  public Future<?> getObject() {
    return this.object;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabri\\util\FutureTaskId.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */