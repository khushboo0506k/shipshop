package com.kns.shipshop.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(unique = true, nullable = false)
	private Integer productCode;
	@Column
	private String productName;
	@Column
	private double mrp;
	@Column
	private Double discount;
	
	@OneToMany(mappedBy = "product")
	private Set<Image> productImages = new HashSet<>();
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "category_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_CATEGORY"))
	private Category category;
	
	@Column
	private String description;
	
	@Column
	private Date insertTimestamp;
	
	@Column
	private Date updateTimestamp;
	
	@Column
	private Double rating;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "brand_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_BRAND"))
	private Brand brand;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "user_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_USER"))
	private User user;
	
	public Product() {
	}
	
	@PrePersist
	protected void onCreate() {
		if(insertTimestamp == null)
			insertTimestamp = new Date();
		if(discount == null)
			this.discount = 0.0;
	}
	
	@PreUpdate
	protected void onUpdate() {
		updateTimestamp = new Date();
	}
	
	public Product(Integer productCode, String productName, double mrp, double discount, Category category,
			String description, Brand brand) {
		this.productCode = productCode;
		this.productName = productName;
		this.mrp = mrp;
		this.discount = discount;
		this.category = category;
		this.description = description;
		this.brand = brand;
	}

	public Integer getProductCode() {
		return productCode;
	}
	public void setProductCode(Integer productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getMrp() {
		return mrp;
	}
	public void setMrp(double mrp) {
		this.mrp = mrp;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public Set<Image> getProductImages() {
		return productImages;
	}
	public void setProductImages(Set<Image> productImages) {
		this.productImages = productImages;
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
}
	