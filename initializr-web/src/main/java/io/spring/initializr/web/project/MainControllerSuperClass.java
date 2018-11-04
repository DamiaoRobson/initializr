package io.spring.initializr.web.project;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Tar;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.TarFileSet;
import org.apache.tools.ant.types.ZipFileSet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.samskivert.mustache.Mustache;

import io.spring.initializr.generator.BasicProjectRequest;
import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.mapper.InitializrMetadataVersion;

public abstract class MainControllerSuperClass extends MainControllerSuperClass2 {

	@RequestMapping("/starter.zip")
	@ResponseBody
	public ResponseEntity<byte[]> springZip(BasicProjectRequest basicRequest) throws IOException {
		ProjectRequest request = (ProjectRequest) basicRequest;
		File dir = this.projectGenerator.generateProjectStructure(request);
	
		File download = this.projectGenerator.createDistributionFile(dir, ".zip");
	
		String wrapperScript = getWrapperScript(request);
		new File(dir, wrapperScript).setExecutable(true);
		Zip zip = new Zip();
		zip.setProject(new Project());
		zip.setDefaultexcludes(false);
		ZipFileSet set = new ZipFileSet();
		set.setDir(dir);
		set.setFileMode("755");
		set.setIncludes(wrapperScript);
		set.setDefaultexcludes(false);
		zip.addFileset(set);
		set = new ZipFileSet();
		set.setDir(dir);
		set.setIncludes("**,");
		set.setExcludes(wrapperScript);
		set.setDefaultexcludes(false);
		zip.addFileset(set);
		zip.setDestFile(download.getCanonicalFile());
		zip.execute();
		return upload(download, dir, generateFileName(request, "zip"), "application/zip");
	}

	public MainControllerSuperClass(InitializrMetadataProvider metadataProvider,
			ResourceUrlProvider resourceUrlProvider) {
		super(metadataProvider, resourceUrlProvider);
	}

	@RequestMapping(path = { "/spring.tar.gz", "spring.tgz" })
	public String springTgz() {
		String url = this.metadataProvider.get().createCliDistributionURl("tar.gz");
		return "redirect:" + url;
	}

	@RequestMapping(path = { "/pom", "/pom.xml" })
	@ResponseBody
	public ResponseEntity<byte[]> pom(BasicProjectRequest request) {
		request.setType("maven-build");
		byte[] mavenPom = this.projectGenerator
				.generateMavenPom((ProjectRequest) request);
		return createResponseEntity(mavenPom, "application/octet-stream", "pom.xml");
	}

	@RequestMapping(path = { "/build", "/build.gradle" })
	@ResponseBody
	public ResponseEntity<byte[]> gradle(BasicProjectRequest request) {
		request.setType("gradle-build");
		byte[] gradleBuild = this.projectGenerator
				.generateGradleBuild((ProjectRequest) request);
		return createResponseEntity(gradleBuild, "application/octet-stream",
				"build.gradle");
	}

	@RequestMapping(path = "/starter.tgz", produces = "application/x-compress")
	@ResponseBody
	public ResponseEntity<byte[]> springTgz(BasicProjectRequest basicRequest)
			throws IOException {
		ProjectRequest request = (ProjectRequest) basicRequest;
		File dir = this.projectGenerator.generateProjectStructure(request);

		File download = this.projectGenerator.createDistributionFile(dir, ".tar.gz");

		String wrapperScript = getWrapperScript(request);
		new File(dir, wrapperScript).setExecutable(true);
		Tar zip = new Tar();
		zip.setProject(new Project());
		zip.setDefaultexcludes(false);
		TarFileSet set = zip.createTarFileSet();
		set.setDir(dir);
		set.setFileMode("755");
		set.setIncludes(wrapperScript);
		set.setDefaultexcludes(false);
		set = zip.createTarFileSet();
		set.setDir(dir);
		set.setIncludes("**,");
		set.setExcludes(wrapperScript);
		set.setDefaultexcludes(false);
		zip.setDestFile(download.getCanonicalFile());
		Tar.TarCompressionMethod method = new Tar.TarCompressionMethod();
		method.setValue("gzip");
		zip.setCompression(method);
		zip.execute();
		return upload(download, dir, generateFileName(request, "tar.gz"),
				"application/x-compress");
	}
	
	@RequestMapping(path = "/dependencies", produces = {
			"application/vnd.initializr.v2.1+json", "application/json" })
	public ResponseEntity<String> dependenciesV21(
			@RequestParam(required = false) String bootVersion) {
		return dependenciesFor(InitializrMetadataVersion.V2_1, bootVersion);
	}

	@ModelAttribute("linkTo")
	public Mustache.Lambda linkTo() {
		return (frag, out) -> out.write(this.getLinkTo().apply(frag.execute()));
	}

	@RequestMapping(path = "/", produces = "text/html")
	public String home(HttpServletRequest request, Map<String, Object> model) {
		if (isForceSsl() && !request.isSecure()) {
			String securedUrl = ServletUriComponentsBuilder.fromCurrentRequest()
					.scheme("https").build().toUriString();
			return "redirect:" + securedUrl;
		}
		renderHome(model);
		return "home";
	}

	@RequestMapping(path = { "/spring", "/spring.zip" })
	public String spring() {
		String url = this.metadataProvider.get().createCliDistributionURl("zip");
		return "redirect:" + url;
	}

}