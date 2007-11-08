/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jee.model.tests;
import junit.framework.Assert;
import junit.framework.Test;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.jst.common.project.facet.IJavaFacetInstallDataModelProperties;
import org.eclipse.jst.common.project.facet.JavaFacetInstallDataModelProvider;
import org.eclipse.jst.j2ee.archive.emftests.GeneralEMFPopulationTest;
import org.eclipse.jst.j2ee.client.ApplicationClient;
import org.eclipse.jst.j2ee.earcreation.IEarFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.ejb.project.operations.IEjbFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.common.J2EEVersionUtil;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.j2ee.project.facet.IAppClientFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.web.project.facet.IWebFacetInstallDataModelProperties;
import org.eclipse.jst.javaee.application.Application;
import org.eclipse.jst.javaee.core.Description;
import org.eclipse.jst.javaee.core.JavaeeFactory;
import org.eclipse.jst.javaee.ejb.EJBJar;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.jee.project.facet.IAppClientCreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.jee.project.facet.ICreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.jee.project.facet.IEJBCreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.jee.project.facet.IEarCreateDeploymentFilesDataModelProperties;
import org.eclipse.jst.jee.project.facet.IWebCreateDeploymentFilesDataModelProperties;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.operations.IHeadlessRunnableWithProgress;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.tests.SimpleTestSuite;
import org.eclipse.wtp.j2ee.headless.tests.appclient.operations.AppClientProjectCreationOperationTest;
import org.eclipse.wtp.j2ee.headless.tests.ear.operations.EARProjectCreationOperationTest;
import org.eclipse.wtp.j2ee.headless.tests.ejb.operations.EJBProjectCreationOperationTest;
import org.eclipse.wtp.j2ee.headless.tests.web.operations.WebProjectCreationOperationTest;

public class ModelProviderTest extends GeneralEMFPopulationTest {

	private static final String PROJECTNAME = "TestModelProviders";
	private final String descText = "Testing setting the desc";
	public ModelProviderTest(String name) {
		super(name);
	}

    public static Test suite() {
        return new SimpleTestSuite(ModelProviderTest.class);
    }

    /**
	 * @param eObject
	 */
	
    protected Object primCreateAttributeValue(EAttribute att, EObject eObject) {
        if (att.getEAttributeType() == XMLTypePackage.eINSTANCE.getQName()) 
        	return null;
        else
            return super.primCreateAttributeValue(att, eObject);
    }

	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if(workspace.getRoot().getProject(PROJECTNAME).isAccessible()) return;
        final IProjectDescription description = workspace
                .newProjectDescription(PROJECTNAME);
        description.setLocation(null);

