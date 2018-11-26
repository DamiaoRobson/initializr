package io.spring.initializr.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.spring.initializr.web.project.CommandLineExampleIntegrationTests;
import io.spring.initializr.web.project.LegacyStsControllerIntegrationTests;
import io.spring.initializr.web.project.MainControllerDefaultsIntegrationTests;
import io.spring.initializr.web.project.MainControllerDependenciesTests;
import io.spring.initializr.web.project.MainControllerEnvIntegrationTests;
import io.spring.initializr.web.project.MainControllerIntegrationTests;
import io.spring.initializr.web.project.MainControllerServiceMetadataIntegrationTests;
import io.spring.initializr.web.project.MainControllerSslIntegrationTests;
import io.spring.initializr.web.project.ProjectGenerationPostProcessorTests;
import io.spring.initializr.web.project.ProjectGenerationSmokeTests;

@RunWith(Suite.class)
@SuiteClasses({ CommandLineExampleIntegrationTests.class, LegacyStsControllerIntegrationTests.class,
		MainControllerDefaultsIntegrationTests.class, MainControllerDependenciesTests.class,
		MainControllerEnvIntegrationTests.class, MainControllerIntegrationTests.class,
		MainControllerServiceMetadataIntegrationTests.class, MainControllerSslIntegrationTests.class,
		ProjectGenerationPostProcessorTests.class, ProjectGenerationSmokeTests.class })
public class AllTests4 {

}
