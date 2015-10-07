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
package com.emxsys.wildfirefx.presentation.simulation;

import com.emxsys.wildfirefx.presentation.FXMLView;
import com.emxsys.wildfirefx.presentation.simulation.SimController;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Bruce Schubert
 */
public class SimView extends FXMLView<SimController> {

    public SimView() {
        super("/fxml/Simulation.fxml");

        // The view's root is an AnchorPane containing a canvas;
        // autofit the canvas to the parent.
        AnchorPane anchorPane = (AnchorPane) getRoot();
        for (Node child : anchorPane.getChildren()) {
            if ("canvas".equals(child.getId())) {
                Canvas canvas = (Canvas) child;
                canvas.widthProperty().bind(anchorPane.widthProperty());
                canvas.heightProperty().bind(anchorPane.heightProperty());
                break;
            }
        }
    }

}
