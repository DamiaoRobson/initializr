package io.spring.initializr.web.mapper;

import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.spring.initializr.metadata.DefaultMetadataElement;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.DependencyGroup;
import io.spring.initializr.metadata.Describable;
import io.spring.initializr.metadata.MetadataElement;
import io.spring.initializr.metadata.SingleSelectCapability;
import io.spring.initializr.metadata.Type;

public class InitializrV2JsonRefectory {

	static final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
	
	public InitializrV2JsonRefectory() {
		super();
	}

	protected void singleSelect(ObjectNode parent, SingleSelectCapability capability) {
		ObjectNode single = nodeFactory.objectNode();
		single.put("type", capability.getType().getName());
		DefaultMetadataElement defaultType = capability.getDefault();
		if (defaultType != null) {
			single.put("default", defaultType.getId());
		}
		ArrayNode values = nodeFactory.arrayNode();
		values.addAll(capability.getContent().stream().map(this::mapValue)
				.collect(Collectors.toList()));
		single.set("values", values);
		parent.set(capability.getId(), single);
	}

	protected ObjectNode mapDependencyGroup(DependencyGroup group) {
		ObjectNode result = nodeFactory.objectNode();
		result.put("name", group.getName());
		if ((group instanceof Describable)
				&& ((Describable) group).getDescription() != null) {
			result.put("description", ((Describable) group).getDescription());
		}
		ArrayNode items = nodeFactory.arrayNode();
		group.getContent().forEach((it) -> {
			JsonNode dependency = mapDependency(it);
			if (dependency != null) {
				items.add(dependency);
			}
		});
		result.set("values", items);
		return result;
	}

	protected ObjectNode mapDependency(Dependency dependency) {
		if (dependency.getVersionRange() == null) {
			// only map the dependency if no versionRange is set
			return mapValue(dependency);
		}
		return null;
	}

	protected ObjectNode mapType(Type type) {
		ObjectNode result = mapValue(type);
		result.put("action", type.getAction());
		ObjectNode tags = nodeFactory.objectNode();
		type.getTags().forEach(tags::put);
		result.set("tags", tags);
		return result;
	}

	protected ObjectNode mapValue(MetadataElement value) {
		ObjectNode result = nodeFactory.objectNode();
		result.put("id", value.getId());
		result.put("name", value.getName());
		if ((value instanceof Describable)
				&& ((Describable) value).getDescription() != null) {
			result.put("description", ((Describable) value).getDescription());
		}
		return result;
	}

}