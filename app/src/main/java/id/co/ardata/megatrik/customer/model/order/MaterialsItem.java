package id.co.ardata.megatrik.customer.model.order;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class MaterialsItem{

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("materiallist")
	private Materiallist materiallist;

	@SerializedName("material_list_id")
	private int materialListId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private int orderId;

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setMateriallist(Materiallist materiallist){
		this.materiallist = materiallist;
	}

	public Materiallist getMateriallist(){
		return materiallist;
	}

	public void setMaterialListId(int materialListId){
		this.materialListId = materialListId;
	}

	public int getMaterialListId(){
		return materialListId;
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

	@Override
 	public String toString(){
		return 
			"MaterialsItem{" + 
			"quantity = '" + quantity + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",materiallist = '" + materiallist + '\'' + 
			",material_list_id = '" + materialListId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",order_id = '" + orderId + '\'' + 
			"}";
		}
}