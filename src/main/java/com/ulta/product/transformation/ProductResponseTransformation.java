package com.ulta.product.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.ulta.product.response.CategoryBean;
import com.ulta.product.response.CategoryResponse;
import com.ulta.product.response.Current;
import com.ulta.product.response.MasterData;
import com.ulta.product.response.MasterVariant;
import com.ulta.product.response.Prices;
import com.ulta.product.response.ProductBean;
import com.ulta.product.response.ProductResponse;
import com.ulta.product.response.ProductType;
import com.ulta.product.response.Value;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.Product;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedQueryResult;

public class ProductResponseTransformation {

	public ProductResponse getProductByKeyTransformation(CompletableFuture<Product> productDetails) {

		ProductResponse productResponse = new ProductResponse();
		if (null != productDetails) {

			Product producRes = null;
			ProductBean product = new ProductBean();
			MasterData masterData = new MasterData();
			Current current = new Current();
			MasterVariant masterVariant = new MasterVariant();
			ProductType productType = new ProductType();
			Prices prices = new Prices();

			try {
				producRes = productDetails.get();
				if (null == producRes) {
					return productResponse;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// set product Id
			product.setId(producRes.getId());
			// set master vairant
			masterVariant.setId(producRes.getMasterData().getCurrent().getMasterVariant().getId().toString());
			masterVariant.setKey(producRes.getMasterData().getCurrent().getMasterVariant().getKey());
			masterVariant.setSku(producRes.getMasterData().getCurrent().getMasterVariant().getSku());
			List<Price> priceList=new ArrayList<>();
			if (null!=producRes.getMasterData().getCurrent().getMasterVariant().getPrices()){
				 priceList = producRes.getMasterData().getCurrent().getMasterVariant().getPrices();
			}
			if (!priceList.isEmpty()) {
				prices.setCountry(priceList.get(0).getCountry().toString());
				prices.setId(priceList.get(0).getId());
				Value value = new Value();
				value.setCentAmount(priceList.get(0).getValue().getNumber().toString());
				value.setCurrencyCode(priceList.get(0).getValue().getCurrency().toString());
				prices.setValue(value );
			}
			
			masterVariant.setPrices(prices);
			// set current object
			current.setMasterVariant(masterVariant);
			
			//System.out.println("testing "+producRes.getMasterData().getCurrent().getName().get("en_US"));
			
			current.setName(producRes.getMasterData().getCurrent().getName().toString());
			current.setDescription(producRes.getMasterData().getCurrent().getDescription().toString());

			// set master Data
			masterData.setCurrent(current);
			// set product resposne
			product.setMasterData(masterData);
			productType.setId(producRes.getProductType().getId());
			productType.setTypeId(producRes.getProductType().getTypeId());

			product.setProductType(productType);
			productResponse.getProduct().add(product);
		}
		return productResponse;
	}
	
	public ProductResponse findProductsWithCategoryTransformation(CompletableFuture<PagedQueryResult<ProductProjection>> productDetails) {
		
		ProductResponse productResponse = new ProductResponse();
		List<ProductProjection> productList =null;
		try {
			if (null!=productDetails && null!=productDetails.get()) {
					productList=productDetails.get().getResults();
					
					for(ProductProjection productprojection : productList) {
						ProductBean productBean= new ProductBean();
						MasterData masterData = new MasterData();
						Current current = new Current();
						MasterVariant masterVariant = new MasterVariant();
						ProductType productType = new ProductType();
						//set productId
						productBean.setId(productprojection.getId());
						//set master data properties
						masterVariant.setId(productprojection.getMasterVariant().getId().toString());
						masterVariant.setKey(productprojection.getMasterVariant().getKey());
						masterVariant.setSku(productprojection.getMasterVariant().getSku());
						//set product description
						current.setDescription(productprojection.getDescription().toString());
						current.setName(productprojection.getName().toString());
						//set master variant
						current.setMasterVariant(masterVariant);
						//set current 
						masterData.setCurrent(current);
						//set master data
						productBean.setMasterData(masterData);
						//set product Type
						productType.setId(productprojection.getProductType().getId());
						productType.setTypeId(productprojection.getProductType().getTypeId());

						productBean.setProductType(productType);
						//adding product bean to product response list
						productResponse.getProduct().add(productBean);
					}
				
			}
			else 
				return productResponse;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productResponse;
		
	}
	
	public  CategoryResponse getCategoryTransformation(CompletableFuture<PagedQueryResult<Category>> categoryRequest) {
		
		CategoryResponse catResponse = new CategoryResponse();
		List<CategoryBean> catLst = new ArrayList<CategoryBean>();
		
		if (null!=categoryRequest) {
			try {
				List<Category> returnCategory=  categoryRequest.get().getResults();
				if(null!=returnCategory) {
					for(Category cat : returnCategory) {
						CategoryBean category = new CategoryBean();
						category.setKey(cat.getId());
						category.setDescription(cat.getDescription().toString());
						category.setName(cat.getName().toString());
						catLst.add(category);
					}
					
					catResponse.setCategoryList(catLst);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return catResponse;
		
	}
	

	public ProductResponse getProductTransformation(CompletableFuture<PagedQueryResult<ProductProjection>> returnProduct) {

	ProductResponse productResponse = new ProductResponse();
	if (null != returnProduct) {

		PagedQueryResult<ProductProjection> producRes = null;
	
		

		try {
			producRes = returnProduct.get();
			if (null == producRes) {
				return productResponse;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!producRes.getResults().isEmpty()) {
			for (ProductProjection projection : producRes.getResults()) {
				MasterData masterData = new MasterData();
				Current current = new Current();
				MasterVariant masterVariant = new MasterVariant();
				ProductType productType = new ProductType();
				Prices prices = new Prices();
				ProductBean product = new ProductBean();
				product.setId(projection.getId());
				// set master vairant
				masterVariant.setId(projection.getMasterVariant().getId().toString());
				masterVariant.setKey(projection.getMasterVariant().getKey());
				masterVariant.setSku(projection.getMasterVariant().getSku());
				List<Price> priceList=new ArrayList<>();
				if (null!=projection.getMasterVariant().getPrices()){
					 priceList = projection.getMasterVariant().getPrices();
				}
				if (!priceList.isEmpty()) {
					if(null!= priceList.get(0).getCountry()){
					prices.setCountry(priceList.get(0).getCountry().toString());
					}
					prices.setId(priceList.get(0).getId());
					Value value = new Value();
					value.setCentAmount(priceList.get(0).getValue().getNumber().toString());
					value.setCurrencyCode(priceList.get(0).getValue().getCurrency().toString());
					prices.setValue(value );
				}
				
				masterVariant.setPrices(prices);
				// set current object
				current.setMasterVariant(masterVariant);
				
				//System.out.println("testing "+producRes.getMasterData().getCurrent().getName().get("en_US"));
				
				current.setName(projection.getName().toString());
				current.setDescription(projection.getDescription().toString());

				// set master Data
				masterData.setCurrent(current);
				// set product resposne
				product.setMasterData(masterData);
				productType.setId(projection.getProductType().getId());
				productType.setTypeId(projection.getProductType().getTypeId());

				product.setProductType(productType);
				productResponse.getProduct().add(product);
			}
		}
		// set product Id
		
	}
	return productResponse;
	}
	

}
