/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.finebi.datasource.sql.criteria.internal.expression.function;

import java.io.Serializable;
import java.util.List;
import com.finebi.datasource.api.criteria.Expression;
import com.finebi.datasource.api.criteria.Root;

import com.finebi.datasource.sql.criteria.internal.CriteriaBuilderImpl;
import com.finebi.datasource.sql.criteria.internal.compile.RenderingContext;
import com.finebi.datasource.sql.criteria.internal.expression.LiteralExpression;

/**
 * Models SQL aggregation functions (<tt>MIN</tt>, <tt>MAX</tt>, <tt>COUNT</tt>, etc).
 *
 * @author Steve Ebersole
 */
public class AggregationFunction<T>
		extends ParameterizedFunctionExpression<T>
		implements Serializable {

	/**
	 * Constructs an aggregation function with a single literal argument.
	 *
	 * @param criteriaBuilder The query builder instance.
	 * @param returnType The function return type.
	 * @param functionName The name of the function.
	 * @param argument The literal argument
	 */
	@SuppressWarnings({ "unchecked" })
	public AggregationFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Class<T> returnType,
			String functionName,
			Object argument) {
		this( criteriaBuilder, returnType, functionName, new LiteralExpression( criteriaBuilder, argument ) );
	}

	/**
	 * Constructs an aggregation function with a single literal argument.
	 *
	 * @param criteriaBuilder The query builder instance.
	 * @param returnType The function return type.
	 * @param functionName The name of the function.
	 * @param argument The argument
	 */
	public AggregationFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Class<T> returnType,
			String functionName,
			Expression<?> argument) {
		super( criteriaBuilder, returnType, functionName, argument );
	}

	@Override
	public boolean isAggregation() {
		return true;
	}

	@Override
	protected boolean isStandardJpaFunction() {
		return true;
	}

	/**
	 * Implementation of a <tt>COUNT</tt> function providing convenience in construction.
	 * <p/>
	 * Parameterized as {@link Long} because thats what JPA states
	 * that the return from <tt>COUNT</tt> should be.
	 */
	public static class COUNT extends AggregationFunction<Long> {
		public static final String NAME = "count";

		private final boolean distinct;

		public COUNT(CriteriaBuilderImpl criteriaBuilder, Expression<?> expression, boolean distinct) {
			super( criteriaBuilder, Long.class, NAME , expression );
			this.distinct = distinct;
		}

		@Override
		protected void renderArguments(StringBuilder buffer, RenderingContext renderingContext) {
			if ( isDistinct() ) {
				buffer.append("distinct ");
			}
			else {
	            // If function specifies a single non-distinct entity with ID, its alias would normally be rendered, which ends up
	            // converting to the column(s) associated with the entity's ID in the rendered SQL.  However, some DBs don't support
	            // the multiple columns that would end up here for entities with composite IDs.  So, since we modify the query to
	            // instead specify star since that's functionally equivalent and supported by all DBs.
				List<Expression<?>> argExprs = getArgumentExpressions();
				if (argExprs.size() == 1) {
					Expression argExpr = argExprs.get(0);
					if (argExpr instanceof Root<?>) {
						Root<?> root = (Root<?>)argExpr;
						if (!root.getModel().hasSingleIdAttribute()) {
							buffer.append('*');
							return;
						}
					}
				}
			}
			super.renderArguments(buffer, renderingContext);
		}

		public boolean isDistinct() {
			return distinct;
		}

	}

	/**
     * Implementation of a <tt>AVG</tt> function providing convenience in construction.
     * <p/>
     * Parameterized as {@link Double} because thats what JPA states that the return from <tt>AVG</tt> should be.
	 */
	public static class AVG extends AggregationFunction<Double> {
		public static final String NAME = "avg";

		public AVG(CriteriaBuilderImpl criteriaBuilder, Expression<? extends Number> expression) {
			super( criteriaBuilder, Double.class, NAME, expression );
		}
	}

	/**
	 * Implementation of a <tt>SUM</tt> function providing convenience in construction.
	 * <p/>
	 * Parameterized as {@link Number N extends Number} because thats what JPA states
	 * that the return from <tt>SUM</tt> should be.
	 */
	public static class SUM<N extends Number> extends AggregationFunction<N> {
		public static final String NAME = "sum";

		@SuppressWarnings({ "unchecked" })
		public SUM(CriteriaBuilderImpl criteriaBuilder, Expression<N> expression) {
			super( criteriaBuilder, (Class<N>)expression.getJavaType(), NAME , expression);
			// force the use of a ValueHandler
			resetJavaType( expression.getJavaType() );
		}

		public SUM(CriteriaBuilderImpl criteriaBuilder, Expression<? extends Number> expression, Class<N> returnType) {
			super( criteriaBuilder, returnType, NAME , expression);
			// force the use of a ValueHandler
			resetJavaType( returnType );
		}
	}

	/**
	 * Implementation of a <tt>MIN</tt> function providing convenience in construction.
	 * <p/>
	 * Parameterized as {@link Number N extends Number} because thats what JPA states
	 * that the return from <tt>MIN</tt> should be.
	 */
	public static class MIN<N extends Number> extends AggregationFunction<N> {
		public static final String NAME = "min";

		@SuppressWarnings({ "unchecked" })
		public MIN(CriteriaBuilderImpl criteriaBuilder, Expression<N> expression) {
			super( criteriaBuilder, ( Class<N> ) expression.getJavaType(), NAME , expression);
		}
	}

	/**
	 * Implementation of a <tt>MAX</tt> function providing convenience in construction.
	 * <p/>
	 * Parameterized as {@link Number N extends Number} because thats what JPA states
	 * that the return from <tt>MAX</tt> should be.
	 */
	public static class MAX<N extends Number> extends AggregationFunction<N> {
		public static final String NAME = "max";

		@SuppressWarnings({ "unchecked" })
		public MAX(CriteriaBuilderImpl criteriaBuilder, Expression<N> expression) {
			super( criteriaBuilder, ( Class<N> ) expression.getJavaType(), NAME , expression);
		}
	}

	/**
	 * Models  the <tt>MIN</tt> function in terms of non-numeric expressions.
	 *
	 * @see MIN
	 */
	public static class LEAST<X extends Comparable<X>> extends AggregationFunction<X> {
		public static final String NAME = "min";

		@SuppressWarnings({ "unchecked" })
		public LEAST(CriteriaBuilderImpl criteriaBuilder, Expression<X> expression) {
			super( criteriaBuilder, ( Class<X> ) expression.getJavaType(), NAME , expression);
		}
	}

	/**
	 * Models  the <tt>MAX</tt> function in terms of non-numeric expressions.
	 *
	 * @see MAX
	 */
	public static class GREATEST<X extends Comparable<X>> extends AggregationFunction<X> {
		public static final String NAME = "max";

		@SuppressWarnings({ "unchecked" })
		public GREATEST(CriteriaBuilderImpl criteriaBuilder, Expression<X> expression) {
			super( criteriaBuilder, ( Class<X> ) expression.getJavaType(), NAME , expression);
		}
	}
}
