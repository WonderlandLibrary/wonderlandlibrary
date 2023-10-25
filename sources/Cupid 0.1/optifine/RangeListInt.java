package optifine;

public class RangeListInt {
  private RangeInt[] ranges = new RangeInt[0];
  
  public void addRange(RangeInt p_addRange_1_) {
    this.ranges = (RangeInt[])Config.addObjectToArray((Object[])this.ranges, p_addRange_1_);
  }
  
  public boolean isInRange(int p_isInRange_1_) {
    for (int i = 0; i < this.ranges.length; i++) {
      RangeInt rangeint = this.ranges[i];
      if (rangeint.isInRange(p_isInRange_1_))
        return true; 
    } 
    return false;
  }
  
  public int getCountRanges() {
    return this.ranges.length;
  }
  
  public RangeInt getRange(int p_getRange_1_) {
    return this.ranges[p_getRange_1_];
  }
  
  public String toString() {
    StringBuffer stringbuffer = new StringBuffer();
    stringbuffer.append("[");
    for (int i = 0; i < this.ranges.length; i++) {
      RangeInt rangeint = this.ranges[i];
      if (i > 0)
        stringbuffer.append(", "); 
      stringbuffer.append(rangeint.toString());
    } 
    stringbuffer.append("]");
    return stringbuffer.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\optifine\RangeListInt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */