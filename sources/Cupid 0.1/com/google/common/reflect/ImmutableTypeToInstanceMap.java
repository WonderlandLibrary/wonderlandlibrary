package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

@Beta
public final class ImmutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {
  private final ImmutableMap<TypeToken<? extends B>, B> delegate;
  
  public static <B> ImmutableTypeToInstanceMap<B> of() {
    return new ImmutableTypeToInstanceMap<B>(ImmutableMap.of());
  }
  
  public static <B> Builder<B> builder() {
    return new Builder<B>();
  }
  
  @Beta
  public static final class Builder<B> {
    private final ImmutableMap.Builder<TypeToken<? extends B>, B> mapBuilder = ImmutableMap.builder();
    
    public <T extends B> Builder<B> put(Class<T> key, T value) {
      this.mapBuilder.put(TypeToken.of(key), value);
      return this;
    }
    
    public <T extends B> Builder<B> put(TypeToken<T> key, T value) {
      this.mapBuilder.put(key.rejectTypeVariables(), value);
      return this;
    }
    
    public ImmutableTypeToInstanceMap<B> build() {
      return new ImmutableTypeToInstanceMap<B>(this.mapBuilder.build());
    }
    
    private Builder() {}
  }
  
  private ImmutableTypeToInstanceMap(ImmutableMap<TypeToken<? extends B>, B> delegate) {
    this.delegate = delegate;
  }
  
  public <T extends B> T getInstance(TypeToken<T> type) {
    return trustedGet(type.rejectTypeVariables());
  }
  
  public <T extends B> T putInstance(TypeToken<T> type, T value) {
    throw new UnsupportedOperationException();
  }
  
  public <T extends B> T getInstance(Class<T> type) {
    return trustedGet(TypeToken.of(type));
  }
  
  public <T extends B> T putInstance(Class<T> type, T value) {
    throw new UnsupportedOperationException();
  }
  
  protected Map<TypeToken<? extends B>, B> delegate() {
    return (Map<TypeToken<? extends B>, B>)this.delegate;
  }
  
  private <T extends B> T trustedGet(TypeToken<T> type) {
    return (T)this.delegate.get(type);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\reflect\ImmutableTypeToInstanceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */