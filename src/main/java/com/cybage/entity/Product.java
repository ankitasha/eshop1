package com.cybage.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Products")
public class Product implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	private double price;
	private byte[] image;
	
	private Date creationDate ;

	public Product() 
	{
		super();
	}
    
	@Id
	@Column(name="Code", length= 25 , nullable=false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
   
	@Column(name="Name", length= 255 , nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
   
	@Column(name="Price", length= 25 , nullable=false)
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
   @Lob
   @Column(name="Image" , length=Integer.MAX_VALUE , nullable= true)
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
    
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Date", length= 25 , nullable=false)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
