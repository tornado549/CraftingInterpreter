package lox;

import java.util.HashMap;
import java.util.Map;

class Environment {
    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();
    private final Map<String, Boolean> initialized = new HashMap<>();

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    void define(String name, Object value) {
        values.put(name, value);
        initialized.put(name, true);
    }

    void define(String name) {
        values.put(name, null);
        initialized.put(name, false);
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            if (initialized.get(name.lexeme))
                return values.get(name.lexeme);
            else
                throw new RuntimeError(name, "Access a variable that has not been initialized or assigned to: '" + name.lexeme + "'.");
        }

        if (enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            initialized.put(name.lexeme, true);
            values.put(name.lexeme, value);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }
}