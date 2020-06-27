package ru.alfabattle.borisov.alfabattlepromo.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import ru.alfabattle.borisov.alfabattlepromo.model.PromoMatrix;

@Service
@SessionScope
public class PromoMatrixActuator {

    @Getter
    @Setter
    private PromoMatrix promoMatrix;

}
