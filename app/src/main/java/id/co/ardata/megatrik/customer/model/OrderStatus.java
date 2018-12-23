package id.co.ardata.megatrik.customer.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class OrderStatus{

	@SerializedName("is_accepted")
	private int isAccepted;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("is_completed")
	private int isCompleted;

	public void setIsAccepted(int isAccepted){
		this.isAccepted = isAccepted;
	}

	public int getIsAccepted(){
		return isAccepted;
	}

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

	public void setIsCompleted(int isCompleted){
		this.isCompleted = isCompleted;
	}

	public int getIsCompleted(){
		return isCompleted;
	}

	@Override
 	public String toString(){
		return 
			"OrderStatus{" + 
			"is_accepted = '" + isAccepted + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",order_id = '" + orderId + '\'' + 
			",is_completed = '" + isCompleted + '\'' + 
			"}";
		}
}