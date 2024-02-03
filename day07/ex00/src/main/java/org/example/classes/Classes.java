package org.example.classes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Classes {
    public static List<? extends Constructor<?>> getDefaultConstructors(Collection<Class<?>> classes)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return classes.stream()
                .map(_class ->
                        Arrays.stream(_class.getConstructors())
                                .filter(constructor -> constructor.getParameterCount() == 0)
                                .limit(1)
                                .toList()
                                .getFirst()
                )
                .toList();
    }
}

//        for (var constructor : defaultConstructors) {
//            Object object = constructor.newInstance();
//
//            System.out.println(object);
//            System.out.println("fields:");
//            Arrays.stream(object.getClass().getDeclaredFields())
//                    .map(Field::getName)
//                    .map(n -> "- " + n)
//                    .forEach(System.out::println);
//            Arrays.stream(object.getClass().getDeclaredFields())
//                    .map(Field::getType)
//                    .map(n -> "- " + n)
//                    .forEach(System.out::println);
//            System.out.println("methods:");
//            Arrays.stream(object.getClass().getDeclaredMethods())
//                    .map(Method::getName)
//                    .map(n -> "- " + n)
//                    .forEach(System.out::println);
//            System.out.println("***************");
//        }
//

//        Map<String, Object> objects = classes
//                .stream()
//                .map(Class::newInstance)
//                .collect(Collectors.toMap(Class::getName));
//
//        System.out.println(User.class);
