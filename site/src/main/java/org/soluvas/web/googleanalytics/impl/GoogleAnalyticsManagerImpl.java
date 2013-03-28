/**
 */
package org.soluvas.web.googleanalytics.impl;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.soluvas.web.googleanalytics.GoogleAnalyticsManager;
import org.soluvas.web.googleanalytics.GoogleanalyticsPackage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Google Analytics Manager</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.soluvas.web.googleanalytics.impl.GoogleAnalyticsManagerImpl#getTrackingId <em>Tracking Id</em>}</li>
 *   <li>{@link org.soluvas.web.googleanalytics.impl.GoogleAnalyticsManagerImpl#isEnabled <em>Enabled</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
@Service("googleAnalyticsMgr")
public class GoogleAnalyticsManagerImpl extends EObjectImpl implements GoogleAnalyticsManager {
	/**
	 * The default value of the '{@link #getTrackingId() <em>Tracking Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTrackingId()
	 * @generated
	 * @ordered
	 */
	protected static final String TRACKING_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTrackingId() <em>Tracking Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTrackingId()
	 * @generated
	 * @ordered
	 */
	protected String trackingId = TRACKING_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected boolean enabled = ENABLED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected GoogleAnalyticsManagerImpl() {
		throw new UnsupportedOperationException("Please use the constructor with arguments");
	}
	
	@Inject
	public GoogleAnalyticsManagerImpl(
			@Value("#{soluvasProps.googleanalyticsTrackingId}") String trackingId,
			@Value("#{soluvasProps.googleanalyticsEnabled}") boolean enabled) {
		super();
		this.trackingId = trackingId;
		this.enabled = enabled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GoogleanalyticsPackage.Literals.GOOGLE_ANALYTICS_MANAGER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getTrackingId() {
		return trackingId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GoogleanalyticsPackage.GOOGLE_ANALYTICS_MANAGER__TRACKING_ID:
				return getTrackingId();
			case GoogleanalyticsPackage.GOOGLE_ANALYTICS_MANAGER__ENABLED:
				return isEnabled();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case GoogleanalyticsPackage.GOOGLE_ANALYTICS_MANAGER__TRACKING_ID:
				return TRACKING_ID_EDEFAULT == null ? trackingId != null : !TRACKING_ID_EDEFAULT.equals(trackingId);
			case GoogleanalyticsPackage.GOOGLE_ANALYTICS_MANAGER__ENABLED:
				return enabled != ENABLED_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (trackingId: ");
		result.append(trackingId);
		result.append(", enabled: ");
		result.append(enabled);
		result.append(')');
		return result.toString();
	}

} //GoogleAnalyticsManagerImpl
