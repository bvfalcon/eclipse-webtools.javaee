/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Jun 7, 2004
 *
 */
package org.eclipse.jst.j2ee.internal.ejb.operations;

import org.eclipse.wst.common.framework.operation.WTPOperation;
import org.eclipse.wst.common.internal.emfworkbench.operation.EditModelOperationDataModel;


/**
 * @author dfholt
 *  
 */
public class RemoveRelationshipDataModel extends EditModelOperationDataModel {
	/**
	 * Required, type String
	 */
	public static final String COMMON_RELATIONSHIP_LIST = "RemoveRelationshipDataModel.COMMON_RELATIONSHIP_LIST"; //$NON-NLS-1$

	protected void init() {
		super.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.internal.emfworkbench.operation.EditModelOperationDataModel#initValidBaseProperties()
	 */
	protected void initValidBaseProperties() {
		addValidBaseProperty(COMMON_RELATIONSHIP_LIST);
		super.initValidBaseProperties();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.framework.operation.WTPOperationDataModel#getDefaultOperation()
	 */
	public WTPOperation getDefaultOperation() {
		return new RemoveRelationshipDataModelOperation(this);
	}

}