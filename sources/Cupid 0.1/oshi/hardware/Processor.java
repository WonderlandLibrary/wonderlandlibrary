package oshi.hardware;

public interface Processor {
  String getVendor();
  
  void setVendor(String paramString);
  
  String getName();
  
  void setName(String paramString);
  
  String getIdentifier();
  
  void setIdentifier(String paramString);
  
  boolean isCpu64bit();
  
  void setCpu64(boolean paramBoolean);
  
  String getStepping();
  
  void setStepping(String paramString);
  
  String getModel();
  
  void setModel(String paramString);
  
  String getFamily();
  
  void setFamily(String paramString);
  
  float getLoad();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\oshi\hardware\Processor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */