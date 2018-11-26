package io.spring.initializr.actuate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.spring.initializr.actuate.autoconfigure.InitializrActuatorEndpointsAutoConfigurationTests;
import io.spring.initializr.actuate.autoconfigure.InitializrMetricsAutoConfigurationTests;
import io.spring.initializr.actuate.autoconfigure.InitializrStatsAutoConfigurationTests;

@RunWith(Suite.class)
@SuiteClasses({ InitializrActuatorEndpointsAutoConfigurationTests.class, InitializrMetricsAutoConfigurationTests.class,
		InitializrStatsAutoConfigurationTests.class })
public class AllTests {

}
