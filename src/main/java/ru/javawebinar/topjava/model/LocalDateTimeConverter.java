package ru.javawebinar.topjava.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**без анотації методи конвертування слід викликати явно (вручну) в коді
* анотація @Convert з параметром disableConversion = false (за замовчанням. мжна не вказувати)
* забезпечує автоконвертування для всіх класів пакету*/
@Convert(disableConversion = false) // true - заборона автоконвертування для всіх класів пакету
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp>{
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        System.out.println("from Convert 1");
        return Timestamp.valueOf(attribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        System.out.println("from Convert 1");
        return dbData.toLocalDateTime();
    }
}
