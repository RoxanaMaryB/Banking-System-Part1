package org.poo.bank;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public final class CurrencyConverter {
    private static CurrencyConverter instance;
    private final List<Exchange> exchangeRates;
    private final Map<String, List<Exchange>> graph;

    private CurrencyConverter(final List<Exchange> exchangeRates) {
        this.exchangeRates = exchangeRates;
        this.graph = buildGraph();
    }

    /**
     * Singleton pattern
     * @param exchangeRates
     * @return
     */
    public static CurrencyConverter getInstance(final List<Exchange> exchangeRates) {
        if (instance == null) {
            instance = new CurrencyConverter(exchangeRates);
        }
        return instance;
    }

    /**
     * Reset the instance after each test
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Build a directed graph from the exchange rates, only in the beginning
     * @return
     */
    private Map<String, List<Exchange>> buildGraph() {
        Map<String, List<Exchange>> exchangeGraph = new HashMap<>();
        for (Exchange exchange : exchangeRates) {
            if (!exchangeGraph.containsKey(exchange.getFrom())) {
                exchangeGraph.put(exchange.getFrom(), new ArrayList<>());
            }
            exchangeGraph.get(exchange.getFrom()).add(exchange);

            if (!exchangeGraph.containsKey(exchange.getTo())) {
                exchangeGraph.put(exchange.getTo(), new ArrayList<>());
            }
            exchangeGraph.get(exchange.getTo()).add(new Exchange(exchange.getTo(),
                    exchange.getFrom(), 1.0 / exchange.getRate()));
        }
        return exchangeGraph;
    }

    /**
     * Find the exchange rate from one currency to another with BFS
     * @param from
     * @param to
     * @return
     */
    public Exchange findExchange(final String from, final String to) {
        Queue<Exchange> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(new Exchange(from, from, 1.0));

        while (!queue.isEmpty()) {
            Exchange current = queue.poll(); // front of the queue
            if (current.getTo().equals(to)) {
                return new Exchange(from, to, current.getRate());
            }
            visited.add(current.getTo());

            List<Exchange> neighbors = graph.getOrDefault(current.getTo(),
                    Collections.emptyList());
            for (Exchange neighbor : neighbors) {
                if (!visited.contains(neighbor.getTo())) {
                    queue.add(new Exchange(from, neighbor.getTo(),
                            current.getRate() * neighbor.getRate()));
                }
            }
        }
        return null;
    }

    /**
     * Convert an amount from one currency to another
     * @param amount
     * @param from
     * @param to
     * @return
     */
    public double convertCurrency(final double amount, final String from, final String to) {
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
