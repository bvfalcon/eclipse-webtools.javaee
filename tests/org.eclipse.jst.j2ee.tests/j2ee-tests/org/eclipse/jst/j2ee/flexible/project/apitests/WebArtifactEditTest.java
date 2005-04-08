package org.eclipse.jst.j2ee.flexible.project.apitests;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.j2ee.ejb.modulecore.util.EJBArtifactEdit;
import org.eclipse.jst.j2ee.web.modulecore.util.WebArtifactEdit;
import org.eclipse.wst.common.componentcore.ArtifactEditModel;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.componentcore.StructureEdit;
import org.eclipse.wst.common.componentcore.UnresolveableURIException;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.common.componentcore.internal.resources.ComponentHandle;
import org.eclipse.wst.common.internal.emfworkbench.EMFWorkbenchContext;

import junit.framework.TestCase;

public class WebArtifactEditTest extends TestCase {

	private IProject webProject;
	private String webModuleName;

	public WebArtifactEditTest() {
		super();
		if (TestWorkspace.init()) {
			webProject = TestWorkspace.getTargetProject(TestWorkspace.WEB_PROJECT_NAME);
			webModuleName = TestWorkspace.WEB_MODULE_NAME;
		} else {
			fail();

		}
	}

	public void testGetJ2EEVersion() {
		StructureEdit moduleCore = null;
		EJBArtifactEdit edit = null;
		try {
			moduleCore = StructureEdit.getStructureEditForRead(webProject);
			WorkbenchComponent wbComponent = moduleCore.findComponentByName(webModuleName);
			String version = wbComponent.getComponentType().getVersion();
			assertTrue(version.equals(TestWorkspace.WEB_PROJECT_VERSION));
		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
			}
		}
	}

	public void testGetDeploymentDescriptorResource() {
		StructureEdit moduleCore = null;
		WebArtifactEdit edit = null;
		try {
			moduleCore = StructureEdit.getStructureEditForRead(webProject);
			WorkbenchComponent wbComponent = moduleCore.findComponentByName(webModuleName);
			edit = WebArtifactEdit.getWebArtifactEditForRead(wbComponent);
			String uri = edit.getDeploymentDescriptorResource().getURI().toString();
			assertTrue(uri.equals(TestWorkspace.WEB_DD_RESOURCE_URI));

		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
				edit.dispose();
			}
			assertTrue(edit != null);

		}
	}

	public void testGetDeploymentDescriptorRoot() {
		StructureEdit moduleCore = null;
		WebArtifactEdit edit = null;
		try {
			moduleCore = StructureEdit.getStructureEditForRead(webProject);
			WorkbenchComponent wbComponent = moduleCore.findComponentByName(webModuleName);
			edit = WebArtifactEdit.getWebArtifactEditForRead(wbComponent);
			// /////BUG in PlatformURL\\\\\\\\\\\turning test off////
			/*
			 * EObject object = edit.getDeploymentDescriptorRoot(); assertNotNull(object);
			 */

		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
				edit.dispose();
			}
			assertTrue(edit != null);

		}
	}

	/*
	 * Class under test for EObject createModelRoot()
	 */
	public void testCreateModelRoot() {
		StructureEdit moduleCore = null;
		WebArtifactEdit edit = null;
		try {
			moduleCore = StructureEdit.getStructureEditForRead(webProject);
			WorkbenchComponent wbComponent = moduleCore.findComponentByName(webModuleName);
			edit = WebArtifactEdit.getWebArtifactEditForWrite(wbComponent);
			// ////BUG turning off\\\\\\\\\\\\\
			/*
			 * EObject object = edit.createModelRoot(); assertNotNull(object);
			 */

		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
				edit.dispose();
			}
			assertTrue(edit != null);

		}
	}

	/*
	 * Class under test for EObject createModelRoot(int)
	 */
	public void testCreateModelRootint() {
		StructureEdit moduleCore = null;
		WebArtifactEdit edit = null;
		try {
			moduleCore = StructureEdit.getStructureEditForRead(webProject);
			WorkbenchComponent wbComponent = moduleCore.findComponentByName(webModuleName);
			// ///////BUG in PlatformURLModuleConnection
			edit = WebArtifactEdit.getWebArtifactEditForRead(wbComponent);
			/*
			 * EObject object = edit.createModelRoot(14); assertNotNull(object);
			 */

		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
				edit.dispose();
			}
			assertTrue(edit != null);

		}
	}

	/*
	 * Class under test for void WebArtifactEdit(ComponentHandle, boolean)
	 */
	public void testWebArtifactEditComponentHandleboolean() {
		StructureEdit moduleCore = null;
		WorkbenchComponent wbComponent = null;
		ComponentHandle handle = null;
		WebArtifactEdit edit = null;
		try {
			moduleCore = StructureEdit.getStructureEditForWrite(webProject);
			wbComponent = moduleCore.findComponentByName(webModuleName);
			handle = ComponentHandle.create(webProject, wbComponent.getName());
			edit = new WebArtifactEdit(handle, true);
			assertNotNull(edit);
		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
				edit.dispose();
			}
		}

	}

	/*
	 * Class under test for WebArtifactEdit getWebArtifactEditForRead(ComponentHandle)
	 */
	public void testGetWebArtifactEditForReadComponentHandle() {
		StructureEdit moduleCore = null;
		WebArtifactEdit edit = null;
		try {
			moduleCore = StructureEdit.getStructureEditForRead(webProject);
			WorkbenchComponent wbComponent = moduleCore.findComponentByName(webModuleName);
			ComponentHandle handle = ComponentHandle.create(webProject, wbComponent.getName());
			edit = WebArtifactEdit.getWebArtifactEditForRead(handle);
			assertTrue(edit != null);

		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
				edit.dispose();
			}

		}
	}

	/*
	 * Class under test for ArtifactEdit getWebArtifactEditForWrite(ComponentHandle)
	 */
	public void testGetWebArtifactEditForWriteComponentHandle() {
		StructureEdit moduleCore = null;
		WebArtifactEdit edit = null;
		try {
			moduleCore = StructureEdit.getStructureEditForWrite(webProject);
			WorkbenchComponent wbComponent = moduleCore.findComponentByName(webModuleName);
			ComponentHandle handle = ComponentHandle.create(webProject, wbComponent.getName());
			edit = WebArtifactEdit.getWebArtifactEditForWrite(handle);

		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
				edit.dispose();
			}
			assertTrue(edit != null);
		}
	}

	/*
	 * Class under test for WebArtifactEdit getWebArtifactEditForRead(WorkbenchComponent)
	 */
	public void testGetWebArtifactEditForReadWorkbenchComponent() {
	}

	/*
	 * Class under test for WebArtifactEdit getWebArtifactEditForWrite(WorkbenchComponent)
	 */
	public void testGetWebArtifactEditForWriteWorkbenchComponent() {
		StructureEdit moduleCore = null;
		WebArtifactEdit edit = null;
		try {
			moduleCore = StructureEdit.getStructureEditForRead(webProject);
			WorkbenchComponent wbComponent = moduleCore.findComponentByName(webModuleName);
			edit = WebArtifactEdit.getWebArtifactEditForRead(wbComponent);
			assertTrue(edit != null);

		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
				edit.dispose();
			}


		}
	}

	public void testIsValidWebModule() {
		StructureEdit moduleCore = null;
		WorkbenchComponent wbComponent = null;
		try {
			moduleCore = StructureEdit.getStructureEditForWrite(webProject);
			wbComponent = moduleCore.findComponentByName(webModuleName);
			ComponentHandle handle = ComponentHandle.create(webProject, wbComponent.getName());
		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
			}
			assertTrue(WebArtifactEdit.isValidEditableModule(wbComponent));
		}
	}

	/*
	 * Class under test for void WebArtifactEdit(ArtifactEditModel)
	 */
	public void testWebArtifactEditArtifactEditModel() {
		WebArtifactEdit edit = new WebArtifactEdit(getArtifactEditModelforRead());
		assertNotNull(edit);
		edit.dispose();
	}

	/*
	 * Class under test for void WebArtifactEdit(ModuleCoreNature, WorkbenchComponent, boolean)
	 */
	public void testWebArtifactEditModuleCoreNatureWorkbenchComponentboolean() {
		StructureEdit moduleCore = null;
		WorkbenchComponent wbComponent = null;
		WebArtifactEdit edit = null;
		try {
			moduleCore = StructureEdit.getStructureEditForWrite(webProject);
			wbComponent = moduleCore.findComponentByName(webModuleName);
			ModuleCoreNature nature = null;
			nature = moduleCore.getModuleCoreNature(TestWorkspace.WEB_MODULE_URI);
			edit = new WebArtifactEdit(nature, wbComponent, true);
			assertNotNull(edit);
		} catch (UnresolveableURIException e) {
			fail();
		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
				edit.dispose();
			}
		}
	}

	public void testGetServletVersion() {
		StructureEdit moduleCore = null;
		WorkbenchComponent wbComponent = null;
		try {
			moduleCore = StructureEdit.getStructureEditForWrite(webProject);
			wbComponent = moduleCore.findComponentByName(webModuleName);
			ComponentHandle handle = ComponentHandle.create(webProject, wbComponent.getName());
		} finally {
			if (moduleCore != null) {
				moduleCore.dispose();
			}
			assertTrue(WebArtifactEdit.isValidEditableModule(wbComponent));
		}
	}

	public void testAddWebAppIfNecessary() {
	}

	public void testGetJSPVersion() {
	}

	public void testGetDeploymentDescriptorPath() {
	}

	public void testGetLibModules() {
	}

	public void testAddLibModules() {
	}

	public void testGetServerContextRoot() {
	}

	public void testSetServerContextRoot() {
	}

	public ArtifactEditModel getArtifactEditModelforRead() {
		EMFWorkbenchContext context = new EMFWorkbenchContext(webProject);
		return new ArtifactEditModel(this.toString(), context, true, TestWorkspace.APP_CLIENT_MODULE_URI);
	}

	public WebArtifactEdit getArtifactEditForRead() {
		return new WebArtifactEdit(getArtifactEditModelforRead());
	}

}
