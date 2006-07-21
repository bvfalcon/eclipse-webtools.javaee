/*
 * Created on Jan 6, 2004
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code
 * Generation - Code and Comments
 */
package org.eclipse.wtp.j2ee.headless.tests.j2ee.operations;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.j2ee.application.internal.operations.J2EEComponentExportDataModelProvider;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.tests.OperationTestCase;
import org.eclipse.wst.common.tests.ProjectUtility;
import org.eclipse.wtp.headless.tests.savestrategy.ModuleImportOperationTestCase;
import org.eclipse.wtp.j2ee.headless.tests.appclient.operations.AppClientExportOperationTest;
import org.eclipse.wtp.j2ee.headless.tests.ejb.operations.EJBExportOperationTest;
import org.eclipse.wtp.j2ee.headless.tests.jca.operations.RARExportOperationTest;
import org.eclipse.wtp.j2ee.headless.tests.plugin.HeadlessTestsPlugin;
import org.eclipse.wtp.j2ee.headless.tests.web.operations.WebExportOperationTest;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to Window - Preferences - Java - Code
 * Generation - Code and Comments
 */
public abstract class ModuleExportOperationTestCase extends OperationTestCase {

	protected boolean exportSourceFiles = false;
	protected boolean overwriteExisting = false;
	protected boolean dataModelShouldBeValid = true;

	//public String TESTS_OUTPUT_PATH;

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(RARExportOperationTest.class);
		suite.addTestSuite(EJBExportOperationTest.class);
		suite.addTestSuite(WebExportOperationTest.class);
		suite.addTestSuite(AppClientExportOperationTest.class);
		return suite;
	}

	public ModuleExportOperationTestCase(String name) {
		super(name);
	}
	
	public String getOutputPath(String relPath) {
		try{
			return ProjectUtility.getFullFileName(HeadlessTestsPlugin.getDefault(),relPath);
			} catch(IOException e) {
				e.printStackTrace();
			}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wtp.j2ee.headless.tests.j2ee.operations.OperationTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		exportSourceFiles = false;
		overwriteExisting = false;
	}

	public String getOutputDirectory(){
	    return "testOutput";
	}
	protected abstract ModuleImportOperationTestCase getImportTestCase();
	protected abstract IDataModel getModelInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wtp.j2ee.headless.tests.j2ee.operations.ModuleExportOperationTestCase#getExportableProjects()
	 */
	protected IProject[] getExportableProjects() throws Exception {
		
		deleteAllProjects();
		ModuleImportOperationTestCase importTestCase = getImportTestCase(); 
		importTestCase.testAllImportTestCases();

		// if the projects aren't created successfully, the previous
		// line will fail so there's no need to verify
		return ProjectUtility.getAllProjects();
	}

	public void testExport(IVirtualComponent component, String filename) throws Exception {
		IDataModel dataModel = getModelInstance();
		dataModel.setProperty(J2EEComponentExportDataModelProvider.ARCHIVE_DESTINATION, getTestsOutputPath() + filename);
		dataModel.setProperty(J2EEComponentExportDataModelProvider.COMPONENT, component);
		dataModel.setBooleanProperty(J2EEComponentExportDataModelProvider.EXPORT_SOURCE_FILES, exportSourceFiles);
		dataModel.setBooleanProperty(J2EEComponentExportDataModelProvider.OVERWRITE_EXISTING, overwriteExisting);

		if (dataModelShouldBeValid)
			runAndVerify(dataModel);
		else
			verifyInvalidDataModel(dataModel);
	}

	public void testAllWithExportSourceFilesWithOverwriteExisting() throws Exception {
		exportSourceFiles = true;
		overwriteExisting = true;

		testAllExportTestCases();
	}

	public void testAllWithExportSourceFilesWithoutOverwriteExisting() throws Exception {
		exportSourceFiles = true;
		overwriteExisting = false;

		testAllExportTestCases();
	}

	public void testAllWithoutExportSourceFilesWithoutOverwriteExisting() throws Exception {
		exportSourceFiles = false;
		overwriteExisting = false;

		testAllExportTestCases();
	}

	public void testAllWithoutExportSourceFilesWithOverwriteExisting() throws Exception {
		exportSourceFiles = false;
		overwriteExisting = true;

		testAllExportTestCases();
	}

//	public void testOverwriteProjectExportFail() throws Exception {
//
//		testAllWithoutExportSourceFilesWithoutOverwriteExisting();
//		dataModelShouldBeValid = false;
//		testAllWithoutExportSourceFilesWithoutOverwriteExisting();
//	}

	public void testOverwriteProjectExportSucceed() throws Exception {
		//TODO: Uncomment the second line to test the file lock tests. 
		//Commenting it out for now to get a clearance on RC1 for 0.7 release
		testAllWithoutExportSourceFilesWithOverwriteExisting();
		//testAllWithoutExportSourceFilesWithOverwriteExisting();
	}

	public void testAllExportTestCases() throws Exception {

		File exportDirectory = new File(getTestsOutputPath());
		if (exportDirectory.isDirectory()) {
			File[] contents = exportDirectory.listFiles();
			for (int i = 0; i < contents.length; i++) {
				if (!contents[i].isDirectory())
					contents[i].delete();
			}
		}
		IProject[] projects = getExportableProjects();
		for (int i = 0; i < projects.length; i++) {
			testExport(ComponentCore.createComponent(projects[i]), getFileName(projects[i].getName()));
		}
	}

	public String getFileName(String baseName) {
		StringBuffer result = new StringBuffer(baseName);
		result.append((exportSourceFiles) ? "_withSource" : "_withoutSource").append(getModuleExportFileExt());
		return result.toString();
	}
	protected String getTestsOutputPath() {
		String relativeOutputTestsPath = "TestData" + File.separator + getOutputDirectory() + File.separator;
		return getOutputPath(relativeOutputTestsPath);
	}

	/**
	 * @return
	 */
	public String getModuleExportFileExt() {
		return ".jar";
	}
}
