package com.blinkcoder.render;

import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-10-24
 * Time: 上午10:51
 */
public class VelocityToolboxRenderFactory implements IMainRenderFactory {

    private String root = "";

    public VelocityToolboxRenderFactory(String root) {
        this.root = root;
    }

    public VelocityToolboxRenderFactory() {
    }

    public Render getRender(String view) {
        return new VelocityToolboxRender(view);
    }

    public String getViewExtension() {
        return ".html";
    }
}
