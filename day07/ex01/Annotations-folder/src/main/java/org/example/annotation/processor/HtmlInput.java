package org.example.annotation.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * <pre> example:
 * -@HtmlInput(type = "text", name = "first_name", placeholder = "Enter First Name")</pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface HtmlInput {
    String type();

    String name();

    String placeholder() default "";
}
