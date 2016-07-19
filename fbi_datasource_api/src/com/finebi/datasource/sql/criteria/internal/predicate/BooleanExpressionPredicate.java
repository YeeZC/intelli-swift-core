
package com.finebi.datasource.sql.criteria.internal.predicate;

import com.finebi.datasource.api.criteria.Expression;
import com.finebi.datasource.sql.criteria.internal.CriteriaBuilderImpl;
import com.finebi.datasource.sql.criteria.internal.ParameterRegistry;
import com.finebi.datasource.sql.criteria.internal.compile.RenderingContext;
import com.finebi.datasource.sql.criteria.internal.render.RenderExtended;

import java.io.Serializable;

/**
 * Defines a {@link com.finebi.datasource.api.criteria.Predicate} used to wrap an {@link Expression Expression&lt;Boolean&gt;}.
 *
 * @author Steve Ebersole
 */
public class BooleanExpressionPredicate
        extends AbstractSimplePredicate
        implements Serializable {
    private final Expression<Boolean> expression;

    public BooleanExpressionPredicate(CriteriaBuilderImpl criteriaBuilder, Expression<Boolean> expression) {
        super(criteriaBuilder);
        this.expression = expression;
    }

    /**
     * Get the boolean expression defining the predicate.
     *
     * @return The underlying boolean expression.
     */
    public Expression<Boolean> getExpression() {
        return expression;
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        Helper.possibleParameter(expression, registry);
    }

    @Override
    public Object render(boolean isNegated, RenderingContext renderingContext) {
        RenderExtended renderExtended = (RenderExtended) renderingContext.getRenderFactory().getBooleanExpressionPredicateRender(this, "defaultTag");
        if (isNegated) {
            renderExtended.negate();
        }
        return renderExtended.render(renderingContext);
    }
}
