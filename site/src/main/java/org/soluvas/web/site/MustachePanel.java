package org.soluvas.web.site;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;

/**
 * Renders a Mustache template, template is contained within the panel HTML markup.
 * The template is cached on first render.
 * @author ceefour
 */
@SuppressWarnings("serial")
public class MustachePanel extends Panel {

	private static final Logger log = LoggerFactory.getLogger(MustachePanel.class);
	private Mustache mainMustache;
	
	public MustachePanel(String id, IModel<?> model) {
		super(id, model);
	}
	
	@Override
	public void onComponentTagBody(MarkupStream markupStream,
			ComponentTag openTag) {
		// Compile the template only if not already compiled
		if (mainMustache == null) {
			log.debug("Compiling Mustache for {}", getPageRelativePath());
			String template = markupStream.get().toString();
			DefaultMustacheFactory mf = new DefaultMustacheFactory();
			mainMustache = mf.compile(new StringReader(template), "main");
		}
		
		final StringWriter writer = new StringWriter();
		mainMustache.execute(writer, getDefaultModelObject());
		final String body = writer.toString();
		replaceComponentTagBody(markupStream, openTag, body);
	}

}
