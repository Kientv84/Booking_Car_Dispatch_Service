package com.service.dispatch.service;

import com.fasterxml.jackson.core.type.TypeReference;

public interface RedisService {

    <T> void setValue(final String key, T data);

    <T> T getValue(String key, TypeReference<T> typeRef);

    <T> void setValue(final String key, T data, int expireDuration);

    <T> T getValue(final String key, Class<T> valueType);

    void deleteByKey(final String key);
}
