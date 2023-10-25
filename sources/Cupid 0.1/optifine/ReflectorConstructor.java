package optifine;

import java.lang.reflect.Constructor;

public class ReflectorConstructor {
  private ReflectorClass reflectorClass = null;
  
  private Class[] parameterTypes = null;
  
  private boolean checked = false;
  
  private Constructor targetConstructor = null;
  
  public ReflectorConstructor(ReflectorClass p_i84_1_, Class[] p_i84_2_) {
    this.reflectorClass = p_i84_1_;
    this.parameterTypes = p_i84_2_;
    Constructor constructor = getTargetConstructor();
  }
  
  public Constructor getTargetConstructor() {
    if (this.checked)
      return this.targetConstructor; 
    this.checked = true;
    Class oclass = this.reflectorClass.getTargetClass();
    if (oclass == null)
      return null; 
    try {
      this.targetConstructor = findConstructor(oclass, this.parameterTypes);
      if (this.targetConstructor == null)
        Config.dbg("(Reflector) Constructor not present: " + oclass.getName() + ", params: " + Config.arrayToString((Object[])this.parameterTypes)); 
      if (this.targetConstructor != null)
        this.targetConstructor.setAccessible(true); 
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    } 
    return this.targetConstructor;
  }
  
  private static Constructor findConstructor(Class p_findConstructor_0_, Class[] p_findConstructor_1_) {
    Constructor[] aconstructor = (Constructor[])p_findConstructor_0_.getDeclaredConstructors();
    for (int i = 0; i < aconstructor.length; i++) {
      Constructor constructor = aconstructor[i];
      Class[] aclass = constructor.getParameterTypes();
      if (Reflector.matchesTypes(p_findConstructor_1_, aclass))
        return constructor; 
    } 
    return null;
  }
  
  public boolean exists() {
    return this.checked ? ((this.targetConstructor != null)) : ((getTargetConstructor() != null));
  }
  
  public void deactivate() {
    this.checked = true;
    this.targetConstructor = null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\optifine\ReflectorConstructor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */