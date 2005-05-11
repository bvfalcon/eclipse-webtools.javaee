package org.eclipse.jem.internal.proxy.remote;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: REMLongTypeBeanProxy.java,v $
 *  $Revision: 1.3 $  $Date: 2005/05/11 22:41:24 $ 
 */


import org.eclipse.jem.internal.proxy.common.remote.Commands;
import org.eclipse.jem.internal.proxy.core.*;
/**
 * Remote Implementation of INumberBeanProxy for the primitive "long".
 * It can answer all of the primitive number types (int, long, short, byte, double, float).
 * Creation date: (2/6/00 9:02:54 AM)
 * @author: Joe Winchester
 */
class REMLongTypeBeanProxy extends REMConstantBeanProxy implements INumberBeanProxy {
	private final long fLong;

/**
 * It is package protected because they are created
 * in a special way and no one else should create them.
 * @param aBean java.lang.Object
 */
REMLongTypeBeanProxy(REMProxyFactoryRegistry aRegistry, long aLong) {
	super(aRegistry);
	fLong = aLong;
}
/**
 * equals: Equal if:
 *         1) This proxy == (identity) to the other object
 *         2) Else if other is an IBeanProxy and not a constant one, then if
 *            equals on the server.
 *         3) If this is a constant proxy and the other is too or is a constant
 *            value (e.g. IStringBeanProxy.equals(String), then true if values are equals.
 */
public boolean equals(Object anObject) {
	if (this == anObject)
		return true;	// Identity
	if (anObject instanceof REMLongTypeBeanProxy)
		return fLong == ((REMLongTypeBeanProxy) anObject).longValue();
	if (anObject instanceof Long)
		return fLong == ((Long) anObject).longValue();
	return false;
}

/* (non-Javadoc)
 * @see org.eclipse.jem.internal.proxy.core.IBeanProxy#sameAs(org.eclipse.jem.internal.proxy.core.IBeanProxy)
 */
public boolean sameAs(IBeanProxy aBeanProxy) {
	if (this == aBeanProxy)
		return true;	// Identity
	if (aBeanProxy instanceof REMLongTypeBeanProxy)
		return fLong == ((REMLongTypeBeanProxy) aBeanProxy).longValue();
	return false;
}
/**
 * byteValue method comment.
 */
public byte byteValue() {
	return (byte) fLong;
}
/**
 * doubleValue method comment.
 */
public double doubleValue() {
	return fLong;
}
/**
 * floatValue method comment.
 */
public float floatValue() {
	return fLong;
}
/**
 * Return the int value
 */
public int intValue() {
	return (int) fLong;
}
/**
 * longValue method comment.
 */
public long longValue() {
	return fLong;
}
/**
 * numberValue method comment.
 */
public Number numberValue() {
	return new Long(fLong);
}
/**
 * shortValue method comment.
 */
public short shortValue() {
	return (short) fLong;
}

/**
 * The bean's toString.
 */
public String toBeanString() {
	return String.valueOf(fLong);
}

/**
 * Get the beantype
 */
public IBeanTypeProxy getTypeProxy() {
	return ((REMStandardBeanTypeProxyFactory) fFactory.getBeanTypeProxyFactory()).longType;
}

/**
 * Render the bean into value object.
 */
public void renderBean(Commands.ValueObject value) {
	value.set(fLong);
}
}


