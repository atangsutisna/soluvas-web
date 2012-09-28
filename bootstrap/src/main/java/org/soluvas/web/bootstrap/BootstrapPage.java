package org.soluvas.web.bootstrap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.ops4j.pax.wicket.api.PaxWicketBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soluvas.web.site.AmdJavaScriptSource;
import org.soluvas.web.site.ComponentFactory;
import org.soluvas.web.site.CssLink;
import org.soluvas.web.site.JavaScriptLink;
import org.soluvas.web.site.JavaScriptLinkImpl;
import org.soluvas.web.site.JavaScriptSource;
import org.soluvas.web.site.Site;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;

/**
 * Base page for Twitter Bootstrap-powered Wicket pages.
 * @author ceefour
 */
@SuppressWarnings("serial")
public class BootstrapPage extends WebPage {

	private transient Logger log = LoggerFactory.getLogger(BootstrapPage.class);
	
	@PaxWicketBean(name="jacksonMapperFactory")
	private Supplier<ObjectMapper> jacksonMapperFactory;
	@PaxWicketBean(name="site")
	private Site site;
	@PaxWicketBean(name="cssLinks")
	private List<CssLink> cssLinks;
	@PaxWicketBean(name="headJavaScripts")
	private List<JavaScriptLink> headJavaScripts;
	@PaxWicketBean(name="footerJavaScripts")
	private List<JavaScriptLink> footerJavaScripts;
	@PaxWicketBean(name="footerJavaScriptSources")
	private List<JavaScriptSource> footerJavaScriptSources;
	@PaxWicketBean(name="beforeFooterJsBlocks")
	private List<ComponentFactory<?>> beforeFooterJsBlocks;
	
	@PaxWicketBean(name="sidebarBlocks")
	private List<ComponentFactory<?>> sidebarBlocks;
	@PaxWicketBean(name="navbarChild")
	private ComponentFactory<?> navbarChildFactory;
	
	private Map<String, String> dependencies = ImmutableMap.of();
	private List<JavaScriptLink> pageJavaScriptLinks = new ArrayList<JavaScriptLink>();
	private List<String> pageJavaScriptSources = new ArrayList<String>();

	public JavaScriptLink addJsLink(String uri) {
		JavaScriptLinkImpl js = new JavaScriptLinkImpl(uri, 100);
		pageJavaScriptLinks.add(js);
		return js;
	}
	
	public String addJsSource(String source) {
		pageJavaScriptSources.add(source);
		return source;
	}
	
	public String addBackboneModel(String name, String className, Object data) {
		try {
			ObjectMapper objectMapper = jacksonMapperFactory.get();
			return addJsSource(name + " = new "+ className + "(" + objectMapper.writeValueAsString(data) + ");");
		} catch (Exception e) {
			throw new RuntimeException("Cannot serialize model to JSON: " + name + ": " + className + " from " + data, e);
		}
	}

	public String addBackboneView(String name, String className, String modelName,
			String elementId) {
		return addJsSource(name + " = new "+ className + "({model: " + modelName + ", id: '"+ elementId +"', el: '#" + elementId + "'});");
	}
	
