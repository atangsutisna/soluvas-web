/**
 */
package org.soluvas.web.site.compose;

import org.apache.wicket.Page;

import org.osgi.framework.Bundle;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Live Target</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.soluvas.web.site.compose.LiveTarget#getPageClass <em>Page Class</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.soluvas.web.site.compose.ComposePackage#getLiveTarget()
 * @model
 * @generated
 */
public interface LiveTarget extends Target {
	/**
	 * Returns the value of the '<em><b>Page Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Note that since bundles that provide a Page and/or ComponentFactory can come and go, a LinkedContributor can be gone at any time as well.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Page Class</em>' attribute.
	 * @see #setPageClass(Class)
	 * @see org.soluvas.web.site.compose.ComposePackage#getLiveTarget_PageClass()
	 * @model required="true"
	 * @generated
	 */
	Class<Page> getPageClass();

	/**
	 * Sets the value of the '{@link org.soluvas.web.site.compose.LiveTarget#getPageClass <em>Page Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Page Class</em>' attribute.
	 * @see #getPageClass()
	 * @generated
	 */
	void setPageClass(Class<Page> value);

} // LiveTarget
