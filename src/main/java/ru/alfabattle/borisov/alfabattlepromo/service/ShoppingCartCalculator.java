package ru.alfabattle.borisov.alfabattlepromo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabattle.borisov.alfabattlepromo.model.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShoppingCartCalculator {

    private final PersistentDataStorageService storage;

    @Autowired
    private PromoMatrixActuator promoMatrixActuator;

    public ShoppingCartCalculator(@Autowired PersistentDataStorageService persistentDataStorageService) {
        this.storage = persistentDataStorageService;
    }

    public FinalPriceReceipt calculate(ShoppingCart cart) {
        log.info("calculator invoked, promoMatrix: {}", promoMatrixActuator.getPromoMatrix());
        FinalPriceReceipt receipt = new FinalPriceReceipt();
        receipt.setTotal(getTotalPrice(cart));
        receipt.setDiscount(new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN));
        receipt.setPositions(getFinalPositions(cart));
        return receipt;
    }

    private BigDecimal getTotalPrice(ShoppingCart cart) {
        BigDecimal totalPrice = cart.getPositions().stream()
                .map(itemPosition -> {
                    return storage.getItems().get(itemPosition.getItemId());
                })
                .map(Item::getPrice)
                .reduce(BigDecimal.valueOf(0), (left, right) -> left.add(right));

        if (cart.getLoyaltyCard() != null && cart.getLoyaltyCard().booleanValue()) {
            PromoMatrix promoMatrix = promoMatrixActuator.getPromoMatrix();
            LoyaltyCardRule cardRule = promoMatrix.getLoyaltyCardRules().stream()
                    .filter(rule -> rule.getShopId().equals(cart.getShopId()))
                    .findFirst().orElse(null);
            if (cardRule != null) {
                BigDecimal discount = totalPrice.multiply(BigDecimal.valueOf(cardRule.getDiscount()));
                totalPrice = totalPrice.subtract(discount);
            }
        }
        return totalPrice.setScale(2, RoundingMode.HALF_EVEN);
    }

    private List<FinalPricePosition> getFinalPositions(ShoppingCart cart) {
        return cart.getPositions().stream()
                .map(itemPosition -> {
                    FinalPricePosition position = new FinalPricePosition();
                    Item itemFromStorage = storage.getItems().get(itemPosition.getItemId());
                    position.setId(itemFromStorage.getId());
                    position.setName(itemFromStorage.getName());
                    position.setPrice(itemFromStorage.getPrice().setScale(2, RoundingMode.HALF_EVEN));
                    position.setRegularPrice(itemFromStorage.getPrice().setScale(2, RoundingMode.HALF_EVEN));
                    return position;
                })
                .collect(Collectors.toList());
    }

}
