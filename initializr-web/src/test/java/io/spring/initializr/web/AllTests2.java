package io.spring.initializr.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.spring.initializr.web.autoconfigure.CloudfoundryEnvironmentPostProcessorTests;
import io.spring.initializr.web.autoconfigure.InitializrAutoConfigurationTests;

@RunWith(Suite.class)
@SuiteClasses({ CloudfoundryEnvironmentPostProcessorTests.class, InitializrAutoConfigurationTests.class })
public class AllTests2 {

}
