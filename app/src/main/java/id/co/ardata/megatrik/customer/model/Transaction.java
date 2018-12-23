package id.co.ardata.megatrik.customer.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Transaction{

	@SerializedName("total")
	private String total;

	@SerializedName("payment_type")
	private String paymentType;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("payment_value")
	private String paymentValue;

	@SerializedName("transaction_number")
	private String transactionNumber;

	@SerializedName("name")
	private String name;

	@SerializedName("bank_name")
	private String bankName;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("bank_account")
	private String bankAccount;

	public void setTotal(String total){
		this.total = total;
	}

	public String getTotal(){
		return total;
	}

	public void setPaymentType(String paymentType){
		this.paymentType = paymentType;
	}

	public String getPaymentType(){
		return paymentType;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setPaymentValue(String paymentValue){
		this.paymentValue = paymentValue;
	}

	public String getPaymentValue(){
		return paymentValue;
	}

	public void setTransactionNumber(String transactionNumber){
		this.transactionNumber = transactionNumber;
	}

	public String getTransactionNumber(){
		return transactionNumber;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setBankName(String bankName){
		this.bankName = bankName;
	}

	public String getBankName(){
		return bankName;
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

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setBankAccount(String bankAccount){
		this.bankAccount = bankAccount;
	}

	public String getBankAccount(){
		return bankAccount;
	}

	@Override
 	public String toString(){
		return 
			"Transaction{" + 
			"total = '" + total + '\'' + 
			",payment_type = '" + paymentType + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",payment_value = '" + paymentValue + '\'' + 
			",transaction_number = '" + transactionNumber + '\'' + 
			",name = '" + name + '\'' + 
			",bank_name = '" + bankName + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",order_id = '" + orderId + '\'' + 
			",bank_account = '" + bankAccount + '\'' + 
			"}";
		}
}