package joptsimple;

import java.util.Collection;

public interface OptionDeclarer {
  OptionSpecBuilder accepts(String paramString);
  
  OptionSpecBuilder accepts(String paramString1, String paramString2);
  
  OptionSpecBuilder acceptsAll(Collection<String> paramCollection);
  
  OptionSpecBuilder acceptsAll(Collection<String> paramCollection, String paramString);
  
  NonOptionArgumentSpec<String> nonOptions();
  
  NonOptionArgumentSpec<String> nonOptions(String paramString);
  
  void posixlyCorrect(boolean paramBoolean);
  
  void allowsUnrecognizedOptions();
  
  void recognizeAlternativeLongOptions(boolean paramBoolean);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\OptionDeclarer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */