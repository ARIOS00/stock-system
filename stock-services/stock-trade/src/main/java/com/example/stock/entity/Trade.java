package com.example.stock.entity;

import com.example.stock.serialization.TradeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "trade")
@JsonSerialize(using = TradeSerialize.class)
@IdClass(TradePK.class)
public class Trade implements Serializable {
    @Id
    @Column(name = "name", nullable = false)
    protected String name;

    @Id
    @Column(name = "fresh_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected String freshTime;

    @Column(name = "price", nullable = false)
    protected Double price;

    @Column(name = "change", nullable = false)
    protected Double change;

    @Column(name = "rate", nullable = false)
    protected Double rate;

    public Trade(TradeDefault tradeDefault) {
        this.setName(tradeDefault.getName());
        this.setFreshTime(tradeDefault.getFreshTime());
        this.setPrice(tradeDefault.getPrice());
        this.setChange(tradeDefault.getChange());
        this.setRate(tradeDefault.getRate());
    }
}
