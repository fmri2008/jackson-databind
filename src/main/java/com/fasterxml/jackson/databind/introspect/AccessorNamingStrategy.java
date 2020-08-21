package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.cfg.MapperConfig;

/**
 * API for handlers used to "mangle" names of "getter" and "setter" methods to
 * find implicit property names.
 *
 * @since 2.12
 */
public abstract class AccessorNamingStrategy
{
    /**
     * Method called to find whether given method would be considered an "is-getter"
     * method in context of type introspected.
     *<p>
     * Note that signature acceptability has already been checked (no arguments,
     * has return value) but NOT the specific limitation that return type should
     * be of boolean type -- implementation should apply latter check, if so desired
     * (some languages may use different criteria).
     */
    public abstract String findNameForIsGetter(AnnotatedMethod am, String name);

    /**
     * Method called to find whether given method would be considered a "regular"
     * getter method in context of type introspected.
     *<p>
     * Note that signature acceptability has already been checked (no arguments,
     * does have a return value) by caller.
     *<p>
     * Note that this method MAY be called for potential "is-getter" methods too
     * (before {@link #findNameForIsGetter})
     */
    public abstract String findNameForRegularGetter(AnnotatedMethod am, String name);

    /**
     * Method called to find whether given method would be considered a "mutator"
     * (usually setter, but for builders "with-method" or similar) in context of
     * type introspected.
     *<p>
     * Note that signature acceptability has already been checked (exactly one parameter)
     * by caller.
     */
    public abstract String findNameForMutator(AnnotatedMethod am, String name);

    /**
     * Interface for provider (factory) for constructing {@link AccessorNamingStrategy}
     * for given type of deserialization target
     */
    public abstract static class Provider
        implements java.io.Serializable // since one configured with Mapper/MapperBuilder
    {
        private static final long serialVersionUID = 1L;

        /**
         * Factory method for creating strategy instance for a "regular" POJO,
         * called if none of the other factory methods is applicable.
         *
         * @param config Current mapper configuration
         * @param valueClass Information about value type
         *
         * @return Naming strategy instance to use
         */
        public abstract AccessorNamingStrategy forPOJO(MapperConfig<?> config,
                AnnotatedClass valueClass);

        /**
         * Factory method for creating strategy instance for POJOs
         * that are deserialized using Builder type: in this case eventual
         * target (value) type is different from type of "builder" object that is
         * used by databinding to accumulate state.
         *
         * @param config Current mapper configuration
         * @param builderClass Information about builder type
         * @param valueTypeDesc Information about the eventual target (value) type
         *
         * @return Naming strategy instance to use
         */
        public abstract AccessorNamingStrategy forBuilder(MapperConfig<?> config,
                AnnotatedClass builderClass, BeanDescription valueTypeDesc);

        /**
         * Factory method for creating strategy instance for special {@code java.lang.Record}
         * type (new in JDK 14).
         *
         * @param config Current mapper configuration
         * @param recordClass Information about value type (of type {@code java.lang.Record})
         *
         * @return Naming strategy instance to use
         */
        public abstract AccessorNamingStrategy forRecord(MapperConfig<?> config,
                AnnotatedClass recordClass);
    }
}