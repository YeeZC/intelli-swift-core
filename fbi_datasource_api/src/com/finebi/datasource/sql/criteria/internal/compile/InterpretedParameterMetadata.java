/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.finebi.datasource.sql.criteria.internal.compile;

import com.finebi.datasource.api.criteria.ParameterExpression;
import java.util.List;
import java.util.Map;

/**
 * Represents information about parameters from a compiled criteria query.
 *
 * @author Steve Ebersole
 */
public interface InterpretedParameterMetadata {
	public Map<ParameterExpression<?>, ExplicitParameterInfo<?>> explicitParameterInfoMap();

//	public Map<ParameterExpression<?>,String> explicitParameterMapping();
//	public Map<String,ParameterExpression<?>> explicitParameterNameMapping();
	public List<ImplicitParameterBinding> implicitParameterBindings();
//	public Map<String,Class> implicitParameterTypes();
}
