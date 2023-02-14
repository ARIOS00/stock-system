package com.example.stock.entity;

import com.example.stock.serialization.KlineSerialize;
import com.example.stock.vo.UserSDK;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private String name;

    @Id
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

    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("name", this.getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        map.put("kdate", simpleDateFormat.format(this.getKdate()));
        map.put("close", this.getClose().toString());
        map.put("volume", this.getVolume().toString());
        map.put("open", this.getOpen().toString());
        map.put("high", this.getHigh().toString());
        map.put("low", this.getLow().toString());
        return map;
    }

    public Kline getKline(Map<Object, Object> objMap) throws ParseException {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<Object, Object> entry : objMap.entrySet()) {
            map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }

        this.setName(map.get("name"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        this.setKdate(formatter.parse(map.get("kdate")));
        this.setClose(Double.parseDouble(map.get("close")));
        this.setVolume(Double.parseDouble(map.get("volume")));
        this.setOpen(Double.parseDouble(map.get("open")));
        this.setHigh(Double.parseDouble(map.get("high")));
        this.setLow(Double.parseDouble(map.get("low")));
        return this;
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