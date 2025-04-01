package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.entity.PaymentConfig;
import com.tencent.wxcloudrun.mapper.PaymentConfigMapper;
import com.tencent.wxcloudrun.service.PaymentConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentConfigServiceImpl implements PaymentConfigService {
    private final PaymentConfigMapper paymentConfigMapper;

    @Override
    public Map<String, String> getPaymentConfig(String configType) {
        LambdaQueryWrapper<PaymentConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentConfig::getConfigType, configType)
            .eq(PaymentConfig::getStatus, 0);
        
        List<PaymentConfig> configs = paymentConfigMapper.selectList(wrapper);
        if (configs.isEmpty()) {
            throw new BusinessException("支付配置不存在");
        }

        Map<String, String> configMap = new HashMap<>();
        for (PaymentConfig config : configs) {
            configMap.put(config.getConfigKey(), config.getConfigValue());
        }
        
        return configMap;
    }

    @Override
    public boolean updatePaymentConfig(PaymentConfig paymentConfig) {
        LambdaQueryWrapper<PaymentConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentConfig::getConfigType, paymentConfig.getConfigType())
            .eq(PaymentConfig::getConfigKey, paymentConfig.getConfigKey());
        
        PaymentConfig config = paymentConfigMapper.selectOne(wrapper);
        if (config == null) {
            throw new BusinessException("支付配置不存在");
        }

        config.setConfigValue(paymentConfig.getConfigValue());
        if(paymentConfigMapper.updateById(config)>0){
            return true;
        }
        return false;
    }
}
