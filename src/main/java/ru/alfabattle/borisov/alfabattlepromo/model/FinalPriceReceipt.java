package ru.alfabattle.borisov.alfabattlepromo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinalPriceReceipt {

  private BigDecimal discount;
  private List<FinalPricePosition> positions = null;
  private BigDecimal total;

}

