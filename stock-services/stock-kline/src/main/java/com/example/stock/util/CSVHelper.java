package com.example.stock.util;

import com.example.stock.entity.Kline;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {
    public static ByteArrayInputStream tutorialsToCSV(List<Kline> klines) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            List<String> titles = Arrays.asList("name", "date", "close", "volume", "open", "high", "low");
            csvPrinter.printRecord(titles);
            for (Kline kline : klines) {
                List<String> data = Arrays.asList(
                        kline.getName(),
                        simpleDateFormat.format(kline.getKdate()),
                        kline.getClose().toString(),
                        kline.getVolume().toString(),
                        kline.getOpen().toString(),
                        kline.getHigh().toString(),
                        kline.getLow().toString()
                );
                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
