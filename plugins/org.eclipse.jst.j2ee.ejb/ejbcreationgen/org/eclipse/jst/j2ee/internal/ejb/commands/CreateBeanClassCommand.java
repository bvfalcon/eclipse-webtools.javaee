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
package org.eclipse.jst.j2ee.internal.ejb.commands;



import org.eclipse.emf.ecore.EObject;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.internal.ejb.codegen.helpers.EJBClassReferenceHelper;
import org.eclipse.jst.j2ee.internal.ejb.codegen.helpers.EJBGenerationHelper;


/**
 * Insert the type's description here. Creation date: (9/5/2000 1:48:38 PM)
 * 
 * @author: Administrator
 */
public class CreateBeanClassCommand extends EJBClassReferenceCommand {
	/**
	 * AddBeanClassClass constructor comment.
	 * 
	 * @param parent
	 *            org.eclipse.jst.j2ee.commands.ICommand
	 * @param aJavaClass
	 *            org.eclipse.jem.internal.java.JavaClass
	 */
	public CreateBeanClassCommand(IRootCommand parent, JavaClass aJavaClass) {
		super(parent, aJavaClass);
	}

	/**
	 * AddBeanClassCommand constructor comment.
	 * 
	 * @param parent
	 *            org.eclipse.jst.j2ee.commands.ICommand
	 */
	public CreateBeanClassCommand(IRootCommand parent, JavaClass aJavaClass, boolean shouldGenMetadata) {
		super(parent, aJavaClass, true, shouldGenMetadata);
	}

	/**
	 * AddBeanClassClass constructor comment.
	 * 
	 * @param parent
	 *            org.eclipse.jst.j2ee.commands.ICommand
	 * @param aJavaClassName
	 *            java.lang.String
	 * @param aPackageName
	 *            java.lang.String
	 */
	public CreateBeanClassCommand(IRootCommand parent, String aJavaClassName, String aPackageName) {
		super(parent, aJavaClassName, aPackageName);
	}

	/**
	 * AddBeanClassCommand constructor comment.
	 * 
	 * @param parent
	 *            org.eclipse.jst.j2ee.commands.ICommand
	 */
	public CreateBeanClassCommand(IRootCommand parent, String aJavaClassName, String aPackageName, boolean shouldGenMetadata) {
		super(parent, aJavaClassName, aPackageName, true, shouldGenMetadata);
	}

	protected EJBGenerationHelper createCodegenHelper() {
		EJBClassReferenceHelper ejbClassRefHelper = new EJBClassReferenceHelper(getJavaClass());
		ejbClassRefHelper.setBeanHelper();
		return ejbClassRefHelper;
	}

	protected EJBGenerationHelper createInverseCodegenHelper() {
		EJBClassReferenceHelper ejbClassRefHelper = new EJBClassReferenceHelper(getOriginalJavaClass());
		ejbClassRefHelper.setBeanHelper();
		return ejbClassRefHelper;
	}

	/**
	 * Update the ejbBeanClass attribute for the EJB.
	 */
	protected void executeForMetadataGeneration() {
		super.executeForMetadataGeneration();
		if (getEjb() == null) {
			failedSettingClass();
			return;
		}
		JavaClass bean = getJavaClass();
		if (bean == null) {
			getEjb().setEjbClassName(getQualifiedName());
			setJavaClass(getEjb().getEjbClass());
		} else
			getEjb().setEjbClass(bean);
	}

	/**
	 * Find the original ejbClass
	 */
	public EObject findOriginalSourceMetaType() {
		EnterpriseBean ejb = getOriginalEjb();
		if (ejb != null)
			return ejb.getEjbClass();
		return null;
	}

	/**
	 * Override to perform any setup on the code generation helper before being passed to code
	 * generation framework. Creation date: (10/11/2000 10:30:08 AM)
	 */
	protected void setupHelper() {
		super.setupHelper();
		getHelper().setCreate();
	}

	/**
	 * Override to perform any setup on the code generation helper before being passed to code
	 * generation framework. Creation date: (10/11/2000 10:30:08 AM)
	 */
	protected void setupInverseHelper() {
		super.setupInverseHelper();
		getInverseHelper().setDelete();
	}

	/**
	 * Undo the ejbBeanClass updates.
	 */
	protected void undoMetadataGeneration() {
		if (getEjb() != null)
			getEjb().setEjbClass(getOriginalJavaClass());
		super.undoMetadataGeneration();
	}
}