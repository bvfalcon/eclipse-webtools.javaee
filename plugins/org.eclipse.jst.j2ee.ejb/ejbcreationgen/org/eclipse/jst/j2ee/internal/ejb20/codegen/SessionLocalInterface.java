/***************************************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.jst.j2ee.internal.ejb20.codegen;


import org.eclipse.jst.j2ee.internal.ejb.codegen.EnterpriseBeanClientInterface;

public class SessionLocalInterface extends EnterpriseBeanClientInterface {
	/*
	 * @see EnterpriseBeanClientInterface#isRemote()
	 */
	public boolean isRemote() {
		return false;
	}

}