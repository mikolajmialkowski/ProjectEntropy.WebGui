package com.entropy.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SchedulerSettings.
 */
@Entity
@Table(name = "scheduler_settings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SchedulerSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "jhi_interval")
    private String interval;

    @Column(name = "jhi_limit")
    private String limit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SchedulerSettings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInterval() {
        return this.interval;
    }

    public SchedulerSettings interval(String interval) {
        this.setInterval(interval);
        return this;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getLimit() {
        return this.limit;
    }

    public SchedulerSettings limit(String limit) {
        this.setLimit(limit);
        return this;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerSettings)) {
            return false;
        }
        return id != null && id.equals(((SchedulerSettings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SchedulerSettings{" +
            "id=" + getId() +
            ", interval='" + getInterval() + "'" +
            ", limit='" + getLimit() + "'" +
            "}";
    }
}
