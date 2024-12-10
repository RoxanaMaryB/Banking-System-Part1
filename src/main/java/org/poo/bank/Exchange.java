package org.poo.bank;

import lombok.Data;
import org.poo.fileio.ExchangeInput;

@Data
public class Exchange {
    private String from;
    private String to;
    private double rate;
    private int timestamp;

    public Exchange(ExchangeInput exchangeInput) {
        this.from = exchangeInput.getFrom();
        this.to = exchangeInput.getTo();
        this.rate = exchangeInput.getRate();
        this.timestamp = exchangeInput.getTimestamp();
    }
}
