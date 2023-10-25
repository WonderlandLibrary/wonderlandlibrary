package org.apache.logging.log4j.core.pattern;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({"allocation"})
public abstract class NameAbbreviator {
  private static final NameAbbreviator DEFAULT = new NOPAbbreviator();
  
  public static NameAbbreviator getAbbreviator(String pattern) {
    if (pattern.length() > 0) {
      boolean isNegativeNumber;
      String number, trimmed = pattern.trim();
      if (trimmed.isEmpty())
        return DEFAULT; 
      if (trimmed.length() > 1 && trimmed.charAt(0) == '-') {
        isNegativeNumber = true;
        number = trimmed.substring(1);
      } else {
        isNegativeNumber = false;
        number = trimmed;
      } 
      int i = 0;
      while (i < number.length() && number.charAt(i) >= '0' && number
        .charAt(i) <= '9')
        i++; 
      if (i == number.length())
        return new MaxElementAbbreviator(Integer.parseInt(number), isNegativeNumber ? MaxElementAbbreviator.Strategy.DROP : MaxElementAbbreviator.Strategy.RETAIN); 
      ArrayList<PatternAbbreviatorFragment> fragments = new ArrayList<>(5);
      int pos = 0;
      while (pos < trimmed.length() && pos >= 0) {
        int charCount, ellipsisPos = pos;
        if (trimmed.charAt(pos) == '*') {
          charCount = Integer.MAX_VALUE;
          ellipsisPos++;
        } else if (trimmed.charAt(pos) >= '0' && trimmed.charAt(pos) <= '9') {
          charCount = trimmed.charAt(pos) - 48;
          ellipsisPos++;
        } else {
          charCount = 0;
        } 
        char ellipsis = Character.MIN_VALUE;
        if (ellipsisPos < trimmed.length()) {
          ellipsis = trimmed.charAt(ellipsisPos);
          if (ellipsis == '.')
            ellipsis = Character.MIN_VALUE; 
        } 
        fragments.add(new PatternAbbreviatorFragment(charCount, ellipsis));
        pos = trimmed.indexOf('.', pos);
        if (pos == -1)
          break; 
        pos++;
      } 
      return new PatternAbbreviator(fragments);
    } 
    return DEFAULT;
  }
  
  public static NameAbbreviator getDefaultAbbreviator() {
    return DEFAULT;
  }
  
  public abstract void abbreviate(String paramString, StringBuilder paramStringBuilder);
  
  private static class NOPAbbreviator extends NameAbbreviator {
    public void abbreviate(String original, StringBuilder destination) {
      destination.append(original);
    }
  }
  
  private static class MaxElementAbbreviator extends NameAbbreviator {
    private final int count;
    
    private final Strategy strategy;
    
    private enum Strategy {
      DROP(0) {
        void abbreviate(int count, String original, StringBuilder destination) {
          int start = 0;
          for (int i = 0; i < count; i++) {
            int nextStart = original.indexOf('.', start);
            if (nextStart == -1) {
              destination.append(original);
              return;
            } 
            start = nextStart + 1;
          } 
          destination.append(original, start, original.length());
        }
      },
      RETAIN(1) {
        void abbreviate(int count, String original, StringBuilder destination) {
          int end = original.length() - 1;
          for (int i = count; i > 0; i--) {
            end = original.lastIndexOf('.', end - 1);
            if (end == -1) {
              destination.append(original);
              return;
            } 
          } 
          destination.append(original, end + 1, original.length());
        }
      };
      
      final int minCount;
      
      Strategy(int minCount) {
        this.minCount = minCount;
      }
      
      abstract void abbreviate(int param2Int, String param2String, StringBuilder param2StringBuilder);
    }
    
    public MaxElementAbbreviator(int count, Strategy strategy) {
      this.count = Math.max(count, strategy.minCount);
      this.strategy = strategy;
    }
    
    public void abbreviate(String original, StringBuilder destination) {
      this.strategy.abbreviate(this.count, original, destination);
    }
  }
  
  private enum Strategy {
    DROP(0) {
      void abbreviate(int count, String original, StringBuilder destination) {
        int start = 0;
        for (int i = 0; i < count; i++) {
          int nextStart = original.indexOf('.', start);
          if (nextStart == -1) {
            destination.append(original);
            return;
          } 
          start = nextStart + 1;
        } 
        destination.append(original, start, original.length());
      }
    },
    RETAIN(1) {
      void abbreviate(int count, String original, StringBuilder destination) {
        int end = original.length() - 1;
        for (int i = count; i > 0; i--) {
          end = original.lastIndexOf('.', end - 1);
          if (end == -1) {
            destination.append(original);
            return;
          } 
        } 
        destination.append(original, end + 1, original.length());
      }
    };
    
    final int minCount;
    
    Strategy(int minCount) {
      this.minCount = minCount;
    }
    
    abstract void abbreviate(int param1Int, String param1String, StringBuilder param1StringBuilder);
  }
  
  private static final class PatternAbbreviatorFragment {
    private final int charCount;
    
    private final char ellipsis;
    
    PatternAbbreviatorFragment(int charCount, char ellipsis) {
      this.charCount = charCount;
      this.ellipsis = ellipsis;
    }
    
    int abbreviate(String input, int inputIndex, StringBuilder buf) {
      int nextDot = input.indexOf('.', inputIndex);
      if (nextDot < 0) {
        buf.append(input, inputIndex, input.length());
        return nextDot;
      } 
      if (nextDot - inputIndex > this.charCount) {
        buf.append(input, inputIndex, inputIndex + this.charCount);
        if (this.ellipsis != '\000')
          buf.append(this.ellipsis); 
        buf.append('.');
      } else {
        buf.append(input, inputIndex, nextDot + 1);
      } 
      return nextDot + 1;
    }
  }
  
  private static final class PatternAbbreviator extends NameAbbreviator {
    private final NameAbbreviator.PatternAbbreviatorFragment[] fragments;
    
    PatternAbbreviator(List<NameAbbreviator.PatternAbbreviatorFragment> fragments) {
      if (fragments.isEmpty())
        throw new IllegalArgumentException("fragments must have at least one element"); 
      this.fragments = fragments.<NameAbbreviator.PatternAbbreviatorFragment>toArray(new NameAbbreviator.PatternAbbreviatorFragment[0]);
    }
    
    public void abbreviate(String original, StringBuilder destination) {
      int originalIndex = 0;
      int iteration = 0;
      int originalLength = original.length();
      while (originalIndex >= 0 && originalIndex < originalLength)
        originalIndex = fragment(iteration++).abbreviate(original, originalIndex, destination); 
    }
    
    NameAbbreviator.PatternAbbreviatorFragment fragment(int index) {
      return this.fragments[Math.min(index, this.fragments.length - 1)];
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\NameAbbreviator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */