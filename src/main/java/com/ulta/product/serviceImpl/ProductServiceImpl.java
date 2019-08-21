/*
 * Copyright (C) 2019 ULTA
 * http://www.ulta.com
 * BrijendraK@ulta.com
 * All rights reserved
 */
package com.ulta.product.serviceImpl;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ulta.product.exception.ProductException;
import com.ulta.product.resources.HystrixCommandPropertyResource;
import com.ulta.product.service.ProductService;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryByKeyGet;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductByKeyGet;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;

@Service
public class ProductServiceImpl implements ProductService {
	static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Autowired
	SphereClient client;
	@Autowired
	HystrixCommandPropertyResource hystrixCommandProp;
	@Autowired
	Environment env;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.product.service.ProductService#getProductByKey(String key)
	 */
	@Override
	@HystrixCommand(fallbackMethod = "getProductByKeyFallback", ignoreExceptions = {
			ProductException.class }, commandKey = "PRODUCTBYKEYCommand", threadPoolKey = "PRODUCTBYKEYThreadPool")
	public Product getProductByKey(String key) throws ProductException, InterruptedException, ExecutionException {
		log.info("getProductByKey method start");
		final ProductByKeyGet request = ProductByKeyGet.of(key);
		CompletionStage<Product> pro = client.execute(request);
		CompletableFuture<Product> returnProduct = null;
		if (null != pro) {
			returnProduct = pro.toCompletableFuture();
		} else {
			throw new ProductException("Product Data is empty");
		}
		log.info("getProductByKey method end");
		return returnProduct.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.product.service.ProductService#getProducts()
	 */

	@Override
	public CompletableFuture<PagedQueryResult<ProductProjection>> getProducts() throws ProductException {
		log.info("getProducts method start");

		final ProductProjectionQuery pro = ProductProjectionQuery.ofCurrent();
		CompletionStage<PagedQueryResult<ProductProjection>> result = client.execute(pro);
		CompletableFuture<PagedQueryResult<ProductProjection>> returnProduct = null;
		if (null != result) {
			returnProduct = result.toCompletableFuture();
		} else {
			throw new ProductException("Product Data is empty");
		}

		log.info("getProducts method end");
		return returnProduct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.product.service.ProductService#findProductsWithCategory(String
	 * categorykey)
	 */

	@Override
	public CompletableFuture<PagedQueryResult<ProductProjection>> findProductsWithCategory(String categorykey)
			throws InterruptedException, ExecutionException, ProductException {
		log.info("findProductsWithCategory method start");

		CompletionStage<Category> category = client.execute(CategoryByKeyGet.of(categorykey));
		CompletableFuture<PagedQueryResult<ProductProjection>> returnProductwithcategory = null;
		ProductProjectionQuery exists = null;
		if (null != category.toCompletableFuture()) {
			Category returnCat = category.toCompletableFuture().get();
			exists = ProductProjectionQuery.ofCurrent()
					.withPredicates(m -> m.categories().isIn(Arrays.asList(returnCat)));
		}
		CompletionStage<PagedQueryResult<ProductProjection>> productsWithCategory = client.execute(exists);

		if (null != productsWithCategory) {
			returnProductwithcategory = productsWithCategory.toCompletableFuture();
		} else {
			throw new ProductException("Product With Category is empty");
		}

		log.info("findProductsWithCategory method end");
		return returnProductwithcategory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ulta.product.service.ProductService#getCategories()
	 */
	@Override
	public CompletableFuture<PagedQueryResult<Category>> getCategories() throws ProductException {
		log.info("getCategories method start");
		CategoryQuery catQuery = CategoryQuery.of();
		CompletionStage<PagedQueryResult<Category>> result = client.execute(catQuery);
		CompletableFuture<PagedQueryResult<Category>> returnCategories = null;
		if (null != result) {
			returnCategories = result.toCompletableFuture();
		} else {
			throw new ProductException("Categories is empty");
		}
		log.info("getCategories method end");
		return returnCategories;
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws ProductException
	 */
	public Product getProductByKeyFallback(String key) throws ProductException {
		log.error("Critical -  CommerceTool UnAvailability error");
		throw new ProductException(key);

	}

	/**
	 * 
	 * @param client
	 */

	public void setClient(SphereClient client) {
		this.client = client;
	}

	/**
	 * @param hystrixCommandProp the hystrixCommandProp to set
	 */
	public void setHystrixCommandProp(HystrixCommandPropertyResource hystrixCommandProp) {
		this.hystrixCommandProp = hystrixCommandProp;
	}

}
