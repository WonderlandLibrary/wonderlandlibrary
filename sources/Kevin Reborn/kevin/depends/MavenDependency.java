package kevin.depends;

import java.net.MalformedURLException;
import java.net.URL;

public class MavenDependency {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private String repoUrl;

    public MavenDependency(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version, "https://repo1.maven.org/maven2");
    }
    public MavenDependency(String groupId, String artifactId, String version, String repoUrl) {

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repoUrl = repoUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }
    public URL getUrl() throws MalformedURLException {
        String repo = this.repoUrl;
        if (!repo.endsWith("/")) {
            repo += "/";
        }
        repo += "%s/%s/%s/%s-%s.jar";

        String url = String.format(repo, this.groupId.replace(".", "/"), this.artifactId, this.version, this.artifactId, this.version);
        return new URL(url);
    }
}
