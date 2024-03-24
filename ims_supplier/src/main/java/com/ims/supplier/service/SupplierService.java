package com.ims.supplier.service;

import com.ims.domain.supplier.Supplier;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SupplierService {

    public void save(Supplier supplier);

    public void update(Supplier supplier);

    public void deleteById(String id);

    public Supplier findById(String id);

    public List<Supplier> findAll(String companyId);

    public Page<Supplier> findAll(int page, int size, Map<String,Object> map);
}