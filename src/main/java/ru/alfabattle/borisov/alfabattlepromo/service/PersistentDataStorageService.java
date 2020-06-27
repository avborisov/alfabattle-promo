package ru.alfabattle.borisov.alfabattlepromo.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.alfabattle.borisov.alfabattlepromo.model.Group;
import ru.alfabattle.borisov.alfabattlepromo.model.Item;
import ru.alfabattle.borisov.alfabattlepromo.model.StringIdEntity;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersistentDataStorageService {

    @Getter
    private Map<String, Item> items;
    @Getter
    private Map<String, Group> groups;

    @PostConstruct
    public void init() {
        items = loadObjectList(Item.class, "items.csv");
        log.info("Loaded {} items", items.size());

        groups = loadObjectList(Group.class, "groups.csv");
        log.info("Loaded {} groups", groups.size());
    }

    private <T extends StringIdEntity> Map<String, T> loadObjectList(Class<T> type, String fileName) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            File file = new ClassPathResource(fileName).getFile();
            MappingIterator<T> readValues = mapper.readerFor(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll().stream().collect(Collectors.toMap(StringIdEntity::getId, Function.identity()));
        } catch (Exception e) {
            log.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyMap();
        }
    }

}
