package io.spring.initializr.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import io.spring.initializr.service.extension.JacksonKotlinRequestPostProcessorTests;
import io.spring.initializr.service.extension.JavaVersionRequestPostProcessorTests;
import io.spring.initializr.service.extension.ReactorTestRequestPostProcessorTests;
import io.spring.initializr.service.extension.SpringBatchTestRequestPostProcessorTests;
import io.spring.initializr.service.extension.SpringBoot2RequestPostProcessorTests;
import io.spring.initializr.service.extension.SpringCloudFunctionRequestPostProcessorTests;
import io.spring.initializr.service.extension.SpringCloudStreamRequestPostProcessorTests;
import io.spring.initializr.service.extension.SpringSecurityTestRequestPostProcessorTests;
import io.spring.initializr.service.extension.SpringSessionRequestPostProcessorTests;

@RunWith(Suite.class)
@SuiteClasses({ JacksonKotlinRequestPostProcessorTests.class, JavaVersionRequestPostProcessorTests.class,
		ReactorTestRequestPostProcessorTests.class, SpringBatchTestRequestPostProcessorTests.class,
		SpringBoot2RequestPostProcessorTests.class, SpringCloudFunctionRequestPostProcessorTests.class,
		SpringCloudStreamRequestPostProcessorTests.class, SpringSecurityTestRequestPostProcessorTests.class,
		SpringSessionRequestPostProcessorTests.class })
public class AllTests2 {

}
