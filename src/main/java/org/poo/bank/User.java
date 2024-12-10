package org.poo.bank;

import lombok.Data;
import org.poo.fileio.UserInput;

@Data
public class User {
    private String firstName;
    private String lastName;
    private String email;

    public User(UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
    }
}
