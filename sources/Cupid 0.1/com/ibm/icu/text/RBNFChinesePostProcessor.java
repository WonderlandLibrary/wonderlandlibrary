package com.ibm.icu.text;

final class RBNFChinesePostProcessor implements RBNFPostProcessor {
  private boolean longForm;
  
  private int format;
  
  private static final String[] rulesetNames = new String[] { "%traditional", "%simplified", "%accounting", "%time" };
  
  public void init(RuleBasedNumberFormat formatter, String rules) {}
  
  public void process(StringBuffer buf, NFRuleSet ruleSet) {
    String name = ruleSet.getName();
    int i;
    for (i = 0; i < rulesetNames.length; i++) {
      if (rulesetNames[i].equals(name)) {
        this.format = i;
        this.longForm = (i == 1 || i == 3);
        break;
      } 
    } 
    if (this.longForm) {
      for (i = buf.indexOf("*"); i != -1; i = buf.indexOf("*", i))
        buf.delete(i, i + 1); 
      return;
    } 
    String DIAN = "點";
    String[][] markers = { { "萬", "億", "兆", "〇" }, { "万", "亿", "兆", "〇" }, { "萬", "億", "兆", "零" } };
    String[] m = markers[this.format];
    for (int j = 0; j < m.length - 1; j++) {
      int i1 = buf.indexOf(m[j]);
      if (i1 != -1)
        buf.insert(i1 + m[j].length(), '|'); 
    } 
    int x = buf.indexOf("點");
    if (x == -1)
      x = buf.length(); 
    int s = 0;
    int n = -1;
    String ling = markers[this.format][3];
    while (x >= 0) {
      int i1 = buf.lastIndexOf("|", x);
      int nn = buf.lastIndexOf(ling, x);
      int ns = 0;
      if (nn > i1)
        ns = (nn > 0 && buf.charAt(nn - 1) != '*') ? 2 : 1; 
      x = i1 - 1;
      switch (s * 3 + ns) {
        case 0:
          s = ns;
          n = -1;
          continue;
        case 1:
          s = ns;
          n = nn;
          continue;
        case 2:
          s = ns;
          n = -1;
          continue;
        case 3:
          s = ns;
          n = -1;
          continue;
        case 4:
          buf.delete(nn - 1, nn + ling.length());
          s = 0;
          n = -1;
          continue;
        case 5:
          buf.delete(n - 1, n + ling.length());
          s = ns;
          n = -1;
          continue;
        case 6:
          s = ns;
          n = -1;
          continue;
        case 7:
          buf.delete(nn - 1, nn + ling.length());
          s = 0;
          n = -1;
          continue;
        case 8:
          s = ns;
          n = -1;
          continue;
      } 
      throw new IllegalStateException();
    } 
    for (int k = buf.length(); --k >= 0; ) {
      char c = buf.charAt(k);
      if (c == '*' || c == '|')
        buf.delete(k, k + 1); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\RBNFChinesePostProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */