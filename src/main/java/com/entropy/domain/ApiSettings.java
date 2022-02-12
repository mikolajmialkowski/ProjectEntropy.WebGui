package com.entropy.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApiSettings.
 */
@Entity
@Table(name = "api_settings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApiSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "api_uri_1")
    private String apiUri1;

    @Column(name = "api_uri_2")
    private String apiUri2;

    @Column(name = "api_uri_3")
    private String apiUri3;

    @Column(name = "api_token")
    private String apiToken;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApiSettings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiUri1() {
        return this.apiUri1;
    }

    public ApiSettings apiUri1(String apiUri1) {
        this.setApiUri1(apiUri1);
        return this;
    }

    public void setApiUri1(String apiUri1) {
        this.apiUri1 = apiUri1;
    }

    public String getApiUri2() {
        return this.apiUri2;
    }

    public ApiSettings apiUri2(String apiUri2) {
        this.setApiUri2(apiUri2);
        return this;
    }

    public void setApiUri2(String apiUri2) {
        this.apiUri2 = apiUri2;
    }

    public String getApiUri3() {
        return this.apiUri3;
    }

    public ApiSettings apiUri3(String apiUri3) {
        this.setApiUri3(apiUri3);
        return this;
    }

    public void setApiUri3(String apiUri3) {
        this.apiUri3 = apiUri3;
    }

    public String getApiToken() {
        return this.apiToken;
    }

    public ApiSettings apiToken(String apiToken) {
        this.setApiToken(apiToken);
        return this;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiSettings)) {
            return false;
        }
        return id != null && id.equals(((ApiSettings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiSettings{" +
            "id=" + getId() +
            ", apiUri1='" + getApiUri1() + "'" +
            ", apiUri2='" + getApiUri2() + "'" +
            ", apiUri3='" + getApiUri3() + "'" +
            ", apiToken='" + getApiToken() + "'" +
            "}";
    }
}
