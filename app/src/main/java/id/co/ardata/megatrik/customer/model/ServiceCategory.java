package id.co.ardata.megatrik.customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ServiceCategory{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("img_url")
	private String imgUrl;

	@SerializedName("name")
	private String name;

	@SerializedName("servicelists")
	private List<ServicelistsItem> servicelists;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setImgUrl(String imgUrl){
		this.imgUrl = imgUrl;
	}

	public String getImgUrl(){
		return imgUrl;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setServicelists(List<ServicelistsItem> servicelists){
		this.servicelists = servicelists;
	}

	public List<ServicelistsItem> getServicelists(){
		return servicelists;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"ServiceCategory{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",img_url = '" + imgUrl + '\'' + 
			",name = '" + name + '\'' + 
			",servicelists = '" + servicelists + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}