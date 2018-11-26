package io.spring.initializr.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.spring.initializr.web.mapper.DependencyMetadataJsonMapperTests;
import io.spring.initializr.web.mapper.InitializrMetadataJsonMapperTests;
import io.spring.initializr.web.mapper.LinkMapperTests;

@RunWith(Suite.class)
@SuiteClasses({ DependencyMetadataJsonMapperTests.class, InitializrMetadataJsonMapperTests.class,
		LinkMapperTests.class })
public class AllTests3 {

}
