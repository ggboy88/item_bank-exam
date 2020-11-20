package com.ggboy.exam.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisUtils {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * @param key
     * @return 获得值
     * redis有五种数据类型 opsForValue表示是操作字符串类型
     */
    public Object get(String key){
        return  key == null ? null : redisTemplate.opsForValue().get(key);
    }
    //本来只可以放入string类型，但是我配置了自动序列化所以这儿可以传入object
    public boolean set(String key,Object value){
        try{
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            log.error("redis set value exception:{}",e);
            return false;
        }
    }

    /**
     * 原子操作
     * @param key
     * @param value
     * @param expire 过期时间 秒
     * @return
     */
    public boolean setex(String key,Object value,long expire){
        try{//TimeUnit.SECONDS指定类型为秒
            redisTemplate.opsForValue().set(key,value,expire, TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            log.error("redis set value and expire exception:{}",e);
            return false;
        }
    }

    /**
     * 非原子操作
     * @param key
     * @param expire
     * @return
     */
    public boolean expire(String key,long expire){
        try{//这儿没有ops什么的是因为每种数据类型都能设置过期时间
            redisTemplate.expire(key,expire,TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            log.error("redis set key expire exception:{}",e);
            return false;
        }
    }

    /**
     *
     * @param key
     * @return 获取key的过期时间
     */
    public long ttl(String key){
        return redisTemplate.getExpire(key);
    }

    /**
     *
     * @param keys 删除key 可变参数
     */
    public void del(String ...keys){
        if(keys!=null&&keys.length>0) {
            redisTemplate.delete(CollectionUtils.arrayToList(keys));
        }
    }

    /**
     *
     * @param key
     * @param step  传入正数 就是加多少 传入负数就是减多少
     * @return
     */
    public long incrBy(String key,long step){
        return redisTemplate.opsForValue().increment(key,step);
    }

    /**
     *
     * @param key
     * @param value
     * @return  如果该key存在就返回false 设置不成功 key不存在就返回ture设置成功
     */
    public boolean setnx(String key,Object value){
        return redisTemplate.opsForValue().setIfAbsent(key,value);
    }

    /**
     *  原子操作
     * @param key
     * @param value
     * @param expire  在上面方法加上过期时间设置
     * @return
     */
    public boolean setnxAndExpire(String key,Object value,long expire){
        return redisTemplate.opsForValue().setIfAbsent(key,value,expire,TimeUnit.SECONDS);
    }

    /**
     *
     * @param key
     * @param value
     * @return  如果该key存在就返回之前的value  不存在就返回null
     */
    public Object getAndSet(String key,Object value){
        return redisTemplate.opsForValue().getAndSet(key,value);
    }

    /**
     *
     * @param key
     * @return 判断key是否存在
     */
    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /***list的长度**/
    public long llen(String key){
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取key中index位置的值，负数就反过来数，-1为最后一个
     * @param key
     * @param index
     * @return
     */
    public Object lgetByIndex(String key,long index){
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("redis lgetByIndex error,key:{},index:{}exception:{}",key,index,e);
            return null;
        }
    }
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lrpush(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis lrpush error,key:{},value:{}exception:{}",key,value,e);
            return false;
        }
    }
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lrpush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            log.error("redis lrpush error,key:{},value:{},timeL{},exception:{}",key,value,time,e);
            return false;
        }
    }
    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lrpush(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis lrpush error,key:{},value:{},exception:{}",key,value,e);
            return false;
        }
    }
    /**
     * 将list放入缓存
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lrpush(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            log.error("redis lrpush error,key:{},value:{},time:{},exception:{}",key,value,time,e);
            return false;
        }
    }
    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateByIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("redis lUpdateByIndex error,key:{},index:{},value:{},exception:{}",key,index,value,e);
            return false;
        }
    }
    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lrem(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error("redis lrem error,key:{},count:{},value:{},exception:{}",key,count,value,e);
            return 0;
        }
    }

}
