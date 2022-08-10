package com.example.Neo4jExample.service.util;

import com.google.gson.Gson;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * Class to serialize and deserialize an Object to Json format and vice versa
 * @param <T> Type of element deserialized.
 */
@Data
@RequiredArgsConstructor
@Service
public class MyGsonSerializer<T extends Object> implements MySerializer<T> {
    private final Gson gson;

    /**
     * return a serialized string from a simple object
     * @param obj to serialize
     * @return string of object
     */
    @Override
    public String serialize(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * deserialize a string to object
     * @param json string to deserialize in an object
     * @param classTo class of object to deserialize
     * @return object deserialized
     */
    @Override
    public T deserialize(String json,Class<T> classTo) {
        return gson.fromJson(json, classTo);
    }

    /**
     * deserialize a string to object
     * @param json string to deserialize in an object
     * @param type of object to deserialize
     * @return object deserialized
     */
    @Override
    public T deserialize(String json, Type type) {
        return gson.fromJson(json, type);
    }
}
