package ru.alfabattle.borisov.alfabattlepromo.model;

import lombok.Data;

import java.util.List;

@Data
public class PromoMatrix {

  private List<ItemCountRule> itemCountRules = null;
  private List<ItemGroupRule> itemGroupRules = null;
  private List<LoyaltyCardRule> loyaltyCardRules = null;

}

