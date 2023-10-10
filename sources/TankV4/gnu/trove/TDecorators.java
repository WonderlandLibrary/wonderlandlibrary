package gnu.trove;

import gnu.trove.decorator.TByteByteMapDecorator;
import gnu.trove.decorator.TByteCharMapDecorator;
import gnu.trove.decorator.TByteDoubleMapDecorator;
import gnu.trove.decorator.TByteFloatMapDecorator;
import gnu.trove.decorator.TByteIntMapDecorator;
import gnu.trove.decorator.TByteListDecorator;
import gnu.trove.decorator.TByteLongMapDecorator;
import gnu.trove.decorator.TByteObjectMapDecorator;
import gnu.trove.decorator.TByteSetDecorator;
import gnu.trove.decorator.TByteShortMapDecorator;
import gnu.trove.decorator.TCharByteMapDecorator;
import gnu.trove.decorator.TCharCharMapDecorator;
import gnu.trove.decorator.TCharDoubleMapDecorator;
import gnu.trove.decorator.TCharFloatMapDecorator;
import gnu.trove.decorator.TCharIntMapDecorator;
import gnu.trove.decorator.TCharListDecorator;
import gnu.trove.decorator.TCharLongMapDecorator;
import gnu.trove.decorator.TCharObjectMapDecorator;
import gnu.trove.decorator.TCharSetDecorator;
import gnu.trove.decorator.TCharShortMapDecorator;
import gnu.trove.decorator.TDoubleByteMapDecorator;
import gnu.trove.decorator.TDoubleCharMapDecorator;
import gnu.trove.decorator.TDoubleDoubleMapDecorator;
import gnu.trove.decorator.TDoubleFloatMapDecorator;
import gnu.trove.decorator.TDoubleIntMapDecorator;
import gnu.trove.decorator.TDoubleListDecorator;
import gnu.trove.decorator.TDoubleLongMapDecorator;
import gnu.trove.decorator.TDoubleObjectMapDecorator;
import gnu.trove.decorator.TDoubleSetDecorator;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.decorator.TFloatByteMapDecorator;
import gnu.trove.decorator.TFloatCharMapDecorator;
import gnu.trove.decorator.TFloatDoubleMapDecorator;
import gnu.trove.decorator.TFloatFloatMapDecorator;
import gnu.trove.decorator.TFloatIntMapDecorator;
import gnu.trove.decorator.TFloatListDecorator;
import gnu.trove.decorator.TFloatLongMapDecorator;
import gnu.trove.decorator.TFloatObjectMapDecorator;
import gnu.trove.decorator.TFloatSetDecorator;
import gnu.trove.decorator.TFloatShortMapDecorator;
import gnu.trove.decorator.TIntByteMapDecorator;
import gnu.trove.decorator.TIntCharMapDecorator;
import gnu.trove.decorator.TIntDoubleMapDecorator;
import gnu.trove.decorator.TIntFloatMapDecorator;
import gnu.trove.decorator.TIntIntMapDecorator;
import gnu.trove.decorator.TIntListDecorator;
import gnu.trove.decorator.TIntLongMapDecorator;
import gnu.trove.decorator.TIntObjectMapDecorator;
import gnu.trove.decorator.TIntSetDecorator;
import gnu.trove.decorator.TIntShortMapDecorator;
import gnu.trove.decorator.TLongByteMapDecorator;
import gnu.trove.decorator.TLongCharMapDecorator;
import gnu.trove.decorator.TLongDoubleMapDecorator;
import gnu.trove.decorator.TLongFloatMapDecorator;
import gnu.trove.decorator.TLongIntMapDecorator;
import gnu.trove.decorator.TLongListDecorator;
import gnu.trove.decorator.TLongLongMapDecorator;
import gnu.trove.decorator.TLongObjectMapDecorator;
import gnu.trove.decorator.TLongSetDecorator;
import gnu.trove.decorator.TLongShortMapDecorator;
import gnu.trove.decorator.TObjectByteMapDecorator;
import gnu.trove.decorator.TObjectCharMapDecorator;
import gnu.trove.decorator.TObjectDoubleMapDecorator;
import gnu.trove.decorator.TObjectFloatMapDecorator;
import gnu.trove.decorator.TObjectIntMapDecorator;
import gnu.trove.decorator.TObjectLongMapDecorator;
import gnu.trove.decorator.TObjectShortMapDecorator;
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.decorator.TShortCharMapDecorator;
import gnu.trove.decorator.TShortDoubleMapDecorator;
import gnu.trove.decorator.TShortFloatMapDecorator;
import gnu.trove.decorator.TShortIntMapDecorator;
import gnu.trove.decorator.TShortListDecorator;
import gnu.trove.decorator.TShortLongMapDecorator;
import gnu.trove.decorator.TShortObjectMapDecorator;
import gnu.trove.decorator.TShortSetDecorator;
import gnu.trove.decorator.TShortShortMapDecorator;
import gnu.trove.list.TByteList;
import gnu.trove.list.TCharList;
import gnu.trove.list.TDoubleList;
import gnu.trove.list.TFloatList;
import gnu.trove.list.TIntList;
import gnu.trove.list.TLongList;
import gnu.trove.list.TShortList;
import gnu.trove.map.TByteByteMap;
import gnu.trove.map.TByteCharMap;
import gnu.trove.map.TByteDoubleMap;
import gnu.trove.map.TByteFloatMap;
import gnu.trove.map.TByteIntMap;
import gnu.trove.map.TByteLongMap;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.TByteShortMap;
import gnu.trove.map.TCharByteMap;
import gnu.trove.map.TCharCharMap;
import gnu.trove.map.TCharDoubleMap;
import gnu.trove.map.TCharFloatMap;
import gnu.trove.map.TCharIntMap;
import gnu.trove.map.TCharLongMap;
import gnu.trove.map.TCharObjectMap;
import gnu.trove.map.TCharShortMap;
import gnu.trove.map.TDoubleByteMap;
import gnu.trove.map.TDoubleCharMap;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.map.TDoubleFloatMap;
import gnu.trove.map.TDoubleIntMap;
import gnu.trove.map.TDoubleLongMap;
import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.TFloatByteMap;
import gnu.trove.map.TFloatCharMap;
import gnu.trove.map.TFloatDoubleMap;
import gnu.trove.map.TFloatFloatMap;
import gnu.trove.map.TFloatIntMap;
import gnu.trove.map.TFloatLongMap;
import gnu.trove.map.TFloatObjectMap;
import gnu.trove.map.TFloatShortMap;
import gnu.trove.map.TIntByteMap;
import gnu.trove.map.TIntCharMap;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.TIntFloatMap;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntLongMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TIntShortMap;
import gnu.trove.map.TLongByteMap;
import gnu.trove.map.TLongCharMap;
import gnu.trove.map.TLongDoubleMap;
import gnu.trove.map.TLongFloatMap;
import gnu.trove.map.TLongIntMap;
import gnu.trove.map.TLongLongMap;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.TLongShortMap;
import gnu.trove.map.TObjectByteMap;
import gnu.trove.map.TObjectCharMap;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortCharMap;
import gnu.trove.map.TShortDoubleMap;
import gnu.trove.map.TShortFloatMap;
import gnu.trove.map.TShortIntMap;
import gnu.trove.map.TShortLongMap;
import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.TShortShortMap;
import gnu.trove.set.TByteSet;
import gnu.trove.set.TCharSet;
import gnu.trove.set.TDoubleSet;
import gnu.trove.set.TFloatSet;
import gnu.trove.set.TIntSet;
import gnu.trove.set.TLongSet;
import gnu.trove.set.TShortSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TDecorators {
   private TDecorators() {
   }

   public static Map wrap(TDoubleDoubleMap var0) {
      return new TDoubleDoubleMapDecorator(var0);
   }

   public static Map wrap(TDoubleFloatMap var0) {
      return new TDoubleFloatMapDecorator(var0);
   }

   public static Map wrap(TDoubleIntMap var0) {
      return new TDoubleIntMapDecorator(var0);
   }

   public static Map wrap(TDoubleLongMap var0) {
      return new TDoubleLongMapDecorator(var0);
   }

   public static Map wrap(TDoubleByteMap var0) {
      return new TDoubleByteMapDecorator(var0);
   }

   public static Map wrap(TDoubleShortMap var0) {
      return new TDoubleShortMapDecorator(var0);
   }

   public static Map wrap(TDoubleCharMap var0) {
      return new TDoubleCharMapDecorator(var0);
   }

   public static Map wrap(TFloatDoubleMap var0) {
      return new TFloatDoubleMapDecorator(var0);
   }

   public static Map wrap(TFloatFloatMap var0) {
      return new TFloatFloatMapDecorator(var0);
   }

   public static Map wrap(TFloatIntMap var0) {
      return new TFloatIntMapDecorator(var0);
   }

   public static Map wrap(TFloatLongMap var0) {
      return new TFloatLongMapDecorator(var0);
   }

   public static Map wrap(TFloatByteMap var0) {
      return new TFloatByteMapDecorator(var0);
   }

   public static Map wrap(TFloatShortMap var0) {
      return new TFloatShortMapDecorator(var0);
   }

   public static Map wrap(TFloatCharMap var0) {
      return new TFloatCharMapDecorator(var0);
   }

   public static Map wrap(TIntDoubleMap var0) {
      return new TIntDoubleMapDecorator(var0);
   }

   public static Map wrap(TIntFloatMap var0) {
      return new TIntFloatMapDecorator(var0);
   }

   public static Map wrap(TIntIntMap var0) {
      return new TIntIntMapDecorator(var0);
   }

   public static Map wrap(TIntLongMap var0) {
      return new TIntLongMapDecorator(var0);
   }

   public static Map wrap(TIntByteMap var0) {
      return new TIntByteMapDecorator(var0);
   }

   public static Map wrap(TIntShortMap var0) {
      return new TIntShortMapDecorator(var0);
   }

   public static Map wrap(TIntCharMap var0) {
      return new TIntCharMapDecorator(var0);
   }

   public static Map wrap(TLongDoubleMap var0) {
      return new TLongDoubleMapDecorator(var0);
   }

   public static Map wrap(TLongFloatMap var0) {
      return new TLongFloatMapDecorator(var0);
   }

   public static Map wrap(TLongIntMap var0) {
      return new TLongIntMapDecorator(var0);
   }

   public static Map wrap(TLongLongMap var0) {
      return new TLongLongMapDecorator(var0);
   }

   public static Map wrap(TLongByteMap var0) {
      return new TLongByteMapDecorator(var0);
   }

   public static Map wrap(TLongShortMap var0) {
      return new TLongShortMapDecorator(var0);
   }

   public static Map wrap(TLongCharMap var0) {
      return new TLongCharMapDecorator(var0);
   }

   public static Map wrap(TByteDoubleMap var0) {
      return new TByteDoubleMapDecorator(var0);
   }

   public static Map wrap(TByteFloatMap var0) {
      return new TByteFloatMapDecorator(var0);
   }

   public static Map wrap(TByteIntMap var0) {
      return new TByteIntMapDecorator(var0);
   }

   public static Map wrap(TByteLongMap var0) {
      return new TByteLongMapDecorator(var0);
   }

   public static Map wrap(TByteByteMap var0) {
      return new TByteByteMapDecorator(var0);
   }

   public static Map wrap(TByteShortMap var0) {
      return new TByteShortMapDecorator(var0);
   }

   public static Map wrap(TByteCharMap var0) {
      return new TByteCharMapDecorator(var0);
   }

   public static Map wrap(TShortDoubleMap var0) {
      return new TShortDoubleMapDecorator(var0);
   }

   public static Map wrap(TShortFloatMap var0) {
      return new TShortFloatMapDecorator(var0);
   }

   public static Map wrap(TShortIntMap var0) {
      return new TShortIntMapDecorator(var0);
   }

   public static Map wrap(TShortLongMap var0) {
      return new TShortLongMapDecorator(var0);
   }

   public static Map wrap(TShortByteMap var0) {
      return new TShortByteMapDecorator(var0);
   }

   public static Map wrap(TShortShortMap var0) {
      return new TShortShortMapDecorator(var0);
   }

   public static Map wrap(TShortCharMap var0) {
      return new TShortCharMapDecorator(var0);
   }

   public static Map wrap(TCharDoubleMap var0) {
      return new TCharDoubleMapDecorator(var0);
   }

   public static Map wrap(TCharFloatMap var0) {
      return new TCharFloatMapDecorator(var0);
   }

   public static Map wrap(TCharIntMap var0) {
      return new TCharIntMapDecorator(var0);
   }

   public static Map wrap(TCharLongMap var0) {
      return new TCharLongMapDecorator(var0);
   }

   public static Map wrap(TCharByteMap var0) {
      return new TCharByteMapDecorator(var0);
   }

   public static Map wrap(TCharShortMap var0) {
      return new TCharShortMapDecorator(var0);
   }

   public static Map wrap(TCharCharMap var0) {
      return new TCharCharMapDecorator(var0);
   }

   public static Map wrap(TObjectDoubleMap var0) {
      return new TObjectDoubleMapDecorator(var0);
   }

   public static Map wrap(TObjectFloatMap var0) {
      return new TObjectFloatMapDecorator(var0);
   }

   public static Map wrap(TObjectIntMap var0) {
      return new TObjectIntMapDecorator(var0);
   }

   public static Map wrap(TObjectLongMap var0) {
      return new TObjectLongMapDecorator(var0);
   }

   public static Map wrap(TObjectByteMap var0) {
      return new TObjectByteMapDecorator(var0);
   }

   public static Map wrap(TObjectShortMap var0) {
      return new TObjectShortMapDecorator(var0);
   }

   public static Map wrap(TObjectCharMap var0) {
      return new TObjectCharMapDecorator(var0);
   }

   public static Map wrap(TDoubleObjectMap var0) {
      return new TDoubleObjectMapDecorator(var0);
   }

   public static Map wrap(TFloatObjectMap var0) {
      return new TFloatObjectMapDecorator(var0);
   }

   public static Map wrap(TIntObjectMap var0) {
      return new TIntObjectMapDecorator(var0);
   }

   public static Map wrap(TLongObjectMap var0) {
      return new TLongObjectMapDecorator(var0);
   }

   public static Map wrap(TByteObjectMap var0) {
      return new TByteObjectMapDecorator(var0);
   }

   public static Map wrap(TShortObjectMap var0) {
      return new TShortObjectMapDecorator(var0);
   }

   public static Map wrap(TCharObjectMap var0) {
      return new TCharObjectMapDecorator(var0);
   }

   public static Set wrap(TDoubleSet var0) {
      return new TDoubleSetDecorator(var0);
   }

   public static Set wrap(TFloatSet var0) {
      return new TFloatSetDecorator(var0);
   }

   public static Set wrap(TIntSet var0) {
      return new TIntSetDecorator(var0);
   }

   public static Set wrap(TLongSet var0) {
      return new TLongSetDecorator(var0);
   }

   public static Set wrap(TByteSet var0) {
      return new TByteSetDecorator(var0);
   }

   public static Set wrap(TShortSet var0) {
      return new TShortSetDecorator(var0);
   }

   public static Set wrap(TCharSet var0) {
      return new TCharSetDecorator(var0);
   }

   public static List wrap(TDoubleList var0) {
      return new TDoubleListDecorator(var0);
   }

   public static List wrap(TFloatList var0) {
      return new TFloatListDecorator(var0);
   }

   public static List wrap(TIntList var0) {
      return new TIntListDecorator(var0);
   }

   public static List wrap(TLongList var0) {
      return new TLongListDecorator(var0);
   }

   public static List wrap(TByteList var0) {
      return new TByteListDecorator(var0);
   }

   public static List wrap(TShortList var0) {
      return new TShortListDecorator(var0);
   }

   public static List wrap(TCharList var0) {
      return new TCharListDecorator(var0);
   }
}
