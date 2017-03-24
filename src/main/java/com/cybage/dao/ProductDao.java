package com.cybage.dao;

import com.cybage.entity.Product;
import com.cybage.model.PaginationResult;
import com.cybage.model.ProductInfo;

public interface ProductDao 
{
public Product findProduct(String code);
public ProductInfo findProductInfo(String code);
public PaginationResult<ProductInfo> queryProducts(int page,int maxResult, int maxNavigationPage);
public PaginationResult<ProductInfo> queryProducts(int page , int maxResult, int maxNavigationPage, String likeName);
public void save(ProductInfo productinfo);

}
