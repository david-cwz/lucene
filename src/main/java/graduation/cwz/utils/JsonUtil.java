/*
* ------------------------------------------------------------------
* Copyright © 2017 Hangzhou DtDream Technology Co.,Lt d. All rights reserved.
* ------------------------------------------------------------------
*       Product: StorageController
*   Module Name: EBS
*  Date Created: 2017-10-31
*   Description:
* ------------------------------------------------------------------
* Modification History
* DATE            Name           Description
* ------------------------------------------------------------------
* 2017-10-31      zzj 0638
* ------------------------------------------------------------------
*/
package graduation.cwz.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zzj on 17-2-6.
 */
public class JsonUtil {

    private static Logger logger = LogManager.getLogger(JsonUtil.class);

    public static String getJsonStr(Object object){
        String result = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    public static  Map<String,String> getJsonMap(String jsonStr){
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> map = null;
        try {
            map = mapper.readValue(jsonStr,Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static  Map<String,String> getJsonMap(Object object){
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> map = null;
        try {
            map = mapper.readValue(mapper.writeValueAsString(object),Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}
