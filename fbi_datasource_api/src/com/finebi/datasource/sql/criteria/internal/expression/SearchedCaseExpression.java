/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.finebi.datasource.sql.criteria.internal.expression;

import com.finebi.datasource.sql.criteria.internal.ParameterRegistry;
import com.finebi.datasource.sql.criteria.internal.CriteriaBuilderImpl;
import com.finebi.datasource.sql.criteria.internal.compile.RenderingContext;

import com.finebi.datasource.api.criteria.CriteriaBuilder.Case;
import com.finebi.datasource.api.criteria.Expression;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Models what ANSI SQL terms a <tt>searched case expression</tt>.  This is a <tt>CASE</tt> expression
 * in the form<pre>
 * CASE
 *     WHEN [firstCondition] THEN [firstResult]
 *     WHEN [secondCondition] THEN [secondResult]
 *     ELSE [defaultResult]
 * END
 * </pre>
 *
 * @author Steve Ebersole
 */
public class SearchedCaseExpression<R>
		extends ExpressionImpl<R>
		implements Case<R>, Serializable {
	private Class<R> javaType; // overrides the javaType kept on tuple-impl so that we can adjust it
	private List<WhenClause> whenClauses = new ArrayList<WhenClause>();
	private Expression<? extends R> otherwiseResult;

	public class WhenClause {
		private final Expression<Boolean> condition;
		private final Expression<? extends R> result;

		public WhenClause(Expression<Boolean> condition, Expression<? extends R> result) {
			this.condition = condition;
			this.result = result;
		}

		public Expression<Boolean> getCondition() {
			return condition;
		}

		public Expression<? extends R> getResult() {
			return result;
		}
	}

	public SearchedCaseExpression(
			CriteriaBuilderImpl criteriaBuilder,
			Class<R> javaType) {
		super( criteriaBuilder, javaType );
		this.javaType = javaType;
	}

	public Case<R> when(Expression<Boolean> condition, R result) {
		return when( condition, buildLiteral( result ) );
	}

	@SuppressWarnings({"unchecked"})
	private LiteralExpression<R> buildLiteral(R result) {
		final Class<R> type = result != null
				? (Class<R>) result.getClass()
				: getJavaType();
		return new LiteralExpression<R>( criteriaBuilder(), type, result );
	}

	public Case<R> when(Expression<Boolean> condition, Expression<? extends R> result) {
		WhenClause whenClause = new WhenClause( condition, result );
		whenClauses.add( whenClause );
		adjustJavaType( result );
		return this;
	}

	@SuppressWarnings({"unchecked"})
	private void adjustJavaType(Expression<? extends R> exp) {
		if ( javaType == null ) {
			javaType = (Class<R>) exp.getJavaType();
		}
	}

	public Expression<R> otherwise(R result) {
		return otherwise( buildLiteral( result ) );
	}

	public Expression<R> otherwise(Expression<? extends R> result) {
		this.otherwiseResult = result;
		adjustJavaType( result );
		return this;
	}

	public Expression<? extends R> getOtherwiseResult() {
		return otherwiseResult;
	}

	public List<WhenClause> getWhenClauses() {
		return whenClauses;
	}

	public void registerParameters(ParameterRegistry registry) {
		Helper.possibleParameter( getOtherwiseResult(), registry );
		for ( WhenClause whenClause : getWhenClauses() ) {
			Helper.possibleParameter( whenClause.getCondition(), registry );
			Helper.possibleParameter( whenClause.getResult(), registry );
		}
	}

	public String render(RenderingContext renderingContext) {
		return null;
	}

	public String renderProjection(RenderingContext renderingContext) {
		return null;
	}



}
