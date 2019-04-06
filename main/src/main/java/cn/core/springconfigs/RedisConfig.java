package cn.core.springconfigs;

import cn.core.caches.CacheNames;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis 配置
 *
 * @author 肖文杰  https://github.com/xwjie
 */
//@Configuration
//@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();

                sb.append(target.getClass().getSimpleName());
                sb.append('.').append(method.getName());

                // FIXME 参数太长的时候请指定key属性，否则key太长
                for (Object obj : params) {
                    if (obj != null) {
                        sb.append(obj.toString());
                    }
                }

                return sb.toString();
            }
        };
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();

        template.setConnectionFactory(factory);

        RedisSerializer keySerializer = new StringRedisSerializer();

        // 设置key序列化类，否则key前面会多了一些乱码
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        // FIXME 有些版本有bug，没有序列化的只能序列化，但无法反序列化
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        // 设置内容序列化类
        template.setValueSerializer(jsonSerializer);

        template.afterPropertiesSet();

        return template;
    }
}
