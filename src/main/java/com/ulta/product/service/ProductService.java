/*
 * Copyright (C) 2019 ULTA
 * http://www.ulta.com
 * BrijendraK@ulta.com
 * All rights reserved
 */
package com.ulta.product.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import com.ulta.product.exception.ProductException;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedQueryResult;

public interface ProductService {

	public Product getProductByKey(String key) throws ProductException, InterruptedException, ExecutionException;

	public PagedQueryResult<ProductProjection> getProducts() throws ProductException,InterruptedException, ExecutionException;

	public PagedQueryResult<ProductProjection> findProductsWithCategory(String ctgId)
			throws InterruptedException, ExecutionException, ProductException;

	public CompletableFuture<PagedQueryResult<Category>> getCategories() throws ProductException;
}
