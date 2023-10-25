package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Comparator;

public final class Range<T> implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final Comparator<T> comparator;
  
  private final T minimum;
  
  private final T maximum;
  
  private transient int hashCode;
  
  private transient String toString;
  
  public static <T extends Comparable<T>> Range<T> is(T element) {
    return between(element, element, null);
  }
  
  public static <T> Range<T> is(T element, Comparator<T> comparator) {
    return between(element, element, comparator);
  }
  
  public static <T extends Comparable<T>> Range<T> between(T fromInclusive, T toInclusive) {
    return between(fromInclusive, toInclusive, null);
  }
  
  public static <T> Range<T> between(T fromInclusive, T toInclusive, Comparator<T> comparator) {
    return new Range<T>(fromInclusive, toInclusive, comparator);
  }
  
  private Range(T element1, T element2, Comparator<T> comp) {
    if (element1 == null || element2 == null)
      throw new IllegalArgumentException("Elements in a range must not be null: element1=" + element1 + ", element2=" + element2); 
    if (comp == null) {
      this.comparator = ComparableComparator.INSTANCE;
    } else {
      this.comparator = comp;
    } 
    if (this.comparator.compare(element1, element2) < 1) {
      this.minimum = element1;
      this.maximum = element2;
    } else {
      this.minimum = element2;
      this.maximum = element1;
    } 
  }
  
  public T getMinimum() {
    return this.minimum;
  }
  
  public T getMaximum() {
    return this.maximum;
  }
  
  public Comparator<T> getComparator() {
    return this.comparator;
  }
  
  public boolean isNaturalOrdering() {
    return (this.comparator == ComparableComparator.INSTANCE);
  }
  
  public boolean contains(T element) {
    if (element == null)
      return false; 
    return (this.comparator.compare(element, this.minimum) > -1 && this.comparator.compare(element, this.maximum) < 1);
  }
  
  public boolean isAfter(T element) {
    if (element == null)
      return false; 
    return (this.comparator.compare(element, this.minimum) < 0);
  }
  
  public boolean isStartedBy(T element) {
    if (element == null)
      return false; 
    return (this.comparator.compare(element, this.minimum) == 0);
  }
  
  public boolean isEndedBy(T element) {
    if (element == null)
      return false; 
    return (this.comparator.compare(element, this.maximum) == 0);
  }
  
  public boolean isBefore(T element) {
    if (element == null)
      return false; 
    return (this.comparator.compare(element, this.maximum) > 0);
  }
  
  public int elementCompareTo(T element) {
    if (element == null)
      throw new NullPointerException("Element is null"); 
    if (isAfter(element))
      return -1; 
    if (isBefore(element))
      return 1; 
    return 0;
  }
  
  public boolean containsRange(Range<T> otherRange) {
    if (otherRange == null)
      return false; 
    return (contains(otherRange.minimum) && contains(otherRange.maximum));
  }
  
  public boolean isAfterRange(Range<T> otherRange) {
    if (otherRange == null)
      return false; 
    return isAfter(otherRange.maximum);
  }
  
  public boolean isOverlappedBy(Range<T> otherRange) {
    if (otherRange == null)
      return false; 
    return (otherRange.contains(this.minimum) || otherRange.contains(this.maximum) || contains(otherRange.minimum));
  }
  
  public boolean isBeforeRange(Range<T> otherRange) {
    if (otherRange == null)
      return false; 
    return isBefore(otherRange.minimum);
  }
  
  public Range<T> intersectionWith(Range<T> other) {
    if (!isOverlappedBy(other))
      throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", new Object[] { other })); 
    if (equals(other))
      return this; 
    T min = (getComparator().compare(this.minimum, other.minimum) < 0) ? other.minimum : this.minimum;
    T max = (getComparator().compare(this.maximum, other.maximum) < 0) ? this.maximum : other.maximum;
    return between(min, max, getComparator());
  }
  
  public boolean equals(Object obj) {
    if (obj == this)
      return true; 
    if (obj == null || obj.getClass() != getClass())
      return false; 
    Range<T> range = (Range<T>)obj;
    return (this.minimum.equals(range.minimum) && this.maximum.equals(range.maximum));
  }
  
  public int hashCode() {
    int result = this.hashCode;
    if (this.hashCode == 0) {
      result = 17;
      result = 37 * result + getClass().hashCode();
      result = 37 * result + this.minimum.hashCode();
      result = 37 * result + this.maximum.hashCode();
      this.hashCode = result;
    } 
    return result;
  }
  
  public String toString() {
    String result = this.toString;
    if (result == null) {
      StringBuilder buf = new StringBuilder(32);
      buf.append('[');
      buf.append(this.minimum);
      buf.append("..");
      buf.append(this.maximum);
      buf.append(']');
      result = buf.toString();
      this.toString = result;
    } 
    return result;
  }
  
  public String toString(String format) {
    return String.format(format, new Object[] { this.minimum, this.maximum, this.comparator });
  }
  
  private enum ComparableComparator implements Comparator {
    INSTANCE;
    
    public int compare(Object obj1, Object obj2) {
      return ((Comparable<Object>)obj1).compareTo(obj2);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\Range.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */