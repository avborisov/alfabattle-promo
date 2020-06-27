package ru.alfabattle.borisov.alfabattlepromo.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FinalPricePosition {

  private String id;
  private String name;
  private BigDecimal price;
  private BigDecimal regularPrice;

}

