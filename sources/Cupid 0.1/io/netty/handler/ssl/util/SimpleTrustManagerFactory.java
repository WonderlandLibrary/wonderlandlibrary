package io.netty.handler.ssl.util;

import io.netty.util.concurrent.FastThreadLocal;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Provider;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.TrustManagerFactorySpi;

public abstract class SimpleTrustManagerFactory extends TrustManagerFactory {
  private static final Provider PROVIDER = new Provider("", 0.0D, "") {
      private static final long serialVersionUID = -2680540247105807895L;
    };
  
  private static final FastThreadLocal<SimpleTrustManagerFactorySpi> CURRENT_SPI = new FastThreadLocal<SimpleTrustManagerFactorySpi>() {
      protected SimpleTrustManagerFactory.SimpleTrustManagerFactorySpi initialValue() {
        return new SimpleTrustManagerFactory.SimpleTrustManagerFactorySpi();
      }
    };
  
  protected SimpleTrustManagerFactory() {
    this("");
  }
  
  protected SimpleTrustManagerFactory(String name) {
    super((TrustManagerFactorySpi)CURRENT_SPI.get(), PROVIDER, name);
    ((SimpleTrustManagerFactorySpi)CURRENT_SPI.get()).init(this);
    CURRENT_SPI.remove();
    if (name == null)
      throw new NullPointerException("name"); 
  }
  
  protected abstract void engineInit(KeyStore paramKeyStore) throws Exception;
  
  protected abstract void engineInit(ManagerFactoryParameters paramManagerFactoryParameters) throws Exception;
  
  protected abstract TrustManager[] engineGetTrustManagers();
  
  static final class SimpleTrustManagerFactorySpi extends TrustManagerFactorySpi {
    private SimpleTrustManagerFactory parent;
    
    void init(SimpleTrustManagerFactory parent) {
      this.parent = parent;
    }
    
    protected void engineInit(KeyStore keyStore) throws KeyStoreException {
      try {
        this.parent.engineInit(keyStore);
      } catch (KeyStoreException e) {
        throw e;
      } catch (Exception e) {
        throw new KeyStoreException(e);
      } 
    }
    
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
      try {
        this.parent.engineInit(managerFactoryParameters);
      } catch (InvalidAlgorithmParameterException e) {
        throw e;
      } catch (Exception e) {
        throw new InvalidAlgorithmParameterException(e);
      } 
    }
    
    protected TrustManager[] engineGetTrustManagers() {
      return this.parent.engineGetTrustManagers();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ss\\util\SimpleTrustManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */