package com.service.dispatch.service.impls;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.dispatch.service.RedisService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private final String EROR_CONVERTING_MSG = "Error while converting JSON";

    /**
     * <T> nằm phía trước một phương thức, method declaration là định nghĩa kiểu tổng quá của một
     * method <T>Generic type, cho phép hoạt động với tất cả các kiểu dữ liệu nào, chấp nhận và xử lý
     * bất kỳ kiểu dữ liệu nào T là kiểu trả về cho phép trả về bất cứ kiểu dữ liệu nào <T> giúp bạn
     * viết một phương thức chung, có thể hoạt động với mọi kiểu dữ liệu, giúp cắt giảm code lặp lại
     * và tăng tính linh hoạt của chương trình.
     */
    @Override
    public <T> T getValue(
            String key,
            Class<T> valueType) { // String key là tham số truyền vào là khóa để lấy dữ liệu từ Redis
        // Class<T> valueType là lớp (class) của kiểu dữ liệu mà bạn mong muốn chuyển đổi JSON thành.
        T t = null;
        try {
            var dataJson =
                    redisTemplate
                            .opsForValue()
                            .get(key); // Khởi tạo biến dataJson để hứng data từ redisTemplate trả về, lấy ra json
            // theo value là key
            if (StringUtils.isNotEmpty(
                    dataJson)) { // Kiểm tra xem dataJson có không rỗng hoặc null không.
                // StringUtils là một tiện ít utils của unity class trong java cung cấp các method làm việc
                // với String.

                t =
                        objectMapper.readValue(
                                dataJson,
                                valueType); // Dùng objectMapper của JackSon để chuyển đổi chuỗi JSON của dataJson
                // thành objec java có kiểu là T
            }
        } catch (
                JsonProcessingException
                        e) { // Bắt lỗi nếu JSON không hợp lệ hoặc xảy ra lỗi trong quá trình chuyển đổi.
            log.error(EROR_CONVERTING_MSG, e); // ghi log
        }
        return t;
    }

    @Override
    public void deleteByKey(@NotNull String key) {
        redisTemplate.delete(key);
    }

    @Override
    public <T> void setValue(String key, T data) {
        try {
            String dataJson = objectMapper.writeValueAsString(data);

            redisTemplate.opsForValue().set(key, dataJson);
        } catch (JsonProcessingException e) {
            log.error(EROR_CONVERTING_MSG, e);
        }
    }

    @Override
    public <T> void setValue(String key, T data, int expireDuration) {
        try {
            String dataJson =
                    objectMapper.writeValueAsString(
                            data); // writeValueAsString nhận vào một object và trả ra một chuỗi JSON

            redisTemplate.opsForValue().set(key, dataJson, expireDuration, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error(EROR_CONVERTING_MSG, e);
        }
    }
}

