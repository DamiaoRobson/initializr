package io.spring.initializr.generator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.Repository;
import io.spring.initializr.util.Version;
import io.spring.initializr.util.VersionProperty;

public class ProjectRequestSuperClass extends BasicProjectRequest {

	/**
	 * The id of the starter to use if no dependency is defined.
	 */
	public static final String DEFAULT_STARTER = "root_starter";
	protected final Map<String, Object> parameters = new LinkedHashMap<>();
	protected List<Dependency> resolvedDependencies;
	protected final Map<String, BillOfMaterials> boms = new LinkedHashMap<>();
	protected final Map<String, Repository> repositories = new LinkedHashMap<>();
	protected final BuildProperties buildProperties = new BuildProperties();
	protected List<String> facets = new ArrayList<>();
	protected String build;

	public ProjectRequestSuperClass() {
		super();
	}

	/**
	 * Set the repositories that this instance should use based on the
	 * {@link InitializrMetadata} and the requested Spring Boot {@link Version}.
	 * @param metadata the initializr metadata
	 * @param requestedVersion the requested version
	 */
	protected void initializeRepositories(InitializrMetadata metadata, Version requestedVersion) {
		if (!"RELEASE".equals(requestedVersion.getQualifier().getQualifier())) {
			this.repositories.put("spring-snapshots", metadata.getConfiguration().getEnv()
					.getRepositories().get("spring-snapshots"));
			this.repositories.put("spring-milestones", metadata.getConfiguration()
					.getEnv().getRepositories().get("spring-milestones"));
		}
		this.boms.values().forEach((it) -> it.getRepositories().forEach((key) -> {
			this.repositories.computeIfAbsent(key,
					(s) -> metadata.getConfiguration().getEnv().getRepositories().get(s));
		}));
	}

	protected void initializeProperties(InitializrMetadata metadata, Version requestedVersion) {
		Supplier<String> kotlinVersion = () -> metadata.getConfiguration().getEnv()
				.getKotlin().resolveKotlinVersion(requestedVersion);
		if ("gradle".equals(this.build)) {
			this.buildProperties.getGradle().put("springBootVersion",
					this::getBootVersion);
			if ("kotlin".equals(getLanguage())) {
				this.buildProperties.getGradle().put("kotlinVersion", kotlinVersion);
			}
		}
		else {
			this.buildProperties.getMaven().put("project.build.sourceEncoding",
					() -> "UTF-8");
			this.buildProperties.getMaven().put("project.reporting.outputEncoding",
					() -> "UTF-8");
			this.buildProperties.getVersions().put(new VersionProperty("java.version"),
					this::getJavaVersion);
			if ("kotlin".equals(getLanguage())) {
				this.buildProperties.getVersions()
						.put(new VersionProperty("kotlin.version"), kotlinVersion);
			}
		}
	}

	protected void resolveBom(InitializrMetadata metadata, String bomId, Version requestedVersion) {
		if (!this.boms.containsKey(bomId)) {
			BillOfMaterials bom = metadata.getConfiguration().getEnv().getBoms()
					.get(bomId).resolve(requestedVersion);
			bom.getAdditionalBoms()
					.forEach((id) -> resolveBom(metadata, id, requestedVersion));
			this.boms.put(bomId, bom);
		}
	}

	/**
	 * Update this request once it has been resolved with the specified
	 * {@link InitializrMetadata}.
	 * @param metadata the initializr metadata
	 */
	protected void afterResolution(InitializrMetadata metadata) {
		if ("war".equals(getPackaging())) {
			if (!hasWebFacet()) {
				// Need to be able to bootstrap the web app
				this.resolvedDependencies.add(determineWebDependency(metadata));
				this.facets.add("web");
			}
			// Add the tomcat starter in provided scope
			Dependency tomcat = new Dependency().asSpringBootStarter("tomcat");
			tomcat.setScope(Dependency.SCOPE_PROVIDED);
			this.resolvedDependencies.add(tomcat);
		}
		if (this.resolvedDependencies.stream().noneMatch(Dependency::isStarter)) {
			// There"s no starter so we add the default one
			addDefaultDependency();
		}
	}

	private Dependency determineWebDependency(InitializrMetadata metadata) {
		Dependency web = metadata.getDependencies().get("web");
		if (web != null) {
			return web;
		}
		return Dependency.withId("web").asSpringBootStarter("web");
	}

	/**
	 * Add a default dependency if the project does not define any dependency.
	 */
	protected void addDefaultDependency() {
		Dependency root = new Dependency();
		root.setId(DEFAULT_STARTER);
		root.asSpringBootStarter("");
		this.resolvedDependencies.add(root);
	}
	
	/**
	 * Specify if this request has the specified facet enabled.
	 * @param facet the facet to check
	 * @return {@code true} if the project has the facet
	 */
	public boolean hasFacet(String facet) {
		return this.facets.contains(facet);
	}

	@Override
	public String toString() {
		return "ProjectRequest [" + "parameters=" + this.parameters + ", "
				+ ((this.resolvedDependencies != null)
						? "resolvedDependencies=" + this.resolvedDependencies + ", " : "")
				+ "boms=" + this.boms + ", " + "repositories=" + this.repositories + ", "
				+ "buildProperties=" + this.buildProperties + ", "
				+ ((this.facets != null) ? "facets=" + this.facets + ", " : "")
				+ ((this.build != null) ? "build=" + this.build : "") + "]";
	}
	
	/**
	 * Specify if this request has the web facet enabled.
	 * @return {@code true} if the project has the web facet
	 */
	public boolean hasWebFacet() {
		return hasFacet("web");
	}


}