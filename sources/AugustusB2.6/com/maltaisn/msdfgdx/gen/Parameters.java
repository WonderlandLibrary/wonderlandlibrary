// 
// Decompiled by Procyon v0.5.36
// 

package com.maltaisn.msdfgdx.gen;

import java.util.ArrayList;
import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import java.util.SortedSet;
import java.nio.charset.Charset;
import kotlin.io.CloseableKt;
import java.util.Collection;
import java.io.IOException;
import java.io.File;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.jvm.internal.Intrinsics;
import java.util.Map;
import com.beust.jcommander.Parameter;
import java.util.List;

@com.beust.jcommander.Parameters(separators = " =")
public final class Parameters
{
    @Parameter
    private List<String> params;
    @Parameter(names = { "-g", "--msdfgen" }, description = "Path of the msdfgen executable", order = 0)
    private String msdfgen;
    @Parameter(names = { "-o", "--output" }, description = "Output path of generated font textures", order = 1)
    private String output;
    @Parameter(names = { "-t", "--field-type" }, description = "Field type: sdf | psdf | msdf", order = 2)
    private String fieldType;
    @Parameter(names = { "-a", "--alpha-field-type" }, description = "Alpha field type: none | sdf | psdf", order = 3)
    private String alphaFieldType;
    @Parameter(names = { "-s", "--font-size" }, description = "Font size for generated textures", order = 4)
    private int fontSize;
    @Parameter(names = { "-r", "--distance-range" }, description = "Distance range in which SDF is encoded", order = 5)
    private int distanceRange;
    @Parameter(names = { "-d", "--texture-size" }, arity = 2, description = "Maximum width and height of generated atlas pages", order = 6)
    private List<Integer> textureSize;
    @Parameter(names = { "-p", "--padding" }, description = "Padding between glyphs and on the border of the atlas pages", order = 7)
    private int padding;
    @Parameter(names = { "-c", "--charset" }, description = "File containing the characters to use (encoded as UTF-8). Can also be one of: ascii, ascii-extended, latin-0, latin-9, windows-1252, extended.", order = 8)
    private String charset;
    @Parameter(names = { "--compression-level" }, description = "Compression level for generated PNG, from 0 to 9", order = 9)
    private int compressionLevel;
    @Parameter(names = { "-h", "--help" }, description = "Show help message", help = true, order = 10)
    private boolean help;
    private String charList;
    private String outputDir;
    private static final List<Integer> VALID_TEXTURE_SIZES;
    private static final Map<String, String> BUILTIN_CHARSETS;
    public static final Companion Companion;
    
    public final List<String> getParams() {
        return this.params;
    }
    
    public final void setParams(final List<String> <set-?>) {
        Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
        this.params = <set-?>;
    }
    
    public final String getMsdfgen() {
        return this.msdfgen;
    }
    
    public final void setMsdfgen(final String <set-?>) {
        Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
        this.msdfgen = <set-?>;
    }
    
    public final String getOutput() {
        return this.output;
    }
    
    public final void setOutput(final String <set-?>) {
        this.output = <set-?>;
    }
    
    public final String getFieldType() {
        return this.fieldType;
    }
    
    public final void setFieldType(final String <set-?>) {
        Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
        this.fieldType = <set-?>;
    }
    
    public final String getAlphaFieldType() {
        return this.alphaFieldType;
    }
    
    public final void setAlphaFieldType(final String <set-?>) {
        Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
        this.alphaFieldType = <set-?>;
    }
    
    public final int getFontSize() {
        return this.fontSize;
    }
    
    public final void setFontSize(final int <set-?>) {
        this.fontSize = <set-?>;
    }
    
    public final int getDistanceRange() {
        return this.distanceRange;
    }
    
    public final void setDistanceRange(final int <set-?>) {
        this.distanceRange = <set-?>;
    }
    
    public final List<Integer> getTextureSize() {
        return this.textureSize;
    }
    
    public final void setTextureSize(final List<Integer> <set-?>) {
        Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
        this.textureSize = <set-?>;
    }
    
    public final int getPadding() {
        return this.padding;
    }
    
    public final void setPadding(final int <set-?>) {
        this.padding = <set-?>;
    }
    
    public final String getCharset() {
        return this.charset;
    }
    
    public final void setCharset(final String <set-?>) {
        Intrinsics.checkParameterIsNotNull(<set-?>, "<set-?>");
        this.charset = <set-?>;
    }
    
    public final int getCompressionLevel() {
        return this.compressionLevel;
    }
    
    public final void setCompressionLevel(final int <set-?>) {
        this.compressionLevel = <set-?>;
    }
    
    public final boolean getHelp() {
        return this.help;
    }
    
    public final void setHelp(final boolean <set-?>) {
        this.help = <set-?>;
    }
    
    public final String getCharList() {
        return this.charList;
    }
    
    public final String getOutputDir() {
        return this.outputDir;
    }
    
    public final boolean getHasAlphaChannel() {
        return Intrinsics.areEqual(this.alphaFieldType, "none") ^ true;
    }
    
    public final void validate() {
        if (this.params.isEmpty()) {
            Pixmap.paramError("No input file.");
            throw null;
        }
        for (final String inputFile : this.params) {
            if (!new File(inputFile).exists()) {
                Pixmap.paramError("Input file '" + inputFile + "' doesn't exist.");
                throw null;
            }
        }
        if (this.output != null) {
            this.outputDir = this.output;
        }
        final File outputPath;
        (outputPath = new File(this.outputDir)).mkdirs();
        if (!outputPath.exists()) {
            Pixmap.paramError("Could not create output directory at '" + this.outputDir + "'.");
            throw null;
        }
        try {
            Runtime.getRuntime().exec(this.msdfgen);
        }
        catch (IOException ex) {
            Pixmap.paramError("msdfgen executable '" + this.msdfgen + "' doesn't exist or isn't executable.");
            throw null;
        }
        if (!ArraysKt__ArraysJVMKt.listOf(new String[] { "sdf", "psdf", "msdf" }).contains(this.fieldType)) {
            Pixmap.paramError("Invalid field type '" + this.fieldType + '\'');
            throw null;
        }
        if (!ArraysKt__ArraysJVMKt.listOf(new String[] { "none", "sdf", "psdf" }).contains(this.alphaFieldType)) {
            Pixmap.paramError("Invalid field type '" + this.alphaFieldType + '\'');
            throw null;
        }
        if (this.fontSize < 8) {
            Pixmap.paramError("Font size must be at least 8.");
            throw null;
        }
        if (this.distanceRange <= 0) {
            Pixmap.paramError("Distance range must be at least 1.");
            throw null;
        }
        boolean b = false;
        Label_0453: {
            final Iterable $this$any$iv;
            if (!(($this$any$iv = this.textureSize) instanceof Collection) || !((Collection)$this$any$iv).isEmpty()) {
                final Iterator<Number> iterator2 = (Iterator<Number>)$this$any$iv.iterator();
                while (iterator2.hasNext()) {
                    final int d = iterator2.next().intValue();
                    if (!Parameters.VALID_TEXTURE_SIZES.contains(d)) {
                        b = true;
                        break Label_0453;
                    }
                }
            }
            b = false;
        }
        if (b) {
            Pixmap.paramError("Texture size must be power of two between 32 and 65536.");
            throw null;
        }
        if (this.padding < 0) {
            Pixmap.paramError("Padding must be at least 0.");
            throw null;
        }
        Parameters parameters = this;
        String joinToString$default$1494b5c;
        if ((joinToString$default$1494b5c = Parameters.BUILTIN_CHARSETS.get(this.charset)) == null) {
            String text$default$2f123cdb;
            try {
                parameters = this;
                text$default$2f123cdb = CloseableKt.readText$default$2f123cdb(new File(this.charset), null, 1);
            }
            catch (IOException ex2) {
                Pixmap.paramError("Could not read charset file.");
                throw null;
            }
            joinToString$default$1494b5c = CollectionsKt___CollectionsJvmKt.joinToString$default$1494b5c(StringsKt__StringsKt.toSortedSet(text$default$2f123cdb), "", null, null, 0, null, null, 62);
        }
        parameters.charList = joinToString$default$1494b5c;
    }
    
    public Parameters() {
        this.params = new ArrayList<String>();
        this.msdfgen = "msdfgen.exe";
        this.fieldType = "msdf";
        this.alphaFieldType = "sdf";
        this.fontSize = 32;
        this.distanceRange = 5;
        this.textureSize = ArraysKt__ArraysJVMKt.listOf(new Integer[] { 512, 512 });
        this.padding = 2;
        this.charset = "ascii";
        this.compressionLevel = 9;
        this.charList = "";
        this.outputDir = System.getProperty("user.dir");
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: iconst_0       
        //     5: invokespecial   com/maltaisn/msdfgdx/gen/Parameters$Companion.<init>:(B)V
        //     8: putstatic       com/maltaisn/msdfgdx/gen/Parameters.Companion:Lcom/maltaisn/msdfgdx/gen/Parameters$Companion;
        //    11: new             Ljava/util/ArrayList;
        //    14: dup            
        //    15: bipush          12
        //    17: invokespecial   java/util/ArrayList.<init>:(I)V
        //    20: astore_0       
        //    21: iconst_0       
        //    22: istore_1       
        //    23: iload_1        
        //    24: bipush          12
        //    26: if_icmpge       56
        //    29: iload_1        
        //    30: istore_2       
        //    31: aload_0        
        //    32: iload_2        
        //    33: istore_2       
        //    34: astore_3       
        //    35: iconst_1       
        //    36: iload_2        
        //    37: iconst_5       
        //    38: iadd           
        //    39: ishl           
        //    40: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    43: astore_2       
        //    44: aload_3        
        //    45: aload_2        
        //    46: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //    49: pop            
        //    50: iinc            1, 1
        //    53: goto            23
        //    56: aload_0        
        //    57: checkcast       Ljava/util/List;
        //    60: putstatic       com/maltaisn/msdfgdx/gen/Parameters.VALID_TEXTURE_SIZES:Ljava/util/List;
        //    63: bipush          7
        //    65: anewarray       Lkotlin/Pair;
        //    68: dup            
        //    69: iconst_0       
        //    70: ldc             "test"
        //    72: ldc             " A@jp&\u00c2O!-$"
        //    74: invokestatic    com/badlogic/gdx/graphics/Pixmap.to:(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
        //    77: aastore        
        //    78: dup            
        //    79: iconst_1       
        //    80: ldc             "ascii"
        //    82: ldc             " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
        //    84: invokestatic    com/badlogic/gdx/graphics/Pixmap.to:(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
        //    87: aastore        
        //    88: dup            
        //    89: iconst_2       
        //    90: ldc             "ascii-extended"
        //    92: ldc             " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|} ~\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc¢£¥\u20a7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1ªº¿\u2310¬½¼¡«»\u03b1\u00df\u0393\u03c0\u03a3\u03c3µ\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u03c6\u03b5\u2229\u2261±\u2265\u2264\u00f7\u2248°\u2219·\u221a\u207f²\u25a0"
        //    94: invokestatic    com/badlogic/gdx/graphics/Pixmap.to:(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
        //    97: aastore        
        //    98: dup            
        //    99: iconst_3       
        //   100: ldc             "latin-0"
        //   102: ldc             " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ ¡¢£¤¥¦§¨©ª«¬\u00ad®¯°±²³´µ¶·¸¹º»¼½¾¿\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u00d6\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe\u00ff"
        //   104: invokestatic    com/badlogic/gdx/graphics/Pixmap.to:(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
        //   107: aastore        
        //   108: dup            
        //   109: iconst_4       
        //   110: ldc             "latin-9"
        //   112: ldc             " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ ¡¢£\u20ac¥\u0160§\u0161©ª«¬\u00ad®¯°±²³\u017dµ¶·\u017e¹º»\u0152\u0153\u0178¿\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u00d6\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe\u00ff"
        //   114: invokestatic    com/badlogic/gdx/graphics/Pixmap.to:(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
        //   117: aastore        
        //   118: dup            
        //   119: iconst_5       
        //   120: ldc             "windows-1252"
        //   122: ldc             " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ ¡¢£¤¥¦§¨©ª«¬\u00ad®¯°±²³´µ¶·¸¹º»¼½¾¿\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u00d6\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe\u00ff\u0152\u0153\u0160\u0161\u0178\u017d\u017e\u0192\u02c6\u02dc\u2013\u2014\u2018\u2019\u201a\u201c\u201d\u201e\u2020\u2021\u2022\u2026\u2030\u2039\u203a\u20ac\u2122"
        //   124: invokestatic    com/badlogic/gdx/graphics/Pixmap.to:(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
        //   127: aastore        
        //   128: dup            
        //   129: bipush          6
        //   131: ldc             "extended"
        //   133: ldc             " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~ ¡¢£¤¥¦§¨©ª«¬\u00ad®¯°±²³´µ¶·¸¹º»¼½¾¿\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u00d6\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe\u00ff\u0100\u0101\u0102\u0103\u0104\u0105\u0106\u0107\u0108\u0109\u010a\u010b\u010c\u010d\u010e\u010f\u0110\u0111\u0112\u0113\u0114\u0115\u0116\u0117\u0118\u0119\u011a\u011b\u011c\u011d\u011e\u011f\u0120\u0121\u0122\u0123\u0124\u0125\u0126\u0127\u0128\u0129\u012a\u012b\u012c\u012d\u012e\u012f\u0130\u0131\u0132\u0133\u0134\u0135\u0136\u0137\u0138\u0139\u013a\u013b\u013c\u013d\u013e\u013f\u0140\u0141\u0142\u0143\u0144\u0145\u0146\u0147\u0148\u0149\u014a\u014b\u014c\u014d\u014e\u014f\u0150\u0151\u0152\u0153\u0154\u0155\u0156\u0157\u0158\u0159\u015a\u015b\u015c\u015d\u015e\u015f\u0160\u0161\u0162\u0163\u0164\u0165\u0166\u0167\u0168\u0169\u016a\u016b\u016c\u016d\u016e\u016f\u0170\u0171\u0172\u0173\u0174\u0175\u0176\u0177\u0178\u0179\u017a\u017b\u017c\u017d\u017e\u017f\u0374\u0375\u037a\u037b\u037c\u037d\u037e\u0384\u0385\u0386\u0387\u0388\u0389\u038a\u038c\u038e\u038f\u0390\u0391\u0392\u0393\u0394\u0395\u0396\u0397\u0398\u0399\u039a\u039b\u039c\u039d\u039e\u039f\u03a0\u03a1\u03a3\u03a4\u03a5\u03a6\u03a7\u03a8\u03a9\u03aa\u03ab\u03ac\u03ad\u03ae\u03af\u03b0\u03b1\u03b2\u03b3\u03b4\u03b5\u03b6\u03b7\u03b8\u03b9\u03ba\u03bb\u03bc\u03bd\u03be\u03bf\u03c0\u03c1\u03c2\u03c3\u03c4\u03c5\u03c6\u03c7\u03c8\u03c9\u03ca\u03cb\u03cc\u03cd\u03ce\u03d0\u03d1\u03d2\u03d3\u03d4\u03d5\u03d6\u03d7\u03d8\u03d9\u03da\u03db\u03dc\u03dd\u03de\u03df\u03e0\u03e1\u03e2\u03e3\u03e4\u03e5\u03e6\u03e7\u03e8\u03e9\u03ea\u03eb\u03ec\u03ed\u03ee\u03ef\u03f0\u03f1\u03f2\u03f3\u03f4\u03f5\u03f6\u03f7\u03f8\u03f9\u03fa\u03fb\u03fc\u03fd\u03fe\u03ff\u0400\u0401\u0402\u0403\u0404\u0405\u0406\u0407\u0408\u0409\u040a\u040b\u040c\u040d\u040e\u040f\u0410\u0411\u0412\u0413\u0414\u0415\u0416\u0417\u0418\u0419\u041a\u041b\u041c\u041d\u041e\u041f\u0420\u0421\u0422\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042a\u042b\u042c\u042d\u042e\u042f\u0430\u0431\u0432\u0433\u0434\u0435\u0436\u0437\u0438\u0439\u043a\u043b\u043c\u043d\u043e\u043f\u0440\u0441\u0442\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044a\u044b\u044c\u044d\u044e\u044f\u0450\u0451\u0452\u0453\u0454\u0455\u0456\u0457\u0458\u0459\u045a\u045b\u045c\u045d\u045e\u045f\u0460\u0461\u0462\u0463\u0464\u0465\u0466\u0467\u0468\u0469\u046a\u046b\u046c\u046d\u046e\u046f\u0470\u0471\u0472\u0473\u0474\u0475\u0476\u0477\u0478\u0479\u047a\u047b\u047c\u047d\u047e\u047f\u0480\u0481\u0482\u0483\u0484\u0485\u0486\u0487\u0488\u0489\u048a\u048b\u048c\u048d\u048e\u048f\u0490\u0491\u0492\u0493\u0494\u0495\u0496\u0497\u0498\u0499\u049a\u049b\u049c\u049d\u049e\u049f\u04a0\u04a1\u04a2\u04a3\u04a4\u04a5\u04a6\u04a7\u04a8\u04a9\u04aa\u04ab\u04ac\u04ad\u04ae\u04af\u04b0\u04b1\u04b2\u04b3\u04b4\u04b5\u04b6\u04b7\u04b8\u04b9\u04ba\u04bb\u04bc\u04bd\u04be\u04bf\u04c0\u04c1\u04c2\u04c3\u04c4\u04c5\u04c6\u04c7\u04c8\u04c9\u04ca\u04cb\u04cc\u04cd\u04ce\u04cf\u04d0\u04d1\u04d2\u04d3\u04d4\u04d5\u04d6\u04d7\u04d8\u04d9\u04da\u04db\u04dc\u04dd\u04de\u04df\u04e0\u04e1\u04e2\u04e3\u04e4\u04e5\u04e6\u04e7\u04e8\u04e9\u04ea\u04eb\u04ec\u04ed\u04ee\u04ef\u04f0\u04f1\u04f2\u04f3\u04f4\u04f5\u04f6\u04f7\u04f8\u04f9\u04fa\u04fb\u04fc\u04fd\u04fe\u04ff\u0500\u0501\u0502\u0503\u0504\u0505\u0506\u0507\u0508\u0509\u050a\u050b\u050c\u050d\u050e\u050f\u0510\u0511\u0512\u0513\u0514\u0515\u0516\u0517\u0518\u0519\u051a\u051b\u051c\u051d\u051e\u051f\u0520\u0521\u0522\u0523\u0524\u0525\u0526\u0527\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u200b\u200c\u200d\u200e\u200f\u2012\u2013\u2014\u2015\u2016\u2017\u2018\u2019\u201a\u201b\u201c\u201d\u201e\u201f\u2020\u2021\u2022\u2026\u202a\u202b\u202c\u202d\u202e\u202f\u2030\u2032\u2033\u2034\u2039\u203a\u203c\u203e\u2044\u205e\u206a\u206b\u206c\u206d\u206e\u206f\u20a0\u20a1\u20a2\u20a3\u20a4\u20a5\u20a6\u20a7\u20a8\u20a9\u20ab\u20ac\u20ad\u20ae\u20af\u20b0\u20b1\u20b2\u20b3\u20b4\u20b5\u20b9\u20ba\u2c60\u2c61\u2c62\u2c63\u2c64\u2c65\u2c66\u2c67\u2c68\u2c69\u2c6a\u2c6b\u2c6c\u2c6d\u2c71\u2c72\u2c73\u2c74\u2c75\u2c76\u2c77"
        //   135: invokestatic    com/badlogic/gdx/graphics/Pixmap.to:(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
        //   138: aastore        
        //   139: invokestatic    kotlin/collections/MapsKt.mapOf:([Lkotlin/Pair;)Ljava/util/Map;
        //   142: putstatic       com/maltaisn/msdfgdx/gen/Parameters.BUILTIN_CHARSETS:Ljava/util/Map;
        //   145: return         
        //    StackMapTable: 00 02 FD 00 17 07 00 3D 01 FA 00 20
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
