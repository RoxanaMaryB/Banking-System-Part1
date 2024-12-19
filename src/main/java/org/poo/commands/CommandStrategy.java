package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public interface CommandStrategy {
    /**
     * Execute the command depending on the input
     * @param output
     * @param objectMapper
     */
    void execute(ArrayNode output, ObjectMapper objectMapper);
}
