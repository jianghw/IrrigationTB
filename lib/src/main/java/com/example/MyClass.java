package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Field;

public class MyClass {

    public static void main(String[] args) throws Exception {
        test6();
    }

    private static void test6() {
        System.out.println(15 & (16-1));
    }

    private static void test5() {
        StringBuilder stringBuilder=new StringBuilder();
        System.out.println(stringBuilder.toString());
    }

    private static void test4() {
        for (int i = 0; i < 50; i++) {
            System.out.println(i / 7);
            System.out.println(i % 7);
        }
    }

    private static void test3() {

        ReflectClass reflectClass = new ReflectClass();
        reflectClass.setAgeA(100);
        reflectClass.setFloat(200f);

        System.out.println(new Gson().toJson(reflectClass));
        String json = new Gson().toJson(reflectClass);
        //        JsonElement element = new JsonParser().parse(json);
        JsonElement element = new Gson().toJsonTree(json);

        if (element.isJsonObject()) {
            if (element.getAsJsonObject().has("name_py")) {
                System.out.println("A");
            }
        } else if (element.isJsonArray()) {
            System.out.println("B");
        } else if (element.isJsonNull()) {
            System.out.println("C");
        } else if (element.isJsonPrimitive()) {
            System.out.println(element.getAsJsonPrimitive().isJsonObject());
            System.out.println(element.getAsJsonPrimitive().isJsonArray());
            System.out.println("D");
        } else {
            System.out.println("E");
        }
    }

    private static void test2() {
        try {
            Class<?> clazz = Class.forName("com.example.ReflectClass");
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                System.out.println("field.getName()=" + field.getName() + "---" + field.getType() + "---"
                        + field.getType().getName() + "---" + field.getType().getSimpleName());
            }

            System.out.println("--------------------------------------------------------------");
            Field[] fieldsD = clazz.getDeclaredFields();
            for (Field field : fieldsD) {
                System.out.println("field.getName()=" + field.getName() + "---" + field.getType() + "---"
                        + field.getType().getName() + "---" + field.getType().getSimpleName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void test1() {
        String day = "04";
        if (day.startsWith("0")) day = day.substring(1, day.length());
        System.out.println(day);
    }
}
