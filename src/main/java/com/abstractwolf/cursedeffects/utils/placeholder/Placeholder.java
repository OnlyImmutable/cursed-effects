package com.abstractwolf.cursedeffects.utils.placeholder;

public class Placeholder {

    private String placeholder;
    private Object value;

    public Placeholder(String placeholder, Object value) {
        this.placeholder = placeholder;
        this.value = value;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public Object getValue() {
        return value;
    }
}
