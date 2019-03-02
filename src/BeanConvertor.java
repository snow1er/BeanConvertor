import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixinyao on 2019/3/2.
 */
public class BeanConvertor {

    enum ConvertRule {

        NORULE {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                targetField.set(targetObj, sourceField.get(sourceObj));
            }
        },

        INT_TO_ENUM {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                int fieldValue = (Integer) sourceField.get(sourceObj);
                int enumSize = targetField.getType().getEnumConstants().length;
                if (fieldValue >= enumSize || fieldValue < 0)
                    throw new RuntimeException("invalid Enum ordinal : " + fieldValue + " for " + targetField.getType().getName());
                Object en = targetField.getType().getEnumConstants()[fieldValue];
                targetField.set(targetObj, en);
            }
        },
        ENUM_TO_INT {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                int fieldValue = -1;
                for (int i = 0; i < sourceField.getType().getEnumConstants().length; i++) {
                    if (sourceField.getType().getEnumConstants()[i] == sourceField.get(sourceObj)) {
                        fieldValue = i;
                        break;
                    }
                }
                if (fieldValue < 0) {
                    throw new RuntimeException("invalid Enum ordinal : " + fieldValue + " for " + sourceField.getType().getName());
                }
                targetField.setInt(targetObj, fieldValue);
            }
        },
        INT_TO_BOOLEAN {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                int fieldValue = (Integer) sourceField.get(sourceObj);
                targetField.setBoolean(targetObj, fieldValue == 1 ? true : false);
            }
        },
        BOOLEAN_TO_INT {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                boolean fieldValue = (Boolean) sourceField.get(sourceObj);
                targetField.setInt(targetObj, fieldValue ? 1 : 0);
            }
        },
        STRING_TO_INT {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                String fieldValue = (String) sourceField.get(sourceObj);
                targetField.setInt(targetObj, Integer.valueOf(fieldValue));
            }
        },
        INT_TO_STRING {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                int fieldValue = (Integer) sourceField.get(sourceObj);
                targetField.set(targetObj, String.valueOf(fieldValue));
            }
        },
        NUMBER_TO_STRING {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                targetField.set(targetObj, String.valueOf(sourceField.get(sourceObj)));
            }
        },
        OFFSETDATETIME_TO_STRING {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                OffsetDateTime fieldValue = (OffsetDateTime) sourceField.get(sourceObj);
                targetField.set(targetObj, fieldValue.toString());
            }
        },
        DATE_TO_STRING {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                Date fieldValue = (Date) sourceField.get(sourceObj);
                targetField.set(targetObj, fieldValue.toString());
            }
        },
        OBJECT_TO_STRING {
            @Override
            void doConvert(Object sourceObj, Object targetObj,
                           Field sourceField, Field targetField) throws Exception {
                targetField.set(targetObj, sourceField.get(sourceObj).toString());
            }
        };

        abstract void doConvert(Object sourceObj, Object targetObj, Field sourceField, Field targetField) throws Exception;
    }

    /**
     * Generate target object by target class, then Convert from sourceObj to targetObj
     *
     * @param sourceObj
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T convert(Object sourceObj, Class<T> targetClass) {
        if (targetClass == null) {
            return null;
        }
        T targetObj = null;
        try {
            targetObj = targetClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (targetObj == null) {
            throw new RuntimeException("创建target对象失败。target ： " + targetClass.getName());
        }
        convert(sourceObj, targetObj);
        return targetObj;
    }

    /**
     * Convert from sourceObj to targetObj
     * 忽略静态变量
     * todo 我们可以定义一些annotation来处理一些特殊规则，比如ignore一些属性
     *
     * @param sourceObj
     * @param targetObj
     */
    public static void convert(Object sourceObj, Object targetObj) {
        if (sourceObj == null || targetObj == null)
            return;
        Map<String, Field> targetFieldMap = getTargetFieldMap(targetObj);

        Class<?> sourceClass = sourceObj.getClass();
        while (sourceClass != null) {
            Field[] fields = sourceClass.getDeclaredFields();
            for (Field sourceField : fields) {
                String fieldName = sourceField.getName();
                if (!targetFieldMap.containsKey(fieldName)) {
                    continue;
                }
                Field targetField = targetFieldMap.get(fieldName);
                // skip static variable
                if ((targetField.getModifiers() & Modifier.STATIC) != 0)
                    continue;

                doConvert(sourceObj, targetObj, sourceField, targetField);

                targetFieldMap.remove(fieldName);

                if (targetFieldMap.isEmpty())
                    return;
            }
            sourceClass = sourceClass.getSuperclass();
        }
    }

    /**
     * Integer and Enum will convert according to EnumConstants's index
     * Integer can convert to Long, but Long can not convert to Integer
     * Boolean and Integer will convert according to (TRUE == 1; FALSE != 1)
     *
     * @param sourceObj
     * @param targetObj
     * @param sourceField
     * @param targetField
     */
    private static void doConvert(Object sourceObj, Object targetObj, Field sourceField, Field targetField) {
        sourceField.setAccessible(true);
        targetField.setAccessible(true);
        try {
            ConvertRule rule = ConvertRule.NORULE;
            if (!sourceField.getType().equals(targetField.getType())) {
                if (targetField.getType().isEnum() && sourceField.get(sourceObj) instanceof Integer) {
                    rule = ConvertRule.INT_TO_ENUM;
                } else if (targetField.get(targetObj) instanceof Integer && sourceField.getType().isEnum()) {
                    rule = ConvertRule.ENUM_TO_INT;
                } else if (targetField.get(targetObj) instanceof Boolean && sourceField.get(sourceObj) instanceof Integer) {
                    rule = ConvertRule.INT_TO_BOOLEAN;
                } else if (targetField.get(targetObj) instanceof Integer && sourceField.get(sourceObj) instanceof Boolean) {
                    rule = ConvertRule.BOOLEAN_TO_INT;
                } else if (targetField.get(targetObj) instanceof Integer && sourceField.getType().getName().equals("java.lang.String")) {
                    rule = ConvertRule.STRING_TO_INT;
                } else if (targetField.getType().getName().equals("java.lang.String") && sourceField.get(sourceObj) instanceof Integer) {
                    rule = ConvertRule.INT_TO_STRING;
                } else if (targetField.getType().getName().equals("java.lang.String") && sourceField.get(sourceObj) instanceof Number) {
                    rule = ConvertRule.NUMBER_TO_STRING;
                } else if (targetField.getType().getName().equals("java.lang.String")) {
                    rule = ConvertRule.OBJECT_TO_STRING;
                }
            }
            rule.doConvert(sourceObj, targetObj, sourceField, targetField);
        } catch (Exception e) {
            System.out.println("convert setField failed! sourceFieldName : " + sourceField.getName() + ", error msg : " + e.getMessage());
        }
    }

    private static Map<String, Field> getTargetFieldMap(Object targetObj) {
        Class<?> targetClass = targetObj.getClass();
        Map<String, Field> targetFieldMap = new HashMap<>();
        while (targetClass != null) {
            Field[] fields = targetClass.getDeclaredFields();
            for (Field targetField : fields) {
                String targetFieldName = targetField.getName();
                if (targetFieldName == null || targetFieldName.isEmpty()) {
                    throw new RuntimeException("Invalid annotation DaoConvertField, fieldName is null! Class : " + targetClass.getName() + ", targetFieldName : " + targetField.getName());
                }
                if (targetFieldMap.containsKey(targetFieldName)) {
                    System.out.println("getTargetFieldMap, SuperClass have the same Field with SubClass, Only deal with SubClass's Field! "
                        + "Class : " + targetClass.getName() + ", targetFieldName : " + targetField.getName());
                    continue;
                }
                targetFieldMap.put(targetFieldName, targetField);
            }
            targetClass = targetClass.getSuperclass();
        }
        return targetFieldMap;
    }
}
