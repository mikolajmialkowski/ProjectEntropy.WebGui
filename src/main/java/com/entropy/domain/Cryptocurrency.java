package com.entropy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cryptocurrency.
 */
@Entity
@Table(name = "cryptocurrency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cryptocurrency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "pair")
    private String pair;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "price")
    private String price;

    @OneToMany(mappedBy = "cryptocurrency")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cryptocurrency" }, allowSetters = true)
    private Set<Prediction> predictions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cryptocurrency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cryptocurrency name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPair() {
        return this.pair;
    }

    public Cryptocurrency pair(String pair) {
        this.setPair(pair);
        return this;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Cryptocurrency symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return this.price;
    }

    public Cryptocurrency price(String price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Set<Prediction> getPredictions() {
        return this.predictions;
    }

    public void setPredictions(Set<Prediction> predictions) {
        if (this.predictions != null) {
            this.predictions.forEach(i -> i.setCryptocurrency(null));
        }
        if (predictions != null) {
            predictions.forEach(i -> i.setCryptocurrency(this));
        }
        this.predictions = predictions;
    }

    public Cryptocurrency predictions(Set<Prediction> predictions) {
        this.setPredictions(predictions);
        return this;
    }

    public Cryptocurrency addPrediction(Prediction prediction) {
        this.predictions.add(prediction);
        prediction.setCryptocurrency(this);
        return this;
    }

    public Cryptocurrency removePrediction(Prediction prediction) {
        this.predictions.remove(prediction);
        prediction.setCryptocurrency(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cryptocurrency)) {
            return false;
        }
        return id != null && id.equals(((Cryptocurrency) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cryptocurrency{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pair='" + getPair() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", price='" + getPrice() + "'" +
            "}";
    }
}
