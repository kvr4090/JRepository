package com.epam.esm.kalimulin.certificate.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.StringJoiner;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.hateoas.RepresentationModel;

import com.epam.esm.kalimulin.certificate.bean.user.UserClient;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "orders")
public class Order extends RepresentationModel<UserClient> implements Serializable {
	
	private static final long serialVersionUID = 3132598496271830039L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "users_id")
	private UserClient user;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "orders_has_certificates",
			joinColumns = @JoinColumn(name = "orders_id"),
			inverseJoinColumns = @JoinColumn(name = "certificates_id"))
	private List<Certificate> certificates;
	
	@CreationTimestamp
	@Column(name = "create_date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "UTC")
	private Instant createDate;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@JsonCreator()
	public Order(@JsonProperty("id") long id,
				@JsonProperty("certificates") List<Certificate> certificates,
				@JsonProperty("createDate")Instant createDate, 
				@JsonProperty("price")BigDecimal price) {
		this.id = id;
		this.certificates = certificates;
		this.createDate = createDate;
		this.price = price;
	}

	public Order() {
	}
	
	public Order(UserClient user) {
		this.user = user;
	}

	public Order(UserClient user, List<Certificate> certificates, Instant createDate) {
		this.user = user;
		this.certificates = certificates;
		this.createDate = createDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserClient getUser() {
		return user;
	}

	public void setUser(UserClient user) {
		this.user = user;
	}

	public List<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}

	public Instant getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Instant createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        if (user != null ? !user.equals(order.user) : order.user != null) return false;
        if (certificates != null ? !certificates.equals(order.certificates) : order.certificates != null) return false;
        if (createDate != null ? !createDate.equals(order.createDate) : order.createDate != null) return false;
        return price != null ? price.equals(order.price) : order.price == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("user=" + user)
                .add("certificates=" + certificates)
                .add("createDate=" + createDate)
                .add("price=" + price)
                .toString();
    }
}
