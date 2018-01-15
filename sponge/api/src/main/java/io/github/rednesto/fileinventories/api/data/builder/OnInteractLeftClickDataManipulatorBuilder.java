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
package io.github.rednesto.fileinventories.api.data.builder;

import io.github.rednesto.fileinventories.api.FileInvKeys;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInteractLeftClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInteractLeftClickData;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class OnInteractLeftClickDataManipulatorBuilder extends AbstractDataBuilder<OnInteractLeftClickData> implements DataManipulatorBuilder<OnInteractLeftClickData, ImmutableOnInteractLeftClickData> {

    public OnInteractLeftClickDataManipulatorBuilder() {
        super(OnInteractLeftClickData.class, 0);
    }

    @Override
    public OnInteractLeftClickData create() {
        return new OnInteractLeftClickData();
    }

    @Override
    public Optional<OnInteractLeftClickData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<OnInteractLeftClickData> buildContent(DataView container) throws InvalidDataException {
        if(container.contains(FileInvKeys.ON_INTERACT_LEFT_CLICK.getQuery()))
            return Optional.of(new OnInteractLeftClickData(container.getString(FileInvKeys.ON_INTERACT_LEFT_CLICK.getQuery()).orElse("")));

        return Optional.empty();
    }
}
