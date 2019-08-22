/*
 * Copyright (C) 2019 ULTA
 * http://www.ulta.com
 * BrijendraK@ulta.com
 * All rights reserved
 */
package com.ulta.product.controller;

import static com.ulta.product.constant.ProductConstants.PRODUCT_BASE_URI;
import static com.ulta.product.constant.ProductConstants.VIEW_CATEGOTY_ALL;
import static com.ulta.product.constant.ProductConstants.VIEW_PRODUCT_ALL;
import static com.ulta.product.constant.ProductConstants.VIEW_PRODUCT_BYCATEGORYID_URI;
import static com.ulta.product.constant.ProductConstants.VIEW_PRODUCT_BYPRODUCTKEY_URI;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ulta.product.exception.ProductException;
import com.ulta.product.service.ProductService;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedQueryResult;

@RestController
@RequestMapping(PRODUCT_BASE_URI)
public class ProductController {

	static Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	ProductService ProductService;

	/**
	 * 
	 * @param productKey
	 * @return
	 * @throws ProductException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@RequestMapping(value = VIEW_PRODUCT_BYPRODUCTKEY_URI, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> getProductByKey(@PathVariable("productKey") String productKey)
			throws ProductException, InterruptedException, ExecutionException {

		log.info("getProductByKey method start");
		Product product = ProductService.getProductByKey(productKey);

		try {

			if (null != product) {
				log.info("get the product details successfully.");
			} else {
				log.info("getting product details as null");
				throw new ProductException("Product not found.");
			}

		} catch (Exception ex) {
			log.error("exception during fetching the product detail-" + ex.getMessage());
			throw new ProductException(ex.getMessage());
		}
		log.info("getProductByKey method end");
		return ResponseEntity.ok().body(product);
	}

	/**
	 * 
	 * @return
	 * @throws ProductException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */

	@RequestMapping(value = VIEW_PRODUCT_ALL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedQueryResult<ProductProjection>> getProducts()
			throws ProductException, InterruptedException, ExecutionException {

		log.info("getProducts method start");
		PagedQueryResult<ProductProjection> products = ProductService.getProducts();

		try {
			if (null != products.getResults()) {
				log.info("get the product details successfully.");
			} else {
				log.info("getting product details as null");
				throw new ProductException("Product not found.");
			}
		} catch (Exception ex) {
			log.error("exception during fetching the product detail-" + ex.getMessage());
			throw new ProductException(ex.getMessage());
		}
		log.info("getProducts method end");
		return ResponseEntity.ok().body(products);
	}

	/**
	 * 
	 * @param categorykey
	 * @return
	 * @throws ProductException
	 */
	@RequestMapping(value = VIEW_PRODUCT_BYCATEGORYID_URI, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedQueryResult<ProductProjection>> getProductByCategory(
			@PathVariable("categorykey") String categorykey) throws ProductException {

		log.info("getProductByCategory method start");

		PagedQueryResult<ProductProjection> productswithcategory;
		try {
			productswithcategory = ProductService.findProductsWithCategory(categorykey);
			if (null != productswithcategory.getResults()) {
				log.info("get the productwithcategory details successfully.");
			} else {
				log.info("getting productwithcategory details as null");
				throw new ProductException("productwithcategory not found.");
			}

		} catch (Exception ex) {
			log.error("exception during fetching the productwithcategory detail-" + ex.getMessage());
			throw new ProductException(ex.getMessage());
		}
		log.info("getProductByCategory method end");
		return ResponseEntity.ok().body(productswithcategory);
	}

	/**
	 * 
	 * @return
	 * @throws ProductException
	 */
	@RequestMapping(value = VIEW_CATEGOTY_ALL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CompletableFuture<PagedQueryResult<Category>>> getCategories() throws ProductException {

		log.info("getCategories method start");
		CompletableFuture<PagedQueryResult<Category>> categories = ProductService.getCategories();

		try {

			if (null != categories.get()) {
				log.info("get the categories details successfully.");
			} else {
				log.info("getting categories details as null");
				throw new ProductException("Product not found.");
			}
		} catch (Exception ex) {
			log.error("exception during fetching the categories detail-" + ex.getMessage());
			throw new ProductException(ex.getMessage());
		}
		log.info("getCategories method end");
		return ResponseEntity.ok().body(categories);
	}

	/**
	 * setter method for product service
	 */
	public void setProductService(ProductService productService) {
		this.ProductService = productService;
	}

}