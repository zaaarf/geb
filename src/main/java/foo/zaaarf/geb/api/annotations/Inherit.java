package foo.zaaarf.geb.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tells the processor that this class is also looking to inherit one or more
 * listeners from its parent class(es).
 * @since 0.5.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Inherit {}
