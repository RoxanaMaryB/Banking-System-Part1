package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class NoCommand implements CommandStrategy {
    @Override
    public void execute(final ArrayNode output, final ObjectMapper objectMapper) {
        // No action
    }
}
