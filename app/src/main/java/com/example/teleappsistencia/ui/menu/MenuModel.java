package com.example.teleappsistencia.ui.menu;

import androidx.fragment.app.Fragment;

/**
 * Clase modelo para crear las opciones del menú principal (MainActivity).
 */
public class MenuModel {

    /**
     * Nombre de la opción del menú.
     */
    private String menuName;

    /**
     * Atributo para saber si tiene o no hijos (sub-opciones).
     */
    private boolean hasChildren;

    /**
     * Atributo para saber si la opción es la primera de un grupo de opciones.
     */
    private boolean isGroup;

    /**
     * Atributo con el fragment que va a cargar la opción cuando se pulse.
     */
    private Fragment fragment;

    /**
     * Constructor parametrizado.
     * @param menuName
     * @param isGroup
     * @param hasChildren
     * @param fragment
     */
    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, Fragment fragment) {
        this.menuName = menuName;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.fragment = fragment;
    }


    /**
    * Getters y Setters
    */

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public boolean hasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean group) {
        isGroup = group;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
