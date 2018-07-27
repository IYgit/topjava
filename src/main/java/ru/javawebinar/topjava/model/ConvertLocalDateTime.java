package ru.javawebinar.topjava.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Convert
public class ConvertLocalDateTime implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        return Timestamp.valueOf(attribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        return dbData.toLocalDateTime();
    }
}
