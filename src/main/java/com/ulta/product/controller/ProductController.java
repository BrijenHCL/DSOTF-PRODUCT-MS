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
	public ResponseEntity<Product> getProductByKey(@PathVariable("productKey") String productKey)
			throws ProductException, InterruptedException, ExecutionException {

		log.info("getProductByKey method start");
		// get the products from service implementation by passing product key
		// as parameter
		Product product = productService.getProductByKey(productKey);

		try {

			// check if data from service layer is null or empty else throw
			// exception for null response
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
		// return the response
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
	public ResponseEntity<PagedQueryResult<ProductProjection>> getProducts()
			throws ProductException, InterruptedException, ExecutionException {

		log.info("getProducts method start");
		//get result from the product service 
		PagedQueryResult<ProductProjection> products = productService.getProducts();

		try {
			//check if response is null or empty result
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
		//return the response
		log.info("getProducts method end");
		return ResponseEntity.ok().body(products);
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
	public ResponseEntity<PagedQueryResult<ProductProjection>> getProductByCategory(
			@PathVariable("categorykey") String categorykey) throws ProductException {

		log.info("getProductByCategory method start");

		PagedQueryResult<ProductProjection> productswithcategory;
		try {
			// get the response from service implementation
			productswithcategory = productService.findProductsWithCategory(categorykey);
			// check if response data is empty or null else throw exception
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
		// return the response
		log.info("getProductByCategory method end");
		return ResponseEntity.ok().body(productswithcategory);
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
	public ResponseEntity<PagedQueryResult<Category>> getCategories()
			throws ProductException, InterruptedException, ExecutionException {

		log.info("getCategories method start");
		// get categories from product service implementation
		PagedQueryResult<Category> categories = productService.getCategories();

		try {
			// check if response from service is null else throw exception
			if (null != categories) {
				log.info("get the categories details successfully.");
			} else {
				// throw new product exception as no category is found
				log.info("getting categories details as null");
				throw new ProductException("Product not found.");
			}
		} catch (Exception ex) {
			log.error("exception during fetching the categories detail-" + ex.getMessage());
			throw new ProductException(ex.getMessage());
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