package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class Languages {
  public static final String ANY = "any";
  
  public static abstract class LanguageSet {
    public static LanguageSet from(Set<String> langs) {
      return langs.isEmpty() ? Languages.NO_LANGUAGES : new Languages.SomeLanguages(langs);
    }
    
    public abstract boolean contains(String param1String);
    
    public abstract String getAny();
    
    public abstract boolean isEmpty();
    
    public abstract boolean isSingleton();
    
    public abstract LanguageSet restrictTo(LanguageSet param1LanguageSet);
  }
  
  public static final class SomeLanguages extends LanguageSet {
    private final Set<String> languages;
    
    private SomeLanguages(Set<String> languages) {
      this.languages = Collections.unmodifiableSet(languages);
    }
    
    public boolean contains(String language) {
      return this.languages.contains(language);
    }
    
    public String getAny() {
      return this.languages.iterator().next();
    }
    
    public Set<String> getLanguages() {
      return this.languages;
    }
    
    public boolean isEmpty() {
      return this.languages.isEmpty();
    }
    
    public boolean isSingleton() {
      return (this.languages.size() == 1);
    }
    
    public Languages.LanguageSet restrictTo(Languages.LanguageSet other) {
      if (other == Languages.NO_LANGUAGES)
        return other; 
      if (other == Languages.ANY_LANGUAGE)
        return this; 
      SomeLanguages sl = (SomeLanguages)other;
      Set<String> ls = new HashSet<String>(Math.min(this.languages.size(), sl.languages.size()));
      for (String lang : this.languages) {
        if (sl.languages.contains(lang))
          ls.add(lang); 
      } 
      return from(ls);
    }
    
    public String toString() {
      return "Languages(" + this.languages.toString() + ")";
    }
  }
  
  private static final Map<NameType, Languages> LANGUAGES = new EnumMap<NameType, Languages>(NameType.class);
  
  private final Set<String> languages;
  
  static {
    for (NameType s : NameType.values())
      LANGUAGES.put(s, getInstance(langResourceName(s))); 
  }
  
  public static Languages getInstance(NameType nameType) {
    return LANGUAGES.get(nameType);
  }
  
  public static Languages getInstance(String languagesResourceName) {
    Set<String> ls = new HashSet<String>();
    InputStream langIS = Languages.class.getClassLoader().getResourceAsStream(languagesResourceName);
    if (langIS == null)
      throw new IllegalArgumentException("Unable to resolve required resource: " + languagesResourceName); 
    Scanner lsScanner = new Scanner(langIS, "UTF-8");
    try {
      boolean inExtendedComment = false;
      while (lsScanner.hasNextLine()) {
        String line = lsScanner.nextLine().trim();
        if (inExtendedComment) {
          if (line.endsWith("*/"))
            inExtendedComment = false; 
          continue;
        } 
        if (line.startsWith("/*")) {
          inExtendedComment = true;
          continue;
        } 
        if (line.length() > 0)
          ls.add(line); 
      } 
    } finally {
      lsScanner.close();
    } 
    return new Languages(Collections.unmodifiableSet(ls));
  }
  
  private static String langResourceName(NameType nameType) {
    return String.format("org/apache/commons/codec/language/bm/%s_languages.txt", new Object[] { nameType.getName() });
  }
  
  public static final LanguageSet NO_LANGUAGES = new LanguageSet() {
      public boolean contains(String language) {
        return false;
      }
      
      public String getAny() {
        throw new NoSuchElementException("Can't fetch any language from the empty language set.");
      }
      
      public boolean isEmpty() {
        return true;
      }
      
      public boolean isSingleton() {
        return false;
      }
      
      public Languages.LanguageSet restrictTo(Languages.LanguageSet other) {
        return this;
      }
      
      public String toString() {
        return "NO_LANGUAGES";
      }
    };
  
  public static final LanguageSet ANY_LANGUAGE = new LanguageSet() {
      public boolean contains(String language) {
        return true;
      }
      
      public String getAny() {
        throw new NoSuchElementException("Can't fetch any language from the any language set.");
      }
      
      public boolean isEmpty() {
        return false;
      }
      
      public boolean isSingleton() {
        return false;
      }
      
      public Languages.LanguageSet restrictTo(Languages.LanguageSet other) {
        return other;
      }
      
      public String toString() {
        return "ANY_LANGUAGE";
      }
    };
  
  private Languages(Set<String> languages) {
    this.languages = languages;
  }
  
  public Set<String> getLanguages() {
    return this.languages;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\language\bm\Languages.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */