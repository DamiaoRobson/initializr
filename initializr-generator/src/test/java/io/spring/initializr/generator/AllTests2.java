package io.spring.initializr.generator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.spring.initializr.metadata.BillOfMaterialsTests;
import io.spring.initializr.metadata.DependenciesCapabilityTests;
import io.spring.initializr.metadata.DependencyTests;
import io.spring.initializr.metadata.InitializrConfigurationTests;
import io.spring.initializr.metadata.InitializrMetadataBuilderTests;
import io.spring.initializr.metadata.InitializrMetadataTests;
import io.spring.initializr.metadata.LinkTests;
import io.spring.initializr.metadata.SingleSelectCapabilityTests;
import io.spring.initializr.metadata.TextCapabilityTests;
import io.spring.initializr.metadata.TypeCapabilityTests;
import io.spring.initializr.metadata.TypeTests;

@RunWith(Suite.class)
@SuiteClasses({ BillOfMaterialsTests.class, DependenciesCapabilityTests.class, DependencyTests.class,
		InitializrConfigurationTests.class, InitializrMetadataBuilderTests.class, InitializrMetadataTests.class,
		LinkTests.class, SingleSelectCapabilityTests.class, TextCapabilityTests.class, TypeCapabilityTests.class,
		TypeTests.class })
public class AllTests2 {

}
