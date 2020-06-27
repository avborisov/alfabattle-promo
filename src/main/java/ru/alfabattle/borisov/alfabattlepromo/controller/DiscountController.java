package ru.alfabattle.borisov.alfabattlepromo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabattle.borisov.alfabattlepromo.model.FinalPriceReceipt;
import ru.alfabattle.borisov.alfabattlepromo.model.PromoMatrix;
import ru.alfabattle.borisov.alfabattlepromo.model.ShoppingCart;
import ru.alfabattle.borisov.alfabattlepromo.service.PromoMatrixActuator;
import ru.alfabattle.borisov.alfabattlepromo.service.ShoppingCartCalculator;

@RestController
@Slf4j
public class DiscountController {

    @Autowired
    private PromoMatrixActuator promoMatrix;

    @Autowired
    private ShoppingCartCalculator calculator;

    @PostMapping(path = "/promo", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> promo(@RequestBody PromoMatrix body) {
        try {
            promoMatrix.setPromoMatrix(body);
            return new ResponseEntity<>("Правила успешно загружены", HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Something wrong with request to endpoint", ex);
        }
        return new ResponseEntity<>("Некорректный запрос", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/receipt", consumes = "application/json", produces = "application/json")
    public ResponseEntity<FinalPriceReceipt> receipt(@RequestBody ShoppingCart cart) {
        return new ResponseEntity<>(calculator.calculate(cart), HttpStatus.OK);
    }

}