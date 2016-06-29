/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.finebi.datasource.sql.criteria.internal.path;

import com.finebi.datasource.sql.criteria.CriteriaBuilderImpl;
import com.finebi.datasource.sql.criteria.internal.*;
import com.finebi.datasource.sql.criteria.internal.compile.RenderingContext;
import com.finebi.datasource.sql.criteria.internal.expression.ListIndexExpression;

import com.finebi.datasource.api.criteria.Expression;
import com.finebi.datasource.api.criteria.JoinType;
import com.finebi.datasource.api.criteria.Predicate;
import com.finebi.datasource.api.metamodel.ListAttribute;
import java.io.Serializable;
import java.util.List;

/**
 * Models a join based on a list-style plural association attribute.
 *
 * @author Steve Ebersole
 */
public class ListAttributeJoin<O,E>
		extends PluralAttributeJoinSupport<O, List<E>,E>
		implements ListJoinImplementor<O,E>, Serializable {

	public ListAttributeJoin(
			CriteriaBuilderImpl criteriaBuilder,
			Class<E> javaType,
			PathSource<O> pathSource,
			ListAttribute<? super O, E> joinAttribute,
			JoinType joinType) {
		super( criteriaBuilder, javaType, pathSource, joinAttribute, joinType );
	}

	@Override
	public Expression<Integer> index() {
		return new ListIndexExpression( criteriaBuilder(), this, getAttribute() );
	}

	@Override
	public ListAttribute<? super O, E> getAttribute() {
		return (ListAttribute<? super O, E>) super.getAttribute();
	}

	@Override
	public ListAttribute<? super O, E> getModel() {
		return getAttribute();
	}

	@Override
	public final ListAttributeJoin<O,E> correlateTo(CriteriaSubqueryImpl subquery) {
		return (ListAttributeJoin<O,E>) super.correlateTo( subquery );
	}

	@Override
	protected FromImplementor<O, E> createCorrelationDelegate() {
		return new ListAttributeJoin<O,E>(
				criteriaBuilder(),
				getJavaType(),
				(PathImplementor<O>) getParentPath(),
				getAttribute(),
				getJoinType()
		);
	}

	@Override
	public ListAttributeJoin<O, E> on(Predicate... restrictions) {
		return (ListAttributeJoin<O, E>) super.on( restrictions );
	}

	@Override
	public ListAttributeJoin<O, E> on(Expression<Boolean> restriction) {
		return (ListAttributeJoin<O, E>) super.on( restriction );
	}

	@Override
	public <T extends E> ListAttributeJoin<O,T> treatAs(Class<T> treatAsType) {
		return new TreatedListAttributeJoin<O,T>( this, treatAsType );
	}

	public static class TreatedListAttributeJoin<O,T> extends ListAttributeJoin<O, T> {
		private final ListAttributeJoin<O, ? super T> original;
		private final Class<T> treatAsType;

		@SuppressWarnings("unchecked")
		public TreatedListAttributeJoin(ListAttributeJoin<O, ? super T> original, Class<T> treatAsType) {
			super(
					original.criteriaBuilder(),
					treatAsType,
					original.getPathSource(),
					(ListAttribute<? super O, T>) original.getAttribute(),
					original.getJoinType()
			);
			this.original = original;
			this.treatAsType = treatAsType;
		}

		@Override
		public String getAlias() {
			return original.getAlias();
		}

		@Override
		public void prepareAlias(RenderingContext renderingContext) {
			// do nothing...
		}

		@Override
		public String render(RenderingContext renderingContext) {
			return "treat(" + original.render( renderingContext ) + " as " + treatAsType.getName() + ")";
		}

		@Override
		public String getPathIdentifier() {
			return "treat(" + getAlias() + " as " + treatAsType.getName() + ")";
		}

		@Override
		protected PathSource getPathSourceForSubPaths() {
			return this;
		}
	}
}
