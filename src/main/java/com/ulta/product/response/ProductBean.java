package com.ulta.product.response;

public class ProductBean
{
    private MasterData masterData;

    private String id;

    private ProductType productType;

    public MasterData getMasterData ()
    {
        return masterData;
    }

    public void setMasterData (MasterData masterData)
    {
        this.masterData = masterData;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public ProductType getProductType ()
    {
        return productType;
    }

    public void setProductType (ProductType productType)
    {
        this.productType = productType;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [masterData = "+masterData+", id = "+id+", productType = "+productType+"]";
    }
}
			