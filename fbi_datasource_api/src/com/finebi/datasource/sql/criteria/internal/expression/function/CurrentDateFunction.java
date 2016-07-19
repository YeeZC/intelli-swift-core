
package com.finebi.datasource.sql.criteria.internal.expression.function;

import java.io.Serializable;
import java.sql.Date;

import com.finebi.datasource.sql.criteria.internal.CriteriaBuilderImpl;

/**
 * Models the ANSI SQL <tt>CURRENT_DATE</tt> function.
 *
 * @author Steve Ebersole
 */
public class CurrentDateFunction
		extends BasicFunctionExpression<Date>
		implements Serializable {
	public static final String NAME = "current_date";

	public CurrentDateFunction(CriteriaBuilderImpl criteriaBuilder) {
		super( criteriaBuilder, Date.class, NAME );
	}
}
