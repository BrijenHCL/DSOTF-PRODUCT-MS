package com.ulta.product.response;

public class Description
{
    private String en_US;

    public String getEn_US ()
    {
        return en_US;
    }

    public void setEn_US (String en_US)
    {
        this.en_US = en_US;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [en-US = "+en_US+"]";
    }
}
