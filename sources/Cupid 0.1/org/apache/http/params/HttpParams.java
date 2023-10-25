package org.apache.http.params;

@Deprecated
public interface HttpParams {
  Object getParameter(String paramString);
  
  HttpParams setParameter(String paramString, Object paramObject);
  
  HttpParams copy();
  
  boolean removeParameter(String paramString);
  
  long getLongParameter(String paramString, long paramLong);
  
  HttpParams setLongParameter(String paramString, long paramLong);
  
  int getIntParameter(String paramString, int paramInt);
  
  HttpParams setIntParameter(String paramString, int paramInt);
  
  double getDoubleParameter(String paramString, double paramDouble);
  
  HttpParams setDoubleParameter(String paramString, double paramDouble);
  
  boolean getBooleanParameter(String paramString, boolean paramBoolean);
  
  HttpParams setBooleanParameter(String paramString, boolean paramBoolean);
  
  boolean isParameterTrue(String paramString);
  
  boolean isParameterFalse(String paramString);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\params\HttpParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */