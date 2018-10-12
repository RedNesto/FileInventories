/*
 * MIT License
 *
 * Copyright (c) 2017 RedNesto
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
import io.github.rednesto.fileinventories.api.data.builder.OnInteractPrimaryClickDataManipulatorBuilder;
import io.github.rednesto.fileinventories.api.data.builder.OnInteractSecondaryClickDataManipulatorBuilder;
import io.github.rednesto.fileinventories.api.data.builder.OnInvPrimaryClickDataManipulatorBuilder;
import io.github.rednesto.fileinventories.api.data.builder.OnInvMiddleClickDataManipulatorBuilder;
import io.github.rednesto.fileinventories.api.data.builder.OnInvSecondaryClickDataManipulatorBuilder;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInteractPrimaryClickData;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInteractSecondaryClickData;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInvPrimaryClickData;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInvMiddleClickData;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInvSecondaryClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInteractPrimaryClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInteractSecondaryClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvPrimaryClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvMiddleClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvSecondaryClickData;
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
        description = "A library letting people customize items and inventories in files",
        authors = {
                "RedNesto"
        }
)
public class FileInventories {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    private static FileInventories instance;

    @Listener
    public void onConstruct(GameConstructionEvent event) {
        instance = this;
        Sponge.getServiceManager().setProvider(this, FileInventoriesService.class, new FileInventoriesServiceImpl());
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        DataRegistration.builder()
                .dataClass(OnInteractSecondaryClickData.class)
                .immutableClass(ImmutableOnInteractSecondaryClickData.class)
                .builder(new OnInteractSecondaryClickDataManipulatorBuilder())
                .manipulatorId("on_interact_secondary_click")
                .dataName("OnInteractSecondaryClick")
                .buildAndRegister(container);

        DataRegistration.builder()
                .dataClass(OnInteractPrimaryClickData.class)
                .immutableClass(ImmutableOnInteractPrimaryClickData.class)
                .builder(new OnInteractPrimaryClickDataManipulatorBuilder())
                .manipulatorId("on_interact_primary_click")
                .dataName("OnInteractPrimaryClick")
                .buildAndRegister(container);


        DataRegistration.builder()
                .dataClass(OnInvSecondaryClickData.class)
                .immutableClass(ImmutableOnInvSecondaryClickData.class)
                .builder(new OnInvSecondaryClickDataManipulatorBuilder())
                .manipulatorId("on_inv_secondary_click")
                .dataName("OnInvSecondaryClick")
                .buildAndRegister(container);

        DataRegistration.builder()
                .dataClass(OnInvPrimaryClickData.class)
                .immutableClass(ImmutableOnInvPrimaryClickData.class)
                .builder(new OnInvPrimaryClickDataManipulatorBuilder())
                .manipulatorId("on_inv_primary_click")
                .dataName("OnInvPrimaryClick")
                .buildAndRegister(container);

        DataRegistration.builder()
                .dataClass(OnInvMiddleClickData.class)
                .immutableClass(ImmutableOnInvMiddleClickData.class)
                .builder(new OnInvMiddleClickDataManipulatorBuilder())
                .manipulatorId("on_inv_middle_click")
                .dataName("OnInvMiddleClick")
                .buildAndRegister(container);
    }

    public static FileInventories getInstance() {
        return instance;
    }
}
