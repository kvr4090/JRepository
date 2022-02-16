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
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "certificates")
public class Certificate extends RepresentationModel<Certificate> implements Serializable {
		
	private static final long serialVersionUID = -6989927377730573761L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "duration")
	private int duration;
	
	@Column(name = "create_date")
	@CreationTimestamp
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "UTC")
	private Instant createDate;
	
	@Column(name = "last_update_date")
	@UpdateTimestamp
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "UTC")
	private Instant lastUpdateDate;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "certificates_has_tags",
			joinColumns = @JoinColumn(name = "certificates_id"),
			inverseJoinColumns = @JoinColumn(name = "tags_id"))
	private List<Tag> tags;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "orders_has_certificates",
			joinColumns = @JoinColumn(name = "certificates_id"),
			inverseJoinColumns = @JoinColumn(name = "orders_id"))
	private List<Order> orders;
	
	@JsonCreator
	public Certificate(@JsonProperty("id") long id,
			@JsonProperty("name") String name, 
			@JsonProperty("description") String description,
			@JsonProperty("price") BigDecimal price, 
			@JsonProperty("duration") int duration, 
			@JsonProperty("createDate") Instant createDate,
			@JsonProperty("lastUpdateDate") Instant lastUpdateDate, 
			@JsonProperty("tags") List<Tag> tags) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.duration = duration;
		this.createDate = createDate;
		this.lastUpdateDate = lastUpdateDate;
		this.tags = tags;
	}

	public Certificate() {
	}
	
	public Certificate(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Instant getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Instant createDate) {
		this.createDate = createDate;
	}

	public Instant getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Instant lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Certificate that = (Certificate) o;

        if (id != that.id) return false;
        if (duration != that.duration) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (lastUpdateDate != null ? !lastUpdateDate.equals(that.lastUpdateDate) : that.lastUpdateDate != null)
            return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;
        return orders != null ? orders.equals(that.orders) : that.orders == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + duration;
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Certificate.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("price=" + price)
                .add("duration=" + duration)
                .add("createDate=" + createDate)
                .add("lastUpdateDate=" + lastUpdateDate)
                .add("tags=" + tags)
                .add("orders=" + orders)
                .toString();
    }
}
