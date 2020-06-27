package ru.alfabattle.borisov.alfabattlepromo.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Item implements StringIdEntity {

    private String id; // O'rly???
    private String name;
    private String groupId;
    private BigDecimal price;

}
