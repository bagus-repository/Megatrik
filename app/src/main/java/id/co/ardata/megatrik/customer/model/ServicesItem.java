package id.co.ardata.megatrik.customer.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ServicesItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("servicelist")
	private Servicelist servicelist;

	@SerializedName("service_list_id")
	private int serviceListId;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private int orderId;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setServicelist(Servicelist servicelist){
		this.servicelist = servicelist;
	}

	public Servicelist getServicelist(){
		return servicelist;
	}

	public void setServiceListId(int serviceListId){
		this.serviceListId = serviceListId;
	}

	public int getServiceListId(){
		return serviceListId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setOrderId(int orderId){
		this.orderId = orderId;
	}

	public int getOrderId(){
		return orderId;
	}

	@Override
 	public String toString(){
		return 
			"ServicesItem{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",servicelist = '" + servicelist + '\'' + 
			",service_list_id = '" + serviceListId + '\'' + 
			",id = '" + id + '\'' + 
			",order_id = '" + orderId + '\'' + 
			"}";
		}
}