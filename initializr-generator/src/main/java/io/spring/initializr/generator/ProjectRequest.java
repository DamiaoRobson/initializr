/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.generator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.DefaultMetadataElement;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.Repository;
import io.spring.initializr.metadata.Type;
import io.spring.initializr.util.Version;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

/**
 * A request to generate a project.
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 */
public class ProjectRequest extends ProjectRequestSuperClass {

	public List<Dependency> getResolvedDependencies() {
		return this.resolvedDependencies;
	}

	public void setResolvedDependencies(List<Dependency> resolvedDependencies) {
		this.resolvedDependencies = resolvedDependencies;
	}

	public List<String> getFacets() {
		return this.facets;
	}

	public void setFacets(List<String> facets) {
		this.facets = facets;
	}

	public String getBuild() {
		return this.build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	/**
	 * Return the additional parameters that can be used to further identify the request.
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	public Map<String, BillOfMaterials> getBoms() {
		return this.boms;
	}

	public Map<String, Repository> getRepositories() {
		return this.repositories;
	}

	/**
	 * Return the build properties.
	 * @return the build properties
	 */
	public BuildProperties getBuildProperties() {
		return this.buildProperties;
	}

	/**
	 * Initializes this instance with the defaults defined in the specified
	 * {@link InitializrMetadata}.
	 * @param metadata the initializr metadata
	 */
	public void initialize(InitializrMetadata metadata) {
		BeanWrapperImpl bean = new BeanWrapperImpl(this);
		metadata.defaults().forEach((key, value) -> {
			if (bean.isWritableProperty(key)) {
				// We want to be able to infer a package name if none has been
				// explicitly set
				if (!key.equals("packageName")) {
					bean.setPropertyValue(key, value);
				}
			}
		});
	}

	/**
	 * Resolve this instance against the specified {@link InitializrMetadata}.
	 * @param metadata the initializr metadata
	 */
	public void resolve(InitializrMetadata metadata) {
		List<String> depIds = (!getStyle().isEmpty() ? getStyle() : getDependencies());
		String actualBootVersion = (getBootVersion() != null) ? getBootVersion()
				: metadata.getBootVersions().getDefault().getId();
		Version requestedVersion = Version.parse(actualBootVersion);
		this.resolvedDependencies = depIds.stream().map((it) -> {
			Dependency dependency = metadata.getDependencies().get(it);
			if (dependency == null) {
				throw new InvalidProjectRequestException(
						"Unknown dependency '" + it + "' check project metadata");
			}
			return dependency.resolve(requestedVersion);
		}).collect(Collectors.toList());
		this.resolvedDependencies.forEach((it) -> {
			it.getFacets().forEach((facet) -> {
				if (!this.facets.contains(facet)) {
					this.facets.add(facet);
				}
			});
			if (!it.match(requestedVersion)) {
				throw new InvalidProjectRequestException(
						"Dependency '" + it.getId() + "' is not compatible "
								+ "with Spring Boot " + requestedVersion);
			}
			if (it.getBom() != null) {
				resolveBom(metadata, it.getBom(), requestedVersion);
			}
			if (it.getRepository() != null) {
				String repositoryId = it.getRepository();
				this.repositories.computeIfAbsent(repositoryId, (s) -> metadata
						.getConfiguration().getEnv().getRepositories().get(s));
			}
		});
		if (getType() != null) {
			Type type = metadata.getTypes().get(getType());
			if (type == null) {
				throw new InvalidProjectRequestException(
						"Unknown type '" + getType() + "' check project metadata");
			}
			String buildTag = type.getTags().get("build");
			if (buildTag != null) {
				this.build = buildTag;
			}
		}
		if (getPackaging() != null) {
			DefaultMetadataElement packaging = metadata.getPackagings()
					.get(getPackaging());
			if (packaging == null) {
				throw new InvalidProjectRequestException("Unknown packaging '"
						+ getPackaging() + "' check project metadata");
			}
		}
		if (getLanguage() != null) {
			DefaultMetadataElement language = metadata.getLanguages().get(getLanguage());
			if (language == null) {
				throw new InvalidProjectRequestException("Unknown language '"
						+ getLanguage() + "' check project metadata");
			}
		}

		if (!StringUtils.hasText(getApplicationName())) {
			setApplicationName(
					metadata.getConfiguration().generateApplicationName(getName()));
		}
		setPackageName(metadata.getConfiguration().cleanPackageName(getPackageName(),
				metadata.getPackageName().getContent()));

		initializeRepositories(metadata, requestedVersion);

		initializeProperties(metadata, requestedVersion);

		afterResolution(metadata);
	}

	
	

}
