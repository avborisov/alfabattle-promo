package ru.alfabattle.borisov.alfabattlepromo.model;

import lombok.Data;

@Data
public class ItemCountRule {

  private Integer bonusQuantity;
  private String itemId;
  private Integer shopId;
  private Integer triggerQuantity;

}

