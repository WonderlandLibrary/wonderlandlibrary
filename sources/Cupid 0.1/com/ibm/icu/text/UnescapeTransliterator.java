package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;

class UnescapeTransliterator extends Transliterator {
  private char[] spec;
  
  private static final char END = '￿';
  
  static void register() {
    Transliterator.registerFactory("Hex-Any/Unicode", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new UnescapeTransliterator("Hex-Any/Unicode", new char[] { '\002', Character.MIN_VALUE, '\020', '\004', '\006', 'U', '+', Character.MAX_VALUE });
          }
        });
    Transliterator.registerFactory("Hex-Any/Java", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new UnescapeTransliterator("Hex-Any/Java", new char[] { '\002', Character.MIN_VALUE, '\020', '\004', '\004', '\\', 'u', Character.MAX_VALUE });
          }
        });
    Transliterator.registerFactory("Hex-Any/C", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new UnescapeTransliterator("Hex-Any/C", new char[] { 
                  '\002', Character.MIN_VALUE, '\020', '\004', '\004', '\\', 'u', '\002', Character.MIN_VALUE, '\020', 
                  '\b', '\b', '\\', 'U', Character.MAX_VALUE });
          }
        });
    Transliterator.registerFactory("Hex-Any/XML", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new UnescapeTransliterator("Hex-Any/XML", new char[] { '\003', '\001', '\020', '\001', '\006', '&', '#', 'x', ';', Character.MAX_VALUE });
          }
        });
    Transliterator.registerFactory("Hex-Any/XML10", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new UnescapeTransliterator("Hex-Any/XML10", new char[] { '\002', '\001', '\n', '\001', '\007', '&', '#', ';', Character.MAX_VALUE });
          }
        });
    Transliterator.registerFactory("Hex-Any/Perl", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new UnescapeTransliterator("Hex-Any/Perl", new char[] { '\003', '\001', '\020', '\001', '\006', '\\', 'x', '{', '}', Character.MAX_VALUE });
          }
        });
    Transliterator.registerFactory("Hex-Any", new Transliterator.Factory() {
          public Transliterator getInstance(String ID) {
            return new UnescapeTransliterator("Hex-Any", new char[] { 
                  '\002', Character.MIN_VALUE, '\020', '\004', '\006', 'U', '+', '\002', Character.MIN_VALUE, '\020', 
                  '\004', '\004', '\\', 'u', '\002', Character.MIN_VALUE, '\020', '\b', '\b', '\\', 
                  'U', '\003', '\001', '\020', '\001', '\006', '&', '#', 'x', ';', 
                  '\002', '\001', '\n', '\001', '\007', '&', '#', ';', '\003', '\001', 
                  '\020', '\001', '\006', '\\', 'x', '{', '}', Character.MAX_VALUE });
          }
        });
  }
  
  UnescapeTransliterator(String ID, char[] spec) {
    super(ID, null);
    this.spec = spec;
  }
  
  protected void handleTransliterate(Replaceable text, Transliterator.Position pos, boolean isIncremental) {
    // Byte code:
    //   0: aload_2
    //   1: getfield start : I
    //   4: istore #4
    //   6: aload_2
    //   7: getfield limit : I
    //   10: istore #5
    //   12: iload #4
    //   14: iload #5
    //   16: if_icmpge -> 452
    //   19: iconst_0
    //   20: istore #7
    //   22: aload_0
    //   23: getfield spec : [C
    //   26: iload #7
    //   28: caload
    //   29: ldc 65535
    //   31: if_icmpeq -> 426
    //   34: aload_0
    //   35: getfield spec : [C
    //   38: iload #7
    //   40: iinc #7, 1
    //   43: caload
    //   44: istore #8
    //   46: aload_0
    //   47: getfield spec : [C
    //   50: iload #7
    //   52: iinc #7, 1
    //   55: caload
    //   56: istore #9
    //   58: aload_0
    //   59: getfield spec : [C
    //   62: iload #7
    //   64: iinc #7, 1
    //   67: caload
    //   68: istore #10
    //   70: aload_0
    //   71: getfield spec : [C
    //   74: iload #7
    //   76: iinc #7, 1
    //   79: caload
    //   80: istore #11
    //   82: aload_0
    //   83: getfield spec : [C
    //   86: iload #7
    //   88: iinc #7, 1
    //   91: caload
    //   92: istore #12
    //   94: iload #4
    //   96: istore #13
    //   98: iconst_1
    //   99: istore #14
    //   101: iconst_0
    //   102: istore #6
    //   104: iload #6
    //   106: iload #8
    //   108: if_icmpge -> 176
    //   111: iload #13
    //   113: iload #5
    //   115: if_icmplt -> 136
    //   118: iload #6
    //   120: ifle -> 136
    //   123: iload_3
    //   124: ifeq -> 130
    //   127: goto -> 452
    //   130: iconst_0
    //   131: istore #14
    //   133: goto -> 176
    //   136: aload_1
    //   137: iload #13
    //   139: iinc #13, 1
    //   142: invokeinterface charAt : (I)C
    //   147: istore #15
    //   149: iload #15
    //   151: aload_0
    //   152: getfield spec : [C
    //   155: iload #7
    //   157: iload #6
    //   159: iadd
    //   160: caload
    //   161: if_icmpeq -> 170
    //   164: iconst_0
    //   165: istore #14
    //   167: goto -> 176
    //   170: iinc #6, 1
    //   173: goto -> 104
    //   176: iload #14
    //   178: ifeq -> 413
    //   181: iconst_0
    //   182: istore #15
    //   184: iconst_0
    //   185: istore #16
    //   187: iload #13
    //   189: iload #5
    //   191: if_icmplt -> 208
    //   194: iload #13
    //   196: iload #4
    //   198: if_icmple -> 271
    //   201: iload_3
    //   202: ifeq -> 271
    //   205: goto -> 452
    //   208: aload_1
    //   209: iload #13
    //   211: invokeinterface char32At : (I)I
    //   216: istore #17
    //   218: iload #17
    //   220: iload #10
    //   222: invokestatic digit : (II)I
    //   225: istore #18
    //   227: iload #18
    //   229: ifge -> 235
    //   232: goto -> 271
    //   235: iload #13
    //   237: iload #17
    //   239: invokestatic getCharCount : (I)I
    //   242: iadd
    //   243: istore #13
    //   245: iload #15
    //   247: iload #10
    //   249: imul
    //   250: iload #18
    //   252: iadd
    //   253: istore #15
    //   255: iinc #16, 1
    //   258: iload #16
    //   260: iload #12
    //   262: if_icmpne -> 268
    //   265: goto -> 271
    //   268: goto -> 187
    //   271: iload #16
    //   273: iload #11
    //   275: if_icmplt -> 282
    //   278: iconst_1
    //   279: goto -> 283
    //   282: iconst_0
    //   283: istore #14
    //   285: iload #14
    //   287: ifeq -> 413
    //   290: iconst_0
    //   291: istore #6
    //   293: iload #6
    //   295: iload #9
    //   297: if_icmpge -> 370
    //   300: iload #13
    //   302: iload #5
    //   304: if_icmplt -> 327
    //   307: iload #13
    //   309: iload #4
    //   311: if_icmple -> 321
    //   314: iload_3
    //   315: ifeq -> 321
    //   318: goto -> 452
    //   321: iconst_0
    //   322: istore #14
    //   324: goto -> 370
    //   327: aload_1
    //   328: iload #13
    //   330: iinc #13, 1
    //   333: invokeinterface charAt : (I)C
    //   338: istore #17
    //   340: iload #17
    //   342: aload_0
    //   343: getfield spec : [C
    //   346: iload #7
    //   348: iload #8
    //   350: iadd
    //   351: iload #6
    //   353: iadd
    //   354: caload
    //   355: if_icmpeq -> 364
    //   358: iconst_0
    //   359: istore #14
    //   361: goto -> 370
    //   364: iinc #6, 1
    //   367: goto -> 293
    //   370: iload #14
    //   372: ifeq -> 413
    //   375: iload #15
    //   377: invokestatic valueOf : (I)Ljava/lang/String;
    //   380: astore #17
    //   382: aload_1
    //   383: iload #4
    //   385: iload #13
    //   387: aload #17
    //   389: invokeinterface replace : (IILjava/lang/String;)V
    //   394: iload #5
    //   396: iload #13
    //   398: iload #4
    //   400: isub
    //   401: aload #17
    //   403: invokevirtual length : ()I
    //   406: isub
    //   407: isub
    //   408: istore #5
    //   410: goto -> 426
    //   413: iload #7
    //   415: iload #8
    //   417: iload #9
    //   419: iadd
    //   420: iadd
    //   421: istore #7
    //   423: goto -> 22
    //   426: iload #4
    //   428: iload #5
    //   430: if_icmpge -> 12
    //   433: iload #4
    //   435: aload_1
    //   436: iload #4
    //   438: invokeinterface char32At : (I)I
    //   443: invokestatic getCharCount : (I)I
    //   446: iadd
    //   447: istore #4
    //   449: goto -> 12
    //   452: aload_2
    //   453: dup
    //   454: getfield contextLimit : I
    //   457: iload #5
    //   459: aload_2
    //   460: getfield limit : I
    //   463: isub
    //   464: iadd
    //   465: putfield contextLimit : I
    //   468: aload_2
    //   469: iload #5
    //   471: putfield limit : I
    //   474: aload_2
    //   475: iload #4
    //   477: putfield start : I
    //   480: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #140	-> 0
    //   #141	-> 6
    //   #145	-> 12
    //   #149	-> 19
    //   #152	-> 34
    //   #153	-> 46
    //   #154	-> 58
    //   #155	-> 70
    //   #156	-> 82
    //   #160	-> 94
    //   #161	-> 98
    //   #163	-> 101
    //   #164	-> 111
    //   #165	-> 118
    //   #170	-> 123
    //   #171	-> 127
    //   #173	-> 130
    //   #174	-> 133
    //   #177	-> 136
    //   #178	-> 149
    //   #179	-> 164
    //   #180	-> 167
    //   #163	-> 170
    //   #184	-> 176
    //   #185	-> 181
    //   #186	-> 184
    //   #188	-> 187
    //   #190	-> 194
    //   #191	-> 205
    //   #195	-> 208
    //   #196	-> 218
    //   #197	-> 227
    //   #198	-> 232
    //   #200	-> 235
    //   #201	-> 245
    //   #202	-> 255
    //   #203	-> 265
    //   #205	-> 268
    //   #207	-> 271
    //   #209	-> 285
    //   #210	-> 290
    //   #211	-> 300
    //   #213	-> 307
    //   #214	-> 318
    //   #216	-> 321
    //   #217	-> 324
    //   #219	-> 327
    //   #220	-> 340
    //   #221	-> 358
    //   #222	-> 361
    //   #210	-> 364
    //   #226	-> 370
    //   #228	-> 375
    //   #229	-> 382
    //   #230	-> 394
    //   #235	-> 410
    //   #240	-> 413
    //   #241	-> 423
    //   #243	-> 426
    //   #244	-> 433
    //   #248	-> 452
    //   #249	-> 468
    //   #250	-> 474
    //   #251	-> 480
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   149	21	15	c	C
    //   218	50	17	ch	I
    //   227	41	18	digit	I
    //   340	24	17	c	C
    //   382	31	17	str	Ljava/lang/String;
    //   184	229	15	u	I
    //   187	226	16	digitCount	I
    //   46	377	8	prefixLen	I
    //   58	365	9	suffixLen	I
    //   70	353	10	radix	I
    //   82	341	11	minDigits	I
    //   94	329	12	maxDigits	I
    //   98	325	13	s	I
    //   101	322	14	match	Z
    //   104	322	6	i	I
    //   22	430	7	ipat	I
    //   0	481	0	this	Lcom/ibm/icu/text/UnescapeTransliterator;
    //   0	481	1	text	Lcom/ibm/icu/text/Replaceable;
    //   0	481	2	pos	Lcom/ibm/icu/text/Transliterator$Position;
    //   0	481	3	isIncremental	Z
    //   6	475	4	start	I
    //   12	469	5	limit	I
  }
  
  public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
    UnicodeSet myFilter = getFilterAsUnicodeSet(inputFilter);
    UnicodeSet items = new UnicodeSet();
    StringBuilder buffer = new StringBuilder();
    int i;
    for (i = 0; this.spec[i] != Character.MAX_VALUE; ) {
      int end = i + this.spec[i] + this.spec[i + 1] + 5;
      int radix = this.spec[i + 2];
      int j;
      for (j = 0; j < radix; j++)
        Utility.appendNumber(buffer, j, radix, 0); 
      for (j = i + 5; j < end; j++)
        items.add(this.spec[j]); 
      i = end;
    } 
    items.addAll(buffer.toString());
    items.retainAll(myFilter);
    if (items.size() > 0) {
      sourceSet.addAll(items);
      targetSet.addAll(0, 1114111);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\UnescapeTransliterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */