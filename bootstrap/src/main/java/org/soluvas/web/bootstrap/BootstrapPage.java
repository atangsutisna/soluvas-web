package org.soluvas.web.bootstrap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.ops4j.pax.wicket.api.PaxWicketBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soluvas.data.repository.CrudRepository;
import org.soluvas.web.site.AmdJavaScriptSource;
import org.soluvas.web.site.CssLink;
import org.soluvas.web.site.JavaScriptLink;
import org.soluvas.web.site.JavaScriptLinkImpl;
import org.soluvas.web.site.JavaScriptSource;
import org.soluvas.web.site.MultitenantPage;
import org.soluvas.web.site.PageMetaSupplier;
import org.soluvas.web.site.PageMetaSupplierFactory;
import org.soluvas.web.site.PageRuleContext;
import org.soluvas.web.site.Site;
import org.soluvas.web.site.TenantService;
import org.soluvas.web.site.client.AmdDependency;
import org.soluvas.web.site.client.JsSource;
import org.soluvas.web.site.compose.ComposeUtils;
import org.soluvas.web.site.compose.LiveContributor;
import org.soluvas.web.site.pagemeta.PageMeta;
import org.soluvas.web.site.webaddress.WebAddress;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Ordering;

/**
 * Base page for Twitter Bootstrap-powered Wicket pages.
 * @author ceefour
 */
@SuppressWarnings("serial")
public class BootstrapPage extends MultitenantPage {

	/**
	 * Usage:
	 * 
	 * <pre>{@code
	 * final Builder<String, String> dependencies = ImmutableMap.builder();
	 * new AmdDependencyVisitor(dependencies).component(BootstrapPage.this, null);
	 * visitChildren(new AmdDependencyVisitor(dependencies));
	 * final Map<String, String> dependencyMap = dependencies.build();
	 * log.debug("Page {} has {} AMD dependencies: {}", getClass().getName(), dependencyMap.size(),
	 * 		dependencyMap.keySet());
	 * }</pre>
	 * 
	 * @author ceefour
	 */
	public static final class AmdDependencyVisitor implements
			IVisitor<Component, Void> {
		private final Builder<String, String> dependencies;

		public AmdDependencyVisitor(Builder<String, String> dependencies) {
			this.dependencies = dependencies;
		}

		@Override
		public void component(Component component,
				IVisit<Void> visit) {
			List<AmdDependency> amdDeps = component.getBehaviors(AmdDependency.class);
			for (AmdDependency dep : amdDeps) {
				dependencies.put(dep.getPath(), dep.getName());
			}
		}
	}

	public static final class JsSourceVisitor implements
		IVisitor<Component, Void> {
		private final ImmutableList.Builder<String> jsSources;
		
		public JsSourceVisitor(ImmutableList.Builder<String> jsSources) {
			this.jsSources = jsSources;
		}
		
		@Override
		public void component(Component component,
				IVisit<Void> visit) {
			List<JsSource> jsSourceBehaviors = component.getBehaviors(JsSource.class);
			for (JsSource src : jsSourceBehaviors) {
				jsSources.add( src.getJsSource() );
			}
		}
	}
	
	private transient Logger log = LoggerFactory.getLogger(BootstrapPage.class);
	
	@PaxWicketBean(name="jacksonMapperFactory")
	private Supplier<ObjectMapper> jacksonMapperFactory;
	/**
	 * Should not use {@link Site} directly!
	 */
	@PaxWicketBean(name="site") @Deprecated
	private Site site;
	@PaxWicketBean(name="cssLinks")
	private List<CssLink> cssLinks;
	@PaxWicketBean(name="headJavaScripts")
	private List<JavaScriptLink> headJavaScripts;
	@PaxWicketBean(name="footerJavaScripts")
	private List<JavaScriptLink> footerJavaScripts;
	@PaxWicketBean(name="footerJavaScriptSources")
	private List<JavaScriptSource> footerJavaScriptSources;
	
	protected final RepeatingView sidebarBlocks;

//	@PaxWicketBean(name="pageRulesSupplier")
//	private Supplier<List<PageRule>> pageRulesSupplier;
	@PaxWicketBean(name="pageMetaSupplierFactory")
	private PageMetaSupplierFactory<PageMetaSupplier> pageMetaSupplierFactory;
	@TenantService(filter="(suppliedClass=org.soluvas.web.site.webaddress.WebAddress)")
	private transient Supplier<WebAddress> webAddressSupplier;
	@PaxWicketBean(name="contributors")
	private CrudRepository<LiveContributor, Integer> contributors;
	
	private final List<JavaScriptLink> pageJavaScriptLinks = new ArrayList<JavaScriptLink>();
	protected Component feedbackPanel;

	protected TransparentWebMarkupContainer contentColumn;

	protected TransparentWebMarkupContainer sidebarColumn;

	private WebAddress webAddress;

	public JavaScriptLink addJsLink(String uri) {
		JavaScriptLinkImpl js = new JavaScriptLinkImpl(uri, 100);
		pageJavaScriptLinks.add(js);
		return js;
	}
	
	/**
	 * @param dependencies
	 * @deprecated Replaced with {@link Component#add(org.apache.wicket.behavior.Behavior...)} with {@link AmdDependency}.
	 */
	@Deprecated
	public void addDependencies(Map<String, String> dependencies) {
		for (Entry<String, String> dep : dependencies.entrySet()) {
			add(new AmdDependency(dep.getKey(), dep.getValue()));
		}
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		log.debug("Page {} has {} CSS links", getClass().getName(), cssLinks.size());
		Ordering<CssLink> cssOrdering = Ordering.from(new Comparator<CssLink>() {
			@Override
			public int compare(CssLink o1, CssLink o2) {
				return o1.getWeight() - o2.getWeight();
			};
		});
		List<CssLink> sortedCsses = cssOrdering.immutableSortedCopy(cssLinks);
		for (CssLink css : sortedCsses) {
			response.renderCSSReference(webAddress.getSkinUri() + css.getPath());
		}
		
		log.debug("Page {} has {} head JavaScript links", getClass().getName(), headJavaScripts.size());
		Ordering<JavaScriptLink> jsOrdering = Ordering.from(new Comparator<JavaScriptLink>() {
			@Override
			public int compare(JavaScriptLink o1, JavaScriptLink o2) {
				return o1.getWeight() - o2.getWeight();
			};
		});
		List<JavaScriptLink> sortedJses = jsOrdering.immutableSortedCopy(headJavaScripts);
		for (JavaScriptLink js : sortedJses) {
			response.renderJavaScriptReference(js.getSrc());
		}
	}
	
	protected PageMeta getPageMeta() {
		PageRuleContext context = new PageRuleContext();
		context.setUri(getRequest().getUrl().toString());
//		List<PageRule> pageRules = pageRulesSupplier.get();
//		PageMetaSupplier pageSupplier = new RulesPageMetaSupplier(pageRules, context);
		PageMetaSupplier pageMetaSupplier = pageMetaSupplierFactory.create(context);
		PageMeta pageMeta = pageMetaSupplier.get();
		return pageMeta;
	}
	
	@Override
	protected void renderPlaceholderTag(ComponentTag tag, Response response) {
		super.renderPlaceholderTag(tag, response);
	}
	
