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

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * 
 * @author Bruce Schubert
 * @param <C> The controller type.
 */
public class FXMLView<C extends FXMLController> implements View<C> {

    private final C controller;
    private final Node root;


    /**
     * Constructs an FxmlView and FXMLController pair from an FXML file.
     * @param fxmlPath The FXML resource.
     */
    public FXMLView(String fxmlPath) {
        this(FXMLView.class.getResource(fxmlPath));
    }

    /**
     * Constructs an FxmlView and FXMLController pair from an FXML file.
     * @param fxmlUrl The FXML resource.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public FXMLView(URL fxmlUrl) {
        if (fxmlUrl == null) {            
            throw new IllegalArgumentException(getClass().getSimpleName() + ": url cannot be null");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        
        try {
            loader.load();
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
            throw new IllegalStateException(getClass().getSimpleName() + ": Cannot load " + fxmlUrl);
        }

        this.root = loader.getRoot();
        this.controller = loader.getController();
        this.controller.setView(this);
    }

    /**
     * @return The controller defined in the FXML.
     */
    @Override
    public C getController() {
        return this.controller;
    }

    /**
     * @return The root node defined in the FXML.
     */
    @Override
    public Node getRoot() {
        return this.root;
    }
}
