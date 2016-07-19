
package com.finebi.datasource.sql.criteria.internal.expression.function;

import java.io.Serializable;

import com.finebi.datasource.sql.criteria.internal.ParameterRegistry;
import com.finebi.datasource.sql.criteria.internal.CriteriaBuilderImpl;
import com.finebi.datasource.sql.criteria.internal.compile.RenderingContext;
import com.finebi.datasource.sql.criteria.internal.expression.ExpressionImpl;

/**
 * Models a <tt>CAST</tt> function.
 *
 * @param <T> The cast result type.
 * @param <Y> The type of the expression to be cast.
 *
 * @author Steve Ebersole
 */
public class CastFunction<T,Y>
		extends BasicFunctionExpression<T>
		implements FunctionExpression<T>, Serializable {
	public static final String CAST_NAME = "cast";

	private final ExpressionImpl<Y> castSource;

	public CastFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Class<T> javaType,
			ExpressionImpl<Y> castSource) {
		super( criteriaBuilder, javaType, CAST_NAME );
		this.castSource = castSource;
	}

	public ExpressionImpl<Y> getCastSource() {
		return castSource;
	}

	@Override
	public void registerParameters(ParameterRegistry registry) {
		Helper.possibleParameter( getCastSource(), registry );
	}

	@Override
	public String render(RenderingContext renderingContext) {
		return CAST_NAME + '(' +
				castSource.render( renderingContext ) +
				" as " +
				renderingContext.getCastType( getJavaType() ) +
				')';
	}
}
