package org.poo.bank;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class Report {
    private String IBAN;
    private double balance;
    private String currency;
    private List<Transaction> transactions;
}
