package com.entropy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prediction.
 */
@Entity
@Table(name = "prediction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Prediction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_from")
    private String dateFrom;

    @Column(name = "date_to")
    private String dateTo;

    @Column(name = "duration")
    private String duration;

    @Column(name = "current_price")
    private String currentPrice;

    @Column(name = "predicted_price")
    private String predictedPrice;

    @Column(name = "probability")
    private String probability;

    @ManyToOne
    @JsonIgnoreProperties(value = { "predictions" }, allowSetters = true)
    private Cryptocurrency cryptocurrency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prediction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateFrom() {
        return this.dateFrom;
    }

    public Prediction dateFrom(String dateFrom) {
        this.setDateFrom(dateFrom);
        return this;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return this.dateTo;
    }

    public Prediction dateTo(String dateTo) {
        this.setDateTo(dateTo);
        return this;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDuration() {
        return this.duration;
    }

    public Prediction duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCurrentPrice() {
        return this.currentPrice;
    }

    public Prediction currentPrice(String currentPrice) {
        this.setCurrentPrice(currentPrice);
        return this;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getPredictedPrice() {
        return this.predictedPrice;
    }

    public Prediction predictedPrice(String predictedPrice) {
        this.setPredictedPrice(predictedPrice);
        return this;
    }

    public void setPredictedPrice(String predictedPrice) {
        this.predictedPrice = predictedPrice;
    }

    public String getProbability() {
        return this.probability;
    }

    public Prediction probability(String probability) {
        this.setProbability(probability);
        return this;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public Cryptocurrency getCryptocurrency() {
        return this.cryptocurrency;
    }

    public void setCryptocurrency(Cryptocurrency cryptocurrency) {
        this.cryptocurrency = cryptocurrency;
    }

    public Prediction cryptocurrency(Cryptocurrency cryptocurrency) {
        this.setCryptocurrency(cryptocurrency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prediction)) {
            return false;
        }
        return id != null && id.equals(((Prediction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prediction{" +
            "id=" + getId() +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            ", duration='" + getDuration() + "'" +
            ", currentPrice='" + getCurrentPrice() + "'" +
            ", predictedPrice='" + getPredictedPrice() + "'" +
            ", probability='" + getProbability() + "'" +
            "}";
    }
}
