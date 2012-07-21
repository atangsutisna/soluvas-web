package org.soluvas.web.nav.ui;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.wicket.Component;
import org.apache.wicket.model.LoadableDetachableModel;
import org.soluvas.async.Callback;
import org.soluvas.web.nav.Menu;
import org.soluvas.web.nav.MenuRepository;
import org.soluvas.web.site.ComponentFactory;

public class NavBarFactory implements ComponentFactory {
	
	private MenuRepository menuRepository;
	
	public NavBarFactory(MenuRepository menuRepository) {
		super();
		this.menuRepository = menuRepository;
	}

	@Override
	public Component create(String id) {
		// TODO: synchronous repo
		final AtomicReference<Menu> menu = new AtomicReference<Menu>();
		menuRepository.findOne("main", new Callback<Menu>() {
			@Override
			public void success(Menu data) {
				menu.set(data);
			}
		});
		return new NavBar(id, new LoadableDetachableModel<Menu>() {
			@Override
			protected Menu load() {
				return menu.get();
			}
		});
	}

}
