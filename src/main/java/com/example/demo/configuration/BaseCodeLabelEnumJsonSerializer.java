package com.example.demo.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.mvc.domain.BaseCodeLabelEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * JSON 변환시 BaseCodeLabelEnum 클래스에 대한 변환을 동일하게 처리.
 * @author arsur
 *
 */
public class BaseCodeLabelEnumJsonSerializer extends JsonSerializer<BaseCodeLabelEnum> {

	@Override
	public void serialize(BaseCodeLabelEnum value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", value.code());
		map.put("label", value.label());
		gen.writeObject(map);
	}
}
