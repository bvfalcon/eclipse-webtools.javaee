/***********************************************************************
 * Copyright (c) 2008 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 ***********************************************************************/
package org.eclipse.jst.jee.ui.internal.navigator;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.IModelProviderEvent;
import org.eclipse.jst.j2ee.model.IModelProviderListener;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.j2ee.navigator.internal.EMFRootObjectProvider.IRefreshHandlerListener;
import org.eclipse.jst.jee.ui.plugin.JEEUIPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

public abstract class JEE5ContentProvider implements ITreeContentProvider, IRefreshHandlerListener, IModelProviderListener {

	protected Viewer viewer;
	protected static final Class IPROJECT_CLASS = IProject.class;
	
	protected static HashMap<IProject, IModelProvider> groupProvidersMap = new HashMap<IProject, IModelProvider>();


	public void inputChanged(Viewer aViewer, Object anOldInput, Object aNewInput) {
		viewer = aViewer;
	}
	
	
	protected IModelProvider getCachedModelProvider(IProject project) {
		IModelProvider provider = groupProvidersMap.get(project);
		if (provider != null){
			Object mObj = provider.getModelObject();
		} else{
			provider = ModelProviderManager.getModelProvider(project);
			provider.addListener(this);
			groupProvidersMap.put(project,provider);
		}
		return provider;
	}

	public void projectChanged(final IProject project) {
		try {
			Runnable refreshThread = new Runnable() {
				public void run() {
					if (viewer != null) {
						ISelection sel = ((TreeViewer) viewer).getSelection();
						ITreeContentProvider contentProvider = ((ITreeContentProvider) ((TreeViewer) viewer)
								.getContentProvider());
						contentProvider.getChildren(project);
						((StructuredViewer) viewer).refresh(project);
						((TreeViewer) viewer).setSelection(sel);
					}
				}
			};
			Display.getDefault().asyncExec(refreshThread);
		} catch (Exception e)
		{
			JEEUIPlugin.logError("Error during refresh", e); //$NON-NLS-1$
		}
	}

	public void modelsChanged(IModelProviderEvent event) {
		projectChanged(event.getProject());
	}

	public void onRefresh(final Object element) {
		if (viewer instanceof AbstractTreeViewer) {
			if (Display.getCurrent() != null) {
				((AbstractTreeViewer) viewer).refresh(element, true);
			} else {
				/* Create and schedule a UI Job to update the Navigator Content Viewer */
				Job job = new UIJob("Update the Navigator Content Viewer Job") { //$NON-NLS-1$
					public IStatus runInUIThread(IProgressMonitor monitor) {
						((AbstractTreeViewer) viewer).refresh(element, true);
						return Status.OK_STATUS;
					}
				};
				ISchedulingRule rule = new ISchedulingRule() {
					public boolean contains(ISchedulingRule rule) {
						return rule == this;	
					}
					public boolean isConflicting(ISchedulingRule rule) {
						return rule == this;
					}
				};
				if (rule != null) {
					job.setRule(rule);
				}
				job.schedule();
			}
		}
	}
	
	public void dispose() {
		groupProvidersMap.clear();
	}
}