        // create the new project operation
        IHeadlessRunnableWithProgress op = new IHeadlessRunnableWithProgress() {
            public void run(IProgressMonitor monitor)
           {
                try {
					createProject(description, workspace.getRoot().getProject(PROJECTNAME), monitor);
				} catch (OperationCanceledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        };
        

        // run the new project creation operation
        try {
        	op.run(new NullProgressMonitor());
        } catch (InterruptedException e) {
            return;
        }
	}

    private void createProject(IProjectDescription description,
            IProject projectHandle, IProgressMonitor monitor)
            throws CoreException, OperationCanceledException {
        try {
            monitor.beginTask("", 2000); //$NON-NLS-1$

            projectHandle.create(description, new SubProgressMonitor(monitor,
                    1000));

            if (monitor.isCanceled())
                throw new OperationCanceledException();

            projectHandle.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1000));

        } finally {
            monitor.done();
        }
    }

	public void testUseEar5Model() throws Exception {

		String projName = "TestEE5EarProject";//$NON-NLS-1$
		IProject earProj = createEarProject(projName, J2EEVersionConstants.JEE_5_0_ID, true);
		
		final IModelProvider provider = ModelProviderManager.getModelProvider(earProj);
		
	
		provider.modify(new Runnable() {
			public void run() {
				Application ear = (Application)provider.getModelObject();
				Description desc = JavaeeFactory.eINSTANCE.createDescription();
				desc.setValue(descText);
				ear.getDescriptions().add(desc);
			}
		}
			, null);
		
		//Close project to force flush
		earProj.close(new NullProgressMonitor());
		//Re-open project
		earProj.open(new NullProgressMonitor());
		
		IModelProvider newProvider = ModelProviderManager.getModelProvider(earProj);
		Application sameEar = (Application)newProvider.getModelObject();
		Assert.assertNotNull("Application Model Object should not be null",sameEar);

		Description desc = (Description)sameEar.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());

	}
	
	public void testUseEar5NoDDModel() throws Exception {

		String projName = "TestEE5EarProject";//$NON-NLS-1$
		IProject earProj = createEarProject(projName, J2EEVersionConstants.JEE_5_0_ID, false);
		
		//create the DD because the project was created without one
		IDataModel ddCreateModel = this.getDDCreationDataModel(earProj);
		ddCreateModel.getDefaultOperation().execute( new NullProgressMonitor(), null);
		
		final IModelProvider provider = ModelProviderManager.getModelProvider(earProj);
		provider.modify(new Runnable() {
			public void run() {
				Application ear = (Application)provider.getModelObject();
				if (ear.getDescriptions().isEmpty())
					ear.getDescriptions().add(JavaeeFactory.eINSTANCE.createDescription());
				Description desc = (Description)ear.getDescriptions().get(0);
				desc.setValue(descText);
			}
		}
			, IModelProvider.FORCESAVE);
		
		//Close project to force flush
		earProj.close(new NullProgressMonitor());
		//Re-open project
		earProj.open(new NullProgressMonitor());
		
		
		IModelProvider newProvider = ModelProviderManager.getModelProvider(earProj);
		Application sameEar = (Application)newProvider.getModelObject();
		Description desc = (Description)sameEar.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());
		
	}

	public void testUseEar14Model() throws Exception {

		String projName = "TestEE14EarProject";//$NON-NLS-1$
		IProject earProj = createEarProject(projName, J2EEVersionConstants.J2EE_1_4_ID, true);
		final IModelProvider provider = ModelProviderManager.getModelProvider(earProj);
		
		// Test getting model through path api.
		org.eclipse.jst.j2ee.application.Application ear = (org.eclipse.jst.j2ee.application.Application)provider.getModelObject(new Path(J2EEConstants.APPLICATION_DD_URI));
		
		
		provider.modify(new Runnable() {
			public void run() {
				org.eclipse.jst.j2ee.application.Application ear = (org.eclipse.jst.j2ee.application.Application)provider.getModelObject();
				ear.setDescription(descText);
				
			}
		}
			, null);
		
		//Close project to force flush
		earProj.close(new NullProgressMonitor());
		//Re-open project
		earProj.open(new NullProgressMonitor());
		
		
		IModelProvider newProvider = ModelProviderManager.getModelProvider(earProj);
		org.eclipse.jst.j2ee.application.Application sameEar = (org.eclipse.jst.j2ee.application.Application)newProvider.getModelObject();
		org.eclipse.jst.j2ee.common.Description desc = (org.eclipse.jst.j2ee.common.Description)sameEar.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());
	}
	
	public void testUseWeb25Model() throws Exception {

		String projName = "TestEE5WebProject";//$NON-NLS-1$
		IProject webProj = createWebProject(projName, J2EEVersionConstants.WEB_2_5_ID, true);

		final IModelProvider provider = ModelProviderManager.getModelProvider(webProj);
		
		provider.modify(new Runnable() {
			public void run() {
				WebApp webApp = (WebApp)provider.getModelObject();
				Description desc = JavaeeFactory.eINSTANCE.createDescription();
				desc.setValue(descText);
				webApp.getDescriptions().add(desc);
			}
		}
			, null);
		
		//Close project to force flush
		webProj.close(new NullProgressMonitor());
		//Re-open project
		webProj.open(new NullProgressMonitor());


		IModelProvider newProvider = ModelProviderManager.getModelProvider(webProj);
		WebApp sameWebApp = (WebApp)newProvider.getModelObject();
		Description desc = (Description)sameWebApp.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());

	}
	
	public void testUseWeb25NoDDModel() throws Exception {
		String projName = "TestEE5WebProject";//$NON-NLS-1$
		IProject webProj = createWebProject(projName, J2EEVersionConstants.WEB_2_5_ID, false);
		
		//create the DD because the project was created without one
		IDataModel ddCreateModel = this.getDDCreationDataModel(webProj);
		ddCreateModel.getDefaultOperation().execute( new NullProgressMonitor(), null);

		final IModelProvider provider = ModelProviderManager.getModelProvider(webProj);
		
		provider.modify(new Runnable() {
			public void run() {
				WebApp webApp = (WebApp)provider.getModelObject();
				if (webApp.getDescriptions().isEmpty())
					webApp.getDescriptions().add(JavaeeFactory.eINSTANCE.createDescription());
				Description desc = (Description)webApp.getDescriptions().get(0);
				desc.setValue(descText);
			}
		}
			, IModelProvider.FORCESAVE);
		
		//Close project to force flush
		webProj.close(new NullProgressMonitor());
		//Re-open project
		webProj.open(new NullProgressMonitor());
		
		
		IModelProvider newProvider = ModelProviderManager.getModelProvider(webProj);
		WebApp sameWebApp = (WebApp)newProvider.getModelObject();
		Description desc = (Description)sameWebApp.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());

	}

	public void testUseWeb24Model() throws Exception {
		
		String projName = "TestEE14WebProject";//$NON-NLS-1$
		IProject webProj = createWebProject(projName, J2EEVersionConstants.WEB_2_4_ID, true);
		final IModelProvider provider = ModelProviderManager.getModelProvider(webProj);
		
		provider.modify(new Runnable() {
			public void run() {
				org.eclipse.jst.j2ee.webapplication.WebApp webApp = (org.eclipse.jst.j2ee.webapplication.WebApp)provider.getModelObject();
				webApp.setDescription(descText);
			}
		}
			, null);
		
		//Close project to force flush
		webProj.close(new NullProgressMonitor());
		//Re-open project
		webProj.open(new NullProgressMonitor());
		
		
		IModelProvider newProvider = ModelProviderManager.getModelProvider(webProj);
		org.eclipse.jst.j2ee.webapplication.WebApp sameWebApp = (org.eclipse.jst.j2ee.webapplication.WebApp)newProvider.getModelObject();
		org.eclipse.jst.j2ee.common.Description desc = (org.eclipse.jst.j2ee.common.Description)sameWebApp.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());
	
	}

	public void testUseEjb3Model() throws Exception {

		String projName = "TestEE5EjbProject";//$NON-NLS-1$
		IProject ejbProj = createEjbProject(projName, J2EEVersionConstants.EJB_3_0_ID, true);

		final IModelProvider provider = ModelProviderManager.getModelProvider(ejbProj);

		provider.modify(new Runnable() {
			public void run() {
				EJBJar ejbJar = (EJBJar)provider.getModelObject();
				Description desc = JavaeeFactory.eINSTANCE.createDescription();
				desc.setValue(descText);
				ejbJar.getDescriptions().add(desc);
			}
		}
		, null);

		//Close project to force flush
		ejbProj.close(new NullProgressMonitor());
		//Re-open project
		ejbProj.open(new NullProgressMonitor());


		IModelProvider newProvider = ModelProviderManager.getModelProvider(ejbProj);
		EJBJar sameEjbJar = (EJBJar)newProvider.getModelObject();
		Assert.assertNotNull("EJBJar Model Object should not be null",sameEjbJar);

		Description desc = (Description)sameEjbJar.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());

	}
	
	public void testUseEjb3NoDDModel() throws Exception {
		String projName = "TestEE5EjbProject";//$NON-NLS-1$
		IProject ejbProj = createEjbProject(projName, J2EEVersionConstants.EJB_3_0_ID, false);
		
		//create the DD because the project was created without one
		IDataModel ddCreateModel = this.getDDCreationDataModel(ejbProj);
		ddCreateModel.getDefaultOperation().execute( new NullProgressMonitor(), null);
		
		final IModelProvider provider = ModelProviderManager.getModelProvider(ejbProj);
		
		provider.modify(new Runnable() {
			public void run() {
				EJBJar ejbJar = (EJBJar)provider.getModelObject();
				if (ejbJar.getDescriptions().isEmpty())
					ejbJar.getDescriptions().add(JavaeeFactory.eINSTANCE.createDescription());
				Description desc = (Description)ejbJar.getDescriptions().get(0);
				desc.setValue(descText);
			}
		}
			, IModelProvider.FORCESAVE);
		
		//Close project to force flush
		ejbProj.close(new NullProgressMonitor());
		//Re-open project
		ejbProj.open(new NullProgressMonitor());
		
		
		IModelProvider newProvider = ModelProviderManager.getModelProvider(ejbProj);
		EJBJar sameEjbJar = (EJBJar)newProvider.getModelObject();
		Description desc = (Description)sameEjbJar.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());

	}

	public void testUseEjb21Model() throws Exception {

		String projName = "TestEE14EjbProject";//$NON-NLS-1$
		IProject ejbProj = createEjbProject(projName, J2EEVersionConstants.EJB_2_1_ID, true);
		final IModelProvider provider = ModelProviderManager.getModelProvider(ejbProj);

		provider.modify(new Runnable() {
			public void run() {
				org.eclipse.jst.j2ee.ejb.EJBJar ejbJar = (org.eclipse.jst.j2ee.ejb.EJBJar)provider.getModelObject();
				ejbJar.setDescription(descText);
			}
		}
		, null);

		//Close project to force flush
		ejbProj.close(new NullProgressMonitor());
		//Re-open project
		ejbProj.open(new NullProgressMonitor());


		IModelProvider newProvider = ModelProviderManager.getModelProvider(ejbProj);
		org.eclipse.jst.j2ee.ejb.EJBJar sameEjbJar = (org.eclipse.jst.j2ee.ejb.EJBJar)newProvider.getModelObject();
		org.eclipse.jst.j2ee.common.Description desc = (org.eclipse.jst.j2ee.common.Description)sameEjbJar.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());

	}
	
	public void testUseAppClient14Model() throws Exception {
		String projName = "TestEE14AppClientProject";//$NON-NLS-1$
		IProject appClientProj = createAppClientProject(projName, J2EEVersionConstants.J2EE_1_4_ID, true);
		final IModelProvider provider = ModelProviderManager.getModelProvider(appClientProj);
		
		// Test getting model through path api.
		ApplicationClient client = (ApplicationClient)provider.getModelObject(new Path(J2EEConstants.APP_CLIENT_DD_URI));
			
		provider.modify(new Runnable() {
			public void run() {
				ApplicationClient client = (ApplicationClient)provider.getModelObject();
				client.setDescription(descText);	
			}
		}
		, null);

		//Close project to force flush
		appClientProj.close(new NullProgressMonitor());
		//Re-open project
		appClientProj.open(new NullProgressMonitor());


		IModelProvider newProvider = ModelProviderManager.getModelProvider(appClientProj);
		ApplicationClient sameEar = (ApplicationClient)newProvider.getModelObject();
		org.eclipse.jst.j2ee.common.Description desc = (org.eclipse.jst.j2ee.common.Description)sameEar.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());
	}

	public void testUseAppClient5Model() throws Exception {

		String projName = "TestEE5AppClientProject";//$NON-NLS-1$
		IProject appClientProj = createAppClientProject(projName, J2EEVersionConstants.JEE_5_0_ID, true);
		final IModelProvider provider = ModelProviderManager.getModelProvider(appClientProj);

		// Test getting model through path api.
		org.eclipse.jst.javaee.applicationclient.ApplicationClient client = (org.eclipse.jst.javaee.applicationclient.ApplicationClient)provider.getModelObject(new Path(J2EEConstants.APP_CLIENT_DD_URI));

		provider.modify(new Runnable() {
			public void run() {
				org.eclipse.jst.javaee.applicationclient.ApplicationClient client = (org.eclipse.jst.javaee.applicationclient.ApplicationClient)provider.getModelObject();	
				Description desc = JavaeeFactory.eINSTANCE.createDescription();
				desc.setValue(descText);
				client.getDescriptions().add(desc);
			}
		}
		, null);

		//Close project to force flush
		appClientProj.close(new NullProgressMonitor());
		//Re-open project
		appClientProj.open(new NullProgressMonitor());


		IModelProvider newProvider = ModelProviderManager.getModelProvider(appClientProj);
		org.eclipse.jst.javaee.applicationclient.ApplicationClient sameClient = (org.eclipse.jst.javaee.applicationclient.ApplicationClient)newProvider.getModelObject();
		Description desc = (Description)sameClient.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());
	}
	
	public void testUseAppClient5NoDDModel() throws Exception {
		String projName = "TestEE5AppClientProject";//$NON-NLS-1$
		IProject appClientProj = createAppClientProject(projName, J2EEVersionConstants.JEE_5_0_ID, false);
		
		//create the DD because the project was created without one
		IDataModel ddCreateModel = this.getDDCreationDataModel(appClientProj);
		ddCreateModel.getDefaultOperation().execute( new NullProgressMonitor(), null);
		
		final IModelProvider provider = ModelProviderManager.getModelProvider(appClientProj);
		
		// Test getting model through path api.
		org.eclipse.jst.javaee.applicationclient.ApplicationClient client = (org.eclipse.jst.javaee.applicationclient.ApplicationClient)provider.getModelObject(new Path(J2EEConstants.APP_CLIENT_DD_URI));
		
		
		provider.modify(new Runnable() {
			public void run() {
				org.eclipse.jst.javaee.applicationclient.ApplicationClient client = (org.eclipse.jst.javaee.applicationclient.ApplicationClient)provider.getModelObject();
				if (client.getDescriptions().isEmpty())
					client.getDescriptions().add(JavaeeFactory.eINSTANCE.createDescription());
				Description desc = (Description)client.getDescriptions().get(0);
				desc.setValue(descText);
				
			}
		}
			, IModelProvider.FORCESAVE);
		
		//Close project to force flush
		appClientProj.close(new NullProgressMonitor());
		//Re-open project
		appClientProj.open(new NullProgressMonitor());
		
		
		IModelProvider newProvider = ModelProviderManager.getModelProvider(appClientProj);
		org.eclipse.jst.javaee.applicationclient.ApplicationClient sameClient = (org.eclipse.jst.javaee.applicationclient.ApplicationClient)newProvider.getModelObject();
		Description desc = (Description)sameClient.getDescriptions().get(0);
		Assert.assertEquals(descText, desc.getValue());
	}
	
	private IDataModel getDDCreationDataModel(IProject project) {
		Class dataModelClass = null;
		if(J2EEProjectUtilities.isEARProject(project)){
			dataModelClass = IEarCreateDeploymentFilesDataModelProperties.class;
		} else if(J2EEProjectUtilities.isEJBProject(project)){
			dataModelClass = IEJBCreateDeploymentFilesDataModelProperties.class;
		} else if(J2EEProjectUtilities.isDynamicWebProject(project)){
			dataModelClass = IWebCreateDeploymentFilesDataModelProperties.class;
		} else if(J2EEProjectUtilities.isApplicationClientProject(project)){
			dataModelClass = IAppClientCreateDeploymentFilesDataModelProperties.class;
		}
		IDataModel dataModel = DataModelFactory.createDataModel(dataModelClass);
		dataModel.setProperty(ICreateDeploymentFilesDataModelProperties.TARGET_PROJECT, project);
		return dataModel;
	}

	private IProject createWebProject(String projName, int vers, boolean createDD) throws ExecutionException {
		String webVersionString = J2EEVersionUtil.convertVersionIntToString(vers);
		IProjectFacet webFacet = ProjectFacetsManager.getProjectFacet(IWebFacetInstallDataModelProperties.DYNAMIC_WEB);
		IProjectFacetVersion webFacetVersion = webFacet.getVersion(webVersionString);
		IDataModel dataModel = WebProjectCreationOperationTest.getWebDataModel(projName, null, null, null, null, webFacetVersion, createDD);
		dataModel.getDefaultOperation().execute( new NullProgressMonitor(), null);
		IProject webProj = ResourcesPlugin.getWorkspace().getRoot().getProject(projName);
		return webProj;
	}

	private IProject createEjbProject(String projName, int vers, boolean createDD) throws ExecutionException {
		String versionString = J2EEVersionUtil.convertVersionIntToString(vers);
		IProjectFacet facet = ProjectFacetsManager.getProjectFacet(IEjbFacetInstallDataModelProperties.EJB);
		IProjectFacetVersion facetVersion = facet.getVersion(versionString);
		IDataModel dataModel = EJBProjectCreationOperationTest.getEJBDataModel(projName, null, null, null, facetVersion, false, createDD);
		dataModel.getDefaultOperation().execute( new NullProgressMonitor(), null);
		IProject ejbProj = ResourcesPlugin.getWorkspace().getRoot().getProject(projName);
		return ejbProj;
	}

	private IProject createEarProject(String projName, int vers, boolean createDD) throws ExecutionException {
		String versionString = J2EEVersionUtil.convertVersionIntToString(vers);
		IProjectFacet facet = ProjectFacetsManager.getProjectFacet(IEarFacetInstallDataModelProperties.ENTERPRISE_APPLICATION);
		IProjectFacetVersion facetVersion = facet.getVersion(versionString);
		IDataModel dataModel = EARProjectCreationOperationTest.getEARDataModel(projName, null, null, null, facetVersion, createDD);
		dataModel.getDefaultOperation().execute( new NullProgressMonitor(), null);
		
		IProject earProj = ResourcesPlugin.getWorkspace().getRoot().getProject(projName);
		return earProj;
	}

	private IProject createAppClientProject(String projName , int vers, boolean createDD) throws ExecutionException {
		String versionString = J2EEVersionUtil.convertVersionIntToString(vers);
		IProjectFacet facet = ProjectFacetsManager.getProjectFacet(IAppClientFacetInstallDataModelProperties.APPLICATION_CLIENT);
		IProjectFacetVersion facetVersion = facet.getVersion(versionString);
		IDataModel dataModel = AppClientProjectCreationOperationTest.getAppClientCreationDataModel(projName, null, facetVersion, true, createDD);
		dataModel.getDefaultOperation().execute( new NullProgressMonitor(), null);
		IProject webProj = ResourcesPlugin.getWorkspace().getRoot().getProject(projName);
		return webProj;
	}

	protected IDataModel setupJavaInstallAction(String aProjectName, String srcFolder) {
		IDataModel dm = DataModelFactory.createDataModel(new JavaFacetInstallDataModelProvider());
		dm.setProperty(IFacetDataModelProperties.FACET_PROJECT_NAME, aProjectName);
		String jVersion = "5.0";
		dm.setProperty(IFacetDataModelProperties.FACET_VERSION_STR, jVersion); //$NON-NLS-1$
		dm.setStringProperty(IJavaFacetInstallDataModelProperties.SOURCE_FOLDER_NAME, srcFolder); //$NON-NLS-1$
		return dm;
	}

	public IProject getProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECTNAME);
	}

	public IProject getProject(String projName) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(projName);
	}

	protected void tearDown() throws Exception {
		// Don't delete these files
	}
}
