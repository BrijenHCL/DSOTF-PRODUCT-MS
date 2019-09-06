/*
 * Copyright (C) 2019 ULTA
 * http://www.ulta.com
 * BrijendraK@ulta.com
 * All rights reserved
 */
package com.ulta.product.serviceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ulta.product.exception.ErrorDetails;
import com.ulta.product.exception.UltaException;
import com.ulta.product.response.CategoryResponse;
import com.ulta.product.response.ProductResponse;
import com.ulta.product.service.ProductService;
import com.ulta.product.transformation.ProductResponseTransformation;

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
	Environment env;

	ProductResponseTransformation responseTransformation = new ProductResponseTransformation();

	ProductResponse product = null;

	/**
	 * 
	 * This method returns the product details on the basis of the provided
	 * product key
	 * 
	 * @param productkey
	 * @return Product
	 * @throws UltaException
	 */
	@Override
	/*
	 * @HystrixCommand(fallbackMethod = "getProductByKeyFallback",
	 * ignoreExceptions = { ProductException.class }, commandKey =
	 * "PRODUCTBYKEYCommand", threadPoolKey = "PRODUCTThreadPool")
	 */
	public ProductResponse getProductByKey(String key) throws UltaException {

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
			product = responseTransformation.getProductByKeyTransformation(returnProduct);
		}
		log.info("getProductByKey method end");
		// return the response
		return product;
	}

	/**
	 * 
	 * This method returns all products
	 * 
	 * @param productkey
	 * @return Product
	 * @throws UltaException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@Override
	/*
	 * @HystrixCommand(fallbackMethod = "getProductFallback", ignoreExceptions =
	 * { UltaException.class }, commandKey = "PRODUCTCommand", threadPoolKey =
	 * "PRODUCTThreadPool")
	 */
	public ProductResponse getProducts() throws UltaException {
		log.info("getProducts method start");

		final ProductProjectionQuery pro = ProductProjectionQuery.ofCurrent();
		CompletionStage<PagedQueryResult<ProductProjection>> result = client.execute(pro);
		CompletableFuture<PagedQueryResult<ProductProjection>> returnProduct = null;
		if (null != result) {
			returnProduct = result.toCompletableFuture();
			product = responseTransformation.getProductTransformation(returnProduct);
		}
		log.info("getProducts method end");
		return product;
	}

	/**
	 * 
	 * This method returns the product details on the basis of the provided
	 * product category
	 * 
	 * @param categorykey
	 * @return PagedQueryResult<ProductProjection>
	 * @throws UltaException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */

	@Override
	/*
	 * @HystrixCommand(fallbackMethod = "getProductByCategoryFallback",
	 * ignoreExceptions = { UltaException.class }, commandKey =
	 * "PRODUCTBYCATEGORYCommand", threadPoolKey = "PRODUCTThreadPool")
	 */
	public ProductResponse findProductsWithCategory(String categorykey) throws UltaException {
		log.info("findProductsWithCategory method start");
		// find the Category from the category key
		CompletableFuture<Category> category = findCategory(categorykey);
		CompletableFuture<PagedQueryResult<ProductProjection>> returnProductwithcategory = null;
		ProductResponse productResponse = new ProductResponse();
		ProductProjectionQuery exists = null;
		// check if category is not null then create ProjectProjectionQuery
		try {
			if (null != category && null != category.get()) {
				Category returnCat = category.get();
				exists = ProductProjectionQuery.ofCurrent()
						.withPredicates(m -> m.categories().isIn(Arrays.asList(returnCat)));
			} else {
				ErrorDetails errorDetails = new ErrorDetails();
				errorDetails.setTimestamp(new Date());
				errorDetails.setErrorCode(String.valueOf(HttpStatus.SC_NO_CONTENT));
				errorDetails.setMessage("Category not Found");
				productResponse.setErrorDetails(errorDetails);
				return productResponse;
			}
		} catch (Exception e) {
			throw new UltaException("Exception in fetching category");
		}
		// get the response from CT
		CompletionStage<PagedQueryResult<ProductProjection>> productsWithCategory = client.execute(exists);

		returnProductwithcategory = productsWithCategory.toCompletableFuture();
		// get the result transformation
		productResponse = responseTransformation.findProductsWithCategoryTransformation(returnProductwithcategory);

		// return the response
		log.info("findProductsWithCategory method end");
		return productResponse;
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
	/*
	 * @HystrixCommand(fallbackMethod = "getCategoriesFallback",
	 * ignoreExceptions = { UltaException.class }, commandKey =
	 * "GETCATEGORIESCommand", threadPoolKey = "PRODUCTThreadPool")
	 */
	public CategoryResponse getCategories() throws UltaException {
		log.info("getCategories method start");
		CategoryResponse categoryResponse = new CategoryResponse();
		// Create Category query object
		CategoryQuery catQuery = CategoryQuery.of();
		// get the response from CT
		CompletionStage<PagedQueryResult<Category>> result = client.execute(catQuery);
		CompletableFuture<PagedQueryResult<Category>> returnCategories = null;

		// check if response is not null
		if (null != result) {
			returnCategories = result.toCompletableFuture();
			categoryResponse = responseTransformation.getCategoryTransformation(returnCategories);
		}

		// return the response
		log.info("getCategories method end");
		return categoryResponse;
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
	 * only for Junit
	 * 
	 * @param responseTransformation
	 */
	public void setResponseTransformation(ProductResponseTransformation responseTransformation) {
		this.responseTransformation = responseTransformation;
	}

}
