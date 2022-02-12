package com.entropy.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GeneralInfo.
 */
@Entity
@Table(name = "general_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GeneralInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "records_in_data_base_amount", precision = 21, scale = 2)
    private BigDecimal recordsInDataBaseAmount;

    @Column(name = "api_calls_amount", precision = 21, scale = 2)
    private BigDecimal apiCallsAmount;

    @Column(name = "api_statistics")
    private String apiStatistics;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GeneralInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRecordsInDataBaseAmount() {
        return this.recordsInDataBaseAmount;
    }

    public GeneralInfo recordsInDataBaseAmount(BigDecimal recordsInDataBaseAmount) {
        this.setRecordsInDataBaseAmount(recordsInDataBaseAmount);
        return this;
    }

    public void setRecordsInDataBaseAmount(BigDecimal recordsInDataBaseAmount) {
        this.recordsInDataBaseAmount = recordsInDataBaseAmount;
    }

    public BigDecimal getApiCallsAmount() {
        return this.apiCallsAmount;
    }

    public GeneralInfo apiCallsAmount(BigDecimal apiCallsAmount) {
        this.setApiCallsAmount(apiCallsAmount);
        return this;
    }

    public void setApiCallsAmount(BigDecimal apiCallsAmount) {
        this.apiCallsAmount = apiCallsAmount;
    }

    public String getApiStatistics() {
        return this.apiStatistics;
    }

    public GeneralInfo apiStatistics(String apiStatistics) {
        this.setApiStatistics(apiStatistics);
        return this;
    }

    public void setApiStatistics(String apiStatistics) {
        this.apiStatistics = apiStatistics;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeneralInfo)) {
            return false;
        }
        return id != null && id.equals(((GeneralInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeneralInfo{" +
            "id=" + getId() +
            ", recordsInDataBaseAmount=" + getRecordsInDataBaseAmount() +
            ", apiCallsAmount=" + getApiCallsAmount() +
            ", apiStatistics='" + getApiStatistics() + "'" +
            "}";
    }
}
