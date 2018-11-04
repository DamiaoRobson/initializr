package io.spring.initializr.web.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import io.spring.initializr.generator.CommandLineHelpGenerator;
import io.spring.initializr.generator.ProjectGenerator;
import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.generator.ProjectRequestSuperClass;
import io.spring.initializr.metadata.DependencyMetadata;
import io.spring.initializr.metadata.DependencyMetadataProvider;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.util.Version;
import io.spring.initializr.web.mapper.DependencyMetadataV21JsonMapper;
import io.spring.initializr.web.mapper.InitializrMetadataJsonMapper;
import io.spring.initializr.web.mapper.InitializrMetadataV21JsonMapper;
import io.spring.initializr.web.mapper.InitializrMetadataV2JsonMapper;
import io.spring.initializr.web.mapper.InitializrMetadataVersion;

public abstract class MainControllerSuperClass2 extends AbstractInitializrController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	/**
	 * HAL JSON content type.
	 */
	public static final MediaType HAL_JSON_CONTENT_TYPE = MediaType
					.parseMediaType("application/hal+json");
	protected ProjectGenerator projectGenerator;
	protected DependencyMetadataProvider dependencyMetadataProvider;
	protected CommandLineHelpGenerator commandLineHelpGenerator;

	private static InitializrMetadataJsonMapper getJsonMapper(InitializrMetadataVersion version) {
		switch (version) {
		case V2:
			return new InitializrMetadataV2JsonMapper();
		default:
			return new InitializrMetadataV21JsonMapper();
		}
	}

	protected static String generateFileName(ProjectRequestSuperClass request, String extension) {
		String tmp = request.getArtifactId().replaceAll(" ", "_");
		try {
			return URLEncoder.encode(tmp, "UTF-8") + "." + extension;
		}
		catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException("Cannot encode URL", ex);
		}
	}

	protected ResponseEntity<String> serviceCapabilitiesFor(InitializrMetadataVersion version) {
		return serviceCapabilitiesFor(version, version.getMediaType());
	}

	protected ResponseEntity<String> serviceCapabilitiesFor(InitializrMetadataVersion version, MediaType contentType) {
		String appUrl = generateAppUrl();
		String content = getJsonMapper(version).write(this.metadataProvider.get(),
				appUrl);
		return ResponseEntity.ok().contentType(contentType).eTag(createUniqueId(content))
				.cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)).body(content);
	}

	protected static String getWrapperScript(ProjectRequest request) {
		String script = ("gradle".equals(request.getBuild()) ? "gradlew" : "mvnw");
		return (request.getBaseDir() != null) ? request.getBaseDir() + "/" + script
				: script;
	}

	protected ResponseEntity<String> dependenciesFor(InitializrMetadataVersion version, String bootVersion) {
		InitializrMetadata metadata = this.metadataProvider.get();
		Version v = (bootVersion != null) ? Version.parse(bootVersion)
				: Version.parse(metadata.getBootVersions().getDefault().getId());
		DependencyMetadata dependencyMetadata = this.dependencyMetadataProvider
				.get(metadata, v);
		String content = new DependencyMetadataV21JsonMapper().write(dependencyMetadata);
		return ResponseEntity.ok().contentType(version.getMediaType())
				.eTag(createUniqueId(content))
				.cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)).body(content);
	}

	public MainControllerSuperClass2(InitializrMetadataProvider metadataProvider,
			ResourceUrlProvider resourceUrlProvider) {
		super(metadataProvider, resourceUrlProvider);
	}

	protected ResponseEntity<byte[]> upload(File download, File dir, String fileName, String contentType) throws IOException {
		byte[] bytes = StreamUtils.copyToByteArray(new FileInputStream(download));
		log.info("Uploading: {} ({} bytes)", download, bytes.length);
		ResponseEntity<byte[]> result = createResponseEntity(bytes, contentType,
				fileName);
		this.projectGenerator.cleanTempFiles(dir);
		return result;
	}

	protected ResponseEntity<byte[]> createResponseEntity(byte[] content, String contentType, String fileName) {
		String contentDispositionValue = "attachment; filename=\"" + fileName + "\"";
		return ResponseEntity.ok().header("Content-Type", contentType)
				.header("Content-Disposition", contentDispositionValue).body(content);
	}

	protected String createUniqueId(String content) {
		StringBuilder builder = new StringBuilder();
		DigestUtils.appendMd5DigestAsHex(content.getBytes(StandardCharsets.UTF_8),
				builder);
		return builder.toString();
	}

}