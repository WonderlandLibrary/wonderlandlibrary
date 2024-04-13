import os

out = "# Wonderland Library\n"

# Basic info
out += "Wonderland library is a world wide library for minecraft clients and mods.\n"
out += "\n"

# Contributors
out += "## Contributors\n";

contributorsInput = "parkopes;allah.himself;.idk1.;.stormingmoon;coccocoahelper;kellohyllyy;TrilliumHQ;HCU (Former);Xoraii"
contributors = contributorsInput.split(";")
for contributor in contributors:
    out += "- " + contributor + "\n";

out += "\n"

# Sources
out += "## [Sources](/sources)\n"

for sourceName in os.listdir("sources"):
    out += "- " + sourceName + "\n";

out += "\n"

# JARs
out += "## [Jars](/jars)\n"

for jarName in os.listdir("jars"):
    out += "- " + jarName + "\n";
    
# Leaks
out += "## [Leaks](/leaks)\n"

for jarName in os.listdir("leaks"):
    out += "- " + jarName + "\n";


# Writing
with open("README.md", "w") as f:
    f.write(out)
