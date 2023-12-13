// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data;

import com.viaversion.viaversion.libs.gson.JsonArray;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.data.IntArrayMappings;
import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import com.google.common.collect.BiMap;
import java.util.Map;
import com.viaversion.viaversion.api.data.MappingDataBase;

public class MappingData extends MappingDataBase
{
    private final Map<String, Integer[]> blockTags;
    private final Map<String, Integer[]> itemTags;
    private final Map<String, Integer[]> fluidTags;
    private final BiMap<Short, String> oldEnchantmentsIds;
    private final Map<String, String> translateMapping;
    private final Map<String, String> mojangTranslation;
    private final BiMap<String, String> channelMappings;
    
    public MappingData() {
        super("1.12", "1.13");
        this.blockTags = new HashMap<String, Integer[]>();
        this.itemTags = new HashMap<String, Integer[]>();
        this.fluidTags = new HashMap<String, Integer[]>();
        this.oldEnchantmentsIds = (BiMap<Short, String>)HashBiMap.create();
        this.translateMapping = new HashMap<String, String>();
        this.mojangTranslation = new HashMap<String, String>();
        this.channelMappings = (BiMap<String, String>)HashBiMap.create();
    }
    
    public void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockTags:Ljava/util/Map;
        //     5: aload_2         /* newMappings */
        //     6: ldc             "block_tags"
        //     8: invokevirtual   com/viaversion/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/gson/JsonObject;
        //    11: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.loadTags:(Ljava/util/Map;Lcom/viaversion/viaversion/libs/gson/JsonObject;)V
        //    14: aload_0         /* this */
        //    15: aload_0         /* this */
        //    16: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.itemTags:Ljava/util/Map;
        //    19: aload_2         /* newMappings */
        //    20: ldc             "item_tags"
        //    22: invokevirtual   com/viaversion/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/gson/JsonObject;
        //    25: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.loadTags:(Ljava/util/Map;Lcom/viaversion/viaversion/libs/gson/JsonObject;)V
        //    28: aload_0         /* this */
        //    29: aload_0         /* this */
        //    30: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.fluidTags:Ljava/util/Map;
        //    33: aload_2         /* newMappings */
        //    34: ldc             "fluid_tags"
        //    36: invokevirtual   com/viaversion/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/gson/JsonObject;
        //    39: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.loadTags:(Ljava/util/Map;Lcom/viaversion/viaversion/libs/gson/JsonObject;)V
        //    42: aload_0         /* this */
        //    43: aload_0         /* this */
        //    44: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.oldEnchantmentsIds:Lcom/google/common/collect/BiMap;
        //    47: aload_1         /* oldMappings */
        //    48: ldc             "legacy_enchantments"
        //    50: invokevirtual   com/viaversion/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/gson/JsonObject;
        //    53: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.loadEnchantments:(Ljava/util/Map;Lcom/viaversion/viaversion/libs/gson/JsonObject;)V
        //    56: aload_0         /* this */
        //    57: invokestatic    com/viaversion/viaversion/api/data/IntArrayMappings.builder:()Lcom/viaversion/viaversion/api/data/Mappings$Builder;
        //    60: bipush          72
        //    62: invokevirtual   com/viaversion/viaversion/api/data/Mappings$Builder.customEntrySize:(I)Lcom/viaversion/viaversion/api/data/Mappings$Builder;
        //    65: aload_1         /* oldMappings */
        //    66: ldc             "legacy_enchantments"
        //    68: invokevirtual   com/viaversion/viaversion/libs/gson/JsonObject.getAsJsonObject:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/gson/JsonObject;
        //    71: invokevirtual   com/viaversion/viaversion/api/data/Mappings$Builder.unmapped:(Lcom/viaversion/viaversion/libs/gson/JsonObject;)Lcom/viaversion/viaversion/api/data/Mappings$Builder;
        //    74: aload_2         /* newMappings */
        //    75: ldc             "enchantments"
        //    77: invokevirtual   com/viaversion/viaversion/libs/gson/JsonObject.getAsJsonArray:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/gson/JsonArray;
        //    80: invokevirtual   com/viaversion/viaversion/api/data/Mappings$Builder.mapped:(Lcom/viaversion/viaversion/libs/gson/JsonArray;)Lcom/viaversion/viaversion/api/data/Mappings$Builder;
        //    83: invokevirtual   com/viaversion/viaversion/api/data/Mappings$Builder.build:()Lcom/viaversion/viaversion/api/data/Mappings;
        //    86: putfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.enchantmentMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //    89: invokestatic    com/viaversion/viaversion/api/Via.getConfig:()Lcom/viaversion/viaversion/api/configuration/ViaVersionConfig;
        //    92: invokeinterface com/viaversion/viaversion/api/configuration/ViaVersionConfig.isSnowCollisionFix:()Z
        //    97: ifeq            115
        //   100: aload_0         /* this */
        //   101: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   104: sipush          1248
        //   107: sipush          3416
        //   110: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   115: invokestatic    com/viaversion/viaversion/api/Via.getConfig:()Lcom/viaversion/viaversion/api/configuration/ViaVersionConfig;
        //   118: invokeinterface com/viaversion/viaversion/api/configuration/ViaVersionConfig.isInfestedBlocksFix:()Z
        //   123: ifeq            213
        //   126: aload_0         /* this */
        //   127: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   130: sipush          1552
        //   133: iconst_1       
        //   134: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   139: aload_0         /* this */
        //   140: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   143: sipush          1553
        //   146: bipush          14
        //   148: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   153: aload_0         /* this */
        //   154: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   157: sipush          1554
        //   160: sipush          3983
        //   163: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   168: aload_0         /* this */
        //   169: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   172: sipush          1555
        //   175: sipush          3984
        //   178: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   183: aload_0         /* this */
        //   184: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   187: sipush          1556
        //   190: sipush          3985
        //   193: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   198: aload_0         /* this */
        //   199: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.blockMappings:Lcom/viaversion/viaversion/api/data/Mappings;
        //   202: sipush          1557
        //   205: sipush          3986
        //   208: invokeinterface com/viaversion/viaversion/api/data/Mappings.setNewId:(II)V
        //   213: ldc             "channelmappings-1.13.json"
        //   215: invokestatic    com/viaversion/viaversion/api/data/MappingDataLoader.loadFromDataDir:(Ljava/lang/String;)Lcom/viaversion/viaversion/libs/gson/JsonObject;
        //   218: astore          object
        //   220: aload           object
        //   222: ifnull          350
        //   225: aload           object
        //   227: invokevirtual   com/viaversion/viaversion/libs/gson/JsonObject.entrySet:()Ljava/util/Set;
        //   230: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   235: astore          5
        //   237: aload           5
        //   239: invokeinterface java/util/Iterator.hasNext:()Z
        //   244: ifeq            350
        //   247: aload           5
        //   249: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   254: checkcast       Ljava/util/Map$Entry;
        //   257: astore          entry
        //   259: aload           entry
        //   261: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   266: checkcast       Ljava/lang/String;
        //   269: astore          oldChannel
        //   271: aload           entry
        //   273: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   278: checkcast       Lcom/viaversion/viaversion/libs/gson/JsonElement;
        //   281: invokevirtual   com/viaversion/viaversion/libs/gson/JsonElement.getAsString:()Ljava/lang/String;
        //   284: astore          newChannel
        //   286: aload           newChannel
        //   288: invokestatic    com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.isValid1_13Channel:(Ljava/lang/String;)Z
        //   291: ifne            333
        //   294: invokestatic    com/viaversion/viaversion/api/Via.getPlatform:()Lcom/viaversion/viaversion/api/platform/ViaPlatform;
        //   297: invokeinterface com/viaversion/viaversion/api/platform/ViaPlatform.getLogger:()Ljava/util/logging/Logger;
        //   302: new             Ljava/lang/StringBuilder;
        //   305: dup            
        //   306: invokespecial   java/lang/StringBuilder.<init>:()V
        //   309: ldc             "Channel '"
        //   311: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   314: aload           newChannel
        //   316: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   319: ldc             "' is not a valid 1.13 plugin channel, please check your configuration!"
        //   321: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   324: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   327: invokevirtual   java/util/logging/Logger.warning:(Ljava/lang/String;)V
        //   330: goto            237
        //   333: aload_0         /* this */
        //   334: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.channelMappings:Lcom/google/common/collect/BiMap;
        //   337: aload           oldChannel
        //   339: aload           newChannel
        //   341: invokeinterface com/google/common/collect/BiMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   346: pop            
        //   347: goto            237
        //   350: invokestatic    com/viaversion/viaversion/util/GsonUtil.getGson:()Lcom/viaversion/viaversion/libs/gson/Gson;
        //   353: new             Ljava/io/InputStreamReader;
        //   356: dup            
        //   357: ldc             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData;.class
        //   359: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   362: ldc             "assets/viaversion/data/mapping-lang-1.12-1.13.json"
        //   364: invokevirtual   java/lang/ClassLoader.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //   367: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   370: new             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData$1;
        //   373: dup            
        //   374: aload_0         /* this */
        //   375: invokespecial   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData$1.<init>:(Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData;)V
        //   378: invokevirtual   com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData$1.getType:()Ljava/lang/reflect/Type;
        //   381: invokevirtual   com/viaversion/viaversion/libs/gson/Gson.fromJson:(Ljava/io/Reader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //   384: checkcast       Ljava/util/Map;
        //   387: astore          translateData
        //   389: new             Ljava/io/InputStreamReader;
        //   392: dup            
        //   393: ldc             Lcom/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData;.class
        //   395: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //   398: ldc_w           "assets/viaversion/data/en_US.properties"
        //   401: invokevirtual   java/lang/ClassLoader.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //   404: getstatic       java/nio/charset/StandardCharsets.UTF_8:Ljava/nio/charset/Charset;
        //   407: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
        //   410: astore          reader
        //   412: aconst_null    
        //   413: astore          8
        //   415: aload           reader
        //   417: invokestatic    com/google/common/io/CharStreams.toString:(Ljava/lang/Readable;)Ljava/lang/String;
        //   420: ldc_w           "\n"
        //   423: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   426: astore          lines
        //   428: aload           reader
        //   430: ifnull          515
        //   433: aload           8
        //   435: ifnull          458
        //   438: aload           reader
        //   440: invokevirtual   java/io/Reader.close:()V
        //   443: goto            515
        //   446: astore          9
        //   448: aload           8
        //   450: aload           9
        //   452: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   455: goto            515
        //   458: aload           reader
        //   460: invokevirtual   java/io/Reader.close:()V
        //   463: goto            515
        //   466: astore          9
        //   468: aload           9
        //   470: astore          8
        //   472: aload           9
        //   474: athrow         
        //   475: astore          10
        //   477: aload           reader
        //   479: ifnull          512
        //   482: aload           8
        //   484: ifnull          507
        //   487: aload           reader
        //   489: invokevirtual   java/io/Reader.close:()V
        //   492: goto            512
        //   495: astore          11
        //   497: aload           8
        //   499: aload           11
        //   501: invokevirtual   java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
        //   504: goto            512
        //   507: aload           reader
        //   509: invokevirtual   java/io/Reader.close:()V
        //   512: aload           10
        //   514: athrow         
        //   515: aload           lines
        //   517: astore          7
        //   519: aload           7
        //   521: arraylength    
        //   522: istore          8
        //   524: iconst_0       
        //   525: istore          9
        //   527: iload           9
        //   529: iload           8
        //   531: if_icmpge       662
        //   534: aload           7
        //   536: iload           9
        //   538: aaload         
        //   539: astore          line
        //   541: aload           line
        //   543: invokevirtual   java/lang/String.isEmpty:()Z
        //   546: ifeq            552
        //   549: goto            656
        //   552: aload           line
        //   554: ldc_w           "="
        //   557: iconst_2       
        //   558: invokevirtual   java/lang/String.split:(Ljava/lang/String;I)[Ljava/lang/String;
        //   561: astore          keyAndTranslation
        //   563: aload           keyAndTranslation
        //   565: arraylength    
        //   566: iconst_2       
        //   567: if_icmpeq       573
        //   570: goto            656
        //   573: aload           keyAndTranslation
        //   575: iconst_0       
        //   576: aaload         
        //   577: astore          key
        //   579: aload           translateData
        //   581: aload           key
        //   583: invokeinterface java/util/Map.containsKey:(Ljava/lang/Object;)Z
        //   588: ifne            623
        //   591: aload           keyAndTranslation
        //   593: iconst_1       
        //   594: aaload         
        //   595: ldc_w           "%(\\d\\$)?d"
        //   598: ldc_w           "%$1s"
        //   601: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   604: astore          translation
        //   606: aload_0         /* this */
        //   607: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.mojangTranslation:Ljava/util/Map;
        //   610: aload           key
        //   612: aload           translation
        //   614: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   619: pop            
        //   620: goto            656
        //   623: aload           translateData
        //   625: aload           key
        //   627: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   632: checkcast       Ljava/lang/String;
        //   635: astore          dataValue
        //   637: aload           dataValue
        //   639: ifnull          656
        //   642: aload_0         /* this */
        //   643: getfield        com/viaversion/viaversion/protocols/protocol1_13to1_12_2/data/MappingData.translateMapping:Ljava/util/Map;
        //   646: aload           key
        //   648: aload           dataValue
        //   650: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   655: pop            
        //   656: iinc            9, 1
        //   659: goto            527
        //   662: goto            672
        //   665: astore          e
        //   667: aload           e
        //   669: invokevirtual   java/io/IOException.printStackTrace:()V
        //   672: return         
        //    StackMapTable: 00 15 FB 00 73 FB 00 61 FD 00 17 07 00 4B 07 00 A5 FE 00 5F 07 00 09 07 00 B1 07 00 B1 FF 00 10 00 05 07 00 02 07 00 4B 07 00 4B 07 00 4B 07 00 4B 00 00 FF 00 5F 00 09 07 00 02 07 00 4B 07 00 4B 07 00 4B 07 00 4B 07 00 0B 07 01 25 07 01 20 07 00 45 00 01 07 00 45 0B FF 00 07 00 09 07 00 02 07 00 4B 07 00 4B 07 00 4B 07 00 4B 07 00 0B 00 07 01 20 07 00 45 00 01 07 00 45 48 07 00 45 FF 00 13 00 0B 07 00 02 07 00 4B 07 00 4B 07 00 4B 07 00 4B 07 00 0B 00 07 01 20 07 00 45 00 07 00 45 00 01 07 00 45 0B 04 FF 00 02 00 07 07 00 02 07 00 4B 07 00 4B 07 00 4B 07 00 4B 07 00 0B 07 01 25 00 00 FE 00 0B 07 01 25 01 01 FC 00 18 07 00 B1 FC 00 14 07 01 25 FC 00 31 07 00 B1 F8 00 20 FF 00 05 00 06 07 00 02 07 00 4B 07 00 4B 07 00 4B 07 00 4B 07 00 0B 00 00 42 07 00 47 06
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  438    443    446    458    Ljava/lang/Throwable;
        //  415    428    466    475    Ljava/lang/Throwable;
        //  415    428    475    515    Any
        //  487    492    495    507    Ljava/lang/Throwable;
        //  466    477    475    515    Any
        //  389    662    665    672    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:837)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2086)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:144)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    protected Mappings loadFromObject(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings, final String key) {
        if (key.equals("blocks")) {
            return IntArrayMappings.builder().customEntrySize(4084).unmapped(oldMappings.getAsJsonObject("blocks")).mapped(newMappings.getAsJsonObject("blockstates")).build();
        }
        return super.loadFromObject(oldMappings, newMappings, diffMappings, key);
    }
    
    public static String validateNewChannel(final String newId) {
        if (!isValid1_13Channel(newId)) {
            return null;
        }
        final int separatorIndex = newId.indexOf(58);
        if (separatorIndex == -1) {
            return "minecraft:" + newId;
        }
        if (separatorIndex == 0) {
            return "minecraft" + newId;
        }
        return newId;
    }
    
    public static boolean isValid1_13Channel(final String channelId) {
        return channelId.matches("([0-9a-z_.-]+:)?[0-9a-z_/.-]+");
    }
    
    private void loadTags(final Map<String, Integer[]> output, final JsonObject newTags) {
        for (final Map.Entry<String, JsonElement> entry : newTags.entrySet()) {
            final JsonArray ids = entry.getValue().getAsJsonArray();
            final Integer[] idsArray = new Integer[ids.size()];
            for (int i = 0; i < ids.size(); ++i) {
                idsArray[i] = ids.get(i).getAsInt();
            }
            output.put(entry.getKey(), idsArray);
        }
    }
    
    private void loadEnchantments(final Map<Short, String> output, final JsonObject enchantments) {
        for (final Map.Entry<String, JsonElement> enchantment : enchantments.entrySet()) {
            output.put(Short.parseShort(enchantment.getKey()), enchantment.getValue().getAsString());
        }
    }
    
    public Map<String, Integer[]> getBlockTags() {
        return this.blockTags;
    }
    
    public Map<String, Integer[]> getItemTags() {
        return this.itemTags;
    }
    
    public Map<String, Integer[]> getFluidTags() {
        return this.fluidTags;
    }
    
    public BiMap<Short, String> getOldEnchantmentsIds() {
        return this.oldEnchantmentsIds;
    }
    
    public Map<String, String> getTranslateMapping() {
        return this.translateMapping;
    }
    
    public Map<String, String> getMojangTranslation() {
        return this.mojangTranslation;
    }
    
    public BiMap<String, String> getChannelMappings() {
        return this.channelMappings;
    }
}
