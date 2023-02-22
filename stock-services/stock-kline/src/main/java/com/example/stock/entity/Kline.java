package com.example.stock.entity;

import com.example.stock.serialization.KlineSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "k_line")
@JsonSerialize(using = KlineSerialize.class)
@IdClass(KlinePK.class)
public class Kline implements Serializable {
    @Id
    @Column(name = "name", nullable = false)
    protected String name;

    @Id
    @Column(name = "k_date", nullable = false)
    protected Date kdate;

    @Column(name = "close", nullable = false)
    protected Double close;

    @Column(name = "volume", nullable = false)
    protected Double volume;

    @Column(name = "open", nullable = false)
    protected Double open;

    @Column(name = "high", nullable = false)
    protected Double high;

    @Column(name = "low", nullable = false)
    protected Double low;

    public Kline(KlineDefault kline) {
        this.setName(kline.getName());
        this.setKdate(kline.getKdate());
        this.setClose(kline.getClose());
        this.setVolume(kline.getVolume());
        this.setOpen(kline.getOpen());
        this.setHigh(kline.getHigh());
        this.setLow(kline.getLow());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Kline)) {
            return false;
        }
        Kline other = (Kline) obj;
        System.out.println(Objects.equals(kdate, other.kdate));
        return Objects.equals(name, other.name) &&
                Objects.equals(kdate, other.kdate) &&
                Objects.equals(close, other.close) &&
                Objects.equals(volume, other.volume) &&
                Objects.equals(open, other.open) &&
                Objects.equals(high, other.high) &&
                Objects.equals(low, other.low);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, kdate, close, volume, open, high, low);
    }
}