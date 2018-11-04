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

package io.spring.initializr.web.project;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.samskivert.mustache.Mustache;
import io.spring.initializr.generator.BasicProjectRequest;
import io.spring.initializr.generator.CommandLineHelpGenerator;
import io.spring.initializr.generator.ProjectGenerator;
import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.metadata.DependencyMetadataProvider;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.util.Agent;
import io.spring.initializr.util.Agent.AgentId;
import io.spring.initializr.util.TemplateRenderer;
import io.spring.initializr.web.mapper.InitializrMetadataVersion;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Tar;
import org.apache.tools.ant.types.TarFileSet;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The main initializr controller provides access to the configured metadata and serves as
 * a central endpoint to generate projects or build files.
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 */
@Controller
public class MainController extends MainControllerSuperClass {

	public MainController(InitializrMetadataProvider metadataProvider,
			TemplateRenderer templateRenderer, ResourceUrlProvider resourceUrlProvider,
			ProjectGenerator projectGenerator,
			DependencyMetadataProvider dependencyMetadataProvider) {
		super(metadataProvider, resourceUrlProvider);
		this.projectGenerator = projectGenerator;
		this.dependencyMetadataProvider = dependencyMetadataProvider;
		super.commandLineHelpGenerator = new CommandLineHelpGenerator(templateRenderer);
	}

	@ModelAttribute
	public BasicProjectRequest projectRequest(
			@RequestHeader Map<String, String> headers) {
		ProjectRequest request = new ProjectRequest();
		request.getParameters().putAll(headers);
		request.initialize(this.metadataProvider.get());
		return request;
	}

	@RequestMapping(path = "/metadata/config", produces = "application/json")
	@ResponseBody
	public InitializrMetadata config() {
		return this.metadataProvider.get();
	}

	@RequestMapping("/metadata/client")
	public String client() {
		return "redirect:/";
	}

	@RequestMapping(path = "/", produces = "text/plain")
	public ResponseEntity<String> serviceCapabilitiesText(
			@RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {
		String appUrl = generateAppUrl();
		InitializrMetadata metadata = this.metadataProvider.get();

		BodyBuilder builder = ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN);
		if (userAgent != null) {
			Agent agent = Agent.fromUserAgent(userAgent);
			if (agent != null) {
				if (AgentId.CURL.equals(agent.getId())) {
					String content = this.commandLineHelpGenerator
							.generateCurlCapabilities(metadata, appUrl);
					return builder.eTag(createUniqueId(content)).body(content);
				}
				if (AgentId.HTTPIE.equals(agent.getId())) {
					String content = this.commandLineHelpGenerator
							.generateHttpieCapabilities(metadata, appUrl);
					return builder.eTag(createUniqueId(content)).body(content);
				}
				if (AgentId.SPRING_BOOT_CLI.equals(agent.getId())) {
					String content = this.commandLineHelpGenerator
							.generateSpringBootCliCapabilities(metadata, appUrl);
					return builder.eTag(createUniqueId(content)).body(content);
				}
			}
		}
		String content = this.commandLineHelpGenerator
				.generateGenericCapabilities(metadata, appUrl);
		return builder.eTag(createUniqueId(content)).body(content);
	}

	@RequestMapping(path = "/", produces = "application/hal+json")
	public ResponseEntity<String> serviceCapabilitiesHal() {
		return serviceCapabilitiesFor(InitializrMetadataVersion.V2_1,
				HAL_JSON_CONTENT_TYPE);
	}

	@RequestMapping(path = "/", produces = { "application/vnd.initializr.v2.1+json",
			"application/json" })
	public ResponseEntity<String> serviceCapabilitiesV21() {
		return serviceCapabilitiesFor(InitializrMetadataVersion.V2_1);
	}

	@RequestMapping(path = "/", produces = "application/vnd.initializr.v2+json")
	public ResponseEntity<String> serviceCapabilitiesV2() {
		return serviceCapabilitiesFor(InitializrMetadataVersion.V2);
	}





}
