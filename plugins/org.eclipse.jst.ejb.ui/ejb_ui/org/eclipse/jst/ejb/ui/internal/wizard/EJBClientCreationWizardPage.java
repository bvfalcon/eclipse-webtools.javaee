/***************************************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.jst.ejb.ui.internal.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jst.ejb.ui.internal.util.EJBUIMessages;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.internal.modulecore.util.EJBArtifactEdit;
import org.eclipse.jst.j2ee.internal.actions.IJ2EEUIContextIds;
import org.eclipse.jst.j2ee.internal.ejb.project.operations.EJBClientProjectDataModel;
import org.eclipse.jst.j2ee.internal.wizard.NewProjectGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.frameworks.internal.operations.ProjectCreationDataModel;
import org.eclipse.wst.common.frameworks.operations.WTPOperationDataModel;
import org.eclipse.wst.common.frameworks.ui.WTPWizardPage;
import org.eclipse.wst.common.modulecore.ModuleCore;
import org.eclipse.wst.common.modulecore.UnresolveableURIException;
import org.eclipse.wst.common.modulecore.WorkbenchModule;

import com.ibm.wtp.common.logger.proxy.Logger;

public class EJBClientCreationWizardPage extends WTPWizardPage {
	public NewProjectGroup newProjectGroup = null;
	protected EJBJar selProject = null;
	private Label selectedProjectLabel;
	private Text selectedProjectName;
	private Label clientJarURILabel;
	private Text clientJarURI;
	private WorkbenchModule module;
	protected int indent = 0;

	/**
	 * @param model
	 * @param pageName
	 */
	public EJBClientCreationWizardPage(EJBClientProjectDataModel model, String pageName) {
		super(model, pageName);
		setTitle(EJBUIMessages.getResourceString(EJBUIMessages.EJB_Client_Title)); //$NON-NLS-1$
		setDescription(EJBUIMessages.getResourceString(EJBUIMessages.EJB_Client_Desc)); //$NON-NLS-1$
	}

	/**
	 * @param model
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public EJBClientCreationWizardPage(WTPOperationDataModel model, String pageName, String title, ImageDescriptor titleImage) {
		super(model, pageName, title, titleImage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.frameworks.internal.ui.wizard.WTPWizardPage#createTopLevelComposite(org.eclipse.swt.widgets.Composite)
	 */
	protected Composite createTopLevelComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		setInfopopID(IJ2EEUIContextIds.NEW_EJB_WIZARD_P2);
		createEJBProjectSelectionSection(composite);
		createNewJ2EEModuleGroup(composite);
		handleHasClientJar();
		return composite;
	}

	private void createEJBProjectSelectionSection(Composite parent) {
		Composite newComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		newComposite.setLayout(layout);

		GridData data = new GridData();

		selectedProjectLabel = new Label(newComposite, SWT.NULL);
		selectedProjectLabel.setText(EJBUIMessages.getResourceString(EJBUIMessages.EJB_Project)); //$NON-NLS-1$ 

		data.widthHint = 305;
		selectedProjectName = new Text(newComposite, SWT.NULL);
		selectedProjectName.setLayoutData(data);
		selectedProjectName.setEditable(false);
		synchHelper.synchText(selectedProjectName, EJBClientProjectDataModel.EJB_PROJECT_NAME, new Control[]{selectedProjectLabel});

		setSpacer(newComposite);

		clientJarURILabel = new Label(newComposite, SWT.NULL);
		clientJarURILabel.setText(EJBUIMessages.getResourceString(EJBUIMessages.Client_JAR_URI) + " "); //$NON-NLS-1$ 

		data = new GridData();
		data.widthHint = 305;
		clientJarURI = new Text(newComposite, SWT.BORDER);
		clientJarURI.setEditable(true);
		clientJarURI.setLayoutData(data);
		synchHelper.synchText(clientJarURI, EJBClientProjectDataModel.CLIENT_PROJECT_URI, new Control[]{clientJarURILabel});
	}

	protected void createNewJ2EEModuleGroup(Composite parent) {
		Composite newComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		newComposite.setLayout(layout);

		newProjectGroup = new NewProjectGroup(newComposite, SWT.NULL, ((EJBClientProjectDataModel) model).getNestedJavaProjectCreationDM());
	}

	private void setSpacer(Composite composite) {
		Label space = new Label(composite, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		space.setLayoutData(data);

		space = new Label(composite, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		space.setLayoutData(data);
	}

	private void handleHasClientJar() {
		EJBArtifactEdit edit = null;
		try {
			if (module != null) {
				edit = EJBArtifactEdit.getEJBArtifactEditForRead(module);
				if (edit != null && edit.hasEJBClientJARProject(ModuleCore.getContainingProject(module.getHandle())));
					enableAllSections(false);
				} else
					enableAllSections(true);
		} catch(UnresolveableURIException e) {
				Logger.getLogger().logError(e);
		  } finally {
			  if(edit != null)
				  edit.dispose();
				  
		  }
	}
	
	private void enableAllSections(boolean state) {
		selectedProjectLabel.setEnabled(state);
		selectedProjectName.setEnabled(state);
		clientJarURILabel.setEnabled(state);
		clientJarURI.setEnabled(state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.frameworks.internal.ui.wizard.WTPWizardPage#getValidationPropertyNames()
	 */
	protected String[] getValidationPropertyNames() {
		return new String[]{EJBClientProjectDataModel.EJB_PROJECT_NAME, EJBClientProjectDataModel.CLIENT_PROJECT_URI, ProjectCreationDataModel.PROJECT_NAME, ProjectCreationDataModel.PROJECT_LOCATION};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.frameworks.internal.ui.wizard.WTPWizardPage#showValidationErrorsOnEnter()
	 */
	protected boolean showValidationErrorsOnEnter() {
		return true;
	}


	public void dispose() {
		newProjectGroup.dispose();
		super.dispose();
	}
}