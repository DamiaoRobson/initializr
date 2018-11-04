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

package io.spring.initializr.actuate.metric;

import io.micrometer.core.instrument.MeterRegistry;
import io.spring.initializr.generator.ProjectFailedEvent;
import io.spring.initializr.generator.ProjectGeneratedEvent;
import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.generator.ProjectRequestSuperClass;

import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;

/**
 * A {@link ProjectGeneratedEvent} listener that uses a {@link MeterRegistry} to update
 * various project related metrics.
 *
 * @author Stephane Nicoll
 */
public class ProjectGenerationMetricsListener extends ProjectGenerationMetriListSuperClass {

	public ProjectGenerationMetricsListener(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

	@EventListener
	public void onGeneratedProject(ProjectGeneratedEvent event) {
		handleProjectRequest(event.getProjectRequest());
	}

	@EventListener
	public void onFailedProject(ProjectFailedEvent event) {
		handleProjectRequest(event.getProjectRequest());
		increment(key("failures"));
	}

	protected void handleLanguage(ProjectRequestSuperClass request) {
		if (StringUtils.hasText(request.getLanguage())) {
			String language = sanitize(request.getLanguage());
			increment(key("language." + language));
		}
	}

	protected void handleBootVersion(ProjectRequestSuperClass request) {
		if (StringUtils.hasText(request.getBootVersion())) {
			String bootVersion = sanitize(request.getBootVersion());
			increment(key("boot_version." + bootVersion));
		}
	}
	
	protected void handleProjectRequest(ProjectRequest request) {
		increment(key("requests")); // Total number of requests
		handleDependencies(request);
		handleType(request);
		handleJavaVersion(request);
		handlePackaging(request);
		handleLanguage(request);
		handleBootVersion(request);
		handleUserAgent(request);
	}
}
