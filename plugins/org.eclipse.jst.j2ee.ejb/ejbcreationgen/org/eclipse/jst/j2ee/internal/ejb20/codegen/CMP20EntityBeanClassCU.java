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
package org.eclipse.jst.j2ee.internal.ejb20.codegen;


/**
 * Insert the type's description here. Creation date: (10/5/00 2:10:53 PM)
 * 
 * @author: Steve Wasleski
 */
import org.eclipse.jst.j2ee.internal.ejb.codegen.EntityBeanClassCU;

public class CMP20EntityBeanClassCU extends EntityBeanClassCU {
	/**
	 * CMPEntityRemoteInterfaceCU constructor comment.
	 */
	public CMP20EntityBeanClassCU() {
		super();
	}

	/**
	 * Returns the logical name for the type generator.
	 */
	protected String getTypeGeneratorName() {
		return IEJB20GenConstants.CMP20_ENTITY_BEAN_CLASS;
	}
}