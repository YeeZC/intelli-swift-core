
package com.finebi.analysis.api.criteria;

import com.finebi.analysis.api.Parameter;

/**
 * Type of criteria query parameter expressions.
 *
 * @param <T> the type of the parameter expression
 *
 * @since Advanced FineBI Analysis 1.0
 */
public interface ParameterExpression<T> extends Parameter<T>, Expression<T> {}
