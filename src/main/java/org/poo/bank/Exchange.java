package org.poo.bank;

import lombok.Data;
import org.poo.fileio.ExchangeInput;

@Data
public class Exchange {
    private String from;
    private String to;
    private double rate;

    public Exchange(String from, String to, double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }
}
