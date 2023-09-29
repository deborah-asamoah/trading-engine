package io.turntabl.project.persistence.attributeconverters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.net.URI;

@Converter
public class URIAttributeConverter implements AttributeConverter<URI, String> {
    @Override
    public String convertToDatabaseColumn(URI attribute) {
        if (attribute == null)
            return null;
        return attribute.toString();
    }

    @Override
    public URI convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        return URI.create(dbData);
    }
}