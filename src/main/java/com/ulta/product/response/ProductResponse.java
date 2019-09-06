package com.ulta.product.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ulta.product.exception.ErrorDetails;

@JsonInclude(Include.NON_NULL)
public class ProductResponse {

	@JsonInclude(Include.NON_EMPTY)
	List<ProductBean> product = new ArrayList<ProductBean>();
	@JsonUnwrapped
	ErrorDetails errorDetails = null;

	public List<ProductBean> getProduct() {
		return product;
	}

	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}

	public void setProduct(List<ProductBean> product) {
		this.product = product;
	}

}
