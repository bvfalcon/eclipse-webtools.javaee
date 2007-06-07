/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.j2ee.internal.ejb.project.operations;

import org.eclipse.jst.j2ee.application.internal.operations.J2EEComponentImportDataModelProvider;
import org.eclipse.jst.j2ee.commonarchivecore.internal.Archive;
import org.eclipse.jst.j2ee.commonarchivecore.internal.CommonarchiveFactory;
import org.eclipse.jst.j2ee.commonarchivecore.internal.exception.OpenFailureException;
import org.eclipse.jst.j2ee.ejb.datamodel.properties.IEJBComponentImportDataModelProperties;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.common.J2EEVersionUtil;
import org.eclipse.jst.j2ee.internal.common.XMLResource;
import org.eclipse.jst.j2ee.internal.ejb.archiveoperations.EJBComponentImportOperation;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.jee.util.internal.JavaEEQuickPeek;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties.FacetDataModelMap;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;

/**
 * This dataModel is used for to import EJB Modules (from EJB Jar files) into the workspace.
 * 
 * This class (and all its fields and methods) is likely to change during the WTP 1.0 milestones as
 * the new project structures are adopted. Use at your own risk.
 * 
 * @plannedfor WTP 1.0
 */
public final class EJBComponentImportDataModelProvider extends J2EEComponentImportDataModelProvider implements IEJBComponentImportDataModelProperties {

	protected int getType() {
		return XMLResource.EJB_TYPE;
	}

	protected Archive openArchive(String uri) throws OpenFailureException {
		Archive archive =  CommonarchiveFactory.eINSTANCE.openEJBJarFile(getArchiveOptions(), uri);
		return archive;
	}

	@Override
	protected void handleUnknownType(JavaEEQuickPeek jqp) {
		jqp.setType(J2EEVersionConstants.EJB_TYPE);
		jqp.setVersion(J2EEVersionConstants.EJB_3_0_ID);
		jqp.setJavaEEVersion(J2EEVersionConstants.JEE_5_0_ID);
	}
	
	public IDataModelOperation getDefaultOperation() {
		return new EJBComponentImportOperation(model);
	}

	protected IDataModel createJ2EEComponentCreationDataModel() {
		IDataModel ejbCreationDM = DataModelFactory.createDataModel(new EjbFacetProjectCreationDataModelProvider());
		
		FacetDataModelMap map = (FacetDataModelMap) ejbCreationDM.getProperty(IFacetProjectCreationDataModelProperties.FACET_DM_MAP);
		IDataModel ejbFacetDataModel = map.getFacetDataModel( J2EEProjectUtilities.EJB );
		ejbFacetDataModel.setBooleanProperty(IEjbFacetInstallDataModelProperties.CREATE_CLIENT, false);
		return ejbCreationDM;
	}
	
	public boolean propertySet(String propertyName, Object propertyValue) {
		boolean set = super.propertySet(propertyName, propertyValue);
		if (propertyName.equals(ARCHIVE_WRAPPER)) {
			IDataModel moduleDM = model.getNestedModel(NESTED_MODEL_J2EE_COMPONENT_CREATION);
			if (getArchiveWrapper() != null) {
				
				FacetDataModelMap map = (FacetDataModelMap) moduleDM.getProperty(IFacetProjectCreationDataModelProperties.FACET_DM_MAP);
				IDataModel ejbFacetDataModel = map.getFacetDataModel( J2EEProjectUtilities.EJB );

				int version = getModuleSpecVersion();
				String versionText = J2EEVersionUtil.getEJBTextVersion( version );
				ejbFacetDataModel.setStringProperty(IFacetDataModelProperties.FACET_VERSION_STR, versionText);
				updateJavaFacetVersion();
				model.notifyPropertyChange(PROJECT_NAME, IDataModel.VALID_VALUES_CHG);
			}
		}
		return set;
	}	
}
