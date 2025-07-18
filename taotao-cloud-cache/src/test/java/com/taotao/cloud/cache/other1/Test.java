package com.taotao.cloud.cache.other1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class Test {

	public static void main(String[] args) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		module.setDeserializerModifier(new CustomBeanDeserializerModifier());
		mapper.registerModule(module);

		 String json = "{" +
			"\"name\":\"John Doe\"," +
			"\"abc\":\"John Doe\"," +
			"\"email\":\"john.doe@example.com\"," +
			"\"address\":{" +
			"  \"street\":\"123 Main St\"," +
			"  \"city\":\"new york\"," +
			"  \"zipCode\":\"10001\"" +
			"}" +
			"}";;
		User user = mapper.readValue(json, User.class);
		System.out.println(user.getEmail());
	}
}
