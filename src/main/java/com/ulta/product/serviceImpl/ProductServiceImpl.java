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

/**
 * implementation class for ProductService
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

	static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Autowired
	SphereClient client;
	@Autowired
	HystrixCommandPropertyResource hystrixCommandProp;
	@Autowired
	Environment env;

	/**
	 * 
	 * This method returns the product details on the basis of the provided
	 * product key
	 * 
	 * @param productkey
	 * @return Product
	 * @throws ProductException
	 */
	@Override
	@HystrixCommand(fallbackMethod = "getProductByKeyFallback", ignoreExceptions = {
			ProductException.class }, commandKey = "PRODUCTBYKEYCommand", threadPoolKey = "PRODUCTThreadPool")
	public Product getProductByKey(String key) throws ProductException, InterruptedException, ExecutionException {
		log.info("getProductByKey method start");
		// create ProductByKeyGet get request object with key
		final ProductByKeyGet request = ProductByKeyGet.of(key);
		// get the response from CT
		CompletionStage<Product> pro = client.execute(request);
		CompletableFuture<Product> returnProduct = null;
		// check for response is null then convert to completable future else
		// throw exception
		if (null != pro) {
			returnProduct = pro.toCompletableFuture();
		} else {
			// throw new exception as no data is found
			throw new ProductException("Product Data is empty");
		}
		log.info("getProductByKey method end");
		// return the response
		return returnProduct.get();
	}

	/**
	 * 
	 * This method returns all products
	 * 
	 * @param productkey
	 * @return Product
	 * @throws ProductException
	 */
	@Override
	@HystrixCommand(fallbackMethod = "getProductFallback", ignoreExceptions = {
			ProductException.class }, commandKey = "PRODUCTCommand", threadPoolKey = "PRODUCTThreadPool")
	public PagedQueryResult<ProductProjection> getProducts()
			throws ProductException, InterruptedException, ExecutionException {
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
		return returnProduct.get();
	}

	/**
	 * 
	 * This method returns the product details on the basis of the provided
	 * product category
	 * 
	 * @param categorykey
	 * @return PagedQueryResult<ProductProjection>
	 * @throws ProductException
	 */

	@Override
	@HystrixCommand(fallbackMethod = "getProductByCategoryFallback", ignoreExceptions = {
			ProductException.class }, commandKey = "PRODUCTBYCATEGORYCommand", threadPoolKey = "PRODUCTThreadPool")
	public PagedQueryResult<ProductProjection> findProductsWithCategory(String categorykey)
			throws InterruptedException, ExecutionException, ProductException {
		log.info("findProductsWithCategory method start");
		// find the Category from the category key
		CompletableFuture<Category> category = findCategory(categorykey);
		CompletableFuture<PagedQueryResult<ProductProjection>> returnProductwithcategory = null;
		ProductProjectionQuery exists = null;
		// check if category is not null then create ProjectProjectionQuery
		if (null != category && null != category.get()) {
			Category returnCat = category.get();
			exists = ProductProjectionQuery.ofCurrent()
					.withPredicates(m -> m.categories().isIn(Arrays.asList(returnCat)));
		} else {
			throw new ProductException("Product With Category is empty");
		}
		// get the response from CT
		CompletionStage<PagedQueryResult<ProductProjection>> productsWithCategory = client.execute(exists);

		// if product response is not null then get the actual result
		if (null != productsWithCategory) {
			returnProductwithcategory = productsWithCategory.toCompletableFuture();
		} else {
			throw new ProductException("Product With Category is empty");
		}

		// return the response
		log.info("findProductsWithCategory method end");
		return returnProductwithcategory.get();
	}

	/**
	 * This method returns the Category from the given category key
	 * 
	 * @param key
	 * @return CompletableFuture<Category>
	 */
	public CompletableFuture<Category> findCategory(String key) {
		CompletionStage<Category> category = client.execute(CategoryByKeyGet.of(key));
		CompletableFuture<Category> catCompletableFuture = category.toCompletableFuture();
		return catCompletableFuture;
	}

	/**
	 * This method returns all the Category
	 * 
	 * @return PagedQueryResult<Category> throws ProductException
	 */
	@Override
	@HystrixCommand(fallbackMethod = "getCategoriesFallback", ignoreExceptions = {
			ProductException.class }, commandKey = "GETCATEGORIESCommand", threadPoolKey = "PRODUCTThreadPool")
	public PagedQueryResult<Category> getCategories()
			throws ProductException, InterruptedException, ExecutionException {
		log.info("getCategories method start");
		// Create Category query object
		CategoryQuery catQuery = CategoryQuery.of();
		// get the response from CT
		CompletionStage<PagedQueryResult<Category>> result = client.execute(catQuery);
		CompletableFuture<PagedQueryResult<Category>> returnCategories = null;
		// check if response is not null
		if (null != result) {
			returnCategories = result.toCompletableFuture();
		} else {
			throw new ProductException("Categories is empty");
		}

		// return the response
		log.info("getCategories method end");
		return returnCategories.get();
	}

	/**
	 * Fallback method for getProductByKey
	 * 
	 * @param key
	 * @return
	 * @throws ProductException
	 */
	public Product getProductByKeyFallback(String key)
			throws ProductException, InterruptedException, ExecutionException {
		log.error("Critical -  CommerceTool UnAvailability error");
		throw new ProductException(key);

	}

	/**
	 * 
	 * Fallback method for getProduct
	 * 
	 * @throws ProductException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public PagedQueryResult<ProductProjection> getProductFallback()
			throws ProductException, InterruptedException, ExecutionException {
		log.error("Critical -  CommerceTool UnAvailability error");
		throw new ProductException("");

	}

	/**
	 * Fallback method for getProductByCategory
	 * 
	 * @param categorykey
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ProductException
	 */
	public PagedQueryResult<ProductProjection> getProductByCategoryFallback(String categorykey)
			throws InterruptedException, ExecutionException, ProductException {
		log.error("Critical -  CommerceTool UnAvailability error");
		throw new ProductException(categorykey);
	}

	/**
	 * Fallback method for getCategories
	 * 
	 * @return
	 * @throws ProductException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public PagedQueryResult<Category> getCategoriesFallback()
			throws ProductException, InterruptedException, ExecutionException {
		log.error("Critical -  CommerceTool UnAvailability error");
		throw new ProductException("");
	}

	/**
	 * Only for Junit
	 * 
	 * @param client
	 */

	public void setClient(SphereClient client) {
		this.client = client;
	}

	/**
	 * Only for Junit
	 * 
	 * @param hystrixCommandProp the hystrixCommandProp to set
	 */
	public void setHystrixCommandProp(HystrixCommandPropertyResource hystrixCommandProp) {
		this.hystrixCommandProp = hystrixCommandProp;
	}

}
