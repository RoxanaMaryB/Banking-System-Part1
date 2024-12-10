package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public interface CommandStrategy {
    void execute(ArrayNode output, ObjectMapper objectMapper);
}
