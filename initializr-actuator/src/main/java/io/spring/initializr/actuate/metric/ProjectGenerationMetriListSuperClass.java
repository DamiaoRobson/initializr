package io.spring.initializr.actuate.metric;

import java.util.List;

import org.springframework.util.StringUtils;

import io.micrometer.core.instrument.MeterRegistry;
import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.generator.ProjectRequestSuperClass;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.util.Agent;

public class ProjectGenerationMetriListSuperClass {

	protected MeterRegistry meterRegistry;

	public ProjectGenerationMetriListSuperClass() {
		super();
	}

	

	protected void handleDependencies(ProjectRequest request) {
		List<Dependency> dependencies = request.getResolvedDependencies();
		if (dependencies != null) {
			dependencies.forEach((it) -> {
				if (!ProjectRequestSuperClass.DEFAULT_STARTER.equals(it.getId())) {
					String id = sanitize(it.getId());
					increment(key("dependency." + id));
				}
			});
		}
	}

	protected void handleType(ProjectRequestSuperClass request) {
		if (StringUtils.hasText(request.getType())) {
			String type = sanitize(request.getType());
			increment(key("type." + type));
		}
	}

	protected void handleJavaVersion(ProjectRequestSuperClass request) {
		if (StringUtils.hasText(request.getJavaVersion())) {
			String javaVersion = sanitize(request.getJavaVersion());
			increment(key("java_version." + javaVersion));
		}
	}

	protected void handlePackaging(ProjectRequestSuperClass request) {
		if (StringUtils.hasText(request.getPackaging())) {
			String packaging = sanitize(request.getPackaging());
			increment(key("packaging." + packaging));
		}
	}

	
	
	protected void handleUserAgent(ProjectRequest request) {
		String userAgent = (String) request.getParameters().get("user-agent");
		if (userAgent != null) {
			Agent agent = Agent.fromUserAgent(userAgent);
			if (agent != null) {
				increment(key("client_id." + agent.getId().getId()));
			}
		}
	}
	
	protected void increment(String key) {
		this.meterRegistry.counter(key).increment();
	}

	protected String key(String part) {
		return "initializr." + part;
	}

	protected String sanitize(String s) {
		return s.replace(".", "_");
	}

}