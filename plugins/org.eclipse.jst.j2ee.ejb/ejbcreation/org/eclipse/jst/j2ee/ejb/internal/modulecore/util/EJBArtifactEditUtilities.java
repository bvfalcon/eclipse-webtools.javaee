package org.eclipse.jst.j2ee.ejb.internal.modulecore.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.ejb.EJBJar;
import org.eclipse.jst.j2ee.ejb.EnterpriseBean;
import org.eclipse.jst.j2ee.ejb.componentcore.util.EJBArtifactEdit;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IFlexibleProject;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class EJBArtifactEditUtilities {
	
	public static IVirtualComponent getEJBComponent(EnterpriseBean bean) {
		IProject project = ProjectUtilities.getProject(bean);
		IFlexibleProject flexProject = ComponentCore.createFlexibleProject(project);
		IVirtualComponent[] components = flexProject.getComponents();
		for (int i = 0; i < components.length; i++) {
			IVirtualComponent component = components[i];
			EJBArtifactEdit edit = null;
			try {
				if (component.getComponentTypeId().equals(IModuleConstants.JST_EJB_MODULE)) {
					edit = EJBArtifactEdit.getEJBArtifactEditForRead(component);
					EJBJar jar = edit.getEJBJar();
					if (jar.getEnterpriseBeanNamed(bean.getName()) != null)
						return component;
				}
			} finally {
				if (edit != null)
					edit.dispose();
			}
		}
		return null;
	}
	
	public static EJBJar getEJBJar(IVirtualComponent comp) {
		EJBArtifactEdit edit = null;
		try {
			edit = EJBArtifactEdit.getEJBArtifactEditForRead(comp);
			if (edit != null)
				edit.getEJBJar();
		} finally {
			if (edit != null)
				edit.dispose();
		}
		return null;	
			
	}
}

