package org.example.annotation.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * <pre> example:
 * -@HtmlForm(fileName = "user_form.html", action = "/users", method = "post") </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface HtmlForm {
    String fileName();

    String action();

    String method() default "get";
}
