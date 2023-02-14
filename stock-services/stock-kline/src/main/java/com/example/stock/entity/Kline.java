package com.example.stock.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "k_line")
public class Kline implements Serializable {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "k_date", nullable = false)
    private Date kdate;

    @Column(name = "close", nullable = false)
    private Double close;

    @Column(name = "volume", nullable = false)
    private Double volume;

    @Column(name = "open", nullable = false)
    private Double open;

    @Column(name = "high", nullable = false)
    private Double high;

    @Column(name = "low", nullable = false)
    private Double low;
}
