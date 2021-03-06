package org.soluvas.web.site;

import com.google.common.base.Optional;

/**
 * @author ceefour
 */
@SuppressWarnings("serial")
public class JavaScriptModuleImpl implements JavaScriptModule {

	private String name;
	private String path;
	private String minifiedPath;
	private Base base = Base.STATIC;
	
	public JavaScriptModuleImpl() {
		super();
	}
	
	public JavaScriptModuleImpl(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}
	
	public JavaScriptModuleImpl(String name, String path, String minifiedPath) {
		super();
		this.name = name;
		this.path = path;
		this.minifiedPath = minifiedPath;
	}
	
	public JavaScriptModuleImpl(String name, String path, String minifiedPath, Base base) {
		super();
		this.name = name;
		this.path = path;
		this.minifiedPath = minifiedPath;
		this.base = base;
	}
	
	/* (non-Javadoc)
	 * @see org.soluvas.web.site.JavaScriptModule#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see org.soluvas.web.site.JavaScriptModule#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see org.soluvas.web.site.JavaScriptModule#getPath()
	 */
	@Override
	public String getPath() {
		return path;
	}
	
	/* (non-Javadoc)
	 * @see org.soluvas.web.site.JavaScriptModule#setPath(java.lang.String)
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/* (non-Javadoc)
	 * @see org.soluvas.web.site.JavaScriptModule#getMinifiedPath()
	 */
	@Override
	public String getMinifiedPath() {
		return minifiedPath;
	}
	
	/* (non-Javadoc)
	 * @see org.soluvas.web.site.JavaScriptModule#setMinifiedPath(java.lang.String)
	 */
	public void setMinifiedPath(String minifiedPath) {
		this.minifiedPath = minifiedPath;
	}
	
	public Base getBase() {
		return base;
	}
	
	@Override
	public String toString() {
		return "JavaScriptModuleImpl [name=" + name + ", path=" + path
				+ ", minifiedPath=" + minifiedPath + ", base=" + base + "]";
	}

	/* (non-Javadoc)
	 * @see org.soluvas.web.site.JavaScriptModule#compareTo(org.soluvas.web.site.JavaScriptModuleImpl)
	 */
	@Override
	public int compareTo(JavaScriptModule o) {
		return Optional.fromNullable(getName()).or("").compareToIgnoreCase(o.getName());
	}
	
}
