/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.j2ee.internal.web.operations;



import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.j2ee.internal.project.ProjectSupportResourceHandler;
import org.xml.sax.SAXException;


/**
 * The WebToolingException class is the generic class for exceptions generated by the web tooling.
 */
public class WebToolingException extends Exception {
	public static final String SAX_ERROR_MESSAGE = ProjectSupportResourceHandler.Operation_failed_due_to_SA_ERROR_; //$NON-NLS-1$ = "Operation failed due to SAX error: "
	public static final String IO_ERROR_MESSAGE = ProjectSupportResourceHandler.Operation_failed_due_to_IO_ERROR_; //$NON-NLS-1$ = "Operation failed due to IO error: "
	public static final String CORE_ERROR_MESSAGE = ProjectSupportResourceHandler.Operation_failed_due_to_Co_ERROR_; //$NON-NLS-1$ = "Operation failed due to Core error: "
	public static final String JAVA_MODEL_ERROR_MESSAGE = ProjectSupportResourceHandler.Operation_failed_due_to_Ja_ERROR_; //$NON-NLS-1$ = "Operation failed due to Java Model error: "

	/**
	 * Create a new WebToolingException with no message.
	 */
	public WebToolingException() {
		super();
	}

	/**
	 * Create a new instance of the receiver for a supplied IO Exception
	 * 
	 * @param message
	 *            java.lang.String
	 */
	public WebToolingException(IOException exception) {
		this(IO_ERROR_MESSAGE, exception);
	}

	/**
	 * Create a new WebToolingException with error message of s.
	 * 
	 * @param s
	 *            java.lang.String
	 */
	public WebToolingException(String s) {
		super(s);
	}

	/**
	 * Create a new instance of the receiver with the supplied preamble and the message of the
	 * example appended to the end.
	 * 
	 * @param message
	 *            java.lang.String
	 */
	public WebToolingException(String preamble, Throwable exception) {
		this(preamble + exception.getMessage());
	}

	/**
	 * Create a new instance of the receiver for a supplied CoreException
	 * 
	 * @param exception
	 *            CoreException
	 */
	public WebToolingException(CoreException exception) {
		this(CORE_ERROR_MESSAGE, exception);
	}

	/**
	 * Create a new instance of the receiver for a supplied JavaModelException.
	 * 
	 * @param exception
	 *            JavaModelException
	 */
	public WebToolingException(JavaModelException exception) {
		this(JAVA_MODEL_ERROR_MESSAGE, exception);
	}

	/**
	 * Create a new instance of the receiver for a supplied SAX Exception
	 * 
	 * @param exception
	 *            SAXException
	 */
	public WebToolingException(SAXException exception) {
		this(SAX_ERROR_MESSAGE, exception);
	}
}
