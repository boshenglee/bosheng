package com.example.letsgro;

import java.util.ArrayList;

/**
 * This is am interface class, IView
 *
 * @author boshenglee
 * @version 1.0
 */
public interface IView {

    /**
     * Set data function to setData of a listview or gridview
     * @param itemList the array list of item
     */
    public void setData(ArrayList<String> itemList);
}
