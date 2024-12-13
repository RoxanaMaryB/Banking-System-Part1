package org.poo.bank;

import java.util.*;

public class CurrencyConverter {
    private final List<Exchange> exchangeRates;

    public CurrencyConverter(List<Exchange> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public Exchange findExchange(String from, String to) {
        Map<String, List<Exchange>> graph = new HashMap<>();
        for (Exchange exchange : exchangeRates) {
            if (!graph.containsKey(exchange.getFrom())) {
                graph.put(exchange.getFrom(), new ArrayList<>());
            }
            graph.get(exchange.getFrom()).add(exchange);

            if (!graph.containsKey(exchange.getTo())) {
                graph.put(exchange.getTo(), new ArrayList<>());
            }
            graph.get(exchange.getTo()).add(new Exchange(exchange.getTo(), exchange.getFrom(), 1.0 / exchange.getRate()));
        }

        // BFS
        Queue<Exchange> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(new Exchange(from, from, 1.0));

        while (!queue.isEmpty()) {
            Exchange current = queue.poll(); // front of the queue
            if(current.getTo().equals(to)) {
                return new Exchange(from, to, current.getRate());
            }
            visited.add(current.getTo());

            List<Exchange> neighbors = graph.getOrDefault(current.getTo(), Collections.emptyList());
            for (Exchange neighbor : neighbors) {
                if(!visited.contains(neighbor.getTo())) {
                    queue.add(new Exchange(from, neighbor.getTo(), current.getRate() * neighbor.getRate()));
                }
            }
        }
        return null;
    }

    public double convertCurrency(double amount, String from, String to) {
        if (from.equals(to)) {
            return amount;
        }

        Exchange exchange = findExchange(from, to);
        if (exchange != null) {
            return amount * exchange.getRate();
        }

        throw new IllegalArgumentException("No exchange rate found for " + from + " to " + to);
    }
}