	public Map<String, String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Map<String, String> dependencies) {
		this.dependencies = dependencies;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		log.debug("Page {} has {} CSS links", getClass().getName(), cssLinks.size());
		Ordering<CssLink> cssOrdering = Ordering.from(new Comparator<CssLink>() {
			public int compare(CssLink o1, CssLink o2) {
				return o1.getWeight() - o2.getWeight();
			};
		});
		List<CssLink> sortedCsses = cssOrdering.immutableSortedCopy(cssLinks);
		for (CssLink css : sortedCsses) {
			response.renderCSSReference(css.getHref());
		}
		
		log.debug("Page {} has {} head JavaScript links", getClass().getName(), headJavaScripts.size());
		Ordering<JavaScriptLink> jsOrdering = Ordering.from(new Comparator<JavaScriptLink>() {
			public int compare(JavaScriptLink o1, JavaScriptLink o2) {
				return o1.getWeight() - o2.getWeight();
			};
		});
		List<JavaScriptLink> sortedJses = jsOrdering.immutableSortedCopy(headJavaScripts);
		for (JavaScriptLink js : sortedJses) {
			response.renderJavaScriptReference(js.getSrc());
		}
	}
	
	public BootstrapPage() {
		final Ordering<JavaScriptSource> sourceOrdering = Ordering.natural();
		final Ordering<JavaScriptLink> linkOrdering = Ordering.natural();

		// HEAD
		add(new Label("pageTitle", "Welcome").setRenderBodyOnly(true));
		add(new Label("pageTitleSuffix", site.getPageTitleSuffix()).setRenderBodyOnly(true));
		
		// NAVBAR
		Navbar navbar = new Navbar("navbar");
		add(navbar);
//		add(new Label("logoText", site.getLogoText()).setRenderBodyOnly(true));
//		add(new Label("logoAlt", site.getLogoAlt()).setRenderBodyOnly(true));
		navbar.add(new BookmarkablePageLink<Page>("homeLink", getApplication().getHomePage()) {
			{
				this.setBody(new Model<String>(site.getLogoText()));
			}
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.getAttributes().put("title", site.getLogoAlt());
			}
		});
		
		try {
			Component navbarChild = navbarChildFactory.create("navbarChild");
			navbar.replace(navbarChild);
		} catch (Exception e) {
			log.debug("Ignoring navbarChildFactory due to error", e);
		}
		
		// HEADER
		add(new Header());
		
		// SIDEBAR
		
		log.info("Page {} has {} sidebar blocks", getClass().getName(), sidebarBlocks.size());
		add(new ListView<ComponentFactory<?>>("sidebarBlocks", sidebarBlocks) {
			@Override
			protected void populateItem(ListItem<ComponentFactory<?>> item) {
				item.setRenderBodyOnly(true);
				final ComponentFactory<?> compFactory = (ComponentFactory<?>) item.getModelObject();
				Component block = compFactory.create("block");
				item.add(block);
			}
		});
		
		// FOOTER
		
		add(new Footer(new Model<String>(site.getFooterText())));
		
		// JAVASCRIPT

		log.info("Page {} has {} before-footer-js blocks", getClass().getName(), beforeFooterJsBlocks.size());
		add(new ListView<ComponentFactory<?>>("beforeFooterJsBlocks", beforeFooterJsBlocks) {
			@Override
			protected void populateItem(ListItem<ComponentFactory<?>> item) {
				item.setRenderBodyOnly(true);
				final ComponentFactory<?> compFactory = (ComponentFactory<?>) item.getModelObject();
				Component block = compFactory.create("block");
				item.add(block);
			}
		});
		
		log.debug("Page {} has {} footer JavaScript links", getClass().getName(), footerJavaScripts.size());
		List<JavaScriptLink> sortedJses = linkOrdering.immutableSortedCopy(footerJavaScripts);
		add(new ListView<JavaScriptLink>("footerJavaScripts", sortedJses) {
			@Override
			protected void populateItem(ListItem<JavaScriptLink> item) {
				item.setRenderBodyOnly(true);
				final JavaScriptLink js = item.getModelObject();
				item.add(new Label("js") {
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.getAttributes().put("src", js.getSrc());
					};
				});
			}
		});
		
		log.debug("Page {} has {} footer JavaScript sources", getClass().getName(), footerJavaScriptSources.size());
		List<JavaScriptSource> sortedJsSources = sourceOrdering.immutableSortedCopy(footerJavaScriptSources);
		add(new ListView<JavaScriptSource>("footerJavaScriptSources", sortedJsSources) {
			@Override
			protected void populateItem(ListItem<JavaScriptSource> item) {
				item.setRenderBodyOnly(true);
				final JavaScriptSource js = item.getModelObject();
				item.add(new Label("js", js.getScript()).setEscapeModelStrings(false));
			}
		});
		
		IModel<List<JavaScriptLink>> pageJavaScriptLinksModel = new LoadableDetachableModel<List<JavaScriptLink>>() {
			protected List<JavaScriptLink> load() {
				log.debug("Page {} has {} page JavaScript links", getClass().getName(), pageJavaScriptLinks.size());
				List<JavaScriptLink> sortedPageJsLinks = linkOrdering.immutableSortedCopy(pageJavaScriptLinks);
				return sortedPageJsLinks;
			};
		};
		add(new ListView<JavaScriptLink>("pageJavaScripts", pageJavaScriptLinksModel) {
			@Override
			protected void populateItem(ListItem<JavaScriptLink> item) {
				item.setRenderBodyOnly(true);
				final JavaScriptLink js = item.getModelObject();
				item.add(new Label("js") {
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.getAttributes().put("src", js.getSrc());
					};
				});
			}
		});
		
		IModel<String> pageJavaScriptSourcesModel = new LoadableDetachableModel<String>() {
			protected String load() {
				log.debug("Page {} has {} page JavaScript sources", getClass().getName(), pageJavaScriptSources.size());
				String merged = Joiner.on('\n').join(pageJavaScriptSources);
				JavaScriptSource js = new AmdJavaScriptSource(merged, dependencies);
				return js.getScript();
			};
		};
		add(new Label("pageJavaScriptSources", pageJavaScriptSourcesModel).setEscapeModelStrings(false));
	}

}
