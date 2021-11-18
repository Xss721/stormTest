package com.geekbang.coupon.calculation.processor;

import com.geekbang.coupon.calculation.api.beans.ShoppingCart;
import com.geekbang.coupon.calculation.processor.impl.DiscountProcessor;
import com.geekbang.coupon.calculation.processor.impl.DummyProcessor;
import com.geekbang.coupon.calculation.processor.impl.MoneyOffProcessor;
import com.geekbang.coupon.template.api.beans.CouponTemplateInfo;
import com.geekbang.coupon.template.api.enums.CouponType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@Slf4j
public class CouponProcessorFactory {

    @Autowired
    private MoneyOffProcessor moneyOffProcessor;

    @Autowired
    private DiscountProcessor discountProcessor;

    @Autowired
    private DummyProcessor dummyProcessor;

    public RuleProcessor getRuleProcessor(ShoppingCart order) {
        // 不使用优惠券
        if (CollectionUtils.isEmpty(order.getCouponInfos())) {
            return dummyProcessor;
        }

        // 暂时不支持多张优惠券
        if (order.getCouponInfos().size() > 1) {
            log.error("不能使用多张优惠券");
            throw new IllegalArgumentException("Cannot apply multiple coupons to one order");
        }

        // 获取优惠券的类别
        CouponTemplateInfo template = order.getCouponInfos().get(0).getTemplate();
        CouponType category = CouponType.convert(template.getType());

        switch (category) {
            case MONEY_OFF:
                return moneyOffProcessor;
            case DISCOUNT:
                return discountProcessor;
            // 未知类型的券模板
            default:
                return dummyProcessor;
        }
    }

}
