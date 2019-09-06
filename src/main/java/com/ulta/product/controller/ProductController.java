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

import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ulta.product.exception.ErrorDetails;
import com.ulta.product.exception.UltaException;
import com.ulta.product.response.CategoryResponse;
import com.ulta.product.response.ProductResponse;
import com.ulta.product.service.ProductService;

/**
 * 
 * This is Controller Class for Product service
 */
@RestController
@RequestMapping(PRODUCT_BASE_URI)
public class ProductController {

	static Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	ProductService productService;
	ProductResponse product = null;

	/**
	 * 
	 * getProductByKey method returns the products from CT by the product Key
	 * 
	 * @param productKey
	 * @return ResponseEntity<Product>
	 * @throws ProductException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@RequestMapping(value = VIEW_PRODUCT_BYPRODUCTKEY_URI, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> getProductByKey(@PathVariable("productKey") String productKey)
			throws UltaException {

		log.info("getProductByKey method start");
		// get the products from service implementation by passing product key
		product = productService.getProductByKey(productKey);

		if (product.getProduct().isEmpty()) {
			log.info("getProductByKey method end with error");

			ErrorDetails errorDetails = new ErrorDetails(new Date(), "No product Found with this productKey",
					String.valueOf(HttpStatus.SC_NO_CONTENT));
			product.setErrorDetails(errorDetails);
		}
		log.info("getProductByKey method end");
		return ResponseEntity.ok().body(product);

	}

	/**
	 * 
	 * getProducts returns all the products
	 * 
	 * @return
	 * @throws ProductException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */

	@RequestMapping(value = VIEW_PRODUCT_ALL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> getProducts() throws UltaException {

		log.info("getProducts method start");

		try {

			// get result from the product service
			product = productService.getProducts();
			// check if response is null or empty result
			if (product.getProduct().isEmpty()) {
				log.info("getProductByKey method end with error");

				ErrorDetails errorDetails = new ErrorDetails(new Date(), "No product Found with this productKey",
						String.valueOf(HttpStatus.SC_NO_CONTENT));
				product.setErrorDetails(errorDetails);
			}

		} catch (Exception ex) {
			log.error("exception during fetching the product detail-" + ex.getMessage());
			throw new UltaException("Internal Server Error.");
		}
		// return the response
		log.info("getProducts method end");
		return ResponseEntity.ok().body(product);
	}

	/**
	 * 
	 * getProductByCategory returns products details by product category
	 * 
	 * @param categorykey
	 * @return ResponseEntity<PagedQueryResult<ProductProjection>>
	 * @throws ProductException
	 */
	@RequestMapping(value = VIEW_PRODUCT_BYCATEGORYID_URI, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable("categorykey") String categorykey)
			throws UltaException {

		log.info("getProductByCategory method start");

		// ProductResponse productswithcategory;
		try {
			// get the response from service implementation
			product = productService.findProductsWithCategory(categorykey);
			// check if response data is empty or null else throw exception
			if (null == product.getErrorDetails()) {
				if (product.getProduct().isEmpty()) {
					log.info("getting productwithcategory details as null");
					ErrorDetails errorDetails = new ErrorDetails();
					errorDetails.setTimestamp(new Date());
					errorDetails.setErrorCode(String.valueOf(HttpStatus.SC_NO_CONTENT));
					errorDetails.setMessage("No product Found with this category");
					product.setErrorDetails(errorDetails);
				}
			}

		} catch (Exception ex) {
			log.error("exception during fetching the productwithcategory detail-" + ex.getMessage());
			throw new UltaException("Internal Server Error.");
		}
		// return the response
		log.info("getProductByCategory method end");
		return ResponseEntity.ok().body(product);
	}

	/**
	 * 
	 * getCategories returns all categories
	 * 
	 * @return ResponseEntity<PagedQueryResult<Category>>
	 * @throws ProductException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@RequestMapping(value = VIEW_CATEGOTY_ALL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CategoryResponse> getCategories()
			throws UltaException, InterruptedException, ExecutionException {

		log.info("getCategories method start");
		CategoryResponse categories = new CategoryResponse();
		try {
			// get categories from product service implementation
			categories = productService.getCategories();

			// check if response from service is null else throw exception
			if (categories.getCategoryList().isEmpty()) {

				ErrorDetails errorDetails = new ErrorDetails(new Date(), "No category Found",
						String.valueOf(HttpStatus.SC_NO_CONTENT));
				categories.setErrorDetails(errorDetails);
				log.info("get the categories details successfully.");
			}
		} catch (Exception ex) {
			log.error("exception during fetching the categories detail-" + ex.getMessage());
			throw new UltaException("Internal Server Error.");
		}
		log.info("getCategories method end");
		// return the response
		return ResponseEntity.ok().body(categories);
	}

	/**
	 * setter method for product service Only for Junit
	 */
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}