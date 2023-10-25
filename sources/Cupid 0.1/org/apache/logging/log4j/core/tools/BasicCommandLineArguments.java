package org.apache.logging.log4j.core.tools;

import org.apache.logging.log4j.core.tools.picocli.CommandLine.Option;

public class BasicCommandLineArguments {
  @Option(names = {"--help", "-?", "-h"}, usageHelp = true, description = {"Prints this help and exits."})
  private boolean help;
  
  public boolean isHelp() {
    return this.help;
  }
  
  public void setHelp(boolean help) {
    this.help = help;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\tools\BasicCommandLineArguments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */