package id.co.ardata.megatrik.customer.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Transaction{

	@SerializedName("transactionstatus")
	private Transactionstatus transactionstatus;

	@SerializedName("payment_value")
	private String paymentValue;

	@SerializedName("transactionreview")
	private Transactionreview transactionreview;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("total")
	private int total;

	@SerializedName("payment_type")
	private String paymentType;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("transaction_number")
	private String transactionNumber;

	@SerializedName("name")
	private String name;

	@SerializedName("bank_name")
	private String bankName;

	@SerializedName("id")
	private int id;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("bank_account")
	private String bankAccount;

	public void setTransactionstatus(Transactionstatus transactionstatus){
		this.transactionstatus = transactionstatus;
	}

	public Transactionstatus getTransactionstatus(){
		return transactionstatus;
	}

	public void setPaymentValue(String paymentValue){
		this.paymentValue = paymentValue;
	}

	public String getPaymentValue(){
		return paymentValue;
	}

	public void setTransactionreview(Transactionreview transactionreview){
		this.transactionreview = transactionreview;
	}

	public Transactionreview getTransactionreview(){
		return transactionreview;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
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
			"transactionstatus = '" + transactionstatus + '\'' + 
			",payment_value = '" + paymentValue + '\'' + 
			",transactionreview = '" + transactionreview + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",total = '" + total + '\'' + 
			",payment_type = '" + paymentType + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",transaction_number = '" + transactionNumber + '\'' + 
			",name = '" + name + '\'' + 
			",bank_name = '" + bankName + '\'' + 
			",id = '" + id + '\'' + 
			",order_id = '" + orderId + '\'' + 
			",bank_account = '" + bankAccount + '\'' + 
			"}";
		}
}