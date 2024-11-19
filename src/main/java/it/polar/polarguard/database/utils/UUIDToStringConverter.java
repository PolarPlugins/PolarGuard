package it.polar.polarguard.database.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.UUID;

@WritingConverter
public class UUIDToStringConverter implements Converter<UUID, String> {
    @Override
    public String convert(UUID source) {
        return source.toString();
    }
}