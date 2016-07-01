/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.finebi.datasource.sql.criteria.internal.metamodel;

import com.finebi.datasource.api.metamodel.Attribute;
import com.finebi.datasource.api.metamodel.ManagedType;

import java.io.Serializable;
import java.lang.reflect.Member;

/**
 * Models the commonality of the JPA {@link Attribute} hierarchy.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractAttribute<X, Y>
		implements Attribute<X, Y>, AttributeImplementor<X,Y>, Serializable {
	private final String name;
	private final Class<Y> javaType;
	private final AbstractManagedType<X> declaringType;
	private transient Member member;
	private final PersistentAttributeType persistentAttributeType;

	public AbstractAttribute(
			String name,
			Class<Y> javaType,
			AbstractManagedType<X> declaringType,
			Member member,
			PersistentAttributeType persistentAttributeType) {
		this.name = name;
		this.javaType = javaType;
		this.declaringType = declaringType;
		this.member = member;
		this.persistentAttributeType = persistentAttributeType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ManagedType<X> getDeclaringType() {
		return declaringType;
	}

	@Override
	public Class<Y> getJavaType() {
		return javaType;
	}

	@Override
	public Member getJavaMember() {
		return member;
	}

	@Override
	public PersistentAttributeType getPersistentAttributeType() {
		return persistentAttributeType;
	}


}
