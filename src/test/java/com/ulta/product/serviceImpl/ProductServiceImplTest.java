/*
 * Copyright (C) 2019 ULTA
 * http://www.ulta.com
 * BrijendraK@ulta.com
 * All rights reserved
 */
package com.ulta.product.serviceImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.ulta.product.exception.ProductException;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryByKeyGet;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductByKeyGet;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;

@SpringBootTest
public class ProductServiceImplTest {
	ProductServiceImpl productServiceImpl = new ProductServiceImpl();

	@Mock
	SphereClient client;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		productServiceImpl.setClient(client);
	}

	@Test(expected = NullPointerException.class)
	public void testGetProductByKey() throws ProductException, InterruptedException, ExecutionException {
		String key = "Liquid";
		ProductByKeyGet request = ProductByKeyGet.of(key);
		CompletionStage<Product> value = (CompletionStage<Product>) Mockito.mock(CompletionStage.class);
		when(client.execute(request)).thenReturn(value);
		Product returnProduct = productServiceImpl.getProductByKey(key);
		assertEquals(null, returnProduct);
	}

	@Test(expected = ProductException.class)
	public void testGetProductByKeyForExceptionWhenProductIsNull()
			throws ProductException, InterruptedException, ExecutionException {
		String key = "facewash";
		ProductByKeyGet request = ProductByKeyGet.of(key);
		when(client.execute(request)).thenReturn(null);
		productServiceImpl.getProductByKey(key);
	}

	@Test(expected=NullPointerException.class)
	public void testGetProductsSucessCase() throws ProductException, InterruptedException, ExecutionException {
		final ProductProjectionQuery pro = ProductProjectionQuery.ofCurrent();
		CompletionStage<PagedQueryResult<ProductProjection>> value = (CompletionStage<PagedQueryResult<ProductProjection>>) Mockito
				.mock(CompletionStage.class);
		when(client.execute(pro)).thenReturn(value);
		PagedQueryResult<ProductProjection> result = productServiceImpl.getProducts();
		assertEquals(null, result);
	}

	@Test(expected = ProductException.class)
	public void testGetProductsWhenProductDataIsNull() throws ProductException, InterruptedException, ExecutionException {
		final ProductProjectionQuery pro = ProductProjectionQuery.ofCurrent();
		CompletionStage<PagedQueryResult<ProductProjection>> value = null;
		when(client.execute(pro)).thenReturn(value);
		productServiceImpl.getProducts();
	}

	@Test(expected=NullPointerException.class)
	public void testFindProductsWithCategory() throws InterruptedException, ExecutionException {
		CompletionStage<Object> value = (CompletionStage<Object>) Mockito.mock(CompletionStage.class);
		@SuppressWarnings("unchecked")
		CompletionStage<Category> category = (CompletionStage<Category>) Mockito.mock(CompletionStage.class);
		String categorykey = "Makeup";
		when(client.execute(CategoryByKeyGet.of(categorykey))).thenReturn(category);
		ProductProjectionQuery exists = Mockito.mock(ProductProjectionQuery.class);
		when(client.execute(null)).thenReturn(value);
		productServiceImpl.findProductsWithCategory(categorykey);
	}

	@Test(expected = NullPointerException.class)
	public void testFindProductsWithCategoryExceptionCase() throws InterruptedException, ExecutionException {
		CompletionStage<PagedQueryResult<ProductProjection>> value = (CompletionStage<PagedQueryResult<ProductProjection>>) Mockito
				.mock(CompletionStage.class);
		@SuppressWarnings("unchecked")
		CompletionStage<Category> category = (CompletionStage<Category>) Mockito.mock(CompletionStage.class);
		String categorykey = "Makeup";
		when(client.execute(CategoryByKeyGet.of(categorykey))).thenReturn(category);
		ProductProjectionQuery exists = Mockito.mock(ProductProjectionQuery.class);
		when(client.execute(exists)).thenReturn(value);
		productServiceImpl.findProductsWithCategory(categorykey);
	}

	@Test
	public void testgetCategories() throws InterruptedException, ExecutionException {
		CompletionStage<PagedQueryResult<Category>> category = (CompletionStage<PagedQueryResult<Category>>) Mockito
				.mock(CompletionStage.class);
		CategoryQuery catQuery = CategoryQuery.of();
		when(client.execute(catQuery)).thenReturn(category);
		CompletableFuture<PagedQueryResult<Category>> result = productServiceImpl.getCategories();
		assertEquals(null, result);
	}

	@Test(expected = ProductException.class)
	public void testgetCategoriesForException() throws InterruptedException, ExecutionException {
		CompletionStage<PagedQueryResult<Category>> category = null;
		CategoryQuery catQuery = CategoryQuery.of();
		when(client.execute(catQuery)).thenReturn(category);
		productServiceImpl.getCategories();
	}
}
