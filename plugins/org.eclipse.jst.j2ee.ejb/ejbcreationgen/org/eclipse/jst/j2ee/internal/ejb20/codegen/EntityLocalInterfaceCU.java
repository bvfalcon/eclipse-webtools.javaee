/***************************************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.jst.j2ee.internal.ejb20.codegen;


import org.eclipse.jst.j2ee.internal.ejb.codegen.IEJBGenConstants;

public class EntityLocalInterfaceCU extends EnterpriseBeanLocalInterfaceCU {

	/**
	 * Constructor for EntityLocalInterfaceCU.
	 */
	public EntityLocalInterfaceCU() {
		super();
	}

	/*
	 * @see EnterpriseBeanRemoteInterfaceCU#getTypeGeneratorName()
	 */
	protected String getTypeGeneratorName() {
		return IEJBGenConstants.ENTITY_LOCAL_INTERFACE;
	}

}