
 
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

import com.ulta.product.exception.UltaException;
import com.ulta.product.response.CategoryBean;
import com.ulta.product.response.CategoryResponse;
import com.ulta.product.response.ProductBean;
import com.ulta.product.response.ProductResponse;
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

@SpringBootTest
public class ProductServiceImplTest {
	ProductServiceImpl productServiceImpl = new ProductServiceImpl();

	@Mock
	SphereClient client;

	@Mock
	ProductResponseTransformation responseTransformation;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		productServiceImpl.setClient(client);
		productServiceImpl.setResponseTransformation(responseTransformation);
	}


	@Test()
	public void testGetProductByKey() throws UltaException, InterruptedException, ExecutionException {
		String key = "facewash";
		ProductByKeyGet request = ProductByKeyGet.of(key);
		CompletionStage<Product> value = (CompletionStage<Product>) Mockito.mock(CompletionStage.class);
		when(client.execute(request)).thenReturn(value);
		ProductResponse productResponse = new  ProductResponse();
		ProductBean productBean = new ProductBean();
		productBean.setId("prod_001");
		productResponse.getProduct().add(productBean);
		when(responseTransformation.getProductByKeyTransformation(null)).thenReturn(productResponse );
		ProductResponse returnProduct = productServiceImpl.getProductByKey(key);
		assertEquals("prod_001", returnProduct.getProduct().get(0).getId());
	}
	
	@Test()
	public void testGetProductByKeyWhenNull() throws UltaException, InterruptedException, ExecutionException {
		String key = "facewash";
		ProductByKeyGet request = ProductByKeyGet.of(key);
		//CompletionStage<Product> value = (CompletionStage<Product>) Mockito.mock(CompletionStage.class);
		when(client.execute(request)).thenReturn(null);
		ProductResponse productResponse = new  ProductResponse();
		ProductBean productBean = new ProductBean();
		productBean.setId("prod_001");
		productResponse.getProduct().add(productBean);
		when(responseTransformation.getProductByKeyTransformation(null)).thenReturn(productResponse );
		ProductResponse returnProduct = productServiceImpl.getProductByKey(key);
		assertEquals(null, returnProduct);
	}

	

	@Test()
	public void testGetProductsSucessCase() throws UltaException {
		final ProductProjectionQuery pro = ProductProjectionQuery.ofCurrent();
		CompletionStage<PagedQueryResult<ProductProjection>> value = (CompletionStage<PagedQueryResult<ProductProjection>>) Mockito
				.mock(CompletionStage.class);
		ProductResponse productResponse = new  ProductResponse();
		ProductBean productBean = new ProductBean();
		productBean.setId("prod_001");
		when(client.execute(pro)).thenReturn(value);
		when(responseTransformation.getProductTransformation(null)).thenReturn(productResponse );
		ProductResponse result = productServiceImpl.getProducts();
		assertEquals(0, result.getProduct().size());
	}

	@Test()
	public void testGetProductsWhenProductDataIsNull()
			throws UltaException {
		final ProductProjectionQuery pro = ProductProjectionQuery.ofCurrent();
		CompletionStage<PagedQueryResult<ProductProjection>> value = null;
		when(client.execute(pro)).thenReturn(value);
		ProductResponse result=productServiceImpl.getProducts();
		
	}

	
	@Test(expected=UltaException.class)
	public void testFindProductsWithCategoryNullExceptionCase() throws InterruptedException, ExecutionException, UltaException {
		CompletionStage<Object> value = (CompletionStage<Object>) Mockito.mock(CompletionStage.class);
		@SuppressWarnings("unchecked")
		//CompletionStage<Category> category = (CompletionStage<Category>) Mockito.mock(CompletionStage.class);
		String categorykey = "Makeup";
		CompletableFuture<Category> category = new CompletableFuture<Category>();
		Category category2 = Mockito.mock(Category.class);
		category.complete(category2);
		when(client.execute(CategoryByKeyGet.of(categorykey))).thenReturn(category);
		when(productServiceImpl.findCategory(categorykey)).thenReturn(category);
		when(client.execute(null)).thenReturn(value);
		productServiceImpl.findProductsWithCategory(categorykey);
	}
	
	@Test()
	public void testFindProductsWithCategory() throws InterruptedException, ExecutionException, UltaException {
		CompletionStage<Object> value = (CompletionStage<Object>) Mockito.mock(CompletionStage.class);
		@SuppressWarnings("unchecked")
		CompletionStage<Category> category = (CompletionStage<Category>) Mockito.mock(CompletionStage.class);
		String categorykey = "Makeup";
		/*CompletableFuture<Category> category = new CompletableFuture<Category>();
		Category category2 = Mockito.mock(Category.class);
		category.complete(category2);*/
		when(client.execute(CategoryByKeyGet.of(categorykey))).thenReturn(category);
		//when(productServiceImpl.findCategory(categorykey)).thenReturn(category);
		when(client.execute(null)).thenReturn(value);
		ProductResponse result = productServiceImpl.findProductsWithCategory(categorykey);
		assertEquals("204", result.getErrorDetails().getErrorCode());
	}
	
	
	/*
	@Test(expected = NullPointerException.class)
	public void testFindProductsWithCategoryCase2() throws InterruptedException, ExecutionException, ProductException {
		String categorykey = "Makeup";
		CompletionStage<Category> category = (CompletionStage<Category>) Mockito.mock(CompletionStage.class);
		CompletableFuture<Category> returnValue= new CompletableFuture<Category>();
		Category category2= Mockito.mock(Category.class);
		returnValue.complete(category2);
		when(client.execute(CategoryByKeyGet.of(categorykey))).thenReturn(category);
		when(productServiceImpl.findCategory(categorykey)).thenReturn(returnValue);
		productServiceImpl.findProductsWithCategory(categorykey);
	}
	
	@Test(expected = ProductException.class)
	public void testFindProductsWithCategoryCase2Exception() throws InterruptedException, ExecutionException, ProductException {
		String categorykey = "Makeup";
		CompletionStage<Category> category = (CompletionStage<Category>) Mockito.mock(CompletionStage.class);
		CompletableFuture<Category> returnValue= new CompletableFuture<Category>();
		Category category2= Mockito.mock(Category.class);
		returnValue.complete(category2);
		when(client.execute(CategoryByKeyGet.of(categorykey))).thenReturn(category);
		when(productServiceImpl.findCategory(categorykey)).thenReturn(null);
		productServiceImpl.findProductsWithCategory(categorykey);
	}

	@Test(expected = ProductException.class)
	public void testFindProductsWithCategoryExceptionCase() throws InterruptedException, ExecutionException, ProductException {
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
*/
	@Test()
	public void testgetCategories() throws InterruptedException, ExecutionException, UltaException {
		CompletionStage<PagedQueryResult<Category>> category = (CompletionStage<PagedQueryResult<Category>>) Mockito
				.mock(CompletionStage.class);
		CategoryQuery catQuery = CategoryQuery.of();
		when(client.execute(catQuery)).thenReturn(category);
		CategoryResponse categorResponse= new CategoryResponse();
		CategoryBean catBean = new CategoryBean();
		catBean.setKey("makeup");
		categorResponse.getCategoryList().add(catBean );
		when(responseTransformation.getCategoryTransformation(null)).thenReturn(categorResponse);
		CategoryResponse result = productServiceImpl.getCategories();
		assertEquals("makeup", result.getCategoryList().get(0).getKey());
	}

	@Test()
	public void testgetCategoriesForException() throws InterruptedException, ExecutionException, UltaException {
		CompletionStage<PagedQueryResult<Category>> category = null;
		CategoryQuery catQuery = CategoryQuery.of();
		when(client.execute(catQuery)).thenReturn(category);
		productServiceImpl.getCategories();
	}
}
