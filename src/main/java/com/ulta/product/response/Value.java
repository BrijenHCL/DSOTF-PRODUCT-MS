package com.ulta.product.response;

public class Value
{
    private String centAmount;

    private String fractionDigits;

    private String type;

    private String currencyCode;

    public String getCentAmount ()
    {
        return centAmount;
    }

    public void setCentAmount (String centAmount)
    {
        this.centAmount = centAmount;
    }

    public String getFractionDigits ()
    {
        return fractionDigits;
    }

    public void setFractionDigits (String fractionDigits)
    {
        this.fractionDigits = fractionDigits;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getCurrencyCode ()
    {
        return currencyCode;
    }

    public void setCurrencyCode (String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [centAmount = "+centAmount+", fractionDigits = "+fractionDigits+", type = "+type+", currencyCode = "+currencyCode+"]";
    }
}
