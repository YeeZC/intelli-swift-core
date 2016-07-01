package com.finebi.datasource.sql.criteria.internal.metamodel;

/**
 * This class created on 2016/7/1.
 *
 * @author Connery
 * @since 4.0
 */
public interface PersistentClass {

    String getEntityName();

    Object getDeclaredIdentifierMapper();

    boolean hasIdentifierProperty();

    boolean isVersioned();

    String getJpaEntityName();
}
