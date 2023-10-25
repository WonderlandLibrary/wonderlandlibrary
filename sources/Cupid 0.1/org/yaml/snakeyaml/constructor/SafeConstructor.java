package org.yaml.snakeyaml.constructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

public class SafeConstructor extends BaseConstructor {
  public static final ConstructUndefined undefinedConstructor = new ConstructUndefined();
  
  public SafeConstructor() {
    this(new LoaderOptions());
  }
  
  public SafeConstructor(LoaderOptions loadingConfig) {
    super(loadingConfig);
    this.yamlConstructors.put(Tag.NULL, new ConstructYamlNull());
    this.yamlConstructors.put(Tag.BOOL, new ConstructYamlBool());
    this.yamlConstructors.put(Tag.INT, new ConstructYamlInt());
    this.yamlConstructors.put(Tag.FLOAT, new ConstructYamlFloat());
    this.yamlConstructors.put(Tag.BINARY, new ConstructYamlBinary());
    this.yamlConstructors.put(Tag.TIMESTAMP, new ConstructYamlTimestamp());
    this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
    this.yamlConstructors.put(Tag.PAIRS, new ConstructYamlPairs());
    this.yamlConstructors.put(Tag.SET, new ConstructYamlSet());
    this.yamlConstructors.put(Tag.STR, new ConstructYamlStr());
    this.yamlConstructors.put(Tag.SEQ, new ConstructYamlSeq());
    this.yamlConstructors.put(Tag.MAP, new ConstructYamlMap());
    this.yamlConstructors.put(null, undefinedConstructor);
    this.yamlClassConstructors.put(NodeId.scalar, undefinedConstructor);
    this.yamlClassConstructors.put(NodeId.sequence, undefinedConstructor);
    this.yamlClassConstructors.put(NodeId.mapping, undefinedConstructor);
  }
  
  protected void flattenMapping(MappingNode node) {
    processDuplicateKeys(node);
    if (node.isMerged())
      node.setValue(mergeNode(node, true, new HashMap<>(), new ArrayList<>())); 
  }
  
  protected void processDuplicateKeys(MappingNode node) {
    List<NodeTuple> nodeValue = node.getValue();
    Map<Object, Integer> keys = new HashMap<>(nodeValue.size());
    TreeSet<Integer> toRemove = new TreeSet<>();
    int i = 0;
    for (NodeTuple tuple : nodeValue) {
      Node keyNode = tuple.getKeyNode();
      if (!keyNode.getTag().equals(Tag.MERGE)) {
        Object key = constructObject(keyNode);
        if (key != null)
          try {
            key.hashCode();
          } catch (Exception e) {
            throw new ConstructorException("while constructing a mapping", node.getStartMark(), "found unacceptable key " + key, tuple.getKeyNode().getStartMark(), e);
          }  
        Integer prevIndex = keys.put(key, Integer.valueOf(i));
        if (prevIndex != null) {
          if (!isAllowDuplicateKeys())
            throw new DuplicateKeyException(node.getStartMark(), key, tuple.getKeyNode().getStartMark()); 
          toRemove.add(prevIndex);
        } 
      } 
      i++;
    } 
    Iterator<Integer> indices2remove = toRemove.descendingIterator();
    while (indices2remove.hasNext())
      nodeValue.remove(((Integer)indices2remove.next()).intValue()); 
  }
  
  private List<NodeTuple> mergeNode(MappingNode node, boolean isPreffered, Map<Object, Integer> key2index, List<NodeTuple> values) {
    Iterator<NodeTuple> iter = node.getValue().iterator();
    while (iter.hasNext()) {
      NodeTuple nodeTuple = iter.next();
      Node keyNode = nodeTuple.getKeyNode();
      Node valueNode = nodeTuple.getValueNode();
      if (keyNode.getTag().equals(Tag.MERGE)) {
        MappingNode mn;
        SequenceNode sn;
        List<Node> vals;
        iter.remove();
        switch (valueNode.getNodeId()) {
          case mapping:
            mn = (MappingNode)valueNode;
            mergeNode(mn, false, key2index, values);
            continue;
          case sequence:
            sn = (SequenceNode)valueNode;
            vals = sn.getValue();
            for (Node subnode : vals) {
              if (!(subnode instanceof MappingNode))
                throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping for merging, but found " + subnode.getNodeId(), subnode.getStartMark()); 
              MappingNode mnode = (MappingNode)subnode;
              mergeNode(mnode, false, key2index, values);
            } 
            continue;
        } 
        throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping or list of mappings for merging, but found " + valueNode.getNodeId(), valueNode.getStartMark());
      } 
      Object key = constructObject(keyNode);
      if (!key2index.containsKey(key)) {
        values.add(nodeTuple);
        key2index.put(key, Integer.valueOf(values.size() - 1));
        continue;
      } 
      if (isPreffered)
        values.set(((Integer)key2index.get(key)).intValue(), nodeTuple); 
    } 
    return values;
  }
  
  protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
    flattenMapping(node);
    super.constructMapping2ndStep(node, mapping);
  }
  
  protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
    flattenMapping(node);
    super.constructSet2ndStep(node, set);
  }
  
  public class ConstructYamlNull extends AbstractConstruct {
    public Object construct(Node node) {
      if (node != null)
        SafeConstructor.this.constructScalar((ScalarNode)node); 
      return null;
    }
  }
  
  private static final Map<String, Boolean> BOOL_VALUES = new HashMap<>();
  
  static {
    BOOL_VALUES.put("yes", Boolean.TRUE);
    BOOL_VALUES.put("no", Boolean.FALSE);
    BOOL_VALUES.put("true", Boolean.TRUE);
    BOOL_VALUES.put("false", Boolean.FALSE);
    BOOL_VALUES.put("on", Boolean.TRUE);
    BOOL_VALUES.put("off", Boolean.FALSE);
  }
  
  public class ConstructYamlBool extends AbstractConstruct {
    public Object construct(Node node) {
      String val = SafeConstructor.this.constructScalar((ScalarNode)node);
      return SafeConstructor.BOOL_VALUES.get(val.toLowerCase());
    }
  }
  
  public class ConstructYamlInt extends AbstractConstruct {
    public Object construct(Node node) {
      String value = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
      int sign = 1;
      char first = value.charAt(0);
      if (first == '-') {
        sign = -1;
        value = value.substring(1);
      } else if (first == '+') {
        value = value.substring(1);
      } 
      int base = 10;
      if ("0".equals(value))
        return Integer.valueOf(0); 
      if (value.startsWith("0b")) {
        value = value.substring(2);
        base = 2;
      } else if (value.startsWith("0x")) {
        value = value.substring(2);
        base = 16;
      } else if (value.startsWith("0")) {
        value = value.substring(1);
        base = 8;
      } else {
        if (value.indexOf(':') != -1) {
          String[] digits = value.split(":");
          int bes = 1;
          int val = 0;
          for (int i = 0, j = digits.length; i < j; i++) {
            val = (int)(val + Long.parseLong(digits[j - i - 1]) * bes);
            bes *= 60;
          } 
          return SafeConstructor.this.createNumber(sign, String.valueOf(val), 10);
        } 
        return SafeConstructor.this.createNumber(sign, value, 10);
      } 
      return SafeConstructor.this.createNumber(sign, value, base);
    }
  }
  
  private static final int[][] RADIX_MAX = new int[17][2];
  
  static {
    int[] radixList = { 2, 8, 10, 16 };
    for (int radix : radixList) {
      (new int[2])[0] = maxLen(2147483647, radix);
      (new int[2])[1] = maxLen(Long.MAX_VALUE, radix);
      RADIX_MAX[radix] = new int[2];
    } 
  }
  
  private static int maxLen(int max, int radix) {
    return Integer.toString(max, radix).length();
  }
  
  private static int maxLen(long max, int radix) {
    return Long.toString(max, radix).length();
  }
  
  private Number createNumber(int sign, String number, int radix) {
    Number number1;
    int len = (number != null) ? number.length() : 0;
    if (sign < 0)
      number = "-" + number; 
    int[] maxArr = (radix < RADIX_MAX.length) ? RADIX_MAX[radix] : null;
    if (maxArr != null) {
      boolean gtInt = (len > maxArr[0]);
      if (gtInt) {
        if (len > maxArr[1])
          return new BigInteger(number, radix); 
        return createLongOrBigInteger(number, radix);
      } 
    } 
    try {
      number1 = Integer.valueOf(number, radix);
    } catch (NumberFormatException e) {
      number1 = createLongOrBigInteger(number, radix);
    } 
    return number1;
  }
  
  protected static Number createLongOrBigInteger(String number, int radix) {
    try {
      return Long.valueOf(number, radix);
    } catch (NumberFormatException e1) {
      return new BigInteger(number, radix);
    } 
  }
  
  public class ConstructYamlFloat extends AbstractConstruct {
    public Object construct(Node node) {
      String value = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
      int sign = 1;
      char first = value.charAt(0);
      if (first == '-') {
        sign = -1;
        value = value.substring(1);
      } else if (first == '+') {
        value = value.substring(1);
      } 
      String valLower = value.toLowerCase();
      if (".inf".equals(valLower))
        return Double.valueOf((sign == -1) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY); 
      if (".nan".equals(valLower))
        return Double.valueOf(Double.NaN); 
      if (value.indexOf(':') != -1) {
        String[] digits = value.split(":");
        int bes = 1;
        double val = 0.0D;
        for (int i = 0, j = digits.length; i < j; i++) {
          val += Double.parseDouble(digits[j - i - 1]) * bes;
          bes *= 60;
        } 
        return Double.valueOf(sign * val);
      } 
      Double d = Double.valueOf(value);
      return Double.valueOf(d.doubleValue() * sign);
    }
  }
  
  public class ConstructYamlBinary extends AbstractConstruct {
    public Object construct(Node node) {
      String noWhiteSpaces = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("\\s", "");
      byte[] decoded = Base64Coder.decode(noWhiteSpaces.toCharArray());
      return decoded;
    }
  }
  
  private static final Pattern TIMESTAMP_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
  
  private static final Pattern YMD_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");
  
  public static class ConstructYamlTimestamp extends AbstractConstruct {
    private Calendar calendar;
    
    public Calendar getCalendar() {
      return this.calendar;
    }
    
    public Object construct(Node node) {
      TimeZone timeZone;
      ScalarNode scalar = (ScalarNode)node;
      String nodeValue = scalar.getValue();
      Matcher match = SafeConstructor.YMD_REGEXP.matcher(nodeValue);
      if (match.matches()) {
        String str1 = match.group(1);
        String str2 = match.group(2);
        String str3 = match.group(3);
        this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.calendar.clear();
        this.calendar.set(1, Integer.parseInt(str1));
        this.calendar.set(2, Integer.parseInt(str2) - 1);
        this.calendar.set(5, Integer.parseInt(str3));
        return this.calendar.getTime();
      } 
      match = SafeConstructor.TIMESTAMP_REGEXP.matcher(nodeValue);
      if (!match.matches())
        throw new YAMLException("Unexpected timestamp: " + nodeValue); 
      String year_s = match.group(1);
      String month_s = match.group(2);
      String day_s = match.group(3);
      String hour_s = match.group(4);
      String min_s = match.group(5);
      String seconds = match.group(6);
      String millis = match.group(7);
      if (millis != null)
        seconds = seconds + "." + millis; 
      double fractions = Double.parseDouble(seconds);
      int sec_s = (int)Math.round(Math.floor(fractions));
      int usec = (int)Math.round((fractions - sec_s) * 1000.0D);
      String timezoneh_s = match.group(8);
      String timezonem_s = match.group(9);
      if (timezoneh_s != null) {
        String time = (timezonem_s != null) ? (":" + timezonem_s) : "00";
        timeZone = TimeZone.getTimeZone("GMT" + timezoneh_s + time);
      } else {
        timeZone = TimeZone.getTimeZone("UTC");
      } 
      this.calendar = Calendar.getInstance(timeZone);
      this.calendar.set(1, Integer.parseInt(year_s));
      this.calendar.set(2, Integer.parseInt(month_s) - 1);
      this.calendar.set(5, Integer.parseInt(day_s));
      this.calendar.set(11, Integer.parseInt(hour_s));
      this.calendar.set(12, Integer.parseInt(min_s));
      this.calendar.set(13, sec_s);
      this.calendar.set(14, usec);
      return this.calendar.getTime();
    }
  }
  
  public class ConstructYamlOmap extends AbstractConstruct {
    public Object construct(Node node) {
      Map<Object, Object> omap = new LinkedHashMap<>();
      if (!(node instanceof SequenceNode))
        throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a sequence, but found " + node.getNodeId(), node.getStartMark()); 
      SequenceNode snode = (SequenceNode)node;
      for (Node subnode : snode.getValue()) {
        if (!(subnode instanceof MappingNode))
          throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a mapping of length 1, but found " + subnode.getNodeId(), subnode.getStartMark()); 
        MappingNode mnode = (MappingNode)subnode;
        if (mnode.getValue().size() != 1)
          throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a single mapping item, but found " + mnode.getValue().size() + " items", mnode.getStartMark()); 
        Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
        Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
        Object key = SafeConstructor.this.constructObject(keyNode);
        Object value = SafeConstructor.this.constructObject(valueNode);
        omap.put(key, value);
      } 
      return omap;
    }
  }
  
  public class ConstructYamlPairs extends AbstractConstruct {
    public Object construct(Node node) {
      if (!(node instanceof SequenceNode))
        throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a sequence, but found " + node.getNodeId(), node.getStartMark()); 
      SequenceNode snode = (SequenceNode)node;
      List<Object[]> pairs = new ArrayList(snode.getValue().size());
      for (Node subnode : snode.getValue()) {
        if (!(subnode instanceof MappingNode))
          throw new ConstructorException("while constructingpairs", node.getStartMark(), "expected a mapping of length 1, but found " + subnode.getNodeId(), subnode.getStartMark()); 
        MappingNode mnode = (MappingNode)subnode;
        if (mnode.getValue().size() != 1)
          throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a single mapping item, but found " + mnode.getValue().size() + " items", mnode.getStartMark()); 
        Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
        Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
        Object key = SafeConstructor.this.constructObject(keyNode);
        Object value = SafeConstructor.this.constructObject(valueNode);
        pairs.add(new Object[] { key, value });
      } 
      return pairs;
    }
  }
  
  public class ConstructYamlSet implements Construct {
    public Object construct(Node node) {
      if (node.isTwoStepsConstruction())
        return SafeConstructor.this.constructedObjects.containsKey(node) ? SafeConstructor.this.constructedObjects.get(node) : SafeConstructor.this.createDefaultSet(((MappingNode)node).getValue().size()); 
      return SafeConstructor.this.constructSet((MappingNode)node);
    }
    
    public void construct2ndStep(Node node, Object object) {
      if (node.isTwoStepsConstruction()) {
        SafeConstructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
      } else {
        throw new YAMLException("Unexpected recursive set structure. Node: " + node);
      } 
    }
  }
  
  public class ConstructYamlStr extends AbstractConstruct {
    public Object construct(Node node) {
      return SafeConstructor.this.constructScalar((ScalarNode)node);
    }
  }
  
  public class ConstructYamlSeq implements Construct {
    public Object construct(Node node) {
      SequenceNode seqNode = (SequenceNode)node;
      if (node.isTwoStepsConstruction())
        return SafeConstructor.this.newList(seqNode); 
      return SafeConstructor.this.constructSequence(seqNode);
    }
    
    public void construct2ndStep(Node node, Object data) {
      if (node.isTwoStepsConstruction()) {
        SafeConstructor.this.constructSequenceStep2((SequenceNode)node, (List)data);
      } else {
        throw new YAMLException("Unexpected recursive sequence structure. Node: " + node);
      } 
    }
  }
  
  public class ConstructYamlMap implements Construct {
    public Object construct(Node node) {
      MappingNode mnode = (MappingNode)node;
      if (node.isTwoStepsConstruction())
        return SafeConstructor.this.createDefaultMap(mnode.getValue().size()); 
      return SafeConstructor.this.constructMapping(mnode);
    }
    
    public void construct2ndStep(Node node, Object object) {
      if (node.isTwoStepsConstruction()) {
        SafeConstructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
      } else {
        throw new YAMLException("Unexpected recursive mapping structure. Node: " + node);
      } 
    }
  }
  
  public static final class ConstructUndefined extends AbstractConstruct {
    public Object construct(Node node) {
      throw new ConstructorException(null, null, "could not determine a constructor for the tag " + node.getTag(), node.getStartMark());
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\constructor\SafeConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */