package org.yaml.snakeyaml.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;

public class Resolver {
  public static final Pattern BOOL = Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");
  
  public static final Pattern FLOAT = Pattern.compile("^([-+]?(\\.[0-9]+|[0-9_]+(\\.[0-9_]*)?)([eE][-+]?[0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
  
  public static final Pattern INT = Pattern.compile("^(?:[-+]?0b[0-1_]+|[-+]?0[0-7_]+|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x[0-9a-fA-F_]+|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
  
  public static final Pattern MERGE = Pattern.compile("^(?:<<)$");
  
  public static final Pattern NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
  
  public static final Pattern EMPTY = Pattern.compile("^$");
  
  public static final Pattern TIMESTAMP = Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
  
  public static final Pattern VALUE = Pattern.compile("^(?:=)$");
  
  public static final Pattern YAML = Pattern.compile("^(?:!|&|\\*)$");
  
  protected Map<Character, List<ResolverTuple>> yamlImplicitResolvers = new HashMap<>();
  
  protected void addImplicitResolvers() {
    addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
    addImplicitResolver(Tag.INT, INT, "-+0123456789");
    addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789.");
    addImplicitResolver(Tag.MERGE, MERGE, "<");
    addImplicitResolver(Tag.NULL, NULL, "~nN\000");
    addImplicitResolver(Tag.NULL, EMPTY, null);
    addImplicitResolver(Tag.TIMESTAMP, TIMESTAMP, "0123456789");
    addImplicitResolver(Tag.YAML, YAML, "!&*");
  }
  
  public Resolver() {
    addImplicitResolvers();
  }
  
  public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
    if (first == null) {
      List<ResolverTuple> curr = this.yamlImplicitResolvers.get(null);
      if (curr == null) {
        curr = new ArrayList<>();
        this.yamlImplicitResolvers.put(null, curr);
      } 
      curr.add(new ResolverTuple(tag, regexp));
    } else {
      char[] chrs = first.toCharArray();
      for (int i = 0, j = chrs.length; i < j; i++) {
        Character theC = Character.valueOf(chrs[i]);
        if (theC.charValue() == '\000')
          theC = null; 
        List<ResolverTuple> curr = this.yamlImplicitResolvers.get(theC);
        if (curr == null) {
          curr = new ArrayList<>();
          this.yamlImplicitResolvers.put(theC, curr);
        } 
        curr.add(new ResolverTuple(tag, regexp));
      } 
    } 
  }
  
  public Tag resolve(NodeId kind, String value, boolean implicit) {
    if (kind == NodeId.scalar && implicit) {
      List<ResolverTuple> resolvers;
      if (value.length() == 0) {
        resolvers = this.yamlImplicitResolvers.get(Character.valueOf(false));
      } else {
        resolvers = this.yamlImplicitResolvers.get(Character.valueOf(value.charAt(0)));
      } 
      if (resolvers != null)
        for (ResolverTuple v : resolvers) {
          Tag tag = v.getTag();
          Pattern regexp = v.getRegexp();
          if (regexp.matcher(value).matches())
            return tag; 
        }  
      if (this.yamlImplicitResolvers.containsKey(null))
        for (ResolverTuple v : this.yamlImplicitResolvers.get(null)) {
          Tag tag = v.getTag();
          Pattern regexp = v.getRegexp();
          if (regexp.matcher(value).matches())
            return tag; 
        }  
    } 
    switch (kind) {
      case scalar:
        return Tag.STR;
      case sequence:
        return Tag.SEQ;
    } 
    return Tag.MAP;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\resolver\Resolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */