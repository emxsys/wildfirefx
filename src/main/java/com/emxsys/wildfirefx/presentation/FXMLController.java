/*
 * Copyright (c) 2015, Bruce Schubert <bruce@emxsys.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     - Neither the name of Bruce Schubert, Emxsys nor the names of its 
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.emxsys.wildfirefx.presentation;

import com.emxsys.wildfirefx.presentation.Controller;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Bruce Schubert
 * @param <M> The model type.
 * @param <V> The view type.
 */
public class FxmlController<M, V> implements Controller<M, V> {

    private SimpleObjectProperty<M> model = new SimpleObjectProperty<>();
    private V view;

    /**
     * Gets the model object.
     *
     * @return The model object. May be null.
     */
    @Override
    public M getModel() {
        return this.model.get();
    }

    /**
     * Sets the model managed and/or observed by this controller.
     *
     * @param model The model object.
     */
    protected void setModel(M model) {
        if (model == null) {
            throw new NullPointerException("model cannot be null.");
        }
        this.model.set(model);
    }

    protected V getView() {
        return this.view;
    }

    /**
     * Establishes the view that will be managed by this controller. Called by
     * FxmlView after the FMXL has been loaded.
     *
     * @param view The view object to be controlled.
     */
    public final void setView(V view) {
        if (view == null) {
            throw new NullPointerException("view cannot be null.");
        }
        if (this.view != null) {
            throw new IllegalStateException("View has already be set.");
        }
        this.view = view;

        // Provide an opportunity to initialize view dependent objects.
        postInitialize();
    }

    /**
     * Post-initialization routines that require an initialized view object.
     * Called after the view has been loaded.
     */
    protected void postInitialize() {
    }

    public static Node fitToParent(Node child) {
        AnchorPane.setTopAnchor(child, 0.0);
        AnchorPane.setBottomAnchor(child, 0.0);
        AnchorPane.setLeftAnchor(child, 0.0);
        AnchorPane.setRightAnchor(child, 0.0);
        return child;
    }
}
