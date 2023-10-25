package nl.matsv.viabackwards.api.rewriters;

import nl.matsv.viabackwards.api.BackwardsProtocol;

public abstract class Rewriter<T extends BackwardsProtocol> {
  protected final T protocol;
  
  protected Rewriter(T protocol) {
    this.protocol = protocol;
  }
  
  public void register() {
    registerPackets();
    registerRewrites();
  }
  
  protected abstract void registerPackets();
  
  protected void registerRewrites() {}
  
  public T getProtocol() {
    return this.protocol;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\Rewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */