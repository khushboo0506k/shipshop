package com.kns.shipshop.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name= "orders")
public class Orders implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long orderId;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_ORDERS_USER"))
	private User user;
	
	@Column
	private Date orderDate;
	
	@ManyToOne(targetEntity = Address.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "address_id", foreignKey = @ForeignKey(name = "FK_ORDERS_ADDRESS"))
	private Address address;
	
	@OneToMany(mappedBy = "order")
	private Set<OrderItems> orderItems = new HashSet<>();
	
	@Column
	private Double amount;
	
	@Column
	private boolean isPaid;

	public Orders() {
	}

	public Orders(User user, Address address, Set<OrderItems> orderItems, Double amount,
			boolean isPaid) {
		this.user = user;
		this.address = address;
		this.orderItems = orderItems;
		this.amount = amount;
		this.isPaid = isPaid;
	}
	
	@PrePersist
	protected void onCreate() {
		if(orderDate == null)
			orderDate = new Date();
	}

	public Long getOrderId() {
		return orderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<OrderItems> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItems> orderItems) {
		this.orderItems = orderItems;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
	
}
	