package io.spring.initializr.actuate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.spring.initializr.actuate.stat.MainControllerStatsIntegrationTests;
import io.spring.initializr.actuate.stat.ProjectGenerationStatPublisherTests;
import io.spring.initializr.actuate.stat.ProjectRequestDocumentFactoryTests;
import io.spring.initializr.actuate.stat.StatsPropertiesTests;

@RunWith(Suite.class)
@SuiteClasses({ MainControllerStatsIntegrationTests.class, ProjectGenerationStatPublisherTests.class,
		ProjectRequestDocumentFactoryTests.class, StatsPropertiesTests.class })
public class AllTests4 {

}
