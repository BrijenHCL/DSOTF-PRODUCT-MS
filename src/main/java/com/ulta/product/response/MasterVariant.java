package com.ulta.product.response;

public class MasterVariant
{
    private String id;

    private String sku;

    private Prices prices;

    private String key;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getSku ()
    {
        return sku;
    }

    public void setSku (String sku)
    {
        this.sku = sku;
    }

    public Prices getPrices ()
    {
        return prices;
    }

    public void setPrices (Prices prices)
    {
        this.prices = prices;
    }

    public String getKey ()
    {
        return key;
    }

    public void setKey (String key)
    {
        this.key = key;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", sku = "+sku+", prices = "+prices+", key = "+key+"]";
    }
}