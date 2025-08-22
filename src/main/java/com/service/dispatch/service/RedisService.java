package com.service.dispatch.service;

public interface RedisService {

    <T> void setValue(final String key, T data);

    <T> void setValue(final String key, T data, int expireDuration);

    <T> T getValue(final String key, Class<T> valueType);

    void deleteByKey(final String key);
}
