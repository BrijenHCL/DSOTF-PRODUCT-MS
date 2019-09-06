/*
 * Copyright (C) 2019 ULTA
 * http://www.ulta.com
 * BrijendraK@ulta.com
 * All rights reserved
 */
package com.ulta.product.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ulta.product.exception.UltaException;
import com.ulta.product.response.CategoryResponse;
import com.ulta.product.response.ProductResponse;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedQueryResult;

public interface ProductService {

	public ProductResponse getProductByKey(String key) throws UltaException;

	public ProductResponse getProducts()
			throws UltaException;

	public ProductResponse findProductsWithCategory(String ctgId)
			throws UltaException;

	public CategoryResponse getCategories() throws UltaException;
}
