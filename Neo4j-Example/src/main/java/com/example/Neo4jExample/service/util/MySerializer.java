package com.example.Neo4jExample.service.util;

import java.lang.reflect.Type;

/**
 * Interface for serialize Objects
 * @param <T>
 */
public interface MySerializer<T extends Object> {
    String serialize(Object obj);
    T deserialize(String json,Class<T> classTo);

    T deserialize(String json, Type type);
}
