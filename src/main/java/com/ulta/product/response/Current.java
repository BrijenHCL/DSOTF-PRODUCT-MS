package com.ulta.product.response;

public class Current
{
    private String name;

    private String description;

    private MasterVariant masterVariant;
    
   
    public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public MasterVariant getMasterVariant() {
		return masterVariant;
	}


	public void setMasterVariant(MasterVariant masterVariant) {
		this.masterVariant = masterVariant;
	}


	@Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", description = "+description+", masterVariant = "+masterVariant+"]";
    }
}
			
