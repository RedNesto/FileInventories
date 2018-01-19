/*
 * MIT License
 *
 * Copyright (c) 2017
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.rednesto.fileinventories;

import com.google.inject.Inject;
import io.github.rednesto.fileinventories.api.FileInventoriesService;
import io.github.rednesto.fileinventories.api.data.builder.OnInteractLeftClickDataManipulatorBuilder;
import io.github.rednesto.fileinventories.api.data.builder.OnInteractRightClickDataManipulatorBuilder;
import io.github.rednesto.fileinventories.api.data.builder.OnInvLeftClickDataManipulatorBuilder;
import io.github.rednesto.fileinventories.api.data.builder.OnInvRightClickDataManipulatorBuilder;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInteractLeftClickData;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInteractRightClickData;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInvLeftClickData;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInvRightClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInteractLeftClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInteractRightClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvLeftClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvRightClickData;
import io.github.rednesto.fileinventories.impl.FileInventoriesServiceImpl;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
        id = "file-inventories",
        name = "FileInventories",
        version = "0.2.2",
        description = "A library letting people design inventories in files",
        authors = {
                "RedNesto"
        }
)
public class FileInventories {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    public static FileInventories instance;

    @Listener
    public void onConstruct(GameConstructionEvent event) {
        instance = this;
        Sponge.getServiceManager().setProvider(this, FileInventoriesService.class, new FileInventoriesServiceImpl());
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        DataRegistration.builder()
                .dataClass(OnInteractRightClickData.class)
                .immutableClass(ImmutableOnInteractRightClickData.class)
                .builder(new OnInteractRightClickDataManipulatorBuilder())
                .manipulatorId("on_interact_right_click")
                .dataName("OnInteractRightClick")
                .buildAndRegister(container);

        DataRegistration.builder()
                .dataClass(OnInteractLeftClickData.class)
                .immutableClass(ImmutableOnInteractLeftClickData.class)
                .builder(new OnInteractLeftClickDataManipulatorBuilder())
                .manipulatorId("on_interact_left_click")
                .dataName("OnInteractLeftClick")
                .buildAndRegister(container);

        DataRegistration.builder()
                .dataClass(OnInvRightClickData.class)
                .immutableClass(ImmutableOnInvRightClickData.class)
                .builder(new OnInvRightClickDataManipulatorBuilder())
                .manipulatorId("on_inv_right_click")
                .dataName("OnInvRightClick")
                .buildAndRegister(container);

        DataRegistration.builder()
                .dataClass(OnInvLeftClickData.class)
                .immutableClass(ImmutableOnInvLeftClickData.class)
                .builder(new OnInvLeftClickDataManipulatorBuilder())
                .manipulatorId("on_inv_left_click")
                .dataName("OnInvLeftClick")
                .buildAndRegister(container);
    }

    public Logger getLogger() {
        return logger;
    }
}
