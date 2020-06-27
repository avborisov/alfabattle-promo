package ru.alfabattle.borisov.alfabattlepromo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabattle.borisov.alfabattlepromo.model.FinalPricePosition;
import ru.alfabattle.borisov.alfabattlepromo.model.FinalPriceReceipt;
import ru.alfabattle.borisov.alfabattlepromo.model.Item;
import ru.alfabattle.borisov.alfabattlepromo.model.ShoppingCart;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartCalculator {

    private static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_EVEN);
    private final PersistentDataStorageService storage;

    @Autowired
    private PromoMatrixActuator promoMatrix;

    public ShoppingCartCalculator(@Autowired PersistentDataStorageService persistentDataStorageService) {
        this.storage = persistentDataStorageService;
    }

    public FinalPriceReceipt calculate(ShoppingCart cart) {
        FinalPriceReceipt receipt = new FinalPriceReceipt();
        receipt.setTotal(getTotalPrice(cart));
        receipt.setDiscount(new BigDecimal(0).round(MATH_CONTEXT));
        receipt.setPositions(getFinalPositions(cart));
        return receipt;
    }

    private BigDecimal getTotalPrice(ShoppingCart cart) {
        return cart.getPositions().stream()
                .map(itemPosition -> {
                    return storage.getItems().get(itemPosition.getItemId());
                })
                .map(Item::getPrice)
                .reduce(BigDecimal.valueOf(0), (left, right) -> left.add(right)).round(MATH_CONTEXT);
    }

    private List<FinalPricePosition> getFinalPositions(ShoppingCart cart) {
        return cart.getPositions().stream()
                .map(itemPosition -> {
                    FinalPricePosition position = new FinalPricePosition();
                    Item itemFromStorage = storage.getItems().get(itemPosition.getItemId());
                    position.setId(itemFromStorage.getId());
                    position.setName(itemFromStorage.getName());
                    position.setPrice(itemFromStorage.getPrice().round(MATH_CONTEXT));
                    position.setRegularPrice(itemFromStorage.getPrice().round(MATH_CONTEXT));
                    return position;
                })
                .collect(Collectors.toList());
    }

}
