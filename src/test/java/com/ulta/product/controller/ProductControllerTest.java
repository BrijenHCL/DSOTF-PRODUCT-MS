/*
 * Copyright (C) 2019 ULTA
 * http://www.ulta.com
 * BrijendraK@ulta.com
 * All rights reserved
 */
package com.ulta.product.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ulta.product.exception.ProductException;
import com.ulta.product.service.ProductService;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedQueryResult;

@SpringBootTest
public class ProductControllerTest {

	ProductController productController = new ProductController();

	@Mock
	SphereClient client;

	@Mock
	ProductService productService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		productController.setProductService(productService);
	}

	@Test(expected = ProductException.class)
	public void testgetProductBykeyWhenProductGetisNull()
			throws ProductException, InterruptedException, ExecutionException {

		// CompletableFuture<Product> products = new CompletableFuture<Product>();
		Product value = null;

		String key = "Liquid-exception";
		when(productService.getProductByKey(key)).thenReturn(value);
		productController.getProductByKey(key);
	}

	@Test()
	public void testgetProductBykey() throws ProductException, InterruptedException, ExecutionException {

		CompletableFuture<Product> products = new CompletableFuture<Product>();
		Product value = Mockito.mock(Product.class);
		String key = "Liquid";
		when(productService.getProductByKey(key)).thenReturn(value);
		ResponseEntity<Product> result = productController.getProductByKey(key);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@SuppressWarnings("unchecked")
	@Test(expected = ProductException.class)
	public void testgetProductBykeyForExceptionCase1()
			throws ProductException, InterruptedException, ExecutionException {

		String key = "Liquid";
		when(productService.getProductByKey(key)).thenThrow(new ProductException("Failure"));
		productController.getProductByKey(key);
	}

	@Test()
	public void testgetProducts() throws ProductException, InterruptedException, ExecutionException {

		CompletableFuture<PagedQueryResult<ProductProjection>> products = new CompletableFuture<PagedQueryResult<ProductProjection>>();
		ProductProjection productProjection = Mockito.mock(ProductProjection.class);
		@SuppressWarnings("unchecked")
		PagedQueryResult<ProductProjection> value = Mockito.mock(PagedQueryResult.class);
		value.getResults().add(productProjection);
		products.complete(value);
		when(productService.getProducts()).thenReturn(products.get());
		ResponseEntity<PagedQueryResult<ProductProjection>> result = productController.getProducts();
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test(expected = ProductException.class)
	public void testgetProductsExceptionCase() throws ProductException, InterruptedException, ExecutionException {

		CompletableFuture<PagedQueryResult<ProductProjection>> products = new CompletableFuture<PagedQueryResult<ProductProjection>>();
		products.complete(null);
		when(productService.getProducts()).thenReturn(products.get());
		productController.getProducts();
	}

	@Test()
	public void testgetProductByCategory() throws InterruptedException, ExecutionException {

		CompletableFuture<PagedQueryResult<ProductProjection>> products = new CompletableFuture<PagedQueryResult<ProductProjection>>();
		ProductProjection productProjection = Mockito.mock(ProductProjection.class);
		@SuppressWarnings("unchecked")
		PagedQueryResult<ProductProjection> value = Mockito.mock(PagedQueryResult.class);
		value.getResults().add(productProjection);
		products.complete(value);
		String categorykey = "Makeup";
		when(productService.findProductsWithCategory(categorykey)).thenReturn(products.get());
		ResponseEntity<PagedQueryResult<ProductProjection>> result = productController
				.getProductByCategory(categorykey);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test(expected = ProductException.class)
	public void testgetProductByCategoryExceptionCase() throws InterruptedException, ExecutionException {

		CompletableFuture<PagedQueryResult<ProductProjection>> products = new CompletableFuture<PagedQueryResult<ProductProjection>>();
		ProductProjection productProjection = Mockito.mock(ProductProjection.class);
		@SuppressWarnings("unchecked")
		PagedQueryResult<ProductProjection> value = Mockito.mock(PagedQueryResult.class);
		value.getResults().add(productProjection);
		products.complete(null);
		String categorykey = "Makeup";
		when(productService.findProductsWithCategory(categorykey)).thenReturn(products.get());
		productController.getProductByCategory(categorykey);
	}

	@Test()
	public void testgetCategory() throws InterruptedException, ExecutionException {

		CompletableFuture<PagedQueryResult<Category>> products = new CompletableFuture<PagedQueryResult<Category>>();
		Category category = Mockito.mock(Category.class);
		PagedQueryResult<Category> value = Mockito.mock(PagedQueryResult.class);
		value.getResults().add(category);
		products.complete(value);
		when(productService.getCategories()).thenReturn(products);
		ResponseEntity<CompletableFuture<PagedQueryResult<Category>>> result = productController.getCategories();
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test(expected = ProductException.class)
	public void testgetCategoryExceptionWhenDataisNotFound() throws InterruptedException, ExecutionException {

		CompletableFuture<PagedQueryResult<Category>> products = new CompletableFuture<PagedQueryResult<Category>>();
		products.complete(null);
		when(productService.getCategories()).thenReturn(products);
		productController.getCategories();
	}

}
