package joptsimple.internal;

class Row {
  final String option;
  
  final String description;
  
  Row(String option, String description) {
    this.option = option;
    this.description = description;
  }
  
  public boolean equals(Object that) {
    if (that == this)
      return true; 
    if (that == null || !getClass().equals(that.getClass()))
      return false; 
    Row other = (Row)that;
    return (this.option.equals(other.option) && this.description.equals(other.description));
  }
  
  public int hashCode() {
    return this.option.hashCode() ^ this.description.hashCode();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\internal\Row.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */