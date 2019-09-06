package com.ulta.product.response;

public class ProductType
{
    private String typeId;

    private String id;

    public String getTypeId ()
    {
        return typeId;
    }

    public void setTypeId (String typeId)
    {
        this.typeId = typeId;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [typeId = "+typeId+", id = "+id+"]";
    }
}
		
