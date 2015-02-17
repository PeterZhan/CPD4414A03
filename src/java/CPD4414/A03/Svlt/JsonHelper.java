package CPD4414.A03.Svlt;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * 1:��JavaBeanת����Map��JSONObject
 * 2:��Mapת����Javabean
 * 3:��JSONObjectת����Map��Javabean
 * 
 * @author Alexia
 */

public class JsonHelper {
    
    /**
     * ��Javabeanת��ΪMap
     * 
     * @param javaBean
     *            javaBean
     * @return Map����
     */
    public static Map toMap(Object javaBean) {

        Map result = new HashMap();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {

            try {

                if (method.getName().startsWith("get")) {

                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;

    }

    /**
     * ��Json����ת����Map
     * 
     * @param jsonObject
     *            json����
     * @return Map����
     * @throws JSONException
     */
    public static Map toMap(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);
        
        Map result = new HashMap();
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;
        
        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        return result;

    }

    /**
     * ��JavaBeanת����JSONObject��ͨ��Map��ת��
     * 
     * @param bean
     *            javaBean
     * @return json����
     */
    public static JSONObject toJSON(Object bean) {

        return new JSONObject(toMap(bean));

    }

    /**
     * ��Mapת����Javabean
     * 
     * @param javabean
     *            javaBean
     * @param data
     *            Map���
     */
    public static Object toJavaBean(Object javabean, Map data) {

        Method[] methods = javabean.getClass().getDeclaredMethods();
        for (Method method : methods) {

            try {
                if (method.getName().startsWith("set")) {

                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    method.invoke(javabean, new Object[] {

                    data.get(field)

                    });

                }
            } catch (Exception e) {
            }

        }

        return javabean;

    }

    /**
     * JSONObject��JavaBean
     * 
     * @param bean
     *            javaBean
     * @return json����
     * @throws ParseException
     *             json�����쳣
     * @throws JSONException
     */
    public static void toJavaBean(Object javabean, String jsonString)
            throws ParseException, JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);
    
        Map map = toMap(jsonObject.toString());
        
        toJavaBean(javabean, map);

    }

}