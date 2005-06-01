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
package org.eclipse.jst.j2ee.internal;


import org.eclipse.jst.common.jdt.internal.integration.WorkingCopyManager;
import org.eclipse.jst.common.jdt.internal.integration.WorkingCopyManagerFactory;
import org.eclipse.jst.j2ee.internal.common.XMLResource;
import org.eclipse.jst.j2ee.internal.project.J2EENature;
import org.eclipse.jst.j2ee.webservice.wsclient.WebServicesResource;
import org.eclipse.wst.common.frameworks.internal.activities.WTPActivityBridge;
import org.eclipse.wst.common.internal.emfworkbench.EMFWorkbenchContext;
import org.eclipse.wst.common.internal.emfworkbench.integration.EditModel;
import org.eclipse.wst.common.internal.emfworkbench.integration.EditModelEvent;
import org.eclipse.wst.common.internal.emfworkbench.integration.EditModelListener;


/**
 * This class is used by clients (mainly editors) when there is an intent to modify a resource
 * within the Workbench. All clients with a specific key (this specifies a grouping of common
 * resources), will get the same instance of the J2EEEditModel. Clients will obtain resources
 * through this edit model. A client can add themselves as a listener (@see J2EEEditModelListener).
 * This will allow clients to fire property change notifications when a J2EEEditModelEvent is
 * detected. This helps to synchronize editors within the Workbench. There is no need for editors to
 * add themselves as CommandStack listeners because this edit model will catch CommandStack changes
 * and forward them to all listeners as J2EEEditModelEvents.
 * 
 * A J2EEEditModel is obtained from the J2EENature (@see getEditModel(Object)).
 * 
 * TIPS ON USING THIS EDIT MODEL:
 * 
 * To determine if a client needs to save (e.g., <code>isSaveNeeded()</code>), one should call
 * <code>isDirty()</code>.
 * 
 * To determine if a client needs to save when closing (e.g., <code>isSaveOnCloseNeeded()</code>),
 * one should call <code>needsToSave()</code>.
 * 
 * When the main client (editor) performs a save (e.g., <code>doSave()</code>), they should call
 * <code>save()</code>.
 * 
 * If a wizard or another client needs to save the resources but they are not the primary editor,
 * <code>saveIfNecessary()</code> should be called. This will save the resources only if there are
 * no other references to this edit model.
 * 
 * When disposing the client, <code>releaseAccess</code> should be called.
 * 
 * @deprecated
 * Use
 * <p>
 * 		ArtifactEdit
 * </p>
 */
public class J2EEEditModel extends EditModel {

	private WorkingCopyManager workingCopyManager = null;
    /**
     * This activity no longer exists use ENTERPRISE_JAVA instead
     * 
     * @deprecated
     */
    public static final String APP_CLIENT_DEV_ACTIVITY_ID = "org.eclipse.jst.j2ee.application.client.development"; //$NON-NLS-1$
    /**
     * This activity no longer exists use ENTERPRISE_JAVA instead
     * 
     * @deprecated
     */
    public static final String EJB_DEV_ACTIVITY_ID = "com.ibm.wtp.ejb.development"; //$NON-NLS-1$
    /**
     * This activity no longer exists use ENTERPRISE_JAVA instead
     * 
     * @deprecated
     */
    public static final String JCA_DEV_ACTIVITY_ID = "com.ibm.wtp.jca.development"; //$NON-NLS-1$
    public static final String WEB_DEV_ACTIVITY_ID = "com.ibm.wtp.web.development"; //$NON-NLS-1$
    public static final String ENTERPRISE_JAVA = "com.ibm.wtp.ejb.development"; //$NON-NLS-1$
   
	/**
	 * J2EEEditModel constructor comment.
	 */
	public J2EEEditModel(String editModelID, EMFWorkbenchContext context, boolean readOnly) {
		super(editModelID, context, readOnly);
	}

	/**
	 * J2EEEditModel constructor comment.
	 */
	public J2EEEditModel(String editModelID, EMFWorkbenchContext context, boolean readOnly, boolean accessUnkownResourcesAsReadOnly) {
		super(editModelID, context, readOnly);
	}

	/**
	 * This will be the type of the deployment descriptor docuemnt. Subclasses should override if
	 * they have a deployment descriptor.
	 * 
	 * @see XMLResource#APP_CLIENT_TYPE
	 * @see XMLResource#APPLICATION_TYPE
	 * @see XMLResource#EJB_TYPE
	 * @see XMLResource#WEB_APP_TYPE
	 * @see XMLResource#RAR_TYPE
	 */
	public int getDeploymentDescriptorType() {
		if (getJ2EENature() != null)
			return getJ2EENature().getDeploymentDescriptorType();
		return -1;
	}

	/**
	 * Subclasses should override to return the proper deployment descriptor resource.
	 */
	public XMLResource getDeploymentDescriptorResource() {
		return null;
	}

	public J2EENature getJ2EENature() {
		return J2EENature.getRegisteredRuntime(getProject());
	}


	public WebServicesResource get13WebServicesClientResource() {
		return null;
	}


	private ActivityEditModelListener activityEditModelListener = null;

	private class ActivityEditModelListener implements EditModelListener {
		public void editModelChanged(EditModelEvent anEvent) {
			if (anEvent.getEventCode() == EditModelEvent.DIRTY) {
				try {
					WTPActivityBridge.getInstance().enableActivity(getDevelopmentAcivityID(), true);
				} finally {
					removeListener(this);
					activityEditModelListener = null;
				}
			}
		}
	}

	public void notifyActivityChanges(boolean b) {
		if (null != activityEditModelListener || null == getDevelopmentAcivityID()) {
			return;
		}
		activityEditModelListener = new ActivityEditModelListener();
		getListeners().add(activityEditModelListener);
	}

	public String getDevelopmentAcivityID() {
		return null;
	}

	/**
	 * Returns the working copy remembered for the compilation unit.
	 * 
	 * @param input
	 *            ICompilationUnit
	 * @return the working copy of the compilation unit, or <code>null</code> if there is no
	 *         remembered working copy for this compilation unit
	 */
	public org.eclipse.jdt.core.ICompilationUnit getWorkingCopy(org.eclipse.jdt.core.ICompilationUnit cu, boolean forNewCU) throws org.eclipse.core.runtime.CoreException {
		if (isReadOnly())
			return null;
		return getWorkingCopyManager().getWorkingCopy(cu, forNewCU);
	}

	public WorkingCopyManager getWorkingCopyManager() {
		if (workingCopyManager == null)
			workingCopyManager = WorkingCopyManagerFactory.newRegisteredInstance();
		return workingCopyManager;
	}

	/**
	 * Reset the working copy manager because the ejb-jar.xml was removed without disposing.
	 */
	protected void resetWorkingCopyManager() {
		if (workingCopyManager != null) {
			workingCopyManager.dispose();
			workingCopyManager = null;
		}
	}
}