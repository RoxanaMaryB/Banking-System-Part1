package org.poo.bank;

import lombok.Data;

@Data
public class Exchange {
    private String from;
    private String to;
    private double rate;

    public Exchange(final String from, final String to, final double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }
}
