package id.co.ardata.megatrik.customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Content{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("contents")
	private List<ContentsItem> contents;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("content_category_id")
	private int contentCategoryId;

	@SerializedName("id")
	private int id;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setContents(List<ContentsItem> contents){
		this.contents = contents;
	}

	public List<ContentsItem> getContents(){
		return contents;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
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

	public void setContentCategoryId(int contentCategoryId){
		this.contentCategoryId = contentCategoryId;
	}

	public int getContentCategoryId(){
		return contentCategoryId;
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
			"Content{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",contents = '" + contents + '\'' + 
			",name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",content_category_id = '" + contentCategoryId + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}