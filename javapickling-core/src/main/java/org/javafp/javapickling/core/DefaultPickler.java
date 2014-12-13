package org.javafp.javapickling.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation class to allow data classes to specify their default pickler.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultPickler {

    /**
     * The default pickler class for the referenced class.
     * @return
     */
    Class<? extends Pickler> value();
}
