package com.codeforge.engine.structure.dependency;

/**
 * Represents a project dependency with its coordinates and configuration.
 */
public class Dependency {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String scope;
    private final boolean optional;

    private Dependency(Builder builder) {
        this.groupId = builder.groupId;
        this.artifactId = builder.artifactId;
        this.version = builder.version;
        this.scope = builder.scope;
        this.optional = builder.optional;
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

    public String getScope() {
        return scope;
    }

    public boolean isOptional() {
        return optional;
    }

    /**
     * Formats the dependency as a Maven XML element.
     */
    public String toMavenXml() {
        StringBuilder xml = new StringBuilder();
        xml.append("        <dependency>\n");
        xml.append("            <groupId>").append(groupId).append("</groupId>\n");
        xml.append("            <artifactId>").append(artifactId).append("</artifactId>\n");
        if (version != null) {
            xml.append("            <version>").append(version).append("</version>\n");
        }
        if (scope != null && !scope.equals("compile")) {
            xml.append("            <scope>").append(scope).append("</scope>\n");
        }
        if (optional) {
            xml.append("            <optional>true</optional>\n");
        }
        xml.append("        </dependency>\n");
        return xml.toString();
    }

    /**
     * Formats the dependency as a Gradle dependency string.
     */
    public String toGradleString() {
        StringBuilder gradle = new StringBuilder();
        String config = scope != null ? scopeToGradleConfiguration(scope) : "implementation";
        gradle.append("    ").append(config).append("(\"")
              .append(groupId).append(":").append(artifactId);
        if (version != null) {
            gradle.append(":").append(version);
        }
        gradle.append("\")");
        return gradle.toString();
    }

    private String scopeToGradleConfiguration(String scope) {
        return switch (scope) {
            case "test" -> "testImplementation";
            case "provided" -> "compileOnly";
            case "runtime" -> "runtimeOnly";
            default -> "implementation";
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String groupId;
        private String artifactId;
        private String version;
        private String scope;
        private boolean optional;

        public Builder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder artifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder scope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder optional(boolean optional) {
            this.optional = optional;
            return this;
        }

        public Dependency build() {
            if (groupId == null || artifactId == null) {
                throw new IllegalStateException("groupId and artifactId are required");
            }
            return new Dependency(this);
        }
    }
}
