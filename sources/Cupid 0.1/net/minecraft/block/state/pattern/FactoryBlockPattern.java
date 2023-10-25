package net.minecraft.block.state.pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import net.minecraft.block.state.BlockWorldState;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class FactoryBlockPattern {
  private static final Joiner COMMA_JOIN = Joiner.on(",");
  
  private final List<String[]> depth = Lists.newArrayList();
  
  private final Map<Character, Predicate<BlockWorldState>> symbolMap = Maps.newHashMap();
  
  private int aisleHeight;
  
  private int rowWidth;
  
  private FactoryBlockPattern() {
    this.symbolMap.put(Character.valueOf(' '), Predicates.alwaysTrue());
  }
  
  public FactoryBlockPattern aisle(String... aisle) {
    if (!ArrayUtils.isEmpty((Object[])aisle) && !StringUtils.isEmpty(aisle[0])) {
      if (this.depth.isEmpty()) {
        this.aisleHeight = aisle.length;
        this.rowWidth = aisle[0].length();
      } 
      if (aisle.length != this.aisleHeight)
        throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + aisle.length + ")"); 
      for (String s : aisle) {
        if (s.length() != this.rowWidth)
          throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + s.length() + ")"); 
        for (char c0 : s.toCharArray()) {
          if (!this.symbolMap.containsKey(Character.valueOf(c0)))
            this.symbolMap.put(Character.valueOf(c0), (Predicate<BlockWorldState>)null); 
        } 
      } 
      this.depth.add(aisle);
      return this;
    } 
    throw new IllegalArgumentException("Empty pattern for aisle");
  }
  
  public static FactoryBlockPattern start() {
    return new FactoryBlockPattern();
  }
  
  public FactoryBlockPattern where(char symbol, Predicate<BlockWorldState> blockMatcher) {
    this.symbolMap.put(Character.valueOf(symbol), blockMatcher);
    return this;
  }
  
  public BlockPattern build() {
    return new BlockPattern(makePredicateArray());
  }
  
  private Predicate<BlockWorldState>[][][] makePredicateArray() {
    checkMissingPredicates();
    Predicate[][][] arrayOfPredicate = (Predicate[][][])Array.newInstance(Predicate.class, new int[] { this.depth.size(), this.aisleHeight, this.rowWidth });
    for (int i = 0; i < this.depth.size(); i++) {
      for (int j = 0; j < this.aisleHeight; j++) {
        for (int k = 0; k < this.rowWidth; k++)
          arrayOfPredicate[i][j][k] = this.symbolMap.get(Character.valueOf(((String[])this.depth.get(i))[j].charAt(k))); 
      } 
    } 
    return (Predicate<BlockWorldState>[][][])arrayOfPredicate;
  }
  
  private void checkMissingPredicates() {
    List<Character> list = Lists.newArrayList();
    for (Map.Entry<Character, Predicate<BlockWorldState>> entry : this.symbolMap.entrySet()) {
      if (entry.getValue() == null)
        list.add(entry.getKey()); 
    } 
    if (!list.isEmpty())
      throw new IllegalStateException("Predicates for character(s) " + COMMA_JOIN.join(list) + " are missing"); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\state\pattern\FactoryBlockPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */