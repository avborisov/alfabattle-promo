package ru.alfabattle.borisov.alfabattlepromo.model;

import lombok.Data;

import java.util.List;

@Data
public class ShoppingCart {

  private Boolean loyaltyCard;
  private List<ItemPosition> positions = null;
  private Integer shopId;

}

