package org.soluvas.web.nav.ui;

import java.util.List;
import java.util.Map.Entry;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.ops4j.pax.wicket.api.PaxWicketBean;
import org.osgi.service.blueprint.container.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soluvas.web.nav.Menu;
import org.soluvas.web.nav.MenuItem;
import org.soluvas.web.nav.PageMenuItem;
import org.soluvas.web.nav.ProcessMenuItem;

/**
 * @author ceefour
 *
 */
@SuppressWarnings("serial")
public class NavBar extends Panel {

	private transient Logger log = LoggerFactory.getLogger(NavBar.class);
	@PaxWicketBean(name="processLinkFactory")
	private ProcessLinkFactory processLinkFactory; 
	
	public NavBar(String id, final IModel<Menu> model) {
		super(id, model);
		setRenderBodyOnly(true);
		LoadableDetachableModel<List<MenuItem>> menuItemsModel = new LoadableDetachableModel<List<MenuItem>>() {
			@Override
			protected List<MenuItem> load() {
				return model.getObject().getItems();
			}
		};
		add( new ListView<MenuItem>("nav", menuItemsModel) {
			@Override
			protected void populateItem(ListItem<MenuItem> listItem) {
				final MenuItem menuItem = listItem.getModelObject();
				listItem.add(new AttributeAppender("id", "navbar-item-" + menuItem.getId()));
				Label iconComponent = new Label("icon") {
					protected void onComponentTag(org.apache.wicket.markup.ComponentTag tag) {
						super.onComponentTag(tag);
						if (menuItem.getIconName() != null)
							tag.getAttributes().put("class", "icon-" + menuItem.getIconName() + " icon-white");
						else
							setVisible(false);
					};
				};
				final Label label = new Label("label", menuItem.getLabel());
				if (menuItem instanceof PageMenuItem) {
					final PageMenuItem pageMi = (PageMenuItem) menuItem;
					Class<Page> pageClass;
					try {
						log.debug("Loading Page class {} from bundle {} for menu item {}", new Object[] {
								pageMi.getPageClass(), menuItem.getBundle(), pageMi.getId() });
						pageClass = menuItem.getBundle().loadClass(pageMi.getPageClass());
						PageParameters pagePars = new PageParameters();
						for (Entry<String, String> param : pageMi.getParameters()) {
							pagePars.add(param.getKey(), param.getValue());
						}
						BookmarkablePageLink link = new BookmarkablePageLink<Page>("link", pageClass, pagePars);
						link.add(iconComponent);
						link.add(label);
						listItem.add(link);
					} catch (ClassNotFoundException e) {
						log.error("Cannot load Page class " + pageMi.getPageClass() +
								" from bundle " + menuItem.getBundle() + " for menu item " + pageMi.getId(), e);
					}
				} else if (menuItem instanceof ProcessMenuItem) {
					final ProcessMenuItem processMi = (ProcessMenuItem) menuItem;
					try {
						Link<? extends Page> link = processLinkFactory.create("link", processMi);
						link.add(iconComponent);
						link.add(label);
						listItem.add(link);
					} catch (ServiceUnavailableException e) {
						log.error("Cannot create process page link for menu item " + menuItem.getId() +
								", process " + processMi.getProcessId(), e);
					}
				} else {
					log.warn("Unknown MenuItem subclass: {}", menuItem.eClass());
					WebMarkupContainer link = new WebMarkupContainer("link");
					link.add(iconComponent);
					link.add(label);
					listItem.add(link);
				}
			}
		} );
	}

}