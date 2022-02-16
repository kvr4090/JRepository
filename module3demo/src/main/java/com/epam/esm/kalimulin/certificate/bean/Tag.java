package com.epam.esm.kalimulin.certificate.bean;

import java.io.Serializable;
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

import org.springframework.hateoas.RepresentationModel;

import com.epam.esm.kalimulin.certificate.bean.user.UserClient;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tags")
public class Tag extends RepresentationModel<UserClient> implements Serializable {
	
	private static final long serialVersionUID = 6655171100825556208L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "certificates_has_tags",
			joinColumns = @JoinColumn(name = "tags_id"),
			inverseJoinColumns = @JoinColumn(name = "certificates_id"))
	private List<Certificate> certificates;
	
	@JsonCreator
	public Tag(@JsonProperty("id") long id, 
			@JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}

	public Tag() {
	}
	
	public Tag(long id) {
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

	public List<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (id != tag.id) return false;
        if (name != null ? !name.equals(tag.name) : tag.name != null) return false;
        return certificates != null ? certificates.equals(tag.certificates) : tag.certificates == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Tag.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("certificates=" + certificates)
                .toString();
    }
}
