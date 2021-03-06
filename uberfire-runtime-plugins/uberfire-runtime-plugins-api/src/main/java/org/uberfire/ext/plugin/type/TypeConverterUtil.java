package org.uberfire.ext.plugin.type;

import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.plugin.model.PluginType;
import org.uberfire.workbench.type.ResourceTypeDefinition;

public final class TypeConverterUtil {

    private static final ScreenPluginResourceTypeDefinition screenDefinition = new ScreenPluginResourceTypeDefinition();
    private static final EditorPluginResourceTypeDefinition editorDefinition = new EditorPluginResourceTypeDefinition();
    private static final SplashPluginResourceTypeDefinition splashDefinition = new SplashPluginResourceTypeDefinition();
    private static final DynamicMenuResourceTypeDefinition dynamicMenuDefinition = new DynamicMenuResourceTypeDefinition();
    private static final PerspectiveLayoutPluginResourceTypeDefinition perspectiveLayoutPluginResourceTypeDefinition = new PerspectiveLayoutPluginResourceTypeDefinition();

    public static PluginType fromPath( final Path path ) {
        if ( screenDefinition.accept( path ) ) {
            return PluginType.SCREEN;
        }
        if ( editorDefinition.accept( path ) ) {
            return PluginType.EDITOR;
        }
        if ( splashDefinition.accept( path ) ) {
            return PluginType.SPLASH;
        }
        if ( dynamicMenuDefinition.accept( path ) ) {
            return PluginType.DYNAMIC_MENU;
        }

        if ( dynamicMenuDefinition.accept( path ) ) {
            return PluginType.DYNAMIC_MENU;
        }
        if ( perspectiveLayoutPluginResourceTypeDefinition.accept( path ) ) {
            return PluginType.PERSPECTIVE_LAYOUT;
        }

        return null;
    }

    public static PluginType fromResourceType( final ResourceTypeDefinition resource ) {
        if ( resource instanceof PerspectiveLayoutPluginResourceTypeDefinition ) {
            return PluginType.PERSPECTIVE_LAYOUT;
        }
        if ( resource instanceof ScreenPluginResourceTypeDefinition ) {
            return PluginType.SCREEN;
        }
        if ( resource instanceof EditorPluginResourceTypeDefinition ) {
            return PluginType.EDITOR;
        }
        if ( resource instanceof SplashPluginResourceTypeDefinition ) {
            return PluginType.SPLASH;
        }
        if ( resource instanceof DynamicMenuResourceTypeDefinition ) {
            return PluginType.DYNAMIC_MENU;
        }
        return null;
    }
}
