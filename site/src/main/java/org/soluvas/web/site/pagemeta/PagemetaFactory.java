/**
 */
package org.soluvas.web.site.pagemeta;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.soluvas.web.site.pagemeta.PagemetaPackage
 * @generated
 */
public interface PagemetaFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PagemetaFactory eINSTANCE = org.soluvas.web.site.pagemeta.impl.PagemetaFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Page Meta</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Page Meta</em>'.
	 * @generated
	 */
	PageMeta createPageMeta();

	/**
	 * Returns a new object of class '<em>Page Icon</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Page Icon</em>'.
	 * @generated
	 */
	PageIcon createPageIcon();

	/**
	 * Returns a new object of class '<em>Open Graph Meta</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Open Graph Meta</em>'.
	 * @generated
	 */
	OpenGraphMeta createOpenGraphMeta();

	/**
	 * Returns a new object of class '<em>Open Graph Image</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Open Graph Image</em>'.
	 * @generated
	 */
	OpenGraphImage createOpenGraphImage();

	/**
	 * Returns a new object of class '<em>Open Graph Video</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Open Graph Video</em>'.
	 * @generated
	 */
	OpenGraphVideo createOpenGraphVideo();

	/**
	 * Returns a new object of class '<em>Open Graph Audio</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Open Graph Audio</em>'.
	 * @generated
	 */
	OpenGraphAudio createOpenGraphAudio();

	/**
	 * Returns a new object of class '<em>Uri Pattern Page Selector</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Uri Pattern Page Selector</em>'.
	 * @generated
	 */
	UriPatternPageSelector createUriPatternPageSelector();

	/**
	 * Returns a new object of class '<em>Page Rule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Page Rule</em>'.
	 * @generated
	 */
	PageRule createPageRule();

	/**
	 * Returns a new object of class '<em>Source Page Declaration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Source Page Declaration</em>'.
	 * @generated
	 */
	SourcePageDeclaration createSourcePageDeclaration();

	/**
	 * Returns a new object of class '<em>Processor Page Declaration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Processor Page Declaration</em>'.
	 * @generated
	 */
	ProcessorPageDeclaration createProcessorPageDeclaration();

	/**
	 * Returns a new object of class '<em>Resource Page Declaration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Page Declaration</em>'.
	 * @generated
	 */
	ResourcePageDeclaration createResourcePageDeclaration();

	/**
	 * Returns a new object of class '<em>Repository Page Declaration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Repository Page Declaration</em>'.
	 * @generated
	 */
	RepositoryPageDeclaration createRepositoryPageDeclaration();

	/**
	 * Returns a new object of class '<em>Page Meta Catalog</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Page Meta Catalog</em>'.
	 * @generated
	 */
	PageMetaCatalog createPageMetaCatalog();

	/**
	 * Returns a new object of class '<em>Page Title</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Page Title</em>'.
	 * @generated
	 */
	PageTitle createPageTitle();

	/**
	 * Returns a new object of class '<em>Class Page Selector</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Class Page Selector</em>'.
	 * @generated
	 */
	ClassPageSelector createClassPageSelector();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	PagemetaPackage getPagemetaPackage();

} //PagemetaFactory