	public BootstrapPage() {
		webAddress = webAddressSupplier.get();

		final Ordering<JavaScriptSource> sourceOrdering = Ordering.natural();
		final Ordering<JavaScriptLink> linkOrdering = Ordering.natural();
		PageMeta pageMeta = getPageMeta();
		
		// HTML
		add(new TransparentWebMarkupContainer("html").add(new AttributeModifier("lang", pageMeta.getLanguageCode())));
		
		// HEAD
		//add(new Label("pageTitle", "Welcome").setRenderBodyOnly(true));
		add(new Label("pageTitle", pageMeta.getTitle()).setRenderBodyOnly(true));
		add(new Label("pageTitleSuffix", site.getPageTitleSuffix()).setRenderBodyOnly(true));
		final WebMarkupContainer faviconLink = new WebMarkupContainer("faviconLink");
		faviconLink.add(new AttributeModifier("href", pageMeta.getIcon().getFaviconUri()));
		add(faviconLink);
		
		//Carousel
		add(new WebMarkupContainer("afterHeader"));
		
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
		
		add(new Header());
		final String requireConfigPath = webAddress.getApiPath() + "org.soluvas.web.backbone/requireConfig.js";
		add(new WebMarkupContainer("requireConfig").add(new AttributeModifier("src", requireConfigPath)));
		
		// SIDEBAR
		sidebarColumn = new TransparentWebMarkupContainer("sidebarColumn");
		add(sidebarColumn);
		sidebarBlocks = new RepeatingView("sidebarBlocks");
		sidebarColumn.add(sidebarBlocks);
		
		contentColumn = new TransparentWebMarkupContainer("contentColumn");
		add(contentColumn);
		feedbackPanel = new FeedbackPanel("feedback").setOutputMarkupId(true);
		add(feedbackPanel);
		
		// FOOTER
		
		add(new Footer(site.getFooterHtml()));
		
		// JAVASCRIPT

		final RepeatingView beforeFooterJs = new RepeatingView("beforeFooterJs");
		add(beforeFooterJs);
		
		log.debug("Page {} has {} footer JavaScript links", getClass().getName(), footerJavaScripts.size());
		final List<JavaScriptLink> sortedJsLinks = linkOrdering.immutableSortedCopy(footerJavaScripts);
		final RepeatingView footerJavaScriptLinks = new RepeatingView("footerJavaScriptLinks");
		for (JavaScriptLink js : sortedJsLinks) {
			footerJavaScriptLinks.add(new WebMarkupContainer(footerJavaScriptLinks.newChildId()).add(new AttributeModifier("src", js.getSrc())));
		}
		add(footerJavaScriptLinks);
		
		log.debug("Page {} has {} footer JavaScript sources", getClass().getName(), footerJavaScriptSources.size());
		final List<JavaScriptSource> sortedJsSources = sourceOrdering.immutableSortedCopy(footerJavaScriptSources);
		final RepeatingView footerJavaScriptSources = new RepeatingView("footerJavaScriptSources");
		for (JavaScriptSource js : sortedJsSources) {
			footerJavaScriptSources.add(new Label(footerJavaScriptSources.newChildId(), js.getScript()).setEscapeModelStrings(false));
		}
		add(footerJavaScriptSources);
		
		log.debug("Page {} has {} page JavaScript links", getClass().getName(), pageJavaScriptLinks.size());
		final List<JavaScriptLink> sortedPageJsLinks = linkOrdering.immutableSortedCopy(pageJavaScriptLinks);
		final RepeatingView pageJavaScriptLinksView = new RepeatingView("pageJavaScriptLinks");
		for (JavaScriptLink js : sortedPageJsLinks) {
			pageJavaScriptLinksView.add(new WebMarkupContainer(pageJavaScriptLinksView.newChildId()).add(new AttributeModifier("src", js.getSrc())));
		}
		add(pageJavaScriptLinksView);
		
		IModel<String> pageJavaScriptSourcesModel = new LoadableDetachableModel<String>() {
			@Override
			protected String load() {
				final ImmutableList.Builder<String> pageJsSourcesBuilder = ImmutableList.builder();
				final JsSourceVisitor jsSourceVisitor = new JsSourceVisitor(pageJsSourcesBuilder);
				jsSourceVisitor.component(BootstrapPage.this, null);
				visitChildren(jsSourceVisitor);
//				log.debug("Page {} has {} page JavaScript sources", getClass().getName(), pageJavaScriptSources.size());
//				String merged = Joiner.on('\n').join(pageJavaScriptSources);
				List<String> pageJsSources = pageJsSourcesBuilder.build();
				log.debug("Page {} has {} page JavaScript sources", getClass().getName(), pageJsSources.size());
				String merged = Joiner.on('\n').join(pageJsSources);
				
				final Builder<String, String> dependencies = ImmutableMap.builder();
				final AmdDependencyVisitor amdDependencyVisitor = new AmdDependencyVisitor(dependencies);
				amdDependencyVisitor.component(BootstrapPage.this, null);
				visitChildren(amdDependencyVisitor);
				final Map<String, String> dependencyMap = dependencies.build();
				log.debug("Page {} has {} AMD dependencies: {}", getClass().getName(), dependencyMap.size(),
						dependencyMap.keySet());

				JavaScriptSource js = new AmdJavaScriptSource(merged, dependencyMap);
				return js.getScript();
			};
		};
		add(new Label("pageJavaScriptSources", pageJavaScriptSourcesModel).setEscapeModelStrings(false));
	}
	
	public BootstrapPage(boolean sidebarVisible) {
		this();
		if (!sidebarVisible) {
			sidebarColumn.setVisible(false);
			contentColumn.add(new AttributeModifier("class", "span12"));
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		// compose other components
		ComposeUtils.compose(this, contributors.findAll());
	}
	
}
