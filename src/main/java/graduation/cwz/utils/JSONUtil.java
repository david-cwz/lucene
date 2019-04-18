package graduation.cwz.utils;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * Json工具类
 *
 */
public class JSONUtil {
    /**
     * 字符串转换为实体类
     *
     */
    public static <T> T StringToEntity(String str, Class<T> classT) {
        T t = JSON.parseObject(str, classT);
        return t;
    }

    /**
     * 字符串转换为实体集合
     *
     */
    public static <T> ArrayList<T> StringToEntityList(String str, Class<T> classT) {
        List<T> lst = JSON.parseArray(str, classT);
        return (ArrayList<T>) lst;
    }

    /**
     * 实体类转换为字符串
     *
     */
    public static <T> String EntityToString(T data) {
        String str = JSON.toJSONString(data);
        return str;
    }
}
