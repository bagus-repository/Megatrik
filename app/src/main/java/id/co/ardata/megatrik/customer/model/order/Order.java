package id.co.ardata.megatrik.customer.model.order;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Order{

	@SerializedName("address")
	private String address;

	@SerializedName("order_end")
	private String orderEnd;

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("technician_id")
	private int technicianId;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("technician")
	private Technician technician;

	@SerializedName("services")
	private List<ServicesItem> services;

	@SerializedName("order_start")
	private String orderStart;

	@SerializedName("order_status")
	private OrderStatus orderStatus;

	@SerializedName("city_name")
	private String cityName;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("materials")
	private List<MaterialsItem> materials;

	@SerializedName("id")
	private int id;

	@SerializedName("customer_id")
	private int customerId;

	@SerializedName("transaction")
	private Transaction transaction;

	@SerializedName("longitude")
	private double longitude;

	@SerializedName("customer")
	private Customer customer;

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setOrderEnd(String orderEnd){
		this.orderEnd = orderEnd;
	}

	public String getOrderEnd(){
		return orderEnd;
	}

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setTechnicianId(int technicianId){
		this.technicianId = technicianId;
	}

	public int getTechnicianId(){
		return technicianId;
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

	public void setTechnician(Technician technician){
		this.technician = technician;
	}

	public Technician getTechnician(){
		return technician;
	}

	public void setServices(List<ServicesItem> services){
		this.services = services;
	}

	public List<ServicesItem> getServices(){
		return services;
	}

	public void setOrderStart(String orderStart){
		this.orderStart = orderStart;
	}

	public String getOrderStart(){
		return orderStart;
	}

	public void setOrderStatus(OrderStatus orderStatus){
		this.orderStatus = orderStatus;
	}

	public OrderStatus getOrderStatus(){
		return orderStatus;
	}

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setMaterials(List<MaterialsItem> materials){
		this.materials = materials;
	}

	public List<MaterialsItem> getMaterials(){
		return materials;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCustomerId(int customerId){
		this.customerId = customerId;
	}

	public int getCustomerId(){
		return customerId;
	}

	public void setTransaction(Transaction transaction){
		this.transaction = transaction;
	}

	public Transaction getTransaction(){
		return transaction;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

	public void setCustomer(Customer customer){
		this.customer = customer;
	}

	public Customer getCustomer(){
		return customer;
	}

	@Override
 	public String toString(){
		return 
			"Order{" + 
			"address = '" + address + '\'' + 
			",order_end = '" + orderEnd + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",technician_id = '" + technicianId + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",technician = '" + technician + '\'' + 
			",services = '" + services + '\'' + 
			",order_start = '" + orderStart + '\'' + 
			",order_status = '" + orderStatus + '\'' + 
			",city_name = '" + cityName + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",materials = '" + materials + '\'' + 
			",id = '" + id + '\'' + 
			",customer_id = '" + customerId + '\'' + 
			",transaction = '" + transaction + '\'' + 
			",longitude = '" + longitude + '\'' + 
			",customer = '" + customer + '\'' + 
			"}";
		}
}