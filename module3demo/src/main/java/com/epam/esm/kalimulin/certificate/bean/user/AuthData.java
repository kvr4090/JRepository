package com.epam.esm.kalimulin.certificate.bean.user;

import java.util.List;
import java.util.StringJoiner;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "users")
public class AuthData {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;
	private boolean enabled;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="users_has_roles",
	    joinColumns= {@JoinColumn(name="users_id")},
	    inverseJoinColumns = {@JoinColumn(name="roles_id")}
	)
	private List<Role> roles;

	public AuthData() {
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthData authData = (AuthData) o;

        if (id != authData.id) return false;
        if (enabled != authData.enabled) return false;
        if (username != null ? !username.equals(authData.username) : authData.username != null) return false;
        if (password != null ? !password.equals(authData.password) : authData.password != null) return false;
        return roles != null ? roles.equals(authData.roles) : authData.roles == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AuthData.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("enabled=" + enabled)
                .add("roles=" + roles)
                .toString();
    }
}