package com.blinkcoder.render;

import com.jfinal.core.JFinal;
import com.jfinal.render.Render;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.view.ToolboxManager;
import org.apache.velocity.tools.view.context.ChainedContext;
import org.apache.velocity.tools.view.servlet.ServletToolboxManager;
import org.apache.velocity.tools.view.servlet.VelocityLayoutServlet;
import org.apache.velocity.util.SimplePool;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-10-15
 * Time: 上午8:18
 */
public class VelocityToolboxRender extends Render {

    private static final String TOOL_BOX_CONF_FILE = "/WEB-INF/classes/velocity-toolbox.xml";
    private transient static final String encoding = getEncoding();
    private transient static final String contentType = "text/html;charset=" + encoding;
    private transient static final Properties properties = new Properties();
    protected ToolboxManager toolboxManager = null;
    private static SimplePool writerPool = new SimplePool(40);

    private transient static boolean notInit = true;
    public static final String KEY_LAYOUT = "layout";
    public static final String KEY_SCREEN_CONTENT = "screen_content";
    private static final String layoutDir = "/WEB-INF/layout/";
    private static final String defaultLayout = "default.vm";


    @Override
    public void render() {
        Context context = createContext(request, response);

        fillContext(context, request);

        VelocityWriter vw = null;
        Template template = Velocity.getTemplate(view);
        response.setContentType(contentType);

        StringWriter sw = new StringWriter();
        template.merge(context, sw);

        context.put(KEY_SCREEN_CONTENT, sw.toString());

        Object obj = context.get(KEY_LAYOUT);
        String layout = (obj == null) ? null : obj.toString();
        if (layout == null) {
            layout = layoutDir + defaultLayout;
        } else {
            layout = layoutDir + layout;
        }

        try {
            template = Velocity.getTemplate(layout);
        } catch (Exception e) {
            if (!layout.equals(layoutDir + defaultLayout)) {
                template = Velocity.getTemplate(layoutDir + defaultLayout);
            }
        }


        try {
            Writer writer = response.getWriter();
            vw = (VelocityWriter) writerPool.get();
            if (vw == null) {
                vw = new VelocityWriter(writer, 4 * 1024, true);
            } else {
                vw.recycle(writer);
            }
            template.merge(context, vw);
        } catch (IOException e) {
        } finally {
            if (vw != null) {
                try {
                    vw.flush();
                    vw.recycle(null);
                    writerPool.put(vw);
                } catch (Exception e) {
//                    Velocity.debug("VelocityViewServlet: " + "Trouble releasing VelocityWriter: " + e.getMessage());
                }
            }
        }
    }

    ;

    static {
        String webPath = JFinal.me().getServletContext().getRealPath("/");
        Properties properties = new Properties();
        properties.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, webPath);
        properties.setProperty(RuntimeConstants.ENCODING_DEFAULT, encoding);
        properties.setProperty(RuntimeConstants.INPUT_ENCODING, encoding);
        properties.setProperty(RuntimeConstants.OUTPUT_ENCODING, encoding);
        properties.setProperty(RuntimeConstants.VM_LIBRARY, "/WEB-INF/macro/common.vm");
        properties.setProperty(VelocityLayoutServlet.PROPERTY_LAYOUT_DIR, layoutDir);
        properties.setProperty(VelocityLayoutServlet.PROPERTY_DEFAULT_LAYOUT, defaultLayout);
        Velocity.init(properties);
    }

    public VelocityToolboxRender(String url) {
        this.url = url;
        int index = url.indexOf("?");
        if (index != -1)
            this.view = url.substring(0, index);
        else
            this.view = url;
        initToolbox(JFinal.me().getServletContext());
    }


    @SuppressWarnings("unchecked")
    protected Context createContext(HttpServletRequest req, HttpServletResponse resp) {
        ChainedContext ctx = new ChainedContext(new VelocityEngine(), req, resp, JFinal.me().getServletContext());

        if (toolboxManager != null) {
            ctx.setToolbox(toolboxManager.getToolbox(ctx));
        }
        return ctx;
    }

    protected void fillContext(Context ctx, HttpServletRequest request) {
        String layout = request.getParameter(KEY_LAYOUT);
        // also look in the request attributes
        if (layout == null) {
            layout = (String) request.getAttribute(KEY_LAYOUT);
        }
        if (layout != null) {
            // let the template know what its new layout is
            ctx.put(KEY_LAYOUT, layout);
        }
    }

    protected void initToolbox(ServletContext servletContext) {
        toolboxManager = ServletToolboxManager.getInstance(servletContext, TOOL_BOX_CONF_FILE);
    }
}
