package id.co.ardata.megatrik.customer.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Transactionreview{

	@SerializedName("transaction_id")
	private int transactionId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("value")
	private int value;

	public void setTransactionId(int transactionId){
		this.transactionId = transactionId;
	}

	public int getTransactionId(){
		return transactionId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
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

	public void setValue(int value){
		this.value = value;
	}

	public int getValue(){
		return value;
	}

	@Override
 	public String toString(){
		return 
			"Transactionreview{" + 
			"transaction_id = '" + transactionId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}
}