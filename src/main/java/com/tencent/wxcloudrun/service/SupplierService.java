package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.wxcloudrun.entity.Supplier;
import com.tencent.wxcloudrun.mapper.SupplierMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;

@Service
public class SupplierService extends ServiceImpl<SupplierMapper, Supplier> {

    public IPage<Supplier> findByKeyword(String keyword, Page<Supplier> page) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Supplier::getName, keyword)
                  .or()
                  .like(Supplier::getPhone, keyword)
                  .or()
                  .like(Supplier::getContact, keyword);
        }
        wrapper.orderByDesc(Supplier::getCreatedAt);
        return this.page(page, wrapper);
    }

    @Transactional
    public boolean updateById(Supplier supplier) {
        supplier.setUpdatedAt(LocalDateTime.now());
        return super.updateById(supplier);
    }

    @Transactional
    public boolean save(Supplier supplier) {
        LocalDateTime now = LocalDateTime.now();
        supplier.setCreatedAt(now);
        supplier.setUpdatedAt(now);
        return super.save(supplier);
    }
}
