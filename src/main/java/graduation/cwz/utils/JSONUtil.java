package graduation.cwz.utils;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * Json工具类
 *
 */
public class JSONUtil {
    /**
     * 字符串转换为实体类
     *
     */
    public static <T> T stringToEntity(String str, Class<T> classT) {
        T t = JSON.parseObject(str, classT);
        return t;
    }

    /**
     * 字符串转换为实体集合
     *
     */
    public static <T> ArrayList<T> stringToEntityList(String str, Class<T> classT) {
        List<T> lst = JSON.parseArray(str, classT);
        return (ArrayList<T>) lst;
    }

    /**
     * 实体类转换为字符串
     *
     */
    public static <T> String entityToString(T data) {
        String str = JSON.toJSONString(data);
        return str;
    }

    /**
     * List集合转换成json字符串
     * @param obj
     * @return
     */
    public static String listToJson(Object obj){
        return JSONArray.toJSONString(obj, true);
    }
}
