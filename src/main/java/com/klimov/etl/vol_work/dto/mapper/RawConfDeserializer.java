package com.klimov.etl.vol_work.dto.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class RawConfDeserializer extends StdDeserializer<String> {


    public RawConfDeserializer() {
        this(null);
    }

    public RawConfDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        JsonNode node = jp.readValueAsTree();
        return node.toString();
    }

}
