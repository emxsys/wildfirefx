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

public class BasicController<M, V> implements Controller<M, V> {

    private SimpleObjectProperty<M> model = new SimpleObjectProperty<> ();
    private V view;

    @Override
    public M getModel() {
        return this.model.get();
    }
    
    public final void setModel(M model) {
        if (model == null) {
            throw new NullPointerException("model cannot be null.");
        }
        this.model.set(model);
    }

    protected V getView() {
        return this.view;
    }

    /**
     * 
     * @param view 
     */
    public final void setView(V view) {
        if (view == null) {
            throw new NullPointerException("view cannot be null.");
        }
        if (this.view != null) {
            throw new IllegalStateException("View has already be set.");
        }
        this.view = view;
        postInitialize();
    }
    
    /**
     * Called after the view has been set.
     */
    protected void postInitialize() {
        
    }
}
