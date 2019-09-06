
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

import com.ulta.product.exception.UltaException;
import com.ulta.product.response.CategoryBean;
import com.ulta.product.response.CategoryResponse;
import com.ulta.product.response.ProductBean;
import com.ulta.product.response.ProductResponse;
import com.ulta.product.service.ProductService;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.client.SphereClient;
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

	@Test
	public void testgetProductBykey() throws UltaException, InterruptedException, ExecutionException {

		ProductResponse value =  new ProductResponse();
		ProductBean productBean = new ProductBean();
		productBean.setId("prod_001");
		value.getProduct().add(productBean);
		String key = "Liquid";
		when(productService.getProductByKey(key)).thenReturn(value);
		ResponseEntity<ProductResponse> result = productController.getProductByKey(key);
		assertEquals("prod_001", result.getBody().getProduct().get(0).getId());
	}
	
	@Test
	public void testgetProductBykeyErrorCase() throws UltaException, InterruptedException, ExecutionException {

		ProductResponse value =  new ProductResponse();
		String key = "Liquid";
		when(productService.getProductByKey(key)).thenReturn(value);
		ResponseEntity<ProductResponse> result = productController.getProductByKey(key);
		assertEquals("204", result.getBody().getErrorDetails().getErrorCode());
	}

	@Test
	public void testgetProducts() throws UltaException, InterruptedException, ExecutionException {

		ProductResponse value =  new ProductResponse();
		ProductBean productBean = new ProductBean();
		productBean.setId("prod_001");
		value.getProduct().add(productBean);
		when(productService.getProducts()).thenReturn(value);
		ResponseEntity<ProductResponse> result = productController.getProducts();
		assertEquals("prod_001", result.getBody().getProduct().get(0).getId());
	}
	
	@Test
	public void testgetProductsWhenNodataFound() throws UltaException {

		ProductResponse value =  new ProductResponse();
		when(productService.getProducts()).thenReturn(value);
		ResponseEntity<ProductResponse> result = productController.getProducts();
		assertEquals("204", result.getBody().getErrorDetails().getErrorCode());
	}

	
	@Test(expected=UltaException.class)
	public void testgetProductsForException() throws UltaException {

		when(productService.getProducts()).thenThrow(UltaException.class);
		productController.getProducts();
	}
	
	

	@Test
	public void testgetProductByCategory() throws UltaException {

		ProductResponse value =  new ProductResponse();
		ProductBean productBean = new ProductBean();
		productBean.setId("prod_001");
		value.getProduct().add(productBean);
		String categorykey = "Makeup";
		when(productService.findProductsWithCategory(categorykey)).thenReturn(value);
		ResponseEntity<ProductResponse> result = productController
				.getProductByCategory(categorykey);
		assertEquals("prod_001", result.getBody().getProduct().get(0).getId());
	}

	
	@Test
	public void testgetProductByCategoryErrorCase() throws InterruptedException, ExecutionException, UltaException {

		
		ProductResponse value =  new ProductResponse();
		String categorykey = "Makeup";
		when(productService.findProductsWithCategory(categorykey)).thenReturn(value);
		ResponseEntity<ProductResponse> result = productController
				.getProductByCategory(categorykey);
		assertEquals("204", result.getBody().getErrorDetails().getErrorCode());
	}
	
	
	
	
	

	@SuppressWarnings("unchecked")
	@Test(expected = UltaException.class)
	public void testgetProductByCategoryExceptionCase()
			throws UltaException, InterruptedException, ExecutionException {

		String key = "Liquid";
		when(productService.getProductByKey(key)).thenThrow(new UltaException("Failure"));
		productController.getProductByCategory(key);
	}

	@Test()
	public void testgetCategoryWhenDataisNotFound() throws InterruptedException, ExecutionException, UltaException {

		CategoryResponse categoryResponse = new CategoryResponse();
		when(productService.getCategories()).thenReturn(categoryResponse);
		ResponseEntity<CategoryResponse> resultCategory = productController.getCategories();
		assertEquals("204", resultCategory.getBody().getErrorDetails().getErrorCode());
	}

	
	@Test()
	public void testgetCategory() throws InterruptedException, ExecutionException, UltaException {

		CategoryResponse categoryResponse = new CategoryResponse();
		CategoryBean catBean = new CategoryBean();
		catBean.setKey("Makeup");
		categoryResponse.getCategoryList().add(catBean );
		when(productService.getCategories()).thenReturn(categoryResponse);
		ResponseEntity<CategoryResponse> resultCategory = productController.getCategories();
		assertEquals("Makeup", resultCategory.getBody().getCategoryList().get(0).getKey());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected= UltaException.class)
	public void testgetCategoryExeptionCase() throws InterruptedException, ExecutionException, UltaException {

		CategoryResponse categoryResponse = new CategoryResponse();
		CategoryBean catBean = new CategoryBean();
		catBean.setKey("Makeup");
		categoryResponse.getCategoryList().add(catBean );
		when(productService.getCategories()).thenThrow(UltaException.class);
		productController.getCategories();
	}
}
