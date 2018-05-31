package com.abstractwolf.cursedeffects.utils;

public interface Callback<T> {

    /**
     * Call back a value later.
     * @param object - object returned.
     */
    void call(T object);
}
