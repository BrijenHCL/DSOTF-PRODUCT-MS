package com.ulta.product.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ulta.product.exception.ErrorDetails;

public class CategoryResponse {

	
	List<CategoryBean> categoryList = new ArrayList<>();

	@JsonUnwrapped
	ErrorDetails errorDetails = null;
	
	
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}

	public List<CategoryBean> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryBean> categoryList) {
		this.categoryList = categoryList;
	}

	
	
}
