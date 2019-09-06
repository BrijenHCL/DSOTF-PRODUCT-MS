package com.ulta.product.response;

public class MasterData
{
    private Current current;

    public Current getCurrent ()
    {
        return current;
    }

    public void setCurrent (Current current)
    {
        this.current = current;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [current = "+current+"]";
    }
}
